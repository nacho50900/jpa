package uo.ri.cws.application.service.professionalgroup.find;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.professionalgroup.ProfessionalGroupCrudService;
import uo.ri.cws.application.service.util.ExceptionBox;

/**
 * Scenario Outline: [Pg.F.4] Try to find a professional group with wrong arg
 */
public class ScenarioPgF4 {
    private ProfessionalGroupCrudService service = Factories.service.forProfessionalGroupCrudService();
    private ExceptionBox ctx = new ExceptionBox();

    @When("[Pg.F.4] I try to find a professional group with name {string}")
    public void whenITryToFindAProfessionalGroupWithName(String name) {
 		String pgName = "null".equals(name) ? null : name;
        ctx.tryAndKeep(() -> service.findByName( pgName));
    }

    @Then("[Pg.F.4] argument is rejected with an explaining message")
    public void thenArgumentIsRejectedWithAnExplainingMessage() {
        ctx.assertIllegalArgumentExceptionWithMessage();
    }
}