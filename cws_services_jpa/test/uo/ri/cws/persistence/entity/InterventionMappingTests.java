package uo.ri.cws.persistence.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceException;
import uo.ri.cws.domain.Intervention;
import uo.ri.cws.domain.Mechanic;
import uo.ri.cws.domain.Vehicle;
import uo.ri.cws.domain.WorkOrder;
import uo.ri.cws.persistence.util.UnitOfWork;

class InterventionMappingTests {

	private EntityManagerFactory factory;
	private UnitOfWork unitOfWork;

	private Vehicle vehicle;
	private WorkOrder workOrder;
	private Mechanic mechanic;
	private Intervention intervention;

	@BeforeEach
	void setUp() {
		factory = Persistence.createEntityManagerFactory("carworkshop");
		unitOfWork = UnitOfWork.over( factory );

		mechanic = new Mechanic("nif", "name", "surname");
		vehicle = new Vehicle( "plateNumber", "make", "model" );
		LocalDateTime now = LocalDateTime.now();
		workOrder = new WorkOrder(vehicle, now, "description");
		workOrder.assignTo(mechanic);
		workOrder.markAsFinished();

		intervention = new Intervention(mechanic, workOrder, 60);

		unitOfWork.persist(vehicle, intervention, workOrder, mechanic);
	}

	@AfterEach
	void tearDown() {
		unitOfWork.remove( vehicle, workOrder, mechanic, intervention );
		factory.close();
	}

	/**
	 * All fields of intervention are persisted properly
	 */
	@Test
	void testAllFieldsPersisted() {
		Intervention restored = unitOfWork.findById( Intervention.class,
				intervention.getId() );

		assertEquals( intervention.getId(), restored.getId() );
		assertEquals( intervention.getMechanic(), restored.getMechanic() );
		assertEquals( intervention.getWorkOrder(), restored.getWorkOrder() );
		assertEquals( 
				intervention.getDate().truncatedTo(ChronoUnit.MILLIS),
				restored.getDate().truncatedTo(ChronoUnit.MILLIS) 
			);
		assertEquals( intervention.getMinutes(), restored.getMinutes() );
		assertEquals( intervention.getSubstitutions(), restored.getSubstitutions() );
	}

	/**
	 * When two interventions have the same mechanic, workOrder and date, then
	 * the second cannot be added
	 */
	@Test
	void testRepeated() {
		Intervention repeatedIntervention = new Intervention(
				intervention.getMechanic(),
				intervention.getWorkOrder(),
				intervention.getDate(),
				100
			);

		assertThrows(PersistenceException.class, 
				() -> unitOfWork.persist( repeatedIntervention )
			);
	}
}