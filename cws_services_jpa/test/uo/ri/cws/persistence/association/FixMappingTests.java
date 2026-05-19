package uo.ri.cws.persistence.association;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import uo.ri.cws.domain.Vehicle;
import uo.ri.cws.domain.WorkOrder;
import uo.ri.cws.persistence.util.UnitOfWork;

class FixMappingTests {

	private UnitOfWork unitOfWork;
	private EntityManagerFactory factory;
	private WorkOrder workOrder;
	private Vehicle vehicle;

	@BeforeEach
	void setUp() {
		factory = Persistence.createEntityManagerFactory("carworkshop");
		unitOfWork = UnitOfWork.over( factory );

		vehicle = new Vehicle("plate-1010", "make", "model" );
		workOrder = new WorkOrder(vehicle);

		unitOfWork.persist(workOrder, vehicle);
	}

	@AfterEach
	void tearDown() {
		unitOfWork.remove( workOrder, vehicle );
		factory.close();
	}

	/**
	 * A vehicle recovers its work orders
	 */
	@Test
	void testVehicleRecoversWorkOrders() {

		Vehicle restored = unitOfWork.findById( Vehicle.class, vehicle.getId() );

		assertFalse( restored.getWorkOrders().isEmpty() );
		assertEquals( 1, restored.getWorkOrders().size() );
	}

	/**
	 * A workOrder recovers its vehicle
	 */
	@Test
	void testWorkOrderRecoversVehicle() {

		WorkOrder restored = unitOfWork.findById( WorkOrder.class, workOrder.getId() );

		assertEquals( vehicle, restored.getVehicle() );
	}

}