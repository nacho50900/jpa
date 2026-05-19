package uo.ri.cws.application.service.contract.update;

import java.time.LocalDate;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.contract.ContractCrudService;
import uo.ri.cws.application.service.contract.ContractCrudService.ContractDto;
import uo.ri.cws.application.service.util.ExceptionBox;
import uo.ri.cws.application.service.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.util.dbfixture.records.TContractsRecord;
import uo.ri.cws.application.service.util.dtobuilders.ContractDtoBuilder;

/**
 * Feature: Update a contract
 * Scenario: [C.U.5] Try to update a contract with wrong end date
 */
public class ScenarioCU5 {
	private ExceptionBox ctx = new ExceptionBox();
	private ContractCrudService service = Factories.service.forContractCrudService();
	private TContractsRecord contract;

	@Given("[C.U.5] an in-force fixed-term contract")
	public void givenAnInForceFixedTermContract() {
		contract = DbFixtures.anContractInForceFixedTerm();
	}

	@When("[C.U.5] I try to update that with end date earlier than start date")
	public void whenITryToUpdateThatWithEndDateEarlierThanStartDate() {
		LocalDate invalidEndDate = contract.startDate.toLocalDate().minusDays(1);
		
		ContractDto dto = new ContractDtoBuilder()
				.withId(contract.id)
				.withEndDate(invalidEndDate)
				.withAnnualSalary(contract.annualBaseSalary)
				.withVersion(contract.version)
				.build();

		ctx.tryAndKeep(() -> service.update(dto));
	}

	@Then("[C.U.5] a business error happens with an explaining message")
	public void thenABusinessErrorHappensWithAnExplainingMessage() {
		ctx.assertBusinessExceptionWithMessage();
	}
}