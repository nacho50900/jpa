package uo.ri.cws.application.service.invoice.create.command;

import java.util.Map;
import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.InvoiceRepository;
import uo.ri.cws.application.repository.PaymentMeanRepository;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Charge;
import uo.ri.cws.domain.Invoice;
import uo.ri.cws.domain.PaymentMean;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class SettleInvoice implements Command<Void> {

	private String invoiceId;
	private Map<String, Double> charges;
	private InvoiceRepository invoiceRepo = Factories.repository.forInvoice();
	private PaymentMeanRepository paymentMeanRepo = Factories.repository.forPaymentMean();

	public SettleInvoice(String invoiceId, Map<String, Double> charges) {
		ArgumentChecks.isNotNull(invoiceId);
		ArgumentChecks.isNotEmpty(invoiceId);
		ArgumentChecks.isNotNull(charges);
		ArgumentChecks.isFalse(charges.isEmpty(), "Charges cannot be empty");
		
		this.invoiceId = invoiceId;
		this.charges = charges;
	}

	@Override
	public Void execute() throws BusinessException {
	    Optional<Invoice> invoiceOpt = invoiceRepo.findById(invoiceId);
	    BusinessChecks.isTrue(invoiceOpt.isPresent(), "Invoice not found");
	    
	    Invoice invoice = invoiceOpt.get();
	    BusinessChecks.isTrue(invoice.isNotSettled(), "Invoice is already settled");
	    
	    for (Map.Entry<String, Double> entry : charges.entrySet()) {
	        String paymentMeanId = entry.getKey();
	        Double amount = entry.getValue();
	        
	        ArgumentChecks.isTrue(amount > 0, "Charge amount must be positive");
	        
	        Optional<PaymentMean> pmOpt = paymentMeanRepo.findById(paymentMeanId);
	        BusinessChecks.isTrue(pmOpt.isPresent(), "Payment mean not found");
	        
	        PaymentMean paymentMean = pmOpt.get();

	        new Charge(invoice, paymentMean, amount);
	    }
	    invoice.settle();
	    return null;
	}
}