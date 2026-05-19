package uo.ri.cws.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import uo.ri.cws.domain.base.BaseEntity;
import uo.ri.util.assertion.ArgumentChecks;

@Entity
@Table(name = "TPAYROLLS")
public class Payroll extends BaseEntity {


    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Contract contract;

    @Column(nullable = false) private LocalDate date;

    @Column(nullable = false) private double baseSalary;

    @Column(nullable = false) private double extraSalary;

    @Column(nullable = false) private double productivityEarning;

    @Column(nullable = false) private double trienniumEarning;

    @Column(nullable = false) private double taxDeduction;

    @Column(nullable = false) private double nicDeduction;
    
    @Transient private double totalDeductions;

    @Transient private double grossSalary;

    @Transient private double netSalary;

    private static final int PAYMENTS_IN_YEAR = 14;
    private static final int PAYROLLS_IN_YEAR = 12;
    private static final double STANDARD_NIC = 0.05;

    protected Payroll() { }

    public Payroll(Contract contract, LocalDate anyDateInMonth) {
        ArgumentChecks.isNotNull(contract, "Contract cannot be null");
        ArgumentChecks.isNotNull(anyDateInMonth, "Payroll date cannot be null");

        LocalDate payrollDate = anyDateInMonth.with(
                TemporalAdjusters.lastDayOfMonth());
            
        ArgumentChecks.isTrue(!payrollDate.isBefore(contract.getStartDate()),
                "Cannot create payroll before contract start date");

        this.contract = contract;
        this.date = payrollDate;
        contract._getPayrolls().add(this);

        double annual = contract.getAnnualBaseSalary();
        
        // Base mensual (14 pagas distribuidas en 12 meses)
        this.baseSalary = annual / PAYMENTS_IN_YEAR;

        // Extra (solo junio/diciembre)
        int month = payrollDate.getMonthValue();
        
        this.extraSalary = 
        		(month == 6 || month == 12) ? (annual / PAYMENTS_IN_YEAR) : 0.0;

        // Productividad del mes facturado
        this.productivityEarning = computeProductivityForMonth(contract, payrollDate);

        // Trienios
        int years = contract.getStartDate().until(payrollDate).getYears();
        int trienniums = years / 3;
        double trienniumUnit = contract.getProfessionalGroup().getTrienniumPayment();
        this.trienniumEarning = trienniums > 0 ? trienniums * trienniumUnit : 0.0;

        // Ingresos totales
        double incomes = baseSalary + extraSalary +
        		productivityEarning + trienniumEarning;

        // IRPF
        double taxRate = contract.getTaxRate();
        this.taxDeduction = incomes * taxRate;

        // Seguridad Social (empleado)
        this.nicDeduction = (annual / PAYROLLS_IN_YEAR) * STANDARD_NIC;

        // Totales
        this.totalDeductions = taxDeduction + nicDeduction;
        this.grossSalary = incomes;
        this.netSalary = incomes - totalDeductions;
    }

    private double computeProductivityForMonth(Contract c, LocalDate payrollDate) {
        double sum = 0.0;
        Mechanic m = c.getMechanic();
        if (m == null) {
            return 0.0;
        }

        // Obtener el productivity rate del professional group
        double productivityRate = c.getProfessionalGroup().getProductivityRate();

        for (Intervention it : m._getInterventions()) {
            if (it == null) {
                continue;
            }
            WorkOrder wo = it.getWorkOrder();
            if (wo == null) {
                continue;
            }
            if (!wo.isInvoiced()) {
                continue;
            }

            LocalDateTime d = wo.getDate();
            if (d == null) {
                continue;
            }

            // Verificar que sea del mismo mes y año
            if (d.getYear() == payrollDate.getYear() &&
                d.getMonthValue() == payrollDate.getMonthValue()) {
                sum += wo.getAmount();
            }
        }
        
        // Aplicar el productivity rate al total
        return sum * productivityRate;
    }
    
    public boolean isFromMonth(LocalDate month) {
        return this.date.getMonthValue() == month.getMonthValue() &&
               this.date.getYear() == month.getYear();
    }

    public Contract getContract() {
        return contract;
    }

    public LocalDate getDate() {
        return date;
    }

    public double getExtraSalary() {
        return extraSalary;
    }

    public double getProductivityEarning() {
        return productivityEarning;
    }

    public double getTrienniumEarning() {
        return trienniumEarning;
    }

    public double getTaxDeduction() {
        return taxDeduction;
    }

    public double getNicDeduction() {
        return nicDeduction;
    }

    public double getTotalDeductions() {
        return totalDeductions;
    }

    public double getGrossSalary() {
        return grossSalary;
    }

    public double getNetSalary() {
        return netSalary;
    }

    public double getMonthlyBaseSalary() {
        return baseSalary;
    }

    @Override
    public String toString() {
        return "Payroll[date=" + date + ", baseSalary=" + baseSalary + 
               ", extraSalary=" + extraSalary + 
               ", productivityEarning=" + productivityEarning + 
               ", trienniumEarning=" + trienniumEarning + 
               ", taxDeduction=" + taxDeduction + ", nicDeduction=" + nicDeduction + "]";
    }

}