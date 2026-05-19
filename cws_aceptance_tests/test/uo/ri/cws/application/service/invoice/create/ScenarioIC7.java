package uo.ri.cws.application.service.invoice.create;

import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.invoice.InvoicingService;
import uo.ri.cws.application.service.util.ExceptionBox;

/**
 * Scenario: [I.C.7] Trying to create one invoice for a null argument
 */
public class ScenarioIC7 {
    private InvoicingService service = Factories.service.forCreateInvoiceService();
    private ExceptionBox ctx = new ExceptionBox();

    @When("[I.C.7] I try to create an invoice for a null list of workorders")
    public void whenITryToCreateInvoiceForNullList() {
        ctx.tryAndKeep(() -> service.create(null));
    }

    @Then("[I.C.7] argument is rejected with an explaining message")
    public void thenArgumentIsRejectedWithMessage() {
        ctx.assertIllegalArgumentExceptionWithMessage();
    }
}