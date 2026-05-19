package uo.ri.cws.application.service.contract.delete;

import static org.junit.jupiter.api.Assertions.assertNull;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.contract.ContractCrudService;
import uo.ri.cws.application.service.util.dbfixture.Db;
import uo.ri.cws.application.service.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.util.dbfixture.records.TContractsRecord;
import uo.ri.util.exception.BusinessException;

/**
 * Scenario: [C.D.1] Delete a contract
 */
public class ScenarioCD1 {
    private ContractCrudService service = Factories.service.forContractCrudService();
    private TContractsRecord contract;

    @Given("[C.D.1] a contract for a mechanic with no payrolls neither interventions")
    public void givenAContractForAMechanicWithNoPayrollsNeitherInterventions() {
        contract = DbFixtures.aContractWithAMechanic();
    }

    @When("[C.D.1] I delete the contract for the mechanic")
    public void whenIDeleteTheContractForTheMechanic() throws BusinessException {
        service.delete(contract.id);
    }

    @Then("[C.D.1] This contract does not exist any more")
    public void thenThisContractDoesNotExistAnyMore() {
        TContractsRecord r = Db.findById(TContractsRecord.class, contract.id);
        assertNull(r);
    }
}