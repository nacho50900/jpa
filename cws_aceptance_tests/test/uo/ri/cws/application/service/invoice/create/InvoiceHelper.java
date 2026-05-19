package uo.ri.cws.application.service.invoice.create;

import java.util.List;

import uo.ri.cws.application.service.invoice.InvoicingService.InvoiceDto;
import uo.ri.cws.application.service.util.dbfixture.records.TWorkOrdersRecord;
import uo.ri.cws.application.service.util.dtobuilders.InvoiceDtoBuilder;
import uo.ri.util.math.Rounds;

public class InvoiceHelper {

	public static InvoiceDto computeFor(TWorkOrdersRecord wo) {
		double taxRate = 0.21;
		double taxAmount = wo.amount * taxRate;
		double totalAmount = wo.amount + taxAmount;
		
		return toDto(taxAmount, totalAmount);
	}

	public static InvoiceDto computeFor(List<TWorkOrdersRecord> workOrders) {
		double total = workOrders.stream().mapToDouble(wo -> wo.amount).sum();
		double taxRate = 0.21;
		double taxAmount = total * taxRate;
		double totalAmount = total + taxAmount;
		
		return toDto(taxAmount, totalAmount);
	}

	private static InvoiceDto toDto(double taxAmount, double totalAmount) {
		return new InvoiceDtoBuilder()
			.withAmount( Rounds.toCents(totalAmount) )
			.withVat( Rounds.toCents( taxAmount ) )
			.withState("NOT_YET_PAID")
			.build();
	}

}
