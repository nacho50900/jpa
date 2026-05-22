package uo.ri.cws.application.service.contract.crud.command;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.ContractRepository;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Contract;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

/**
 * Deletes a contract only if the mechanic has had no work orders
 * and no payrolls were generated from it.
 */
public class DeleteContract implements Command<Void> {

    private String id;
    private ContractRepository repo = Factories.repository.forContract();

    public DeleteContract(String id) {
        ArgumentChecks.isNotBlank(id, "ID cannot be blank");
        this.id = id;
    }

    @Override
    public Void execute() throws BusinessException {
        Optional<Contract> oc = repo.findById(id);
        BusinessChecks.exists(oc, "Contract not found");

        Contract c = oc.get();

        // No payrolls generated from this contract
        BusinessChecks.isTrue(c.getPayrolls().isEmpty(),
                "Cannot delete a contract with associated payrolls");

        // Mechanic has no work orders at all
        BusinessChecks.isTrue(c.getMechanic().getAssigned().isEmpty()
                && c.getMechanic().getInterventions().isEmpty(),
                "Cannot delete: mechanic has work orders or interventions");

        repo.remove(c);
        return null;
    }

}