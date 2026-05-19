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
import uo.ri.cws.application.service.util.dbfixture.records.TProfessionalGroupsRecord;
import uo.ri.cws.application.service.util.dtobuilders.ContractDtoBuilder;

/**
 * Scenario Outline: [C.A.12] Try to add a contract with wrong nif
 */
public class ScenarioCA12 {
    private ExceptionBox ctx = new ExceptionBox();
    private ContractCrudService service = Factories.service.forContractCrudService();

    @When("[C.A.12] I try to add a contract with wrong nif {string}")
    public void whenITryToAddAContractWithWrongNif(String nif) {
        
        // Handle the different parameter values from the Examples table
        String mechanicNif = "null".equals(nif) ? null : nif;
        
    	TProfessionalGroupsRecord pg = DbFixtures.aProfessionalGroup();
        TContractTypesRecord ct = DbFixtures.aContractType();

        ContractDto contract = new ContractDtoBuilder()
        		.forMechanicNif( mechanicNif ) // <--- This is the point of the test
        		.forContractTypeName(ct.name)
        		.forProfessionalGroupName(pg.name)
        		.withAnnualSalary(25000.0)
        		.withState("IN_FORCE") 
        		.withStartDate( LocalDate.now().plusMonths( 1 ) )
        		.build();

        ctx.tryAndKeep(() -> service.create(contract));
    }

    @Then("[C.A.12] argument is rejected with an explaining message")
    public void thenArgumentIsRejectedWithAnExplainingMessage() {
        ctx.assertIllegalArgumentExceptionWithMessage();
    }
}