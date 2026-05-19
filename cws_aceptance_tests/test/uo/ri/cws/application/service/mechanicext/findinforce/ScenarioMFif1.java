package uo.ri.cws.application.service.mechanicext.findinforce;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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

public class ScenarioMFif1 {
	private ContractCrudService service = Factories.service.forContractCrudService();
	private List<TMechanicsRecord> mechanicsWithNoInforceContracts;
	private List<TMechanicsRecord> mechanicsWithInforceContracts;
	private List<ContractDto> result;

	@Given("[M.Fif.1] a relation of mechanics with no inforce contracts")
	public void givenARelationOfMechanicsWithNoInforceContracts() {
		mechanicsWithNoInforceContracts = DbFixtures.someMechanicsWithNoInforceContracts();
	}

	@Given("[M.Fif.1] another relation of mechanics with inforce contracts")
	public void givenAnotherRelationOfMechanicsWithInforceContracts() {
		mechanicsWithInforceContracts = DbFixtures.someMechanicsWithInforceContracts();
	}

	@When("[M.Fif.1] we find for mechanics with contract in force")
	public void whenWeFindForMechanicsWithContractInForce() throws BusinessException {
		result = service.findInforceContracts();
	}

	@Then("[M.Fif.1] we get only the inforce mechanics")
	public void thenWeGetOnlyTheInforceMechanics() {
		assertNotNull(result);
		assertEquals(mechanicsWithInforceContracts.size(), result.size());
		
		// Verify that all returned mechanics are from the inforce list
		for (ContractDto cto : result) {
			assertTrue(
				mechanicsWithInforceContracts.stream()
					.anyMatch(m -> m.id.equals(cto.mechanic.id))
			);
		}
		
		// Verify that no mechanics from the no-inforce list are returned
		for (TMechanicsRecord noInforceMechanic : mechanicsWithNoInforceContracts) {
			assertFalse(
					result.stream()
						.anyMatch(c -> c.mechanic.id.equals( noInforceMechanic.id ))
				);
		}
	}
}