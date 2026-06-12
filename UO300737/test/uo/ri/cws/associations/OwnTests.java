package uo.ri.cws.associations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uo.ri.cws.domain.Associations;
import uo.ri.cws.domain.Client;
import uo.ri.cws.domain.Vehicle;

class OwnTests {
	private Vehicle vehicle;
	private Client client;

	@BeforeEach
	void setUp() {
		client = new Client("nif-cliente", "nombre", "apellidos");
		vehicle = new Vehicle("1234 GJI", "seat", "ibiza");
		Associations.Owns.link(client, vehicle);
	}
	
	@Test
	void testLinkOnOwn() {
		assertTrue( client.getVehicles().contains( vehicle ));
		assertEquals( client, vehicle.getClient() );
	}

	@Test
	void testUnlinkOnOwn() {
		Associations.Owns.unlink(client, vehicle);

		assertFalse( client.getVehicles().contains( vehicle ));
		assertNull( vehicle.getClient() );
	}

	@Test
	void testSafeReturn() {
		Set<Vehicle> vehicles = client.getVehicles();
		vehicles.remove( vehicle );

		assertTrue( vehicles.isEmpty() );

		assertEquals( 1, client.getVehicles().size(), 
			"It must be a copy of the collection or a read-only version"
		);
	}
}