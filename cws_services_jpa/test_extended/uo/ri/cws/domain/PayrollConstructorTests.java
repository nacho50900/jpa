package uo.ri.cws.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uo.ri.util.ContractBuilder;
import uo.ri.util.ContractTypeBuilder;
import uo.ri.util.MechanicBuilder;
import uo.ri.util.ProfessionalGroupBuilder;
import uo.ri.util.random.Random;

/**
 * Scenarios:
 *	  - Date of the payroll is before the contract start date
 *    - Date of the payroll is correct
 *    - Payroll and contract are linked each other
 *    - One month old contract without interventions
 *    - Six months old contract without interventions (bonus)
 *    - Three months old contract with interventions
 *    - 2 year, 11 month old contract without interventions (no triennium)
 *    - 3 year old contract without interventions (with triennium)
 *    - 10 year old contract, with 30.000 €/year of base year salary and interventions
 *    - Contract terminated in February, one moth old at that moment
 */
public class PayrollConstructorTests {

    private static final double BASE_SALARY = 10000; 
    private static final double TAX_RATE_FOR_BASE_SALARY = 0.19; // 19% 
    
    // 14 payments in 12 payrolls!
    private static final int PAYMENTS_IN_YEAR = 14;
    private static final int PAYROLLS_IN_YEAR = 12;
    
    private static final double STANDARD_NIC = 0.05;

    private static final double BETTER_BASE_SALARY = BASE_SALARY * 3;
    private static final double TAX_RATE_FOR_BETTER_BASE_SALARY = 0.3; // 30%
    private static final double TRIENNIUM_SALARY = 30;
    private static final double TOTAL_BY_INTERVENTIONS = 10000;
    private static final String PG_NAME = "testPG";
    private static final String DNI = Random.string(10);

    private ProfessionalGroup professionalGroup;
	private ContractType contractType;
    private Mechanic mechanic;

    @BeforeEach
    public void setUp() throws Exception {
        contractType = new ContractTypeBuilder().build();
        mechanic = new MechanicBuilder().withDni(DNI).build();
        professionalGroup = new ProfessionalGroupBuilder()
        		.withName(PG_NAME)
                .withTrienniumSalary(TRIENNIUM_SALARY )
                .build();
    }

    /**
     * GIVEN: a date previous to the contract start date 
     * WHEN: try to create a payroll for that date
     * THEN: it throws IllegalArgumentException
     */
    @Test
    public void testNoPayroll() {
        LocalDate contractSigningDate = LocalDate.of(2019, 02, 10);
        LocalDate payrollDate = LocalDate.of(2019, 01, 01);
        Contract c = createContract(contractSigningDate, BASE_SALARY);
        
        // the payroll will be for the previous month, must throw exception
        assertThrows(IllegalArgumentException.class,
                () -> new Payroll(c, payrollDate)
            );
    }

    /**
     * GIVEN: a contract starting at 10/10/2018
     * WHEN: create a payroll for 31/01/2019
     * THEN: the payroll is created with date 31/01/2019
     */
    @Test
    public void testPayrollDate() {
        LocalDate contractSigningDate = LocalDate.of(2018, 10, 10);
        LocalDate payrollDate = LocalDate.of(2019, 01, 31);
        LocalDate expectedLocalDate = LocalDate.of(2019, 01, 31);
        Contract c = createContract(contractSigningDate, BASE_SALARY);
    
        Payroll payrollOfJanuary = new Payroll(c, payrollDate);
        
        assertEquals(expectedLocalDate, payrollOfJanuary.getDate());
    }

    /**
     * GIVEN: a contract
     * WHEN: create a payroll for that contract
     * THEN: the payroll and the contract are linked each other
     */
    @Test
    public void testLinkedPayrollAndContract() {
        LocalDate contractSigningDate = LocalDate.now().minusMonths(6);
        Contract c = createContract(contractSigningDate, BASE_SALARY);
        
        Payroll p = new Payroll(c, LocalDate.now());

        assertTrue(c.getPayrolls().contains(p));
        assertTrue(p.getContract() == c);
    }

    /**
     * GIVEN: a one month old contract without interventions
     * WHEN: create a payroll for that contract
     * THEN: the payroll has right values for all its concepts
     */
    @Test
    public void testForJanuaryPayroll() {
        LocalDate january = LocalDate.of(2019, 01, 10);
        LocalDate endOfMarch = LocalDate.of(2019, 03, 31);
        Contract c = createContract(january, BASE_SALARY);
        
        double[] expected = new double[] {
	        /* base salary */ BASE_SALARY / PAYMENTS_IN_YEAR,
	        /* extra salary */ 0.0, 
	        /* productivity */ 0.0,
	        /* trienniums */ 0.0, 
	        /* irpf */ 0.0, // assigned below
	        /* social security */ BASE_SALARY / PAYROLLS_IN_YEAR * STANDARD_NIC 
        };
        /* irpf */ expected[4] = TAX_RATE_FOR_BASE_SALARY
                	* (expected[0] + expected[1] + expected[2] + expected[3]);

        Payroll payrollOfMarch = new Payroll(c, endOfMarch);

        assertRightValues(expected, payrollOfMarch);
    }

