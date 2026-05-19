package uo.ri.cws.persistence.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceException;
import uo.ri.cws.domain.Address;
import uo.ri.cws.domain.Associations;
import uo.ri.cws.domain.Client;
import uo.ri.cws.domain.CreditCard;
import uo.ri.cws.persistence.util.UnitOfWork;

class CreditCardMappingTests {

	private Client client;
	private UnitOfWork unitOfWork;
	private EntityManagerFactory factory;
	private CreditCard creditCard;

	@BeforeEach
	void setUp() {
		factory = Persistence.createEntityManagerFactory("carworkshop");
		unitOfWork = UnitOfWork.over( factory );

		client = new Client("nif", "nombre", "apellidos");
		Address address = new Address("street", "city", "zipcode");
		client.setAddress(address);

		creditCard = new CreditCard("1234-5678", "card-type",
				LocalDate.now().plus(1, ChronoUnit.YEARS)
			);

		Associations.Holds.link(creditCard, client);

		unitOfWork.persist(client, creditCard);
	}

	@AfterEach
	void tearDown() {
		unitOfWork.remove( client, creditCard );
		factory.close();
	}

	/**
	 * All fields of credit card are persisted properly
	 */
	@Test
	void testAllFieldsPersisted() {
		CreditCard restored = unitOfWork.findById( CreditCard.class,
				creditCard.getId()
			);

		assertEquals( creditCard.getId(), restored.getId() );
		assertEquals( creditCard.getAccumulated(), restored.getAccumulated(), 0.001 );
		assertEquals( creditCard.getNumber(), restored.getNumber() );
		assertEquals( creditCard.getType(), restored.getType() );
		assertEquals( creditCard.getValidThru(), restored.getValidThru() );
	}

	/**
	 * When two cards with the same number, the second cannot be persisted
	 */
	@Test
	void testRepeated() {
		CreditCard repeated = new CreditCard( 
				creditCard.getNumber(),
				"another-card-type",
				LocalDate.now()
			);

		assertThrows(PersistenceException.class, 
				() -> unitOfWork.persist( repeated ) 
			);
	}

}