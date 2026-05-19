package uo.ri.cws.application.service.professionalgroup.delete;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.professionalgroup.ProfessionalGroupCrudService;
import uo.ri.cws.application.service.util.ExceptionBox;

/**
 * Scenario: [Pg.D.3] Try to del a non existent professional group
 */
public class ScenarioPgD3 {
    private ExceptionBox ctx = new ExceptionBox();
    private ProfessionalGroupCrudService service = Factories.service.forProfessionalGroupCrudService();

    @When("[Pg.D.3] I try to del a non existent professional group")
    public void whenITryToDelANonExistentProfessionalGroup() {
        ctx.tryAndKeep(() -> 
        	  service.delete("NON_EXISTENT_PROFESSIONAL_GROUP")
        	);
    }

    @Then("[Pg.D.3] a business error happens with an explaining message")
    public void thenABusinessErrorHappensWithAnExplainingMessage() {
        ctx.assertBusinessExceptionWithMessage();
    }
}