package uo.ri.cws.application.service.payroll.generate;

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
 * Feature: Generate payrolls for mechanics last month
 * Scenario: [P.G.1] Generate payrolls but no mechanic
 */
public class ScenarioPG1 {
    private PayrollService service = Factories.service.forPayrollService();

    @When("[P.G.1] I generate payrolls")
    public void whenIGeneratePayrolls() throws BusinessException {
        service.generateForPreviousMonth();
    }

    @Then("[P.G.1] zero payrolls are generated")
    public void thenZeroPayrollsAreGenerated() {
    	List<TPayrollsRecord> loaded = Db.findAll(TPayrollsRecord.class);
        assertEquals(0, loaded.size());
    }
}