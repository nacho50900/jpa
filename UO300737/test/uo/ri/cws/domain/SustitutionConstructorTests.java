package uo.ri.cws.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Scenarios: 
 * - A substitution with 0 spare parts throws IllegalArgumentException 
 * - A substitution with negative spare parts throws IllegalArgumentException 
 * - A substitution computes the right amount
 * - Cannot create a substitution with null intervention, throws IAE
 * - Cannot create a substitution with null spare part, throws IAE
 */
class SustitutionConstructorTests {

	private Intervention intervention;
	private SparePart spare;

	@BeforeEach
	void setUp() {
		Mechanic m = new Mechanic("nif-mecanico", "nombre", "apellidos");
		WorkOrder wo = mock(WorkOrder.class);
		intervention = new Intervention(m, wo, 60);
		spare = new SparePart("R1001", "junta la trocla", 100.0);
	}

	/**
	 * GIVEN: a spare part
	 * WHEN: we create a substitution with 2 spare parts
	 * THEN: It computes the right amount of a substitution
	 * AND: is linked bidirectionally to the intervention
	 * AND: is linked bidirectionally to the spare part
	 */
	@Test
	void testSubstitutionAmount() {
		Substitution s = new Substitution(spare, intervention, 2);
		
		double expectedAmount = 2 * spare.getPrice();
		assertEquals( expectedAmount, s.getAmount(), 0.001 );
		
		assertSame( intervention, s.getIntervention() );
		assertSame( spare, s.getSparePart() );
		assertTrue( intervention.getSubstitutions().contains(s) );
		assertTrue( spare.getSubstitutions().contains(s) );
	}

	/**
	 * GIVEN: a spare part
	 * WHEN: we create a substitution with 0 spare parts
	 * THEN: an IllegalArgumentException is thrown
	 */
	@Test
	void testSustitutionThrowsExceptionIfZero() {
		
		assertThrows(IllegalArgumentException.class, 
				() -> new Substitution(spare, intervention, 0)
			);
	}

	/**
	 * GIVEN: a spare part
	 * WHEN: we create a substitution with negative spare parts
	 * THEN: an IllegalArgumentException is thrown
	 */
	@Test
	void testSustitutionThrowsExceptionIfNegative() {
		assertThrows(IllegalArgumentException.class, 
				() -> new Substitution(spare, intervention, -1)
			);
	}

	/**
	 * GIVEN: a spare part 
	 * WHEN: we create a substitution with null intervention
	 * THEN: an IllegalArgumentException is thrown
	 */
	@Test
	void testSustitutionThrowsExceptionIfNullIntervention() {
		assertThrows(IllegalArgumentException.class,
				() -> new Substitution(spare, null, 2)
			);
	}
	
	/**
	 * GIVEN: an intervention 
	 * WHEN: we create a substitution with null spare part 
	 * THEN: an IllegalArgumentException is thrown
	 */
	@Test
	void testSustitutionThrowsExceptionIfNullSparePart() {
		assertThrows(IllegalArgumentException.class,
				() -> new Substitution(null, intervention, 2)
			);
	}
}