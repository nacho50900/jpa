package uo.ri.cws.persistence.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceException;
import uo.ri.cws.domain.Vehicle;
import uo.ri.cws.domain.WorkOrder;
import uo.ri.cws.persistence.util.UnitOfWork;

class WorkOrderMappingTests {

	private EntityManagerFactory factory;
	private WorkOrder workOrder;
	private UnitOfWork unitOfWork;
	private Vehicle vehicle;

	@BeforeEach
	void setUp() {
		factory = Persistence.createEntityManagerFactory("carworkshop");
		unitOfWork = UnitOfWork.over( factory );

		vehicle = new Vehicle( "plateNumber", "make", "model" );
		LocalDateTime now = LocalDateTime.now();
		workOrder = new WorkOrder(vehicle, now, "description");

		unitOfWork.persist(vehicle, workOrder);
	}

	@AfterEach
	void tearDown() {
		unitOfWork.remove( workOrder, vehicle );
		factory.close();
	}

	/**
	 * All fields of workOrder are persisted properly
	 */
	@Test
	void testAllFieldsPersisted() {

		WorkOrder restored = unitOfWork.findById( WorkOrder.class, workOrder.getId() );

		assertEquals( workOrder.getId(), restored.getId() );
		assertEquals( workOrder.getVehicle(), restored.getVehicle() ); // same id
		assertEquals( workOrder.getDate().truncatedTo(ChronoUnit.MILLIS), 
				restored.getDate().truncatedTo(ChronoUnit.MILLIS));
		assertEquals( workOrder.getDescription(), restored.getDescription() );
		assertEquals( workOrder.getAmount(), restored.getAmount(), 0.0001 );
		assertEquals( workOrder.getState(), restored.getState() );
	}

	/**
	 * When two workOrders are for the same vehicle at the same timestamp,
	 * the second cannot be persisted
	 */
	@Test
	void testRepeated() {
		WorkOrder repeatedWorkOrder = new WorkOrder(
				workOrder.getVehicle(),
				workOrder.getDate(),
				"another description"
			);

		assertThrows(PersistenceException.class, 
				() -> unitOfWork.persist( repeatedWorkOrder ) 
			);
	}
}