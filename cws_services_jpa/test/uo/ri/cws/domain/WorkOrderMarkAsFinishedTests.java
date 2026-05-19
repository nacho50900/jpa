package uo.ri.cws.domain;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * To better understand this tests, please review the WorkOrder state diagram
 * see the "project scope statement" document
 * 
 * Scenarios:
 * - An ASSIGNED work order can be marked as finished
 * - An OPEN work order cannot be marked as finished, throws ISE
 * - A finished work order cannot be marked as finished, throws ISE
 * - An invoiced work order cannot be marked as finished, throws ISE
 */
class WorkOrderMarkAsFinishedTests {

	private Vehicle vehicle;
	private Mechanic mechanic;

	@BeforeEach
	void setUp() {
		vehicle = new Vehicle("1234 GJI", "ibiza", "seat");
		mechanic = new Mechanic("nif-mecanico", "nombre", "apellidos");
	}

	/**
	 * GIVEN: A work order in state ASSIGNED
	 * WHEN: marking it as finished
	 * THEN: the work order state is FINISHED
	 * AND: the work order is no longer assigned to the mechanic
	 */
	@Test
	void testMarkAsFinished() {
		WorkOrder wo = new WorkOrder(vehicle);
		wo.assignTo(mechanic);

		wo.markAsFinished();
		assertTrue( wo.isFinished() );
		assertTrue( wo.getMechanic() == null );
		assertFalse( mechanic.getAssigned().contains(wo) );
	}
	
	/**
	 * GIVEN: A work order in state OPEN
	 * WHEN: marking it as finished
	 * THEN: an IllegalStateException is thrown
	 */
	@Test
	void testMarkAsFinishedFromOpenWorkOrder() {
		WorkOrder wo = new WorkOrder(vehicle);

		assertThrows(IllegalStateException.class, () -> wo.markAsFinished());
		assertTrue(wo.isOpen());
	}
	
	/**
	 * GIVEN: A work order in state FINISHED 
	 * WHEN: marking it as finished 
	 * THEN: an IllegalStateException is thrown
	 */
	@Test
	void testMarkAsFinishedFromFinishedWorkOrder() {
		WorkOrder wo = new WorkOrder(vehicle);
		wo.assignTo(mechanic);
		wo.markAsFinished();

		assertThrows(IllegalStateException.class, () -> wo.markAsFinished());
		assertTrue(wo.isFinished());
	}
	
	/**
	 * GIVEN: A work order in state INVOICED 
	 * WHEN: marking it as finished 
	 * THEN: an IllegalStateException is thrown
	 */
	@Test
	void testMarkAsFinishedFromInvoicedWorkOrder() {
		WorkOrder wo = new WorkOrder(vehicle);
		wo.assignTo(mechanic);
		wo.markAsFinished();
		new Invoice(1000L, List.of(wo)); // wo is now invoiced

		assertThrows(IllegalStateException.class, () -> wo.markAsFinished());
		assertTrue(wo.isInvoiced());
	}

}