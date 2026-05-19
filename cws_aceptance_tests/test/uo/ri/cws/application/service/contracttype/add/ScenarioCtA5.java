package uo.ri.cws.application.service.contracttype.add;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.contracttype.ContractTypeCrudService;
import uo.ri.cws.application.service.contracttype.ContractTypeCrudService.ContractTypeDto;
import uo.ri.cws.application.service.util.ExceptionBox;
import uo.ri.cws.application.service.util.dtobuilders.ContractTypeDtoBuilder;

public class ScenarioCtA5 {
	private ExceptionBox ctx = new ExceptionBox();
	private ContractTypeCrudService service = Factories.service.forContractTypeCrudService();

	@When("[Ct.A.5] I try to add a contract type with negative compensation days")
	public void whenITryToAddAContractTypeWithNegativeCompensationDays() {
		ContractTypeDto dto = new ContractTypeDtoBuilder().build();
		dto.compensationDays = -1; // Negative compensation days

		ctx.tryAndKeep(() -> service.create(dto));
	}

	@Then("[Ct.A.5] argument is rejected with an explaining message")
	public void thenArgumentIsRejectedWithAnExplainingMessage() {
		ctx.assertIllegalArgumentExceptionWithMessage();
	}
}