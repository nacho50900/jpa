package uo.ri.cws.application.service.contracttype.crud;

import java.util.List;
import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.contracttype.ContractTypeCrudService;
import uo.ri.cws.application.service.contracttype.crud.command.AddContractType;
import uo.ri.cws.application.service.contracttype.crud.command.DeleteContractType;
import uo.ri.cws.application.service.contracttype.crud.command.FindAllContractTypes;
import uo.ri.cws.application.service.contracttype.crud.command.FindContractTypeById;
import uo.ri.cws.application.service.contracttype.crud.command.FindContractTypeByName;
import uo.ri.cws.application.service.contracttype.crud.command.GetContractTypeSummary;
import uo.ri.cws.application.service.contracttype.crud.command.UpdateContractType;
import uo.ri.cws.application.util.command.CommandExecutor;
import uo.ri.util.exception.BusinessException;

public class ContractTypeCrudServiceImpl implements ContractTypeCrudService {

    private CommandExecutor executor = Factories.executor.forExecutor();

    @Override
    public ContractTypeDto create(ContractTypeDto dto) throws BusinessException {
        return executor.execute(new AddContractType(dto));
    }

    @Override
    public void update(ContractTypeDto dto) throws BusinessException {
        executor.execute(new UpdateContractType(dto));
    }

    @Override
    public void delete(String id) throws BusinessException {
        executor.execute(new DeleteContractType(id));
    }

    @Override
    public Optional<ContractTypeDto> findByName(String name) throws BusinessException {
        return executor.execute(new FindContractTypeByName(name));
    }

    @Override
    public List<ContractTypeDto> findAll() throws BusinessException {
        return executor.execute(new FindAllContractTypes());
    }

    public Optional<ContractTypeDto> findById(String id) throws BusinessException {
        return executor.execute(new FindContractTypeById(id));
    }
    
    public ContractTypeDto getSummaryByName(String name) throws BusinessException {
        return executor.execute(new GetContractTypeSummary(name));
    }

}