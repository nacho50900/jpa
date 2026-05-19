package uo.ri.cws.application.service.professionalgroup.update;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.professionalgroup.ProfessionalGroupCrudService;
import uo.ri.cws.application.service.util.ExceptionBox;

/**
 * Scenario: [Pg.U.6] Try to update a professional group with null arg
 */
public class ScenarioPgU6 {
    private ExceptionBox ctx = new ExceptionBox();
    private ProfessionalGroupCrudService service = Factories.service.forProfessionalGroupCrudService();

    @When("[Pg.U.6] I try to update a professional group with null arg")
    public void whenITryToUpdateAProfessionalGroupWithNullArg() {
        ctx.tryAndKeep(() -> service.update(null));
    }

    @Then("[Pg.U.6] argument is rejected with an explaining message")
    public void thenArgumentIsRejectedWithAnExplainingMessage() {
        ctx.assertIllegalArgumentExceptionWithMessage();
    }
}