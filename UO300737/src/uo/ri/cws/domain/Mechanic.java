package uo.ri.cws.domain;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import uo.ri.cws.domain.base.BaseEntity;
import uo.ri.util.assertion.ArgumentChecks;

@Entity
@Table(name = "TMECHANICS")
public class Mechanic extends BaseEntity {
	
	// natural attributes
	@Column(unique=true) private String nif;
	@Basic(optional=false) private String surname;
	@Basic(optional=false) private String name;

	// accidental attributes
	@OneToMany(mappedBy = "mechanic")
	private Set<WorkOrder> assigned = new HashSet<>();
	
	@OneToMany(mappedBy = "mechanic")
	private Set<Intervention> interventions = new HashSet<>();

    @OneToMany(mappedBy = "mechanic")
    private Set<Contract> contracts = new HashSet<>();
    //Set is better than list to avoid duplicates
	
	public Mechanic(String nif, String name, String surname) {
		ArgumentChecks.isNotBlank(nif, "nif can not be blank");
		ArgumentChecks.isNotBlank(surname, "surname can not be blank");
		this.nif = nif;
		this.surname = surname;
		this.name = name;
	}
	
	Mechanic(){}

	public Mechanic(String nif) {
		this(nif, "no-surname", "no-name");
	}

	public Set<WorkOrder> getAssigned() {
		return new HashSet<>( assigned );
	}

	Set<WorkOrder> _getAssigned() {
		return assigned;
	}

	public Set<Intervention> getInterventions() {
		return new HashSet<>(interventions);
	}

	Set<Intervention> _getInterventions() {
		return interventions;
	}

	public String getNif() {
		return nif;
	}

	public String getSurname() {
		return surname;
	}

	public String getName() {
		return name;
	}
	

	public Set<Contract> getContracts() {
        return new LinkedHashSet<Contract>(contracts);
    }

    Set<Contract> _getContracts() {
        return contracts;
    }


    public Set<Contract> getContractsInForce() {
        Set<Contract> active = new LinkedHashSet<>();
        //Linked Hash Set because preserves insertion order
        for (Contract c : contracts) {
            if (c != null && c.isInForce()) {
                active.add(c);
            }
        }
        return active;
    }

    public Optional<Contract> getContractInForce() {
    	Optional<Contract> latest = Optional.empty();
	    for (Contract c : contracts) {
	        if (c != null && c.isInForce() 
	        		&& ((!latest.isPresent() ||
	        			c.getStartDate().isAfter(latest.get().getStartDate())))) {
	        	
	            	latest = Optional.of(c);
	        }
	    }
	    return latest;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void setSurname(String surname) {
		this.surname = surname;
	}
	
	@Override
	public String toString() {
	    return "Mechanic[nif=" + nif + ", name=" + name + ", surname=" + surname + "]";
	}
	
}