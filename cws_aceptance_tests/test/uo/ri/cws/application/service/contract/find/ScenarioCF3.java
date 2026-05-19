package uo.ri.cws.application.service.contract.find;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.contract.ContractCrudService;
import uo.ri.cws.application.service.contract.ContractCrudService.ContractDto;
import uo.ri.cws.application.service.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.util.dbfixture.records.TContractsRecord;
import uo.ri.util.exception.BusinessException;

/**
 * Feature: Find contracts
 * Scenario: [C.F.3] Find by id an existent contract in force
 */
public class ScenarioCF3 {
	private ContractCrudService service = Factories.service.forContractCrudService();
	private TContractsRecord contract;
	private Optional<ContractDto> result;

	@Given("[C.F.3] an in-force contract")
	public void givenAnInForceContract() {
		contract = DbFixtures.aContractInForceWithMechanicTypeAndGroup();
	}

	@When("[C.F.3] I search for that contract")
	public void whenISearchForThatContract() throws BusinessException {
		result = service.findById(contract.id);
	}

	@Then("[C.F.3] the contract is found")
	public void thenTheContractIsFound() {
		assertTrue(result.isPresent());
		ContractDto dto = result.get();
		assertEquals(contract.id, dto.id);
		assertEquals(contract.annualBaseSalary, dto.annualBaseSalary);
	}
}