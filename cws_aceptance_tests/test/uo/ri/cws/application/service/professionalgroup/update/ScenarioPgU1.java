package uo.ri.cws.application.service.professionalgroup.update;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.professionalgroup.ProfessionalGroupCrudService;
import uo.ri.cws.application.service.professionalgroup.ProfessionalGroupCrudService.ProfessionalGroupDto;
import uo.ri.cws.application.service.util.dbfixture.Db;
import uo.ri.cws.application.service.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.util.dbfixture.records.TProfessionalGroupsRecord;
import uo.ri.cws.application.service.util.dtobuilders.ProfessionalGroupDtoBuilder;
import uo.ri.util.exception.BusinessException;

/**
 * Scenario: [Pg.U.1] Update a professional group
 */
public class ScenarioPgU1 {
    private ProfessionalGroupCrudService service = Factories.service.forProfessionalGroupCrudService();
    private TProfessionalGroupsRecord loaded;
	private TProfessionalGroupsRecord original;

    @Given("[Pg.U.1] a registered professional group")
    public void givenARegisteredProfessionalGroup() {
        original = DbFixtures.aProfessionalGroup();
    }

    @When("[Pg.U.1] I update that professional group with new triennium and productivity")
    public void whenIUpdateThatProfessionalGroupWithNewTrienniumAndProductivity() throws BusinessException {
        ProfessionalGroupDto dto = new ProfessionalGroupDtoBuilder()
                .withId(original.id)
                .withName(original.name)
                .withTriennium(original.trienniumPayment + 100)
                .withProductivityRate(original.productivityRate + 0.5)
                .withVersion(original.version)
                .build();

        service.update(dto);
    }

    @Then("[Pg.U.1] Triennium and productivity are updated")
    public void thenTrienniumAndProductivityAreUpdated() {
        loaded = Db.findById(TProfessionalGroupsRecord.class, original.id);
        
        assertEquals(original.name, loaded.name); // name not updated
        assertEquals(original.trienniumPayment + 100, loaded.trienniumPayment);
        assertEquals(original.productivityRate + 0.5, loaded.productivityRate, 0.001);
        
		assertTrue(original.version < loaded.version, "Version must increase");
		assertEquals(original.createdAt, loaded.createdAt);
		assertTrue(original.updatedAt.before(loaded.updatedAt));
    }
}