package uo.ri.cws.application.service.mechanic.add;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.mechanic.MechanicCrudService;
import uo.ri.cws.application.service.util.ExceptionBox;

public class ScenarioMA3 {
	private ExceptionBox ctx = new ExceptionBox();
	private MechanicCrudService service = Factories.service.forMechanicCrudService();

	@When("[M.A.3] I try to add a new mechanic with null argument")
	public void whenITryToAddANewMechanicWithNullArgument() {
		ctx.tryAndKeep(() -> service.create( null ));
	}

	@Then("[M.A.3] argument is rejected with an explaining message")
	public void thenArgumentIsRejectedWithAnExplainingMessage() {
		ctx.assertIllegalArgumentExceptionWithMessage();
	}
}
