package uo.ri.cws.application.service.contract.terminate;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.contract.ContractCrudService;
import uo.ri.cws.application.service.util.ExceptionBox;

/**
 * Feature: Terminate a contract
 * Scenario Outline: [C.T.5] Try to terminate a contract with wrong id
 */
public class ScenarioCT5 {
    private ContractCrudService service = Factories.service.forContractCrudService();
    private ExceptionBox ctx = new ExceptionBox();

    @When("[C.T.5] I try to terminate a contract with wrong {string}")
    public void whenITryToTerminateContractWithWrongId(String id) {
    	String idToUse = "null".equals(id) ? null : id;
    	ctx.tryAndKeep(() -> service.terminate(idToUse));
    }

    @Then("[C.T.5] argument is rejected with an explaining message")
    public void thenArgumentIsRejected() {
    	ctx.assertIllegalArgumentExceptionWithMessage();
    }
}
