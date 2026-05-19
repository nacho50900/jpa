package uo.ri.cws.application.service.contracttype.update;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.contracttype.ContractTypeCrudService;
import uo.ri.cws.application.service.contracttype.ContractTypeCrudService.ContractTypeDto;
import uo.ri.cws.application.service.util.ExceptionBox;
import uo.ri.cws.application.service.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.util.dbfixture.records.TContractTypesRecord;
import uo.ri.cws.application.service.util.dtobuilders.ContractTypeDtoBuilder;

/**
 * Feature: Update a contract type
 * Scenario: [Ct.U.5] Try to update a contract type in the while (wrong version)
 */
public class ScenarioCtU5 {
	private ExceptionBox ctx = new ExceptionBox();
	private ContractTypeCrudService service = Factories.service.forContractTypeCrudService();
	private TContractTypesRecord contractType;

	@Given("[Ct.U.5] a registered contract type")
	public void givenARegisteredContractType() {
		contractType = DbFixtures.aContractType();
	}

	@When("[Ct.U.5] I try to update a contract type updated in the while")
	public void whenITryToUpdateAContractTypeUpdatedInTheWhile() {
		ContractTypeDto dto = new ContractTypeDtoBuilder()
				.withId(contractType.id)
				.withName("UpdatedName")
				.withVersion(contractType.version - 1) // simulates old version
				.build();

		ctx.tryAndKeep(() -> service.update(dto));
	}

	@Then("[Ct.U.5] a business error happens with an explaining message")
	public void thenABusinessErrorHappensWithAnExplainingMessage() {
		ctx.assertBusinessExceptionWithMessage();
	}
}