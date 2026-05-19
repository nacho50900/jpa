package uo.ri.cws.application.service.payroll.delete;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.payroll.PayrollService;
import uo.ri.cws.application.service.util.ExceptionBox;

/**
 * Feature: Delete previous month payroll for a mechanic
 * Scenario Outline: [P.D.6] Try to delete last payroll with invalid mechanic dni
 */
public class ScenarioPD6 {
    private PayrollService service = Factories.service.forPayrollService();
    private ExceptionBox ctx = new ExceptionBox();

    @When("[P.D.6] I try to delete with wrong mechanic {string} argument")
    public void whenITryToDeleteWithWrongMechanicArgument(String id) {
        String mechanicId = "null".equals(id) ? null : id;
        ctx.tryAndKeep(() -> service.deleteLastGeneratedOfMechanicId(mechanicId));
    }

    @Then("[P.D.6] argument is rejected with an explaining message")
    public void thenArgumentIsRejectedWithAnExplainingMessage() {
        ctx.assertIllegalArgumentExceptionWithMessage();
    }
}