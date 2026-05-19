package uo.ri.cws.application.service.payroll.findsummaries;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.payroll.PayrollService;
import uo.ri.cws.application.service.util.ExceptionBox;

/**
 * Feature: Find payrolls all scenarios
 * Scenario: [P.Fs.12] Try to find a payroll with invalid professional group name
 */
public class ScenarioPFs12 {
	private PayrollService service = Factories.service.forPayrollService();
	private ExceptionBox ctx = new ExceptionBox();

	@When("[P.Fs.12] I try to find payrolls with invalid professional group {string}")
	public void whenITryToFindPayrollsWithInvalidProfessionalGroupName(String name) {
		String actualName = "null".equals(name) ? null : name;
		ctx.tryAndKeep(() -> service.findSummarizedByProfessionalGroupName(actualName));
	}

	@Then("[P.Fs.12] argument is rejected with an explaining message")
	public void thenArgumentIsRejectedWithAnExplainingMessage() {
		ctx.assertIllegalArgumentExceptionWithMessage();
	}
}