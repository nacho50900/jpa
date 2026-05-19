package uo.ri.cws.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import uo.ri.cws.domain.base.BaseEntity;
import uo.ri.util.assertion.ArgumentChecks;

@Entity
@Table(name = "TINVOICES")
public class Invoice extends BaseEntity {
	public enum InvoiceState { NOT_YET_PAID, PAID }

	// natural attributes
	@Column(unique = true, nullable = false) private Long number;
	@Basic(optional=false) private LocalDate date;
	private double amount;
	private double vat;
	@Enumerated(EnumType.STRING) @Column(nullable = false)
	private InvoiceState state = InvoiceState.NOT_YET_PAID;

	// accidental attributes
	@OneToMany (mappedBy="invoice")
	private Set<WorkOrder> workOrders = new HashSet<>();
	@OneToMany (mappedBy="invoice")
	private Set<Charge> charges = new HashSet<>();

	Invoice(){}
	
	public Invoice(Long number) {
		this(number, LocalDate.now(), new ArrayList<>());
	}

	public Invoice(Long number, LocalDate date) {
		this(number, date, new ArrayList<WorkOrder>());
	}

	public Invoice(Long number, List<WorkOrder> workOrders) {
		this(number, LocalDate.now(), workOrders);
	}

	public Invoice(Long number, LocalDate date, List<WorkOrder> workOrders) {
		// check arguments (always), through IllegalArgumentException
		ArgumentChecks.isNotNull(number, "number can not be null");
		ArgumentChecks.isTrue(number >= 0, "number can not be negative");
		ArgumentChecks.isNotNull(date, "date can not be null");
		ArgumentChecks.isNotNull(workOrders, "workOrders can not be null");
		this.number = number;
		this.date = date;
        if (!workOrders.isEmpty()) {
			for(WorkOrder w :workOrders) {
				addWorkOrder(w);
			}
        }
		computeAmount();
	}

	private void computeAmount() {
	    amount = 0.0;
	    for (WorkOrder w : workOrders) {
	        amount += w.getAmount();
	    }
	    double vatPercentage = date.isBefore(LocalDate.of(2012, 7, 1)) ? 0.18:0.21;
	    vat = amount * vatPercentage;
	}
	

	public void addWorkOrder(WorkOrder workOrder) {
		ArgumentChecks.isNotNull(workOrder, "workOrder can not be null");
        if (this.state != InvoiceState.NOT_YET_PAID) {
            throw new IllegalStateException(
            		"Invoice must be NOT_YET_PAID to add work orders");
        }
        if (workOrders.contains(workOrder)) {
            return;
        }
		workOrders.add(workOrder);
		Associations.Bills.link(this, workOrder);
		computeAmount();
		workOrder.markAsInvoiced();
	}

	public void removeWorkOrder(WorkOrder workOrder) {
	    if (!isNotSettled()) {
	        throw new IllegalStateException(
	        		"Cannot remove work order from a settled invoice");
	    }
		ArgumentChecks.isNotNull(workOrder, "workOrder can not be null");
        if (this.state != InvoiceState.NOT_YET_PAID) {
            throw new IllegalStateException(
            		"Invoice must be NOT_YET_PAID to remove work orders");
        }
        if (!workOrders.contains(workOrder)) {
            throw new IllegalArgumentException(
            		"Invoice does not contain the given work order");
        }
		Associations.Bills.unlink(this, workOrder);
		workOrder.markBackToFinished();
		workOrders.remove(workOrder);
		computeAmount();
	}

	public void settle() {
	    double totalCharges = 0.0;
	    for (Charge c : charges) {
	        totalCharges += c.getAmount();
	    }
		if (state == InvoiceState.PAID || totalCharges < getAmount()) {
		    throw new IllegalStateException(
		    		"Invoice cannot be settled: insufficient charges");
		}
	    state = InvoiceState.PAID;
	}

    public Long getNumber() {
        return number;
    }

    public LocalDate getDate() {
        return date;
    }

    public double getAmount() {
        return amount + vat;
    }

    /** Porcentaje de IVA aplicado (18.0 o 21.0) */
    public double getVat() {
        return vat;
    }

    public InvoiceState getState() {
        return state;
    }

    public Set<WorkOrder> getWorkOrders() {
        return new HashSet<>(workOrders);
    }

    Set<WorkOrder> _getWorkOrders() {
        return workOrders;
    }

    public Set<Charge> getCharges() {
        return new HashSet<>(charges);
    }

    public Set<Charge> _getCharges() {
        return charges;
    }

	public boolean isNotSettled() {
		return (state == InvoiceState.NOT_YET_PAID);
	}
	
	public void markAsPaid() {
	    state = InvoiceState.PAID;
	}

	public void markAsNotPaid() {
	    if (!charges.isEmpty()) {
	        throw new IllegalStateException(
	        		"Cannot mark as not paid: invoice has charges");
	    }
	    state = InvoiceState.NOT_YET_PAID;
	}

	public double getAmountWithoutVat() {
	    return amount;
	}

	public boolean isFullyCharged() {
	    double totalCharges = 0.0;
	    for (Charge c : charges) {
	        totalCharges += c.getAmount();
	    }
	    return totalCharges >= getAmount();
	}

	public double getRemainingAmount() {
	    double totalCharges = 0.0;
	    for (Charge c : charges) {
	        totalCharges += c.getAmount();
	    }
	    return Math.max(0, getAmount() - totalCharges);
	}

	@Override
	public String toString() {
	    return "Invoice[number=" + number + ", date=" + date + 
	           ", amount=" + amount + ", vat=" + vat + ", state=" + state + "]";
	}
	
}
