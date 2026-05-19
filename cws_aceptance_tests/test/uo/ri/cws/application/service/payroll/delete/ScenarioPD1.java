package uo.ri.cws.application.service.payroll.delete;

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
 * Feature: Delete previous month payroll for a mechanic
 * Scenario: [P.D.1] Delete previous month payroll for a mechanic with no payroll
 */
public class ScenarioPD1 {
    private PayrollService service = Factories.service.forPayrollService();
    private TMechanicsRecord mechanic;

    @Given("[P.D.1] a mechanic with a contract in force but still no payrolls")
    public void givenMechanicWithContractInForceButStillNoPayrolls() {
        mechanic = DbFixtures.aMechanic();
        DbFixtures.aContractInForceForMechanic(mechanic.id);
    }

    @When("[P.D.1] I delete previous month payroll for that mechanic")
    public void whenIDeletePreviousMonthPayrollForThatMechanic() throws BusinessException {
        service.deleteLastGeneratedOfMechanicId(mechanic.id);
    }

    @Then("[P.D.1] no payroll is deleted")
    public void thenNoPayrollIsDeleted() {
        List<TPayrollsRecord> payrolls = Db.findAll(TPayrollsRecord.class);
        assertEquals(0, payrolls.size());
    }
}