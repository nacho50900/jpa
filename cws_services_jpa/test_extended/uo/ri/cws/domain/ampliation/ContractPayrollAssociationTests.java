package uo.ri.cws.domain.ampliation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uo.ri.cws.domain.Contract;
import uo.ri.cws.domain.Mechanic;
import uo.ri.cws.domain.Payroll;
import uo.ri.util.ContractBuilder;
import uo.ri.util.MechanicBuilder;

/**
 * Scenarios:
 *  - A new contract has no payrolls initially
 *  - A payroll created for a contract is linked to that contract
 *  - A payroll created for a contract appears in its payrolls collection
 *  - Two payrolls created for the same contract both appear in its collection
 *  - A payroll links back to exactly its contract
 */
public class ContractPayrollAssociationTests {

	private static final LocalDate THREE_MONTHS_AGO = LocalDate.now().minusMonths(3);
	private static final LocalDate TWO_MONTHS_AGO   = LocalDate.now().minusMonths(2);
	private static final LocalDate LAST_MONTH       = LocalDate.now().minusMonths(1);

	private Contract contract;

	@BeforeEach
	public void setUp() {
		Mechanic mechanic = new MechanicBuilder().build();
		contract = new ContractBuilder()
				.forMechanic(mechanic)
				.withSigningDate(THREE_MONTHS_AGO)
				.build();
	}

	/**
	 * GIVEN: A newly created contract
	 * WHEN: Its payrolls collection is checked
	 * THEN: It is empty
	 */
	@Test
	public void testNewContractHasNoPayrolls() {
		assertTrue(contract.getPayrolls().isEmpty());
	}

	/**
	 * GIVEN: A contract
	 * WHEN: A payroll is created for that contract
	 * THEN: The payroll appears in the contract's payrolls collection
	 */
	@Test
	public void testPayrollAppearsInContract() {
		Payroll p = new Payroll(contract, LAST_MONTH);

		assertTrue(contract.getPayrolls().contains(p));
		assertEquals(1, contract.getPayrolls().size());
	}

	/**
	 * GIVEN: A contract
	 * WHEN: A payroll is created for that contract
	 * THEN: The payroll holds a reference back to its contract
	 */
	@Test
	public void testPayrollLinksBackToContract() {
		Payroll p = new Payroll(contract, LAST_MONTH);

		assertSame(contract, p.getContract());
	}

	/**
	 * GIVEN: A contract
	 * WHEN: Two payrolls are created for that contract in different months
	 * THEN: Both payrolls appear in the contract's payrolls collection
	 */
	@Test
	public void testTwoPayrollsAppearInContract() {
		Payroll p1 = new Payroll(contract, TWO_MONTHS_AGO);
		Payroll p2 = new Payroll(contract, LAST_MONTH);

		assertTrue(contract.getPayrolls().contains(p1));
		assertTrue(contract.getPayrolls().contains(p2));
		assertEquals(2, contract.getPayrolls().size());
	}

}