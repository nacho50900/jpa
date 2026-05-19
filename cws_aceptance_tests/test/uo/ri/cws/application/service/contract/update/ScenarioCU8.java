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
 * Scenario: [C.U.8] Try to update a contract with null id
 */
public class ScenarioCU8 {
	private ExceptionBox ctx = new ExceptionBox();
	private ContractCrudService service = Factories.service.forContractCrudService();

	@When("[C.U.8] I try to update a contract with null id")
	public void whenITryToUpdateAContractWithNullId() {
		ContractDto dto = new ContractDtoBuilder()
				.withId(null)
				.build();

		ctx.tryAndKeep(() -> service.update(dto));
	}

	@Then("[C.U.8] argument is rejected with an explaining message")
	public void thenArgumentIsRejectedWithAnExplainingMessage() {
		ctx.assertIllegalArgumentExceptionWithMessage();
	}
}