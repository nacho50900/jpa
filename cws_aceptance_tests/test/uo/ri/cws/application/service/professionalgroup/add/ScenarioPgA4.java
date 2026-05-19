package uo.ri.cws.application.service.professionalgroup.add;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.professionalgroup.ProfessionalGroupCrudService;
import uo.ri.cws.application.service.professionalgroup.ProfessionalGroupCrudService.ProfessionalGroupDto;
import uo.ri.cws.application.service.util.ExceptionBox;
import uo.ri.cws.application.service.util.dtobuilders.ProfessionalGroupDtoBuilder;

/**
 * Scenario: [Pg.A.4] Try to add a professional group with negative triennium
 */
public class ScenarioPgA4 {
    private ExceptionBox ctx = new ExceptionBox();
    private ProfessionalGroupCrudService service = Factories.service.forProfessionalGroupCrudService();

    @When("[Pg.A.4] I try to add a professional group with negative triennium")
    public void whenITryToAddAProfessionalGroupWithNegativeTriennium() {
        ProfessionalGroupDto dto = new ProfessionalGroupDtoBuilder()
                .withTriennium(-1.0)
                .build();

        ctx.tryAndKeep(() -> service.create(dto));
    }

    @Then("[Pg.A.4] argument is rejected with an explaining message")
    public void thenArgumentIsRejectedWithAnExplainingMessage() {
        ctx.assertIllegalArgumentExceptionWithMessage();
    }
}