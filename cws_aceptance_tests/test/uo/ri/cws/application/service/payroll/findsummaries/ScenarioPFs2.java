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
import uo.ri.cws.application.service.util.Asserts;
import uo.ri.cws.application.service.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.util.dbfixture.records.TPayrollsRecord;
import uo.ri.util.exception.BusinessException;

/**
 * Feature: Find payrolls all scenarios
 * Scenario: [P.Fs.2] Find payrolls when there are some
 */
public class ScenarioPFs2 {
	private PayrollService service = Factories.service.forPayrollService();
	private List<TPayrollsRecord> payrolls;
	private List<PayrollSummaryDto> result;

	@Given("[P.Fs.2] several payrolls already created")
	public void givenSeveralPayrollsAlreadyCreated() {
		payrolls = List.of(
			DbFixtures.aPayrollWithContractAndMechanic(),
			DbFixtures.aPayrollWithContractAndMechanic(),
			DbFixtures.aPayrollWithContractAndMechanic()
		);
	}

	@When("[P.Fs.2] I search all payrolls")
	public void whenISearchAllPayrolls() throws BusinessException {
		result = service.findAllSummarized();
	}

	@Then("[P.Fs.2] all the payroll summaries are returned")
	public void thenAllThePayrollSummariesAreReturned() {
		assertFalse(result.isEmpty());
		assertEquals(payrolls.size(), result.size());
	}

	@Then("[P.Fs.2] all the summaries are right")
	public void thenAllTheSummariesAreRight() {
		Asserts.rightPayrollSummaries(payrolls, result);
	}
}