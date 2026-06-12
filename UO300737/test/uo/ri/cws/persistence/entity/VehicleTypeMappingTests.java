package uo.ri.cws.persistence.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceException;
import uo.ri.cws.domain.VehicleType;
import uo.ri.cws.persistence.util.UnitOfWork;

class VehicleTypeMappingTests {

	private VehicleType vehicleType;
	private UnitOfWork unitOfWork;
	private EntityManagerFactory factory;

	@BeforeEach
	void setUp() {
		factory = Persistence.createEntityManagerFactory("carworkshop");
		unitOfWork = UnitOfWork.over( factory );

		vehicleType = new VehicleType("vehicleType", 50.0 );
	}

	@AfterEach
	void tearDown() {
		unitOfWork.remove( vehicleType );
		factory.close();
	}

	/**
	 * All fields of client are persisted properly
	 */
	@Test
	void testAllFieldsPersisted() {
		unitOfWork.persist(vehicleType);

		VehicleType restored = unitOfWork.findById( VehicleType.class, vehicleType.getId() );

		assertEquals( vehicleType.getId(), restored.getId() );
		assertEquals( vehicleType.getName(), restored.getName() );
		assertEquals( vehicleType.getPricePerHour(), restored.getPricePerHour(), 0.001 );
	}

	/**
	 * When two vehicle types have the same name, the second cannot be persisted
	 */
	@Test
	void testRepeated() {
		unitOfWork.persist(vehicleType);
		VehicleType repeatedNameVehicleType = new VehicleType( vehicleType.getName() );

		assertThrows(PersistenceException.class, 
				() -> unitOfWork.persist( repeatedNameVehicleType ) 
			);
	}
}