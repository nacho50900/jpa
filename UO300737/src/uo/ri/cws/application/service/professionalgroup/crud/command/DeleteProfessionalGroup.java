package uo.ri.cws.application.service.professionalgroup.crud.command;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.ProfessionalGroupRepository;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.ProfessionalGroup;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class DeleteProfessionalGroup implements Command<Void> {

	private String name;
	private ProfessionalGroupRepository repo = Factories.repository.forProfessionalGroup();

	public DeleteProfessionalGroup(String name) {
		ArgumentChecks.isNotBlank(name, "groupName cannot be blank");
		this.name = name;
	}

	@Override
	public Void execute() throws BusinessException {
		
		Optional<ProfessionalGroup> opg = repo.findByName(name);
		BusinessChecks.exists(opg, "Professional group does not exist");
		
		ProfessionalGroup pg = opg.get();
		
		// Un grupo profesional NO puede eliminarse si tiene contratos asociados
		BusinessChecks.isTrue(pg.getContracts().isEmpty(), 
			"Cannot delete professional group with associated contracts");
		
		repo.remove(pg);
		
		return null;
	}

}