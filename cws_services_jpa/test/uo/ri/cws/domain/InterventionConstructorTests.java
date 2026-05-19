package uo.ri.cws.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Scenarios:
 * - An intervention with no time nor substitutions amounts 0.0 €
 * - An intervention with 60 minutes amounts the price of an labor hour
 * - An intervention with just one spare part amounts the price of it
 * - An intervention with time and spare parts returns the right amount
 * - The date of an intervention must be stored truncated to milliseconds
 * - An intervention with negative minutes throws IllegalArgumentException
 * - An intervention with null work order throws IllegalArgumentException
 * - An intervention with null mechanic throws IllegalArgumentException
 */
class InterventionConstructorTests {

	private static final double SPARE_PRICE = 100.0;
	private static final int SIXTY_MINS = 60;
	private static final double PRICE_PER_HOUR = 50.0;
	private Mechanic mechanic;
	private WorkOrder workOrder;
	private Vehicle vehicle;
	private VehicleType vehicleType;

	@BeforeEach
	void setUp() {
		vehicle = new Vehicle("1234 GJI", "seat", "ibiza");
		vehicleType = new VehicleType("coche", PRICE_PER_HOUR);
		Associations.Classifies.link(vehicleType, vehicle);

		workOrder = new WorkOrder(vehicle, "falla la junta la trocla");
		mechanic = new Mechanic("nif-mecanico", "nombre", "apellidos");
	}

	/**
	 * GIVEN: a work order and a mechanic 
	 * WHEN: we create an intervention 
	 * THEN: the intervention is correctly created
	 * AND: the timestamp is adjusted to milliseconds to avoid database round problems
	 */
	@Test
	void testAmountsZero() {
		Intervention i = new Intervention(mechanic, workOrder, 0);

		assertEquals( 0.0, i.getAmount() );
		LocalDateTime date = i.getDate().truncatedTo( ChronoUnit.MILLIS );
		assertEquals( date, i.getDate() );
	}

	/**
	 * GIVEN: a work order and a mechanic
	 * WHEN: we create an intervention with 60 minutes
	 * THEN: the intervention amounts the price of an labor hour
	 */
	@Test
	void testAmountOneHour() {
		Intervention i = new Intervention(mechanic, workOrder, SIXTY_MINS);

		assertEquals( vehicleType.getPricePerHour(), i.getAmount(), 0.001 );
	}

	/**
	 * GIVEN: a work order and a mechanic
	 * WHEN: we create an intervention with just one spare part
	 * THEN: the intervention amounts the price of the spare part
	 */
	@Test
	void testSparePartAmount() {
		Intervention i = new Intervention(mechanic, workOrder, 0);
		SparePart r = new SparePart("R1001", "junta la trocla", 100.0);
		new Substitution(r, i, 1);

		assertEquals( r.getPrice(), i.getAmount(), 0.001 );
	}

	/**
	 * GIVEN: a work order and a mechanic
	 * WHEN: we create an intervention with time and spare parts
	 * THEN: the intervention returns the right amount
	 */
	@Test
	void testSpareAndTimeAmount() {
		Intervention i = new Intervention(mechanic, workOrder, 60);

		SparePart r = new SparePart("R1001", "junta la trocla", 100.0);
		new Substitution(r, i, 2);

		double expected =
				PRICE_PER_HOUR * SIXTY_MINS / 60.0  // labor price
				+ 2 * SPARE_PRICE; 					// spares price

		assertEquals( expected, i.getAmount(), 0.001 );
	}
	
	/**
	 * GIVEN: a work order and a mechanic
	 * WHEN: we create an intervention with negative minutes
	 * THEN: it throws IllegalArgumentException
	 */
	@Test
	void testInterventionThrowsExceptionIfNegativeMinutes() {
		assertThrows(
				IllegalArgumentException.class,
				() -> new Intervention(mechanic, workOrder, -1)
			);
	}

	/**
	 * GIVEN: a work order 
	 * WHEN: we create an intervention with null work order
	 * THEN: it throws IllegalArgumentException
	 */
	@Test
	void testInterventionThrowsExceptionIfNullWorkOrder() {
		assertThrows(
				IllegalArgumentException.class,
				() -> new Intervention(mechanic, null, 60)
			);
	}
	
	/**
	 * GIVEN: a mechanic 
	 * WHEN: we create an intervention with null mechanic
	 * THEN: it throws IllegalArgumentException
	 */
	@Test
	void testInterventionThrowsExceptionIfNullMechanic() {
		assertThrows(
				IllegalArgumentException.class,
				() -> new Intervention(null, workOrder, 60)
			);
	}
	
}