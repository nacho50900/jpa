package uo.ri.cws.application.service.payroll.findsummaries;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.payroll.PayrollService;
import uo.ri.cws.application.service.payroll.PayrollService.PayrollSummaryDto;
import uo.ri.cws.application.service.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.util.dbfixture.records.TMechanicsRecord;
import uo.ri.util.exception.BusinessException;

/**
 * Feature: Find payrolls all scenarios
 * Scenario: [P.Fs.5] Find payrolls for a mechanic with no payroll
 */
public class ScenarioPFs5 {
	private PayrollService service = Factories.service.forPayrollService();
	private TMechanicsRecord mechanic;
	private List<PayrollSummaryDto> result;

	@Given("[P.Fs.5] a mechanic with no payrolls")
	public void givenAMechanicWithNoPayrolls() {
		mechanic = DbFixtures.aMechanic();
	}

	@When("[P.Fs.5] I search payrolls by that mechanic")
	public void whenISearchPayrollsByThatMechanic() throws BusinessException {
		result = service.findSummarizedByMechanicId(mechanic.id);
	}

	@Then("[P.Fs.5] the result is empty")
	public void thenTheResultIsEmpty() {
		assertTrue(result.isEmpty());
	}
}