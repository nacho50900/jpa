package uo.ri.cws.application.service.contract.terminate;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.contract.ContractCrudService;
import uo.ri.cws.application.service.util.ExceptionBox;

/**
 * Feature: Terminate a contract
 * Scenario: [C.T.4] Try to terminate a non existent contract
 */
public class ScenarioCT4 {
    private ContractCrudService service = Factories.service.forContractCrudService();
    private ExceptionBox ctx = new ExceptionBox();

    @When("[C.T.4] I try to terminate a non existent contract")
    public void whenITryToTerminateNonExistentContract() {
    	ctx.tryAndKeep(() -> service.terminate("non-existent-id"));
    }

    @Then("[C.T.4] a business error happens with an explaining message")
    public void thenBusinessErrorHappens() {
    	ctx.assertBusinessExceptionWithMessage();
    }
}
