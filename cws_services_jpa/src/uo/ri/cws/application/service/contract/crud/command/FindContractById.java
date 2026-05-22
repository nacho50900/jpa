package uo.ri.cws.application.service.contract.crud.command;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.ContractRepository;
import uo.ri.cws.application.service.contract.ContractCrudService.ContractDto;
import uo.ri.cws.application.service.contract.crud.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessException;

public class FindContractById implements Command<Optional<ContractDto>> {

    private String id;
    private ContractRepository repo = Factories.repository.forContract();

    public FindContractById(String id) {
        ArgumentChecks.isNotBlank(id, "ID cannot be blank");
        this.id = id;
    }

    @Override
    public Optional<ContractDto> execute() throws BusinessException {
        return repo.findById(id).map(DtoAssembler::toDto);
    }

}