    /**
     * GIVEN: a six months old contract without interventions
     * WHEN: create a payroll for June
     * THEN: the payroll has bonus (extra) and the right values for its concepts
     */
    @Test
    public void testForJunePayroll() {
        LocalDate january = LocalDate.of(2019, 01, 10);
        LocalDate endOfJune = LocalDate.of(2019, 06, 30);
        Contract c = createContract(january, BASE_SALARY);

        double[] expected = new double[] {
            /* base salary */ BASE_SALARY / PAYMENTS_IN_YEAR,
            /* extra salary */ BASE_SALARY / PAYMENTS_IN_YEAR,
            /* productivity */ 0.0, 
            /* trienniums */ 0.0, 
	        /* irpf */ 0.0, // assigned below
            /* social security */ BASE_SALARY / PAYROLLS_IN_YEAR * STANDARD_NIC 
        };
        /* irpf */ expected[4] = TAX_RATE_FOR_BASE_SALARY
                	* (expected[0] + expected[1] + expected[2] + expected[3]);

        Payroll payrollOfJune = new Payroll(c, endOfJune);

        assertRightValues(expected, payrollOfJune);
    }

    /**
     * GIVEN: a three months old contract with interventions
     * WHEN: create a payroll for March
     * THEN: the payroll has right values for all its concepts
     */
    @Test
    public void testPayrollWithInterventions() {
        LocalDate january = LocalDate.of(2019, 01, 10);
        LocalDate endOfMarch = LocalDate.of(2019, 03, 31);
		LocalDate interventionDate = endOfMarch.minusDays(10);
        Contract c = createContract(january, BASE_SALARY);
        addInterventions(TOTAL_BY_INTERVENTIONS, interventionDate);

        double productivityRate = professionalGroup.getProductivityRate();
		double[] expected = new double[] {
            /* base salary */ BASE_SALARY / PAYMENTS_IN_YEAR,
            /* extra salary */ 0.0,
            /* productivity */ TOTAL_BY_INTERVENTIONS * productivityRate,
            /* trienniums */ 0.0, 
	        /* irpf */ 0.0, // assigned below
            /* social security */ BASE_SALARY / PAYROLLS_IN_YEAR * STANDARD_NIC 
        };
        /* irpf */ expected[4] = TAX_RATE_FOR_BASE_SALARY
                	* (expected[0] + expected[1] + expected[2] + expected[3]);

        Payroll payrollOfMarch = new Payroll(c, endOfMarch);

        assertRightValues(expected, payrollOfMarch);
    }

	/**
     * GIVEN: a 2 year, 11 month old contract without interventions
     * WHEN: create a payroll for November
     * THEN: the payroll has right values for all its concepts
     */
    @Test
    public void testPayrollNoTriennium() {
        LocalDate december = LocalDate.of(2014, 12, 10);
        LocalDate endOfOctober = LocalDate.of(2017, 10, 31);
        Contract c = createContract(december, BASE_SALARY);

        double[] expected = new double[] {
		    /* base salary */ BASE_SALARY / PAYMENTS_IN_YEAR,
		    /* extra salary */ 0.0, 
		    /* productivity */ 0.0,
		    /* trienniums */ 0.0, 
	        /* irpf */ 0.0, // assigned below
		    /* social security */ BASE_SALARY / PAYROLLS_IN_YEAR * STANDARD_NIC 
        };
        /* irpf */ expected[4] = TAX_RATE_FOR_BASE_SALARY
                	* (expected[0] + expected[1] + expected[2] + expected[3]);

        Payroll payrollOfOctober2017 = new Payroll(c, endOfOctober);

        assertRightValues(expected, payrollOfOctober2017);
    }

    /**
     * GIVEN: a 3 year old contract without interventions
     * WHEN: create a payroll for March
     * THEN: the payroll has right values for all its concepts
     */
    @Test
    public void testPayrollWithTriennium() {
        LocalDate february = LocalDate.of(2015, 02, 10);
        LocalDate endOfFebruary2018 = LocalDate.of(2018, 02, 28);
        Contract c = createContract(february, BASE_SALARY);

        double[] expected = new double[] {
            /* base salary */ BASE_SALARY / PAYMENTS_IN_YEAR,
            /* extra salary */ 0.0, 
            /* productivity */ 0.0,
            /* trienniums */ 1 * TRIENNIUM_SALARY, 
	        /* irpf */ 0.0, // assigned below
            /* social security */ BASE_SALARY / PAYROLLS_IN_YEAR * STANDARD_NIC 
        };
        /* irpf */ expected[4] = TAX_RATE_FOR_BASE_SALARY
        			* (expected[0] + expected[1] + expected[2] + expected[3]);

        Payroll payrollOfFebruary2018 = new Payroll(c, endOfFebruary2018);
        
        assertRightValues(expected, payrollOfFebruary2018);
    }

