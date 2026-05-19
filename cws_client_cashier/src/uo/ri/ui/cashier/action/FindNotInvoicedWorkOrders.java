package uo.ri.ui.cashier.action;

import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.invoice.InvoicingService;
import uo.ri.cws.application.service.invoice.InvoicingService.InvoicingWorkOrderDto;
import uo.ri.ui.util.Printer;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class FindNotInvoicedWorkOrders implements Action {

	@Override
	public void execute() throws BusinessException {
		InvoicingService cs = Factories.service.forCreateInvoiceService();

		String nif = Console.readString("Client nif");

		Console.println("\nInvoice-pending work orders\n");

		List<InvoicingWorkOrderDto> reps = cs.findWorkOrdersByClientNif( nif );

		if (reps.size() == 0) {
			Console.printf("There is no pending work orders\n");
			return;
		}

		for(InvoicingWorkOrderDto rep : reps) {
			Printer.printWorkOrder( rep );
		}
	}

}
