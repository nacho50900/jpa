package uo.ri.cws.application.service.contract.update;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.contract.ContractCrudService;
import uo.ri.cws.application.service.contract.ContractCrudService.ContractDto;
import uo.ri.cws.application.service.util.dbfixture.Db;
import uo.ri.cws.application.service.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.util.dbfixture.records.TContractsRecord;
import uo.ri.cws.application.service.util.dtobuilders.ContractDtoBuilder;
import uo.ri.util.exception.BusinessException;

/**
 * Feature: Update a contract
 * Scenario: [C.U.2] Update salary
 */
public class ScenarioCU2 {
	private ContractCrudService service = Factories.service.forContractCrudService();
	private TContractsRecord original;
	private TContractsRecord loaded;

	@Given("[C.U.2] an in-force contract")
	public void givenAnInForceContract() {
		original = DbFixtures.anContractInForceFixedTerm();
	}

	@When("[C.U.2] I update the salary of the contract")
	public void whenIUpdateTheSalaryOfTheContract() throws BusinessException {
		double newSalary = original.annualBaseSalary + 500.0;
		
		ContractDto dto = new ContractDtoBuilder()
				.withId(original.id)
				.withEndDate(original.endDate != null ? original.endDate.toLocalDate() : null)
				.withAnnualSalary(newSalary)
				.withVersion(original.version)
				.build();

		service.update(dto);
	}

	@Then("[C.U.2] the salary is updated")
	public void thenTheSalaryIsUpdated() {
		loaded = Db.findById(TContractsRecord.class, original.id);
		
		assertEquals(original.annualBaseSalary + 500.0, loaded.annualBaseSalary);
		assertEquals(original.endDate, loaded.endDate); // end date not updated
		
		assertTrue(original.version < loaded.version, "Version must increase");
		assertEquals(original.startDate, loaded.startDate);
		assertTrue(original.updatedAt.before(loaded.updatedAt));
	}
}