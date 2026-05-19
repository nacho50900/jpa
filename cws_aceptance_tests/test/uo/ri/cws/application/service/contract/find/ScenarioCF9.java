package uo.ri.cws.application.service.contract.find;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.contract.ContractCrudService;
import uo.ri.cws.application.service.contract.ContractCrudService.ContractSummaryDto;
import uo.ri.cws.application.service.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.util.dbfixture.records.TMechanicsRecord;
import uo.ri.util.exception.BusinessException;

/**
 * Feature: Find contracts
 * Scenario: [C.F.9] Find contracts by mechanic id for an existent mechanic with no contracts
 */
public class ScenarioCF9 {
	private ContractCrudService service = Factories.service.forContractCrudService();
	private TMechanicsRecord mechanic;
	private List<ContractSummaryDto> result;

	@Given("[C.F.9] a mechanic with no contracts")
	public void givenAMechanicWithNoContracts() {
		mechanic = DbFixtures.aMechanic();
	}

	@When("[C.F.9] I search contracts for that mechanic")
	public void whenISearchContractsForThatMechanic() throws BusinessException {
		result = service.findByMechanicNif(mechanic.nif);
	}

	@Then("[C.F.9] List of contracts summary is empty")
	public void thenListOfContractsSummaryIsEmpty() {
		assertTrue(result.isEmpty());
	}
}