package uo.ri.cws.application.service.professionalgroup.crud;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.service.professionalgroup.ProfessionalGroupCrudService.ProfessionalGroupDto;
import uo.ri.cws.domain.ProfessionalGroup;
import uo.ri.cws.domain.base.BaseEntity;

public class DtoAssembler {


	public static Optional<ProfessionalGroupDto> toProfessionalGroupDto(Optional<ProfessionalGroup> arg) {
		ProfessionalGroupDto dto = null;
		
		if (arg.isPresent()) {
			dto = toDto(arg.get());
		}
		return Optional.ofNullable(dto);
	}

	public static List<ProfessionalGroupDto> toProfessionalGroupDtoList(List<ProfessionalGroup> list) {
		List<ProfessionalGroupDto> res = new ArrayList<>();
		for (ProfessionalGroup m : list) {
			res.add(toDto(m));
		}
		return res;
	}
	
	public static ProfessionalGroupDto toDto(ProfessionalGroup m) {
		ProfessionalGroupDto 	dto = new ProfessionalGroupDto();
		dto.id = m.getId();
		dto.version = ((BaseEntity) m).getVersion();

		dto.name = m.getName();
		dto.productivityRate = m.getProductivityRate();
		dto.trienniumPayment = m.getTrienniumPayment();
		return dto;
	}
}
