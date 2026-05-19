package uo.ri.cws.application.service.payroll.find;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.payroll.PayrollService;
import uo.ri.cws.application.service.util.ExceptionBox;

/**
 * Feature: Find payrolls
 * Scenario: [P.F.3] Try to find a payroll with invalid id
 */
public class ScenarioPF3 {
	private PayrollService service = Factories.service.forPayrollService();
	private ExceptionBox ctx = new ExceptionBox();

	@When("[P.F.3] I try to find payroll with and invalid {string}")
	public void whenITryToFindPayrollWithAnInvalidId(String id) {
		String actualId = "null".equals(id) ? null : id;
		ctx.tryAndKeep(() -> service.findById(actualId));
	}

	@Then("[P.F.3] argument is rejected with an explaining message")
	public void thenArgumentIsRejectedWithAnExplainingMessage() {
		ctx.assertIllegalArgumentExceptionWithMessage();
	}
}