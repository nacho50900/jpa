package uo.ri.cws.application.service.mechanic.crud.command;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.MechanicRepository;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.cws.application.service.mechanic.crud.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Mechanic;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class AddMechanic implements Command<MechanicDto> {

	private MechanicDto dto;
	private MechanicRepository repo = Factories.repository.forMechanic();

	public AddMechanic(MechanicDto dto) {
		ArgumentChecks.isNotNull(dto, "Mechanic cannot be null");
		ArgumentChecks.isNotBlank(dto.nif, "NIF cannot be blank");
		ArgumentChecks.isNotBlank(dto.name, "Name cannot be blank");
		ArgumentChecks.isNotBlank(dto.surname, "Surname cannot be blank");
		this.dto = dto;
	}

	@Override
	public MechanicDto execute() throws BusinessException {

		Optional<Mechanic> om = repo.findByNif(dto.nif);
		BusinessChecks.doesNotExist(om, "Mechanic already exist");
		    
		Mechanic m = new Mechanic(dto.nif, dto.name, dto.surname);
		repo.add(m);
		
		return DtoAssembler.toDto(m);
	}

}
