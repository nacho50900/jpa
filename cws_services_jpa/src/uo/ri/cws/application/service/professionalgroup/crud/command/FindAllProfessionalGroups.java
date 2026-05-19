package uo.ri.cws.application.service.professionalgroup.crud.command;

import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.ProfessionalGroupRepository;
import uo.ri.cws.application.service.professionalgroup.ProfessionalGroupCrudService.ProfessionalGroupDto;
import uo.ri.cws.application.service.professionalgroup.crud.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.ProfessionalGroup;

public class FindAllProfessionalGroups implements Command<List<ProfessionalGroupDto>> {

	private ProfessionalGroupRepository repo = Factories.repository.forProfessionalGroup();

	@Override
	public List<ProfessionalGroupDto> execute() {
		
		List<ProfessionalGroup> groups = repo.findAll();
		
		return DtoAssembler.toProfessionalGroupDtoList(groups);
	}

}