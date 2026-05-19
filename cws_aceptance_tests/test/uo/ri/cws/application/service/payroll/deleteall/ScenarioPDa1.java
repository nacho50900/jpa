package uo.ri.cws.application.service.payroll.deleteall;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.payroll.PayrollService;
import uo.ri.cws.application.service.util.dbfixture.Db;
import uo.ri.cws.application.service.util.dbfixture.records.TPayrollsRecord;
import uo.ri.util.exception.BusinessException;

/**
 * Feature: Delete all Payrolls previous month
 * Scenario: [P.Da.1] There is no payroll at all
 */
public class ScenarioPDa1 {
    private PayrollService service = Factories.service.forPayrollService();
	private int numOfDeleted;

    @When("[P.Da.1] I delete previous month payrolls")
    public void whenIDeletePreviousMonthPayrolls() throws BusinessException {
        numOfDeleted = service.deleteLastGenerated();
    }

    @Then("[P.Da.1] there is no payroll")
    public void thenThereIsNoPayroll() {
		assertEquals(0, numOfDeleted);
		
        List<TPayrollsRecord> payrolls = Db.findAll(TPayrollsRecord.class);
        assertEquals(0, payrolls.size());
    }
}