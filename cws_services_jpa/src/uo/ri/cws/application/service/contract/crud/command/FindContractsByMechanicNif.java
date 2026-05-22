package uo.ri.cws.application.service.contract.crud.command;

import java.util.List;
import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.ContractRepository;
import uo.ri.cws.application.repository.MechanicRepository;
import uo.ri.cws.application.service.contract.ContractCrudService.ContractSummaryDto;
import uo.ri.cws.application.service.contract.crud.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Contract;
import uo.ri.cws.domain.Mechanic;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class FindContractsByMechanicNif implements Command<List<ContractSummaryDto>> {

    private String nif;
    private MechanicRepository mechanicRepo = Factories.repository.forMechanic();
    private ContractRepository contractRepo = Factories.repository.forContract();

    public FindContractsByMechanicNif(String nif) {
        ArgumentChecks.isNotBlank(nif, "NIF cannot be blank");
        this.nif = nif;
    }

    @Override
    public List<ContractSummaryDto> execute() throws BusinessException {
        Optional<Mechanic> om = mechanicRepo.findByNif(nif);
        BusinessChecks.exists(om, "Mechanic not found: " + nif);
        List<Contract> contracts = contractRepo.findByMechanicId(om.get().getId());
        return DtoAssembler.toContractSummaryDtoList(contracts);
    }

}