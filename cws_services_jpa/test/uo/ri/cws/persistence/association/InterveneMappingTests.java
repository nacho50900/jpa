package uo.ri.cws.persistence.association;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import uo.ri.cws.domain.Associations;
import uo.ri.cws.domain.Intervention;
import uo.ri.cws.domain.Invoice;
import uo.ri.cws.domain.Mechanic;
import uo.ri.cws.domain.Vehicle;
import uo.ri.cws.domain.VehicleType;
import uo.ri.cws.domain.WorkOrder;
import uo.ri.cws.persistence.util.UnitOfWork;

class InterveneMappingTests {

	private UnitOfWork unitOfWork;
	private EntityManagerFactory factory;
	private WorkOrder workOrder;
	private Vehicle vehicle;
	private Invoice invoice;
	private Mechanic mechanic;
	private Intervention intervention;
	private VehicleType vehicleType;

	@BeforeEach
	void setUp() {
		factory = Persistence.createEntityManagerFactory("carworkshop");
		unitOfWork = UnitOfWork.over( factory );

		mechanic = new Mechanic("mechanic-nif");
		vehicleType = new VehicleType("vehicle-type", 50);
		vehicle = new Vehicle("plate-1010", "make", "model" );
		Associations.Classifies.link(vehicleType, vehicle);

		workOrder = new WorkOrder(vehicle);
		workOrder.assignTo(mechanic);
		intervention = new Intervention(mechanic, workOrder, 60);
		workOrder.markAsFinished();

		invoice = new Invoice( 1L );
		invoice.addWorkOrder(workOrder);

		unitOfWork.persist(workOrder, vehicle, invoice,
				mechanic, intervention, vehicleType);
	}

	@AfterEach
	void tearDown() {
		unitOfWork.remove(
				workOrder, vehicle, invoice,
				mechanic, intervention, vehicleType
			);
		factory.close();
	}

	/**
	 * A work order recovers its interventions
	 */
	@Test
	void testWorkOrderRecoversInterventions() {

		WorkOrder restored = unitOfWork.findById( WorkOrder.class, workOrder.getId() );

		assertFalse( restored.getInterventions().isEmpty() );
		assertEquals( 1, restored.getInterventions().size() );
	}

	/**
	 * A mechanic recovers its interventions
	 */
	@Test
	void testMechanicRecoversInterventions() {

		Mechanic restored = unitOfWork.findById( Mechanic.class, mechanic.getId() );

		assertFalse( restored.getInterventions().isEmpty() );
		assertEquals( 1, restored.getInterventions().size() );
	}

	/**
	 * An intervention recovers its work order
	 */
	@Test
	void testInterventionRecoversWorkOrder() {

		Intervention restored = unitOfWork.findById( Intervention.class,
				intervention.getId() );

		assertEquals( workOrder, restored.getWorkOrder() );
	}

	/**
	 * An intervention recovers its mechanic
	 */
	@Test
	void testInterventionRecoversMechanic() {

		Intervention restored = unitOfWork.findById( Intervention.class,
				intervention.getId() );

		assertEquals( mechanic, restored.getMechanic() );
	}

}