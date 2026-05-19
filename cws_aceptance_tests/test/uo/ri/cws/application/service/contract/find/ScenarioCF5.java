package uo.ri.cws.application.service.contract.find;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.contract.ContractCrudService;
import uo.ri.cws.application.service.contract.ContractCrudService.ContractDto;
import uo.ri.util.exception.BusinessException;

/**
 * Feature: Find contracts
 * Scenario: [C.F.5] Find by id a non existent contract
 */
public class ScenarioCF5 {
	private ContractCrudService service = Factories.service.forContractCrudService();
	private Optional<ContractDto> result;

	@When("[C.F.5] I search a non existing contract id")
	public void whenISearchANonExistingContractId() throws BusinessException {
		result = service.findById("non-existent-contract-id");
	}

	@Then("[C.F.5] nothing is found")
	public void thenNothingIsFound() {
		assertTrue(result.isEmpty());
	}
}