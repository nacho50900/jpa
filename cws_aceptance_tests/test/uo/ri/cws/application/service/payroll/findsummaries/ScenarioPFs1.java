package uo.ri.cws.application.service.payroll.findsummaries;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.payroll.PayrollService;
import uo.ri.cws.application.service.payroll.PayrollService.PayrollSummaryDto;
import uo.ri.util.exception.BusinessException;

/**
 * Feature: Find payrolls all scenarios
 * Scenario: [P.Fs.1] Find payrolls when there is none
 */
public class ScenarioPFs1 {
	private PayrollService service = Factories.service.forPayrollService();
	private List<PayrollSummaryDto> result;

	@When("[P.Fs.1] I search for all payrolls")
	public void whenISearchForAllPayrolls() throws BusinessException {
		result = service.findAllSummarized();
	}

	@Then("[P.Fs.1] the result is empty")
	public void thenTheResultIsEmpty() {
		assertTrue(result.isEmpty());
	}
}