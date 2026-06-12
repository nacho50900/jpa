package uo.ri.cws.application.service.contract.crud.command;

import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.ContractRepository;
import uo.ri.cws.application.service.contract.ContractCrudService.ContractDto;
import uo.ri.cws.application.service.contract.crud.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Contract;
import uo.ri.util.exception.BusinessException;

public class FindInForceContracts implements Command<List<ContractDto>>{
	
	private ContractRepository contractRepo = Factories.repository.forContract();
	
	@Override
	public List<ContractDto> execute() throws BusinessException {
		List<Contract> contracts = contractRepo.findAllInForce();
		
		return DtoAssembler.toDtoList(contracts);
	}

}
