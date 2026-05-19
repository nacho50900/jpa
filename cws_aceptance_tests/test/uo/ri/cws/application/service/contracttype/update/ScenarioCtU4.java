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
 * Scenario: [Ct.U.4] Try to update a contract type with negative days
 */
public class ScenarioCtU4 {
	private ExceptionBox ctx = new ExceptionBox();
	private ContractTypeCrudService service = Factories.service.forContractTypeCrudService();

	@When("[Ct.U.4] I try to update a contract type with negative days")
	public void whenITryToUpdateAContractTypeWithNegativeDays() {
		ContractTypeDto dto = new ContractTypeDtoBuilder()
				.withCompensationDays(-1)
				.build();

		ctx.tryAndKeep(() -> service.update(dto));
	}

	@Then("[Ct.U.4] argument is rejected with an explaining message")
	public void thenArgumentIsRejectedWithAnExplainingMessage() {
		ctx.assertIllegalArgumentExceptionWithMessage();
	}
}