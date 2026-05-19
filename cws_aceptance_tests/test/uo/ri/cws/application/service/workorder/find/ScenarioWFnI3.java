package uo.ri.cws.application.service.workorder.find;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.invoice.InvoicingService;
import uo.ri.cws.application.service.invoice.InvoicingService.InvoicingWorkOrderDto;
import uo.ri.cws.application.service.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.util.dbfixture.records.TClientsRecord;
import uo.ri.cws.application.service.util.dbfixture.records.TWorkOrdersRecord;
import uo.ri.util.exception.BusinessException;

/**
 * Scenario: [W.FnI.3] Find not invoiced workorders by client nif with some not finished workorders
 */
public class ScenarioWFnI3 {
	private InvoicingService service = Factories.service.forCreateInvoiceService();

	private List<TWorkOrdersRecord> finishedWorkOrders;
	private TClientsRecord client;
	private String vehicleId;
	private List<InvoicingWorkOrderDto> found;

	@Given("[W.FnI.3] a client registered with a vehicle and a list of several finished workorders")
    public void givenClientWithVehicleAndSeveralFinishedWorkorders() {
		client = DbFixtures.aClient();
    	finishedWorkOrders = DbFixtures.someWorkordersFinishedWithVehicleForClient(client.id);
    	// Get vehicle ID from the first finished workorder
    	vehicleId = finishedWorkOrders.get(0).vehicle_Id;
    }

    @And("[W.FnI.3] one INVOICED workorder")
    public void andOneInvoicedWorkorder() {
    	DbFixtures.aWorkOrderInvoicedForVehicle(vehicleId);
    }

    @And("[W.FnI.3] one OPEN workorder")
    public void andOneOpenWorkorder() {
    	DbFixtures.aWorkOrderOpenForVehicle(vehicleId);
    }

    @And("[W.FnI.3] one ASSIGNED workorder")
    public void andOneAssignedWorkorder() {
    	DbFixtures.aWorkOrderAssignedForVehicle(vehicleId);
    }

    @When("[W.FnI.3] I search not invoiced workorders by client nif")
    public void whenISearchNotInvoicedWorkordersByClientNif() throws BusinessException {
    	found = service.findNotInvoicedWorkOrdersByClientNif(client.nif);
    }

    @Then("[W.FnI.3] I get only finished workorders")
    public void thenIGetOnlyFinishedWorkorders() {
    	assertEquals(finishedWorkOrders.size(), found.size());
    	found.forEach(wo -> assertEquals("FINISHED", wo.state));
    }
}