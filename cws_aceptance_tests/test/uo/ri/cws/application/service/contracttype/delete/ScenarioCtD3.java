package uo.ri.cws.application.service.contracttype.delete;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.contracttype.ContractTypeCrudService;
import uo.ri.cws.application.service.util.ExceptionBox;
import uo.ri.cws.application.service.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.util.dbfixture.records.TContractTypesRecord;

/**
 * Feature: Delete a contract type
 * Scenario: [Ct.D.3] Try to delete a contract type with contracts
 */
public class ScenarioCtD3 {
	private ExceptionBox ctx = new ExceptionBox();
	private ContractTypeCrudService service = Factories.service.forContractTypeCrudService();
	private String contractTypeId;

	@Given("[Ct.D.3] a contract type with several contracts")
	public void givenAContractTypeWithSeveralContracts() {
		TContractTypesRecord contractType = DbFixtures.aContractTypeWithContracts();
		contractTypeId = contractType.id;
	}

	@When("[Ct.D.3] I try to delete that contract type")
	public void whenITryToDeleteThatContractType() {
		ctx.tryAndKeep(() -> service.delete(contractTypeId));
	}

	@Then("[Ct.D.3] a business error happens with an explaining message")
	public void thenABusinessErrorHappensWithAnExplainingMessage() {
		ctx.assertBusinessExceptionWithMessage();
	}
}