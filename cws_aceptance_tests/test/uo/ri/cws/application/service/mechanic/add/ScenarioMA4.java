package uo.ri.cws.application.service.mechanic.add;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.mechanic.MechanicCrudService;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.cws.application.service.util.ExceptionBox;
import uo.ri.cws.application.service.util.dtobuilders.MechanicDtoBuilder;

public class ScenarioMA4 {
    private ExceptionBox ctx = new ExceptionBox();
    private MechanicCrudService service = Factories.service.forMechanicCrudService();

    @When("[M.A.4] I try to add a new mechanic with null nif")
    public void whenITryToAddANewMechanicWithNullNif() {
        MechanicDto dto = new MechanicDtoBuilder().build();
        dto.nif = null; // Null NIF

        ctx.tryAndKeep(() -> service.create( dto ));
    }

    @Then("[M.A.4] argument is rejected with an explaining message")
    public void thenArgumentIsRejectedWithAnExplainingMessage() {
        ctx.assertIllegalArgumentExceptionWithMessage();
    }
}
