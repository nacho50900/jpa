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
 * Scenario: [P.Da.3] There are payrolls but all for the previous month
 */
public class ScenarioPDa3 {
    private PayrollService service = Factories.service.forPayrollService();
	private int initialPayrollCount;
	private int numOfDeleted;

    @Given("[P.Da.3] several payrolls of several mechanics all for the previous month")
    public void givenSeveralPayrollsOfSeveralMechanicsAllForThePreviousMonth() {
        // Create payrolls for different months but not previous month
    	List<LocalDate> justOneMonth = List.of(
			LocalDate.now().minusMonths(1)
		);

        DbFixtures.somePayrollsWithContractAtDates( justOneMonth );
        DbFixtures.somePayrollsWithContractAtDates( justOneMonth );
        DbFixtures.somePayrollsWithContractAtDates( justOneMonth );
        
        initialPayrollCount = Db.findAll(TPayrollsRecord.class).size();
    }

    @When("[P.Da.3] I delete previous month payrolls")
    public void whenIDeletePreviousMonthPayrolls() throws BusinessException {
        numOfDeleted = service.deleteLastGenerated();
    }

    @Then("[P.Da.3] all the payrolls are deleted")
    public void thenAllThePayrollsAreDeleted() {
    	assertEquals(initialPayrollCount, numOfDeleted);
        List<TPayrollsRecord> payrolls = Db.findAll(TPayrollsRecord.class);
        assertEquals(0, payrolls.size());
    }
}