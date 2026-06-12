package uo.ri.cws.application.service.contracttype.crud;

import java.util.List;

import uo.ri.cws.application.service.contracttype.ContractTypeCrudService.ContractTypeDto;
import uo.ri.cws.domain.ContractType;

public class DtoAssembler {

    public static ContractTypeDto toDto(ContractType ct) {
        ContractTypeDto dto = new ContractTypeDto();
        dto.id = ct.getId();
        dto.version = ct.getVersion();
        dto.name = ct.getName();
        dto.compensationDays = ct.getCompensationDaysPerYear();
        return dto;
    }

    public static List<ContractTypeDto> toDtoList(List<ContractType> list) {
        return list.stream().map(DtoAssembler::toDto).toList();
    }

    /**
     * Calculates active employees and accumulated salary for a contract type.
    
    public static int countActiveEmployees(ContractType ct) {
        return (int) ct.getContracts().stream()
                .filter(Contract::isInForce)
                .count();
    }

    public static double accumulatedSalary(ContractType ct) {
        return ct.getContracts().stream()
                .filter(Contract::isInForce)
                .mapToDouble(Contract::getAnnualBaseSalary)
                .sum();
    }
     */

}