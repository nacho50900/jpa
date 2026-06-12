package uo.ri.cws.persistence.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceException;
import uo.ri.cws.domain.Vehicle;
import uo.ri.cws.persistence.util.UnitOfWork;

class VehicleMappingTests {

	private Vehicle vehicle;
	private UnitOfWork unitOfWork;
	private EntityManagerFactory factory;

	@BeforeEach
	void setUp() {
		factory = Persistence.createEntityManagerFactory("carworkshop");
		unitOfWork = UnitOfWork.over( factory );

		vehicle = new Vehicle("plate-1010", "make", "model" );
	}

	@AfterEach
	void tearDown() {
		unitOfWork.remove( vehicle );
		factory.close();
	}

	/**
	 * All fields of vehicle are persisted properly
	 */
	@Test
	void testAllFieldsPersisted() {
		unitOfWork.persist(vehicle);

		Vehicle restored = unitOfWork.findById( Vehicle.class, vehicle.getId() );

		assertEquals( vehicle.getId(), restored.getId() );
		assertEquals( vehicle.getPlateNumber(), restored.getPlateNumber() );
		assertEquals( vehicle.getMake(), restored.getMake() );
		assertEquals( vehicle.getModel(), restored.getModel() );
	}

	/**
	 * When two vehicle types have the same plate, the second cannot be persisted
	 */
	@Test
	void testRepeated() {
		unitOfWork.persist(vehicle);
		Vehicle repeatedPlateVehicle = new Vehicle( vehicle.getPlateNumber() );

		assertThrows(PersistenceException.class, 
				() -> unitOfWork.persist( repeatedPlateVehicle )
			);
	}
}