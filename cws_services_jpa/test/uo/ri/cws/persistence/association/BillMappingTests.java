package uo.ri.cws.persistence.association;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import uo.ri.cws.domain.Invoice;
import uo.ri.cws.domain.Mechanic;
import uo.ri.cws.domain.Vehicle;
import uo.ri.cws.domain.WorkOrder;
import uo.ri.cws.persistence.util.UnitOfWork;

class BillMappingTests {

	private UnitOfWork unitOfWork;
	private EntityManagerFactory factory;
	private WorkOrder workOrder;
	private Vehicle vehicle;
	private Invoice invoice;
	private Mechanic mechanic;

	@BeforeEach
	void setUp() {
		factory = Persistence.createEntityManagerFactory("carworkshop");
		unitOfWork = UnitOfWork.over( factory );

		mechanic = new Mechanic("mechanic-nif");
		vehicle = new Vehicle("plate-1010", "make", "model" );
		workOrder = new WorkOrder(vehicle);
		workOrder.assignTo(mechanic);
		workOrder.markAsFinished();

		invoice = new Invoice( 1L );
		invoice.addWorkOrder(workOrder);

		unitOfWork.persist(workOrder, vehicle, invoice, mechanic);
	}

	@AfterEach
	void tearDown() {
		unitOfWork.remove( workOrder, vehicle, invoice, mechanic );
		factory.close();
	}

	/**
	 * An invoice recovers its work orders
	 */
	@Test
	void testInvoiceRecoversWorkOrders() {

		Invoice restored = unitOfWork.findById( Invoice.class, invoice.getId() );

		assertFalse( restored.getWorkOrders().isEmpty() );
		assertEquals( 1, restored.getWorkOrders().size() );
	}

	/**
	 * A workOrder recovers its invoice
	 */
	@Test
	void testWorkOrderRecoversVehicle() {

		WorkOrder restored = unitOfWork.findById( WorkOrder.class, workOrder.getId() );

		assertEquals( invoice, restored.getInvoice() );
	}

}