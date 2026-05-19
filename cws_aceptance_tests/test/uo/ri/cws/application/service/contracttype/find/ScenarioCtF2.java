package uo.ri.cws.application.service.contracttype.find;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.contracttype.ContractTypeCrudService;
import uo.ri.cws.application.service.contracttype.ContractTypeCrudService.ContractTypeDto;
import uo.ri.cws.application.service.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.util.dbfixture.records.TContractTypesRecord;
import uo.ri.util.exception.BusinessException;

/**
 * Scenario: [Ct.F.2] Find an existent contract type
 */
public class ScenarioCtF2 {
    private ContractTypeCrudService service = Factories.service.forContractTypeCrudService();
    private TContractTypesRecord contractType;
    private Optional<ContractTypeDto> result;

    @Given("[Ct.F.2] a contract type with a name")
    public void givenContractTypeWithName() {
        contractType = DbFixtures.aContractType();
    }

    @When("[Ct.F.2] I search a contract type by that name")
    public void whenISearchContractTypeByName() throws BusinessException {
        result = service.findByName(contractType.name);
    }

    @Then("[Ct.F.2] contract type is found")
    public void thenContractTypeIsFound() {
        assertTrue(result.isPresent());
        ContractTypeDto dto = result.get();
        assertEquals( contractType.name, dto.name );
        assertEquals( contractType.compensationDaysPerYear, dto.compensationDays );
        assertEquals( contractType.id, dto.id );
    }
}