package uo.ri.cws.domain;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import uo.ri.cws.domain.base.BaseEntity;
import uo.ri.util.assertion.ArgumentChecks;

@Entity
@Table(name = "TVEHICLES")
public class Vehicle extends BaseEntity {
	
	@Column(unique=true) private String plateNumber;
	@Basic(optional=false) private String make;
	@Basic(optional=false) private String model;
	
	//Accidental
	@ManyToOne private Client client;
	@ManyToOne private VehicleType vehicleType;
	
	@OneToMany(mappedBy = "vehicle")
	private Set<WorkOrder> workOrders = new HashSet<WorkOrder>();

	Vehicle(){}
	
	public Vehicle(String plateNumber, String make, String model) {
		ArgumentChecks.isNotBlank(plateNumber, "plateNumber can not be blank");
		ArgumentChecks.isNotBlank(make, "make can not be blank");
		ArgumentChecks.isNotBlank(model, "model can not be blank");
		this.plateNumber = plateNumber;
		this.make = make;
		this.model = model;
	}

	public Vehicle(String plateNumber) {
		this(plateNumber, "no-make", "no-model");
	}

	public String getPlateNumber() {
		return plateNumber;
	}

	public String getMake() {
		return make;
	}

	public String getModel() {
		return model;
	}

	@Override
	public int hashCode() {
		return Objects.hash(plateNumber);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Vehicle other = (Vehicle) obj;
		return Objects.equals(plateNumber, other.plateNumber);
	}
	
	public Client getClient() {
		return client;
	}

	void _setClient(Client client) {
		this.client = client;
	}
	
	
	public VehicleType getVehicleType() {
		return vehicleType;
	}

	void _setVehicleType(VehicleType type) {
		this.vehicleType = type;
	}
	
	public Set<WorkOrder> getWorkOrders() {
		return new HashSet<WorkOrder>(workOrders);
	}

	Set<WorkOrder> _getWorkOrders() {
		return workOrders;
	}
	
	@Override
	public String toString() {
	    return "Vehicle[plateNumber=" + plateNumber + ", make=" + make + 
	           ", model=" + model + "]";
	}
	
    public void setMake(String make) {
        uo.ri.util.assertion.ArgumentChecks.isNotBlank(make, "make can not be blank");
        this.make = make;
    }

    public void setModel(String model) {
        uo.ri.util.assertion.ArgumentChecks.isNotBlank(model, "model can not be blank");
        this.model = model;
    }
	
}
