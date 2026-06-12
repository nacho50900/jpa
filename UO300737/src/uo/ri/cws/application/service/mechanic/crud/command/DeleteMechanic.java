package uo.ri.cws.application.service.mechanic.crud.command;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.MechanicRepository;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Mechanic;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class DeleteMechanic implements Command<Void> {

	private String mechanicId;
	private MechanicRepository repo = Factories.repository.forMechanic();

	public DeleteMechanic(String mechanicId) {
		ArgumentChecks.isNotBlank(mechanicId, "mechanicId can not be blank");
		this.mechanicId = mechanicId;
	}

	@Override
	public Void execute() throws BusinessException {
		
		Optional<Mechanic> om = repo.findById(mechanicId);
		BusinessChecks.exists(om, "The mechanic does not exist");
		Mechanic m = om.get();
		BusinessChecks.isTrue(m.getInterventions().isEmpty(),
				"Cannot delete mechanic with interventions");
		BusinessChecks.isTrue(m.getAssigned().isEmpty(),
				"Cannot delete mechanic with assigned work orders");
        BusinessChecks.isTrue(m.getContracts().isEmpty(),
        		"Cannot delete mechanic with associated contracts");

		repo.remove(m);
		return null;
	}

}
