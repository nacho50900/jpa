
package uo.ri.cws.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uo.ri.util.MechanicBuilder;
import uo.ri.util.ProfessionalGroupBuilder;

/**
 * Scenarios:
 * - Contract terminated before one year of service: no compensation
 * - Contract terminated after one year of service: compensation for 12 months
 * - Contract terminated after 18 months of service: compensation for 12 months
 * - Contract terminated after 30 months of service: compensation for 24 months
 * - Contract terminated with a date before the end of month: end date is set to
 *  the last day of that month
 *  - Contract terminated with a date before the starting date: exception
 *  - Contract terminated with a null date: exception
 *  - Contract already terminated: exception
 */
public class ContractTerminateTests {
    private static final double ANNUAL_GROSS_SALARY = 51100;
    private static final double MONTH_GROSS_SALARY = ANNUAL_GROSS_SALARY / 12;
    private static final double DAILY_GROSS_SALARY = ANNUAL_GROSS_SALARY / 365;
    private static final LocalDate SIGNING_DATE = LocalDate.now().minusDays(100);
    private static final ContractType TYPE = new ContractType("INDEFINITE", 2.5);
    
    private static final int FIFTEEN_DAYS = 15;
	private static final int MONTHS_IN_YEAR = 12;

    private Contract contract;

    @BeforeEach
    public void setUp() {
        Mechanic mechanic = new MechanicBuilder().build();
        ProfessionalGroup group = new ProfessionalGroupBuilder().build();
        contract = new Contract(mechanic, TYPE, group, SIGNING_DATE, ANNUAL_GROSS_SALARY);
    }

	/**
	 * GIVEN: A contract with specific attributes 
	 * WHEN: The contract is terminated before one year of service 
	 * THEN: The settlement is zero 
	 * AND: The contract is marked as terminated
	 */
    @Test
    public void testWithNoCompensation() {
        LocalDate endDate = contract.getStartDate().plusDays(15);

        contract.terminate(endDate);

        assertTrue(contract.getSettlement() == 0.0);
        assertTrue(contract.isTerminated());
        assertFalse(contract.isInForce());
    }

    /**
     * GIVEN: A contract
     * WHEN: The contract is terminated twice
     * THEN: An IllegalStateException is thrown
     */
    @Test
    public void testAlreadyFinished() {
        LocalDate endDate = contract.getStartDate().plusDays(15);

        contract.terminate(endDate);
        assertThrows(
        		IllegalStateException.class, 
        		() -> contract.terminate(endDate)
        	);
    }
    
	/**
	 * GIVEN: A contract with specific attributes 
	 * WHEN: The contract is terminated after one year of service 
	 * THEN: The settlement is properly calculated 
	 * AND: The contract is marked as terminated
	 */
    @Test
    public void testWith12MonthsCompensation() {
    	int durationInMonths = MONTHS_IN_YEAR; // 18 months
        LocalDate start = contract.getStartDate();
        LocalDate end = start.plusMonths(durationInMonths).minusDays(FIFTEEN_DAYS);

        addPayrollsTo(contract, durationInMonths);

        double expected = 
        		1 /* year */
        		* DAILY_GROSS_SALARY 
        		* TYPE.getCompensationDaysPerYear();

        contract.terminate(end);

        assertEquals(expected, contract.getSettlement(), 0.001);
        assertTrue(contract.isTerminated());
    }

	/**
	 * GIVEN: A contract with 18 months of service
	 * WHEN: The contract is terminated after one and a half year of service 
	 * THEN: The settlement is properly calculated for 12 months only
	 * AND: The contract is marked as terminated
	 */
    @Test
    public void testWith18MonthsCompensation() {
    	int durationInMonths = MONTHS_IN_YEAR + 6; // 18 months
        LocalDate start = contract.getStartDate();
        LocalDate end = start.plusMonths( durationInMonths );

        addPayrollsTo(contract, durationInMonths);

        double expected = 
        		1 /* year */
        		* DAILY_GROSS_SALARY 
        		* TYPE.getCompensationDaysPerYear();

        contract.terminate(end);

        assertEquals(expected, contract.getSettlement(), 0.001);
        assertTrue(contract.isTerminated());
    }

	/**
	 * GIVEN: A contract with 30 months of service
	 * WHEN: The contract is terminated after TWO and a half year of service 
	 * THEN: The settlement is properly calculated for 24 months only
	 * AND: The contract is marked as terminated
	 */
    @Test
    public void testWith30MonthsCompensation() {
    	int durationInMonths = 2 * MONTHS_IN_YEAR + 6; // 30 months
        LocalDate startDate = contract.getStartDate();
        LocalDate endDate = startDate
        			.plusMonths(durationInMonths)
        			.minusDays(FIFTEEN_DAYS);

        addPayrollsTo(contract, durationInMonths);

        LocalDate expectedEndDate = endDate.with(TemporalAdjusters.lastDayOfMonth());
        double expectedAmount = 
        			2 /* years */
        			* DAILY_GROSS_SALARY 
        			* TYPE.getCompensationDaysPerYear();

        contract.terminate(endDate);

        assertEquals(expectedAmount, contract.getSettlement(), 0.001);
        assertEquals(expectedEndDate, contract.getEndDate());
        assertTrue(contract.isTerminated());
    }

    /**
	 * GIVEN: A contract 
	 * WHEN: The contract is terminated with a date before the end of month 
	 * THEN: The contract end date is set to the last day of that month
	 */
    @Test
	public void testEndDateIsLastDayOfMonth() {
		LocalDate endDate = contract.getStartDate()
				.plusMonths(1)
				.withDayOfMonth(15);
		LocalDate expected = endDate.with(TemporalAdjusters.lastDayOfMonth());

		contract.terminate(endDate);
		
		assertEquals(expected, contract.getEndDate());
	}
    
    /**
	 * GIVEN: A contract 
	 * WHEN: The contract is terminated with a date before the starting date 
	 * THEN: An IllegalArgumentException is thrown
	 */
    @Test
	public void testEndDateBeforeStartDate() {
		LocalDate endDate = contract.getStartDate().minusDays(1);

		assertThrows(
				IllegalArgumentException.class,
				() -> contract.terminate(endDate)
			);
	}
    
	/**
	 * GIVEN: A contract 
	 * WHEN: The contract is terminated with a null date 
	 * THEN: An IllegalArgumentException is thrown
	 */
    @Test
    public void testEndDateIsNull() {
        assertThrows(
			IllegalArgumentException.class,
			() -> contract.terminate(null)
		);
    }
    
	/**
	 * Adds a number of payrolls to a contract, simulating that they were
	 * created when the contract was in force.
	 * 
	 * @param c   the contract to which the payrolls are added
	 * @param qty the number of payrolls (months) to add
	 */
    private void addPayrollsTo(Contract c, int qty) {
        LocalDate payrollDate = contract.getStartDate().plusMonths(1).minusDays(1);
        
        for (int i = 0; i < qty /*months*/; i++) {
        	Payroll mockPayroll = mock(Payroll.class);
        	when( mockPayroll.getGrossSalary() ).thenReturn( MONTH_GROSS_SALARY );
        	when( mockPayroll.getDate() ).thenReturn( payrollDate);
        	
        	c._getPayrolls().add( mockPayroll ); // Simulate association with back door
        	
            payrollDate = payrollDate.plusMonths(1);
        }
    }

}

