
package uo.ri.cws.application.service.mechanic.delete;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.mechanic.MechanicCrudService;
import uo.ri.cws.application.service.util.ExceptionBox;

public class ScenarioMD5 {
	private ExceptionBox ctx = new ExceptionBox();
	private MechanicCrudService service = Factories.service
			.forMechanicCrudService();

	@When("[M.D.5] I try to remove a mechanic with null argument")
	public void whenITryToRemoveAMechanicWithNullArgument() {
		ctx.tryAndKeep(() -> service.delete(null));
	}

	@Then("[M.D.5] argument is rejected with an explaining message")
	public void thenArgumentIsRejectedWithAnExplainingMessage() {
		ctx.assertIllegalArgumentExceptionWithMessage();
	}
}
