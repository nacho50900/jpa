package uo.ri.cws.persistence.association;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import uo.ri.cws.domain.Address;
import uo.ri.cws.domain.Associations;
import uo.ri.cws.domain.Client;
import uo.ri.cws.domain.Vehicle;
import uo.ri.cws.persistence.util.UnitOfWork;

class OwnMappingTests {

	private UnitOfWork unitOfWork;
	private EntityManagerFactory factory;
	private Vehicle vehicle;
	private Client client;

	@BeforeEach
	void setUp() {
		factory = Persistence.createEntityManagerFactory("carworkshop");
		unitOfWork = UnitOfWork.over( factory );

		vehicle = new Vehicle("plate-1010", "make", "model" );
		client = new Client("nif", "nombre", "apellidos");
		Address address = new Address("street", "city", "zipcode");
		client.setAddress(address);

		Associations.Owns.link(client, vehicle);

		unitOfWork.persist(vehicle, client);
	}

	@AfterEach
	void tearDown() {
		unitOfWork.remove( vehicle, client );
		factory.close();
	}

	/**
	 * A client recovers its vehicles
	 */
	@Test
	void testClientRecoversVehicles() {

		Client restored = unitOfWork.findById( Client.class, client.getId() );

		assertFalse( restored.getVehicles().isEmpty() );
		assertEquals( 1, restored.getVehicles().size() );
	}

	/**
	 * A vehicle recovers its client
	 */
	@Test
	void testVehicleRecoversClient() {

		Vehicle restored = unitOfWork.findById( Vehicle.class, vehicle.getId() );

		assertEquals( client, restored.getClient() );
	}

}