package uo.ri.cws.application.service.contract.crud;

import java.util.List;
import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.contract.ContractCrudService;
import uo.ri.cws.application.service.contract.crud.command.AddContract;
import uo.ri.cws.application.service.contract.crud.command.DeleteContract;
import uo.ri.cws.application.service.contract.crud.command.FindAllContracts;
import uo.ri.cws.application.service.contract.crud.command.FindContractById;
import uo.ri.cws.application.service.contract.crud.command.FindContractsByMechanicNif;
import uo.ri.cws.application.service.contract.crud.command.FindInForceContracts;
import uo.ri.cws.application.service.contract.crud.command.TerminateContract;
import uo.ri.cws.application.service.contract.crud.command.UpdateContract;
import uo.ri.cws.application.util.command.CommandExecutor;
import uo.ri.util.exception.BusinessException;

public class ContractCrudServiceImpl implements ContractCrudService {

    private CommandExecutor executor = Factories.executor.forExecutor();

    @Override
    public ContractDto create(ContractDto dto) throws BusinessException {
        return executor.execute(new AddContract(dto));
    }

    @Override
    public void update(ContractDto dto) throws BusinessException {
        executor.execute(new UpdateContract(dto));
    }

    @Override
    public void delete(String id) throws BusinessException {
        executor.execute(new DeleteContract(id));
    }

    @Override
    public void terminate(String contractId) throws BusinessException {
        executor.execute(new TerminateContract(contractId));
    }

    @Override
    public Optional<ContractDto> findById(String id) throws BusinessException {
        return executor.execute(new FindContractById(id));
    }

    @Override
    public List<ContractSummaryDto> findByMechanicNif(String nif)
            throws BusinessException {
        return executor.execute(new FindContractsByMechanicNif(nif));
    }

    @Override
    public List<ContractDto> findInforceContracts() throws BusinessException {
        return executor.execute(new FindInForceContracts());
    }

    @Override
    public List<ContractSummaryDto> findAll() throws BusinessException {
        return executor.execute(new FindAllContracts());
    }

}