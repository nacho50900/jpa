package uo.ri.cws.application.service.contract.crud.command;

import java.time.LocalDate;
import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.ContractRepository;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Contract;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class TerminateContract implements Command<Void> {

    private String contractId;
    private ContractRepository repo = Factories.repository.forContract();

    public TerminateContract(String contractId) {
    	ArgumentChecks.isNotNull(contractId, "Contract ID cannot be null");
        ArgumentChecks.isNotBlank(contractId, "Contract ID cannot be blank");
        this.contractId = contractId;
    }

    @Override
    public Void execute() throws BusinessException {
        Optional<Contract> oc = repo.findById(contractId);
        BusinessChecks.exists(oc, "Contract not found");

        Contract c = oc.get();
        BusinessChecks.isTrue(c.isInForce(),
                "Only in-force contracts can be terminated");

        c.terminate(LocalDate.now());
        return null;
    }

}