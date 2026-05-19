package uo.ri.cws.application.service.payroll.delete;

import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.payroll.PayrollService;
import uo.ri.cws.application.service.util.dbfixture.Db;
import uo.ri.cws.application.service.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.util.dbfixture.records.TContractsRecord;
import uo.ri.cws.application.service.util.dbfixture.records.TMechanicsRecord;
import uo.ri.cws.application.service.util.dbfixture.records.TPayrollsRecord;
import uo.ri.util.exception.BusinessException;

/**
 * Feature: Delete previous month payroll for a mechanic
 * Scenario: [P.D.2] Delete previous month payroll for a mechanic with only one payroll
 */
public class ScenarioPD2 {
    private PayrollService service = Factories.service.forPayrollService();
    private TMechanicsRecord mechanic;
    private TContractsRecord contract;
    private TPayrollsRecord payroll;

    @Given("[P.D.2] a mechanic with a contract in force and only one payroll previous month")
    public void givenMechanicWithContractInForceAndOnlyOnePayrollPreviousMonth() {
        mechanic = DbFixtures.aMechanic();
        contract = DbFixtures.aContractInForceForMechanic(mechanic.id);
        
        LocalDate lastMonth = LocalDate.now().minusMonths(1);
        payroll = DbFixtures.aPayrollForContractAndMonth(contract.id, lastMonth);
    }

    @When("[P.D.2] I delete previous month payroll for that mechanic")
    public void whenIDeletePreviousMonthPayrollForThatMechanic() throws BusinessException {
        service.deleteLastGeneratedOfMechanicId(mechanic.id);
    }

    @Then("[P.D.2] the previous month payroll is removed")
    public void thenThePreviousMonthPayrollIsRemoved() {
        TPayrollsRecord loaded = Db.findById(TPayrollsRecord.class, payroll.id);
        assertNull( loaded );
    }
}