package uo.ri.ui.util;

import java.util.List;

import uo.ri.cws.application.service.invoice.InvoicingService.CardDto;
import uo.ri.cws.application.service.invoice.InvoicingService.InvoiceDto;
import uo.ri.cws.application.service.invoice.InvoicingService.InvoicingWorkOrderDto;
import uo.ri.cws.application.service.invoice.InvoicingService.PaymentMeanDto;
import uo.ri.cws.application.service.invoice.InvoicingService.VoucherDto;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.util.console.Console;

public class Printer {

	public static void printInvoice(InvoiceDto invoice) {

		double importeConIVa = invoice.amount;
		double iva =  invoice.vat;
		double importeSinIva = importeConIVa / (1 + iva / 100);

		Console.printf("Factura nº: %d\n", 				invoice.number );
		Console.printf("\tFecha: %1$td/%1$tm/%1$tY\n", 	invoice.date);
		Console.printf("\tTotal: %.2f €\n", 			importeSinIva);
		Console.printf("\tIva: %.1f %% \n", 			invoice.vat );
		Console.printf("\tTotal con IVA: %.2f €\n", 	invoice.amount );
		Console.printf("\tEstado: %s\n", 				invoice.state );
	}

	public static void printPaymentMeans(List<PaymentMeanDto> medios) {
		Console.println();
		Console.println("Medios de pago disponibles");

		Console.printf("\t%s \t%-8.8s \t%s \n", "ID", "Type", "Acummulated");
		for (PaymentMeanDto medio : medios) {
			printPaymentMean( medio );
		}
	}

	private static void printPaymentMean(PaymentMeanDto medio) {
		String name = medio.getClass().getName().substring(55);
		String extra = formatExtra( medio );
		Console.printf("\t%s \t%-15.15s \t%.2f \t%s\n",
				medio.id,
				name,  // not the best...
				medio.accumulated,
				extra
		);
	}

	private static String formatExtra(PaymentMeanDto mean) {
		if ( mean instanceof VoucherDto) {
			return "ava " + ((VoucherDto)mean).available;
		} else if (mean instanceof CardDto) {
			return "exp " + ((CardDto)mean).cardExpiration;
		}
		return "";
	}

	public static void printWorkOrder(InvoicingWorkOrderDto rep) {

		Console.printf("\t%s \t%-40.40s \t%td/%<tm/%<tY \t%-12.12s \t%.2f\n",
				rep.id
				, rep.description
				, rep.date
				, rep.state
				, rep.amount
		);
	}

	public static void printMechanic(MechanicDto m) {

		Console.printf("\t%s %-10.10s %-25.25s %-25.25s\n",
				m.id
				, m.nif
				, m.name
				, m.surname
			);
	}

}
