package uo.ri.cws.application.service.contracttype.crud.command;

import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.ContractTypeRepository;
import uo.ri.cws.application.service.contracttype.ContractTypeCrudService.ContractTypeDto;
import uo.ri.cws.application.service.contracttype.crud.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.util.exception.BusinessException;

public class FindAllContractTypes implements Command<List<ContractTypeDto>> {

    private ContractTypeRepository repo = Factories.repository.forContractType();

    @Override
    public List<ContractTypeDto> execute() throws BusinessException {
        return DtoAssembler.toDtoList(repo.findAll());
    }

}