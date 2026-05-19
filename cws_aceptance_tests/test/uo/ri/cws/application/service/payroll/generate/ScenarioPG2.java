package uo.ri.cws.application.service.payroll.generate;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.payroll.PayrollService;
import uo.ri.cws.application.service.util.dbfixture.Db;
import uo.ri.cws.application.service.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.util.dbfixture.records.TMechanicsRecord;
import uo.ri.cws.application.service.util.dbfixture.records.TPayrollsRecord;
import uo.ri.util.exception.BusinessException;

/**
 * Feature: Generate payrolls for mechanics last month
 * Scenario: [P.G.2] Generate payrolls for a mechanic with no active contract last month
 */
public class ScenarioPG2 {
    private PayrollService service = Factories.service.forPayrollService();
    private TMechanicsRecord mechanic;

    @Given("[P.G.2] a mechanic with several contracts but none active")
    public void givenMechanicWithSeveralContractsButNoneActive() {
        mechanic = DbFixtures.aMechanic();
        // Create terminated contracts for this mechanic
        DbFixtures.aContractTerminatedForMechanic(mechanic.id);
        DbFixtures.aContractTerminatedForMechanic(mechanic.id);
    }

    @When("[P.G.2] I generate payrolls")
    public void whenIGeneratePayrolls() throws BusinessException {
        service.generateForPreviousMonth();
    }

    @Then("[P.G.2] zero payrolls are generated")
    public void thenZeroPayrollsAreGenerated() {
    	List<TPayrollsRecord> loaded = Db.findAll(TPayrollsRecord.class);
        assertEquals(0, loaded.size());
    }
}