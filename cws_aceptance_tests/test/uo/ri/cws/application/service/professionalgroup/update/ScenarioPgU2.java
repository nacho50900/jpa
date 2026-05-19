package uo.ri.cws.application.service.professionalgroup.update;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.professionalgroup.ProfessionalGroupCrudService;
import uo.ri.cws.application.service.professionalgroup.ProfessionalGroupCrudService.ProfessionalGroupDto;
import uo.ri.cws.application.service.util.ExceptionBox;
import uo.ri.cws.application.service.util.dtobuilders.ProfessionalGroupDtoBuilder;

/**
 * Scenario: [Pg.U.2] Try to update a non existent professional group
 */
public class ScenarioPgU2 {
    private ExceptionBox ctx = new ExceptionBox();
    private ProfessionalGroupCrudService service = Factories.service.forProfessionalGroupCrudService();

    @When("[Pg.U.2] I try to update a non existent professional group")
    public void whenITryToUpdateANonExistentProfessionalGroup() {
        ProfessionalGroupDto dto = new ProfessionalGroupDtoBuilder()
                .withId("non-existent-id")
                .build();

        ctx.tryAndKeep(() -> service.update(dto));
    }

    @Then("[Pg.U.2] a business error happens with an explaining message")
    public void thenABusinessErrorHappensWithAnExplainingMessage() {
        ctx.assertBusinessExceptionWithMessage();
    }
}