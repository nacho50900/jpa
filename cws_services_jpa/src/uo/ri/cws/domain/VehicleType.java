package uo.ri.cws.domain;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import uo.ri.cws.domain.base.BaseEntity;
import uo.ri.util.assertion.ArgumentChecks;

@Entity
@Table(name = "TVEHICLETYPES")
public class VehicleType extends BaseEntity {
	
	// natural attributes
	@Column(unique = true) private String name;
	private double pricePerHour;

	// accidental attributes
	@OneToMany(mappedBy = "vehicleType")
	private Set<Vehicle> vehicles = new HashSet<>();

	public Set<Vehicle> getVehicles() {
		return new HashSet<>( vehicles );
	}

	Set<Vehicle> _getVehicles() {
		return vehicles;
	}
	
	VehicleType(){}

	public VehicleType(String name, double pricePerHour, Set<Vehicle> vehicles) {
		ArgumentChecks.isNotBlank(name, "name can not be blank");
		ArgumentChecks.isTrue(pricePerHour>=0, "pricePerHour can not be negative");
		ArgumentChecks.isNotNull(vehicles, "vehicles can not be null");
		this.name = name;
		this.pricePerHour = pricePerHour;
		this.vehicles = vehicles;
	}

	public VehicleType(String name, double pricePerHour) {
		this(name, pricePerHour, new HashSet<Vehicle>());
		
	}

	public VehicleType(String name) {
		this(name, 0, new HashSet<Vehicle>());
	}

	public String getName() {
		return name;
	}

	public double getPricePerHour() {
		return pricePerHour;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	//To string is just over natural atributes not accidental ones
	@Override
	public String toString() {
	    return "VehicleType[name=" + name + ", pricePerHour=" + pricePerHour + "]";
	}

}
