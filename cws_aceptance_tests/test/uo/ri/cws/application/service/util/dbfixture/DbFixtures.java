package uo.ri.cws.application.service.util.dbfixture;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import uo.ri.cws.application.service.util.dbfixture.builders.TClientsRecordBuilder;
import uo.ri.cws.application.service.util.dbfixture.builders.TContractTypesRecordBuilder;
import uo.ri.cws.application.service.util.dbfixture.builders.TContractsRecordBuilder;
import uo.ri.cws.application.service.util.dbfixture.builders.TInterventionsRecordBuilder;
import uo.ri.cws.application.service.util.dbfixture.builders.TMechanicsRecordBuilder;
import uo.ri.cws.application.service.util.dbfixture.builders.TPayrollsRecordBuilder;
import uo.ri.cws.application.service.util.dbfixture.builders.TProfessionalGroupsRecordBuilder;
import uo.ri.cws.application.service.util.dbfixture.builders.TVehicleTypesRecordBuilder;
import uo.ri.cws.application.service.util.dbfixture.builders.TVehiclesRecordBuilder;
import uo.ri.cws.application.service.util.dbfixture.builders.TWorkOrdersRecordBuilder;
import uo.ri.cws.application.service.util.dbfixture.records.TClientsRecord;
import uo.ri.cws.application.service.util.dbfixture.records.TContractTypesRecord;
import uo.ri.cws.application.service.util.dbfixture.records.TContractsRecord;
import uo.ri.cws.application.service.util.dbfixture.records.TInterventionsRecord;
import uo.ri.cws.application.service.util.dbfixture.records.TMechanicsRecord;
import uo.ri.cws.application.service.util.dbfixture.records.TPayrollsRecord;
import uo.ri.cws.application.service.util.dbfixture.records.TProfessionalGroupsRecord;
import uo.ri.cws.application.service.util.dbfixture.records.TVehicleTypesRecord;
import uo.ri.cws.application.service.util.dbfixture.records.TVehiclesRecord;
import uo.ri.cws.application.service.util.dbfixture.records.TWorkOrdersRecord;
import uo.ri.util.date.Dates;
import uo.ri.util.random.Random;

public class DbFixtures {

	public static TMechanicsRecord aMechanic() {
		TMechanicsRecord mechanic = new TMechanicsRecordBuilder().build();
		Db.insert(mechanic);
		return mechanic;
	}

	public static TMechanicsRecord aMechanicOf(String nif) {
		
		TMechanicsRecord mechanic = new TMechanicsRecordBuilder()
				.withNif(nif)
				.build();
		Db.insert(mechanic);
		return mechanic;
	}

	public static TMechanicsRecord aMechanicOf(String nif, 
			String name, String surname) {
		
		TMechanicsRecord mechanic = new TMechanicsRecordBuilder()
				.withNif(nif)
				.withName(name)
				.withSurname(surname)
				.build();
		Db.insert(mechanic);
		return mechanic;
	}

	public static TMechanicsRecord aMechanicWithWorkOrders() {
		TMechanicsRecord m = aMechanic();
		aWorkOrderForMechanic(m.id);
		aWorkOrderForMechanic(m.id);
		return m;
	}

	public static TMechanicsRecord aMechanicWithInterventions() {
		TMechanicsRecord m = aMechanic();
		anInterventionForMechanic(m.id);
		anInterventionForMechanic(m.id);
		return m;
	}

	public static TMechanicsRecord aMechanicWithTerminatedContract() {
		TMechanicsRecord m = aMechanic();
		aContractTerminatedForMechanic(m.id);
		return m;
	}

	public static TMechanicsRecord aMechanicWithInForceContract() {
		TMechanicsRecord m = aMechanic();
		aContractInForceForMechanic(m.id);
		return m;
	}

	public static TMechanicsRecord aMechanicWithRecentInForceContract() {
		TMechanicsRecord m = aMechanic();
		TContractsRecord c = aContractInForceForMechanic(m.id);
		
		// make the contract start date 6 months ago
		c.startDate = Date.valueOf( LocalDate.now().minusMonths(6) );
		
		Db.update( c );
		return m;
	}

