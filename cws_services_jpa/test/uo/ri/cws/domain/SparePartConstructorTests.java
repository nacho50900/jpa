package uo.ri.cws.domain;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Scenarios:
 * - Spare part code cannot be null, empty or blank
 * - Spare part description cannot be null, empty or blank
 * - Cannot be negative values for price, stock, minStock or maxStock
 */
class SparePartConstructorTests {

	@BeforeEach
	void setUp() throws Exception {
	}

	/**
	 * WHEN: we create a spare part with null, empty or blank code
	 * THEN: an IllegalArgumentException is thrown
	 */
	@Test
	void testCodeNull() {
		assertThrows(IllegalArgumentException.class, () -> new SparePart(null));
		assertThrows(IllegalArgumentException.class, () -> new SparePart(""));
		assertThrows(IllegalArgumentException.class, () -> new SparePart(" "));
	}

	/**
	 * WHEN: we create a spare part with null, empty or blank description 
	 * THEN: an IllegalArgumentException is thrown
	 */
	@Test
	void testDescriptionCannotBeNull() {
		assertThrows(
				IllegalArgumentException.class, 
				() -> new SparePart( "code", null, 0 )
			);
		
		assertThrows(
				IllegalArgumentException.class, 
				() -> new SparePart( "code", "", 0 )
			);
		
		assertThrows(
				IllegalArgumentException.class, 
				() -> new SparePart( "code", "  ", 0 )
			);
	}

	/**
	 * WHEN: we create a spare part with negative price, stock, minStock or
	 * maxStock THEN: an IllegalArgumentException is thrown
	 */
	@Test
	void testPriceCannotBeNgative() {
		assertThrows( 
				IllegalArgumentException.class, 
				() -> new SparePart( "code", "description", -1.0, 0, 0, 0 )
			);

		assertThrows( IllegalArgumentException.class, () -> {
			new SparePart( "code", "description", 0.0, -1, 0, 0 );
		});

		assertThrows( IllegalArgumentException.class, () -> {
			new SparePart( "code", "description", 0.0, 0, -1, 0 );
		});

		assertThrows( IllegalArgumentException.class, () -> {
			new SparePart( "code", "description", 0.0, 0, 0, -1 );
		});
	}
}