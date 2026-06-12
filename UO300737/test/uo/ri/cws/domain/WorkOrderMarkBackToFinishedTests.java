package uo.ri.cws.domain;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * To better understand this tests, please review the WorkOrder state diagram
 * see the "project scope statement" document
 * 
 * Scenarios:
 * - A INVOICED work order can be marked back to finished
 * - An OPEN work order cannot be marked back to finished, throws ISE
 * - An ASSIGNED work order cannot be marked back to finished, throws ISE
 * - A FINISHED work order cannot be marked back to finished, throws ISE
 */
class WorkOrderMarkBackToFinishedTests {

	private Vehicle vehicle;
	private Mechanic mechanic;

	@BeforeEach
	void setUp() {
		vehicle = new Vehicle("1234 GJI", "ibiza", "seat");
		mechanic = new Mechanic("nif-mecanico", "nombre", "apellidos");
	}
	
	/**
	 * GIVEN: A work order in state INVOICED 
	 * WHEN: marking it back to finished
	 * THEN: the work order state is FINISHED
	 */
	@Test
	void testMarkBackToFinished() {
		WorkOrder wo = new WorkOrder(vehicle);
		wo.assignTo(mechanic);
		wo.markAsFinished();
		Invoice invoice = new Invoice(1000L);
		Associations.Bills.link(invoice, wo);
		wo.markAsInvoiced();

		wo.markBackToFinished();

		assertTrue(wo.isFinished());
	}
	
	/**
	 * GIVEN: A work order in state OPEN
	 * WHEN: marking it back to finished
	 * THEN: an IllegalStateException is thrown
	 */
	@Test
	void testMarkBackToFinishedFromOpenWorkOrder() {
		WorkOrder wo = new WorkOrder(vehicle);

        assertThrows(IllegalStateException.class, () -> wo.markBackToFinished());
        assertTrue(wo.isOpen());
	}
	
	/**
	 * GIVEN: A work order in state ASSIGNED
	 * WHEN: marking it back to finished
	 * THEN: an IllegalStateException is thrown
	 */
	@Test
	void testMarkBackToFinishedFromAssignedWorkOrder() {
		WorkOrder wo = new WorkOrder(vehicle);
		wo.assignTo(mechanic);

		assertThrows(IllegalStateException.class, () -> wo.markBackToFinished());
		assertTrue(wo.isAssigned());
	}
	
	/**
	 * GIVEN: A work order in state FINISHED
	 * WHEN: marking it back to finished
	 * THEN: an IllegalStateException is thrown
	 */
	@Test
	void testMarkBackToFinishedFromFinishedWorkOrder() {
		WorkOrder wo = new WorkOrder(vehicle);
		wo.assignTo(mechanic);
		wo.markAsFinished();

		assertThrows(IllegalStateException.class, () -> wo.markBackToFinished());
		assertTrue(wo.isFinished());
	}	

}