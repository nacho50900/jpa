package uo.ri.cws.application.service.contract.update;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.contract.ContractCrudService;
import uo.ri.cws.application.service.contract.ContractCrudService.ContractDto;
import uo.ri.cws.application.service.util.ExceptionBox;
import uo.ri.cws.application.service.util.dtobuilders.ContractDtoBuilder;

/**
 * Feature: Update a contract
 * Scenario: [C.U.3] Try to update a non existing contract
 */
public class ScenarioCU3 {
	private ExceptionBox ctx = new ExceptionBox();
	private ContractCrudService service = Factories.service.forContractCrudService();

	@When("[C.U.3] I try to update a non existing contract")
	public void whenITryToUpdateANonExistingContract() {
		ContractDto dto = new ContractDtoBuilder()
				.withId("non-existent-id")
				.build();

		ctx.tryAndKeep(() -> service.update(dto));
	}

	@Then("[C.U.3] a business error happens with an explaining message")
	public void thenABusinessErrorHappensWithAnExplainingMessage() {
		ctx.assertBusinessExceptionWithMessage();
	}
}