package uo.ri.cws.application.service.professionalgroup.add;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.professionalgroup.ProfessionalGroupCrudService;
import uo.ri.cws.application.service.util.ExceptionBox;

/**
 * Scenario: [Pg.A.3] Try to add a professional group with null arg
 */
public class ScenarioPgA3 {
    private ExceptionBox ctx = new ExceptionBox();
    private ProfessionalGroupCrudService service = Factories.service.forProfessionalGroupCrudService();

    @When("[Pg.A.3] I try to add a professional group with null argument")
    public void whenITryToAddAProfessionalGroupWithNullArgument() {
        ctx.tryAndKeep(() -> service.create(null));
    }

    @Then("[Pg.A.3] argument is rejected with an explaining message")
    public void thenArgumentIsRejectedWithAnExplainingMessage() {
        ctx.assertIllegalArgumentExceptionWithMessage();
    }
}