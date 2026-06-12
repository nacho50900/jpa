package uo.ri.cws.associations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uo.ri.cws.domain.Associations;
import uo.ri.cws.domain.Intervention;
import uo.ri.cws.domain.Mechanic;
import uo.ri.cws.domain.Vehicle;
import uo.ri.cws.domain.WorkOrder;

class InterveneTests {
	private Mechanic mechanic;
	private WorkOrder workOrder;
	private Intervention intervention;
	private Vehicle vehicle;

	@BeforeEach
	void setUp() {
		vehicle = new Vehicle("1234 GJI", "seat", "ibiza");
		workOrder = new WorkOrder(vehicle, "falla la junta la trocla");
		mechanic = new Mechanic("nif-mecanico", "nombre", "apellidos");
		
		/*
		 * Intervention's constructor links this intervention with the mechanic 
		 * and the work order by calling 
		 * Associations.Intervene.link(workOrder, this, mechanic)
		 */
		intervention = new Intervention(mechanic, workOrder, 60);
	}
	
	@Test
	void testWorkOrderInterventionLinked() {
		assertTrue( workOrder.getInterventions().contains( intervention ));
		assertTrue( intervention.getWorkOrder() == workOrder );
	}

	@Test
	void testUnlinkOnInterventionByWorkOrder() {
		Associations.Intervenes.unlink(intervention);
		
		assertFalse( workOrder.getInterventions().contains( intervention ));
		assertTrue( workOrder.getInterventions().isEmpty() );
		assertNull( intervention.getWorkOrder() );
	}

	@Test
	void testMechanicInterventionLinked() {
		assertTrue( mechanic.getInterventions().contains( intervention ));
		assertTrue( intervention.getMechanic() == mechanic );
	}

	@Test
	void testUnlinkOnInterventionByMechanic() {
		Associations.Intervenes.unlink(intervention);
		
		assertFalse( mechanic.getInterventions().contains( intervention ));
		assertTrue( mechanic.getInterventions().isEmpty() );
		assertNull( intervention.getMechanic() );
	}

	@Test
	void testSafeReturnOnMechanic() {
		Set<Intervention> intervenciones = mechanic.getInterventions();
		intervenciones.remove( intervention );

		assertTrue( intervenciones.isEmpty() );
		assertEquals(1, mechanic.getInterventions().size(),
				"It must be a copy of the collection or a read-only version"
			); 
	}

	@Test
	void testSafeReturnOnWorkOrders() {
		Set<Intervention> intervenciones = workOrder.getInterventions();
		intervenciones.remove( intervention );

		assertTrue( intervenciones.isEmpty() );
		assertEquals(1, workOrder.getInterventions().size(),
				"It must be a copy of the collection or a read-only version"
			); 
	}
}