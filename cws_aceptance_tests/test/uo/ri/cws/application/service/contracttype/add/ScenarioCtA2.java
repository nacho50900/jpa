package uo.ri.cws.application.service.contracttype.add;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.contracttype.ContractTypeCrudService;
import uo.ri.cws.application.service.contracttype.ContractTypeCrudService.ContractTypeDto;
import uo.ri.cws.application.service.util.ExceptionBox;
import uo.ri.cws.application.service.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.util.dbfixture.records.TContractTypesRecord;
import uo.ri.cws.application.service.util.dtobuilders.ContractTypeDtoBuilder;

public class ScenarioCtA2 {
	private ExceptionBox ctx = new ExceptionBox();
	private ContractTypeCrudService service = Factories.service.forContractTypeCrudService();
	private String contractTypeName;

	@Given("[Ct.A.2] a registered contract type")
	public void givenAContractTypeWithName() {
		TContractTypesRecord contractType = DbFixtures.aContractType();
		contractTypeName = contractType.name;
	}

	@When("[Ct.A.2] I try to add another contract type with the same name")
	public void whenITryToAddAContractTypeWith() {
		ContractTypeDto dto = new ContractTypeDtoBuilder()
				.withName( contractTypeName )
				.build();

		ctx.tryAndKeep(() -> service.create(dto));
	}

	@Then("[Ct.A.2] a business error happens with an explaining message")
	public void thenABusinessErrorHappensWithAnExplainingMessage() {
		ctx.assertBusinessExceptionWithMessage();
	}
}