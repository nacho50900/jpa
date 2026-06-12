package uo.ri.cws.domain.ampliation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uo.ri.cws.domain.Contract;
import uo.ri.cws.domain.ContractType;
import uo.ri.cws.domain.Mechanic;
import uo.ri.cws.domain.ProfessionalGroup;
import uo.ri.util.ContractBuilder;
import uo.ri.util.ContractTypeBuilder;
import uo.ri.util.MechanicBuilder;
import uo.ri.util.ProfessionalGroupBuilder;

/**
 * Scenarios:
 *  - A new mechanic has no contracts initially
 *  - A contract appears in the mechanic's contracts collection
 *  - A contract holds a reference back to its mechanic
 *  - Two contracts for the same mechanic both appear in its collection
 *  - A terminated contract remains in the mechanic's contracts collection
 */
public class MechanicContractAssociationTests {

	private Mechanic mechanic;
	private ContractType type;
	private ProfessionalGroup group;

	@BeforeEach
	public void setUp() {
		mechanic = new MechanicBuilder().build();
		type     = new ContractTypeBuilder().build();
		group    = new ProfessionalGroupBuilder().build();
	}

	/**
	 * GIVEN: A newly created mechanic
	 * WHEN: Its contracts collection is checked
	 * THEN: It is empty
	 */
	@Test
	public void testNewMechanicHasNoContracts() {
		assertTrue(mechanic.getContracts().isEmpty());
	}

	/**
	 * GIVEN: A mechanic
	 * WHEN: A contract is created for that mechanic
	 * THEN: The contract appears in the mechanic's contracts collection
	 */
	@Test
	public void testContractAppearsInMechanic() {
		Contract c = new ContractBuilder()
				.forMechanic(mechanic)
				.withType(type)
				.withProfessionalGroup(group)
				.build();

		assertTrue(mechanic.getContracts().contains(c));
		assertEquals(1, mechanic.getContracts().size());
	}

	/**
	 * GIVEN: A mechanic
	 * WHEN: A contract is created for that mechanic
	 * THEN: The contract holds a reference back to its mechanic
	 */
	@Test
	public void testContractLinksBackToMechanic() {
		Contract c = new ContractBuilder()
				.forMechanic(mechanic)
				.withType(type)
				.withProfessionalGroup(group)
				.build();

		assertSame(mechanic, c.getMechanic());
	}

	/**
	 * GIVEN: A mechanic with one contract already terminated
	 * WHEN: A second contract is created for that mechanic
	 * THEN: Both contracts appear in the mechanic's contracts collection
	 */
	@Test
	public void testTwoContractsAppearInMechanic() {
		LocalDate today       = LocalDate.now();
		LocalDate nextMonth   = today.plusMonths(1);

		Contract c1 = new ContractBuilder()
				.forMechanic(mechanic)
				.withType(type)
				.withProfessionalGroup(group)
				.withSigningDate(today)
				.build();
		c1.terminate(c1.getStartDate().plusDays(1));

		Contract c2 = new ContractBuilder()
				.forMechanic(mechanic)
				.withType(type)
				.withProfessionalGroup(group)
				.withSigningDate(nextMonth)
				.build();

		assertTrue(mechanic.getContracts().contains(c1));
		assertTrue(mechanic.getContracts().contains(c2));
		assertEquals(2, mechanic.getContracts().size());
	}

	/**
	 * GIVEN: A mechanic with one contract in force
	 * WHEN: The contract is terminated
	 * THEN: It remains in the mechanic's contracts collection
	 * AND: It is marked as terminated
	 */
	@Test
	public void testTerminatedContractRemainsInMechanic() {
		Contract c = new ContractBuilder()
				.forMechanic(mechanic)
				.withType(type)
				.withProfessionalGroup(group)
				.build();

		c.terminate(c.getStartDate().plusDays(1));

		assertTrue(mechanic.getContracts().contains(c));
		assertTrue(c.isTerminated());
	}

}