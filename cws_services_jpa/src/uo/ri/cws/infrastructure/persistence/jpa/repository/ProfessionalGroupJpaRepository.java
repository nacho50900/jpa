package uo.ri.cws.infrastructure.persistence.jpa.repository;

import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.repository.ProfessionalGroupRepository;
import uo.ri.cws.domain.ProfessionalGroup;
import uo.ri.cws.infrastructure.persistence.jpa.util.BaseJpaRepository;
import uo.ri.cws.infrastructure.persistence.jpa.util.Jpa;

public class ProfessionalGroupJpaRepository 
		extends BaseJpaRepository<ProfessionalGroup>
		implements ProfessionalGroupRepository {
	
	@Override
	public Optional<ProfessionalGroup> findByName(String name) {
	    return Jpa.getManager()
	        .createNamedQuery(
	        		"ProfessionalGroup.findByName", 
	        		ProfessionalGroup.class
	        )
	        .setParameter("name", name)
	        .getResultStream()
	        .findFirst();
	}

	@Override
	public List<ProfessionalGroup> findAll() {
	    return Jpa.getManager().
	    		createNamedQuery(
	    			"ProfessionalGroup.findAll", 
	    			ProfessionalGroup.class)
	            .getResultList();
	}
	
}
