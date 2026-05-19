package uo.ri.cws.application.service.payroll.delete;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.List;

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
 * Scenario: [P.D.3] Delete previous month payroll for a mechanic with several 
 * 		payroll none previous month
 */
public class ScenarioPD3 {
    private PayrollService service = Factories.service.forPayrollService();
    private TMechanicsRecord mechanic;
    private TContractsRecord contract;
    private int initialPayrollCount;

    @Given("[P.D.3] a mechanic with a contract in force and several payrolls none previous month")
    public void givenMechanicWithContractInForceAndSeveralPayrollsNonePreviousMonth() {
        mechanic = DbFixtures.aMechanic();
        contract = DbFixtures.aContractInForceForMechanic(mechanic.id);
        
        // Create payrolls for different months but not previous month
        LocalDate twoMonthsAgo = LocalDate.now().minusMonths(2);
        LocalDate threeMonthsAgo = LocalDate.now().minusMonths(3);
        
        DbFixtures.aPayrollForContractAndMonth(contract.id, twoMonthsAgo);
        DbFixtures.aPayrollForContractAndMonth(contract.id, threeMonthsAgo);
        
        initialPayrollCount = Db.findAll(TPayrollsRecord.class).size();
    }

    @When("[P.D.3] I delete previous month payroll for that mechanic")
    public void whenIDeletePreviousMonthPayrollForThatMechanic() throws BusinessException {
        service.deleteLastGeneratedOfMechanicId(mechanic.id);
    }

    @Then("[P.D.3] no payroll is removed")
    public void thenNoPayrollIsRemoved() {
        List<TPayrollsRecord> payrolls = Db.findAll(TPayrollsRecord.class);
        assertEquals(initialPayrollCount, payrolls.size());
    }
}