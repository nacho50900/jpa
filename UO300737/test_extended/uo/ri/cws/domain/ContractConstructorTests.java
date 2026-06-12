
package uo.ri.cws.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uo.ri.util.MechanicBuilder;
import uo.ri.util.ProfessionalGroupBuilder;

/**
 * Scenarios:
 * 	- Full constructor with valid data
 * 	- Full constructor with null mechanic
 * 	- Full constructor with null contract type
 * 	- Full constructor with null professional group
 * 	- Full constructor with null signing date
 * 	- Full constructor with negative salary
 * 	- Full constructor with null end date for FIXED_TERM contract
 * 	- Full constructor with end date before signing date for FIXED_TERM contract
 * 	- Full constructor with end date for non FIXED_TERM contract
 * 	- Short constructor for FIXED_TERM contract
 * 	- Short constructor for non FIXED_TERM contract
 */
public class ContractConstructorTests {
	private static final double ANNUAL_SALARY = 51100;
	private static final LocalDate END_DATE = LocalDate.now();
	private static final LocalDate SIGNING_DATE = END_DATE.minusDays(100);

	private Mechanic mechanic;
	private ProfessionalGroup group;
	private ContractType type;

	@BeforeEach
	public void setUp() {
		type = new ContractType("FIXED_TERM", 2.5);
		mechanic = new MechanicBuilder().build();
		group = new ProfessionalGroupBuilder().build();
	}

	/**
	 * GIVEN: A contract with specific attributes 
	 * WHEN: The full constructor is invoked
	 * THEN: Verify that all attributes are correctly set
	 * AND: The associations are properly established	
	 * AND: The start and end dates are adjusted to the first and last day of their respective months
	 * AND: The contract is in force and has no payrolls initially
	 * AND: The settlement is initialized to zero
	 * AND: The annual base salary is set correctly
	 */
	@Test
	public void testFullConstructor() {
		LocalDate expectedStart = SIGNING_DATE.with(TemporalAdjusters.firstDayOfMonth());
		LocalDate expectedEnd = END_DATE.with(TemporalAdjusters.lastDayOfMonth());

		Contract c = new Contract(mechanic, type, group, 
						SIGNING_DATE, END_DATE, 
						ANNUAL_SALARY
					);
		
		assertEquals(ANNUAL_SALARY, c.getAnnualBaseSalary());
		assertEquals(expectedStart, c.getStartDate());
		assertEquals(expectedEnd, c.getEndDate());
		assertEquals(0.0, c.getSettlement());

		assertSame(type, c.getContractType());
		assertSame(group, c.getProfessionalGroup());
		assertSame(mechanic, c.getMechanic());

		assertTrue( mechanic.getContracts().contains(c) );
		assertTrue( group.getContracts().contains(c) );
		assertTrue( type.getContracts().contains(c) );
		assertTrue(c.isInForce());
		assertTrue(c.getPayrolls().isEmpty());
	}
	
	/**
	 * WHEN: A contract is created with a null mechanic
	 * THEN: An IllegalArgumentException is thrown
	 */
	@Test
	public void testNullMechanicThrowsException() {
		assertThrows(
				IllegalArgumentException.class, 
				() -> new Contract(null,
						type, group, SIGNING_DATE, END_DATE, ANNUAL_SALARY
					)
			);
	}

	/**
	 * WHEN: A contract is created with a null contract type 
	 * THEN: An IllegalArgumentException is thrown
	 */
	@Test
	public void testNullContractTypeThrowsException() {
		assertThrows(
				IllegalArgumentException.class,
				() -> new Contract(mechanic, 
						null, 
						group, SIGNING_DATE, END_DATE, ANNUAL_SALARY
					)
			);
	}

	/**
	 * WHEN: A contract is created with a null professional group 
	 * THEN: An IllegalArgumentException is thrown
	 */
	@Test
	public void testNullProfessionalGroupThrowsException() {
		assertThrows(
				IllegalArgumentException.class,
				() -> new Contract(mechanic, type, 
						null, 
						SIGNING_DATE, END_DATE,	ANNUAL_SALARY
					)
			);
	}

