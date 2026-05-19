package uo.ri.cws.application.service.payroll.generate;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.List;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.payroll.PayrollService;
import uo.ri.cws.application.service.payroll.PayrollService.PayrollDto;
import uo.ri.cws.application.service.util.dbfixture.Db;
import uo.ri.cws.application.service.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.util.dbfixture.records.TPayrollsRecord;
import uo.ri.util.exception.BusinessException;

/**
 * Feature: Generate payrolls for mechanics last month
 * Scenario: [P.G.5] Generating payrolls twice in the month has no effect
 */
public class ScenarioPG5 {
    private PayrollService service = Factories.service.forPayrollService();
    private int initialPayrollCount;
    private LocalDate today = LocalDate.now();
	private List<PayrollDto> generated;

    @Given("[P.G.5] several already generated payrolls for this month")
    public void givenSeveralAlreadyGeneratedPayrollsForThisMonth() {
        // Insert several payrolls for the current month
    	List<TPayrollsRecord> inserted = DbFixtures.somelPayrollsForCurrentMonth();
        initialPayrollCount = inserted.size();
    }

    @When("[P.G.5] I generate payrolls again")
    public void whenIGeneratePayrollsAgain() throws BusinessException {
        generated = service.generateForPreviousMonthOf(today);
    }

    @Then("[P.G.5] no new payrolls are generated")
    public void thenNoNewPayrollsAreGenerated() {
    	assertEquals(0, generated.size());
    	
        List<TPayrollsRecord> loaded = Db.findAll(TPayrollsRecord.class);
        assertEquals(initialPayrollCount, loaded.size());
    }
}
