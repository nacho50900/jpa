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
 * Scenario: [C.A.7] Try to add a contract with end date earlier than start date
 */
public class ScenarioCA7 {
    private ExceptionBox ctx = new ExceptionBox();
    private ContractCrudService service = Factories.service.forContractCrudService();

    @When("[C.A.7] I try to add a contract with end date not later than start date")
    public void whenITryToAddAContractWithEndDateNotLaterThanStartDate() {
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
        		.withStartDate( LocalDate.now().plusMonths( 1 ) ) // start date in the future
        		.withEndDate( LocalDate.now() )
        		.build();

        ctx.tryAndKeep(() -> service.create(contract));
    }

    @Then("[C.A.7] a business error happens with an explaining message")
    public void thenABusinessErrorHappensWithAnExplainingMessage() {
        ctx.assertBusinessExceptionWithMessage();
    }
}