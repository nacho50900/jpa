package uo.ri.cws.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Scenarios:
 * - A charge to a credit card increases the accumulated
 * - A charge to cash increases the accumulated
 * - A charge to a voucher increases the accumulated and decreases the available
 * - A credit card cannot be charged if its expiration date is over
 * - A voucher cannot be charged if it has no available money
 * - A charge with a null invoice throws IAE
 * - A charge with a null payment mean throws IAE
 */
class ChargeConstructorTests {

	private static final double AMOUNT = 100.0;
	private Invoice invoice;

	@BeforeEach
	void setUp() throws Exception {
		invoice = new Invoice(123L);
	}

	/**
	 * GIVEN: a CreditCard with a validity date in the future and an Invoice
	 * WHEN: a Charge is created with that card and invoice
	 * THEN: the charge is created correctly
	 * AND: the accumulated of the card is increased by the amount of the charge
	 * AND: the charge is in the invoice charges list
	 * AND: the charge is in the card charges list
	 * AND: the invoice of the charge is the given invoice
	 * AND: the payment mean of the charge is the given card
	 * AND: the amount of the charge is the given amount
	 */
	@Test
	void testCardCharge() {
		LocalDate tomorrow = LocalDate.now().plus(1, ChronoUnit.DAYS);
		CreditCard card = new CreditCard("123", "visa", tomorrow);
		Charge c = new Charge(invoice, card, AMOUNT);

		assertValid(c, card);
	}

	private void assertValid(Charge c, PaymentMean card) {
		assertEquals(AMOUNT, card.getAccumulated() );
		assertTrue( invoice.getCharges().contains( c ) );
		assertTrue( card.getCharges().contains( c ) );
		assertSame( invoice, c.getInvoice() );
		assertSame( card, c.getPaymentMean() );
	}

	/**
	 * GIVEN: a Cash payment mean and an Invoice
	 * WHEN: a Charge is created with that cash and invoice
	 * THEN: the charge is created correctly 
	 */
	@Test
	void testCashCharge() {
		Cash m = new Cash(new Client("123", "n", "a"));
		Charge c = new Charge(invoice, m, AMOUNT);

		assertValid(c, m);
	}

	/**
	 * GIVEN: a Voucher with available money and an Invoice
	 * WHEN: a Charge is created with that voucher and invoice
	 * THEN: the charge is created correctly
	 */
	@Test
	void testVoucherCharge() {
		Voucher v = new Voucher("123", "For testing", 150.0);

		Charge c = new Charge(invoice, v, AMOUNT);

		assertValid(c, v);
	}

	/**
	 * GIVEN: a CreditCard with a validity date in the past and an Invoice
	 * WHEN: a Charge is created with that card and invoice
	 * THEN: an IllegalStateException is thrown
	 */
	@Test
	void tesChargeWithExpiredCard() {
		LocalDate expired = LocalDate.now().minus(1, ChronoUnit.DAYS);
		CreditCard t = new CreditCard("123", "TTT", expired);

		assertThrows(IllegalStateException.class, () -> 
			new Charge(invoice, t, AMOUNT) // Card validity date expired exception
		);
	}

	/**
	 * GIVEN: a Voucher with no available money and an Invoice
	 * WHEN: a Charge is created with that voucher and invoice
	 * THEN: an IllegalStateException is thrown
	 */
	@Test
	void testEmptyVoucherCannotBeCharged() {
		Voucher v = new Voucher("123", "For testing", AMOUNT);

		assertThrows(IllegalStateException.class, () -> 
			new Charge(invoice, v, AMOUNT + 1)  // Not enough money exception
		);
	}
	
	/**
	 * GIVEN: a null Invoice
	 * WHEN: a Charge is created with that invoice
	 * THEN: an IllegalArgumentException is thrown
	 */
	@Test
	void testChargeWithNullInvoice() {
		CreditCard t = new CreditCard("123", "TTT",	LocalDate.now().plusDays(1));

		assertThrows(
				IllegalArgumentException.class,
				() -> new Charge(null, t, AMOUNT) // Null invoice exception
			);
	}
	
	/**
	 * GIVEN: a null PaymentMean
	 * WHEN: a Charge is created with that payment mean
	 * THEN: an IllegalArgumentException is thrown
	 */
	@Test
	void testChargeWithNullPaymentMean() {
		assertThrows(
				IllegalArgumentException.class,
				() -> new Charge(invoice, null, AMOUNT) // Null payment mean
		);
	}
}