package uo.ri.util;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

import uo.ri.cws.domain.Contract;
import uo.ri.cws.domain.ContractType;
import uo.ri.cws.domain.Mechanic;
import uo.ri.cws.domain.ProfessionalGroup;

public class ContractBuilder implements Builder<Contract> {
    private static final int DEFAULT_BASE_SALARY = 10000;

    private Mechanic mechanic = new MechanicBuilder().build();
    private ProfessionalGroup category = new ProfessionalGroupBuilder().build();
    private ContractType type = new ContractTypeBuilder().build();
    private LocalDate signingDate = LocalDate.now();

    private LocalDate endDate = null;
    private double baseSalary = DEFAULT_BASE_SALARY;

    @Override
    public Contract build() {
        return new Contract(
        		mechanic, 
        		type, category, 
        		signingDate, endDate, 
                baseSalary
            );
    }

    public ContractBuilder withProfessionalGroup(ProfessionalGroup category) {
        this.category = category;
        return this;
    }

    public ContractBuilder withType(ContractType type) {
        this.type = type;
        return this;
    }

    public ContractBuilder withSigningDate(LocalDate startDate) {
        this.signingDate = startDate;
        return this;
    }

    public ContractBuilder withEndDate(LocalDate endDate) {
        this.endDate = endDate.with(TemporalAdjusters.lastDayOfMonth());
        return this;
    }

    public ContractBuilder withBaseSalary(double baseSalary) {
        this.baseSalary = baseSalary;
        return this;
    }

    public ContractBuilder forMechanic(Mechanic mechanic) {
        this.mechanic = mechanic;
        return this;
    }

}
