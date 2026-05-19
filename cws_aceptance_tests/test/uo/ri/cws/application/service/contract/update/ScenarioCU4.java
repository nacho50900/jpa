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
 * Scenario: [C.U.4] Try to update a contract terminated
 */
public class ScenarioCU4 {
	private ExceptionBox ctx = new ExceptionBox();
	private ContractCrudService service = Factories.service.forContractCrudService();
	private TContractsRecord contract;

	@Given("[C.U.4] a terminated contract")
	public void givenATerminatedContract() {
		contract = DbFixtures.aContractTerminatedWithMechanicTypeAndGroup();
	}

	@When("[C.U.4] I try to update that terminated contract")
	public void whenITryToUpdateThatTerminatedContract() {
		ContractDto dto = new ContractDtoBuilder()
				.withId(contract.id)
				.withAnnualSalary(contract.annualBaseSalary + 100.0)
				.withEndDate( LocalDate.now().plusMonths(1) )
				.withVersion(contract.version)
				.build();

		ctx.tryAndKeep(() -> service.update(dto));
	}

	@Then("[C.U.4] a business error happens with an explaining message")
	public void thenABusinessErrorHappensWithAnExplainingMessage() {
		ctx.assertBusinessExceptionWithMessage();
	}
}