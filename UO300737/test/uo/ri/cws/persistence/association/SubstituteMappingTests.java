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
import uo.ri.cws.domain.SparePart;
import uo.ri.cws.domain.Substitution;
import uo.ri.cws.domain.Vehicle;
import uo.ri.cws.domain.VehicleType;
import uo.ri.cws.domain.WorkOrder;
import uo.ri.cws.persistence.util.UnitOfWork;

class SubstituteMappingTests {

	private UnitOfWork unitOfWork;
	private EntityManagerFactory factory;
	private WorkOrder workOrder;
	private Vehicle vehicle;
	private Invoice invoice;
	private Mechanic mechanic;
	private Intervention intervention;
	private VehicleType vehicleType;
	private SparePart sparePart;
	private Substitution substitution;

	@BeforeEach
	void setUp() {
		factory = Persistence.createEntityManagerFactory("carworkshop");
		unitOfWork = UnitOfWork.over( factory );

		mechanic = new Mechanic("mechanic-nif");
		vehicleType = new VehicleType("vehicle-type", 50);
		vehicle = new Vehicle("plate-1010", "make", "model" );
		Associations.Classifies.link(vehicleType, vehicle);

		sparePart = new SparePart("code", "description", 100);

		workOrder = new WorkOrder(vehicle);
		workOrder.assignTo(mechanic);
		intervention = new Intervention(mechanic, workOrder, 60);
		substitution = new Substitution(sparePart, intervention, 1);
		workOrder.markAsFinished();

		invoice = new Invoice( 1L );
		invoice.addWorkOrder(workOrder);

		unitOfWork.persist(workOrder, vehicle, invoice,
				mechanic, intervention, vehicleType,
				substitution, sparePart);
	}

	@AfterEach
	void tearDown() {
		unitOfWork.remove(
				workOrder, vehicle, invoice,
				mechanic, intervention, vehicleType,
				substitution, sparePart
			);
		factory.close();
	}

	/**
	 * A spare part recovers its substitutions
	 */
	@Test
	void testSparePartRecoversSubstitutions() {
		SparePart restored = unitOfWork.findById( SparePart.class, sparePart.getId() );

		assertFalse( restored.getSubstitutions().isEmpty() );
		assertEquals( 1, restored.getSubstitutions().size() );
	}

	/**
	 * A intervention recovers its substitutions
	 */
	@Test
	void testInterventionRecoversSubstitutions() {
		Intervention restored = unitOfWork.findById( Intervention.class,
				intervention.getId()
			);

		assertFalse( restored.getSubstitutions().isEmpty() );
		assertEquals( 1, restored.getSubstitutions().size() );
	}

	/**
	 * An substitution recovers its intervention
	 */
	@Test
	void testSubstitutionRecovrsItsInterventions() {
		Substitution restored = unitOfWork.findById( Substitution.class,
				substitution.getId() );

		assertEquals( intervention, restored.getIntervention() );
	}

	/**
	 * An substitution recovers its spare part
	 */
	@Test
	void testSubstitutionRecoversSparePart() {
		Substitution restored = unitOfWork.findById( Substitution.class,
				substitution.getId() );

		assertEquals( sparePart, restored.getSparePart() );
	}

}