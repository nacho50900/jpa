package uo.ri.cws.application.service.util.dbfixture.builders;

import java.util.Random;
import java.util.UUID;

import javax.annotation.processing.Generated;

import uo.ri.cws.application.service.util.dbfixture.records.TVouchersRecord;

@Generated("LLM")
public class TVouchersRecordBuilder {

	private TVouchersRecord record = createDefaultRecord();

	public TVouchersRecordBuilder withId(String id) {
		record.id = id;
		return this;
	}

	public TVouchersRecordBuilder withAvailable(Double available) {
		record.available = available;
		return this;
	}

	public TVouchersRecordBuilder withCode(String code) {
		record.code = code;
		return this;
	}

	public TVouchersRecordBuilder withDescription(String description) {
		record.description = description;
		return this;
	}

	public TVouchersRecord build() {
		return record;
	}

	private TVouchersRecord createDefaultRecord() {
		TVouchersRecord defaultRecord = new TVouchersRecord();
		defaultRecord.id = UUID.randomUUID().toString();
		defaultRecord.available = 0.0;
		defaultRecord.code = "DEFAULT-VOUCHER " + new Random().nextInt(10000);
		defaultRecord.description = "Default Voucher";
		return defaultRecord;
	}
}
