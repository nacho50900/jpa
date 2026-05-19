package uo.ri.cws.application.service.professionalgroup.find;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
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
 * Scenario: [Pg.F.1] Find all existent professional groups
 */
public class ScenarioPgF1 {
    private ProfessionalGroupCrudService service = Factories.service.forProfessionalGroupCrudService();
    private List<ProfessionalGroupDto> result;
	private List<TProfessionalGroupsRecord> registered;

    @Given("[Pg.F.1] Several registered professional groups")
    public void givenSeveralRegisteredProfessionalGroups() {
    	registered = List.of(
		        DbFixtures.aProfessionalGroup(),
		        DbFixtures.aProfessionalGroup(),
		        DbFixtures.aProfessionalGroup()
	        );
    }

    @When("[Pg.F.1] I find for all professional groups")
    public void whenIFindForAllProfessionalGroups() throws BusinessException {
        result = service.findAll();
    }

    @Then("[Pg.F.1] all professional groups are found")
    public void thenAllProfessionalGroupsAreFound() {
        assertEquals(registered.size(), result.size());
        
        for (TProfessionalGroupsRecord expected : registered) {
        	Optional<ProfessionalGroupDto> od = findInResult(expected.id);
        	assertTrue(od.isPresent());
        	
        	ProfessionalGroupDto dto = od.get();
        	assertEquals(expected.id, dto.id);
        	assertEquals(expected.name, dto.name);
        	assertEquals(expected.trienniumPayment, dto.trienniumPayment);
        	assertEquals(expected.productivityRate, dto.productivityRate, 0.001);
        }
    }

	private Optional<ProfessionalGroupDto> findInResult(String id) {
		return result.stream()
			.filter(dto -> dto.id.equals(id))
			.findFirst();
	}
}