package uo.ri.cws.persistence.association;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import uo.ri.cws.domain.Associations;
import uo.ri.cws.domain.Cash;
import uo.ri.cws.domain.Charge;
import uo.ri.cws.domain.Client;
import uo.ri.cws.domain.CreditCard;
import uo.ri.cws.domain.Invoice;
import uo.ri.cws.domain.Voucher;
import uo.ri.cws.persistence.util.UnitOfWork;

class SettleMappingTests {

	private UnitOfWork unitOfWork;
	private EntityManagerFactory factory;
	private Invoice invoice;
	private Client client;
	private CreditCard card;
	private Voucher voucher;
	private Cash cash;
	private Charge chargeWithCash;
	private Charge chargeWithCard;
	private Charge chargeWithVoucher;

	@BeforeEach
	void setUp() {
		factory = Persistence.createEntityManagerFactory("carworkshop");
		unitOfWork = UnitOfWork.over( factory );

		client = new Client("nif");
		invoice = new Invoice( 1L );
		card = new CreditCard("1234-5678", "visa",
				LocalDate.now().plus(1, ChronoUnit.MONTHS)
			);
		voucher = new Voucher("code", "a voucher", 100);
		cash = new Cash( client );

		Associations.Holds.link(card, client);
		Associations.Holds.link(voucher, client);
		// cash automatically linked

		chargeWithCash = new Charge(invoice, cash, 0);
		chargeWithCard = new Charge(invoice, card, 0);
		chargeWithVoucher = new Charge(invoice, voucher, 0);

		unitOfWork.persist(client, cash, voucher, card, invoice,
				chargeWithCash, chargeWithCard, chargeWithVoucher);
	}

	@AfterEach
	void tearDown() {
		unitOfWork.remove(client, cash, voucher, card, invoice,
				chargeWithCash, chargeWithCard, chargeWithVoucher);
		factory.close();
	}

	/**
	 * An invoice recovers all its charges
	 */
	@Test
	void testInvoiceRecoversCharges() {
		Invoice restored = unitOfWork.findById( Invoice.class, invoice.getId() );

		assertFalse( restored.getCharges().isEmpty() );
		assertFalse( restored.getCharges().isEmpty() );
		assertFalse( restored.getCharges().isEmpty() );
		assertEquals( 3, restored.getCharges().size() );
	}

	/**
	 * A cash payment mean recovers all its charges
	 */
	@Test
	void testCashRecoversCharges() {
		Cash restored = unitOfWork.findById( Cash.class, cash.getId() );

		assertFalse( restored.getCharges().isEmpty() );
		assertEquals( 1, restored.getCharges().size() );
	}

	/**
	 * A voucher payment mean recovers all its charges
	 */
	@Test
	void testVoucherRecoversCharges() {
		Voucher restored = unitOfWork.findById(Voucher.class, voucher.getId() );

		assertFalse( restored.getCharges().isEmpty() );
		assertEquals( 1, restored.getCharges().size() );
	}

	/**
	 * A card payment mean recovers all its charges
	 */
	@Test
	void testCardRecoversCharges() {
		CreditCard restored = unitOfWork.findById( CreditCard.class, card.getId() );

		assertFalse( restored.getCharges().isEmpty() );
		assertEquals( 1, restored.getCharges().size() );
	}

	/**
	 * A charge for cash recovers its cash and invoice
	 */
	@Test
	void testChargeForCashRecoversCharges() {
		Charge restored = unitOfWork.findById( Charge.class, chargeWithCash.getId() );

		assertEquals( invoice, restored.getInvoice() );
		assertEquals( cash, restored.getPaymentMean() );
	}

	/**
	 * A charge for voucher recovers its voucher and invoice
	 */
	@Test
	void testChargeForVoucherRecoversCharges() {
		Charge restored = unitOfWork.findById( Charge.class, chargeWithVoucher.getId() );

		assertEquals( invoice, restored.getInvoice() );
		assertEquals( voucher, restored.getPaymentMean() );
	}

	/**
	 * A charge for card recovers its card and invoice
	 */
	@Test
	void testChargeForCardRecoversCharges() {
		Charge restored = unitOfWork.findById( Charge.class, chargeWithCard.getId() );

		assertEquals( invoice, restored.getInvoice() );
		assertEquals( card, restored.getPaymentMean() );
	}
}