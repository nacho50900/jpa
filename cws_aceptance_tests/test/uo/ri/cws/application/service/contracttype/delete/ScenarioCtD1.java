package uo.ri.cws.application.service.contracttype.delete;

import static org.junit.jupiter.api.Assertions.assertNull;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.contracttype.ContractTypeCrudService;
import uo.ri.cws.application.service.util.dbfixture.Db;
import uo.ri.cws.application.service.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.util.dbfixture.records.TContractTypesRecord;
import uo.ri.util.exception.BusinessException;

/**
 * Feature: Delete a contract type
 * Scenario: [Ct.D.1] Delete a contract type
 */
public class ScenarioCtD1 {
	private ContractTypeCrudService service = Factories.service.forContractTypeCrudService();
	private TContractTypesRecord contractType;

	@Given("[Ct.D.1] a contract type with no contracts")
	public void givenAContractTypeWithNoContracts() {
		contractType = DbFixtures.aContractType();
	}

	@When("[Ct.D.1] I delete that contract type")
	public void whenIDeleteThatContractType() throws BusinessException {
		service.delete(contractType.name);
	}

	@Then("[Ct.D.1] The contract is deleted")
	public void thenTheContractIsDeleted() {
		TContractTypesRecord r = Db.findById(TContractTypesRecord.class, contractType.id);
		assertNull(r);
	}
}