package uo.ri.cws.application.service.contract.crud.command;

import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.ContractRepository;
import uo.ri.cws.application.service.contract.ContractCrudService.ContractSummaryDto;
import uo.ri.cws.application.service.contract.crud.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.util.exception.BusinessException;

public class FindAllContracts implements Command<List<ContractSummaryDto>> {

    private ContractRepository repo = Factories.repository.forContract();

    @Override
    public List<ContractSummaryDto> execute() throws BusinessException {
        return DtoAssembler.toContractSummaryDtoList(repo.findAll());
    }

}