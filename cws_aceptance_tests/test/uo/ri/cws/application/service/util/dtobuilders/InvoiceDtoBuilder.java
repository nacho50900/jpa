package uo.ri.cws.application.service.util.dtobuilders;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import uo.ri.cws.application.service.invoice.InvoicingService.InvoiceDto;
import uo.ri.util.random.Random;

public class InvoiceDtoBuilder {

	private InvoiceDto dto = createDefaultInvoiceRecord();

	public InvoiceDto build() {
		return dto;
	}
	
	private LocalDate randomDate() {
		LocalDate dateBefore = LocalDate.parse("2020-01-01");
		LocalDate dateAfter = LocalDate.now();
		long noOfDaysBetween = ChronoUnit.DAYS.between(dateBefore, dateAfter);
		return dateBefore.plusDays( Random.inRange(0, noOfDaysBetween) );
	}

	private InvoiceDto createDefaultInvoiceRecord() {
		InvoiceDto res = new InvoiceDto();

		res.date = randomDate();
		res.state = "NOT_YET_PAID";
		res.amount = Random.inRange(100.0, 500.0);
		res.vat = res.amount * 0.21;
		return res;
	}
	
	public InvoiceDtoBuilder withState(String state) {
		dto.state =  state;
		return this;
	}
	
	public InvoiceDtoBuilder withAmount(double amount) {
		dto.amount = amount;
		return this;
	}
	
	public InvoiceDtoBuilder withDate(String date) {
		dto.date = LocalDate.parse(date);
		return this;
	}

	public InvoiceDtoBuilder withVat(double arg) {
		dto.vat = arg;
		return this;
	}

	public InvoiceDtoBuilder withNumber(long arg) {
		dto.number = arg;
		return this;
	}
}
