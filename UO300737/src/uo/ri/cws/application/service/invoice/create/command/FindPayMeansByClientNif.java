package uo.ri.cws.application.service.invoice.create.command;

import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.PaymentMeanRepository;
import uo.ri.cws.application.service.invoice.InvoicingService.PaymentMeanDto;
import uo.ri.cws.application.service.invoice.create.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.PaymentMean;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessException;

public class FindPayMeansByClientNif implements Command<List<PaymentMeanDto>> {

	private String nif;
	private PaymentMeanRepository paymentMeanRepo = Factories.repository.forPaymentMean();

	public FindPayMeansByClientNif(String nif) {
		ArgumentChecks.isNotNull(nif);
		ArgumentChecks.isNotEmpty(nif);
		this.nif = nif;
	}

	@Override
	public List<PaymentMeanDto> execute() throws BusinessException {
		List<PaymentMean> paymentMeans = paymentMeanRepo.findPaymentMeansByClientNif(nif);

		return DtoAssembler.toPaymentMeanDtoList(paymentMeans);
	}
}