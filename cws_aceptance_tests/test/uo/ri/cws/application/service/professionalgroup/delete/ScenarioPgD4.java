package uo.ri.cws.application.service.professionalgroup.delete;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.professionalgroup.ProfessionalGroupCrudService;
import uo.ri.cws.application.service.util.ExceptionBox;
import uo.ri.cws.application.service.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.util.dbfixture.records.TProfessionalGroupsRecord;

/**
 * Scenario: [Pg.D.4] Try to del a professional group involved in some contract
 */
public class ScenarioPgD4 {
    private ProfessionalGroupCrudService service = Factories.service.forProfessionalGroupCrudService();
    private ExceptionBox ctx = new ExceptionBox();

    private TProfessionalGroupsRecord professionalGroup;

    @Given("[Pg.D.4] a professional group with some contracts")
    public void givenAProfessionalGroupWithSomeContracts() {
    	professionalGroup = DbFixtures.aProfessionalGroupWithContracts();
    }

    @When("[Pg.D.4] I try to delete that professional group")
    public void whenITryToDeleteThatProfessionalGroup() {
        ctx.tryAndKeep(() -> 
        	  service.delete(professionalGroup.name)
           );
    }

    @Then("[Pg.D.4] a business error happens with an explaining message")
    public void thenABusinessErrorHappensWithAnExplainingMessage() {
        ctx.assertBusinessExceptionWithMessage();
    }
}