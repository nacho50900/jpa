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
 * - A finished work order can be marked as invoiced when it has Invoiced attached
 * - A finished work order cannot be marked as invoiced if it has not Invoiced attached, throws ISE
 * - An OPEN work order cannot be marked as invoiced, throws ISE
 * - An ASSIGNED work order cannot be marked as invoiced, throws ISE
 * - An invoiced work order cannot be marked as invoiced, throws ISE
 */
class WorkOrderMarkAsInvoicedTests {
	
	private Vehicle vehicle;
	private Mechanic mechanic;

	@BeforeEach
	void setUp() {
		vehicle = new Vehicle("1234 GJI", "ibiza", "seat");
		mechanic = new Mechanic("nif-mecanico", "nombre", "apellidos");
	}
	
	/**
	 * GIVEN: A work order in state FINISHED with an invoice attached 
	 * WHEN: marking it as invoiced 
	 * THEN: the work order state is INVOICED
	 */
	@Test
	void testMarkAsInvoiced() {
		WorkOrder wo = new WorkOrder(vehicle);
		wo.assignTo(mechanic);
		wo.markAsFinished();
		Invoice invoice = new Invoice(1000L);
		Associations.Bills.link(invoice, wo);

		wo.markAsInvoiced();
		
		assertTrue( wo.isInvoiced() );
	}
	
	/**
	 * GIVEN: A work order in state FINISHED without an invoice
	 * WHEN: marking it as invoiced
	 * THEN: an IllegalStateException is thrown
	 */ 
	 @Test
	 void testMarkAsInvoicedFromFinishedWorkOrderWithoutInvoice() {
		 WorkOrder wo = new WorkOrder(vehicle);
         wo.assignTo(mechanic);
         wo.markAsFinished();

         assertThrows(IllegalStateException.class, () -> wo.markAsInvoiced());
         assertTrue( wo.isFinished() );
     }
	 
	 /**
	  * GIVEN: A work order in state OPEN
	  * WHEN: marking it as invoiced
	  * THEN: an IllegalStateException is thrown
	  */
	 @Test
	void testMarkAsInvoicedFromOpenWorkOrder() {
		WorkOrder wo = new WorkOrder(vehicle);

		assertThrows(IllegalStateException.class, () -> wo.markAsInvoiced());
		assertTrue(wo.isOpen());
	}

	/**
	 * GIVEN: A work order in state ASSIGNED 
	 * WHEN: marking it as invoiced 
	 * THEN: an IllegalStateException is thrown
	 */
	 @Test
	void testMarkAsInvoicedFromAssignedWorkOrder() {
		WorkOrder wo = new WorkOrder(vehicle);
		wo.assignTo(mechanic);

		assertThrows(IllegalStateException.class, () -> wo.markAsInvoiced());
		assertTrue(wo.isAssigned());
	}
	 
	/**
     * GIVEN: A work order in state INVOICED 
     * WHEN: marking it as invoiced 
     * THEN: an IllegalStateException is thrown
     */
     @Test
	void testMarkAsInvoicedFromInvoicedWorkOrder() {
		WorkOrder wo = new WorkOrder(vehicle);
		wo.assignTo(mechanic);
		wo.markAsFinished();
		Invoice invoice = new Invoice(1000L);
		Associations.Bills.link(invoice, wo);
		wo.markAsInvoiced();

		assertThrows(IllegalStateException.class, () -> wo.markAsInvoiced());
		assertTrue(wo.isInvoiced());
	}
     
}