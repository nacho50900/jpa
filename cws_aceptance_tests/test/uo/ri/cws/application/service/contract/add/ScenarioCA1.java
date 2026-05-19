package uo.ri.cws.application.service.contract.add;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.contract.ContractCrudService;
import uo.ri.cws.application.service.contract.ContractCrudService.ContractDto;
import uo.ri.cws.application.service.util.Asserts;
import uo.ri.cws.application.service.util.TaxRate;
import uo.ri.cws.application.service.util.dbfixture.Db;
import uo.ri.cws.application.service.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.util.dbfixture.records.TContractTypesRecord;
import uo.ri.cws.application.service.util.dbfixture.records.TContractsRecord;
import uo.ri.cws.application.service.util.dbfixture.records.TMechanicsRecord;
import uo.ri.cws.application.service.util.dbfixture.records.TProfessionalGroupsRecord;
import uo.ri.cws.application.service.util.dtobuilders.ContractDtoBuilder;
import uo.ri.util.date.Dates;
import uo.ri.util.exception.BusinessException;

/**
 * Scenario: [C.A.1] Hire an existing mechanic with no previous contract in force
 */
public class ScenarioCA1 {
	private ContractCrudService service = Factories.service.forContractCrudService();
    private TMechanicsRecord mechanic;
	private TContractTypesRecord contractType;
	private TProfessionalGroupsRecord professionalGroup;

    private ContractDto generated;
	private double annualSalary;
	private double taxRate;

    @Given("[C.A.1] a mechanic with no contract")
    public void givenAMechanicWithNoContract() {
    	contractType = DbFixtures.aContractType();
    	professionalGroup = DbFixtures.aProfessionalGroup();
        mechanic = DbFixtures.aMechanic();
    }

    @When("[C.A.1] I hire that mechanic with an annual {double}")
    public void whenIHireThatMechanic(double salary) throws BusinessException {
        annualSalary = salary;
        taxRate = TaxRate.forSalary(salary);

        ContractDto contract = new ContractDtoBuilder()
        		.forMechanicNif(mechanic.nif)
        		.forContractTypeName(contractType.name)
        		.forProfessionalGroupName(professionalGroup.name)
        		.withAnnualSalary( annualSalary )
        		.withState("IN_FORCE") 
        		.withStartDate( LocalDate.now() )
        		.build();
        
        generated = service.create(contract);
    }

    @Then("[C.A.1] a contract in force for that mechanic is created")
    public void thenAContractInForceForThatMechanicIsCreated() {
    	// checks on the returned object
        assertNotNull(generated);
        assertNotNull(generated.id);
        assertFalse(generated.id.isBlank());
        assertEquals(1L, generated.version);

        assertEquals("IN_FORCE", generated.state);
        assertEquals(mechanic.nif, generated.mechanic.nif);
        assertEquals(contractType.name, generated.contractType.name);
        assertEquals(professionalGroup.name, generated.professionalGroup.name);
        assertEquals(annualSalary, generated.annualBaseSalary);
        assertEquals(taxRate, generated.taxRate);
        assertEquals(0.0, generated.settlement);
        
        // checks on the database
        TContractsRecord loaded = Db.findById(TContractsRecord.class, generated.id);
        assertNotNull(loaded);
        Asserts.recordMatchesDto(generated, loaded);
        
		Asserts.isNow( loaded.createdAt );
		Asserts.isNow( loaded.updatedAt );
    }

    @And("[C.A.1] start date is first day next month")
    public void andStartDateIsFirstDayNextMonth() {
        LocalDate expectedStartDate = Dates.firstDayOfNextMonth();
        assertEquals(expectedStartDate, generated.startDate);
    }
}