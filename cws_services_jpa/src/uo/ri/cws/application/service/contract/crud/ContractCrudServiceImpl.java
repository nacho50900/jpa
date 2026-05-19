package uo.ri.cws.application.service.contract.crud;

import java.util.List;
import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.contract.ContractCrudService;
import uo.ri.cws.application.service.contract.crud.command.FindInForceContracts;
import uo.ri.cws.application.util.command.CommandExecutor;
import uo.ri.util.exception.BusinessException;

public class ContractCrudServiceImpl implements ContractCrudService {
	
	private CommandExecutor executor = Factories.executor.forExecutor();

	@Override
	public ContractDto create(ContractDto c) throws BusinessException {
		throw new UnsupportedOperationException("Not from this user case");
	}

	@Override
	public void update(ContractDto dto) throws BusinessException {
		throw new UnsupportedOperationException("Not from this user case");
	}

	@Override
	public void delete(String id) throws BusinessException {
		throw new UnsupportedOperationException("Not from this user case");
	}

	@Override
	public void terminate(String contractId) throws BusinessException {
		throw new UnsupportedOperationException("Not from this user case");
	}

	@Override
	public Optional<ContractDto> findById(String id) throws BusinessException {
		throw new UnsupportedOperationException("Not from this user case");
	}

	@Override
	public List<ContractSummaryDto> findByMechanicNif(String nif) throws BusinessException {
		throw new UnsupportedOperationException("Not from this user case");
	}

	@Override
	public List<ContractDto> findInforceContracts() throws BusinessException {
		return executor.execute( new FindInForceContracts() );
	}

	@Override
	public List<ContractSummaryDto> findAll() throws BusinessException {
		throw new UnsupportedOperationException("Not from this user case");
	}

	/*
	public List<ContractDto> findEligibleForPayrollGeneration(LocalDate month) 
		throws BusinessException {
		return executor.execute( new FindEligibleForPayrollGeneration(month) );
	}*/

}