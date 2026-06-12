package uo.ri.cws.application.service.contract.crud.command;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.ContractRepository;
import uo.ri.cws.application.service.contract.ContractCrudService.ContractDto;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Contract;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class UpdateContract implements Command<Void> {

    private ContractDto dto;
    private ContractRepository repo = Factories.repository.forContract();

    public UpdateContract(ContractDto dto) {
        ArgumentChecks.isNotNull(dto, "ContractDto cannot be null");
        ArgumentChecks.isNotBlank(dto.id, "ID cannot be blank");
        ArgumentChecks.isTrue(dto.annualBaseSalary > 0.0, 
                "Annual base salary must be positive");
        this.dto = dto;
    }

    @Override
    public Void execute() throws BusinessException {
        Optional<Contract> oc = repo.findById(dto.id);
        BusinessChecks.exists(oc, "Contract not found");

        Contract c = oc.get();
        BusinessChecks.hasVersion(dto.version, c.getVersion());
        BusinessChecks.isTrue(c.isInForce(), 
        		"Only in-force contracts can be updated");
        BusinessChecks.isTrue(Contract.isFixedTerm(c.getContractType()), 
        		"Only FIXED_TERM contracts can be updated");

        c.setAnnualBaseSalary(dto.annualBaseSalary);

        if (dto.endDate != null) {
            BusinessChecks.isTrue(!dto.endDate.isBefore(c.getStartDate()),
                    "End date cannot be before start date");
            c.setEndDate(dto.endDate);
        }
        c.updatedNow();
        return null;
    }

}