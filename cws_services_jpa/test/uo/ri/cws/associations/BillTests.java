package uo.ri.cws.associations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uo.ri.cws.domain.Associations;
import uo.ri.cws.domain.Invoice;
import uo.ri.cws.domain.Vehicle;
import uo.ri.cws.domain.WorkOrder;

class BillTests {
	private WorkOrder workOrder;
	private Vehicle vehicle;
	private Invoice invoice;

	@BeforeEach
	void setUp() {
		vehicle = new Vehicle("1234 GJI", "seat", "ibiza");
		workOrder = new WorkOrder(vehicle, "falla la junta la trocla");
		invoice = new Invoice(0L, LocalDate.now());
		
		/*
		 *  The method "addWorkOrder" in "Invoice" calls
		 *  "Associations.Bill.link(invoice, workOrder)"
		 *  to establish the link between the invoice and the work order.
		 *  
		 *  Therefore, we don't need to call the link explicitly. 
		 *  We do it here just for testing purposes. 
		 */
		Associations.Bills.link(invoice, workOrder);
	}
	
	@Test
	void testLinkOnInvoice() {
		assertTrue( invoice.getWorkOrders().contains( workOrder ));
		assertEquals( invoice, workOrder.getInvoice());
	}

	@Test
	void testUnlinkOnInvoice() {
		Associations.Bills.unlink(invoice, workOrder);

		assertTrue( invoice.getWorkOrders().isEmpty() );
		assertNull( workOrder.getInvoice());
	}
	
	@Test
	void testSafeReturn() {
		Set<WorkOrder> returned = invoice.getWorkOrders();
		returned.remove( workOrder );

		assertTrue( returned.isEmpty() );
		assertEquals( 1, invoice.getWorkOrders().size(), 
				"It must be a copy of the collection or a read-only version" 
		);
	}
}