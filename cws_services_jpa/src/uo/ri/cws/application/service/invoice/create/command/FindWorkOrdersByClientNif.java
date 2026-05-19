package uo.ri.cws.application.service.invoice.create.command;

import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.WorkOrderRepository;
import uo.ri.cws.application.service.invoice.InvoicingService.InvoicingWorkOrderDto;
import uo.ri.cws.application.service.invoice.create.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.WorkOrder;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessException;

public class FindWorkOrdersByClientNif implements Command<List<InvoicingWorkOrderDto>> {

	private String nif;
	private WorkOrderRepository workOrderRepo = Factories.repository.forWorkOrder();

	public FindWorkOrdersByClientNif(String nif) {
		ArgumentChecks.isNotNull(nif);
		ArgumentChecks.isNotEmpty(nif);
		this.nif = nif;
	}

	@Override
	public List<InvoicingWorkOrderDto> execute() throws BusinessException {
		List<WorkOrder> workOrders = workOrderRepo.findByClientNif(nif);
		
		return DtoAssembler.toInvoicingWorkOrderDtoList(workOrders);
	}
}