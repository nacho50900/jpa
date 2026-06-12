package uo.ri.cws.domain;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import uo.ri.cws.domain.base.BaseEntity;
import uo.ri.util.assertion.ArgumentChecks;

@Entity
@Table(name = "TINTERVENTIONS", uniqueConstraints = {
		@UniqueConstraint(columnNames = {
				"DATE", "WORKORDER_ID", "MECHANIC_ID"
		})
})
public class Intervention extends BaseEntity {
	
	private static final double PRICE_PER_HOUR = 50.0;
	
	// natural attributes
	@Basic(optional=false) @Column(name = "DATE") private LocalDateTime date;
	private int minutes;

	// accidental attributes
	
	@ManyToOne private WorkOrder workOrder;
	@ManyToOne private Mechanic mechanic;
	@OneToMany (mappedBy="intervention")
	private Set<Substitution> substitutions = new HashSet<>();
	//mappedBy name of the attribute in the other class

	Intervention(){}
	
	public Intervention(Mechanic mechanic, WorkOrder workOrder,
			LocalDateTime date, int minutes) {
		
		ArgumentChecks.isNotNull(mechanic, "mechanic can not be null");
	    ArgumentChecks.isNotNull(workOrder, "workOrder can not be null");
	    ArgumentChecks.isNotNull(date, "date can not be null");
	    ArgumentChecks.isTrue(minutes >= 0, "minutes must be greater than zero");
		this.date = date;
		this.minutes = minutes;
		this.workOrder = workOrder;
		this.mechanic = mechanic;
		Associations.Intervenes.link(workOrder, this, mechanic);
	}
	
	public Intervention(Mechanic mechanic, WorkOrder workOrder, int minutes) {
		this(mechanic, workOrder, LocalDateTime.now().withNano(0), minutes);
	}

	void _setWorkOrder(WorkOrder workOrder) {
		this.workOrder = workOrder;
	}

	void _setMechanic(Mechanic mechanic) {
		this.mechanic = mechanic;
	}

	public Set<Substitution> getSubstitutions() {
		return new HashSet<>( substitutions );
	}

	Set<Substitution> _getSubstitutions() {
		return substitutions;
	}

	public WorkOrder getWorkOrder() {
		return workOrder;
	}

	public Mechanic getMechanic() {
		return mechanic;
	}

	public double getAmount() {
	    double amount = (minutes / 60.0) * PRICE_PER_HOUR;
	    for (Substitution s : substitutions) {
	        amount += s.getAmount();
	    }
	    return amount;
	}
	public LocalDateTime getDate() {
		return date;
	}

	public int getMinutes() {
		return minutes;
	}
	
	@Override
	public String toString() {
	    return "Intervention[date=" + date + ", minutes=" + minutes + "]";
	}
	
}
