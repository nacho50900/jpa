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
import uo.ri.cws.domain.Associations;
import uo.ri.cws.domain.Client;
import uo.ri.cws.domain.Voucher;
import uo.ri.cws.persistence.util.UnitOfWork;

class VoucherMappingTests {

	private Client client;
	private UnitOfWork unitOfWork;
	private EntityManagerFactory factory;
	private Voucher voucher;

	@BeforeEach
	void setUp() {
		factory = Persistence.createEntityManagerFactory("carworkshop");
		unitOfWork = UnitOfWork.over( factory );

		client = new Client("nif", "nombre", "apellidos");
		Address address = new Address("street", "city", "zipcode");
		client.setAddress(address);

		voucher = new Voucher("voucher-code", "voucher-description", 100);

		Associations.Holds.link(voucher, client);

		unitOfWork.persist(client, voucher);
	}

	@AfterEach
	void tearDown() {
		unitOfWork.remove( client, voucher );
		factory.close();
	}

	/**
	 * All fields of credit card are persisted properly
	 */
	@Test
	void testAllFieldsPersisted() {
		Voucher restored = unitOfWork.findById( Voucher.class, voucher.getId());

		assertEquals( voucher.getId(), restored.getId() );
		assertEquals( voucher.getCode(), restored.getCode() );
		assertEquals( voucher.getDescription(), restored.getDescription() );
		assertEquals( voucher.getAccumulated(), restored.getAccumulated(), 0.001 );
		assertEquals( voucher.getAvailable(), restored.getAvailable(), 0.001 );
	}

	/**
	 * When two vouchers with the same code, the second cannot be persisted
	 */
	@Test
	void testRepeated() {
		Voucher repeated = new Voucher( voucher.getCode(),"another-voucher", 50);

		assertThrows(PersistenceException.class, 
				() -> unitOfWork.persist( repeated )
			);
	}
}