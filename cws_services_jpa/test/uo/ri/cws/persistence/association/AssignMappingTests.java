package uo.ri.cws.persistence.association;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import uo.ri.cws.domain.Associations;
import uo.ri.cws.domain.Mechanic;
import uo.ri.cws.domain.Vehicle;
import uo.ri.cws.domain.WorkOrder;
import uo.ri.cws.persistence.util.UnitOfWork;

class AssignMappingTests {

	private UnitOfWork unitOfWork;
	private EntityManagerFactory factory;
	private WorkOrder workOrder;
	private Mechanic mechanic;
	private Vehicle vehicle;

	@BeforeEach
	void setUp() {
		factory = Persistence.createEntityManagerFactory("carworkshop");
		unitOfWork = UnitOfWork.over( factory );

		mechanic = new Mechanic("plate-1010", "make", "model" );
		vehicle = new Vehicle("1234-Z", "make", "model");
		workOrder = new WorkOrder( vehicle );

		Associations.Assigns.link(mechanic, workOrder);

		unitOfWork.persist(workOrder, mechanic, vehicle);
	}

	@AfterEach
	void tearDown() {
		unitOfWork.remove( workOrder, mechanic, vehicle );
		factory.close();
	}

	/**
	 * A mechanic recovers its work orders
	 */
	@Test
	void testMechanicRecoversWorkOrders() {

		Mechanic restored = unitOfWork.findById( Mechanic.class, mechanic.getId() );

		assertFalse( restored.getAssigned().isEmpty() );
		assertEquals( 1, restored.getAssigned().size() );
	}

	/**
	 * A workOrder recovers its mechanic
	 */
	@Test
	void testWorkOrderRecoversMechanic() {

		WorkOrder restored = unitOfWork.findById( WorkOrder.class, workOrder.getId() );

		assertEquals( mechanic, restored.getMechanic() );
	}

}