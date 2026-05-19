package uo.ri.cws.application.service.professionalgroup.delete;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.professionalgroup.ProfessionalGroupCrudService;
import uo.ri.cws.application.service.util.ExceptionBox;

/**
 * Scenario Outline: [Pg.D.2] Try to del a professional group with empty argument
 */
public class ScenarioPgD2 {
    private ExceptionBox ctx = new ExceptionBox();
    private ProfessionalGroupCrudService service = Factories.service.forProfessionalGroupCrudService();

    @When("[Pg.D.2] I try to del a professional group with wrong name {string}")
    public void whenITryToDelAProfessionalGroupWithWrongId(String name) {

    	// Handle the different parameter values from the Examples table
        String profGroupName = "null".equals(name) ? null : name;

        ctx.tryAndKeep(() -> service.delete(profGroupName));
    }

    @Then("[Pg.D.2] argument is rejected with an explaining message")
    public void thenArgumentIsRejectedWithAnExplainingMessage() {
        ctx.assertIllegalArgumentExceptionWithMessage();
    }
}