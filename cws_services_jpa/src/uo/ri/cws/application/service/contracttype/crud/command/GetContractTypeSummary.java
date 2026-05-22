package uo.ri.cws.application.service.contracttype.crud.command;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.ContractTypeRepository;
import uo.ri.cws.application.service.contracttype.ContractTypeCrudService.ContractTypeDto;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.ContractType;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class GetContractTypeSummary implements Command<ContractTypeDto> {

    private String name;
    private ContractTypeRepository repo = Factories.repository.forContractType();

    public GetContractTypeSummary(String name) {
        ArgumentChecks.isNotBlank(name, "Name cannot be blank");
        this.name = name;
    }

    @Override
    public ContractTypeDto execute() throws BusinessException {
        Optional<ContractType> oct = repo.findByName(name);
        BusinessChecks.exists(oct, "Contract type not found: " + name);

        ContractType ct = oct.get();

        ContractTypeDto dto = new ContractTypeDto();
        dto.id = ct.getId();
        dto.name = ct.getName();
        dto.compensationDays = ct.getCompensationDaysPerYear();
        return dto;
    }

}