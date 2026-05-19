package uo.ri.cws.domain;

import java.time.LocalDate;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import uo.ri.util.assertion.ArgumentChecks;

@Entity
@Table(name = "TCREDITCARDS")
public class CreditCard extends PaymentMean {
	
	@Column(unique = true) private String number;
	@Basic(optional=false) private String type;
	@Basic(optional=false) private LocalDate validThru;
	
	CreditCard(){}
	
	public CreditCard(String number, String type, LocalDate validThru) {
	    ArgumentChecks.isNotBlank(number, "number can not be blank");
	    ArgumentChecks.isNotBlank(type, "type can not be blank");
	    ArgumentChecks.isNotNull(validThru, "validThru can not be null");
		this.number = number;
		this.type = type;
		this.validThru = validThru;
	}

	public String getNumber() {
		return number;
	}

	public String getType() {
		return type;
	}

	public LocalDate getValidThru() {
		return validThru;
	}

	@Override
	public boolean canPay(Double amount) {
	    return validThru.isAfter(LocalDate.now());
	}

	@Override
	public String toString() {
	    return "CreditCard[number=" + number + ", type=" + type + 
	           ", validThru=" + validThru + ", accumulated=" + getAccumulated() + "]";
	}
	
}
