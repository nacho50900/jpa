package uo.ri.cws.application.service.contract.terminate;

import java.sql.Date;
import java.time.LocalDate;

import uo.ri.cws.application.service.util.dbfixture.Db;
import uo.ri.cws.application.service.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.util.dbfixture.builders.TPayrollsRecordBuilder;
import uo.ri.cws.application.service.util.dbfixture.records.TContractsRecord;
import uo.ri.cws.application.service.util.dbfixture.records.TPayrollsRecord;
import uo.ri.util.date.Dates;

public class ContractFixtures {

	public static TContractsRecord aContractForMechanicWithFixedPayrolls(
			Date startDateOfContract, 
			String mechanicId, 
			int numPayrolls, 
			double gossSalaryPerPayroll, 
			double compensationDaysPerYear) {
		
		TContractsRecord c = DbFixtures.aContractInForceForMechanicOf(
					mechanicId, startDateOfContract, compensationDaysPerYear
				);
	
		LocalDate date = Dates.lastDayOfMonth( startDateOfContract.toLocalDate() );
		for ( int i = 0; i < numPayrolls; i++ ) {
			ContractFixtures.aFixedPayrollForContractWith(c.id, date, gossSalaryPerPayroll);
			date = date.plusMonths(1);
		}
		return c;
	}

	public static TPayrollsRecord aFixedPayrollForContractWith(
			String contractId, LocalDate date, double grossSalaryPerPayroll) {
		
		double[] amounts = { 
				0.70 * grossSalaryPerPayroll,  // base salary
				0.00, 						   // extra salary
				0.20 * grossSalaryPerPayroll,  // productivity earning
				0.10 * grossSalaryPerPayroll,  // triennium earning
				0.15 * grossSalaryPerPayroll,  // tax deduction
				0.10 * grossSalaryPerPayroll   // nic deduction
			};
		
		int i = 0;
		TPayrollsRecord p = new TPayrollsRecordBuilder()
				.forContractId( contractId )
				.withDate( Date.valueOf( date ) )
				.withBaseSalary( amounts[i++] )
				.withExtraSalary( amounts[i++] )
				.withProductivityEarning( amounts[i++] )
				.withTrienniumEarning( amounts[i++])
				.withTaxDeduction( amounts[i++] )
				.withNicDeduction( amounts[i++])
				.build();
		Db.insert( p );
		return p;
	}

}
