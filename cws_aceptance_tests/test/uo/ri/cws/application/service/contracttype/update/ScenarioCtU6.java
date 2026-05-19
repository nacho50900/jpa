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
 * Scenario Outline: [Ct.U.6] Try to update a contract type with empty name
 */
public class ScenarioCtU6 {
	private ExceptionBox ctx = new ExceptionBox();
	private ContractTypeCrudService service = Factories.service.forContractTypeCrudService();

	@When("[Ct.U.6] I try to update a contract type with {string} name")
	public void whenITryToUpdateAContractTypeWithName(String name) {
		ContractTypeDto dto = new ContractTypeDtoBuilder().build();
		
		// Handle the different parameter values from the Examples table
		dto.name = "null".equals(name) ? null : name;

		ctx.tryAndKeep(() -> service.update(dto));
	}

	@Then("[Ct.U.6] argument is rejected with an explaining message")
	public void thenArgumentIsRejectedWithAnExplainingMessage() {
		ctx.assertIllegalArgumentExceptionWithMessage();
	}
}