package uo.ri.cws.application.service.contract;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import uo.ri.util.exception.BusinessException;

public interface ContractCrudService {

	/**
	 * Creates a new contract for a given mechanic. 
	 * If there already exists an earlier contract for the same mechanic, 
	 * and that is in force, it will be marked as terminated and the settlement 
	 * will be computed. Contract start date will be the first day of the next month.
	 *  
	 * @param c, contract business DTO. Consider only fields mechanic dni, type name,
	 * professionalGroup name, startDate, endDate (if not null) and annualSalary. 
	 * Others will be ignored.
	 * 
	 * @return a fully filled DTO, including its secondary DTO.
	 * 
	 * @throws BusinessException when:
	 * 		- Contract type does not exist, or 
	 * 		- mechanic does not exist, or 
	 * 		- professional group does not exist, or
	 * 		- end date, when no null, is not later than start date.
	 * @throws IllegalArgumentException when:
	 * 		- argument is null, or
	 * 		- one of the mandatory values is null or empty String, or
	 * 		- professional group is FIXED_TERM and end date is null, or
	 * 		- annualBaseWage is not greater than 0
	 */
	ContractDto create(ContractDto c) throws BusinessException;
	
	/**
	 * It updates a contract that must be in force.
	 * Only three of the fields in the argument will be considered: id to identify 
	 * the contract to update and endDate (if not null in the argument) and 
	 * annualBaseWage, to update values stored. 
	 * 
	 * If endDate provided is not null, it must be a valid future date and will 
	 * be updated. If it is null, then contract endDate will be set to null.
	 * 
	 * @param dto, just id, endDate and annualBaseWage are mandatory. Other 
	 * fields in the argument will be ignored.
	 * 
	 * @throws BusinessException when:
	 * 		- The contract does not exist, or
	 * 		- the contract is no longer in force, or
	 * 		- contract type is FIXED_TERM and end date is earlier than startDate, or
	 * 		- the contract is no longer in force.
	 * @throws IllegalArgumentException when:
	 * 		- arg is null, or
 	 * 		- id is null or empty, or
	 * 		- the annualBaseWage is a negative value.
	 */
	void update(ContractDto dto) throws BusinessException;
	
	/**
	 * It deletes the contract, if possible.
	 * 
	 * @param id Contract identifier
	 * @throws BusinessException when:
	 * 		- The contract does not exist, or
	 * 		- mechanic has workorders, or
	 * 		- there are payrolls for this contract.
	 * @throws IllegalArgumentException when
 	 * 		- id is null or empty.
	 */
	void delete(String id) throws BusinessException;
	
	/**
	 * It changes contract to TERMINATED and calculates settlement according 
	 * the rules described in the problem statement document.
	 * 
	 * @param contractId, 
	 * @throws BusinessException when, 
	 * 		- The contract does not exist, or
	 * 		- the contract is not in force, or
	 * @throws IllegalArgumentException when
 	 * 		- id is null or empty, or
	 */
	void terminate(String contractId) throws BusinessException;

	/**
	 * @param contract id, 
	 * @return the contract with the given id, if it exists
	 * @throws BusinessException DOES NOT
	 * @throws IllegalArgumentException when id is null or empty.
	 */
	Optional<ContractDto> findById(String id) throws BusinessException;
	
	/**
	 * @param mechanic id 
	 * @return a list with every Contract registered for a mechanic, probably empty
	 * @throws BusinessException DOES NOT
	 * @throws IllegalArgumentException when id is null or empty. 
	 */
	List<ContractSummaryDto> findByMechanicNif(String nif) throws BusinessException;

	/**
	 * @return a list with every Contract_BLDto in force, probably empty
	 * @throws BusinessException DOES NOT
	 */
	List<ContractDto> findInforceContracts() throws BusinessException;

	/**
	 * @return a list with every Contract registered, probably empty
	 * @throws BusinessException DOES NOT
	 * @throws IllegalArgumentException when id is null or empty. 
	 */
	List<ContractSummaryDto> findAll( ) throws BusinessException;
	
	public class ContractDto {

		public String id;
		public long version;
		
		public MechanicOfContractDto mechanic = new MechanicOfContractDto();
		public ContractTypeOfContractDto contractType = new ContractTypeOfContractDto();
		public ProfessionalGroupOfContractDto professionalGroup = new ProfessionalGroupOfContractDto();
		
		public LocalDate startDate;
		public LocalDate endDate;
		public double annualBaseSalary;
		public double taxRate;

		// Filled in reading operations
		public double settlement;
		public String state;
	}
	
	public class ContractTypeOfContractDto {
		public String id;

		public String name;
		public double compensationDaysPerYear;	
	}
	
	public class MechanicOfContractDto {
		public String id;

		// Filled in reading operations
		public String nif;
		public String name;
		public String surname;
	}
	
	public class ProfessionalGroupOfContractDto {
		public String id;
		
		// Filled in reading operations
		public String name;
		public double trieniumPayment;
		public double productivityRate;	
	}
	
	public class ContractSummaryDto {
		public String id;
		
		public String nif;

		// Filled in reading operations
		public double settlement;
		public String state;
		public int numPayrolls;
	}

}