package uo.ri.cws.application.service.contracttype.find;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.contracttype.ContractTypeCrudService;
import uo.ri.cws.application.service.util.ExceptionBox;

/**
 * Scenario: [Ct.F.3] Try to find a contract type with wrong name
 */
public class ScenarioCtF3 {
    private ContractTypeCrudService service = Factories.service.forContractTypeCrudService();
    private ExceptionBox ctx = new ExceptionBox();

    @When("[Ct.F.3] I try to find a contract type with name null")
    public void whenITryToFindContractTypeWithNullName() {
        ctx.tryAndKeep(() -> service.findByName(null));
    }

    @When("[Ct.F.3] I try to find a contract type with name {string}")
    public void whenITryToFindContractTypeWithName(String name) {
        ctx.tryAndKeep(() -> service.findByName(name));
    }

    @Then("[Ct.F.3] argument is rejected with an explaining message")
    public void thenArgumentIsRejectedWithMessage() {
        ctx.assertIllegalArgumentExceptionWithMessage();
    }
}