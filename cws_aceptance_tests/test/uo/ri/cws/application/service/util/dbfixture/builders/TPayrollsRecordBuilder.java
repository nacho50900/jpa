package uo.ri.cws.application.service.util.dbfixture.builders;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.UUID;

import javax.annotation.processing.Generated;

import uo.ri.cws.application.service.util.dbfixture.records.TPayrollsRecord;
import uo.ri.util.random.Random;

@Generated("LLM")
public class TPayrollsRecordBuilder {

	private TPayrollsRecord record = createDefaultRecord();

	public TPayrollsRecordBuilder withId(String id) {
		record.id = id;
		return this;
	}

	public TPayrollsRecordBuilder withBaseSalary(double baseSalary) {
		record.baseSalary = baseSalary;
		return this;
	}

	public TPayrollsRecordBuilder withCreatedAt(Timestamp createdAt) {
		record.createdAt = createdAt;
		return this;
	}

	public TPayrollsRecordBuilder withDate(Date date) {
		record.date = date;
		return this;
	}

	public TPayrollsRecordBuilder withEntityState(String entityState) {
		record.entityState = entityState;
		return this;
	}

	public TPayrollsRecordBuilder withExtraSalary(double extraSalary) {
		record.extraSalary = extraSalary;
		return this;
	}

	public TPayrollsRecordBuilder withNicDeduction(double nicDeduction) {
		record.nicDeduction = nicDeduction;
		return this;
	}

	public TPayrollsRecordBuilder withProductivityEarning(double productivityEarning) {
		record.productivityEarning = productivityEarning;
		return this;
	}

	public TPayrollsRecordBuilder withTaxDeduction(double taxDeduction) {
		record.taxDeduction = taxDeduction;
		return this;
	}

	public TPayrollsRecordBuilder withTrienniumEarning(double trienniumEarning) {
		record.trienniumEarning = trienniumEarning;
		return this;
	}

	public TPayrollsRecordBuilder withUpdatedAt(Timestamp updatedAt) {
		record.updatedAt = updatedAt;
		return this;
	}

	public TPayrollsRecordBuilder withVersion(long version) {
		record.version = version;
		return this;
	}

	public TPayrollsRecordBuilder forContractId(String contractId) {
		record.contract_Id = contractId;
		return this;
	}

	public TPayrollsRecord build() {
		return record;
	}

	private TPayrollsRecord createDefaultRecord() {
		TPayrollsRecord defaultRecord = new TPayrollsRecord();
		defaultRecord.id = UUID.randomUUID().toString();
		defaultRecord.createdAt = new Timestamp(System.currentTimeMillis());
		defaultRecord.updatedAt = new Timestamp(System.currentTimeMillis());
		defaultRecord.version = 1L;
		defaultRecord.entityState = "ENABLED";

		defaultRecord.date = new Date(System.currentTimeMillis());
		defaultRecord.baseSalary = 0.0;
		defaultRecord.extraSalary = 0.0;
		defaultRecord.productivityEarning = 0.0;
		defaultRecord.trienniumEarning = 0.0;
		defaultRecord.taxDeduction = 0.0;
		defaultRecord.nicDeduction = 0.0;
		defaultRecord.contract_Id = null;
		return defaultRecord;
	}

	public TPayrollsRecordBuilder randomizeSalariesAndDeductions() {
		double baseSalary = Random.inRange(1000.0, 2000.0);
		withBaseSalary( baseSalary );
		withExtraSalary( Random.choose(0, 0, 0, 0, 0, 1) * baseSalary);
		withTrienniumEarning( Random.choose(0, 0, 1, 2, 3) * 30.0 );
		withProductivityEarning( Random.inRange( 50.0, 300.0) );
		withNicDeduction( baseSalary * 0.05);
		withTaxDeduction( baseSalary * 0.20 );
		return this;
	}
}
