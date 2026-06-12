package uo.ri.cws.application.service.invoice.create;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.invoice.InvoicingService;
import uo.ri.cws.application.service.invoice.create.command.CreateInvoiceFor;
import uo.ri.cws.application.service.invoice.create.command.FindInvoiceByNumber;
import uo.ri.cws.application.service.invoice.create.command.FindNotInvoicedWorkOrdersByClientNif;
import uo.ri.cws.application.service.invoice.create.command.FindPayMeansByClientNif;
import uo.ri.cws.application.service.invoice.create.command.FindWorkOrdersByClientNif;
import uo.ri.cws.application.service.invoice.create.command.FindWorkOrdersByPlateNumber;
import uo.ri.cws.application.service.invoice.create.command.SettleInvoice;
import uo.ri.cws.application.util.command.CommandExecutor;
import uo.ri.util.exception.BusinessException;

public class InvoicingServiceImpl implements InvoicingService {

	private CommandExecutor executor = Factories.executor.forExecutor();

	@Override
	public InvoiceDto create(List<String> woIds) throws BusinessException {
		return executor.execute(new CreateInvoiceFor(woIds));
	}

	@Override
	public List<InvoicingWorkOrderDto> findWorkOrdersByClientNif(String nif)
			throws BusinessException {
		return executor.execute(new FindWorkOrdersByClientNif(nif));
	}

	@Override
	public List<InvoicingWorkOrderDto> findNotInvoicedWorkOrdersByClientNif(String nif) 
			throws BusinessException {
		return executor.execute(new FindNotInvoicedWorkOrdersByClientNif(nif));
	}

	@Override
	public List<InvoicingWorkOrderDto> findWorkOrdersByPlateNumber(String plate)
			throws BusinessException {
		return executor.execute(new FindWorkOrdersByPlateNumber(plate));
	}

	@Override
	public Optional<InvoiceDto> findInvoiceByNumber(Long number)
			throws BusinessException {
		return executor.execute(new FindInvoiceByNumber(number));
	}

	@Override
	public List<PaymentMeanDto> findPayMeansByClientNif(String nif)
			throws BusinessException {
		return executor.execute(new FindPayMeansByClientNif(nif));
	}

	@Override
	public void settleInvoice(String invoiceId, Map<String, Double> charges)
			throws BusinessException {
		executor.execute(new SettleInvoice(invoiceId, charges));
	}
}