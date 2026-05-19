package uo.ri.cws.application.service.util.dbfixture.builders;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.UUID;

import uo.ri.cws.application.service.util.dbfixture.records.TContractsRecord;

public class TContractsRecordBuilder {

	private TContractsRecord record = createDefaultRecord();

	public TContractsRecordBuilder withId(String id) {
		record.id = id;
		return this;
	}

	public TContractsRecordBuilder withAnnualBaseSalary(double annualBaseSalary) {
		record.annualBaseSalary = annualBaseSalary;
		return this;
	}

	public TContractsRecordBuilder withCreatedAt(Timestamp createdAt) {
		record.createdAt = createdAt;
		return this;
	}

	public TContractsRecordBuilder withEndDate(Date endDate) {
		record.endDate = endDate;
		return this;
	}

	public TContractsRecordBuilder withEntityState(String entityState) {
		record.entityState = entityState;
		return this;
	}

	public TContractsRecordBuilder withSettlement(double settlement) {
		record.settlement = settlement;
		return this;
	}

	public TContractsRecordBuilder withStartDate(Date startDate) {
		record.startDate = startDate;
		return this;
	}

	public TContractsRecordBuilder withState(String state) {
		record.state = state;
		return this;
	}

	public TContractsRecordBuilder withTaxRate(double taxRate) {
		record.taxRate = taxRate;
		return this;
	}

	public TContractsRecordBuilder withUpdatedAt(Timestamp updatedAt) {
		record.updatedAt = updatedAt;
		return this;
	}

	public TContractsRecordBuilder withVersion(long version) {
		record.version = version;
		return this;
	}

	public TContractsRecordBuilder forContractTypeId(String contractTypeId) {
		record.contractType_Id = contractTypeId;
		return this;
	}

	public TContractsRecordBuilder forMechanicId(String mechanicId) {
		record.mechanic_Id = mechanicId;
		return this;
	}

	public TContractsRecordBuilder forProfessionalGroupId(
			String professionalGroupId) {
		record.professionalGroup_Id = professionalGroupId;
		return this;
	}

	public TContractsRecordBuilder inForce() {
		record.state = "IN_FORCE";
		return this;
	}

	public TContractsRecordBuilder terminated() {
		record.state = "TERMINATED";
		return this;
	}

	public TContractsRecord build() {
		return record;
	}

	private TContractsRecord createDefaultRecord() {
		TContractsRecord defaultRecord = new TContractsRecord();
		defaultRecord.id = UUID.randomUUID().toString();
		defaultRecord.annualBaseSalary = 1000.0;
		defaultRecord.createdAt = new Timestamp(System.currentTimeMillis());
		defaultRecord.endDate = null;
		defaultRecord.entityState = "ENABLED";
		defaultRecord.settlement = 0.0;
		defaultRecord.startDate = new Date(System.currentTimeMillis());
		defaultRecord.state = "IN_FORCE";
		defaultRecord.taxRate = 0.0;
		defaultRecord.updatedAt = new Timestamp(System.currentTimeMillis());
		defaultRecord.version = 1L;
		defaultRecord.contractType_Id = null;
		defaultRecord.mechanic_Id = null;
		defaultRecord.professionalGroup_Id = null;
		return defaultRecord;
	}
}
