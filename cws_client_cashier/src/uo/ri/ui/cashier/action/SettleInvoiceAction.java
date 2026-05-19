package uo.ri.ui.cashier.action;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.invoice.InvoicingService;
import uo.ri.cws.application.service.invoice.InvoicingService.InvoiceDto;
import uo.ri.cws.application.service.invoice.InvoicingService.PaymentMeanDto;
import uo.ri.ui.util.Printer;
import uo.ri.util.console.Console;
import uo.ri.util.exception.UserInteractionChecks;
import uo.ri.util.menu.Action;

public class SettleInvoiceAction implements Action {

	private InvoicingService cs = Factories.service.forCreateInvoiceService();

	@Override
	public void execute() throws Exception {
		Long number = Console.readLong("Invoice number?");
		Optional<InvoiceDto> oi = cs.findInvoiceByNumber(number);
		UserInteractionChecks.exists( oi, "There is no such invoice");
		InvoiceDto invoice = oi.get();

		Printer.printInvoice( invoice );

		String nif = Console.readString("Client nif?");
		List<PaymentMeanDto> means = cs.findPayMeansByClientNif(nif);
		UserInteractionChecks.isFalse( means.isEmpty(),
				"The client has no payment means or the nif is wrong"
			);

		Map<String, Double> charges = askForCharges(means, invoice.amount);

		cs.settleInvoice(invoice.id, charges);

		Console.println("The invoice has been settled");
	}

	private Map<String, Double> askForCharges(List<PaymentMeanDto> means,
			double totalAmount) {
		Map<String, Double> res = new HashMap<>();

		do {
			showPaymentMeans(means);
			String id = askForPaymentMeanId();
			Double amount = askForAmount();
			res.put(id, amount);

		} while ( sumAmounts( res.values() ) < totalAmount );

		return res;
	}

	private double sumAmounts(Collection<Double> amounts) {
		return amounts.stream().mapToDouble(a -> a).sum();
	}

	private Double askForAmount() {
		return Console.readDouble("Amount to charge?");
	}

	private String askForPaymentMeanId() {
		return Console.readString("Payment mean id?");
	}

	private void showPaymentMeans(List<PaymentMeanDto> means) {
		Printer.printPaymentMeans( means );
	}

}
