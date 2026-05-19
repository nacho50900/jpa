package uo.ri.cws.application.service.contracttype.add;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.contracttype.ContractTypeCrudService;
import uo.ri.cws.application.service.contracttype.ContractTypeCrudService.ContractTypeDto;
import uo.ri.cws.application.service.util.ExceptionBox;
import uo.ri.cws.application.service.util.dtobuilders.ContractTypeDtoBuilder;

public class ScenarioCtA6 {
	private ExceptionBox ctx = new ExceptionBox();
	private ContractTypeCrudService service = Factories.service.forContractTypeCrudService();

	@When("[Ct.A.6] I try to add a contract type with {string}")
	public void whenITryToAddAContractTypeWith(String name) {
		ContractTypeDto dto = new ContractTypeDtoBuilder().build();
		
		// Handle the different parameter values from the Examples table
		dto.name = "null".equals(name) ? null : name;
		
		ctx.tryAndKeep(() -> service.create(dto));
	}

	@Then("[Ct.A.6] argument is rejected with an explaining message")
	public void thenArgumentIsRejectedWithAnExplainingMessage() {
		ctx.assertIllegalArgumentExceptionWithMessage();
	}
}