package uo.ri.cws.application.service.payroll.findsummaries;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.payroll.PayrollService;
import uo.ri.cws.application.service.util.ExceptionBox;

/**
 * Feature: Find payrolls all scenarios
 * Scenario: [P.Fs.7] Try to find a payroll with invalid mechanic id
 */
public class ScenarioPFs7 {
	private PayrollService service = Factories.service.forPayrollService();
	private ExceptionBox ctx = new ExceptionBox();

	@When("[P.Fs.7] I try to find payrolls with an invalid mechanic {string}")
	public void whenITryToFindPayrollsWithAnInvalidMechanicId(String id) {
		String actualId = "null".equals(id) ? null : id;
		ctx.tryAndKeep(() -> service.findSummarizedByProfessionalGroupName(actualId));
	}

	@Then("[P.Fs.7] argument is rejected with an explaining message")
	public void thenArgumentIsRejectedWithAnExplainingMessage() {
		ctx.assertIllegalArgumentExceptionWithMessage();
	}
}