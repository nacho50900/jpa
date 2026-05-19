package uo.ri.cws.application.service.contract.find;

import java.util.List;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.contract.ContractCrudService;
import uo.ri.cws.application.service.contract.ContractCrudService.ContractSummaryDto;
import uo.ri.cws.application.service.util.Asserts;
import uo.ri.cws.application.service.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.util.dbfixture.records.TContractsRecord;
import uo.ri.cws.application.service.util.dbfixture.records.TMechanicsRecord;
import uo.ri.util.exception.BusinessException;

/**
 * Feature: Find contracts
 * Scenario: [C.F.10] Find contracts by mechanic ID for an existing mechanic 
 * 		with no active contracts and several terminated contracts 
 * 		with no payrolls.
 */
public class ScenarioCF10 {
	private ContractCrudService service = Factories.service.forContractCrudService();
	private TMechanicsRecord mechanic;
	private List<ContractSummaryDto> result;
	private List<TContractsRecord> contracts;

	@Given("[C.F.10] a mechanic with several contracts terminated")
	public void givenAMechanicWithSeveralContractsTerminated() {
		mechanic = DbFixtures.aMechanic();
		contracts = List.of(
			DbFixtures.aContractTerminatedForMechanic(mechanic.id),
			DbFixtures.aContractTerminatedForMechanic(mechanic.id)
		);
	}

	@When("[C.F.10] I search contracts for that mechanic")
	public void whenISearchContractsForThatMechanic() throws BusinessException {
		result = service.findByMechanicNif(mechanic.nif);
	}

	@Then("[C.F.10] All contract summaries are found")
	public void thenAllContractSummariesAreFound() {
		Asserts.rightSummaries(contracts, result, mechanic.nif, 0);
	}

}