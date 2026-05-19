package uo.ri.cws.application.service.payroll.deleteall;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Date;
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
 * Scenario: [P.Da.4] There are payrolls from several months ago including 
 * 		previous month
 */
public class ScenarioPDa4 {
    private PayrollService service = Factories.service.forPayrollService();

    private LocalDate previousMonth;
	private int initialPayrollCount;

	private int numOfDeleted;

    @Given("[P.Da.4] several payrolls of several mechanics from several month ago including previous month")
    public void givenSeveralPayrollsOfSeveralMechanicsFromSeveralMonthAgoIncludingPreviousMonth() {
    	previousMonth = LocalDate.now().minusMonths(1);

    	// Create payrolls for different months including previous month
		List<LocalDate> months = List.of(
   			previousMonth,
			LocalDate.now().minusMonths(2),
			LocalDate.now().minusMonths(3),
			LocalDate.now().minusMonths(4)
		);

        DbFixtures.somePayrollsWithContractAtDates( months );
        DbFixtures.somePayrollsWithContractAtDates( months );
        DbFixtures.somePayrollsWithContractAtDates( months );
        // We created 3 payrolls for previous month
        
        initialPayrollCount = Db.findAll(TPayrollsRecord.class).size();
    }

    @When("[P.Da.4] I delete previous month payrolls")
    public void whenIDeletePreviousMonthPayrolls() throws BusinessException {
        numOfDeleted = service.deleteLastGenerated();
    }

    @Then("[P.Da.4] all previous month payrolls are deleted")
    public void thenAllPreviousMonthPayrollAreDeleted() {
    	assertEquals(3, numOfDeleted); 
        int expectedRemainingCount = initialPayrollCount - 3;

        List<TPayrollsRecord> remainingPayrolls = Db.findAll(TPayrollsRecord.class);
        assertEquals(expectedRemainingCount, remainingPayrolls.size());
        
        // Verify no payrolls from previous month remain
        boolean hasPreviousMonthPayroll = remainingPayrolls.stream()
            .anyMatch(p -> p.date.equals( Date.valueOf(previousMonth) ));
        
        assertTrue(!hasPreviousMonthPayroll, "No previous month payrolls should remain");
    }
}