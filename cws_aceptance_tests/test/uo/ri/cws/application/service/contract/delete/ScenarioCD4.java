package uo.ri.cws.application.service.contract.delete;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.contract.ContractCrudService;
import uo.ri.cws.application.service.util.ExceptionBox;

/**
 * Scenario: [C.D.4] Try to delete a non existing contract
 */
public class ScenarioCD4 {
    private ExceptionBox ctx = new ExceptionBox();
    private ContractCrudService service = Factories.service.forContractCrudService();

    @When("[C.D.4] I try to delete a non existing contract")
    public void whenITryToDeleteANonExistingContract() {
        ctx.tryAndKeep(() -> 
        		service.delete("non-existent-contract-id")
        	);
    }

    @Then("[C.D.4] a business error happens with an explaining message")
    public void thenABusinessErrorHappensWithAnExplainingMessage() {
        ctx.assertBusinessExceptionWithMessage();
    }
}