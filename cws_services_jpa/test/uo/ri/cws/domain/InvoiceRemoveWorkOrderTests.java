package uo.ri.cws.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Scenarios: 
 * - Removing a work order from an invoice unlinks, recomputes the amount and 
 * 		puts backs the work order to FINISHED
 * - Removing a work order from an invoice with multiple work orders
 * - Removing a work order from an invoice that does not have throws IAE
 * - Removing a work order from a null invoice throws IAE
 * - Removing a work order from a settled invoice throws ISE
 */
class InvoiceRemoveWorkOrderTests {

	private static final double AMOUNT = 100.0;
	private WorkOrder workOrder;
	private Invoice invoice;

	@BeforeEach
	void setUp() {
		workOrder = mock(WorkOrder.class);
		when(workOrder.isFinished()).thenReturn(true);
		when(workOrder.getAmount()).thenReturn(AMOUNT);

		invoice = new Invoice(1000L);
		invoice.addWorkOrder(workOrder);
	}

	/**
	 * GIVEN: an invoice with a work order
	 * WHEN: removing the work order from the invoice
	 * THEN: the work order is removed from the invoice
	 * AND: the invoice amount is recomputed
	 * AND: the work order is unlinked from the invoice
	 * AND: the work order state is FINISHED
	 */
	@Test
	void testUnlinkOnInvoice() {
		invoice.removeWorkOrder(workOrder);
		
		assertTrue( invoice.getWorkOrders().isEmpty() );
		//assertNull( workOrder.getInvoice()); mock limitation
		assertEquals( 0.0, invoice.getAmount() );
		verify( workOrder ).markBackToFinished();
	}

	/**
	 * GIVEN: an invoice with two work orders
	 * WHEN: removing one of them from the invoice
	 * THEN: the invoice amount is recomputed
	 * AND: the work order is unlinked from the invoice
	 * AND: the work order state goes back to FINISHED
	 */
	@Test
	void testUnlinkOneOfTwoOnInvoice() {
		WorkOrder wo2 = mock(WorkOrder.class);
		when(wo2.isFinished()).thenReturn(true);
		when(wo2.getAmount()).thenReturn(AMOUNT);
		invoice.addWorkOrder(wo2);
		double oldAmount = invoice.getAmount();

		invoice.removeWorkOrder(wo2);

		assertEquals(oldAmount / 2, invoice.getAmount(), 0.001);

		assertFalse(invoice.getWorkOrders().contains(wo2));
		assertEquals(1, invoice.getWorkOrders().size());
		// assertNull( wo2.getInvoice()); mock limitation
		verify(wo2).markBackToFinished();
		verify(workOrder, never()).markBackToFinished();
	}
	
	/**
	 * GIVEN: an invoice with a work order
	 * WHEN: removing a work order that is not in the invoice
	 * THEN: and IAE is thrown 
	 * AND: the invoice amount is not changed
	 */
	@Test
	void testUnlinkWorkOrderNotInInvoice() {
		double oldPrevious = invoice.getAmount();
		WorkOrder wo2 = mock(WorkOrder.class);

		assertThrows(
				IllegalArgumentException.class,
				() -> invoice.removeWorkOrder(wo2)
			);

		assertEquals(oldPrevious, invoice.getAmount(), 0.001);
		assertTrue(invoice.getWorkOrders().contains(workOrder));
		assertEquals(1, invoice.getWorkOrders().size());
		verify(workOrder, never()).markBackToFinished();
		verify(wo2, never()).markBackToFinished();
	}
	
	/**
	 * GIVEN: a settled invoice with a work order
	 * WHEN: removing the work order from the invoice
	 * THEN: an IllegalStateException is thrown
	 */
	@Test
	void testUnlinkFromSettledInvoiceThrowsISE() {
		new Charge(invoice, new Cash( mock(Client.class) ), invoice.getAmount());
		invoice.settle();

		assertThrows(IllegalStateException.class,
				() -> invoice.removeWorkOrder(workOrder)
			);

		assertTrue(invoice.getWorkOrders().contains(workOrder));
		assertEquals(1, invoice.getWorkOrders().size());
		verify(workOrder, never()).markBackToFinished();
	}
	
	/**
	 * GIVEN: an invoice with a work order
	 * WHEN: removing a null work order from the invoice
	 * THEN: an IllegalArgumentException is thrown
	 * AND: the invoice amount is not changed
	 */
	@Test
	void testUnlinkNullWorkOrderThrowsIAE() {
		double oldAmount = invoice.getAmount();

		assertThrows(IllegalArgumentException.class,
				() -> invoice.removeWorkOrder(null)
			);

		assertEquals(oldAmount, invoice.getAmount(), 0.001);
	}
	
}