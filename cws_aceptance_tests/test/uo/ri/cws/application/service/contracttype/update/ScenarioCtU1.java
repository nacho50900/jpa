package uo.ri.cws.application.service.contracttype.update;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.contracttype.ContractTypeCrudService;
import uo.ri.cws.application.service.contracttype.ContractTypeCrudService.ContractTypeDto;
import uo.ri.cws.application.service.util.dbfixture.Db;
import uo.ri.cws.application.service.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.util.dbfixture.records.TContractTypesRecord;
import uo.ri.cws.application.service.util.dtobuilders.ContractTypeDtoBuilder;
import uo.ri.util.exception.BusinessException;

/**
 * Feature: Update a contract type
 * Scenario: [Ct.U.1] Update an existent contract type
 */
public class ScenarioCtU1 {
	private ContractTypeCrudService service = Factories.service.forContractTypeCrudService();
	private TContractTypesRecord original;
	private TContractTypesRecord loaded;

	@Given("[Ct.U.1] a registered contract type")
	public void givenARegisteredContractType() {
		original = DbFixtures.aContractType();
	}

	@When("[Ct.U.1] I update contract type compensatiom days")
	public void whenIUpdateContractTypeNameAndCompensationDays() throws BusinessException {
		ContractTypeDto dto = new ContractTypeDtoBuilder()
				.withId(original.id)
				.withName(original.name)
				.withCompensationDays( original.compensationDaysPerYear + 5 )
				.withVersion(original.version)
				.build();

		service.update(dto);
	}

	@Then("[Ct.U.1] only the compensation are updated")
	public void thenOnlyTheCompensationAreUpdated() {
		loaded = Db.findById(TContractTypesRecord.class, original.id);
		
		assertEquals(original.name, loaded.name); // name not updated
		assertEquals( // compensation days updated
				original.compensationDaysPerYear + 5, 
				loaded.compensationDaysPerYear
			);
		
		assertTrue(original.version < loaded.version, "Version must increase");
		assertEquals(original.createdAt, loaded.createdAt);
		assertTrue(original.updatedAt.before(loaded.updatedAt));
	}
}