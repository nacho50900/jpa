package uo.ri.cws.application.service.util.dtobuilders;

import java.util.Random;

import uo.ri.cws.application.service.contracttype.ContractTypeCrudService.ContractTypeDto;

public class ContractTypeDtoBuilder {
	private ContractTypeDto dto = createContractTypeDto();

	public ContractTypeDto build() {
		return dto;
	}
	
	private ContractTypeDto createContractTypeDto() {
		ContractTypeDto res = new ContractTypeDto();
		res.name = "dummy-contract-type-name " + new Random().nextInt(1000000);
		res.compensationDays = new Random().nextDouble() * 5.0;
		return res;
	}
	public ContractTypeDtoBuilder withName(String arg) {
		dto.name = arg;
		return this;
	}
	
	public ContractTypeDtoBuilder withCompensationDays(double arg) {
		dto.compensationDays= arg;
		return this;
	}
	public ContractTypeDtoBuilder withId(String arg) {
		this.dto.id = arg;
		return this;
	}

	public ContractTypeDtoBuilder withVersion(Long version) {
		this.dto.version = version;
		return this;
	}

}
