package uo.ri.cws.application.service.contracttype.add;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.contracttype.ContractTypeCrudService;
import uo.ri.cws.application.service.util.ExceptionBox;

public class ScenarioCtA3 {
	private ExceptionBox ctx = new ExceptionBox();
	private ContractTypeCrudService service = Factories.service.forContractTypeCrudService();

	@When("[Ct.A.3] I try to add a contract type with null argument")
	public void whenITryToAddAContractTypeWithNullArgument() {
		ctx.tryAndKeep(() -> service.create(null));
	}

	@Then("[Ct.A.3] argument is rejected with an explaining message")
	public void thenArgumentIsRejectedWithAnExplainingMessage() {
		ctx.assertIllegalArgumentExceptionWithMessage();
	}
}