package uo.ri.cws.persistence.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceException;
import uo.ri.cws.domain.Mechanic;
import uo.ri.cws.persistence.util.UnitOfWork;

class MechanicMappingTests {

	private Mechanic mechanic;
	private UnitOfWork unitOfWork;
	private EntityManagerFactory factory;

	@BeforeEach
	void setUp() {
		factory = Persistence.createEntityManagerFactory("carworkshop");
		unitOfWork = UnitOfWork.over( factory );

		mechanic = new Mechanic("nif", "nombre", "apellidos");
	}

	@AfterEach
	void tearDown() {
		unitOfWork.remove( mechanic );
		factory.close();
	}

	/**
	 * All fields of mechanic are persisted properly
	 */
	@Test
	void testAllFieldsPersisted() {
		unitOfWork.persist(mechanic);

		Mechanic restored = unitOfWork.findById(Mechanic.class, mechanic.getId() );

		assertEquals( mechanic.getId(), restored.getId() );
		assertEquals( mechanic.getNif(), restored.getNif() );
		assertEquals( mechanic.getName(), restored.getName() );
		assertEquals( mechanic.getSurname(), restored.getSurname() );
	}

	/**
	 * When two mechanics with the same NIF, the second cannot be persisted
	 */
	@Test
	void testRepeated() {
		unitOfWork.persist(mechanic);
		Mechanic repeatedNifMechanic = new Mechanic( mechanic.getNif() );

		assertThrows(PersistenceException.class, 
				() -> unitOfWork.persist( repeatedNifMechanic ) 
			);
	}

}