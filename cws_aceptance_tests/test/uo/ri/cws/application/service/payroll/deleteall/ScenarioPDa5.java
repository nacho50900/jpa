package uo.ri.cws.application.service.payroll.deleteall;

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
import uo.ri.cws.application.service.util.dbfixture.records.TPayrollsRecord;
import uo.ri.util.exception.BusinessException;

/**
 * Feature: Delete all Payrolls previous month
 * Scenario: [P.Da.5] Deleting payrolls twice in a row
 */
public class ScenarioPDa5 {
    private PayrollService service = Factories.service.forPayrollService();
    private int payrollCountAfterFirstDeletion;
	private int numOfDeletions;

    @Given("[P.Da.5] several payrolls of several mechanics from several month ago including previous month")
    public void givenSeveralPayrollsOfSeveralMechanicsFromSeveralMonthAgoIncludingPreviousMonth() {

    	// Create payrolls for different months including previous month
		List<LocalDate> months = List.of(
   			LocalDate.now().minusMonths(1), // previous month
			LocalDate.now().minusMonths(2),
			LocalDate.now().minusMonths(3),
			LocalDate.now().minusMonths(4)
		);

        DbFixtures.somePayrollsWithContractAtDates( months );
        DbFixtures.somePayrollsWithContractAtDates( months );
        DbFixtures.somePayrollsWithContractAtDates( months );
    }

    @Given("[P.Da.5] I delete previous month payrolls")
    public void givenIDeletePreviousMonthPayrolls() throws BusinessException {
        service.deleteLastGenerated();
        payrollCountAfterFirstDeletion = Db.findAll(TPayrollsRecord.class).size();
    }

    @When("[P.Da.5] I \\(re)delete previous month payrolls")
    public void whenIReDeletePreviousMonthPayrolls() throws BusinessException {
        numOfDeletions = service.deleteLastGenerated();
    }

    @Then("[P.Da.5] no payroll was deleted")
    public void thenNoPayrollWasDeleted() {
    	assertEquals(0, numOfDeletions);
    	
        List<TPayrollsRecord> payrolls = Db.findAll(TPayrollsRecord.class);
        assertEquals(payrollCountAfterFirstDeletion, payrolls.size());
    }
}