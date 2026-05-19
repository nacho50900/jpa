package uo.ri.cws.application.service.contract.find;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.contract.ContractCrudService;
import uo.ri.cws.application.service.util.ExceptionBox;

/**
 * Feature: Find contracts
 * Scenario: [C.F.6] Try to find a contract with null id
 */
public class ScenarioCF6 {
	private ContractCrudService service = Factories.service.forContractCrudService();
	private ExceptionBox ctx = new ExceptionBox();

	@When("[C.F.6] I try to find a contract with null id")
	public void whenITryToFindAContractWithNullId() {
		ctx.tryAndKeep(() -> service.findById(null));
	}

	@Then("[C.F.6] argument is rejected with an explaining message")
	public void thenArgumentIsRejectedWithAnExplainingMessage() {
		ctx.assertIllegalArgumentExceptionWithMessage();
	}
}