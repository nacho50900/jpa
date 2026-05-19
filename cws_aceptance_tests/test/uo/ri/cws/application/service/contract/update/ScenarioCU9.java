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
 * Scenario Outline: [C.U.9] Try to update a contract with wrong id
 */
public class ScenarioCU9 {
	private ExceptionBox ctx = new ExceptionBox();
	private ContractCrudService service = Factories.service.forContractCrudService();

	@When("[C.U.9] I try to update a contract with id {string}")
	public void whenITryToUpdateAContractWithId(String ident) {
		ContractDto dto = new ContractDtoBuilder().build();
		
		// Handle the different parameter values from the Examples table
		final String contractId = switch (ident) {
			case "null" -> null;
			default -> ident;
		};
		
		dto.id = contractId;

		ctx.tryAndKeep(() -> service.update(dto));
	}

	@Then("[C.U.9] argument is rejected with an explaining message")
	public void thenArgumentIsRejectedWithAnExplainingMessage() {
		ctx.assertIllegalArgumentExceptionWithMessage();
	}
}