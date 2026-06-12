package uo.ri.cws.application.service.contracttype.crud.command;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.ContractTypeRepository;
import uo.ri.cws.application.service.contracttype.ContractTypeCrudService.ContractTypeDto;
import uo.ri.cws.application.service.contracttype.crud.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.ContractType;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class AddContractType implements Command<ContractTypeDto> {

    private ContractTypeDto dto;
    private ContractTypeRepository repo = Factories.repository.forContractType();

    public AddContractType(ContractTypeDto dto) {
        ArgumentChecks.isNotNull(dto, "ContractTypeDto cannot be null");
        ArgumentChecks.isNotBlank(dto.name, "Name cannot be blank");
        ArgumentChecks.isTrue(dto.compensationDays >= 0,
                "Compensation days must be >= 0");
        this.dto = dto;
    }

    @Override
    public ContractTypeDto execute() throws BusinessException {
        Optional<ContractType> existing = repo.findByName(dto.name);
        BusinessChecks.doesNotExist(existing,
                "Contract type already exists: " + dto.name);

        ContractType ct = new ContractType(dto.name, dto.compensationDays);
        repo.add(ct);
        return DtoAssembler.toDto(ct);
    }

}