package uo.ri.cws.domain;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * To better understand this tests, please review the WorkOrder state diagram
 * see the "project scope statement" document
 * 
 * 
 * Scenarios:
 * - An ASSIGNED work order can be unassigned from a mechanic
 * - An OPEN work order cannot be unassigned, throws ISE
 * - A finished work order cannot be unassigned, throws ISE
 * - An invoiced work order cannot be unassigned, throws ISE
 */
class WorkOrderUnassignTests {
	
	private Vehicle vehicle;
	private Mechanic mechanic;
	private WorkOrder workorder;

	@BeforeEach
	void setUp() {
		vehicle = new Vehicle("1234 GJI", "make", "model");
		mechanic = new Mechanic("nif-1", "name-1", "surname-1");
		workorder = new WorkOrder(vehicle, "falla la junta la trocla");
	}
	
	/**
	 * GIVEN: A work order in state ASSIGNED 
	 * WHEN: unassigning it 
	 * THEN: the work order is unassigned from the mechanic 
	 * AND: the work order state is OPEN
	 */
	@Test
	void testunassignFromMechanic() {
		workorder.assignTo(mechanic);

		workorder.unassign();

		assertTrue( workorder.isOpen() );
		assertNull( workorder.getMechanic() );
		assertFalse( mechanic.getAssigned().contains(workorder) );
	}

	/**
	 * GIVEN: A work order in state OPENED 
	 * WHEN: unassigning it 
	 * THEN: an IllegalStateException is thrown
	 */
	@Test
	void testUnassignFromOpenWorkOrder() {
		assertThrows(IllegalStateException.class, () -> workorder.unassign());
		assertTrue(workorder.isOpen());
	}
	
	/**
	 * GIVEN: A work order in state FINISHED 
	 * WHEN: unassigning it 
	 * THEN: an IllegalStateException is thrown
	 */
	@Test
	void testUnassignFromFinishedWorkOrder() {
		workorder.assignTo(mechanic);
		workorder.markAsFinished();

		assertThrows(IllegalStateException.class, () -> workorder.unassign());
		assertTrue(workorder.isFinished());
	}
	 
	/**
	 * GIVEN: A work order in state INVOICED 
	 * WHEN: unassigning it 
	 * THEN: an IllegalStateException is thrown
	 */
	@Test
	void testUnassignFromInvoicedWorkOrder() {
		workorder.assignTo(mechanic);
		workorder.markAsFinished();
		new Invoice(100L, List.of(workorder) );

		assertThrows(IllegalStateException.class, () -> workorder.unassign());
		assertTrue(workorder.isInvoiced());
	}

}