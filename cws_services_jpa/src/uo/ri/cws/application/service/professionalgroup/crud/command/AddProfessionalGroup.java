package uo.ri.cws.application.service.professionalgroup.crud.command;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.ProfessionalGroupRepository;
import uo.ri.cws.application.service.professionalgroup.ProfessionalGroupCrudService.ProfessionalGroupDto;
import uo.ri.cws.application.service.professionalgroup.crud.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.ProfessionalGroup;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class AddProfessionalGroup implements Command<ProfessionalGroupDto> {

	private ProfessionalGroupDto dto;
	private ProfessionalGroupRepository repo = Factories.repository.forProfessionalGroup();

	public AddProfessionalGroup(ProfessionalGroupDto dto) {
		ArgumentChecks.isNotNull(dto, "ProfessionalGroupDto cannot be null");
		ArgumentChecks.isNotBlank(dto.name, "groupName cannot be blank");
		ArgumentChecks.isTrue(dto.trienniumPayment >= 0 , 
				"trienniumSalary must be >= 0");
		ArgumentChecks.isTrue(dto.productivityRate >= 0 , 
				"productivityRate must be >= 0");
		this.dto = dto;
	}

	@Override
	public ProfessionalGroupDto execute() throws BusinessException {
		Optional<ProfessionalGroup> op = repo.findByName(dto.name);
		BusinessChecks.doesNotExist(
				op, "The Professional Group does already exists"); // Repeated
		
		ProfessionalGroup pg = new ProfessionalGroup(
			dto.name, 
			dto.trienniumPayment, 
			dto.productivityRate
		);
		
		repo.add(pg);
		
		return DtoAssembler.toDto(pg);
	}

}