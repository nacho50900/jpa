package uo.ri.cws.domain.ampliation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import uo.ri.cws.domain.Contract;
import uo.ri.cws.domain.ContractType;
import uo.ri.cws.domain.Mechanic;
import uo.ri.cws.domain.ProfessionalGroup;
import uo.ri.util.ContractTypeBuilder;
import uo.ri.util.MechanicBuilder;
import uo.ri.util.ProfessionalGroupBuilder;

/**
 * Scenarios:
 *  - Full constructor with valid data
 *  - Full constructor with null name throws exception
 *  - Full constructor with empty name throws exception
 *  - Full constructor with negative triennium salary throws exception
 *  - Full constructor with negative productivity rate throws exception
 *  - A contract linked to a professional group appears in its contracts collection
 *  - Two contracts linked to the same professional group both appear in its collection
 */
public class ProfessionalGroupConstructorTests {

	private static final String NAME = "SENIOR";
	private static final double TRIENNIUM_SALARY = 46.74;
	private static final double PRODUCTIVITY_RATE = 0.05;

	/**
	 * GIVEN: A name, triennium salary and productivity rate
	 * WHEN: The constructor is invoked with valid data
	 * THEN: All attributes are correctly set
	 * AND: The contracts collection is initially empty
	 */
	@Test
	public void testFullConstructor() {
		ProfessionalGroup group = new ProfessionalGroup(
				NAME, TRIENNIUM_SALARY, PRODUCTIVITY_RATE);

		assertEquals(NAME, group.getName());
		assertEquals(TRIENNIUM_SALARY, group.getTrienniumPayment(), 0.001);
		assertEquals(PRODUCTIVITY_RATE, group.getProductivityRate(), 0.001);
		assertTrue(group.getContracts().isEmpty());
	}

	/**
	 * WHEN: A professional group is created with a null name
	 * THEN: An IllegalArgumentException is thrown
	 */
	@Test
	public void testNullNameThrowsException() {
		assertThrows(
				IllegalArgumentException.class,
				() -> new ProfessionalGroup(null, TRIENNIUM_SALARY, PRODUCTIVITY_RATE)
			);
	}

	/**
	 * WHEN: A professional group is created with an empty name
	 * THEN: An IllegalArgumentException is thrown
	 */
	@Test
	public void testEmptyNameThrowsException() {
		assertThrows(
				IllegalArgumentException.class,
				() -> new ProfessionalGroup("", TRIENNIUM_SALARY, PRODUCTIVITY_RATE)
			);
	}

	/**
	 * WHEN: A professional group is created with a negative triennium salary
	 * THEN: An IllegalArgumentException is thrown
	 */
	@Test
	public void testNegativeTrienniumSalaryThrowsException() {
		assertThrows(
				IllegalArgumentException.class,
				() -> new ProfessionalGroup(NAME, -1.0, PRODUCTIVITY_RATE)
			);
	}

	/**
	 * WHEN: A professional group is created with a negative productivity rate
	 * THEN: An IllegalArgumentException is thrown
	 */
	@Test
	public void testNegativeProductivityRateThrowsException() {
		assertThrows(
				IllegalArgumentException.class,
				() -> new ProfessionalGroup(NAME, TRIENNIUM_SALARY, -0.01)
			);
	}

	/**
	 * GIVEN: A professional group
	 * WHEN: A contract is created with that group
	 * THEN: The contract appears in the group's contracts collection
	 */
	@Test
	public void testProfessionalGroupGetsContract() {
		ProfessionalGroup group = new ProfessionalGroupBuilder().build();
		Mechanic mechanic = new MechanicBuilder().build();
		ContractType type = new ContractTypeBuilder().build();

		Contract c = new Contract(mechanic, type, group,
				java.time.LocalDate.now(), 24000.0);

		assertTrue(group.getContracts().contains(c));
		assertEquals(1, group.getContracts().size());
	}

	/**
	 * GIVEN: A professional group
	 * WHEN: Two contracts are created with that group
	 * THEN: Both contracts appear in the group's contracts collection
	 */
	@Test
	public void testProfessionalGroupGetsTwoContracts() {
		ProfessionalGroup group = new ProfessionalGroupBuilder().build();
		ContractType type = new ContractTypeBuilder().build();
		Mechanic mechanic1 = new MechanicBuilder().build();
		Mechanic mechanic2 = new MechanicBuilder().build();

		Contract c1 = new Contract(mechanic1, type, group,
				java.time.LocalDate.now(), 24000.0);
		Contract c2 = new Contract(mechanic2, type, group,
				java.time.LocalDate.now(), 18000.0);

		assertTrue(group.getContracts().contains(c1));
		assertTrue(group.getContracts().contains(c2));
		assertEquals(2, group.getContracts().size());
	}

}