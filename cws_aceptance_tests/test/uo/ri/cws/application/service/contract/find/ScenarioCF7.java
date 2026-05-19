package uo.ri.cws.application.service.contract.find;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.contract.ContractCrudService;
import uo.ri.cws.application.service.util.ExceptionBox;

/**
 * Feature: Find contracts
 * Scenario: [C.F.7] Try to find a contract for a mechanic with null arg
 */
public class ScenarioCF7 {
	private ContractCrudService service = Factories.service.forContractCrudService();
	private ExceptionBox ctx = new ExceptionBox();

	@When("[C.F.7] I try to find a contract for a mechanic with null id")
	public void whenITryToFindAContractForAMechanicWithNullId() {
		ctx.tryAndKeep(() -> service.findByMechanicNif(null));
	}

	@Then("[C.F.7] argument is rejected with an explaining message")
	public void thenArgumentIsRejectedWithAnExplainingMessage() {
		ctx.assertIllegalArgumentExceptionWithMessage();
	}
}