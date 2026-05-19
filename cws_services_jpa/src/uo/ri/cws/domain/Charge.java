package uo.ri.cws.domain;

import jakarta.persistence.Basic;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;
import uo.ri.cws.domain.base.BaseEntity;
import uo.ri.util.assertion.ArgumentChecks;

@Entity
@Table(name = "TCHARGES", uniqueConstraints = {
		@UniqueConstraint(columnNames = {
				"INVOICE_ID", "PAYMENTMEAN_ID"
		})
})
public class Charge extends BaseEntity {
	
	// natural attributes
	@Basic(optional=false) private double amount = 0.0;
	
	@Transient private double vat= 0.0;

	// accidental attributes
	@ManyToOne @JoinColumn(name = "INVOICE_ID") private Invoice invoice;
	@ManyToOne @JoinColumn(name = "PAYMENTMEAN_ID")
	private PaymentMean paymentMean;
	//JoinCloumn(name) needed in order to JPA to recognize
	
	Charge(){}
	
	public Charge(Invoice invoice, PaymentMean paymentMean, double amount) {
		ArgumentChecks.isNotNull(invoice, "invoice can not be null");
		ArgumentChecks.isNotNull(paymentMean, "paymentMean can not be null");
		ArgumentChecks.isTrue(amount >= 0, "amount can not be negative");
		if (!paymentMean.canPay(amount)) {
		    throw new IllegalStateException("PaymentMean cannot cover the amount");
		}
		this.amount = amount;
		this.paymentMean = paymentMean;
		this.invoice = invoice;
		// store the amount
		// increment the paymentMean accumulated -> paymentMean.pay( amount )
		paymentMean.pay(amount);
		// link invoice, this and paymentMean
		Associations.Settles.link(invoice, this, paymentMean);
	}

	public void rewind() {
		 if (invoice.getState() == Invoice.InvoiceState.PAID) {
		            throw new IllegalStateException("Invoice already settled");
		        }
	    paymentMean.pay(-amount);

		Associations.Settles.unlink(this);
	}

	public double getAmount() {
	    return amount + vat;
	}
	
	public double getVat() {
	    return vat;  // Ya es el monto, no el porcentaje
	}

	public double getAmountWithoutVat() {
	    return amount;
	}

	public Invoice getInvoice() {
		return invoice;
	}

	public PaymentMean getPaymentMean() {
		return paymentMean;
	}

	public void _setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

	public void _setPaymentMean(PaymentMean paymentMean) {
		this.paymentMean = paymentMean;
	}
	
	@Override
	public String toString() {
	    return "Charge[amount=" + amount + ", vat=" + vat + "]";
	}
	
}
