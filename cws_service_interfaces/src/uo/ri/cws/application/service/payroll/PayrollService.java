package uo.ri.cws.application.service.payroll;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import uo.ri.util.exception.BusinessException;

public interface PayrollService {
	
    /**
     * Generates all payrolls at last day of the previous month
     * Note: Notice next method and be careful to not repeat code.
     * @return a list of payrolls generated
     * 
     * @throws {@link BusinessException}
     */
    List<PayrollDto> generateForPreviousMonth() throws BusinessException;

    /**
     * Generates all payrolls at last day of the previous month of the one 
     * passed as argument
     * @param arg Payroll will be generated considering this as present day
     * @return a list of payrolls generated
     *
     * @throws {@link BusinessException}
     */
    List<PayrollDto> generateForPreviousMonthOf(LocalDate present) throws BusinessException;
    
    /**
     * Deletes the payroll generated last month for the mechanic passed as argument
     *
     * @param mechanic identifier
     * @throws {@link BusinessException} if there are no mechanic with that id
     */
    void deleteLastGeneratedOfMechanicId(String mechanicId) throws BusinessException;

    /**
     * Deletes all payrolls generated for last month.
     * @return number of payrolls deleted
     * @throws DOES NOT BusinessException
     */
    int deleteLastGenerated() throws BusinessException;

    /**
     * Returns one payroll details.
     *
     * @param id payroll id
     * @return payroll dto 
     * @throws IllegalArgumentException when argument is null or empty
     */
    Optional<PayrollDto> findById(String id) throws BusinessException;

    /**
     * Returns all payrolls (a summary).
     *
     * @return List of all payrolls summary, or empty.
     * @throws {@link BusinessException}
     */
    List<PayrollSummaryDto> findAllSummarized() throws BusinessException;

    /**
     * Returns all payrolls (a summary) for a certain mechanic.
     *
     * @param id mechanic id
     * @return List of payrolls summary, or empty.
     * @throws IllegalArgument Exception when argument is null or empty
     * @throws BusinessException when mechanic does not exist
     */
    List<PayrollSummaryDto> findSummarizedByMechanicId(String id) 
    		throws BusinessException;

    /**
     * Returns all payrolls (a summary) for a professional group
     * 
     * @param name the name of the professional group
     * @throws BussinessException DOES NOT
     * @throws IllegalArgumentException when argument is null or empty.
     */
    List<PayrollSummaryDto> findSummarizedByProfessionalGroupName(String name) 
    		throws BusinessException;

    public class PayrollDto {

    	public String id;
    	public long version;
    	
    	public String contractId;
    	public LocalDate date;
    	
    	// Earnings
    	public double baseSalary;
    	public double extraSalary;
    	public double productivityEarning;
    	public double trienniumEarning;
    	
    	// Deductions
    	public double taxDeduction;
    	public double nicDeduction;
    	
    	// Net wage
    	public double netSalary;
		public double grossSalary;
		public double totalDeductions;
    }

    public class PayrollSummaryDto {

    	public String id;
    	
    	public LocalDate date;
    	public double netSalary;
    }
}
