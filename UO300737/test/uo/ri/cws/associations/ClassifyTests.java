package uo.ri.cws.associations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uo.ri.cws.domain.Associations;
import uo.ri.cws.domain.Vehicle;
import uo.ri.cws.domain.VehicleType;

class ClassifyTests {
	private Vehicle vehicle;
	private VehicleType vehicleType;

	@BeforeEach
	void setUp() {
		vehicle = new Vehicle("1234 GJI", "seat", "ibiza");
		vehicleType = new VehicleType("coche", 50.0);
		Associations.Classifies.link(vehicleType, vehicle);
	}

	@Test
	void testLinkOnClassify() {
		assertTrue( vehicleType.getVehicles().contains( vehicle ));
		assertEquals( vehicleType, vehicle.getVehicleType() );
	}

	@Test
	void testUnlinkOnClassify() {
		Associations.Classifies.unlink(vehicleType, vehicle);

		assertFalse( vehicleType.getVehicles().contains( vehicle ));
		assertNull( vehicle.getVehicleType() );
	}

	@Test
	void testSafeReturn() {
		Set<Vehicle> vehiculos = vehicleType.getVehicles();
		vehiculos.remove( vehicle );

		assertTrue( vehiculos.isEmpty() );
		assertEquals( 1, vehicleType.getVehicles().size(), "It must be a copy of the collection" );
	}
}