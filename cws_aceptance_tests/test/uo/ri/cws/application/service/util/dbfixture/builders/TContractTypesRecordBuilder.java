package uo.ri.cws.application.service.util.dbfixture.builders;

import java.sql.Timestamp;
import java.util.UUID;

import javax.annotation.processing.Generated;

import uo.ri.cws.application.service.util.dbfixture.records.TContractTypesRecord;

@Generated("LLM")
public class TContractTypesRecordBuilder {

	private TContractTypesRecord record = createDefaultRecord();

	public TContractTypesRecordBuilder withId(String id) {
		record.id = id;
		return this;
	}

	public TContractTypesRecordBuilder withCompensationDaysPerYear(double compensationDaysPerYear) {
		record.compensationDaysPerYear = compensationDaysPerYear;
		return this;
	}

	public TContractTypesRecordBuilder withCreatedAt(Timestamp createdAt) {
		record.createdAt = createdAt;
		return this;
	}

	public TContractTypesRecordBuilder withEntityState(String entityState) {
		record.entityState = entityState;
		return this;
	}

	public TContractTypesRecordBuilder withName(String name) {
		record.name = name;
		return this;
	}

	public TContractTypesRecordBuilder fixedTerm() {
		return withName("FIXED_TERM");
	}

	public TContractTypesRecordBuilder withUpdatedAt(Timestamp updatedAt) {
		record.updatedAt = updatedAt;
		return this;
	}

	public TContractTypesRecordBuilder withVersion(Long version) {
		record.version = version;
		return this;
	}

	public TContractTypesRecord build() {
		return record;
	}

	private TContractTypesRecord createDefaultRecord() {
		TContractTypesRecord defaultRecord = new TContractTypesRecord();
		defaultRecord.id = UUID.randomUUID().toString();
		defaultRecord.compensationDaysPerYear = 0.0;
		defaultRecord.createdAt = new Timestamp(System.currentTimeMillis());
		defaultRecord.entityState = "ENABLED";
		defaultRecord.name = "Default Contract Type " + defaultRecord.id.substring(0, 5);
		defaultRecord.updatedAt = new Timestamp(System.currentTimeMillis());
		defaultRecord.version = 1L;
		return defaultRecord;
	}

}
