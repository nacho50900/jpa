package uo.ri.cws.domain;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import uo.ri.cws.domain.base.BaseEntity;

@Entity
@Table(name = "TCONTRACTTYPES")
public class ContractType extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private double compensationDaysPerYear;

    @OneToMany(mappedBy = "contractType")
    private Set<Contract> contracts = new HashSet<>();

    protected ContractType() { }

    public ContractType(String name, double compensationDaysPerYear) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
            		"ContractType name cannot be null/blank");
        }
        if (compensationDaysPerYear < 0.0) {
            throw new IllegalArgumentException(
            		"Compensation days per year cannot be negative");
        }
        this.name = name;
        this.compensationDaysPerYear = compensationDaysPerYear;
    }

    public String getName() {
        return name;
    }

    public double getCompensationDaysPerYear() {
        return compensationDaysPerYear;
    }

    public Set<Contract> getContracts() {
        return Collections.unmodifiableSet(contracts);
    }

    // Acceso interno para mantener asociaciones bidireccionales
    Set<Contract> _getContracts() {
        return contracts;
    }
    
    @Override
    public String toString() {
        return "ContractType[name=" + name + 
               ", compensationDaysPerYear=" + compensationDaysPerYear + "]";
    }
    
}