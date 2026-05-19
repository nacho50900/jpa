package uo.ri.cws.application.service.contracttype.delete;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.contracttype.ContractTypeCrudService;
import uo.ri.cws.application.service.util.ExceptionBox;

/**
 * Feature: Delete a contract type Scenario Outline: [Ct.D.4] Try to delete a
 * contract type with invalid argument
 */
public class ScenarioCtD4 {
	private ExceptionBox ctx = new ExceptionBox();
	private ContractTypeCrudService service = Factories.service
			.forContractTypeCrudService();

	@When("[Ct.D.4] I try to del a contract type with name {string}")
	public void whenITryToDelAContractTypeWithName(String name) {

		// Handle the different parameter values from the Examples table
		String contractTypeId = "null".equals(name) ? null : name;

		ctx.tryAndKeep(
				() -> service.delete(contractTypeId)
			);
	}

	@Then("[Ct.D.4] argument is rejected with an explaining message")
	public void thenArgumentIsRejectedWithAnExplainingMessage() {
		ctx.assertIllegalArgumentExceptionWithMessage();
	}
}