	/**
	 * WHEN: A contract is created with a null signing date 
	 * THEN: An IllegalArgumentException is thrown
	 */
	@Test
	public void testNullSigningDateThrowsException() {
		assertThrows(
				IllegalArgumentException.class,
				() -> new Contract(mechanic, type, group, 
						null, 
						END_DATE, ANNUAL_SALARY
					)
			);
	}

	/**
	 * WHEN: A contract is created with a negative salary 
	 * THEN: An IllegalArgumentException is thrown
	 */
	@Test
	public void testNegativeSalaryThrowsException() {
		assertThrows(
				IllegalArgumentException.class,
				() -> new Contract(mechanic, type, group, SIGNING_DATE,
						END_DATE, 
						-1000.0
					)
			);
	}

	/**
	 * WHEN: A contract of type FIXED_TERM is created with a null end date
	 * THEN: An IllegalArgumentException is thrown
	 */
	@Test
	public void testNullEndDateThrowsException() {
		assertThrows(
				IllegalArgumentException.class,
				() -> new Contract(mechanic, type, group, SIGNING_DATE, 
						null,
						ANNUAL_SALARY
					)
			);
	}

	/**
	 * WHEN: A FIXED_TERM contract is created with an end date earlier than start date 
	 * THEN: An IllegalArgumentException is thrown
	 */
	@Test
	public void testEndDateBeforeStartDateThrowsException() {
		LocalDate invalidEndDate = SIGNING_DATE.minusDays(1);
		assertThrows(
				IllegalArgumentException.class,
				() -> new Contract(mechanic, type, group, SIGNING_DATE,
						invalidEndDate, 
						ANNUAL_SALARY
					)
			);
	}
	
	/**
	 * GIVEN: A non FIXED_TERM contract type
	 * WHEN: A contract with that type is created with an end date 
	 * THEN: The endDate attribute is ignored
	 */
	@Test
	public void testEndDateForNonFixedTermContract() {
		ContractType type = new ContractType("INDEFINITE", 2.5);
		Contract c = new Contract(mechanic, 
						type,
						group, SIGNING_DATE, END_DATE, ANNUAL_SALARY
					);

		assertNull( c.getEndDate() );
	}
	
	/**
	 * GIVEN: A fixed-term contract type
	 * WHEN: A contract with that type is created with the short constructor
	 * THEN: An IllegalArgumentException is thrown because the end date is mandatory
	 */
	@Test
	public void testShortConstructorForFixedTermThrowsException() {
		assertThrows(
				IllegalArgumentException.class,
				() -> new Contract(mechanic, 
						type, 
						group, SIGNING_DATE, ANNUAL_SALARY
					)
			);
	}
	
	/**
	 * GIVEN: A non fixed-term contract type
	 * WHEN: A contract with that type is created with the short constructor
	 * THEN: The contract is created successfully
	 * AND: The end date is null
	 */
	@Test
	public void testShortConstructorForNonFixedTerm() {
		ContractType type = new ContractType("INDEFINITE", 2.5);
		LocalDate expectedStartDate = SIGNING_DATE.with(TemporalAdjusters.firstDayOfMonth());
	
		Contract c = new Contract(mechanic, type, group, SIGNING_DATE, ANNUAL_SALARY);

		assertEquals(ANNUAL_SALARY, c.getAnnualBaseSalary());
		assertEquals(expectedStartDate,	c.getStartDate());
		assertNull(c.getEndDate());
		
		assertEquals(0.0, c.getSettlement());

		assertSame(type, c.getContractType());
		assertSame(group, c.getProfessionalGroup());
		assertSame(mechanic, c.getMechanic());

		assertTrue(mechanic.getContracts().contains(c));
		assertTrue(group.getContracts().contains(c));
		assertTrue(type.getContracts().contains(c));
		assertTrue(c.isInForce());
		assertTrue(c.getPayrolls().isEmpty());
	}
	
}