	public static TInterventionsRecord anInterventionForMechanic(String mId) {
		TInterventionsRecord i = new TInterventionsRecordBuilder()
				.forMechanicId( mId )
				.build();
		
		Db.insert( i );
		return i;
	}

	public static TContractsRecord aContractForMechanic(String mechanicId) {
		TContractsRecord c = new TContractsRecordBuilder()
				.forMechanicId( mechanicId )
				.build();
		Db.insert( c );
		return c;
	}

	public static TContractsRecord aContractTerminatedForMechanic(String mId) {
		TContractTypesRecord ct = aContractType();
		TProfessionalGroupsRecord pg = aProfessionalGroup();
		TContractsRecord c = new TContractsRecordBuilder()
				.forMechanicId( mId )
				.forContractTypeId( ct.id )
				.forProfessionalGroupId( pg.id )
				.terminated()
				.build();

		Db.insert( c );
		return c;
	}

	public static TWorkOrdersRecord aWorkOrderForMechanic(String mId) {
		TWorkOrdersRecord wo1 = new TWorkOrdersRecordBuilder()
				.forMechanicId(mId)
				.build();
		
		Db.insert( wo1 );
		return wo1;
	}

	public static TContractsRecord aContractInForceForMechanic(String mId) {
		TContractTypesRecord ct = aContractType();
		TProfessionalGroupsRecord pg = aProfessionalGroup();
		TContractsRecord c = new TContractsRecordBuilder()
				.forMechanicId(mId)
				.forContractTypeId(ct.id)
				.forProfessionalGroupId(pg.id)
				.withStartDate( Date.valueOf(LocalDate.now().minusMonths(6)) )
				.inForce()
				.build();
		Db.insert( c );
		return c;
	}

	public static TWorkOrdersRecord aClientWithVehicleAndOneFinishedWorkorder() {
		TClientsRecord client = aClient();
		TVehiclesRecord vehicle = aVehicleForClient(client.id);
		TWorkOrdersRecord wo = aWorkOrderFinishedForVehicle(vehicle.id);
		return wo;
	}

	public static List<TWorkOrdersRecord> someWorkordersFinishedWithVehicleAndClient() {
		TClientsRecord client = aClient();
		return someWorkordersFinishedWithVehicleForClient(client.id);
	}

	public static List<TWorkOrdersRecord> someWorkordersFinishedWithVehicleForClient(
			String clientId) {

		TVehiclesRecord vehicle = aVehicleForClient(clientId);
		return List.of(
				aWorkOrderFinishedForVehicle(vehicle.id),
				aWorkOrderFinishedForVehicle(vehicle.id),
				aWorkOrderFinishedForVehicle(vehicle.id)
			);
	}

	public static TWorkOrdersRecord aWorkOrderFinishedForVehicle(String vId) {
		return aWorkOrderForVehicleOf(vId, "FINISHED");
	}

	public static TWorkOrdersRecord aWorkOrderOpenForVehicle(String vId) {
		return aWorkOrderForVehicleOf(vId, "OPEN");
	}

	public static TWorkOrdersRecord aWorkOrderInvoicedForVehicle(String vId) {
		return aWorkOrderForVehicleOf(vId, "INVOICED");
	}

	public static TWorkOrdersRecord aWorkOrderAssignedForVehicle(String vId) {
		sleep(10); // to ensure different timestamps
		
		TMechanicsRecord m = aMechanic();
		TWorkOrdersRecord wo = new TWorkOrdersRecordBuilder()
				.forVehicleId(vId)
				.forMechanicId(m.id)
				.withState("ASSIGNED")
				.build();
		Db.insert(wo);
		return wo;
	}

	public static TWorkOrdersRecord aWorkOrderForVehicleOf(String vId,
			String state) {
		
		sleep(10); // to ensure different timestamps
		TWorkOrdersRecord wo = new TWorkOrdersRecordBuilder()
				.forVehicleId(vId)
				.withState( state )
				.build();
		Db.insert(wo);
		return wo;
	}

