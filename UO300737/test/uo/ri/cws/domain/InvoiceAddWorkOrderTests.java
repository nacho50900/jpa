package uo.ri.cws.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * To better understand this tests, please review the WorkOrder state diagram
 * see the "project scope statement" document
 *
 * Scenarios:
 * - Add a work order to an invoice, the amount is correctly computed 
 * - Add two work orders to an invoice, the amount is correctly computed 
 * - Add a work order to an invoice, the work order state changes to INVOICED 
 * - Add two work orders to an invoice, both work orders change their state to INVOICED
 * - Add a null work order to an invoice, throws IAE
 * - If the date of the invoice is before is before 1/7/2012 the VAT (IVA) is 18%
 */
class InvoiceAddWorkOrderTests {

	private static final double WO_AMOUNT = 100.0;
	private static final double VAT_RATE = 0.21;
	
	private WorkOrder workOrder;

	@BeforeEach
	void setUp() {
		workOrder = mock(WorkOrder.class);
		when( workOrder.isFinished() ).thenReturn( true );
		when( workOrder.getAmount() ).thenReturn( WO_AMOUNT );
	}

	/**
	 * GIVEN: An invoice with no work orders
	 * WHEN: adding a work order to the invoice
	 * THEN: the amount of the invoice is the amount of the work order
	 * AND: the work order and the invoice are linked
	 * AND: the work order state changes to INVOICED
	 * AND: the invoice state is NOT_YET_PAID
	 * AND: the work order markAsInvoiced method is invoked once
	 */
	@Test
	void testNewAmountAfterAddingWorkOrder() {
		Invoice invoice = new Invoice(1000L);
		
		invoice.addWorkOrder(workOrder);

		double expected = WO_AMOUNT * (1 + VAT_RATE);
		assertEquals(expected, invoice.getAmount(), 0.001);

		assertTrue(invoice.getWorkOrders().contains(workOrder));
		//assertEquals(invoice, workOrder.getInvoice()); mock limitation
		
		assertTrue( invoice.isNotSettled() );
		verify(workOrder, times(1)).markAsInvoiced();
	}

	/**
	 * GIVEN: An invoice with one work order
	 * WHEN: adding another work order to the invoice
	 * THEN: the amount of the invoice is the sum of the amounts of both work orders
	 * AND: both work orders and the invoice are linked
	 * AND: both work orders change their state to INVOICED
	 */
	@Test
	void testInvoiceAmountAddingTwoWorkOrders() {
		Invoice invoice = new Invoice(0L);
		WorkOrder anotherWo = createAnotherWorkOrder();
		invoice.addWorkOrder( workOrder );
		invoice.addWorkOrder( anotherWo );

		double expected = 2 * WO_AMOUNT * (1 + VAT_RATE);
		assertEquals(expected, invoice.getAmount(), 0.001);
		assertTrue( invoice.isNotSettled() );
		verify(workOrder, times(1)).markAsInvoiced();
		verify(anotherWo, times(1)).markAsInvoiced();
	}

	/**
	 * GIVEN: An invoice with one work order 
	 * WHEN: adding a null work order
	 * THEN: an IllegalArgumentException is thrown
	 */
	@Test
	void testAddNullWorkOrderThrowsIAE() {
		Invoice invoice = new Invoice(0L);
		
		assertThrows(
				IllegalArgumentException.class, 
				() -> invoice.addWorkOrder(null)
			);
	}
	
	/**
	 * GIVEN: An invoice with one work order
	 * WHEN: the date of the invoice is before 1/7/2012
	 * THEN: the VAT (IVA) is 18%
	 */
	@Test
	void testAmountForInvoicesPriorJuly2012() {
		LocalDate JUNE_6_2012 = LocalDate.of(2012, 6, 15);

		Invoice invoice = new Invoice(0L, JUNE_6_2012);
		invoice.addWorkOrder(workOrder);

		double expected = WO_AMOUNT * (1 + 0.18);
		assertEquals(expected, invoice.getAmount(), 0.001);
	}
	
	/**
	 * Creates a new invoice with a delay of 100 milliseconds to avoid a
	 * collision in the dates field (same millisecond)
	 *
	 * As the identity of the work order is the date (timestamp, milliseconds 
	 * precision)) we sleep 100 milliseconds to avoid collisions
	 */
	private WorkOrder createAnotherWorkOrder() {
		sleep(100); // to avoid collisions in the date field
		
		WorkOrder workOrder = mock(WorkOrder.class);
		when( workOrder.isFinished() ).thenReturn( true );
		when( workOrder.getAmount() ).thenReturn( WO_AMOUNT );
		return workOrder;
	}

	private void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException ignored) {
			// dont't care if this occurs
		}
	}

}