package uo.ri.cws.application.service.contract.update;

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
 * Scenario: [C.U.6] Try to update a contract in the while (wrong version)
 */
public class ScenarioCU6 {
	private ExceptionBox ctx = new ExceptionBox();
	private ContractCrudService service = Factories.service.forContractCrudService();
	private TContractsRecord contract;

	@Given("[C.U.6] an in-force contract")
	public void givenAnInForceContract() {
		contract = DbFixtures.aContractInForceWithMechanicTypeAndGroup();
	}

	@When("[C.U.6] I try to update a contract updated in the while")
	public void whenITryToUpdateAContractUpdatedInTheWhile() {
		ContractDto dto = new ContractDtoBuilder()
				.withId(contract.id)
				.withAnnualSalary(contract.annualBaseSalary + 100.0)
				.withVersion(contract.version - 1) // simulates old version
				.build();

		ctx.tryAndKeep(() -> service.update(dto));
	}

	@Then("[C.U.6] a business error happens with an explaining message")
	public void thenABusinessErrorHappensWithAnExplainingMessage() {
		ctx.assertBusinessExceptionWithMessage();
	}
}