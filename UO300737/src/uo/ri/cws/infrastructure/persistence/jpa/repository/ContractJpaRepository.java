package uo.ri.cws.infrastructure.persistence.jpa.repository;

import java.time.LocalDate;
import java.util.List;

import uo.ri.cws.application.repository.ContractRepository;
import uo.ri.cws.domain.Contract;
import uo.ri.cws.infrastructure.persistence.jpa.util.BaseJpaRepository;
import uo.ri.cws.infrastructure.persistence.jpa.util.Jpa;

public class ContractJpaRepository 
	extends BaseJpaRepository<Contract> 
	implements ContractRepository {

	@Override
	public List<Contract> findAll() {
	    return Jpa.getManager()
	        .createNamedQuery(
	        		"Contract.findAll", 
	        		Contract.class)
	        .getResultList();
	}

	@Override
	public List<Contract> findAllInForce() {
	    return Jpa.getManager()
	        .createNamedQuery(
	        		"Contract.findAllInForce", 
	        		Contract.class)
	        .setParameter("state", Contract.ContractState.IN_FORCE)
	        .getResultList();
	}

	public List<Contract> findEligibleForPayrollGeneration() {
	    return Jpa.getManager()
	        .createNamedQuery(
	        		"Contract.findEligibleForPayrollGeneration", 
	        		Contract.class)
	        .setParameter("state", Contract.ContractState.IN_FORCE)
	        .getResultList();
	}
	
  @Override
    public List<Contract> findByMechanicId(String mechanicId) {
        return Jpa.getManager()
                .createNamedQuery("Contract.findByMechanicId", Contract.class)
                .setParameter("mid", mechanicId)
                .getResultList();
    }

    @Override
    public List<Contract> findByProfessionalGroupId(String id) {
        return Jpa.getManager()
                .createNamedQuery("Contract.findByProfessionalGroupId", Contract.class)
                .setParameter("gid", id)
                .getResultList();
    }

    @Override
    public List<Contract> findByContractTypeId(String id) {
        return Jpa.getManager()
                .createNamedQuery("Contract.findByContractTypeId", Contract.class)
                .setParameter("tid", id)
                .getResultList();
    }

    @Override
    public List<Contract> findAllInForceThisMonth(LocalDate present) {
        return findAllInForce(); // filtered in domain if needed
    }

    @Override
    public List<Contract> findInforceContracts() {
        return findAllInForce();
    }

}
