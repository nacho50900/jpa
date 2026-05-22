package uo.ri.cws.application.service.contracttype.crud.command;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.ContractTypeRepository;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.ContractType;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class DeleteContractType implements Command<Void> {

    private String id;
    private ContractTypeRepository repo = Factories.repository.forContractType();

    public DeleteContractType(String id) {
        ArgumentChecks.isNotBlank(id, "ID cannot be blank");
        this.id = id;
    }

    @Override
    public Void execute() throws BusinessException {
        Optional<ContractType> oct = repo.findById(id);
        BusinessChecks.exists(oct, "Contract type not found");

        ContractType ct = oct.get();
        BusinessChecks.isTrue(ct.getContracts().isEmpty(),
                "Cannot delete a contract type with associated contracts");

        repo.remove(ct);
        return null;
    }

}