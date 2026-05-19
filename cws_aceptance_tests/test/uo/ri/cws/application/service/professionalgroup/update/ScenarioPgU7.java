package uo.ri.cws.application.service.professionalgroup.update;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.professionalgroup.ProfessionalGroupCrudService;
import uo.ri.cws.application.service.professionalgroup.ProfessionalGroupCrudService.ProfessionalGroupDto;
import uo.ri.cws.application.service.util.ExceptionBox;
import uo.ri.cws.application.service.util.dtobuilders.ProfessionalGroupDtoBuilder;

/**
 * Scenario Outline: [Pg.U.7] Try to update a professional group with empty name
 */
public class ScenarioPgU7 {
    private ExceptionBox ctx = new ExceptionBox();
    private ProfessionalGroupCrudService service = Factories.service.forProfessionalGroupCrudService();

    @When("[Pg.U.7] I try to update a professional group with name {string}")
    public void whenITryToUpdateAProfessionalGroupWithName(String name) {
        ProfessionalGroupDto dto = new ProfessionalGroupDtoBuilder().build();
        
        // Handle the different parameter values from the Examples table
        dto.name = "null".equals(name) ? null : name;

        ctx.tryAndKeep(() -> service.update(dto));
    }

    @Then("[Pg.U.7] argument is rejected with an explaining message")
    public void thenArgumentIsRejectedWithAnExplainingMessage() {
        ctx.assertIllegalArgumentExceptionWithMessage();
    }
}