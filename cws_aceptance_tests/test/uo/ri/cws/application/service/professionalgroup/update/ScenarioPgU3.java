package uo.ri.cws.application.service.professionalgroup.update;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.professionalgroup.ProfessionalGroupCrudService;
import uo.ri.cws.application.service.professionalgroup.ProfessionalGroupCrudService.ProfessionalGroupDto;
import uo.ri.cws.application.service.util.ExceptionBox;
import uo.ri.cws.application.service.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.util.dbfixture.records.TProfessionalGroupsRecord;
import uo.ri.cws.application.service.util.dtobuilders.ProfessionalGroupDtoBuilder;

/**
 * Scenario: [Pg.U.3] Try to update a professional group in the while (wrong version)
 */
public class ScenarioPgU3 {
    private ProfessionalGroupCrudService service = Factories.service.forProfessionalGroupCrudService();
    private ExceptionBox ctx = new ExceptionBox();
	private TProfessionalGroupsRecord original;

    @Given("[Pg.U.3] a registered professional group")
    public void givenARegisteredProfessionalGroup() {
        original = DbFixtures.aProfessionalGroup();
    }

    @When("[Pg.U.3] I try to update that professional group updated in the while")
    public void whenITryToUpdateThatProfessionalGroupUpdatedInTheWhile() {
        ProfessionalGroupDto dto = new ProfessionalGroupDtoBuilder()
                .withId(original.id)
                .withName(original.name)
                .withTriennium(original.trienniumPayment + 100)
                .withProductivityRate(original.productivityRate + 0.05)
                .withVersion(original.version - 1) // simulates old version
                .build();

        ctx.tryAndKeep(() -> service.update(dto));
    }

    @Then("[Pg.U.3] a business error happens with an explaining message")
    public void thenABusinessErrorHappensWithAnExplainingMessage() {
        ctx.assertBusinessExceptionWithMessage();
    }
}