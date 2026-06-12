package uo.ri.util;

import uo.ri.cws.domain.ProfessionalGroup;

public class ProfessionalGroupBuilder implements Builder<ProfessionalGroup> {

    private static final double DEFAULT_PRODUCTIVITY_PLUS = 0.05;
    private static final double DEFAULT_TRIENNIUM_SALARY = 30.0;

    private String name = Values.newString("cat", 3);
    private double productivityPlus = DEFAULT_PRODUCTIVITY_PLUS;
    private double trienniumSalary = DEFAULT_TRIENNIUM_SALARY;

    @Override
    public ProfessionalGroup build() {
        return new ProfessionalGroup(name, trienniumSalary, productivityPlus);
    }

    public ProfessionalGroupBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ProfessionalGroupBuilder withProductivityPlus(double amount) {
        this.productivityPlus = amount;
        return this;
    }

    public ProfessionalGroupBuilder withTrienniumSalary(double amount) {
        this.trienniumSalary = amount;
        return this;
    }

}
