package uo.ri.cws.application.service.workorder.find;

import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.invoice.InvoicingService;
import uo.ri.cws.application.service.util.ExceptionBox;

/**
 * Scenario: [W.FnI.6] Try to find workorders with null argument
 */
public class ScenarioWFnI6 {
	private InvoicingService service = Factories.service.forCreateInvoiceService();
	private ExceptionBox ctx = new ExceptionBox();

	@When("[W.FnI.6] I try to find workorders with null nif")
    public void whenITryToFindWorkordersWithNullNif() {
    	ctx.tryAndKeep(() -> service.findNotInvoicedWorkOrdersByClientNif(null));
    }

    @Then("[W.FnI.6] argument is rejected with an explaining message")
    public void thenArgumentIsRejectedWithMessage() {
    	ctx.assertIllegalArgumentExceptionWithMessage();
    }
}