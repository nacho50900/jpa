package uo.ri.cws.application.service.contracttype.delete;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.contracttype.ContractTypeCrudService;
import uo.ri.cws.application.service.util.ExceptionBox;

/**
 * Feature: Delete a contract type
 * Scenario: [Ct.D.2] Try to delete a non existent contract type
 */
public class ScenarioCtD2 {
	private ExceptionBox ctx = new ExceptionBox();
	private ContractTypeCrudService service = Factories.service.forContractTypeCrudService();

	@When("[Ct.D.2] I try to delete a non existent contract type")
	public void whenITryToDelANonExistentContractType() {
		ctx.tryAndKeep(
				() -> service.delete("non-existent-id")
			);
	}

	@Then("[Ct.D.2] a business error happens with an explaining message")
	public void thenABusinessErrorHappensWithAnExplainingMessage() {
		ctx.assertBusinessExceptionWithMessage();
	}
}