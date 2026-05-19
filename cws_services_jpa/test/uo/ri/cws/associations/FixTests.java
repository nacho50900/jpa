package uo.ri.cws.associations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uo.ri.cws.domain.Associations;
import uo.ri.cws.domain.Vehicle;
import uo.ri.cws.domain.WorkOrder;

class FixTests {
	private WorkOrder workOrder;
	private Vehicle vehicle;

	@BeforeEach
	void setUp() {
		vehicle = new Vehicle("1234 GJI", "seat", "ibiza");
		workOrder = new WorkOrder(vehicle, "falla la junta la trocla");
	}
	
	@Test
	void testLinkOnOrder() {
		// The constructor of "WorkOrder" creates the link with "vehicle"
		// It calls Associations.Fix.link(...)
		assertTrue( vehicle.getWorkOrders().contains( workOrder ));
		assertEquals( vehicle, workOrder.getVehicle() );
	}

	@Test
	void testUnlinkOnOrder() {
		Associations.Fixes.unlink(vehicle, workOrder);
		
		assertFalse( vehicle.getWorkOrders().contains( workOrder ));
		assertNull( workOrder.getVehicle() );
	}

	@Test
	void testUnlinkTwiceOnOrder() {
		Associations.Fixes.unlink(vehicle, workOrder);
		Associations.Fixes.unlink(vehicle, workOrder);
		
		assertFalse( vehicle.getWorkOrders().contains( workOrder ));
		assertNull( workOrder.getVehicle() );
	}

	@Test
	void testSafeReturn() {
		Set<WorkOrder> workOrders = vehicle.getWorkOrders();
		workOrders.remove( workOrder );

		assertTrue( workOrders.isEmpty() );
		assertEquals( 1, vehicle.getWorkOrders().size() );
	}
}