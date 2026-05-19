package uo.ri.cws.application.service.contract.add;

import java.time.LocalDate;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.contract.ContractCrudService;
import uo.ri.cws.application.service.contract.ContractCrudService.ContractDto;
import uo.ri.cws.application.service.util.ExceptionBox;
import uo.ri.cws.application.service.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.util.dbfixture.records.TContractTypesRecord;
import uo.ri.cws.application.service.util.dbfixture.records.TMechanicsRecord;
import uo.ri.cws.application.service.util.dbfixture.records.TProfessionalGroupsRecord;
import uo.ri.cws.application.service.util.dtobuilders.ContractDtoBuilder;

/**
 * Scenario: [C.A.11] Try to add a contract with null end date when mandatory
 */
public class ScenarioCA11 {
    private ExceptionBox ctx = new ExceptionBox();
    private ContractCrudService service = Factories.service.forContractCrudService();

    @When("[C.A.11] I try to add a contract with null end date for FIXED_TERM contract type")
    public void whenITryToAddAContractWithNullEndDateForFixedTermContractType() {
        TMechanicsRecord m = DbFixtures.aMechanic();
    	TProfessionalGroupsRecord pg = DbFixtures.aProfessionalGroup();
    	
    	// Using a fixed term contract type to be able to set an end date
        TContractTypesRecord ct = DbFixtures.aContractTypeFixedTerm();

        ContractDto contract = new ContractDtoBuilder()
        		.forMechanicNif( m.nif )
        		.forContractTypeName(ct.name)
        		.forProfessionalGroupName(pg.name)
        		.withAnnualSalary(25000.0)
        		.withState("IN_FORCE") 
        		.withStartDate( LocalDate.now().plusMonths( 1 ) )
        		.withEndDate( null )	// <--- This is the point of the test
        		.build();

        ctx.tryAndKeep(() -> service.create(contract));
    }

    @Then("[C.A.11] argument is rejected with an explaining message")
    public void thenArgumentIsRejectedWithAnExplainingMessage() {
        ctx.assertIllegalArgumentExceptionWithMessage();
    }
}