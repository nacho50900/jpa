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
 * Scenario Outline: [C.U.10] Try to update a contract with wrong salary
 */
public class ScenarioCU10 {
	private ExceptionBox ctx = new ExceptionBox();
	private ContractCrudService service = Factories.service.forContractCrudService();
	private TContractsRecord original;

	@Given("[C.U.10] an in-force contract")
	public void givenAnInForceContract() {
		original = DbFixtures.aContractInForceWithMechanicTypeAndGroup();
	}

	@When("[C.U.10] I try to update a contract with wrong salary {double}")
	public void whenITryToUpdateAContractWithWrongSalary(double money) {
		ContractDto dto = new ContractDtoBuilder()
				.withId(original.id)
				.withEndDate(original.endDate != null ? original.endDate.toLocalDate() : null)
				.withVersion(original.version)
				.withAnnualSalary( money )	// wrong salary
				.build();

		ctx.tryAndKeep(() -> service.update(dto));
	}

	@Then("[C.U.10] argument is rejected with an explaining message")
	public void thenArgumentIsRejectedWithAnExplainingMessage() {
		ctx.assertIllegalArgumentExceptionWithMessage();
	}
}