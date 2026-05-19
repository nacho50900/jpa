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
 * Scenario: [P.Da.2] There are payrolls but not of previous month
 */
public class ScenarioPDa2 {
    private PayrollService service = Factories.service.forPayrollService();
    private int initialPayrollCount;
	private int numOfDeleted;

    @Given("[P.Da.2] several payrolls of several mechanics not of the previous month")
    public void givenSeveralPayrollsOfSeveralMechanicsNotOfThePreviousMonth() {
        // Create payrolls for different months but not previous month
    	List<LocalDate> months = List.of(
			LocalDate.now().minusMonths(2),
			LocalDate.now().minusMonths(3),
			LocalDate.now().minusMonths(4)
		);

        DbFixtures.somePayrollsWithContractAtDates( months );
        DbFixtures.somePayrollsWithContractAtDates( months );
        DbFixtures.somePayrollsWithContractAtDates( months );
        
        initialPayrollCount = Db.findAll(TPayrollsRecord.class).size();
    }

    @When("[P.Da.2] I delete previous month payrolls")
    public void whenIDeletePreviousMonthPayrolls() throws BusinessException {
        numOfDeleted = service.deleteLastGenerated();
    }

    @Then("[P.Da.2] no payroll was deleted")
    public void thenNoPayrollWasDeleted() {
    	assertEquals(0, numOfDeleted);
        List<TPayrollsRecord> payrolls = Db.findAll(TPayrollsRecord.class);
        assertEquals(initialPayrollCount, payrolls.size());
    }
}