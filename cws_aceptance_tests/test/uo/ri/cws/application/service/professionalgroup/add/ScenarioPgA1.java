package uo.ri.cws.application.service.professionalgroup.add;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.professionalgroup.ProfessionalGroupCrudService;
import uo.ri.cws.application.service.professionalgroup.ProfessionalGroupCrudService.ProfessionalGroupDto;
import uo.ri.cws.application.service.util.Asserts;
import uo.ri.cws.application.service.util.dbfixture.Db;
import uo.ri.cws.application.service.util.dbfixture.records.TProfessionalGroupsRecord;
import uo.ri.cws.application.service.util.dtobuilders.ProfessionalGroupDtoBuilder;
import uo.ri.util.exception.BusinessException;

/**
 * Scenario: [Pg.A.1] Add a new professional group
 */
public class ScenarioPgA1 {
    private ProfessionalGroupCrudService service = Factories.service.forProfessionalGroupCrudService();
    private ProfessionalGroupDto newProfessionalGroup;
    private ProfessionalGroupDto generated;

    @When("[Pg.A.1] I register a new professional group")
    public void whenIRegisterANewProfessionalGroup() throws BusinessException {
        newProfessionalGroup = new ProfessionalGroupDtoBuilder().build();
        generated = service.create(newProfessionalGroup);
    }

    @Then("[Pg.A.1] The professional group is added")
    public void thenTheProfessionalGroupIsAdded() {
		// checks on the returned object
    	assertNotNull(generated.id);
    	assertFalse(generated.id.isEmpty());
		assertEquals(1L, generated.version);
		
        assertEquals(newProfessionalGroup.name, generated.name);
        assertEquals(newProfessionalGroup.trienniumPayment, generated.trienniumPayment, 0.01);
        assertEquals(newProfessionalGroup.productivityRate, generated.productivityRate, 0.01);
        
        // checks on the database
        TProfessionalGroupsRecord loaded = Db.findById(TProfessionalGroupsRecord.class, generated.id);
        assertEquals(generated.name, loaded.name);
        assertEquals(generated.trienniumPayment, loaded.trienniumPayment, 0.01);
        assertEquals(generated.productivityRate, loaded.productivityRate, 0.01);
        assertEquals(generated.version, loaded.version);
        Asserts.isNow(loaded.createdAt);
        Asserts.isNow(loaded.updatedAt);
    }
}