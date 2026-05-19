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
 * Scenario: [C.A.4] Try to add a contract for a non existing mechanic
 */
public class ScenarioCA4 {
    private ExceptionBox ctx = new ExceptionBox();
    private ContractCrudService service = Factories.service.forContractCrudService();

    @When("[C.A.4] I try to hire a non existent mechanic")
    public void whenITryToHireANonExistentMechanic() {
        TContractTypesRecord ct = DbFixtures.aContractType();
    	TProfessionalGroupsRecord pg = DbFixtures.aProfessionalGroup();

        ContractDto contract = new ContractDtoBuilder()
        		.forMechanicNif( "DOES-NOT-EXIST" )
        		.forContractTypeName(ct.name)
        		.forProfessionalGroupName(pg.name)
        		.withAnnualSalary(25000.0)
        		.withState("IN_FORCE") 
        		.withStartDate( LocalDate.now() )
        		.build();

        ctx.tryAndKeep(() -> service.create(contract));
    }

    @Then("[C.A.4] a business error happens with an explaining message")
    public void thenABusinessErrorHappensWithAnExplainingMessage() {
        ctx.assertBusinessExceptionWithMessage();
    }
}