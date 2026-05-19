package uo.ri.cws.application.service.contract.add;

import java.time.LocalDate;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.contract.ContractCrudService;
import uo.ri.cws.application.service.contract.ContractCrudService.ContractDto;
import uo.ri.cws.application.service.util.ExceptionBox;
import uo.ri.cws.application.service.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.util.dbfixture.records.TMechanicsRecord;
import uo.ri.cws.application.service.util.dtobuilders.ContractDtoBuilder;

/**
 * Scenario Outline: [C.A.13] Try to add a contract with wrong values
 */
public class ScenarioCA13 {
    private ExceptionBox ctx = new ExceptionBox();
    private ContractCrudService service = Factories.service.forContractCrudService();

    @When("[C.A.13] I try to add a contract with wrong fields {string} {string} {double}")
    public void whenITryToAddAContractWithWrongFields(
    		String typeName, String profGroupName, double annualWage) {
    	
        TMechanicsRecord m = DbFixtures.aMechanic();

        ContractDto contract = new ContractDtoBuilder()
        		.forMechanicNif( m.nif )
        		.withState("IN_FORCE") 
        		.withStartDate( LocalDate.now().plusMonths( 1 ) )
        		.forContractTypeName( typeName ) 			// <-- This is 
        		.forProfessionalGroupName( profGroupName )  // <-- This is 
        		.withAnnualSalary( annualWage ) 			// <-- This is 
        		.build();

        ctx.tryAndKeep(() -> service.create(contract));
    }

    @Then("[C.A.13] argument is rejected with an explaining message")
    public void thenArgumentIsRejectedWithAnExplainingMessage() {
        ctx.assertIllegalArgumentExceptionWithMessage();
    }
}