package uo.ri.cws.application.service.mechanic.update;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.mechanic.MechanicCrudService;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.cws.application.service.util.ExceptionBox;
import uo.ri.cws.application.service.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.util.dbfixture.records.TMechanicsRecord;
import uo.ri.cws.application.service.util.dtobuilders.MechanicDtoBuilder;

public class ScenarioMU7 {
	private ExceptionBox ctx = new ExceptionBox();
	private MechanicCrudService service = Factories.service.forMechanicCrudService();
    private TMechanicsRecord mechanic;

	@Given("[M.U.7] a mechanic")
	public void givenAMechanic() {
		mechanic = DbFixtures.aMechanic();
	}

	@When("[M.U.7] I try to update the mechanic to {string}, {string} and {string}")
	public void whenITryToUpdateTheMechanicToNameSurnameAndNif(String name, String surname, String nif) {
        MechanicDto dto = new MechanicDtoBuilder()
                .withId(mechanic.id)
                .withName(name)
                .withSurname(surname)
                .withNif(nif)
                .withVersion(mechanic.version)
                .build();

        ctx.tryAndKeep(() -> service.update(dto) );
	}

	@Then("[M.U.7] argument is rejected with an explaining message")
	public void thenArgumentIsRejectedWithAnExplainingMessage() {
		ctx.assertIllegalArgumentExceptionWithMessage();
	}
}
