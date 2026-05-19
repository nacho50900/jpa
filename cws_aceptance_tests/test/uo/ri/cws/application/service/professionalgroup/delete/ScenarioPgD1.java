package uo.ri.cws.application.service.professionalgroup.delete;

import static org.junit.jupiter.api.Assertions.assertNull;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.professionalgroup.ProfessionalGroupCrudService;
import uo.ri.cws.application.service.util.dbfixture.Db;
import uo.ri.cws.application.service.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.util.dbfixture.records.TProfessionalGroupsRecord;
import uo.ri.util.exception.BusinessException;

/**
 * Scenario: [Pg.D.1] Delete a professional group with no contracts
 */
public class ScenarioPgD1 {
    private ProfessionalGroupCrudService service = Factories.service.forProfessionalGroupCrudService();
	private TProfessionalGroupsRecord professionalGroup;

    @Given("[Pg.D.1] a professional group with no contracts")
    public void givenAProfessionalGroupWithNoContracts() {
    	professionalGroup = DbFixtures.aProfessionalGroup();
    }

    @When("[Pg.D.1] I delete that professional group")
    public void whenIDeleteThatProfessionalGroup() throws BusinessException {
        service.delete( professionalGroup.name );
    }

    @Then("[Pg.D.1] The professional group is removed from the system")
    public void thenTheProfessionalGroupIsRemovedFromTheSystem() {
        TProfessionalGroupsRecord loaded = 
        		Db.findById(TProfessionalGroupsRecord.class, professionalGroup.id);
        
        assertNull( loaded );
    }
}