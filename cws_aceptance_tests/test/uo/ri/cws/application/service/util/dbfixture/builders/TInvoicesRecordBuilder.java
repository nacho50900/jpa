package uo.ri.cws.application.service.util.dbfixture.builders;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.UUID;

import javax.annotation.processing.Generated;

import uo.ri.cws.application.service.util.dbfixture.records.TInvoicesRecord;

@Generated("LLM")
public class TInvoicesRecordBuilder {

	private TInvoicesRecord record = createDefaultRecord();

	public TInvoicesRecordBuilder withId(String id) {
		record.id = id;
		return this;
	}

	public TInvoicesRecordBuilder withAmount(Double amount) {
		record.amount = amount;
		return this;
	}

	public TInvoicesRecordBuilder withCreatedAt(Timestamp createdAt) {
		record.createdAt = createdAt;
		return this;
	}

	public TInvoicesRecordBuilder withDate(Date date) {
		record.date = date;
		return this;
	}

	public TInvoicesRecordBuilder withEntityState(String entityState) {
		record.entityState = entityState;
		return this;
	}

	public TInvoicesRecordBuilder withNumber(Long number) {
		record.number = number;
		return this;
	}

	public TInvoicesRecordBuilder withState(String state) {
		record.state = state;
		return this;
	}

	public TInvoicesRecordBuilder withUpdatedAt(Timestamp updatedAt) {
		record.updatedAt = updatedAt;
		return this;
	}

	public TInvoicesRecordBuilder withVat(Double vat) {
		record.vat = vat;
		return this;
	}

	public TInvoicesRecordBuilder withVersion(Long version) {
		record.version = version;
		return this;
	}

	public TInvoicesRecordBuilder paid() {
		record.state = "PAID";
		return this;
	}

	public TInvoicesRecordBuilder notPaid() {
		record.state = "NOT_PAID";
		return this;
	}

	public TInvoicesRecord build() {
		return record;
	}

	private TInvoicesRecord createDefaultRecord() {
		TInvoicesRecord defaultRecord = new TInvoicesRecord();
		defaultRecord.id = UUID.randomUUID().toString();
		defaultRecord.amount = 0.0;
		defaultRecord.createdAt = new Timestamp(System.currentTimeMillis());
		defaultRecord.date = new Date(System.currentTimeMillis());
		defaultRecord.entityState = "ENABLED";
		defaultRecord.number = 1L;
		defaultRecord.state = "NOT_YET_PAID";
		defaultRecord.updatedAt = new Timestamp(System.currentTimeMillis());
		defaultRecord.vat = 0.0;
		defaultRecord.version = 1L;
		return defaultRecord;
	}
}
