package uo.ri.cws.persistence.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceException;
import uo.ri.cws.domain.Associations;
import uo.ri.cws.domain.Intervention;
import uo.ri.cws.domain.Mechanic;
import uo.ri.cws.domain.SparePart;
import uo.ri.cws.domain.Substitution;
import uo.ri.cws.domain.Vehicle;
import uo.ri.cws.domain.VehicleType;
import uo.ri.cws.domain.WorkOrder;
import uo.ri.cws.persistence.util.UnitOfWork;

class SubstitutionMappingTests {

	private EntityManagerFactory factory;
	private UnitOfWork unitOfWork;

	private VehicleType vehicleType;
	private Vehicle vehicle;
	private WorkOrder workOrder;
	private Mechanic mechanic;
	private Intervention intervention;
	private SparePart sparePart;
	private Substitution substitution;

	@BeforeEach
	void setUp() {
		factory = Persistence.createEntityManagerFactory("carworkshop");
		unitOfWork = UnitOfWork.over( factory );

		vehicleType = new VehicleType("vehicle-type", 50);
		vehicle = new Vehicle( "plateNumber", "make", "model" );
		Associations.Classifies.link(vehicleType, vehicle);

		LocalDateTime now = LocalDateTime.now();
		workOrder = new WorkOrder(vehicle, now, "description");

		mechanic = new Mechanic("nif", "name", "surname");
		workOrder.assignTo(mechanic);
		intervention = new Intervention(mechanic, workOrder, 60);
		sparePart = new SparePart("spare-code", "spare description", 100.0);

		substitution = new Substitution(sparePart, intervention, 1);

		workOrder.markAsFinished();

		unitOfWork.persist(
				vehicleType, mechanic,
				vehicle, intervention, workOrder,
				sparePart, substitution
			);
	}

	@AfterEach
	void tearDown() {
		unitOfWork.remove(
				vehicleType, mechanic,
				vehicle, intervention, workOrder,
				sparePart, substitution
			);
		factory.close();
	}

	/**
	 * All fields of substitution are persisted properly
	 */
	@Test
	void testAllFieldsPersisted() {

		Substitution restored = unitOfWork.findById( Substitution.class,
				substitution.getId() );

		assertEquals( substitution.getId(), restored.getId() );
		assertEquals( substitution.getQuantity(), restored.getQuantity() );
		assertEquals( substitution.getIntervention(), restored.getIntervention() );
		assertEquals( substitution.getSparePart(), restored.getSparePart() );
	}

	/**
	 * When two substitution are for the same intervention and spare part,
	 * the second cannot be persisted
	 */
	@Test
	void testRepeated() {
		Substitution repeated = new Substitution(
				substitution.getSparePart(),
				substitution.getIntervention(),
				100
			);

		assertThrows(PersistenceException.class, 
				() -> unitOfWork.persist( repeated )
			);
	}
}