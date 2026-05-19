package uo.ri.cws.application.service.payroll.findsummaries;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.payroll.PayrollService;
import uo.ri.cws.application.service.util.ExceptionBox;

/**
 * Feature: Find payrolls all scenarios
 * Scenario: [P.Fs.11] Find payrolls for a non existent professional group
 */
public class ScenarioPFs11 {
	private PayrollService service = Factories.service.forPayrollService();
	private ExceptionBox ctx = new ExceptionBox();

	@When("[P.Fs.11] I try to find payrolls with a non existent professional group name")
	public void whenITryToFindPayrollsWithANonExistentProfessionalGroupName() {
		ctx.tryAndKeep(() -> service.findSummarizedByProfessionalGroupName("non-existent-professional-group"));
	}

	@Then("[P.Fs.11] a business error happens with an explaining message")
	public void thenABusinessErrorHappensWithAnExplainingMessage() {
		ctx.assertBusinessExceptionWithMessage();
	}
}