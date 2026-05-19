package uo.ri.cws.application.service.contract.terminate;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Date;
import java.time.LocalDate;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.contract.ContractCrudService;
import uo.ri.cws.application.service.util.dbfixture.Db;
import uo.ri.cws.application.service.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.util.dbfixture.records.TContractsRecord;
import uo.ri.cws.application.service.util.dbfixture.records.TMechanicsRecord;
import uo.ri.util.exception.BusinessException;

/**
 * Feature: Terminate a contract
 * Scenario: [C.T.2] Terminate a contract in force larger than a year
 */
public class ScenarioCT2 {
    private static final int YEARS_OF_CONTRACT = 2;
	private static final int PAYROLLS_PER_YEAR = 12;
	private static final double BASE_MONTH_SALARY = 1000.0;  
	private static final double COMPENSATION_DAYS_PER_YEAR = 25;

	private ContractCrudService service = Factories.service.forContractCrudService();
    private TContractsRecord contract;
    private TMechanicsRecord mechanic;
	private double expectedSettlement;

    @Given("[C.T.2] an in-force contract longer than a year with its payrolls")
    public void givenInForceContractLongerThanYearWithPayrolls() {
        mechanic = DbFixtures.aMechanic();

        double grossMonthlySalary = BASE_MONTH_SALARY + 200.0 * Math.random();
        contract = ContractFixtures.aContractForMechanicWithFixedPayrolls(
    			Date.valueOf(LocalDate.now().minusYears(YEARS_OF_CONTRACT)), // start date
    			mechanic.id, 
    			YEARS_OF_CONTRACT * PAYROLLS_PER_YEAR,	// 24 payrolls
    			grossMonthlySalary,
    			COMPENSATION_DAYS_PER_YEAR
    		);
        
        expectedSettlement = 
        		PAYROLLS_PER_YEAR 
        		* grossMonthlySalary // <- annual salary
        		/ 365.0 			 // <- daily salary
        		* COMPENSATION_DAYS_PER_YEAR
        		* YEARS_OF_CONTRACT; 
    }

    @When("[C.T.2] I terminate that contract")
    public void whenITerminateThatContract() throws BusinessException {
        service.terminate(contract.id);
    }

    @Then("[C.T.2] settlement is properlly calculated")
    public void thenSettlementIsProperlyCalculated() {
        TContractsRecord updated = Db.findById(TContractsRecord.class, contract.id);
        
		assertEquals(expectedSettlement, updated.settlement, 0.001);
    }
}
