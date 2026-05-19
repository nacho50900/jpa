package uo.ri.cws.application.service.util;

import uo.ri.cws.application.service.util.dbfixture.records.TPayrollsRecord;

public class PayrollTotalizer {

	private double grossSalary;
	private double totalDeductions;
	private double netSalary;

	public PayrollTotalizer(TPayrollsRecord r) {
		grossSalary = r.baseSalary
				+ r.extraSalary
				+ r.productivityEarning
				+ r.trienniumEarning;
		
		totalDeductions = r.taxDeduction + r.nicDeduction;
		
		netSalary = grossSalary - totalDeductions;
	}

	public double netSalary() {
		return netSalary;
	}

	public double grossSalary() {
		return grossSalary;
	}

	public double totalDeductions() {
		return totalDeductions;
	}

}
