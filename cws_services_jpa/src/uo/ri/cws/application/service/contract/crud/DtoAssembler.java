package uo.ri.cws.application.service.contract.crud;

import java.util.List;

import uo.ri.cws.application.service.contract.ContractCrudService.ContractDto;
import uo.ri.cws.application.service.contract.ContractCrudService.ContractSummaryDto;
import uo.ri.cws.domain.Contract;

public class DtoAssembler {

	public static ContractDto toDto(Contract arg) {
		ContractDto result = new ContractDto();
		result.id = arg.getId();
		result.version = arg.getVersion();
		
		result.annualBaseSalary = arg.getAnnualBaseSalary();
		result.startDate = arg.getStartDate();
		result.endDate = arg.getEndDate();
		result.state = arg.getState().toString();
		result.taxRate = arg.getTaxRate();
		result.settlement = arg.getSettlement();
		
		result.contractType.id = arg.getContractType().getId();
		result.contractType.name = arg.getContractType().getName();
		result.contractType.compensationDaysPerYear = arg.getContractType().getCompensationDaysPerYear();
		
		result.professionalGroup.id = arg.getProfessionalGroup().getId();		
		result.professionalGroup.name = arg.getProfessionalGroup().getName();
		result.professionalGroup.productivityRate = arg.getProfessionalGroup().getProductivityRate();
		result.professionalGroup.trieniumPayment = arg.getProfessionalGroup().getTrienniumPayment();
		
		result.mechanic.id = arg.getMechanic().getId();
		result.mechanic.nif = arg.getMechanic().getNif();
		result.mechanic.name = arg.getMechanic().getName();
		result.mechanic.surname = arg.getMechanic().getSurname();

		return result;
	}

	public static ContractSummaryDto toContractSummaryDto(Contract arg) {
		ContractSummaryDto result = new ContractSummaryDto();
		result.id = arg.getId();
		result.state = arg.getState().toString();
		result.nif = arg.getMechanic().getNif();
		result.settlement = arg.getSettlement();
		return result;
	}

	public static List<ContractSummaryDto> toContractSummaryDtoList(List<Contract> arg) {
		return arg.stream().map(c -> toContractSummaryDto(c) ).toList();
	}

	public static List<ContractDto> toDtoList(List<Contract> contracts) {
		return contracts.stream().map( c -> toDto(c) ).toList();
	}

}
