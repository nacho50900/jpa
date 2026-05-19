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
 * Scenario: [Pg.U.5] Try to update a professional group with negative productivity plus
 */
public class ScenarioPgU5 {
    private ExceptionBox ctx = new ExceptionBox();
    private ProfessionalGroupCrudService service = Factories.service.forProfessionalGroupCrudService();
	private TProfessionalGroupsRecord original;

    @Given("[Pg.U.5] a registered professional group")
    public void givenARegisteredProfessionalGroup() {
        original = DbFixtures.aProfessionalGroup();
    }

    @When("[Pg.U.5] I try to update a professional group with negative productivity plus")
    public void whenITryToUpdateAProfessionalGroupWithNegativeProductivityPlus() {
        ProfessionalGroupDto dto = new ProfessionalGroupDtoBuilder()
                .withId(original.id)
                .withName(original.name)
                .withTriennium(original.trienniumPayment)
                .withProductivityRate( -0.01 ) // negative productivity plus
                .withVersion(original.version)
                .build();

        ctx.tryAndKeep(() -> service.update(dto));
    }

    @Then("[Pg.U.5] argument is rejected with an explaining message")
    public void thenArgumentIsRejectedWithAnExplainingMessage() {
        ctx.assertIllegalArgumentExceptionWithMessage();
    }
}