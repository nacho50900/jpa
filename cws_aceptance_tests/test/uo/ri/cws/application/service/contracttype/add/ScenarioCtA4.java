package uo.ri.cws.application.service.contracttype.add;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.contracttype.ContractTypeCrudService;
import uo.ri.cws.application.service.contracttype.ContractTypeCrudService.ContractTypeDto;
import uo.ri.cws.application.service.util.ExceptionBox;
import uo.ri.cws.application.service.util.dtobuilders.ContractTypeDtoBuilder;

public class ScenarioCtA4 {
    private ExceptionBox ctx = new ExceptionBox();
    private ContractTypeCrudService service = Factories.service.forContractTypeCrudService();

    @When("[Ct.A.4] I try to add a contract type with null name")
    public void whenITryToAddAContractTypeWithNullName() {
        ContractTypeDto dto = new ContractTypeDtoBuilder().build();
        dto.name = null; // Null name

        ctx.tryAndKeep(() -> service.create(dto));
    }

    @Then("[Ct.A.4] argument is rejected with an explaining message")
    public void thenArgumentIsRejectedWithAnExplainingMessage() {
        ctx.assertIllegalArgumentExceptionWithMessage();
    }
}