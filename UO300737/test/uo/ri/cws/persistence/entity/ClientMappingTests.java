package uo.ri.cws.persistence.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceException;
import uo.ri.cws.domain.Address;
import uo.ri.cws.domain.Client;
import uo.ri.cws.persistence.util.UnitOfWork;

class ClientMappingTests {

	private Client client;
	private UnitOfWork unitOfWork;
	private EntityManagerFactory factory;

	@BeforeEach
	void setUp() {
		factory = Persistence.createEntityManagerFactory("carworkshop");
		unitOfWork = UnitOfWork.over( factory );

		client = new Client("nif", "nombre", "apellidos");
		Address address = new Address("street", "city", "zipcode");
		client.setAddress(address);
	}

	@AfterEach
	void tearDown() {
		unitOfWork.remove( client );
		factory.close();
	}

	/**
	 * All fields of client are persisted properly
	 */
	@Test
	void testAllFieldsPersisted() {
		unitOfWork.persist(client);

		Client restored = unitOfWork.findById( Client.class, client.getId() );

		assertEquals( client.getId(), restored.getId() );
		assertEquals( client.getNif(), restored.getNif() );
		assertEquals( client.getName(), restored.getName() );
		assertEquals( client.getSurname(), restored.getSurname() );
		assertEquals( client.getEmail(), restored.getEmail() );
		assertEquals( client.getPhone(), restored.getPhone() );
		assertEquals( client.getAddress(), restored.getAddress() );
	}

	/**
	 * When two clients with the same NIF, the second cannot be persisted
	 */
	@Test
	void testRepeated() {
		unitOfWork.persist(client);
		Client repeatedNifClient = new Client( client.getNif() );

		assertThrows( PersistenceException.class, () -> {
			unitOfWork.persist( repeatedNifClient );
		});
	}
}