package uo.ri.cws.application.service.professionalgroup.add;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.professionalgroup.ProfessionalGroupCrudService;
import uo.ri.cws.application.service.professionalgroup.ProfessionalGroupCrudService.ProfessionalGroupDto;
import uo.ri.cws.application.service.util.ExceptionBox;
import uo.ri.cws.application.service.util.dtobuilders.ProfessionalGroupDtoBuilder;

/**
 * Scenario: [Pg.A.5] Try to add a professional group with negative productivity plus
 */
public class ScenarioPgA5 {
    private ExceptionBox ctx = new ExceptionBox();
    private ProfessionalGroupCrudService service = Factories.service.forProfessionalGroupCrudService();

    @When("[Pg.A.5] I try to add a professional group with negative productivity plus")
    public void whenITryToAddAProfessionalGroupWithNegativeProductivityPlus() {
        ProfessionalGroupDto dto = new ProfessionalGroupDtoBuilder()
                .withProductivityRate(-1.0)
                .build();

        ctx.tryAndKeep(() -> service.create(dto));
    }

    @Then("[Pg.A.5] argument is rejected with an explaining message")
    public void thenArgumentIsRejectedWithAnExplainingMessage() {
        ctx.assertIllegalArgumentExceptionWithMessage();
    }
}