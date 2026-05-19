package uo.ri.cws.application.service.util.dtobuilders;

import java.util.UUID;

import uo.ri.cws.application.service.professionalgroup.ProfessionalGroupCrudService.ProfessionalGroupDto;
import uo.ri.util.math.Rounds;
import uo.ri.util.random.Random;

public class ProfessionalGroupDtoBuilder {
	private ProfessionalGroupDto dto = createRandomProfessionalGroupDto();;

	public ProfessionalGroupDto build() {
		return dto;
	}

	private ProfessionalGroupDto createRandomProfessionalGroupDto() {
		ProfessionalGroupDto res = new ProfessionalGroupDto();
	
		res.id = UUID.randomUUID().toString();
		res.version = 0L;
		res.name = "dummy-professional-group-name" + Random.inRange(1, 100000);
		res.trienniumPayment = Rounds.toCents( Random.inRange(50, 500 ));
		res.productivityRate = Rounds.toCents( Random.inRange(0.05, 0.25) );
		return res;
	}

	public ProfessionalGroupDtoBuilder withName(String name) {
		dto.name = name;
		return this;
	}

	public ProfessionalGroupDtoBuilder withId(String id) {
		dto.id = id;
		return this;
	}

	public ProfessionalGroupDtoBuilder withTriennium(double amount) {
		dto.trienniumPayment = amount;
		return this;
	}

	public ProfessionalGroupDtoBuilder withProductivityRate(double rate) {
		dto.productivityRate = rate;
		return this;
	}

	public ProfessionalGroupDtoBuilder withVersion(long version) {
		dto.version = version;
		return this;
	}
}
