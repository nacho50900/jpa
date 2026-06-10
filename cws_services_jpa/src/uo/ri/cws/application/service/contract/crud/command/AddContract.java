package uo.ri.cws.application.service.contract.crud.command;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.ContractRepository;
import uo.ri.cws.application.repository.ContractTypeRepository;
import uo.ri.cws.application.repository.MechanicRepository;
import uo.ri.cws.application.repository.ProfessionalGroupRepository;
import uo.ri.cws.application.service.contract.ContractCrudService.ContractDto;
import uo.ri.cws.application.service.contract.crud.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Contract;
import uo.ri.cws.domain.ContractType;
import uo.ri.cws.domain.Mechanic;
import uo.ri.cws.domain.ProfessionalGroup;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class AddContract implements Command<ContractDto> {

    private ContractDto dto;
    private MechanicRepository mechanicRepo =
            Factories.repository.forMechanic();
    private ContractTypeRepository typeRepo =
            Factories.repository.forContractType();
    private ProfessionalGroupRepository groupRepo =
            Factories.repository.forProfessionalGroup();
    private ContractRepository contractRepo =
            Factories.repository.forContract();

    public AddContract(ContractDto dto) {
        ArgumentChecks.isNotNull(dto, "ContractDto cannot be null");
        ArgumentChecks.isNotBlank(dto.mechanic.nif, "Mechanic NIF cannot be blank");
        ArgumentChecks.isNotBlank(dto.contractType.name,
                "Contract type name cannot be blank");
        ArgumentChecks.isNotBlank(dto.professionalGroup.name,
                "Professional group name cannot be blank");
        ArgumentChecks.isTrue(dto.annualBaseSalary > 0,
                "Annual base salary must be > 0");
        this.dto = dto;
    }

    @Override
    public ContractDto execute() throws BusinessException {
        Optional<Mechanic> om = mechanicRepo.findByNif(dto.mechanic.nif);
        BusinessChecks.exists(om, "Mechanic not found: " + dto.mechanic.nif);

        Optional<ContractType> ot = typeRepo.findByName(dto.contractType.name);
        BusinessChecks.exists(ot, "Contract type not found: " + dto.contractType.name);

        Optional<ProfessionalGroup> og =
                groupRepo.findByName(dto.professionalGroup.name);
        BusinessChecks.exists(og,
                "Professional group not found: " + dto.professionalGroup.name);

        Mechanic mechanic = om.get();
        ContractType type = ot.get();
        ProfessionalGroup group = og.get();

        LocalDate startDate = dto.startDate.with(TemporalAdjusters.firstDayOfNextMonth());
        //Needed because of test extended and acceptance test compatibility
        
        // Validate FIXED_TERM requires end date
        boolean isFixedTerm = "FIXED_TERM".equalsIgnoreCase(type.getName());
        if (isFixedTerm) {
            ArgumentChecks.isNotNull(dto.endDate,
                    "End date is mandatory for FIXED_TERM contracts");
            
            BusinessChecks.isTrue(!dto.endDate.isBefore(dto.startDate),
                    "End date cannot be before start date");
        }

        Contract contract;
        if (isFixedTerm) {
            contract = new Contract(mechanic, type, group,
            		startDate, dto.endDate, dto.annualBaseSalary);
        } else {
            contract = new Contract(mechanic, type, group,
            		startDate, dto.annualBaseSalary);
        }

        contractRepo.add(contract);
        return DtoAssembler.toDto(contract);
    }

}