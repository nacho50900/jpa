package uo.ri.cws.application.service.contracttype.crud.command;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.ContractTypeRepository;
import uo.ri.cws.application.service.contracttype.ContractTypeCrudService.ContractTypeDto;
import uo.ri.cws.application.service.contracttype.crud.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessException;

public class FindContractTypeById implements Command<Optional<ContractTypeDto>> {

    private String id;
    private ContractTypeRepository repo = Factories.repository.forContractType();

    public FindContractTypeById(String id) {
        ArgumentChecks.isNotBlank(id, "ID cannot be blank");
        this.id = id;
    }

    @Override
    public Optional<ContractTypeDto> execute() throws BusinessException {
        return repo.findById(id).map(DtoAssembler::toDto);
    }

}