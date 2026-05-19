package uo.ri.cws.application.service.payroll.findsummaries;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.payroll.PayrollService;
import uo.ri.cws.application.service.payroll.PayrollService.PayrollSummaryDto;
import uo.ri.cws.application.service.util.Asserts;
import uo.ri.cws.application.service.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.util.dbfixture.records.TMechanicsRecord;
import uo.ri.cws.application.service.util.dbfixture.records.TPayrollsRecord;
import uo.ri.util.exception.BusinessException;

/**
 * Feature: Find payrolls all scenarios
 * Scenario: [P.Fs.3] Find payrolls for a mechanic with one payroll
 */
public class ScenarioPFs3 {
	private PayrollService service = Factories.service.forPayrollService();
	private TMechanicsRecord mechanic;
	private TPayrollsRecord payroll;
	private List<PayrollSummaryDto> result;

	@Given("[P.Fs.3] a mechanic with one payroll")
	public void givenAMechanicWithOnePayroll() {
		mechanic = DbFixtures.aMechanic();
		payroll = DbFixtures.aPayrollWithContractForMechanic(mechanic.id);
	}

	@When("[P.Fs.3] I search payrolls by that mechanic")
	public void whenISearchPayrollsByThatMechanic() throws BusinessException {
		result = service.findSummarizedByMechanicId(mechanic.id);
	}

	@Then("[P.Fs.3] the right payroll summary for that mechanic is found")
	public void thenThePayrollSummaryForThatMechanicIsFound() {
		assertEquals(1, result.size());
		PayrollSummaryDto dto = result.get(0);
		
		Asserts.rightPayrollSummary(payroll, dto);
	}

}