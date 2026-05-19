package uo.ri.cws.application.service.util.dbfixture.builders;

import java.sql.Timestamp;
import java.util.UUID;

import javax.annotation.processing.Generated;

import uo.ri.cws.application.service.util.dbfixture.records.TProfessionalGroupsRecord;

@Generated("LLM")
public class TProfessionalGroupsRecordBuilder {

	private TProfessionalGroupsRecord record = createDefaultRecord();

	public TProfessionalGroupsRecordBuilder withId(String id) {
		record.id = id;
		return this;
	}

	public TProfessionalGroupsRecordBuilder withCreatedAt(Timestamp createdAt) {
		record.createdAt = createdAt;
		return this;
	}

	public TProfessionalGroupsRecordBuilder withEntityState(String entityState) {
		record.entityState = entityState;
		return this;
	}

	public TProfessionalGroupsRecordBuilder withName(String name) {
		record.name = name;
		return this;
	}

	public TProfessionalGroupsRecordBuilder withProductivityRate(Double productivityRate) {
		record.productivityRate = productivityRate;
		return this;
	}

	public TProfessionalGroupsRecordBuilder withTrienniumPayment(Double trienniumPayment) {
		record.trienniumPayment = trienniumPayment;
		return this;
	}

	public TProfessionalGroupsRecordBuilder withUpdatedAt(Timestamp updatedAt) {
		record.updatedAt = updatedAt;
		return this;
	}

	public TProfessionalGroupsRecordBuilder withVersion(Long version) {
		record.version = version;
		return this;
	}

	public TProfessionalGroupsRecord build() {
		return record;
	}

	private TProfessionalGroupsRecord createDefaultRecord() {
		TProfessionalGroupsRecord defaultRecord = new TProfessionalGroupsRecord();
		defaultRecord.id = UUID.randomUUID().toString();
		defaultRecord.createdAt = new Timestamp(System.currentTimeMillis());
		defaultRecord.entityState = "ENABLED";
		defaultRecord.name = "Default Professional Group " + defaultRecord.id.substring(0, 5);
		defaultRecord.productivityRate = 0.0;
		defaultRecord.trienniumPayment = 0.0;
		defaultRecord.updatedAt = new Timestamp(System.currentTimeMillis());
		defaultRecord.version = 1L;
		return defaultRecord;
	}
}
