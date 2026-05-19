package uo.ri.cws.application.service.workorder.find;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.invoice.InvoicingService;
import uo.ri.cws.application.service.invoice.InvoicingService.InvoicingWorkOrderDto;
import uo.ri.util.exception.BusinessException;

/**
 * Scenario: [W.FnI.5] Try to find workorders with wrong parameters
 */
public class ScenarioWFnI5 {
	private InvoicingService service = Factories.service.forCreateInvoiceService();

	private List<InvoicingWorkOrderDto> found;

	@When("[W.FnI.5] I look for a workorder by empty nif")
    public void whenILookForWorkorderByEmptyNif() throws BusinessException {
    	found = service.findNotInvoicedWorkOrdersByClientNif(" ");
    }

    @Then("[W.FnI.5] I get an empty list")
    public void thenIGetAnEmptyList() {
    	assertTrue(found.isEmpty());
    }
}