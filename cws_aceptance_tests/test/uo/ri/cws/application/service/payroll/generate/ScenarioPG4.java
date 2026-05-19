package uo.ri.cws.application.service.payroll.generate;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.payroll.PayrollService;
import uo.ri.cws.application.service.payroll.PayrollService.PayrollDto;
import uo.ri.cws.application.service.util.dbfixture.Db;
import uo.ri.cws.application.service.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.util.dbfixture.records.TMechanicsRecord;
import uo.ri.cws.application.service.util.dbfixture.records.TPayrollsRecord;
import uo.ri.util.exception.BusinessException;

/**
 * Feature: Generate payrolls for mechanics last month
 * Scenario: [P.G.4] Generate payrolls only for mechanics with an active contract.
 */
public class ScenarioPG4 {
    private PayrollService service = Factories.service.forPayrollService();
	private LocalDate today;
	private List<PayrollDto> generated;

    @Given("[P.G.4] today is {string}")
    public void givenTodayIs(String todayStr) {
        today = LocalDate.parse(todayStr);
    }

    @Given("[P.G.4] the following relation of mechanics with a contract")
    public void givenTheFollowingRelationOfMechanicsWithContract(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        
        for (Map<String, String> row : rows) {
            String nif = row.get("nif");
            String state = row.get("state");
            String startDateStr = row.get("startDate");
            String endDateStr = row.get("endDate");
            
            Date startDate = Date.valueOf( LocalDate.parse( startDateStr ));
    		Date endDate = (endDateStr == null) 
    				? null 
    				: Date.valueOf(LocalDate.parse( endDateStr ));
    		
            TMechanicsRecord mechanic = findOrCreateMechanic(nif);
			DbFixtures.aContractWithTypeAndGroupForMechanicOf(
            		mechanic.id, 
            		startDate,
            		endDate,
            		state
            	);
        }
    }

	@When("[P.G.4] I generate payrolls")
    public void whenIGeneratePayrolls() throws BusinessException {
        generated = service.generateForPreviousMonthOf( today );
    }

    @Then("[P.G.4] two payrolls are generated")
    public void thenTwoPayrollsAreGenerated() {
    	// checks on the returned object
    	assertEquals(2, generated.size());
    	
    	// checks on the database
    	List<TPayrollsRecord> loaded = Db.findAll(TPayrollsRecord.class);
        assertEquals(2, loaded.size());
    }

    private TMechanicsRecord findOrCreateMechanic(String nif) {
    	TMechanicsRecord m = Db.findBy(TMechanicsRecord.class, "nif", nif);
		return (m == null) ? DbFixtures.aMechanicOf(nif) : m;
	}

}