package uo.ri.cws.application.service.util.dtobuilders;

import java.time.LocalDate;
import java.util.UUID;

import uo.ri.cws.application.service.contract.ContractCrudService.ContractDto;
import uo.ri.cws.application.service.contract.ContractCrudService.ContractTypeOfContractDto;
import uo.ri.cws.application.service.contract.ContractCrudService.MechanicOfContractDto;
import uo.ri.cws.application.service.contract.ContractCrudService.ProfessionalGroupOfContractDto;
import uo.ri.cws.application.service.contracttype.ContractTypeCrudService.ContractTypeDto;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.cws.application.service.professionalgroup.ProfessionalGroupCrudService.ProfessionalGroupDto;

public class ContractDtoBuilder {

	private ContractDto dto = createDefaultDto();

	private ContractDto createDefaultDto() {
		ContractDto d = new ContractDto();
		d.id = UUID.randomUUID().toString();
		d.version = 0L;
		d.mechanic = new MechanicOfContractDto();
		d.contractType = new ContractTypeOfContractDto();
		d.professionalGroup = new ProfessionalGroupOfContractDto();
		d.startDate = LocalDate.now();
		d.endDate = null;
		d.annualBaseSalary = 1000.0;
		d.settlement = 0.0;
		d.state = "IN_FORCE";
		d.taxRate = 0.15;
		return d;
	}

	public ContractDto build() {
		return dto;
	}

	public ContractDtoBuilder forMechanic(MechanicDto mDto) {
		dto.mechanic.id = mDto.id;
		dto.mechanic.nif = mDto.nif;
		dto.mechanic.name = mDto.name;
		dto.mechanic.surname = mDto.surname;
		return this;
	}

	public ContractDtoBuilder withId(String arg) {
		dto.id = arg;
		return this;
	}

	public ContractDtoBuilder withContractType(ContractTypeDto ct) {
		dto.contractType.name = ct.name;
		return this;
	}

	public ContractDtoBuilder withProfessionalGroup(ProfessionalGroupDto arg) {
		dto.professionalGroup.name = arg.name;
		dto.professionalGroup.id = arg.id;
		return this;
	}

	public ContractDtoBuilder withState(String state) {
		dto.state = state;
		return this;
	}

	public ContractDtoBuilder withStartDate(LocalDate start) {
		dto.startDate = start;
		return this;
	}

	public ContractDtoBuilder withEndDate(LocalDate end) {
		dto.endDate = end;
		return this;
	}

	public ContractDtoBuilder withAnnualSalary(double pay) {
		dto.annualBaseSalary = pay;
		return this;
	}

	public ContractDtoBuilder withTaxRate(double irpf) {
		this.dto.taxRate = irpf;
		return this;
	}

	public ContractDtoBuilder forMechanicNif(String nif) {
		dto.mechanic.nif = nif;
		return this;
	}

	public ContractDtoBuilder forContractTypeName(String name) {
		dto.contractType.name = name;
		return this;
	}

	public ContractDtoBuilder forProfessionalGroupName(String name) {
		dto.professionalGroup.name = name;
		return this;
	}

	public ContractDtoBuilder withVersion(Long version) {
		dto.version = version;
		return this;
	}


}
