package uo.ri.cws.application.service.professionalgroup.add;

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
 * Scenario: [Pg.A.2] Try to add a repeated professional group
 */
public class ScenarioPgA2 {
    private ExceptionBox ctx = new ExceptionBox();
    private ProfessionalGroupCrudService service = Factories.service.forProfessionalGroupCrudService();
    private String professionalGroupName;

    @Given("[Pg.A.2] a professional group")
    public void givenAProfessionalGroup() {
        TProfessionalGroupsRecord existing = DbFixtures.aProfessionalGroup();
        professionalGroupName = existing.name;
    }

    @When("[Pg.A.2] I try to add a professional group with the same name")
    public void whenITryToAddAProfessionalGroupWithTheSameName() {
        ProfessionalGroupDto dto = new ProfessionalGroupDtoBuilder()
                .withName(professionalGroupName)
                .build();

        ctx.tryAndKeep(() -> service.create(dto));
    }

    @Then("[Pg.A.2] a business error happens with an explaining message")
    public void thenABusinessErrorHappensWithAnExplainingMessage() {
        ctx.assertBusinessExceptionWithMessage();
    }
}