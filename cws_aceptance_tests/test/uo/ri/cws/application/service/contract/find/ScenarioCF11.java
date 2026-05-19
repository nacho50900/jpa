package uo.ri.cws.application.service.contract.find;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Random;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.contract.ContractCrudService;
import uo.ri.cws.application.service.contract.ContractCrudService.ContractSummaryDto;
import uo.ri.cws.application.service.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.util.dbfixture.records.TContractsRecord;
import uo.ri.cws.application.service.util.dbfixture.records.TMechanicsRecord;
import uo.ri.util.exception.BusinessException;

/**
 * Feature: Find contracts
 * Scenario: [C.F.11] Find contracts by mechanic ID for an existing mechanic 
 * 		with one active contract and several payrolls.
 */
public class ScenarioCF11 {
	private ContractCrudService service = Factories.service.forContractCrudService();
	private TMechanicsRecord mechanic;
	private TContractsRecord contract;
	private List<ContractSummaryDto> results;
	private int numPayrolls;

	@Given("[C.F.11] a mechanic with one in-force contract and several payrolls")
	public void givenAMechanicWithOneInForceContractAndSeveralPayrolls() {
		mechanic = DbFixtures.aMechanic();
		numPayrolls = new Random().nextInt(10) + 1;
		contract = DbFixtures.aContractWithPayrollsForMechanicOf( mechanic.id, numPayrolls );
	}

	@When("[C.F.11] I search contracts for that mechanic")
	public void whenISearchContractsForThatMechanic() throws BusinessException {
		results = service.findByMechanicNif(mechanic.nif);
	}

	@Then("[C.F.11] All contract summaries are found")
	public void thenAllContractSummariesAreFound() {
		ContractSummaryDto dto = results.get(0);
		assertEquals(1, results.size());
		assertEquals(contract.id, dto.id);
		assertEquals(mechanic.nif, dto.nif);
		assertEquals(contract.settlement, dto.settlement);
		assertEquals(contract.state, dto.state);
		assertEquals(numPayrolls, dto.numPayrolls);
	}
}