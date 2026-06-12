package uo.ri.cws.domain;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import uo.ri.cws.domain.base.BaseEntity;

@Entity
@Table(name = "TPAYMENTMEANS")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class PaymentMean extends BaseEntity {
	
	// natural attributes
	private double accumulated = 0.0;

	// accidental attributes
	@ManyToOne private Client client;
	
	public Client getClient() {
		return client;
	}

	@OneToMany (mappedBy = "paymentMean")
	private Set<Charge> charges = new HashSet<>();

	public abstract boolean canPay(Double amount);

	public void pay(double importe) {
		if (!canPay(importe)) {
            throw new IllegalStateException("Not enough credit");
        }
		this.accumulated += importe;
	}

	void _setClient(Client client) {
		this.client = client;
	}

	public Set<Charge> getCharges() {
		return new HashSet<>( charges );
	}

	public Set<Charge> _getCharges() {
		return charges;
	}

	public Double getAccumulated() {
		return accumulated;
	}

}
