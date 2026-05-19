package uo.ri.cws.application.service.payroll.find;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.payroll.PayrollService;
import uo.ri.cws.application.service.payroll.PayrollService.PayrollDto;
import uo.ri.cws.application.service.util.Asserts;
import uo.ri.cws.application.service.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.util.dbfixture.records.TPayrollsRecord;
import uo.ri.util.exception.BusinessException;

/**
 * Feature: Find payrolls
 * Scenario: [P.F.4] Find an existent payroll
 */
public class ScenarioPF4 {
	private PayrollService service = Factories.service.forPayrollService();
	private TPayrollsRecord payroll;
	private Optional<PayrollDto> result;

	@Given("[P.F.4] several payrolls")
	public void givenAPayroll() {
		payroll = DbFixtures.aPayrollWithContractAndMechanic();
		DbFixtures.aPayrollWithContractAndMechanic();
		DbFixtures.aPayrollWithContractAndMechanic();
		DbFixtures.aPayrollWithContractAndMechanic();
	}

	@When("[P.F.4] I search payroll details for that payroll")
	public void whenISearchPayrollDetailsForThatPayroll() throws BusinessException {
		result = service.findById(payroll.id);
	}

	@Then("[P.F.4] that payroll is returned")
	public void thenThatPayrollIsReturned() {
		assertTrue(result.isPresent());
		PayrollDto dto = result.get();
		
		Asserts.dtoMatchesRecord(payroll, dto);
	}
}