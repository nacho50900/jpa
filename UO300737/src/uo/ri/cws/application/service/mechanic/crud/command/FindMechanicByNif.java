package uo.ri.cws.application.service.mechanic.crud.command;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.MechanicRepository;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.cws.application.service.mechanic.crud.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessException;

public class FindMechanicByNif implements Command<Optional<MechanicDto>>{

	private String nif;
	private MechanicRepository repo = Factories.repository.forMechanic();

	public FindMechanicByNif(String nif) {
		ArgumentChecks.isNotNull( nif , "The mechanic nif can not be null");
		ArgumentChecks.isNotEmpty( nif , "The mechanic nif can not be empty");
		this.nif = nif;
	}

	@Override
	public Optional<MechanicDto> execute() throws BusinessException {
		return repo.findByNif( nif ).map( m -> DtoAssembler.toDto( m ) );
	}

}
