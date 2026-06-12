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
import uo.ri.util.MechanicBuilder;
import uo.ri.util.ProfessionalGroupBuilder;

/**
 * Scenarios:
 *  - A new contract type has no contracts initially
 *  - A contract holds a reference to its contract type
 *  - A contract appears in the contract type's contracts collection
 *  - Unlinking a contract removes it from the contract type's collection
 *  - Two contracts with the same type both appear in its collection
 */
public class ContractTypeAssociationTests {

	private Mechanic mechanic;
	private ProfessionalGroup group;
	private ContractType type;

	@BeforeEach
	public void setUp() {
		mechanic = new MechanicBuilder().build();
		group    = new ProfessionalGroupBuilder().build();
		type     = new ContractType("INDEFINITE", 2.5);
	}

	/**
	 * GIVEN: A newly created contract type
	 * WHEN: Its contracts collection is checked
	 * THEN: It is empty
	 */
	@Test
	public void testNewContractTypeHasNoContracts() {
		assertTrue(type.getContracts().isEmpty());
	}

	/**
	 * GIVEN: A contract type
	 * WHEN: A contract is created with that type
	 * THEN: The contract holds a reference to its contract type
	 */
	@Test
	public void testContractLinksToItsType() {
		Contract c = new Contract(mechanic, type, group,
				LocalDate.now(), 24000.0);

		assertSame(type, c.getContractType());
	}

	/**
	 * GIVEN: A contract type
	 * WHEN: A contract is created with that type
	 * THEN: The contract appears in the type's contracts collection
	 */
	@Test
	public void testContractAppearsInContractType() {
		Contract c = new Contract(mechanic, type, group,
				LocalDate.now(), 24000.0);

		assertTrue(type.getContracts().contains(c));
		assertEquals(1, type.getContracts().size());
	}

	/**
	 * GIVEN: A contract type with two contracts
	 * WHEN: Its contracts collection is checked
	 * THEN: Both contracts appear in it
	 */
	@Test
	public void testTwoContractsAppearInContractType() {
		Mechanic mechanic2 = new MechanicBuilder().build();

		Contract c1 = new Contract(mechanic,  type, group,
				LocalDate.now(), 24000.0);
		Contract c2 = new Contract(mechanic2, type, group,
				LocalDate.now(), 18000.0);

		assertTrue(type.getContracts().contains(c1));
		assertTrue(type.getContracts().contains(c2));
		assertEquals(2, type.getContracts().size());
	}

}