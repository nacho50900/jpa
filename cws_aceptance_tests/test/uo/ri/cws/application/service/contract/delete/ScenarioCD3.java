package uo.ri.cws.application.service.contract.delete;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.contract.ContractCrudService;
import uo.ri.cws.application.service.util.ExceptionBox;
import uo.ri.cws.application.service.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.util.dbfixture.records.TContractsRecord;

/**
 * Scenario: [C.D.3] Try to delete a contract for a mechanic with interventions during the contract
 */
public class ScenarioCD3 {
    private ExceptionBox ctx = new ExceptionBox();
    private ContractCrudService service = Factories.service.forContractCrudService();
	private TContractsRecord contract;

    @Given("[C.D.3] a contract for a mechanic with interventions during the contract")
    public void givenAContractForAMechanicWithInterventionsDuringTheContract() {
        contract = DbFixtures.aContractWithInterventions();
    }

    @When("[C.D.3] I try to delete that contract")
    public void whenITryToDeleteThatContract() {
        ctx.tryAndKeep(() -> service.delete(contract.id) );
    }

    @Then("[C.D.3] a business error happens with an explaining message")
    public void thenABusinessErrorHappensWithAnExplainingMessage() {
        ctx.assertBusinessExceptionWithMessage();
    }
}