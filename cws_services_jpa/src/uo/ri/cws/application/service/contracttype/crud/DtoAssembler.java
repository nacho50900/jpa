package uo.ri.cws.application.service.contracttype.crud;

import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.service.contracttype.ContractTypeCrudService.ContractTypeDto;
import uo.ri.cws.domain.ContractType;

public class DtoAssembler {
	
	public static Optional<ContractTypeDto> toOptionalDto(Optional<ContractType> arg) {
		return arg.map(ct -> toDto(ct));
	}

	public static ContractTypeDto toDto(ContractType arg) {
		ContractTypeDto result = new ContractTypeDto();
		result.id = arg.getId();
		result.version = arg.getVersion();
		result.name = arg.getName();
		result.compensationDays = arg.getCompensationDaysPerYear();
		return result;
	}

	public static List<ContractTypeDto> toDtoList(List<ContractType> list) {
		return list.stream().map(ct -> toDto(ct)).toList();
	}
	
}
