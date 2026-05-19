package uo.ri.cws.application.service.contract.update;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

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
 * Scenario: [C.U.1] Update end date
 */
public class ScenarioCU1 {
	private ContractCrudService service = Factories.service.forContractCrudService();
	private TContractsRecord original;
	private TContractsRecord loaded;

	@Given("[C.U.1] an in-force fixed-term contract")
	public void givenAnInForceFixedTermContract() {
		original = DbFixtures.anContractInForceFixedTerm();
	}

	@When("[C.U.1] I update the end date of the contract")
	public void whenIUpdateTheEndDateOfTheContract() throws BusinessException {
		LocalDate newEndDate = original.endDate.toLocalDate().plusDays(30);
		
		ContractDto dto = new ContractDtoBuilder()
				.withId(original.id)
				.withEndDate(newEndDate)
				.withAnnualSalary(original.annualBaseSalary)
				.withVersion(original.version)
				.build();

		service.update(dto);
	}

	@Then("[C.U.1] the end date is updated")
	public void thenTheEndDateIsUpdated() {
		loaded = Db.findById(TContractsRecord.class, original.id);
		
		assertEquals(
				original.endDate.toLocalDate().plusDays(30), 
				loaded.endDate.toLocalDate()
			);
		
		assertEquals(original.annualBaseSalary, loaded.annualBaseSalary); // salary not updated
		
		assertTrue(original.version < loaded.version, "Version must increase");
		assertEquals(original.startDate, loaded.startDate);
		assertTrue(original.updatedAt.before(loaded.updatedAt));
	}
}