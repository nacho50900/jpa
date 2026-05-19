package uo.ri.cws.application.service.payroll.findsummaries;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.payroll.PayrollService;
import uo.ri.cws.application.service.util.ExceptionBox;

/**
 * Feature: Find payrolls all scenarios
 * Scenario: [P.Fs.6] Find payrolls for a non existent mechanic
 */
public class ScenarioPFs6 {
	private PayrollService service = Factories.service.forPayrollService();
	private ExceptionBox ctx = new ExceptionBox();

	@When("[P.Fs.6] I try to find payrolls for a non existent mechanic")
	public void whenITryToFindPayrollsForANonExistentMechanic() {
		ctx.tryAndKeep(() -> service.findSummarizedByMechanicId("non-existent-mechanic-id"));
	}

	@Then("[P.Fs.6] a business error happens with an explaining message")
	public void thenABusinessErrorHappensWithAnExplainingMessage() {
		ctx.assertBusinessExceptionWithMessage();
	}
}