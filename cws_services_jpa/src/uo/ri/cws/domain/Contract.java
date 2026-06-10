package uo.ri.cws.domain;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import uo.ri.cws.domain.base.BaseEntity;
import uo.ri.util.assertion.ArgumentChecks;

@Entity
@Table(name = "TCONTRACTS")
public class Contract extends BaseEntity {

    public enum ContractState { IN_FORCE, TERMINATED }

    // Natural Attributes
    @Column(nullable = false)
    private LocalDate startDate;

    private LocalDate endDate;

    @Column(nullable = false)
    private double annualBaseSalary;

    @Column(nullable = false)
    private double taxRate = 0.0;

    @Column(nullable = false)
    private double settlement = 0.0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContractState state = ContractState.IN_FORCE;
    
    // Accidental Atributes
    @ManyToOne(optional = false)
    private Mechanic mechanic;

    @ManyToOne(optional = false)
    private ContractType contractType;

    @ManyToOne(optional = false)
    private ProfessionalGroup professionalGroup;

    @OneToMany(mappedBy = "contract", fetch = FetchType.EAGER)
    private Set<Payroll> payrolls = new HashSet<>();

    protected Contract() { }

    public Contract(Mechanic mechanic, ContractType type, ProfessionalGroup group,
            LocalDate signingDate, LocalDate endDate, double annualBaseSalary) {

        ArgumentChecks.isNotNull(mechanic, "Mechanic cannot be null");
        ArgumentChecks.isNotNull(type, "ContractType cannot be null");
        ArgumentChecks.isNotNull(group, "ProfessionalGroup cannot be null");
        ArgumentChecks.isNotNull(signingDate, "Signing date cannot be null");
        ArgumentChecks.isTrue(
        		annualBaseSalary > 0.0, "Annual base salary must be positive");

        LocalDate adjustedStart = signingDate.with(TemporalAdjusters.firstDayOfMonth());
    
        LocalDate adjustedEnd = null;
        if (isFixedTerm(type)) {
            // 2.a) Para FIXED_TERM: endDate es obligatorio y
            //     además DEBE ser >= signingDate ORIGINAL (sin ajustar)
            if (endDate == null) {
                throw new IllegalArgumentException(
                		"End date is mandatory for FIXED_TERM contracts");
            }
            if (endDate.isBefore(signingDate)) {
                throw new IllegalArgumentException(
                		"End date cannot be before signing date");
            }
    
            // 2.b) Y también comprobar las fechas AJUSTADAS a mes
            adjustedEnd = endDate.with(TemporalAdjusters.lastDayOfMonth());
            if (adjustedEnd.isBefore(adjustedStart)) {
                throw new IllegalArgumentException(
                		"Adjusted end date cannot be before adjusted start date");
            }
        } else {
            // Para no FIXED_TERM se ignora la endDate
            adjustedEnd = null;
        }

        this.startDate = signingDate.with(TemporalAdjusters.firstDayOfMonth());
        this.endDate = adjustedEnd;
        this.annualBaseSalary = annualBaseSalary;
        
        this.taxRate = calculateTaxRate(annualBaseSalary);
        
        Associations.Employs.link(mechanic, this, type, group);
    }

    public Contract(Mechanic mechanic, ContractType type, ProfessionalGroup group,
                    LocalDate signingDate, double annualBaseSalary) {
        
        ArgumentChecks.isNotNull(mechanic, "Mechanic cannot be null");
        ArgumentChecks.isNotNull(type, "ContractType cannot be null");
        ArgumentChecks.isNotNull(group, "ProfessionalGroup cannot be null");
        ArgumentChecks.isNotNull(signingDate, "Signing date cannot be null");
        ArgumentChecks.isTrue(
        		annualBaseSalary > 0.0, "Annual base salary must be positive");
    
        // NO PODEMOS HACER THIS() PORQUE NECESITAMOS ESTE CHECK
        ArgumentChecks.isTrue(!isFixedTerm(type),
        		"Short constructor cannot be used for FIXED_TERM contracts"
        		+ "(end date is mandatory)");
    
        this.startDate = signingDate.with(TemporalAdjusters.firstDayOfMonth());
        this.annualBaseSalary = annualBaseSalary;
        this.endDate = null;

        this.taxRate = calculateTaxRate(annualBaseSalary);
        
        Associations.Employs.link(mechanic, this, type, group);
    }
    
    private static double calculateTaxRate(double annualSalary) {
        if (annualSalary < 12450) {
            return 0.19;
        } else if (annualSalary < 20200) {
            return 0.24;
        } else if (annualSalary < 35200) {
            return 0.30;
        } else if (annualSalary < 60000) {
            return 0.37;
        } else if (annualSalary < 300000) {
            return 0.45;
        } else {
            return 0.47;
        }
    }
    
    private static boolean isFixedTerm(ContractType type) {
        // According to tests, the name "FIXED_TERM" is the discriminator
        return "FIXED_TERM".equalsIgnoreCase(type.getName());
    }

    public boolean isInForce() {
        return state == ContractState.IN_FORCE;
    }

    public boolean isTerminated() {
        return state == ContractState.TERMINATED;
    }

