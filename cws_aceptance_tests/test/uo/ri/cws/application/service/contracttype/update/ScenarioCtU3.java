package uo.ri.cws.application.service.contracttype.update;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.contracttype.ContractTypeCrudService;
import uo.ri.cws.application.service.util.ExceptionBox;

/**
 * Feature: Update a contract type
 * Scenario: [Ct.U.3] Try to update a contract type with null arg
 */
public class ScenarioCtU3 {
	private ExceptionBox ctx = new ExceptionBox();
	private ContractTypeCrudService service = Factories.service.forContractTypeCrudService();

	@When("[Ct.U.3] I try to update a contract type with null arg")
	public void whenITryToUpdateAContractTypeWithNullArg() {
		ctx.tryAndKeep(() -> service.update(null));
	}

	@Then("[Ct.U.3] argument is rejected with an explaining message")
	public void thenArgumentIsRejectedWithAnExplainingMessage() {
		ctx.assertIllegalArgumentExceptionWithMessage();
	}
}