package uo.ri.cws.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import uo.ri.cws.domain.base.BaseEntity;
import uo.ri.util.assertion.ArgumentChecks;

@Entity
@Table(name = "TSUBSTITUTIONS", uniqueConstraints = {
		@UniqueConstraint(columnNames = {
				"SPAREPART_ID", "INTERVENTION_ID"
		})
})
public class Substitution extends BaseEntity {
	
	// natural attributes
	private int quantity;

	// accidental attributes
	@ManyToOne private SparePart sparePart;
	@ManyToOne private Intervention intervention;
	
	Substitution(){}
	
	public Substitution(SparePart sparePart,Intervention intervention,int quantity) {
		ArgumentChecks.isNotNull(sparePart, "sparePart can not be null");
		ArgumentChecks.isNotNull(intervention, "Intervention can not be null");
		ArgumentChecks.isTrue(quantity > 0, "quantity can not be negative");
		this.quantity = quantity;
		Associations.Substitutes.link(sparePart, this, intervention);
	}

	void _setSparePart(SparePart sparePart) {
		this.sparePart = sparePart;
	}

	void _setIntervention(Intervention intervention) {
		this.intervention = intervention;
	}


	public int getQuantity() {
		return quantity;
	}


	public SparePart getSparePart() {
		return sparePart;
	}


	public Intervention getIntervention() {
		return intervention;
	}

	public Double getAmount() {
		return sparePart.getPrice() * quantity;
	}

	@Override
	public String toString() {
	    return "Substitution[quantity=" + quantity + "]";
	}
}
