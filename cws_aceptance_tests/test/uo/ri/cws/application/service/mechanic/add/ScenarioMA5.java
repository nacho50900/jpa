
package uo.ri.cws.application.service.mechanic.add;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.mechanic.MechanicCrudService;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.cws.application.service.util.ExceptionBox;
import uo.ri.cws.application.service.util.dtobuilders.MechanicDtoBuilder;

public class ScenarioMA5 {
	private ExceptionBox ctx = new ExceptionBox();
	private MechanicCrudService service = Factories.service
			.forMechanicCrudService();

	@When("[M.A.5] I try to add a new mechanic with {string}, {string}, {string}")
	public void whenITryToAddANewMechanicWithNifNameSurname(
			String nif, String name, String surname) {
		
		MechanicDto dto = new MechanicDtoBuilder().build();
		dto.nif = nif;
		dto.name = name;
		dto.surname = surname;

		ctx.tryAndKeep(() -> service.create(dto));
	}

	@Then("[M.A.5] argument is rejected with an explaining message")
	public void thenArgumentIsRejectedWithAnExplainingMessage() {
		ctx.assertIllegalArgumentExceptionWithMessage();
	}
}
