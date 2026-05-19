package uo.ri.cws.application.service.payroll.generate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.util.List;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.payroll.PayrollService;
import uo.ri.cws.application.service.payroll.PayrollService.PayrollDto;
import uo.ri.cws.application.service.util.Asserts;
import uo.ri.cws.application.service.util.TaxRate;
import uo.ri.cws.application.service.util.dbfixture.Db;
import uo.ri.cws.application.service.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.util.dbfixture.records.TContractsRecord;
import uo.ri.cws.application.service.util.dbfixture.records.TMechanicsRecord;
import uo.ri.cws.application.service.util.dbfixture.records.TPayrollsRecord;
import uo.ri.cws.application.service.util.dbfixture.records.TProfessionalGroupsRecord;
import uo.ri.util.date.Dates;
import uo.ri.util.exception.BusinessException;

/**
 * Feature: Generate payrolls for mechanics last month
 * Scenario Outline: [P.G.3] Generate payrolls for a mechanic in force last month
 */
public class ScenarioPG3 {
    private PayrollService service = Factories.service.forPayrollService();
    private TMechanicsRecord mechanic;
    private TContractsRecord contract;
    private TProfessionalGroupsRecord professionalGroup;
    private LocalDate today;
    
	private List<PayrollDto> generated;

    @Given("[P.G.3] today is {string}")
    public void givenTodayIs(String todayStr) {
        today = Dates.parseToLocalDate(todayStr);
    }

    @Given("[P.G.3] a professional group with triennium pay of {double} and productivity rate of {double}")
    public void givenProfessionalGroupWithTrienniumPayAndProductivityRate(
    		double trienniumPay, double productivityRate) {
    	
        professionalGroup = DbFixtures.aProfessionalGroupOf( 
        			trienniumPay, 
        			productivityRate 
        		);
    }

    @Given("[P.G.3] a contract in-force with {string} date, {double} salary and its corresponding tax rate")
    public void givenContractInForceWithStartDateBaseSalaryAndTaxRate(
    		String startDateStr, 
    		double annualSalary) {
    	
        LocalDate startDate = Dates.parseToLocalDate(startDateStr);
        double taxRate = TaxRate.forSalary( annualSalary );
                
        mechanic = DbFixtures.aMechanic();
        contract = DbFixtures.aContractInForceForMechanicAndProfGroupOf(
        		mechanic.id, 
        		professionalGroup.id, 
        		startDate, 
        		annualSalary,
        		taxRate
        	);
    }

    @Given("[P.G.3] having invoiced {double} euros in workorders in the month prior")
    public void givenHavingInvoicedEurosInWorkordersInMonthPrior(double invoicedAmount) {
    	
        if (invoicedAmount <= 0) {
			return; // No invoicing, no productivity pay
		}

        LocalDate firstDayOfLastMonth = today.minusMonths(1).withDayOfMonth(1);
        DbFixtures.someWorkOrdersInvoicedWithInterventionsForMechanicOf(
        		mechanic.id, 
        		invoicedAmount, 
        		firstDayOfLastMonth
        	);
    }

    @When("[P.G.3] I generate payrolls")
    public void whenIGeneratePayrolls() throws BusinessException {
        generated = service.generateForPreviousMonthOf( today );
    }

    @Then("[P.G.3] one is generated with {string}, {double}, {double}, {double}, {double}, {double}, {double}")
    public void thenOneIsGeneratedWith(
    		String payrollDate,
    		double monthly, 
    		double extra, 
    		double prod, 
    		double tri, 
    		double tax, 
    		double nic) {
    	
    	// Expected values calculation
        LocalDate date = Dates.parseToLocalDate(payrollDate);
        double grossSalary = monthly + extra + prod + tri;
		double totalDeductions = tax + nic;
		double netSalary = grossSalary - totalDeductions;
        
    	// Checks on the returned object
        assertEquals(1, generated.size());
        PayrollDto dto = generated.get(0);
        
        assertEquals(1, dto.version);
        assertNotNull(dto.id);
        assertFalse(dto.id.isBlank());
        assertEquals(contract.id, dto.contractId);
        assertEquals(date, dto.date);
        assertEquals(monthly, dto.baseSalary, 0.01);
        assertEquals(extra, dto.extraSalary, 0.01);
        assertEquals(prod, dto.productivityEarning, 0.01);
        assertEquals(tri, dto.trienniumEarning, 0.01);
        assertEquals(tax, dto.taxDeduction, 0.01);
        assertEquals(nic, dto.nicDeduction, 0.01);
        assertEquals(grossSalary, dto.grossSalary, 0.01);
        assertEquals(totalDeductions, dto.totalDeductions, 0.01);
        assertEquals(netSalary, dto.netSalary, 0.01);

        // Checks on the database
    	List<TPayrollsRecord> loaded = Db.findAllBy(TPayrollsRecord.class, 
    			"contract_Id", contract.id
    		);
		assertEquals(1, loaded.size());
		TPayrollsRecord generated = loaded.get(0);
		
		assertEquals(date, generated.date.toLocalDate());
        assertEquals(monthly, generated.baseSalary, 0.01);
        assertEquals(extra, generated.extraSalary, 0.01);
        assertEquals(prod, generated.productivityEarning, 0.01);
        assertEquals(tri, generated.trienniumEarning, 0.01);
        assertEquals(tax, generated.taxDeduction, 0.01);
        assertEquals(nic, generated.nicDeduction, 0.01);
        
		Asserts.isNow( generated.createdAt );
		Asserts.isNow( generated.updatedAt );
		assertEquals(dto.version, generated.version);
    }
}