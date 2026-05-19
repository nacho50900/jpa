package uo.ri.cws.application.service.mechanicext.findinforce;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import uo.ri.cws.application.service.util.dbfixture.records.TMechanicsRecord;
import uo.ri.util.exception.BusinessException;

public class ScenarioMFif2 {
	private ContractCrudService service = Factories.service.forContractCrudService();
	private List<TMechanicsRecord> mechanicsWithInforceContracts;
	private List<ContractDto> result;

	@Given("[M.Fif.2] a relation of mechanics with inforce contracts")
	public void givenARelationOfMechanicsWithInforceContracts() {
		mechanicsWithInforceContracts = DbFixtures.someMechanicsWithInforceContracts();
	}

	@When("[M.Fif.2] we find for mechanics with contract in force")
	public void whenWeFindForMechanicsWithContractInForce() throws BusinessException {
		result = service.findInforceContracts();
	}

	@Then("[M.Fif.2] we get all the mechanics")
	public void thenWeGetAllTheMechanics() {
		assertNotNull(result);
		assertEquals(mechanicsWithInforceContracts.size(), result.size());
		
		// Verify that all mechanics with in-force contracts are returned
		for (TMechanicsRecord mechanic : mechanicsWithInforceContracts) {
			assertTrue(result.stream()
				.anyMatch(c -> c.mechanic.id.equals(mechanic.id)));
		}
	}
}