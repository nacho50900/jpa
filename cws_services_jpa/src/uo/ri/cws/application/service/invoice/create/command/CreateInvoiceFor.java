package uo.ri.cws.application.service.invoice.create.command;

import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.InvoiceRepository;
import uo.ri.cws.application.repository.WorkOrderRepository;
import uo.ri.cws.application.service.invoice.InvoicingService.InvoiceDto;
import uo.ri.cws.application.service.invoice.create.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Invoice;
import uo.ri.cws.domain.WorkOrder;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class CreateInvoiceFor implements Command<InvoiceDto>{

	private List<String> workOrderIds;
	private WorkOrderRepository workOrderRepo = Factories.repository.forWorkOrder();
	private InvoiceRepository invoiceRepo = Factories.repository.forInvoice();

	public CreateInvoiceFor(List<String> workOrderIds) {
		ArgumentChecks.isNotNull( workOrderIds );
		ArgumentChecks.isFalse( workOrderIds.isEmpty() );
		ArgumentChecks.isFalse(workOrderIds.stream().anyMatch(i -> i == null));
		
		this.workOrderIds = workOrderIds;
	}

	@Override
	public InvoiceDto execute() throws BusinessException {
		
		Long number = invoiceRepo.getNextInvoiceNumber();
		List<WorkOrder> workorders = workOrderRepo.findByIds(workOrderIds);
		BusinessChecks.isTrue(workOrderIds.size() == workorders.size());
		BusinessChecks.isTrue(allWorkOrdersAreFinished(workorders));
		
		Invoice i =new Invoice(number, workorders);
		invoiceRepo.add(i);
		
		return DtoAssembler.toDto(i);
	}

	private boolean allWorkOrdersAreFinished(List<WorkOrder> workorders) {
		return workorders.stream().allMatch(w -> w.isFinished());
	}

}
