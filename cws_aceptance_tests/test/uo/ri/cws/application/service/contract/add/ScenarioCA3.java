package uo.ri.cws.application.service.contract.add;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.contract.ContractCrudService;
import uo.ri.cws.application.service.contract.ContractCrudService.ContractDto;
import uo.ri.cws.application.service.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.util.dbfixture.records.TContractTypesRecord;
import uo.ri.cws.application.service.util.dbfixture.records.TMechanicsRecord;
import uo.ri.cws.application.service.util.dbfixture.records.TProfessionalGroupsRecord;
import uo.ri.cws.application.service.util.dtobuilders.ContractDtoBuilder;
import uo.ri.util.exception.BusinessException;

/**
 * Scenario: [C.A.3] Hire an existing mechanic with previous contract terminated
 */
public class ScenarioCA3 {
    private ContractCrudService service = Factories.service.forContractCrudService();
    private TMechanicsRecord mechanic;
    private ContractDto addedContract;
	private TContractTypesRecord contractType;
	private TProfessionalGroupsRecord professionalGroup;

    @Given("[C.A.3] a mechanic with a previous contract terminated")
    public void givenAMechanicWithAPreviousContractTerminated() {
        mechanic = DbFixtures.aMechanicWithTerminatedContract(); /* terminated */

        contractType = DbFixtures.aContractType();
    	professionalGroup = DbFixtures.aProfessionalGroup();
    }

    @When("[C.A.3] I hire that mechanic")
    public void whenIHireThatMechanic() throws BusinessException {
        ContractDto contract = new ContractDtoBuilder()
        		.forMechanicNif(mechanic.nif)
        		.forContractTypeName(contractType.name)
        		.forProfessionalGroupName(professionalGroup.name)
        		.withAnnualSalary(25000.0)
        		.withState("IN_FORCE") 
        		.withStartDate( LocalDate.now() )
        		.build();
        
        addedContract = service.create(contract);
    }

    @Then("[C.A.3] a contract in force for that mechanic is created")
    public void thenAContractInForceForThatMechanicIsCreated() {
        assertNotNull(addedContract);
        assertNotNull(addedContract.id);
        assertEquals("IN_FORCE", addedContract.state);
        assertEquals(mechanic.nif, addedContract.mechanic.nif);
    }

}