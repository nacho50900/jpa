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

public class UpdateContractType implements Command<Void> {

    private ContractTypeDto dto;
    private ContractTypeRepository repo = Factories.repository.forContractType();

    public UpdateContractType(ContractTypeDto dto) {
        ArgumentChecks.isNotNull(dto, "ContractTypeDto cannot be null");
        ArgumentChecks.isNotBlank(dto.id, "ID cannot be blank");
        ArgumentChecks.isTrue(dto.compensationDays >= 0,
                "Compensation days must be >= 0");
        this.dto = dto;
    }

    @Override
    public Void execute() throws BusinessException {
        Optional<ContractType> oct = repo.findById(dto.id);
        BusinessChecks.exists(oct, "Contract type not found");

        ContractType ct = oct.get();
        BusinessChecks.hasVersion(dto.version, ct.getVersion());

        ct.setCompensationDaysPerYear(dto.compensationDays);
        ct.updatedNow();
        return null;
    }

}