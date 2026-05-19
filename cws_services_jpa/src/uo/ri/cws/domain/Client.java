package uo.ri.cws.domain;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import uo.ri.cws.domain.base.BaseEntity;
import uo.ri.util.assertion.ArgumentChecks;

@Entity
@Table(name = "TCLIENTS", uniqueConstraints = {
		@UniqueConstraint(columnNames = {
				"NIF"
		})
})
public class Client extends BaseEntity {

	//better to use an artificial id as a primary key, becuase the user
	// may misstyoe, and ups u cant change nif, is the primary key
	@Column(unique=true, name="NIF") private String nif;
	@Basic(optional=false) private String name;
	private String surname;
	private String email;
	private String phone;
	@Embedded private Address address;
	
	// accidental attributes
	@OneToMany (mappedBy="client") private Set<Vehicle> vehicles = new HashSet<Vehicle>();
	@OneToMany (mappedBy="client") private Set<PaymentMean> paymentMeans = new HashSet<PaymentMean>();

	Client() {} //Not public
	
	public Client(String nif, String name, String surname, String email, String phone, Address address) {
		ArgumentChecks.isNotBlank(nif, "nif can not be blank");
		ArgumentChecks.isNotBlank(name, "name can not be blank");
		ArgumentChecks.isNotBlank(surname, "surname can not be blank");
		ArgumentChecks.isNotBlank(email, "email can not be blank");
		ArgumentChecks.isNotBlank(phone, "phone can not be blank");
		ArgumentChecks.isNotNull(address, "address can not be null");
		
		this.nif = nif;
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.phone = phone;
		this.address = address;
	}

	public Client(String nif, String name, String surname) {
		this(nif, name, surname, "no-email", "no-phone", 
				new Address("nos-street", "no-city", "no-zip"));
	}//Avoid repeating code reusing constructor
	
	public Client(String nif) {
		this(nif, "no-name", "no.surname", "no-email", "no-phone", 
				new Address("nos-street", "no-city", "no-zip"));
	}

	public String getNif() {
		return nif;
	}

	public String getName() {
		return name;
	}
	public String getSurname() {
		return surname;
	}

	public String getEmail() {
		return email;
	}

	public String getPhone() {
		return phone;
	}
	public Address getAddress() {
		return address;
	}

	public Set<Vehicle> getVehicles() {
		return new HashSet<>(vehicles);
	}

	protected Set<Vehicle> _getVehicles() {
		return vehicles;
	}

	public Set<PaymentMean> getPaymentMeans() {
		return  new HashSet<>(paymentMeans);
	}

	Set<PaymentMean> _getPaymentMeans() {
		return paymentMeans;
	}

	public void setAddress(Address address) {
		this.address = address;
	}
	
	@Override
	public String toString() {
	    return "Client[nif=" + nif + ", name=" + name + ", surname=" + surname + 
	           ", email=" + email + ", phone=" + phone + ", address=" + address + "]";
	}
	
}

