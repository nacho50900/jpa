package uo.ri.cws.application.service.mechanic.delete;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.mechanic.MechanicCrudService;
import uo.ri.cws.application.service.util.ExceptionBox;
import uo.ri.cws.application.service.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.util.dbfixture.records.TMechanicsRecord;

public class ScenarioMD6 {
	private ExceptionBox ctx = new ExceptionBox();
	private MechanicCrudService service = Factories.service.forMechanicCrudService();
	private String mechanicId;

	@Given("[M.D.6] a mechanic with a contract in force")
	public void givenAMechanicWithAContractInForce() {
		TMechanicsRecord m = DbFixtures.aMechanicWithInForceContract();
		mechanicId = m.id;
	}

	@When("[M.D.6] I try to delete the mechanic")
	public void whenITryToDeleteTheMechanic() {
		ctx.tryAndKeep(() -> service.delete(mechanicId));
	}

	@Then("[M.D.6] a business error happens with an explaining message")
	public void thenABusinessErrorHappensWithAnExplainingMessage() {
		ctx.assertBusinessExceptionWithMessage();
	}
}
