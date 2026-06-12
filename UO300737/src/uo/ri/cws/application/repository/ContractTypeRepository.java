package uo.ri.cws.application.repository;

import java.util.List;
import java.util.Optional;

import uo.ri.cws.domain.ContractType;

public interface ContractTypeRepository extends Repository<ContractType>{

	/**
	 * @param name
	 * @return the contract type object 
	 */
	Optional<ContractType> findByName(String name);

	/**
	 * @return the contract types 
	 */
	List<ContractType> findAll();
}