    public void terminate(LocalDate terminationDate) {
        if (isTerminated()) {
            throw new IllegalStateException("Contract already terminated");
        }
        if (terminationDate == null) {
            throw new IllegalArgumentException("Termination date cannot be null");
        }
        if (terminationDate.isBefore(this.startDate)) {
            throw new IllegalArgumentException(
                    "Termination date cannot be before start date");
        }

        this.endDate = terminationDate.with(TemporalAdjusters.lastDayOfMonth());

        long years = ChronoUnit.YEARS.between(this.startDate, this.endDate.plusDays(1));

        this.settlement = 0.0;
        if (years >= 1) {
            double compDaysPerYear = this.contractType.getCompensationDaysPerYear();
            
            double totalGross = this.payrolls.stream()
                    .mapToDouble(Payroll::getGrossSalary)
                    .sum();
            double annualGross = (totalGross / this.payrolls.size()) * 12.0; 
            // media mensual * 12
            double dailyGross = annualGross / 365.0;
            
            this.settlement = dailyGross * compDaysPerYear * years;
        }   

        this.state = ContractState.TERMINATED;
        this.updatedNow();
        /*
        if (daysWorked > 365) {
            long years = ChronoUnit.YEARS.between(this.startDate,
            		this.endDate.plusDays(1));
    	    double compDaysPerYear = this.contractType.getCompensationDaysPerYear();
    	    
    	    //double dailyGross = this.annualBaseSalary / 365.0; //NO DA EXACTO
    	    
    	    double totalGross = this.payrolls.stream()
    	    	    .mapToDouble(Payroll::getGrossSalary)
    	    	    .sum();
    	    double dailyGross = (totalGross / years) / 365.0;
    	    
    	    this.settlement = dailyGross * compDaysPerYear * years;
        }*/

        this.state = ContractState.TERMINATED;
    	this.updatedNow();
    }
    
	 public boolean hasPayrollForMonth(LocalDate month) {
	     ArgumentChecks.isNotNull(month, "Month cannot be null");
	     
	     for (Payroll payroll : payrolls) {
	         if (payroll.isFromMonth(month)) {
	             return true;
	         }
	     }
	     return false;
	 }

    // When endDate < startDate, treat it as if endDate is null(indefinite contract)
    public boolean isActiveInMonth(LocalDate month) {
        ArgumentChecks.isNotNull(month, "Month cannot be null");
     
        LocalDate firstDayOfMonth = month.withDayOfMonth(1);
        LocalDate lastDayOfMonth = month.withDayOfMonth(month.lengthOfMonth());
     
        boolean startedBeforeMonthEnds = !startDate.isAfter(lastDayOfMonth);

        boolean notEndedBeforeMonthStarts = (endDate == null || 
            endDate.isBefore(startDate) ||  
            !endDate.isBefore(firstDayOfMonth));
     
        return startedBeforeMonthEnds && notEndedBeforeMonthStarts;
    }

    public Payroll generatePayrollForMonth(LocalDate month) {
        ArgumentChecks.isNotNull(month, "Month cannot be null");
        
        if (!isActiveInMonth(month)) {
            throw new IllegalStateException(
                "Cannot generate payroll: contract not active in month " + month);
        }
        
        if (hasPayrollForMonth(month)) {
            throw new IllegalStateException(
                "Payroll already exists for month " + month);
        }
        
        return new Payroll(this, month);
    }
    
    public Mechanic getMechanic() {
        return mechanic;
    }

    void _setTaxRate(double taxRate) {
        if (taxRate < 0.0) {
            throw new IllegalArgumentException("Tax rate cannot be negative");
        }
        this.taxRate = taxRate;
    }
    
    public ContractType getContractType() {
        return contractType;
    }

    public ProfessionalGroup getProfessionalGroup() {
        return professionalGroup;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
    
    public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

    public double getAnnualBaseSalary() {
        return annualBaseSalary;
    }
    
    public void setAnnualBaseSalary(double annualBaseSalary) {
        ArgumentChecks.isTrue(
        		annualBaseSalary > 0.0, "Annual base salary must be positive");
        this.annualBaseSalary = annualBaseSalary;
    }

    public double getTaxRate() {
        return taxRate;
    }

    public double getSettlement() {
        return settlement;
    }

    public Set<Payroll> getPayrolls() {
        return Collections.unmodifiableSet(payrolls);
    }

    Set<Payroll> _getPayrolls() {
        return payrolls;
    }

	public Object getState() {
		return state;
	}
	

	void _setMechanic(Mechanic mechanic) {
		this.mechanic = mechanic;
	}

	void _setContractType(ContractType contractType) {
		this.contractType = contractType;
	}

	void _setProfessionalGroup(ProfessionalGroup professionalGroup) {
		this.professionalGroup = professionalGroup;
	}
	
	@Override
	public String toString() {
	    return "Contract[startDate=" + startDate + ", endDate=" + endDate + 
	           ", annualBaseSalary=" + annualBaseSalary + ", taxRate=" + taxRate + 
	           ", settlement=" + settlement + ", state=" + state + "]";
	}
	
}