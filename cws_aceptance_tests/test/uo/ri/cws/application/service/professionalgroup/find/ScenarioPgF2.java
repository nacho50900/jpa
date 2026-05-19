package uo.ri.cws.application.service.professionalgroup.find;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.professionalgroup.ProfessionalGroupCrudService;
import uo.ri.cws.application.service.professionalgroup.ProfessionalGroupCrudService.ProfessionalGroupDto;
import uo.ri.cws.application.service.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.util.dbfixture.records.TProfessionalGroupsRecord;
import uo.ri.util.exception.BusinessException;

/**
 * Scenario: [Pg.F.2] Find an existent professional group
 */
public class ScenarioPgF2 {
    private ProfessionalGroupCrudService service = Factories.service.forProfessionalGroupCrudService();
    private TProfessionalGroupsRecord professionalGroup;
    private Optional<ProfessionalGroupDto> result;

    @Given("[Pg.F.2] Several registered professional groups")
    public void givenSeveralRegisteredProfessionalGroups() {
        // Register multiple professional groups
        DbFixtures.aProfessionalGroup();
        professionalGroup = DbFixtures.aProfessionalGroup();
        DbFixtures.aProfessionalGroup();
    }

    @When("[Pg.F.2] I find by name for a specific existing professional group")
    public void whenIFindByNameForASpecificExistingProfessionalGroup() throws BusinessException {
        result = service.findByName(professionalGroup.name);
    }

    @Then("[Pg.F.2] the professional group is found")
    public void thenTheProfessionalGroupIsFound() {
        assertTrue(result.isPresent());
        ProfessionalGroupDto dto = result.get();
        assertEquals(professionalGroup.name, dto.name);
        assertEquals(professionalGroup.trienniumPayment, dto.trienniumPayment);
        assertEquals(professionalGroup.productivityRate, dto.productivityRate, 0.001);
        assertEquals(professionalGroup.id, dto.id);
    }
}