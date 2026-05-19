package uo.ri.cws.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import uo.ri.util.assertion.ArgumentChecks;

@Entity
@Table(name = "TVOUCHERS")
public class Voucher extends PaymentMean {
	
	@Column(unique=true) private String code;
    private double available = 0.0;
    private String description;

    Voucher(){}
    
    public Voucher(String code, String description, double available) {
		ArgumentChecks.isNotBlank(code, "code can not be blank");
		ArgumentChecks.isNotBlank(description, "description can not be blank");
		ArgumentChecks.isTrue(available > 0, "available can not be negative");
		this.code = code;
		this.description = description;
		this.available = available;
	}

    @Override
    public void pay(double amount) {
        ArgumentChecks.isTrue(amount <= available, "Not enough available to pay");
        super.pay(amount);
        available -= amount;
    }

    @Override
    public boolean canPay(Double amount) {
        return ((amount != null) && (amount <= available));
    }

    public String getCode() {
        return code;
    }

    public double getAvailable() {
        return available;
    }

    public String getDescription() {
        return description;
    }
    
    @Override
    public String toString() {
        return "Voucher[code=" + code + ", description=" + description + 
               ", available=" + available + ", accumulated=" + getAccumulated() + "]";
    }
    
}