package uo.ri.cws.associations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uo.ri.cws.domain.Associations;
import uo.ri.cws.domain.Mechanic;
import uo.ri.cws.domain.Vehicle;
import uo.ri.cws.domain.WorkOrder;

class AssignTests {
	private Mechanic mechanic;
	private WorkOrder workOrder;
	private Vehicle vehicle;

	@BeforeEach
	void setUp() {
		vehicle = new Vehicle("1234 GJI", "seat", "ibiza");
		workOrder = new WorkOrder(vehicle, "falla la junta la trocla");

		mechanic = new Mechanic("nif-mecanico", "nombre", "apellidos");
		Associations.Assigns.link(mechanic, workOrder);
	}

	@Test
	void testLinkOnAssign() {
		assertTrue(mechanic.getAssigned().contains(workOrder));
		assertSame(mechanic, workOrder.getMechanic());
	}

	@Test
	void testUnlinkOnAssign() {
		Associations.Assigns.unlink(mechanic, workOrder);
		
		assertTrue( mechanic.getAssigned().isEmpty() );
		assertNull(workOrder.getMechanic());
	}

	@Test
	void testSafeReturn() {
		Set<WorkOrder> assigned = mechanic.getAssigned();
		assigned.remove(workOrder);
		
		assertTrue( assigned.isEmpty() );

		assertEquals(1, mechanic.getAssigned().size(), 
				"It must be a copy of the collection"
		);
	}
}