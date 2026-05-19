package uo.ri.cws.application.service.contract.terminate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.contract.ContractCrudService;
import uo.ri.cws.application.service.util.dbfixture.Db;
import uo.ri.cws.application.service.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.util.dbfixture.records.TContractsRecord;
import uo.ri.cws.application.service.util.dbfixture.records.TMechanicsRecord;
import uo.ri.util.date.Dates;
import uo.ri.util.exception.BusinessException;

/**
 * Feature: Terminate a contract
 * Scenario: [C.T.1] Terminate a contract in force shorter than a year
 */
public class ScenarioCT1 {
    private static final int SIX_MONTHS = 6;
	private ContractCrudService service = Factories.service.forContractCrudService();
    private TContractsRecord contract;
    private TMechanicsRecord mechanic;

    @Given("[C.T.1] an in-force contract shorter than a year with its payrolls")
    public void givenInForceContractShorterThanYearWithPayrolls() {
        mechanic = DbFixtures.aMechanic();
        contract = DbFixtures.aContractWithPayrollsForMechanicOf(mechanic.id, SIX_MONTHS);
        
        // Set contract start date to less than a year ago
        contract.startDate = Date.valueOf(LocalDate.now().minusMonths(SIX_MONTHS));
        contract.endDate = null;
        Db.update(contract);
    }

    @When("[C.T.1] I terminate that contract")
    public void whenITerminateThatContract() throws BusinessException {
    	service.terminate(contract.id);
    }

    @Then("[C.T.1] there is no active contract for the mechanic")
    public void thenNoActiveContractForMechanic() {
        List<TContractsRecord> contracts = Db.findAllBy(TContractsRecord.class, 
        		"mechanic_Id", mechanic.id
        	);
        boolean hasActive = contracts.stream()
        		.anyMatch(c -> "IN_FORCE".equals(c.state));
        
        assertFalse(hasActive);
    }

    @Then("[C.T.1] end date of the terminated contract is set to end day in the month")
    public void thenEndDateIsEndDayInMonth() {
        TContractsRecord updated = Db.findById(TContractsRecord.class, contract.id);
        assertTrue( 
        		Dates.isLastDayOfMonth( updated.endDate.toLocalDate() ),
        		"Must be last day of month"
        	);
    }

    @Then("[C.T.1] there is no settlement")
    public void thenNoSettlement() {
        TContractsRecord updated = Db.findById(TContractsRecord.class, contract.id);
        assertEquals(0, updated.settlement);
    }
}
