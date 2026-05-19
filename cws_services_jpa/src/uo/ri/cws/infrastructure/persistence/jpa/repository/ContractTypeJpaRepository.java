package uo.ri.cws.infrastructure.persistence.jpa.repository;

import java.util.Optional;

import uo.ri.cws.application.repository.ContractTypeRepository;
import uo.ri.cws.domain.ContractType;
import uo.ri.cws.infrastructure.persistence.jpa.util.BaseJpaRepository;

@SuppressWarnings("unused")
public class ContractTypeJpaRepository 
		extends BaseJpaRepository<ContractType> 
		implements ContractTypeRepository {

	@Override
	public Optional<ContractType> findByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

}
