package uo.ri.cws.application.service.contract.add;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.contract.ContractCrudService;
import uo.ri.cws.application.service.util.ExceptionBox;

/**
 * Scenario: [C.A.8] Try to add a null contract
 */
public class ScenarioCA8 {
    private ExceptionBox ctx = new ExceptionBox();
    private ContractCrudService service = Factories.service.forContractCrudService();

    @When("[C.A.8] I try to add a null argument")
    public void whenITryToAddANullArgument() {
        ctx.tryAndKeep(() -> service.create(null));
    }

    @Then("[C.A.8] argument is rejected with an explaining message")
    public void thenArgumentIsRejectedWithAnExplainingMessage() {
        ctx.assertIllegalArgumentExceptionWithMessage();
    }
}