package uo.ri.cws.application.service.contract.find;

import java.util.List;
import java.util.Random;

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
 * Scenario: [C.F.12] Find contracts by mechanic id for an existent mechanic 
 * 		with several contracts terminated and one contract in force all with 
 * 		several payrolls
 */
public class ScenarioCF12 {
	private ContractCrudService service = Factories.service.forContractCrudService();
	private TMechanicsRecord mechanic;
	private List<ContractSummaryDto> results;
	private List<TContractsRecord> contracts;
	private int numPayrolls;

	@Given("[C.F.12] a mechanic with several contracts terminated and one in force all with payrolls")
	public void givenAMechanicWithSeveralContractsTerminatedAndOneInForceAllWithPayrolls() {
		mechanic = DbFixtures.aMechanic();
		numPayrolls = new Random().nextInt(10) + 1;
		contracts = List.of(
			DbFixtures.aContractWithPayrollsForMechanicOf(mechanic.id, numPayrolls),
			DbFixtures.aContractWithPayrollsTerminatedForMechanicOf(mechanic.id, numPayrolls),
			DbFixtures.aContractWithPayrollsTerminatedForMechanicOf(mechanic.id, numPayrolls)
		);
	}

	@When("[C.F.12] I search contracts for that mechanic")
	public void whenISearchContractsForThatMechanic() throws BusinessException {
		results = service.findByMechanicNif(mechanic.nif);
	}

	@Then("[C.F.12] All contract summaries are found")
	public void thenAllContractSummariesAreFound() {
		Asserts.rightSummaries(contracts, results, mechanic.nif, numPayrolls);
	}

}