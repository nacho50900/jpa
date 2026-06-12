package uo.ri.cws.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * To better understand this tests, please review the WorkOrder state diagram
 * see the "project scope statement" document
 * 
 * Scenarios:
 * - An open work order with no interventions has no amount computed
 * - An assigned work order with no interventions has no amount computed
 * - An assigned work order with interventions has no amount computed
 * - A finished work order has the amount computed from interventions and substitutions
 * - An invoiced work order has the amount computed from interventions and substitutions
 * - Adding a new intervention and finishing the work order recomputes the amount
 * - Removing an intervention and finishing the work order recomputes the amount
 */
class WorkOrderGetAmountTests {

	private static final double EUR_50_PER_HOUR = 50.0;
	private static final int NUM_OF_SPARES = 2;
	private static final double EUR_100 = 100.0;
	private static final int SIXTY_MINS = 60;
	private Mechanic mechanic;
	private Vehicle vehicle;
	private VehicleType vehicleType;

	@BeforeEach
	void setUp() {
		vehicle = new Vehicle("1234 GJI", "ibiza", "seat");
		vehicleType = new VehicleType("coche", EUR_50_PER_HOUR);
		Associations.Classifies.link(vehicleType, vehicle);

		mechanic = new Mechanic("nif-mecanico", "nombre", "apellidos");
	}

	/**
	 * GIVEN: a work order in state OPEN with no interventions
	 * WHEN: we ask for the amount
	 * THEN: the amount is zero
	 */
	@Test
	void testOpenWorkOrderHasNoAmount() {
		WorkOrder wo = new WorkOrder(vehicle);
		
		assertEquals(0.0, wo.getAmount(), 0.001);
	}
	
	/**
	 * GIVEN: a work order in state ASSIGNED with no interventions
	 * WHEN: we ask for the amount
	 * THEN: the amount is zero
	 */
	@Test
	void testAssignedWorkOrderHasNoAmount() {
		WorkOrder wo = new WorkOrder(vehicle);
		wo.assignTo(mechanic);

		assertEquals(0.0, wo.getAmount(), 0.001);
	}
	
	/**
	 * GIVEN: a work order in state ASSIGNED with interventions 
	 * WHEN: we ask for the amount 
	 * THEN: the amount is zero
	 */
	@Test
	void testAssignedWorkOrderWithInterventionHasNoAmount() {
		WorkOrder wo = createAssignedWithIntervention();

		assertEquals(0.0, wo.getAmount(), 0.001);
	}

	/**
	 * GIVEN: a work order in state FINISHED with one intervention 
	 * WHEN: we ask for the amount 
	 * THEN: the amount is correctly computed
	 */
	@Test
	void testFinishedWorkOrderWithOneInterventionHasAmount() {
		WorkOrder wo = createAssignedWithIntervention();
		wo.markAsFinished(); // changes state & computes price

		double expected = 
				EUR_50_PER_HOUR * (SIXTY_MINS / 60.0 /*hours*/)  
				+ NUM_OF_SPARES * EUR_100;
		
		assertEquals(expected, wo.getAmount(), 0.001);
	}
	
	/**
	 * GIVEN: a work order in state INVOICED with one intervention 
	 * WHEN: we ask for the amount 
	 * THEN: the amount is correctly computed
	 */
	 @Test
	 void testInvoicedWorkOrderWithOneInterventionHasAmount() {
			WorkOrder wo = createFinishedWorkOrder();
			new Invoice(100L, List.of(wo)); // changes state to INVOICED

			double expected = 
					EUR_50_PER_HOUR * (SIXTY_MINS / 60.0 /* hours */)
					+ NUM_OF_SPARES * EUR_100;

			assertEquals(expected, wo.getAmount(), 0.001);
	 }
	
	/**
	 * GIVEN: a work order in state FINISHED with one intervention 
	 * WHEN: we add a new intervention and finish the work order again 
	 * THEN: the amount is correctly recomputed
	 */
	@Test
	void testComputeAmountForTwoInterventions() {
		WorkOrder wo = createFinishedWorkOrder();
		
		wo.reopen();  // changes from FINISHED to OPEN again
		Mechanic another = new Mechanic("1", "a", "n");
		wo.assignTo(another);
		addIntervention(another, wo);

		wo.markAsFinished();  // changes state & recomputes price

		double expected =
				2 * (
					EUR_50_PER_HOUR * (SIXTY_MINS / 60.0 /* hours */)
					+ NUM_OF_SPARES * EUR_100
				);

		assertEquals(expected, wo.getAmount(), 0.001);
	}
	
	/**
	 * GIVEN: a work order in state FINISHED with two intervention
	 * WHEN: we remove one intervention and finish the work order again
	 * THEN: the amount is correctly recomputed
	 */
	@Test
	void testRcomputeAmountWhenReopeningAndFinishing() {
		WorkOrder wo = createAssignedWithIntervention();
		addIntervention(mechanic, wo); // now has two interventions
		wo.markAsFinished();

		wo.reopen(); // changes from FINISHED to OPEN again
		wo.assignTo(mechanic);
		Intervention one = wo.getInterventions().iterator().next();
		Associations.Intervenes.unlink(one);

		wo.markAsFinished(); // changes state & recomputes price

		double expected = 
				EUR_50_PER_HOUR * (SIXTY_MINS / 60.0 /* hours */)
				+ NUM_OF_SPARES * EUR_100;

		assertEquals(expected, wo.getAmount(), 0.001);
	}
	
	private WorkOrder createFinishedWorkOrder() {
		WorkOrder wo = createAssignedWithIntervention();
		wo.markAsFinished();
		return wo;
	}

	private WorkOrder createAssignedWithIntervention() {
		WorkOrder wo = new WorkOrder(vehicle);
		wo.assignTo(mechanic);
		addIntervention(mechanic, wo);
		return wo;
	}

	/**
	 * Fixture: creates one intervention with one substitution with amount 250 EUR
	 * @param mechanic
	 * @param workOrder
	 */
	private void addIntervention(Mechanic mechanic, WorkOrder workOrder) {
		Intervention i = new Intervention(mechanic, workOrder, SIXTY_MINS);
		SparePart sp = new SparePart("R1001", "junta la trocla", EUR_100);
		new Substitution(sp, i, NUM_OF_SPARES);
	}

}