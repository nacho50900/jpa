package uo.ri.ui.util;

import java.util.List;

import uo.ri.cws.application.service.contract.ContractCrudService.ContractDto;
import uo.ri.cws.application.service.contract.ContractCrudService.ContractSummaryDto;
import uo.ri.cws.application.service.contracttype.ContractTypeCrudService.ContractTypeDto;
import uo.ri.cws.application.service.invoice.InvoicingService.InvoiceDto;
import uo.ri.cws.application.service.invoice.InvoicingService.PaymentMeanDto;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.cws.application.service.payroll.PayrollService.PayrollDto;
import uo.ri.cws.application.service.payroll.PayrollService.PayrollSummaryDto;
import uo.ri.cws.application.service.professionalgroup.ProfessionalGroupCrudService.ProfessionalGroupDto;
import uo.ri.cws.application.service.spare.SparePartCrudService.SparePartDto;
import uo.ri.cws.application.service.vehicletype.VehicleTypeCrudService.VehicleTypeDto;
import uo.ri.cws.application.service.workorder.WorkOrderCrudService.WorkOrderDto;
import uo.ri.util.console.Console;

public class Printer {

	public static void printInvoice(InvoiceDto invoice) {

		double importeConIVa = invoice.amount;
		double iva =  invoice.vat;
		double importeSinIva = importeConIVa / (1 + iva / 100);

		Console.printf("Invoice #: %d\n", 				invoice.number );
		Console.printf("\tDate: %1$td/%1$tm/%1$tY\n", 	invoice.date);
		Console.printf("\tTotal: %.2f €\n", 			importeSinIva);
		Console.printf("\tTax: %.1f %% \n", 			invoice.vat );
		Console.printf("\tTotal, tax inc.: %.2f €\n", 	invoice.amount );
		Console.printf("\tState: %s\n", 				invoice.state );
	}

	public static void printPaymentMeans(List<PaymentMeanDto> medios) {
		Console.println();
		Console.println("Available payment means");

		Console.printf("\t%s \t%-8.8s \t%s \n", "Id", "Type", "Acummulated");
		for (PaymentMeanDto medio : medios) {
			printPaymentMean( medio );
		}
	}

	private static void printPaymentMean(PaymentMeanDto medio) {
		Console.printf("\t%s \t%-8.8s \t%s \n"
				, medio.id
				, medio.getClass().getName()  // not the best...
				, medio.accumulated
		);
	}

	public static void printWorkOrder(WorkOrderDto rep) {

		Console.printf("\t%s \t%-40.40s \t%td/%<tm/%<tY \t%-12.12s \t%.2f\n"
				, rep.id
				, rep.description
				, rep.date
				, rep.state
				, rep.amount
		);
	}

	public static void printMechanic(MechanicDto m) {

		Console.printf("\t%s %-10.10s %-15.15s %-25.25s\n"
				, m.id
				, m.nif
				, m.name
				, m.surname
			);
	}

	public static void printVehicleType(VehicleTypeDto vt) {

		Console.printf("\t%s %-10.10s %5.2f €/hour %d\n"
				, vt.id
				, vt.name
				, vt.pricePerHour
				, vt.minTrainigHours
			);
	}

	public static void print(SparePartDto sp) {
		Console.printf("\t%-10.10s %-30.30s %4d %4d %4d %6.2f €\n"
				, sp.code
				, sp.description
				, sp.stock
				, sp.minStock
				, sp.maxStock
				, sp.price
			);
	}

	public static void printContractSummary(ContractSummaryDto c) {
		Console.printf("\t%s \t%-10.10s \t%.2f \t%d \t%-12.12s\n"
				, c.id
				, c.nif
				, c.settlement
				, c.numPayrolls
				, c.state
		);
	}

	public static void printContractType(ContractTypeDto dto) {
		Console.printf("Name: %s, Compensation days: %.2f\n"
				, dto.name
				, dto.compensationDays
			);
	}

	public static void printProfessionalGroup(ProfessionalGroupDto dto) {
		Console.printf("Name: %s, Triennium payment: %.2f, Productivity rate: %.2f\n"
				, dto.name
				, dto.trienniumPayment
				, dto.productivityRate
			);
	}

	public static void printPayrolls(List<PayrollDto> payrolls) {
		payrolls.forEach( p -> printPayrollLine(p) );
	}

	private static void printPayrollLine(PayrollDto p) {
		Console.printf("\t%s \t%-12.12s \t%.2f \t%.2f \t%.2f\n"
				, p.contractId
				, p.date
				, p.grossSalary
				, p.totalDeductions
				, p.netSalary
			);
	}

	public static void printPayrollDetails(PayrollDto dto) {
		Console.printf("Payroll ID: %s\n", dto.id);
		Console.printf("Contract ID: %s\n", dto.contractId);
		Console.printf("Date: %s\n", dto.date);
		Console.printf("Base Salary: %.2f\n", dto.baseSalary);
		Console.printf("Extra Salary: %.2f\n", dto.extraSalary);
		Console.printf("Productivity Earning: %.2f\n", dto.productivityEarning);
		Console.printf("Triennium Earning: %.2f\n", dto.trienniumEarning);
		Console.printf("Tax Deduction: %.2f\n", dto.taxDeduction);
		Console.printf("NIC Deduction: %.2f\n", dto.nicDeduction);
		Console.printf("Gross Salary: %.2f\n", dto.grossSalary);
		Console.printf("Total Deductions: %.2f\n", dto.totalDeductions);
		Console.printf("Net Salary: %.2f\n", dto.netSalary);
	}

	public static void printPayrollSummary(PayrollSummaryDto dto) {
		Console.printf("Payroll ID: %s, Date: %s, Net Salary: %.2f\n"
				, dto.id
				, dto.date
				, dto.netSalary
			);
	}

	public static void printContractDetails(ContractDto c) {
		Console.println("Contract details:");
		Console.printf("Id: %s\n", c.id);
		Console.printf("Mechanic: %s %s (NIF: %s)\n", c.mechanic.name, c.mechanic.surname, c.mechanic.nif);
		Console.printf("Contract type: %s\n", c.contractType.name);
		Console.printf("Professional group: %s\n", c.professionalGroup.name);
		Console.printf("Start date: %s\n", c.startDate);
		Console.printf("End date: %s\n", c.endDate);
		Console.printf("Annual base salary: %.2f\n", c.annualBaseSalary);
		Console.printf("Tax rate: %.2f\n", c.taxRate);
		Console.printf("Settlement: %.2f\n", c.settlement);
		Console.printf("State: %s\n", c.state);
	}

	public static void printSumarizedContract(ContractDto c) {
		Console.printf("\t%s \t%-10.10s \t%-30.30s \t%-12.12s \t%s \t%s \t%s \n"
				, c.id
				, c.mechanic.nif
				, c.mechanic.name + " " + c.mechanic.surname
				, c.contractType.name
				, c.startDate
				, c.endDate != null ? c.endDate : "---"
				, c.state
		);
	}

}