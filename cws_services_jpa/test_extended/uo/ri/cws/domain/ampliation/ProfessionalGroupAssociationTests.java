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
import uo.ri.util.ContractTypeBuilder;
import uo.ri.util.MechanicBuilder;

/**
 * Scenarios:
 *  - A new professional group has no contracts initially
 *  - A contract holds a reference to its professional group
 *  - A contract appears in the professional group's contracts collection
 *  - Two contracts with the same professional group both appear in its collection
 */
public class ProfessionalGroupAssociationTests {

	private Mechanic mechanic;
	private ContractType type;
	private ProfessionalGroup group;

	@BeforeEach
	public void setUp() {
		mechanic = new MechanicBuilder().build();
		type     = new ContractTypeBuilder().build();
		group    = new ProfessionalGroup("SENIOR", 46.74, 0.05);
	}

	/**
	 * GIVEN: A newly created professional group
	 * WHEN: Its contracts collection is checked
	 * THEN: It is empty
	 */
	@Test
	public void testNewProfessionalGroupHasNoContracts() {
		assertTrue(group.getContracts().isEmpty());
	}

	/**
	 * GIVEN: A professional group
	 * WHEN: A contract is created with that group
	 * THEN: The contract holds a reference to its professional group
	 */
	@Test
	public void testContractLinksToItsGroup() {
		Contract c = new Contract(mechanic, type, group,
				LocalDate.now(), 24000.0);

		assertSame(group, c.getProfessionalGroup());
	}

	/**
	 * GIVEN: A professional group
	 * WHEN: A contract is created with that group
	 * THEN: The contract appears in the group's contracts collection
	 */
	@Test
	public void testContractAppearsInProfessionalGroup() {
		Contract c = new Contract(mechanic, type, group,
				LocalDate.now(), 24000.0);

		assertTrue(group.getContracts().contains(c));
		assertEquals(1, group.getContracts().size());
	}

	/**
	 * GIVEN: A professional group with two contracts
	 * WHEN: Its contracts collection is checked
	 * THEN: Both contracts appear in it
	 */
	@Test
	public void testTwoContractsAppearInProfessionalGroup() {
		Mechanic mechanic2 = new MechanicBuilder().build();

		Contract c1 = new Contract(mechanic,  type, group,
				LocalDate.now(), 24000.0);
		Contract c2 = new Contract(mechanic2, type, group,
				LocalDate.now(), 18000.0);

		assertTrue(group.getContracts().contains(c1));
		assertTrue(group.getContracts().contains(c2));
		assertEquals(2, group.getContracts().size());
	}

}