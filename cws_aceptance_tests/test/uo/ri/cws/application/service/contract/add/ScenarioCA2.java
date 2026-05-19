package uo.ri.cws.application.service.contract.add;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.contract.ContractCrudService;
import uo.ri.cws.application.service.contract.ContractCrudService.ContractDto;
import uo.ri.cws.application.service.util.dbfixture.Db;
import uo.ri.cws.application.service.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.util.dbfixture.records.TContractTypesRecord;
import uo.ri.cws.application.service.util.dbfixture.records.TContractsRecord;
import uo.ri.cws.application.service.util.dbfixture.records.TMechanicsRecord;
import uo.ri.cws.application.service.util.dbfixture.records.TProfessionalGroupsRecord;
import uo.ri.cws.application.service.util.dtobuilders.ContractDtoBuilder;
import uo.ri.util.exception.BusinessException;

/**
 * Scenario: [C.A.2] Hire an existing mechanic with a previous contract in force shorter than a year
 */
public class ScenarioCA2 {
    private ContractCrudService service = Factories.service.forContractCrudService();
    private TMechanicsRecord mechanic;
    private ContractDto newContract;
	private TContractTypesRecord contractType;
	private TProfessionalGroupsRecord professionalGroup;
	private TContractsRecord previousContract;

    @Given("[C.A.2] an already less-than-a-year hired mechanic")
    public void givenAnAlreadyLessThanAYearHiredMechanic() {
        mechanic = DbFixtures.aMechanicWithRecentInForceContract(); /* recent */

        contractType = DbFixtures.aContractType();
    	professionalGroup = DbFixtures.aProfessionalGroup();
        previousContract = Db.findBy(TContractsRecord.class, "mechanic_Id", mechanic.id);
    }

    @When("[C.A.2] I create a new contract for that mechanic")
    public void whenICreateANewContractForThatMechanic() throws BusinessException {
        ContractDto contract = new ContractDtoBuilder()
        		.forMechanicNif(mechanic.nif)
        		.forContractTypeName(contractType.name)
        		.forProfessionalGroupName(professionalGroup.name)
        		.withAnnualSalary(25000.0)
        		.withState("IN_FORCE") 
        		.withStartDate( LocalDate.now() )
        		.build();
        
        newContract = service.create(contract);
    }

    @Then("[C.A.2] there is a new contract in force for that mechanic")
    public void thenThereIsANewContractInForceForThatMechanic() {
        assertNotNull(newContract);
        assertNotNull(newContract.id);
        assertEquals("IN_FORCE", newContract.state);
        assertEquals(mechanic.nif, newContract.mechanic.nif);
    }

    @And("[C.A.2] previous contract is terminated with no settlement")
    public void andPreviousContractIsTerminatedWithNoSettlement() {
    	TContractsRecord updatedPreviousContract = 
    			Db.findById(TContractsRecord.class, previousContract.id);

		assertEquals("TERMINATED", updatedPreviousContract.state);
		assertEquals(0.0, updatedPreviousContract.settlement, 0.01);
    }
}