package uo.ri.cws.associations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uo.ri.cws.domain.Associations;
import uo.ri.cws.domain.Cash;
import uo.ri.cws.domain.Client;
import uo.ri.cws.domain.CreditCard;
import uo.ri.cws.domain.PaymentMean;

class HoldTests {
	private Client client;
	private Cash cash;

	@BeforeEach
	void setUp() {
		client = new Client("nif-cliente", "nombre", "apellidos");
		/*
		 * Cash constructor calls Associations.Hold.link( this, client )
		 */
		cash = new Cash( client );
	}
	
	@Test
	void testClientCashLinked() {
		assertTrue( client.getPaymentMeans().contains( cash ));
		assertEquals( client, cash.getClient() );
	}

	@Test
	void testClientCashUnlinked() {
		Associations.Holds.unlink(client, cash);
		
		assertFalse( client.getPaymentMeans().contains( cash ));
		assertTrue( client.getPaymentMeans().isEmpty() );
		assertNull( cash.getClient() );
	}
	
	@Test
	void testSafeReturn() {
		Set<PaymentMean> pms = client.getPaymentMeans();
		pms.remove(cash);

		assertTrue( pms.isEmpty() );
		assertEquals(1, client.getPaymentMeans().size(), 
                "It must be a copy of the collection"
			);
	}
	
	@Test
	void testHoldLink() {
		CreditCard cc = new CreditCard("123", "visa", LocalDate.now().plusYears(1));
		Associations.Holds.link(cc, client);
		
		assertTrue( client.getPaymentMeans().contains( cash ));
		assertTrue( client.getPaymentMeans().contains( cc ));
		
		assertEquals( client, cc.getClient() );
	}

}