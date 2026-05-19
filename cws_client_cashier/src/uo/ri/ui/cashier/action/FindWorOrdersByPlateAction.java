package uo.ri.ui.cashier.action;

import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.invoice.InvoicingService;
import uo.ri.cws.application.service.invoice.InvoicingService.InvoicingWorkOrderDto;
import uo.ri.ui.util.Printer;
import uo.ri.util.console.Console;
import uo.ri.util.menu.Action;

public class FindWorOrdersByPlateAction implements Action {

	@Override
	public void execute() throws Exception {
		InvoicingService cs = Factories.service.forCreateInvoiceService();

		String plate = Console.readString("Plate number");

		List<InvoicingWorkOrderDto> reps = cs.findWorkOrdersByPlateNumber( plate );

		if (reps.size() == 0) {
			Console.printf("There is not work orders for the vehicle\n");
			return;
		}

		Console.println("\nWork orders for the vehicle\n");
		for(InvoicingWorkOrderDto rep : reps) {
			Printer.printWorkOrder( rep );
		}
	}

}
