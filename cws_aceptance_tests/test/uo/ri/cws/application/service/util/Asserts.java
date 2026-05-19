package uo.ri.cws.application.service.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.service.contract.ContractCrudService.ContractDto;
import uo.ri.cws.application.service.contract.ContractCrudService.ContractSummaryDto;
import uo.ri.cws.application.service.contracttype.ContractTypeCrudService.ContractTypeDto;
import uo.ri.cws.application.service.invoice.InvoicingService.InvoiceDto;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.cws.application.service.payroll.PayrollService.PayrollDto;
import uo.ri.cws.application.service.payroll.PayrollService.PayrollSummaryDto;
import uo.ri.cws.application.service.util.dbfixture.records.TContractTypesRecord;
import uo.ri.cws.application.service.util.dbfixture.records.TContractsRecord;
import uo.ri.cws.application.service.util.dbfixture.records.TInvoicesRecord;
import uo.ri.cws.application.service.util.dbfixture.records.TMechanicsRecord;
import uo.ri.cws.application.service.util.dbfixture.records.TPayrollsRecord;

public class Asserts {

	public static void matches(MechanicDto expected, MechanicDto dto) {
		assertEquals(expected.nif, dto.nif);
		assertEquals(expected.name, dto.name);
		assertEquals(expected.surname, dto.surname);
	}

	public static void recordMatchesDto(MechanicDto expected, TMechanicsRecord rec) {
		assertEquals(expected.id, rec.id);
		assertEquals(expected.nif, rec.nif);
		assertEquals(expected.name, rec.name);
		assertEquals(expected.surname, rec.surname);
		assertEquals(expected.version, rec.version);
	}

	public static void matches(TMechanicsRecord expected, MechanicDto dto) {
		assertEquals(expected.id, dto.id);
		assertEquals(expected.nif, dto.nif);
		assertEquals(expected.name, dto.name);
		assertEquals(expected.surname, dto.surname);
		assertEquals(expected.version, dto.version);
	}

	public static void isNow(Timestamp ts) {
        LocalDateTime ts1 = ts.toLocalDateTime().truncatedTo(ChronoUnit.SECONDS);
        LocalDateTime ts2 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        long difference = Math.abs(ts1.until(ts2, ChronoUnit.SECONDS));
        
        assertTrue(difference <= 1, "Difference is greater than 1 sec");
    }

	public static void recordMatchesDto(InvoiceDto expected, TInvoicesRecord loaded) {
		assertEquals(expected.id, loaded.id);
		assertEquals(expected.date, loaded.date.toLocalDate());
		assertEquals(expected.amount, loaded.amount, 0.01);
		assertEquals(expected.vat, loaded.vat, 0.01);
		assertEquals(expected.state, loaded.state);
		assertEquals(expected.version, loaded.version);
	}

	public static void rightAmounts(InvoiceDto expected, InvoiceDto invoice) {
		assertEquals(expected.amount, invoice.amount, 0.01);
		assertEquals(expected.vat, invoice.vat, 0.01);
		assertEquals(expected.state, invoice.state);
	}

	public static void recordMatchesDto(ContractTypeDto expected, TContractTypesRecord loaded) {
		assertEquals(expected.id, loaded.id);
		assertEquals(expected.name, loaded.name);
		assertEquals(expected.version, loaded.version);		
	}

	public static void recordMatchesDto(ContractDto expected, TContractsRecord loaded) {
        assertEquals(expected.id, loaded.id);
        assertEquals(expected.version, loaded.version);

        assertEquals(expected.startDate, loaded.startDate.toLocalDate());
        assertEquals(expected.endDate, loaded.endDate != null ? loaded.endDate.toLocalDate() : null);
        assertEquals(expected.annualBaseSalary, loaded.annualBaseSalary);
        assertEquals(expected.taxRate, loaded.taxRate);
        assertEquals(expected.settlement, loaded.settlement);
        assertEquals(expected.state, loaded.state);
        
        assertEquals(expected.mechanic.id, loaded.mechanic_Id);
        assertEquals(expected.contractType.id, loaded.contractType_Id);
        assertEquals(expected.professionalGroup.id, loaded.professionalGroup_Id);
	}

    public static void rightSummaries(
			List<TContractsRecord> contracts, 
			List<ContractSummaryDto> results, 
			String mechanicNif, int numPayrolls) {

		assertEquals(contracts.size(), results.size());
		for (TContractsRecord expected : contracts) {
			Optional<ContractSummaryDto> ocs = findDto(results, expected.id);
			assertTrue(ocs.isPresent());

			ContractSummaryDto dto = ocs.get();
			rightSummary(expected, dto, mechanicNif, numPayrolls);
		}
		
	}

	public static void rightSummary(
			TContractsRecord expected, 
			ContractSummaryDto dto, 
			String mechanicNif,	int numPayrolls) {
				
		assertEquals(expected.id, dto.id);
		assertEquals(mechanicNif, dto.nif);
		assertEquals(expected.settlement, dto.settlement);
		assertEquals(expected.state, dto.state);
		assertEquals(numPayrolls, dto.numPayrolls);
	}

	private static Optional<ContractSummaryDto> findDto(
			List<ContractSummaryDto> results, String id) {
		
		return results.stream()
			.filter(dto -> dto.id.equals(id))
			.findFirst();
	}

	public static void dtoMatchesRecord(TPayrollsRecord expected, PayrollDto found) {
		assertEquals(expected.id, found.id);
		assertEquals(expected.contract_Id, found.contractId);
		assertEquals(expected.version, found.version);

		PayrollTotalizer totalizer = new PayrollTotalizer( expected );
		
		assertEquals(expected.date.toLocalDate(), found.date);
		assertEquals(expected.baseSalary, found.baseSalary, 0.001);
		assertEquals(expected.extraSalary, found.extraSalary, 0.001);
		assertEquals(expected.trienniumEarning, found.trienniumEarning, 0.001);
		assertEquals(expected.productivityEarning, found.productivityEarning, 0.001);
		assertEquals(expected.taxDeduction, found.taxDeduction, 0.001);
		assertEquals(expected.nicDeduction, found.nicDeduction, 0.001);
		
		assertEquals(totalizer.grossSalary(), found.grossSalary, 0.001);
		assertEquals(totalizer.totalDeductions(), found.totalDeductions, 0.01);
		assertEquals(totalizer.netSalary(), found.netSalary, 0.001);
	}

	public static void rightPayrollSummaries(List<TPayrollsRecord> payrolls,
			List<PayrollSummaryDto> result) {
		
		for (TPayrollsRecord expected : payrolls) {
			Optional<PayrollSummaryDto> op = findPayrollSummary(result, expected.id);
			assertTrue(op.isPresent());
			PayrollSummaryDto dto = op.get();
			rightPayrollSummary(expected, dto);
		}
	}

	private static Optional<PayrollSummaryDto> findPayrollSummary(
			List<PayrollSummaryDto> result, String id) {
		return result.stream()
				.filter(dto -> dto.id.equals(id))
				.findFirst();
	}

	public static void rightPayrollSummary(TPayrollsRecord expected,
			PayrollSummaryDto dto) {

		PayrollTotalizer totalizer = new PayrollTotalizer( expected );

		assertEquals(expected.id, dto.id);
		assertEquals(totalizer.netSalary(), dto.netSalary, 0.01);
		assertEquals(expected.date.toLocalDate(), dto.date);
	}

}
