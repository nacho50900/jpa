package uo.ri.cws.application.service.contract.delete;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.contract.ContractCrudService;
import uo.ri.cws.application.service.util.ExceptionBox;

/**
 * Scenario: [C.D.5] Try to delete a null contract id
 */
public class ScenarioCD5 {
    private ExceptionBox ctx = new ExceptionBox();
    private ContractCrudService service = Factories.service.forContractCrudService();

    @When("[C.D.5] I try to delete a null contract id")
    public void whenITryToDeleteANullContractId() {
        ctx.tryAndKeep(() -> service.delete( null ));
    }

    @Then("[C.D.5] argument is rejected with an explaining message")
    public void thenArgumentIsRejectedWithAnExplainingMessage() {
        ctx.assertIllegalArgumentExceptionWithMessage();
    }
}