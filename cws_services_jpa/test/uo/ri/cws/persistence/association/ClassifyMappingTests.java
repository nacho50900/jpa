package uo.ri.cws.persistence.association;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import uo.ri.cws.domain.Associations;
import uo.ri.cws.domain.Vehicle;
import uo.ri.cws.domain.VehicleType;
import uo.ri.cws.persistence.util.UnitOfWork;

class ClassifyMappingTests {

	private UnitOfWork unitOfWork;
	private EntityManagerFactory factory;
	private Vehicle vehicle;
	private VehicleType vehicleType;

	@BeforeEach
	void setUp() {
		factory = Persistence.createEntityManagerFactory("carworkshop");
		unitOfWork = UnitOfWork.over( factory );

		vehicle = new Vehicle("plate-1010", "make", "model" );
		vehicleType = new VehicleType("type", 50);

		Associations.Classifies.link(vehicleType, vehicle);

		unitOfWork.persist(vehicle, vehicleType);
	}

	@AfterEach
	void tearDown() {
		unitOfWork.remove( vehicle, vehicleType );
		factory.close();
	}

	/**
	 * A vehicle type recovers its vehicles
	 */
	@Test
	void testVehicleTypeRecoversVehicles() {

		VehicleType restored = unitOfWork.findById( VehicleType.class, 
				vehicleType.getId() 
			);

		assertFalse( restored.getVehicles().isEmpty() );
		assertEquals( 1, restored.getVehicles().size() );
	}

	/**
	 * A vehicle recovers its vehicle type
	 */
	@Test
	void testVehicleRecoversVehicleType() {

		Vehicle restored = unitOfWork.findById( Vehicle.class, vehicle.getId() );

		assertEquals( vehicleType, restored.getVehicleType() );
	}

}