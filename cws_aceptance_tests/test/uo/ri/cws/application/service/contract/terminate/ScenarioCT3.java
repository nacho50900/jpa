package uo.ri.cws.application.service.contract.terminate;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.contract.ContractCrudService;
import uo.ri.cws.application.service.util.ExceptionBox;
import uo.ri.cws.application.service.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.util.dbfixture.records.TContractsRecord;
import uo.ri.cws.application.service.util.dbfixture.records.TMechanicsRecord;

/**
 * Feature: Terminate a contract
 * Scenario: [C.T.3] Try to terminate a terminated contract
 */
public class ScenarioCT3 {
    private ContractCrudService service = Factories.service.forContractCrudService();
    private ExceptionBox ctx = new ExceptionBox();
    private TContractsRecord contract;

    @Given("[C.T.3] a terminated contract")
    public void givenTerminatedContract() {
        TMechanicsRecord mechanic = DbFixtures.aMechanic();
        contract = DbFixtures.aContractTerminatedForMechanic(mechanic.id);
    }

    @When("[C.T.3] I try to terminate a terminated contract for mechanic <nif>")
    public void whenITryToTerminateTerminatedContract() {
    	ctx.tryAndKeep(() -> service.terminate( contract.id ));
    }

    @Then("[C.T.3] a business error happens with an explaining message")
    public void thenBusinessErrorHappens() {
    	ctx.assertBusinessExceptionWithMessage();
    }
}
