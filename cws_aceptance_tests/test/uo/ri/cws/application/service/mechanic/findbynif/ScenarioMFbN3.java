package uo.ri.cws.application.service.mechanic.findbynif;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.mechanic.MechanicCrudService;
import uo.ri.cws.application.service.util.ExceptionBox;

/**
 * Scenario: [M.FbN.3] Try to find a mechanic with null argument
 *   When [M.FbN.3] I try to find a mechanic with null nif
 *   Then [M.FbN.3] argument is rejected with an explaining message
 */
public class ScenarioMFbN3 {
    private MechanicCrudService service = Factories.service.forMechanicCrudService();
    private ExceptionBox ctx = new ExceptionBox();

    @When("[M.FbN.3] I try to find a mechanic with null nif")
    public void whenITryToFindMechanicWithNullNif() {
        ctx.tryAndKeep(() -> service.findByNif(null));
    }

    @Then("[M.FbN.3] argument is rejected with an explaining message")
    public void thenArgumentIsRejectedWithMessage() {
    	ctx.assertIllegalArgumentExceptionWithMessage();
    }
}
