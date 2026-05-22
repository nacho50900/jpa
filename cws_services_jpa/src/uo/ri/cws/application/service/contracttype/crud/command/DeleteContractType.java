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

    private String contractTypeName; //TEST PASA NAME
    private ContractTypeRepository repo = Factories.repository.forContractType();

    public DeleteContractType(String name) {
        ArgumentChecks.isNotBlank(name, "ContractType name cannot be blank");
        this.contractTypeName = name;
    }

    @Override
    public Void execute() throws BusinessException {
        Optional<ContractType> oct = repo.findByName(contractTypeName);
        BusinessChecks.exists(oct, "Contract type not found");

        ContractType ct = oct.get();
        BusinessChecks.isTrue(ct.getContracts().isEmpty(),
                "Cannot delete a contract type with associated contracts");

        repo.remove(ct);
        return null;
    }

}