package uo.ri.cws.application.service.contracttype.update;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.contracttype.ContractTypeCrudService;
import uo.ri.cws.application.service.contracttype.ContractTypeCrudService.ContractTypeDto;
import uo.ri.cws.application.service.util.ExceptionBox;
import uo.ri.cws.application.service.util.dtobuilders.ContractTypeDtoBuilder;

/**
 * Feature: Update a contract type
 * Scenario: [Ct.U.2] Try to update a non existent contract type
 */
public class ScenarioCtU2 {
	private ExceptionBox ctx = new ExceptionBox();
	private ContractTypeCrudService service = Factories.service.forContractTypeCrudService();

	@When("[Ct.U.2] I try to update a non existent contract type")
	public void whenITryToUpdateANonExistentContractType() {
		ContractTypeDto dto = new ContractTypeDtoBuilder()
				.withId("non-existent-id")
				.build();

		ctx.tryAndKeep(() -> service.update(dto));
	}

	@Then("[Ct.U.2] a business error happens with an explaining message")
	public void thenABusinessErrorHappensWithAnExplainingMessage() {
		ctx.assertBusinessExceptionWithMessage();
	}
}