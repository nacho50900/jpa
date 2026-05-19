package uo.ri.cws.application.service.payroll.crud;

import java.util.ArrayList;
import java.util.List;

import uo.ri.cws.application.service.payroll.PayrollService.PayrollDto;
import uo.ri.cws.application.service.payroll.PayrollService.PayrollSummaryDto;
import uo.ri.cws.domain.Payroll;

public class DtoAssembler {

	public static PayrollDto toDto(Payroll arg) {
		PayrollDto result = new PayrollDto();

		result.id = arg.getId();
		result.version = arg.getVersion();
		result.date = arg.getDate();


		result.grossSalary = arg.getMonthlyBaseSalary() +
				arg.getExtraSalary() + 
				arg.getProductivityEarning() + 
				arg.getTrienniumEarning();


		result.totalDeductions = arg.getTaxDeduction() + arg.getNicDeduction();
		result.netSalary = result.grossSalary - result.totalDeductions;

		result.baseSalary = arg.getMonthlyBaseSalary();
		result.extraSalary = arg.getExtraSalary();
		result.productivityEarning = arg.getProductivityEarning();
		result.trienniumEarning = arg.getTrienniumEarning();
		result.nicDeduction = arg.getNicDeduction();
		result.taxDeduction = arg.getTaxDeduction();
		
		//result.netSalary = arg.getNetSalary(); not saved in db
		//result.grossSalary = arg.getGrossSalary();
		//result.totalDeductions = arg.getTotalDeductions();

		result.contractId = arg.getContract().getId();
		return result;

	}

	public static List<PayrollDto> toDtoList(List<Payroll> all) {
		List<PayrollDto> result = new ArrayList<>();
		for (Payroll record : all) {
			result.add(toDto(record));
		}
		return result;
	}


	public static PayrollSummaryDto toSummaryDto(Payroll arg) {
		PayrollSummaryDto result = new PayrollSummaryDto();
		result.id = arg.getId();
		result.date = arg.getDate();
		//result.netSalary = arg.getNetSalary();

		double grossSalary = arg.getMonthlyBaseSalary()
            + arg.getExtraSalary()
            + arg.getProductivityEarning()
            + arg.getTrienniumEarning();
		double totalDeductions = arg.getTaxDeduction() + arg.getNicDeduction();
		result.netSalary = grossSalary - totalDeductions;

		return result;
	}

	public static List<PayrollSummaryDto> toSummaryDtoList(List<Payroll> all) {
		List<PayrollSummaryDto> result = new ArrayList<>();
		for (Payroll record : all) {
			result.add(toSummaryDto(record));
		}
		return result;
	}

}
