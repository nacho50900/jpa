package uo.ri.cws.application.service.professionalgroup.crud.command;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.ProfessionalGroupRepository;
import uo.ri.cws.application.service.professionalgroup.ProfessionalGroupCrudService.ProfessionalGroupDto;
import uo.ri.cws.application.service.professionalgroup.crud.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessException;

public class FindProfessionalGroupByName implements Command<Optional<ProfessionalGroupDto>> {

	private String name;
	private ProfessionalGroupRepository repo = Factories.repository.forProfessionalGroup();

	public FindProfessionalGroupByName(String name) {
		ArgumentChecks.isNotEmpty(name, "groupName cannot be empty");
		ArgumentChecks.isNotBlank(name, "groupName cannot be blank");
		this.name = name;
	}

	@Override
	public Optional<ProfessionalGroupDto> execute() throws BusinessException {
		return repo.findByName(name).map(pg -> DtoAssembler.toDto(pg));
	}

}