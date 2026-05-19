package uo.ri.cws.application.service.invoice.create;

import java.util.List;

import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.invoice.InvoicingService;
import uo.ri.cws.application.service.util.ExceptionBox;

/**
 * Scenario: [I.C.8] Trying to create one invoice for an empty argument
 */
public class ScenarioIC8 {
    private InvoicingService service = Factories.service.forCreateInvoiceService();
    private ExceptionBox ctx = new ExceptionBox();

    @When("[I.C.8] I try to create an invoice for an empty list of workorders")
    public void whenITryToCreateInvoiceForEmptyList() {
        ctx.tryAndKeep(() -> service.create(List.of()));
    }

    @Then("[I.C.8] argument is rejected with an explaining message")
    public void thenArgumentIsRejectedWithMessage() {
        ctx.assertIllegalArgumentExceptionWithMessage();
    }
}