package uo.ri.cws.associations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uo.ri.cws.domain.Associations;
import uo.ri.cws.domain.Cash;
import uo.ri.cws.domain.Charge;
import uo.ri.cws.domain.Client;
import uo.ri.cws.domain.Intervention;
import uo.ri.cws.domain.Invoice;
import uo.ri.cws.domain.Mechanic;
import uo.ri.cws.domain.SparePart;
import uo.ri.cws.domain.Substitution;
import uo.ri.cws.domain.Vehicle;
import uo.ri.cws.domain.VehicleType;
import uo.ri.cws.domain.WorkOrder;

class SettleTests {
	private Mechanic mechanic;
	private WorkOrder workOrder;
	private Intervention intervention;
	private SparePart sparePart;
	private Vehicle vehicle;
	private VehicleType vehicleType;
	private Client client;
	private Invoice invoice;
	private Cash cash;
	private Charge charge;

	@BeforeEach
	void setUp() {
		client = new Client("nif-cliente", "nombre", "apellidos");
		vehicle = new Vehicle("1234 GJI", "seat", "ibiza");
		Associations.Owns.link(client, vehicle );

		vehicleType = new VehicleType("coche", 50.0);
		Associations.Classifies.link(vehicleType, vehicle);
		
		workOrder = new WorkOrder(vehicle, "falla la junta la trocla");
		mechanic = new Mechanic("nif-mecanico", "nombre", "apellidos");
		workOrder.assignTo(mechanic);
	
		intervention = new Intervention(mechanic, workOrder, 60);
		
		sparePart = new SparePart("R1001", "junta la trocla", 100.0);
		new Substitution(sparePart, intervention, 2);
		
		workOrder.markAsFinished();

		invoice = new Invoice(0L, LocalDate.now() );
		invoice.addWorkOrder(workOrder);
		
		cash = new Cash( client );
		
		/*
		 * Charge's constructor links the 3 objects by calling
		 * "Associations.Settle.link(invoice, this, cash)"
		 */
		charge = new Charge(invoice, cash, 100.0);
	}
	
	@Test
	void testInvoiceChargeLinked() {
		assertTrue( cash.getCharges().contains( charge ));
		assertTrue( invoice.getCharges().contains( charge ));
		
		assertEquals( invoice, charge.getInvoice() );
		assertEquals( cash, charge.getPaymentMean() );
	}

	@Test
	void testInvoiceCashUnlinked() {
		Associations.Settles.unlink( charge );
		
		assertFalse( cash.getCharges().contains( charge ));
		assertTrue( cash.getCharges().isEmpty() );

		assertFalse( invoice.getCharges().contains( charge ));
		assertTrue( cash.getCharges().isEmpty() );
		
		assertNull( charge.getInvoice() );
		assertNull( charge.getPaymentMean() );
	}
	
	@Test
	void testSafeRetunOnPaymentMean() {
		Set<Charge> charges = cash.getCharges();
		charges.remove( charge );
		
		assertTrue( charges.isEmpty() );
		assertTrue( cash.getCharges().contains( charge ), 
				"It must return a copy of the collection" 
			);
	}

	@Test
	void testSafeRetunOnInvoice() {
		Set<Charge> charges = invoice.getCharges();
		charges.remove( charge );
		
		assertTrue( charges.isEmpty() );
		assertTrue( invoice.getCharges().contains( charge ), 
				"It must return a copy of the collection" 
			);
	}

}