package uo.ri.cws.application.service.contract.find;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.contract.ContractCrudService;
import uo.ri.cws.application.service.contract.ContractCrudService.ContractSummaryDto;
import uo.ri.cws.application.service.util.Asserts;
import uo.ri.cws.application.service.util.dbfixture.Db;
import uo.ri.cws.application.service.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.util.dbfixture.records.TContractsRecord;
import uo.ri.cws.application.service.util.dbfixture.records.TMechanicsRecord;
import uo.ri.util.exception.BusinessException;

/**
 * Feature: Find contracts
 * Scenario: [C.F.2] Find all contracts when there are some
 */
public class ScenarioCF2 {
	private ContractCrudService service = Factories.service.forContractCrudService();
	private List<TContractsRecord> contracts;
	private List<ContractSummaryDto> result;

	@Given("[C.F.2] several contracts in the system")
	public void givenSeveralContractsInTheSystem() {
		contracts = List.of(
			// with no payrolls and random mechanics
			DbFixtures.aContractInForceWithMechanicTypeAndGroup(),
			DbFixtures.aContractTerminatedWithMechanicTypeAndGroup(),
			DbFixtures.anContractInForceFixedTerm()
		);
	}

	@When("[C.F.2] I search for all contracts")
	public void whenISearchForAllContracts() throws BusinessException {
		result = service.findAll();
	}

	@Then("[C.F.2] All contract summaries are found")
	public void thenAllContractSummariesAreFound() {
		assertEquals(contracts.size(), result.size());
		
		for (TContractsRecord expected : contracts) {
			Optional<ContractSummaryDto> ocs = findDto(expected.id);
			assertTrue(ocs.isPresent());

			ContractSummaryDto dto = ocs.get();
			TMechanicsRecord m = Db.findById(TMechanicsRecord.class, expected.mechanic_Id);

			Asserts.rightSummary(expected, dto, m.nif, 0);
		}
	}

	private Optional<ContractSummaryDto> findDto(String id) {
		return result.stream()
			.filter(dto -> dto.id.equals(id))
			.findFirst();
	}
}