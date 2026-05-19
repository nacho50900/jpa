package uo.ri.cws.application.service.contract.find;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.contract.ContractCrudService;
import uo.ri.cws.application.service.contract.ContractCrudService.ContractSummaryDto;
import uo.ri.util.exception.BusinessException;

/**
 * Feature: Find contracts
 * Scenario: [C.F.1] Find all contracts when there are none
 */
public class ScenarioCF1 {
	private ContractCrudService service = Factories.service.forContractCrudService();
	private List<ContractSummaryDto> result;

	@When("[C.F.1] I search for all contracts")
	public void whenISearchForAllContracts() throws BusinessException {
		result = service.findAll();
	}

	@Then("[C.F.1] List of contracts summary is empty")
	public void thenListOfContractsSummaryIsEmpty() {
		assertTrue(result.isEmpty());
	}
}