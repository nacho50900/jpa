package uo.ri.cws.application.service.payroll.delete;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.payroll.PayrollService;
import uo.ri.cws.application.service.util.ExceptionBox;

/**
 * Feature: Delete previous month payroll for a mechanic
 * Scenario: [P.D.5] Try to delete previous month payroll for a non existent mechanic
 */
public class ScenarioPD5 {
    private PayrollService service = Factories.service.forPayrollService();
    private ExceptionBox ctx = new ExceptionBox();

    @When("[P.D.5] I try to delete previous month payroll for a non existent mechanic")
    public void whenITryToDeletePreviousMonthPayrollForANonExistentMechanic() {
        ctx.tryAndKeep(() -> service.deleteLastGeneratedOfMechanicId("NON_EXISTENT_MECHANIC_ID"));
    }

    @Then("[P.D.5] a business error happens with an explaining message")
    public void thenABusinessErrorHappensWithAnExplainingMessage() {
        ctx.assertBusinessExceptionWithMessage();
    }
}