package uo.ri.cws.application.service.professionalgroup;

import java.util.List;
import java.util.Optional;

import uo.ri.util.exception.BusinessException;

public interface ProfessionalGroupCrudService {


	/**
	 * Add a new professional group with the given data
	 * 
	 * @param dto, the id value, if any, will be ignored
	 * @return a dto filled with id and version
	 * @throws IllegalArgumentException when
	 * 		- dto is null, or
	 * 		- name is null or empty, or
	 * 		- triennium salary is negative, or
	 * 		- productivity plus is negative
	 * @throws BusinessException if:
	 * 		- another category with the same name already exists
	 */
	ProfessionalGroupDto create(ProfessionalGroupDto dto) throws BusinessException;
	
	/**
	 * Removes the given category
	 * @param name of the category
	 * @throws IllegalArgumentException when
	 * 		- name is null or empty
	 * @throws BusinessException if:
	 * 		- the group does not exist, or
	 * 		- the group has contracts assigned
	 */
	void delete(String name) throws BusinessException;

	/**
	 * Updates only the productivityPlus and the trienniumSalary fields 
	 * @param dto, only name, trienniumSalary and productivitySalary fields 
	 * 	are useful for this operation, the rest will be ignored
	 * @throws IllegalArgumentException when
	 * 		- arg is null, or
	 * 		- name is null or empty
	 * 		- the new trienniumSalary is negative, or
	 * 		- the new productivitySalary is negative
	 * @throws BusinessException if:
	 * 		- the group does not exist
	 */
	void update(ProfessionalGroupDto dto) throws BusinessException;
	
	/**
	 * @param name, of the category
	 * @return the contract category dto, probably empty
	 * @throws IllegalArgumentException when
	 * 		- id is null or empty
	 * @throws BusinessException DOES NOT
	 */
	Optional<ProfessionalGroupDto> findByName(String id) throws BusinessException;

	/**
	 * @return the list of all categories, or 
	 * 		an empty list if there is none
	 * @throws BusinessException DOES NOT
	 */
	List<ProfessionalGroupDto> findAll() throws BusinessException;

	public class ProfessionalGroupDto {
		public String id;
		public long version;
		
		public String name;
		public double trienniumPayment;
		public double productivityRate;
	}

}
