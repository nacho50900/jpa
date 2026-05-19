package uo.ri.cws.application.service.payroll.findsummaries;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.payroll.PayrollService;
import uo.ri.cws.application.service.payroll.PayrollService.PayrollSummaryDto;
import uo.ri.cws.application.service.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.util.dbfixture.records.TMechanicsRecord;
import uo.ri.cws.application.service.util.dbfixture.records.TPayrollsRecord;
import uo.ri.util.exception.BusinessException;

/**
 * Feature: Find payrolls all scenarios
 * Scenario: [P.Fs.4] Find payrolls for a mechanic with several payrolls
 */
public class ScenarioPFs4 {
	private PayrollService service = Factories.service.forPayrollService();
	private TMechanicsRecord mechanic;
	private List<TPayrollsRecord> payrolls;
	private List<PayrollSummaryDto> result;

	@Given("[P.Fs.4] a mechanic with several payrolls")
	public void givenAMechanicWithSeveralPayrolls() {
		mechanic = DbFixtures.aMechanic();
		payrolls = List.of(
			DbFixtures.aPayrollWithContractForMechanic(mechanic.id),
			DbFixtures.aPayrollWithContractForMechanic(mechanic.id),
			DbFixtures.aPayrollWithContractForMechanic(mechanic.id)
		);
	}

	@When("[P.Fs.4] I search payrolls by that mechanic")
	public void whenISearchPayrollsByThatMechanic() throws BusinessException {
		result = service.findSummarizedByMechanicId(mechanic.id);
	}

	@Then("[P.Fs.4] all the payroll summaries for that mechanic are found")
	public void thenAllThePayrollSummariesForThatMechanicAreFound() {
		assertFalse(result.isEmpty());
		assertEquals(payrolls.size(), result.size());
	}
}