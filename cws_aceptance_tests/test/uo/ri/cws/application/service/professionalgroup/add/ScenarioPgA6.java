package uo.ri.cws.application.service.professionalgroup.add;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.professionalgroup.ProfessionalGroupCrudService;
import uo.ri.cws.application.service.professionalgroup.ProfessionalGroupCrudService.ProfessionalGroupDto;
import uo.ri.cws.application.service.util.ExceptionBox;
import uo.ri.cws.application.service.util.dtobuilders.ProfessionalGroupDtoBuilder;

/**
 * Scenario: [Pg.A.6] Try to add a professional group with empty name
 */
public class ScenarioPgA6 {
    private ExceptionBox ctx = new ExceptionBox();
    private ProfessionalGroupCrudService service = Factories.service.forProfessionalGroupCrudService();

    @When("[Pg.A.6] I try to add a professional group with name {string}")
    public void whenITryToAddAProfessionalGroupWithString(String name) {
    	name = "null".equals(name) ? null : name;
    	
        ProfessionalGroupDto dto = new ProfessionalGroupDtoBuilder()
                .withName(name)
                .build();

        ctx.tryAndKeep(() -> service.create(dto));
    }

    @Then("[Pg.A.6] argument is rejected with an explaining message")
    public void thenArgumentIsRejectedWithAnExplainingMessage() {
        ctx.assertIllegalArgumentExceptionWithMessage();
    }
}