	public static TVehiclesRecord aVehicleForClient(String id) {
		TVehicleTypesRecord vt = aVehicleType();
		TVehiclesRecord vehicle = new TVehiclesRecordBuilder()
				.forClientId(id)
				.forVehicleTypeId(vt.id)
				.build();
		Db.insert(vehicle);
		return vehicle;
	}

	public static TVehicleTypesRecord aVehicleType() {
		TVehicleTypesRecord vt = new TVehicleTypesRecordBuilder().build();
		Db.insert(vt);
		return vt;
	}

	public static TClientsRecord aClient() {
		TClientsRecord client = new TClientsRecordBuilder().build();
		Db.insert(client);
		return client;
	}

	private static void sleep(int miliseconds) {
		try {
			Thread.sleep(miliseconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static TContractTypesRecord aContractType() {
		TContractTypesRecord ct = new TContractTypesRecordBuilder().build();
		Db.insert(ct);
		return ct;
	}

	public static TContractTypesRecord aContractTypeFixedTerm() {
		TContractTypesRecord ct = new TContractTypesRecordBuilder()
				.fixedTerm()
				.build();
		Db.insert(ct);
		return ct;
	}

	public static TContractTypesRecord aContractTypeWithContracts() {
		TContractTypesRecord ct = aContractType();
		aContractTerminatedForContractType(ct.id);
		aContractTerminatedForContractType(ct.id);
		return ct;
	}

	public static TContractsRecord aContractTerminatedForContractType(String cTypeId) {
		TContractsRecord c = new TContractsRecordBuilder()
				.forContractTypeId( cTypeId )
				.terminated()
				.build();
		Db.insert( c );
		return c;
	}

	public static TProfessionalGroupsRecord aProfessionalGroup() {
		return aProfessionalGroupOf( 50.0, 0.05 );
	}

	public static TProfessionalGroupsRecord aProfessionalGroupOf(
			double trienniumPay, double productivityRate) {
		
		TProfessionalGroupsRecord pg = new TProfessionalGroupsRecordBuilder()
				.withTrienniumPayment( trienniumPay )
				.withProductivityRate( productivityRate )
				.build();
		Db.insert(pg);
		return pg;
	}

	public static TProfessionalGroupsRecord aProfessionalGroupWithContracts() {
		TProfessionalGroupsRecord pg = aProfessionalGroup();
		aContractTerminatedForProfessionalGroup(pg.id);
		aContractTerminatedForProfessionalGroup(pg.id);
		return pg;
	}

	public static TContractsRecord aContractTerminatedForProfessionalGroup(String id) {
		TContractsRecord c = new TContractsRecordBuilder()
				.forProfessionalGroupId( id )
				.terminated()
				.build();
		Db.insert( c );
		return c;
	}

	public static TContractsRecord aContractWithAMechanic() {
		TMechanicsRecord m = aMechanic();
		return aContractInForceForMechanic(m.id);
	}

	public static TContractsRecord aContractWithPayrolls() {
		TContractsRecord c = aContractWithAMechanic();
		someRandomPayrollsForContract(c, 3);
		return c;
	}

	public static TPayrollsRecord aPayrollRandomForContractOfDate(
			String contractId,
			LocalDate date) {
		
		TPayrollsRecord p = new TPayrollsRecordBuilder()
				.forContractId( contractId )
				.withDate( Date.valueOf(date) )
				.build();
		Db.insert( p );
		return p;
	}

	public static TContractsRecord aContractWithInterventions() {
		TContractsRecord c = aContractWithAMechanic();
		anInterventionDuringContract( c );
		anInterventionDuringContract( c );
		return c;
	}

	public static TInterventionsRecord anInterventionDuringContract(TContractsRecord c) {
		Date endDate = c.endDate != null 
				? c.endDate 
				: new Date(System.currentTimeMillis());
		
		TInterventionsRecord i = new TInterventionsRecordBuilder()
				.forMechanicId( c.mechanic_Id )
				.withDate( Dates.rndTimestampBetween( c.startDate, endDate ) )
				.build();
		
		Db.insert( i );
		return i;
	}

	public static TContractsRecord anContractInForceFixedTerm() {
		TMechanicsRecord m = aMechanic();
		TContractTypesRecord ct = aContractTypeFixedTerm();
		TProfessionalGroupsRecord pg = aProfessionalGroup();
		TContractsRecord c = new TContractsRecordBuilder()
				.forMechanicId(m.id)
				.forContractTypeId(ct.id)
				.forProfessionalGroupId(pg.id)
				.withStartDate( Date.valueOf(LocalDate.now().minusMonths(6)) )
				.withEndDate( Date.valueOf(LocalDate.now().plusMonths(6)) )
				.inForce()
				.build();
		Db.insert(c);
		return c;
	}

	public static TContractsRecord aContractTerminatedWithMechanicTypeAndGroup() {
		TMechanicsRecord m = aMechanic();
		TContractTypesRecord ct = aContractType();
		TProfessionalGroupsRecord pg = aProfessionalGroup();
		TContractsRecord c = new TContractsRecordBuilder()
				.forMechanicId(m.id)
				.forContractTypeId(ct.id)
				.forProfessionalGroupId(pg.id)
				.withStartDate( Date.valueOf(LocalDate.now().minusMonths(6)) )
				.withEndDate( Date.valueOf(LocalDate.now()) )
				.terminated()
				.build();
		Db.insert(c);
		return c;
	}

	public static TContractsRecord aContractInForceWithMechanicTypeAndGroup() {
		TMechanicsRecord m = aMechanic();
		TContractTypesRecord ct = aContractType();
		TProfessionalGroupsRecord pg = aProfessionalGroup();
		TContractsRecord c = new TContractsRecordBuilder()
				.forMechanicId(m.id)
				.forContractTypeId(ct.id)
				.forProfessionalGroupId(pg.id)
				.withStartDate( Date.valueOf(LocalDate.now().minusMonths(6)) )
				.build();
		Db.insert(c);
		return c;
	}

    public static TContractsRecord aContractWithPayrollsForMechanicOf(
    		String mechanicId, int numPayrolls) {
    	
		TContractsRecord c = aContractInForceForMechanic(mechanicId);
		someRandomPayrollsForContract(c, numPayrolls);
		return c;
    }

    private static List<TPayrollsRecord> someRandomPayrollsForContract(
    		TContractsRecord c, 
    		int numPayrolls) {
    	
    	List<TPayrollsRecord> res = new ArrayList<>();
    	LocalDate date = Dates.lastDayOfMonth( c.startDate.toLocalDate());
		for ( int i = 0; i < numPayrolls; i++ ) {
			TPayrollsRecord p = aPayrollRandomForContractOfDate(c.id, date);
			res.add( p );
			
			date = date.plusMonths(1);
		}
		return res;
	}

	public static TContractsRecord aContractWithPayrollsTerminatedForMechanicOf(
    		String mechanicId, int numPayrolls) {
    	
		TContractsRecord c = aContractTerminatedForMechanic(mechanicId);
		someRandomPayrollsForContract(c, numPayrolls);
		return c;
    }

	public static TContractsRecord aContractInForceForMechanicOf(
			String mechanicId, 
			Date startDateOfContract,
			double compensationDaysPerYear) {

		TContractTypesRecord ct = aContractTypeOf( 
				compensationDaysPerYear );
		TProfessionalGroupsRecord pg = aProfessionalGroup();
		TContractsRecord c = new TContractsRecordBuilder()
				.forMechanicId(mechanicId)
				.forContractTypeId(ct.id)
				.forProfessionalGroupId(pg.id)
				.withStartDate( startDateOfContract )
				.inForce()
				.build();
		Db.insert(c);
		return c;
	}

	public static TContractTypesRecord aContractTypeOf(double compensationDays) {
		TContractTypesRecord ct = new TContractTypesRecordBuilder()
				.withCompensationDaysPerYear( compensationDays )
				.build();
		Db.insert(ct);
		return ct;
	}

	public static TContractsRecord aContractInForceForMechanicAndProfGroupOf(
			String mecanicId, 
			String professionalGroupId, 
			LocalDate startDate,
			double annualSalary, 
			double taxRate) {
		
		TContractTypesRecord ct = aContractType();
		TContractsRecord c = new TContractsRecordBuilder()
				.forMechanicId(mecanicId)
				.forContractTypeId(ct.id)
				.forProfessionalGroupId(professionalGroupId)
				.withStartDate( Date.valueOf(startDate) )
				.withAnnualBaseSalary( annualSalary )
				.withTaxRate( taxRate )
				.inForce()
				.build();
		Db.insert(c);
		return c;
	}

	public static List<TWorkOrdersRecord> someWorkOrdersInvoicedWithInterventionsForMechanicOf(
			String mechanicId, 
			double invoicedAmount,
			LocalDate firstDayOfLastMonth) {

		return List.of(
				aWorkOrderInvoicedWithInterventionForMechanicOf(
						mechanicId, 
						invoicedAmount / 3, 
						firstDayOfLastMonth.plusDays(1)
					),
				aWorkOrderInvoicedWithInterventionForMechanicOf(
						mechanicId, 
						invoicedAmount / 3, 
						firstDayOfLastMonth.plusDays(10)
					),
				aWorkOrderInvoicedWithInterventionForMechanicOf(
						mechanicId, 
						invoicedAmount / 3, 
						firstDayOfLastMonth.plusDays(20)
					)
			);		
	}

	public static TWorkOrdersRecord aWorkOrderInvoicedWithInterventionForMechanicOf(
			String mechanicId, 
			double amount,
			LocalDate date) {
		TVehiclesRecord v = aVehicleForClient( aClient().id );
		Timestamp invoiceDate = Timestamp.valueOf(date.atTime(12, 30));
		TWorkOrdersRecord wo = new TWorkOrdersRecordBuilder()
				.forVehicleId(v.id)
				.withAmount(amount)
				.withDate( invoiceDate )
				.invoiced()
				.build();
		Db.insert( wo );
		anInterventionForMechanicAndWorkOrder(mechanicId, wo.id, invoiceDate);
		return wo;
	}

	public static TInterventionsRecord anInterventionForMechanicAndWorkOrder(
			String mechanicId, String workOrderId, Timestamp invoiceDate) {

		TInterventionsRecord i = new TInterventionsRecordBuilder()
				.forMechanicId( mechanicId )
				.forWorkOrderId( workOrderId )
				.withMinutes( 60 )
				.withDate( invoiceDate )
				.build();		
		Db.insert( i );
		return i;
	}

	public static TContractsRecord aContractWithTypeAndGroupForMechanicOf(
			String mechanicId, 
			Date startDate, 
			Date endDate, 
			String state) {
		
		TContractTypesRecord ct = aContractType();
		TProfessionalGroupsRecord pg = aProfessionalGroup();
		TContractsRecord c = new TContractsRecordBuilder()
				.forMechanicId(mechanicId)
				.forContractTypeId(ct.id)
				.forProfessionalGroupId(pg.id)
				.withStartDate( startDate )
				.withEndDate( endDate )
				.withState( state )
				.build();
		Db.insert(c);
		return c;
	}

	public static TPayrollsRecord aPayrollWithContractAndMechanic() {
		TContractsRecord c = aContractWithAMechanic();
		return aPayrollForContract(c.id);
	}

	public static TPayrollsRecord aPayrollWithContractForMechanic(String mechanicId) {
		TContractsRecord c = aContractForMechanic( mechanicId );
		return aPayrollForContract(c.id);
	}

	public static TPayrollsRecord aPayrollForContract(String contractId) {
		TPayrollsRecord p = new TPayrollsRecordBuilder()
				.forContractId( contractId )
				.withDate( Dates.rndDateThisYear() )
				.randomizeSalariesAndDeductions()
				.build();
		Db.insert(p);
		return p;
	}

	public static TPayrollsRecord aPayrollWithContractForProfGroup(String pgId) {
		TMechanicsRecord mechanic = aMechanic();
		TContractsRecord c = aContractInForceForMechanicAndProfGroup(
				mechanic.id, 
				pgId
			);
		return aPayrollForContract(c.id);
	}

	public static TContractsRecord aContractInForceForMechanicAndProfGroup(
			String mechanicId, String professionalGroupgId) {
		TContractTypesRecord ct = aContractType();
		TContractsRecord c = new TContractsRecordBuilder()
				.forMechanicId(mechanicId)
				.forContractTypeId(ct.id)
				.forProfessionalGroupId(professionalGroupgId)
				.withStartDate( Date.valueOf(LocalDate.now().minusMonths(6)) )
				.inForce()
				.build();
		Db.insert(c);
		return c;
	}

	public static TContractsRecord aContractForProfessionalGroup(String pgId) {
		TMechanicsRecord mechanic = aMechanic();
		return aContractInForceForMechanicAndProfGroup(
				mechanic.id, 
				pgId
			);
	}

	public static List<TPayrollsRecord> somePayrollsWithContractsWithProfessionalGroup() {
		TProfessionalGroupsRecord pg = aProfessionalGroup();
		return somePayrollsWithContractsForProfessionalGroup(pg.id);
	}

	public static List<TPayrollsRecord> somePayrollsWithContractsForProfessionalGroup(
			String id) {
		TContractsRecord c1 = aContractForProfessionalGroup(id);
		TContractsRecord c2 = aContractForProfessionalGroup(id);
		return List.of(
				aPayrollForContract(c1.id),
				aPayrollForContract(c1.id),
				aPayrollForContract(c2.id),
				aPayrollForContract(c2.id)
			);
	}

	public static TPayrollsRecord aPayrollForContractAndMonth(
			String contractId, LocalDate lastMonth) {
		TPayrollsRecord p = new TPayrollsRecordBuilder()
				.forContractId( contractId )
				.withDate( Date.valueOf(lastMonth) )
				.randomizeSalariesAndDeductions()
				.build();
		Db.insert(p);
		return p;
	}

	public static List<TPayrollsRecord> somePayrollsWithContractAtDates(
			List<LocalDate> months) {
		List<TPayrollsRecord> res = new ArrayList<>();
		
		TContractsRecord c = aContractInForceWithMechanicTypeAndGroup();
		for ( LocalDate month : months ) {
			TPayrollsRecord p = aPayrollForContractAndMonth(c.id, month);
			res.add(p);
		}
		
		return res;
	}

	public static List<TPayrollsRecord> somelPayrollsForCurrentMonth() {
		LocalDate lastDayOfPreviousMonth = Dates.lastDayOfMonth(LocalDate.now().minusMonths(1));
		
		List<TPayrollsRecord> res = new ArrayList<>();
		int numberOfPayrolls = Random.inRange(3, 10);
		
		for ( int i = 0; i < numberOfPayrolls; i++ ) {
			TContractsRecord c = aContractInForceWithMechanicTypeAndGroup();
			TPayrollsRecord p = aPayrollForContractAndMonth(c.id, lastDayOfPreviousMonth);
			res.add(p);
		}
		
		return res;
	}

	public static List<TMechanicsRecord> someMechanicsWithNoInforceContracts() {
		return List.of(
				aMechanicWithTerminatedContract(),
				aMechanicWithTerminatedContract(),
				aMechanicWithTerminatedContract()
			);
	}

	public static List<TMechanicsRecord> someMechanicsWithInforceContracts() {
		return List.of(
				aMechanicWithInForceContract(),
				aMechanicWithInForceContract(),
				aMechanicWithInForceContract()
			);
	}

}