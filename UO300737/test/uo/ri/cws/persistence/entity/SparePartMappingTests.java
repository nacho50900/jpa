package uo.ri.cws.persistence.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceException;
import uo.ri.cws.domain.SparePart;
import uo.ri.cws.persistence.util.UnitOfWork;

class SparePartMappingTests {

	private SparePart sparePart;
	private UnitOfWork unitOfWork;
	private EntityManagerFactory factory;

	@BeforeEach
	void setUp() {
		factory = Persistence.createEntityManagerFactory("carworkshop");
		unitOfWork = UnitOfWork.over( factory );

		sparePart = new SparePart("code", "description", 100.0);
	}

	@AfterEach
	void tearDown() {
		unitOfWork.remove( sparePart );
		factory.close();
	}

	/**
	 * All fields of sparePart are persisted properly
	 */
	@Test
	void testAllFieldsPersisted() {
		unitOfWork.persist(sparePart);

		SparePart restored = unitOfWork.findById( SparePart.class, sparePart.getId() );

		assertEquals( sparePart.getId(), restored.getId() );
		assertEquals( sparePart.getCode(), restored.getCode() );
		assertEquals( sparePart.getDescription(), restored.getDescription() );
		assertEquals( sparePart.getPrice(), restored.getPrice(), 0.0001 );
	}

	/**
	 * There cannot be persisted two spareParts with the same code
	 */
	@Test
	void testRepeated() {
		unitOfWork.persist(sparePart);
		SparePart repeatedCodeSparePart = new SparePart( sparePart.getCode() );

		assertThrows( PersistenceException.class, () -> {
			unitOfWork.persist( repeatedCodeSparePart );
		});
	}

}