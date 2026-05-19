package uo.ri.cws.application.service.professionalgroup.find;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.professionalgroup.ProfessionalGroupCrudService;
import uo.ri.cws.application.service.professionalgroup.ProfessionalGroupCrudService.ProfessionalGroupDto;
import uo.ri.util.exception.BusinessException;

/**
 * Scenario: [Pg.F.3] Find a non existent professional group
 */
public class ScenarioPgF3 {
    private ProfessionalGroupCrudService service = Factories.service.forProfessionalGroupCrudService();
    private Optional<ProfessionalGroupDto> result;

    @When("[Pg.F.3] I search a non existing professional group")
    public void whenISearchANonExistingProfessionalGroup() throws BusinessException {
        result = service.findByName("NON_EXISTENT_PROFESSIONAL_GROUP");
    }

    @Then("[Pg.F.3] professional group is not found")
    public void thenProfessionalGroupIsNotFound() {
        assertTrue(result.isEmpty());
    }
}