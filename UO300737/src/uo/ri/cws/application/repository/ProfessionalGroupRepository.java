package uo.ri.cws.application.repository;

import java.util.List;
import java.util.Optional;

import uo.ri.cws.domain.ProfessionalGroup;

public interface ProfessionalGroupRepository extends Repository<ProfessionalGroup>{

	/**
	 * @param name
	 * @return the professional group
	 */
	Optional<ProfessionalGroup> findByName(String name);
	
	/**
	 * @return all the professional groups
	 */
	List<ProfessionalGroup> findAll( );

}
