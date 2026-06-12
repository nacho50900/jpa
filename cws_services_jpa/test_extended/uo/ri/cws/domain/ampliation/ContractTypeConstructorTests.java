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
 *  - Full constructor with negative compensation days throws exception
 *  - Full constructor with zero compensation days is valid (no severance contract)
 *  - A contract linked to a contract type appears in its contracts collection
 *  - Two contracts linked to the same contract type both appear in its collection
 */
public class ContractTypeConstructorTests {

	private static final double COMPENSATION_DAYS = 30.0;
	private static final String NAME = "INDEFINITE";

	/**
	 * GIVEN: A name and compensation days per year
	 * WHEN: The constructor is invoked with valid data
	 * THEN: All attributes are correctly set
	 * AND: The contracts collection is initially empty
	 */
	@Test
	public void testFullConstructor() {
		ContractType type = new ContractType(NAME, COMPENSATION_DAYS);

		assertEquals(NAME, type.getName());
		assertEquals(COMPENSATION_DAYS, type.getCompensationDaysPerYear(), 0.001);
		assertTrue(type.getContracts().isEmpty());
	}

	/**
	 * WHEN: A contract type is created with a null name
	 * THEN: An IllegalArgumentException is thrown
	 */
	@Test
	public void testNullNameThrowsException() {
		assertThrows(
				IllegalArgumentException.class,
				() -> new ContractType(null, COMPENSATION_DAYS)
			);
	}

	/**
	 * WHEN: A contract type is created with an empty name
	 * THEN: An IllegalArgumentException is thrown
	 */
	@Test
	public void testEmptyNameThrowsException() {
		assertThrows(
				IllegalArgumentException.class,
				() -> new ContractType("", COMPENSATION_DAYS)
			);
	}

	/**
	 * WHEN: A contract type is created with negative compensation days
	 * THEN: An IllegalArgumentException is thrown
	 */
	@Test
	public void testNegativeCompensationDaysThrowsException() {
		assertThrows(
				IllegalArgumentException.class,
				() -> new ContractType(NAME, -1.0)
			);
	}

	/**
	 * GIVEN: A contract type with zero compensation days
	 * WHEN: The constructor is invoked
	 * THEN: The contract type is created successfully
	 * AND: getCompensationDaysPerYear returns zero
	 */
	@Test
	public void testZeroCompensationDaysIsValid() {
		ContractType type = new ContractType(NAME, 0.0);

		assertEquals(0.0, type.getCompensationDaysPerYear(), 0.001);
	}

	/**
	 * GIVEN: A contract type
	 * WHEN: A contract is created with that type
	 * THEN: The contract appears in the type's contracts collection
	 */
	@Test
	public void testContractTypeGetsContract() {
		ContractType type = new ContractTypeBuilder().build();
		Mechanic mechanic = new MechanicBuilder().build();
		ProfessionalGroup group = new ProfessionalGroupBuilder().build();

		Contract c = new Contract(mechanic, type, group,
				java.time.LocalDate.now(), 24000.0);

		assertTrue(type.getContracts().contains(c));
		assertEquals(1, type.getContracts().size());
	}

	/**
	 * GIVEN: A contract type
	 * WHEN: Two contracts are created with that type
	 * THEN: Both contracts appear in the type's contracts collection
	 */
	@Test
	public void testContractTypeGetsTwoContracts() {
		ContractType type = new ContractTypeBuilder().build();
		ProfessionalGroup group = new ProfessionalGroupBuilder().build();
		Mechanic mechanic1 = new MechanicBuilder().build();
		Mechanic mechanic2 = new MechanicBuilder().build();

		Contract c1 = new Contract(mechanic1, type, group,
				java.time.LocalDate.now(), 24000.0);
		Contract c2 = new Contract(mechanic2, type, group,
				java.time.LocalDate.now(), 18000.0);

		assertTrue(type.getContracts().contains(c1));
		assertTrue(type.getContracts().contains(c2));
		assertEquals(2, type.getContracts().size());
	}

}