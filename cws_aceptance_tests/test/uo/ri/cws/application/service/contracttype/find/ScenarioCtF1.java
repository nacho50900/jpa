package uo.ri.cws.application.service.contracttype.find;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.contracttype.ContractTypeCrudService;
import uo.ri.cws.application.service.contracttype.ContractTypeCrudService.ContractTypeDto;
import uo.ri.util.exception.BusinessException;

/**
 * Scenario: [Ct.F.1] Find a non existent contract type
 */
public class ScenarioCtF1 {
    private ContractTypeCrudService service = Factories.service.forContractTypeCrudService();
    private Optional<ContractTypeDto> result;

    @When("[Ct.F.1] I search a non existent contract type")
    public void whenISearchNonExistentContractType() throws BusinessException {
        result = service.findByName("NON_EXISTENT_CONTRACT_TYPE");
    }

    @Then("[Ct.F.1] contract type is not found")
    public void thenContractTypeIsNotFound() {
        assertTrue(result.isEmpty());
    }
}