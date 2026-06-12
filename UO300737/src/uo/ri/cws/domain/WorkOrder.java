package uo.ri.cws.domain;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import uo.ri.cws.domain.base.BaseEntity;
import uo.ri.util.assertion.ArgumentChecks;

@Entity
@Table(name = "TWORKORDERS", uniqueConstraints = {
		@UniqueConstraint(columnNames = {
				"DATE", "VEHICLE_ID"
		})//Names of columns & tables not attributes(capital letters:convention)
})
public class WorkOrder extends BaseEntity {

	public enum WorkOrderState {
		OPEN,
		ASSIGNED,
		FINISHED,
		INVOICED
	}

	// natural attributes
	@Basic(optional=false) private LocalDateTime date;
	@Basic(optional=false) private String description;
	private double amount = 0.0;
	
	//If you swicth to this version decoment this
	//And put in the tables Table(new name) (a T first)
	//@Enumerated(EnumeratedType = EnumeratdType.STRING)
    @Enumerated(EnumType.STRING) @Column(name = "STATE")
	private WorkOrderState state = WorkOrderState.OPEN;

	// accidental attributes
	@ManyToOne private Vehicle vehicle;
	@ManyToOne private Mechanic mechanic;
	@ManyToOne private Invoice invoice;
	@OneToMany(mappedBy="workOrder")
	private Set<Intervention> interventions = new HashSet<>();

	WorkOrder(){}
	
	public WorkOrder(Vehicle vehicle, String description) {
		this(LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS),vehicle,description);
	}

	public WorkOrder(LocalDateTime date,Vehicle vehicle, String description) {
		ArgumentChecks.isNotNull(date, "date can not be null");
		ArgumentChecks.isNotNull(description, "description can not be null");
		ArgumentChecks.isNotNull(vehicle, "surname can not be null");
		this.date = date.truncatedTo(ChronoUnit.MILLIS);//Date affects hash code
		this.description = description;
		Associations.Fixes.link(vehicle, this);
		//Associations.fixes.link automatically do the link in both sides
		//this.vehicle = vehicle; just link one side (this one)
	}


	public WorkOrder(Vehicle vehicle) {
		this(LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS), vehicle, "");
	}

	public WorkOrder(Vehicle vehicle, LocalDateTime now) {
		this(now.truncatedTo(ChronoUnit.MILLIS), vehicle, "");
	}

	public WorkOrder(Vehicle vehicle, LocalDateTime now, String description) {
		this(now, vehicle, description);
	}

	public void markAsInvoiced() {
	   if (state != WorkOrderState.FINISHED || invoice == null) {
	        throw new IllegalStateException("Work order must be finished and "
	        		+ "linked to an invoice to be marked as invoiced");
	    }
	    state = WorkOrderState.INVOICED;
	}

	public void markAsFinished() {
	    if (state != WorkOrderState.ASSIGNED) {
	        throw new IllegalStateException();
	    }

	    state = WorkOrderState.FINISHED;
	    //FIX: Solo recalcular si hay algo que sumar
	    if (!interventions.isEmpty()) {
	        double total = 0.0;
	        for (Intervention i : interventions) {
	            total += i.getAmount();
	        }
	        amount = total;
	    }
	    Associations.Assigns.unlink(mechanic, this);
	}	

	public void markBackToFinished() {
	    if (state != WorkOrderState.INVOICED) {
	        throw new IllegalStateException();
	    }
	    state = WorkOrderState.FINISHED;
	}

	public void assignTo(Mechanic mechanic) {
		ArgumentChecks.isNotNull(mechanic, "Cannot assign a work order "
	    		+ "without a mechanic");
	    if (state != WorkOrderState.OPEN) {
	        throw new IllegalStateException();
	    }
	    Associations.Assigns.link(mechanic, this);
	    state = WorkOrderState.ASSIGNED;
	}

	public void unassign() {
	    if (state != WorkOrderState.ASSIGNED && state != WorkOrderState.INVOICED) {
	        throw new IllegalStateException("WorkOrder must be ASSIGNED or "
	        		+ "INVOICED to be unassigned");
	    }
		if (mechanic == null) {
		    throw new IllegalStateException("Cannot unassign a work order "
		    		+ "without a mechanic");
		}
	    Associations.Assigns.unlink(mechanic, this);
	    mechanic = null;
	    if (state == WorkOrderState.ASSIGNED) {
	        state = WorkOrderState.OPEN;
	    }
	}

	public void reopen() {
	    if (this.state == WorkOrderState.INVOICED) {
	        throw new IllegalStateException("Cannot reopen an invoiced work order");
	    }
	    if (this.state != WorkOrderState.FINISHED) {
	        throw new IllegalStateException("Only finished work orders can "
	        		+ "be reopened");
	    }
	    state = WorkOrderState.OPEN;
	}


	public WorkOrderState getState() {
		return state;
	}
	
	public Set<Intervention> getInterventions() {
		return new HashSet<>( interventions );
	}

	Set<Intervention> _getInterventions() {
		return interventions;
	}


	public Vehicle getVehicle() {
		return vehicle;
	}
	
	void _setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	public Mechanic getMechanic() {
		return mechanic;
	}
	
	void _setMechanic(Mechanic mechanic) {
		this.mechanic = mechanic;
	}

	public Invoice getInvoice() {
		return invoice;
	}
	
	void _setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

	@Override
	public String toString() {
	    return "WorkOrder[date=" + date + ", description=" + description + 
	           ", amount=" + amount + ", state=" + state + "]";
	}

	public double getAmount() {
		return amount;
	}

	public boolean isFinished() {
		return state ==  WorkOrderState.FINISHED;
	}

	public boolean isAssigned() {
		return state == WorkOrderState.ASSIGNED;
	}

	public boolean isOpen() {
		return state == WorkOrderState.OPEN;
	}
	
	public boolean isInvoiced() {
		return state == WorkOrderState.INVOICED;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public String getDescription() {
		return description;
	}


}
