package uo.ri.cws.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Scenarios:
 * - New invoice with work orders computes the right amount and is in NOT_YET_PAID state
 * - When invoice number is null or date or the list of work orders then an IAE is thrown
 */
class InvoiceConstructorTests {

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
	 * GIVEN: a work order
	 * WHEN: we create an invoice with them
	 * THEN: the invoice computes the right amount
	 * AND: the invoice is in NOT_YET_PAID state
	 * AND: the work orders are in INVOICED state
	 * AND: the work orders are linked to the invoice
	 * AND: the invoice is linked to the work orders
	 * AND: the invoice date is today
	 */
	@Test
	void testNewAmountAfterAddingWorkOrder() {
		Invoice invoice = new Invoice(1000L, List.of(workOrder));

		double expected = WO_AMOUNT * (1 + VAT_RATE);
		assertEquals(expected, invoice.getAmount(), 0.001);

		assertTrue(invoice.getWorkOrders().contains(workOrder));
		//assertEquals(invoice, workOrder.getInvoice()); mock limitation
		
		assertTrue( invoice.isNotSettled() );
		verify(workOrder, times(1)).markAsInvoiced();
	}

	/**
	 * WHEN: we create an invoice with null values
	 * THEN: an IllegalArgumentException is thrown
	 */
	@Test
	void testInvoiceThrowsExceptionIfNull() {
		LocalDate now = LocalDate.now();
		List<WorkOrder> wos = List.of(workOrder);
		
		assertThrows(IllegalArgumentException.class, () -> new Invoice(null));
		assertThrows(IllegalArgumentException.class, () -> new Invoice(null, now, wos));
		assertThrows(IllegalArgumentException.class, () -> new Invoice(1000L, null, wos));
		assertThrows(IllegalArgumentException.class, () -> new Invoice(1000L, now, null));
	}

}