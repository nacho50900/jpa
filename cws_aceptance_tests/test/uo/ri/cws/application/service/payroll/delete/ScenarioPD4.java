package uo.ri.cws.application.service.payroll.delete;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

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
 * Scenario: [P.D.4] Delete previous month payroll for a mechanic with several 
 * 		payrolls including previous month
 */
public class ScenarioPD4 {
    private PayrollService service = Factories.service.forPayrollService();
    private TMechanicsRecord mechanic;
    private TContractsRecord contract;
	private TPayrollsRecord previousMonthPayroll;

    @Given("[P.D.4] a mechanic with a contract in force and several payrolls one previous month")
    public void givenMechanicWithContractInForceAndSeveralPayrollsOnePreviousMonth() {
        mechanic = DbFixtures.aMechanic();
        contract = DbFixtures.aContractInForceForMechanic(mechanic.id);
        
        // Create payrolls for different months including previous month
        LocalDate lastMonth = LocalDate.now().minusMonths(1);
        LocalDate twoMonthsAgo = LocalDate.now().minusMonths(2);
        LocalDate threeMonthsAgo = LocalDate.now().minusMonths(3);
        
        previousMonthPayroll = DbFixtures.aPayrollForContractAndMonth(contract.id, lastMonth);
        DbFixtures.aPayrollForContractAndMonth(contract.id, twoMonthsAgo);
        DbFixtures.aPayrollForContractAndMonth(contract.id, threeMonthsAgo);
    }

    @When("[P.D.4] I delete previous month payroll for that mechanic")
    public void whenIDeletePreviousMonthPayrollForThatMechanic() throws BusinessException {
        service.deleteLastGeneratedOfMechanicId(mechanic.id);
    }

    @Then("[P.D.4] the previous month payroll is removed")
    public void thenThePreviousMonthPayrollIsRemoved() {
        List<TPayrollsRecord> allPayrolls = Db.findAllBy(TPayrollsRecord.class, 
        		"contract_id", contract.id
        	);
        
        assertEquals(2, allPayrolls.size());
        boolean found = allPayrolls.stream()
			.anyMatch( p -> p.id.equals(previousMonthPayroll.id) );
        assertFalse( found, "The payroll should be deleted" );
    }
}