    /**
     * GIVEN: a 10 year old contract, with 30.000 €/year of base year salary and interventions
     * WHEN: create a payroll for December with
     * THEN: the payroll has right values for all its concepts
     */
    @Test
    public void testForCompletePayroll() {
        LocalDate january2010 = LocalDate.of(2010, 01, 10);
        LocalDate endOfDecember2019 = LocalDate.of(2019, 12, 31);
        Contract c = createContract(january2010, BETTER_BASE_SALARY);
        addInterventions(TOTAL_BY_INTERVENTIONS, endOfDecember2019);

        double[] expected = new double[] {
            /* base salary */ BETTER_BASE_SALARY / PAYMENTS_IN_YEAR,
            /* extra salary */ BETTER_BASE_SALARY / PAYMENTS_IN_YEAR,
            /* productivity */ TOTAL_BY_INTERVENTIONS
                    * professionalGroup.getProductivityRate(),
            /* trienniums */ 3 * TRIENNIUM_SALARY, 
            /* irpf */ 0.0, // see below 
            /* social security */ BETTER_BASE_SALARY / PAYROLLS_IN_YEAR * STANDARD_NIC 
        };
        /* irpf */ expected[4] = TAX_RATE_FOR_BETTER_BASE_SALARY
                	* (expected[0] + expected[1] + expected[2] + expected[3]);

        Payroll payrollOfDecember2019 = new Payroll(c, endOfDecember2019);

        assertRightValues(expected, payrollOfDecember2019);
    }

    /**
     * GIVEN: a contract terminated in February, one moth old at that moment
     * WHEN: create a payroll for February
     * THEN: the payroll has right values for all its concepts
     */    
    @Test
    public void testPayrollTerminatedContract() {
    	LocalDate january2010 = LocalDate.of(2010, 01, 10);
        LocalDate february2010 = LocalDate.of(2010, 02, 02);
        Contract c = createContract(january2010, BETTER_BASE_SALARY);

        double[] expected = new double[] {
            /* base salary */ BETTER_BASE_SALARY / PAYMENTS_IN_YEAR,
            /* extra salary */ 0.0, 
            /* productivity */ 0.0,
            /* trienniums */ 0.0, 
	        /* irpf */ 0.0, // assigned below
            /* social security */ BETTER_BASE_SALARY / PAYROLLS_IN_YEAR * STANDARD_NIC 
        };
		/* irpf */ expected[4] = TAX_RATE_FOR_BETTER_BASE_SALARY
					* (expected[0] + expected[1] + expected[2] + expected[3]);

		c.terminate(february2010);
		LocalDate endOfFebruary2010 = february2010.withDayOfMonth(28);
        Payroll payrollOfFebruary2010 = new Payroll(c, endOfFebruary2010);
        
        assertRightValues(expected, payrollOfFebruary2010);
    }

	private Contract createContract(LocalDate signDate, double baseSalary) {
		return new ContractBuilder()
        		.forMechanic(mechanic)
        		.withSigningDate(signDate)
                .withBaseSalary(baseSalary)          
                .withProfessionalGroup(professionalGroup)
                .withType(contractType)
                .build();
	}
    
	/*
	 * Adds 2 interventions to the mechanic that amounts totalByInterventions
	 * It uses Mockito to mock the interventions (avoids creating complex 
	 * graphs of objects)
	 */
    private void addInterventions(double totalByInterventions, LocalDate month) {
    	int n = 2; // number of interventions to create
    	double amount = totalByInterventions / n;
		for (int i = 0; i < n; i++) {
			WorkOrder mockWorkOrder = mock(WorkOrder.class);
			when( mockWorkOrder.isInvoiced() ).thenReturn( true );
			when( mockWorkOrder.getDate() ).thenReturn( month.atStartOfDay() );
			when( mockWorkOrder.getAmount() ).thenReturn( amount );

			Intervention mockIntervention = mock(Intervention.class);
			when( mockIntervention.getWorkOrder() ).thenReturn(mockWorkOrder);
			mechanic._getInterventions().add(mockIntervention); // the back door
		}
	}

    private void assertRightValues(double[] expected, Payroll p) {
        int i = 0;
        assertEquals(expected[i++], p.getMonthlyBaseSalary(), 0.001);
        assertEquals(expected[i++], p.getExtraSalary(), 0.01);
        assertEquals(expected[i++], p.getProductivityEarning(), 0.001);
        assertEquals(expected[i++], p.getTrienniumEarning(), 0.001);

        assertEquals(expected[i++], p.getTaxDeduction(), 0.001);
        assertEquals(expected[i++], p.getNicDeduction(), 0.001);
    }

}
