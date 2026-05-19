package uo.ri.cws.application.service.mechanic.findbyid;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.mechanic.MechanicCrudService;
import uo.ri.cws.application.service.util.ExceptionBox;

public class ScenarioMFbI3 {
    private MechanicCrudService service = Factories.service.forMechanicCrudService();
    private ExceptionBox ctx = new ExceptionBox();

    @When("[M.FbI.3] I try to find a mechanic with null argument")
    public void whenITryToFindMechanicWithNullArgument() {
    	ctx.tryAndKeep(() -> service.findById(null));
    }

    @Then("[M.FbI.3] argument is rejected with an explaining message")
    public void thenArgumentIsRejectedWithMessage() {
    	ctx.assertIllegalArgumentExceptionWithMessage();
    }
}
