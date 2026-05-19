package uo.ri.cws.domain;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import uo.ri.cws.domain.base.BaseEntity;
import uo.ri.util.assertion.ArgumentChecks;

@Entity
@Table(name = "TPROFESSIONALGROUPS")
public class ProfessionalGroup extends BaseEntity {

    @Id
    protected String id = UUID.randomUUID().toString();

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private double trienniumPayment;

    @Column(nullable = false)
    private double productivityRate;

    @OneToMany(mappedBy = "professionalGroup")
    private Set<Contract> contracts = new HashSet<>();

    protected ProfessionalGroup() { }

    public ProfessionalGroup(String name, double trienniumPayment, double productivityRate) {
        ArgumentChecks.isNotNull(name, "Name cannot be null");
        ArgumentChecks.isNotEmpty(name, "Name cannot be empty");
        ArgumentChecks.isTrue(trienniumPayment >= 0, "Triennium payment must be >= 0");
        ArgumentChecks.isTrue(productivityRate >= 0, "Productivity rate must be >= 0");

        this.name = name;
        this.trienniumPayment = trienniumPayment;
        this.productivityRate = productivityRate;
    }

    //Calcula el tax rate (IRPF) basándose en el salario anual según tramos.
    //El salario anual se divide en tramos, cada uno con su tasa correspondiente.

    public double calculateTaxRate(double annualSalary) {
        if (annualSalary <= 12450) {
            return 0.19;
        } else if (annualSalary <= 20200) {
            return 0.24;
        } else if (annualSalary <= 35200) {
            return 0.30;
        } else if (annualSalary <= 60000) {
            return 0.37;
        } else if (annualSalary <= 300000) {
            return 0.45;
        } else {
            return 0.47;
        }
    }

    public Set<Mechanic> getMechanicsWithActiveContracts() {
        Set<Mechanic> mechanics = new LinkedHashSet<>();
        for (Contract contract : contracts) {
            if (contract != null && contract.isInForce()) {
                mechanics.add(contract.getMechanic());
            }
        }
        return mechanics;
    }

    public int getActiveEmployeesCount() {
        return getMechanicsWithActiveContracts().size();
    }

    public double getAccumulatedAnnualBaseSalary() {
        double total = 0.0;
        for (Contract contract : contracts) {
            if (contract != null && contract.isInForce()) {
                total += contract.getAnnualBaseSalary();
            }
        }
        return total;
    }

    public void setTrienniumPayment(double trienniumPayment) {
        ArgumentChecks.isTrue(trienniumPayment >= 0, "Triennium payment must be >= 0");
        this.trienniumPayment = trienniumPayment;
    }

    public void setProductivityRate(double productivityRate) {
        ArgumentChecks.isTrue(productivityRate >= 0, "Productivity rate must be >= 0");
        this.productivityRate = productivityRate;
    }

    @Override
	public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getTrienniumPayment() {
        return trienniumPayment;
    }

    public double getProductivityRate() {
        return productivityRate;
    }

    public Set<Contract> getContracts() {
        return Collections.unmodifiableSet(contracts);
    }

    Set<Contract> _getContracts() {
        return contracts;
    }
    
    @Override
    public String toString() {
        return "ProfessionalGroup[name=" + name + 
               ", trienniumPayment=" + trienniumPayment + 
               ", productivityRate=" + productivityRate + "]";
    }

}