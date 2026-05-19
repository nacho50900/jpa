package uo.ri.cws.application.service.invoice.create;

import java.util.ArrayList;
import java.util.List;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.invoice.InvoicingService;
import uo.ri.cws.application.service.util.ExceptionBox;
import uo.ri.cws.application.service.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.util.dbfixture.records.TWorkOrdersRecord;

/**
 * Scenario: [I.C.9] Trying to create one invoice and one of the id is null
 */
public class ScenarioIC9 {
    private InvoicingService service = Factories.service.forCreateInvoiceService();
    private ExceptionBox ctx = new ExceptionBox();
    private List<TWorkOrdersRecord> workOrders;
    private List<String> workOrderIds;

    @Given("[I.C.9] a client registered with a vehicle and a list of several finished workorder")
    public void givenClientWithVehicleAndFinishedWorkorders() {
        workOrders = DbFixtures.someWorkordersFinishedWithVehicleAndClient();
        workOrderIds = new ArrayList<>(workOrders.stream().map(wo -> wo.id).toList());
    }

    @And("[I.C.9] a null workorder id")
    public void andNullWorkorderId() {
        workOrderIds.add(null);
    }

    @When("[I.C.9] I try to create an invoice")
    public void whenITryToCreateInvoice() {
        ctx.tryAndKeep(() -> service.create(workOrderIds));
    }

    @Then("[I.C.9] argument is rejected with an explaining message")
    public void thenArgumentIsRejectedWithMessage() {
        ctx.assertIllegalArgumentExceptionWithMessage();
    }
}