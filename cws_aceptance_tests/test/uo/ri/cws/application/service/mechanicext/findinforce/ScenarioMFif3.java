package uo.ri.cws.application.service.mechanicext.findinforce;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.contract.ContractCrudService;
import uo.ri.cws.application.service.contract.ContractCrudService.ContractDto;
import uo.ri.cws.application.service.util.dbfixture.DbFixtures;
import uo.ri.util.exception.BusinessException;

public class ScenarioMFif3 {
	private ContractCrudService service = Factories.service.forContractCrudService();
	private List<ContractDto> result;

	@Given("[M.Fif.3] a relation of mechanics with no inforce contracts")
	public void givenARelationOfMechanicsWithNoInforceContracts() {
		DbFixtures.someMechanicsWithNoInforceContracts();
	}

	@When("[M.Fif.3] we find for mechanics with contract in force")
	public void whenWeFindForMechanicsWithContractInForce() throws BusinessException {
		result = service.findInforceContracts();
	}

	@Then("[M.Fif.3] we get no mechanics")
	public void thenWeGetNoMechanics() {
		assertNotNull(result);
		assertTrue(result.isEmpty(), "Expected empty list when no mechanics have in-force contracts");
	}
}