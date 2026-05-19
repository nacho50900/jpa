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
 * Scenario: [C.F.8] Find contracts by mechanic id for a non existent mechanic
 */
public class ScenarioCF8 {
	private ContractCrudService service = Factories.service.forContractCrudService();
	private List<ContractSummaryDto> result;

	@When("[C.F.8] I search contracts for a non existent mechanic")
	public void whenISearchContractsForANonExistentMechanic() throws BusinessException {
		result = service.findByMechanicNif("non-existent-mechanic-dni");
	}

	@Then("[C.F.8] List of contracts summary is empty")
	public void thenListOfContractsSummaryIsEmpty() {
		assertTrue(result.isEmpty());
	}
}