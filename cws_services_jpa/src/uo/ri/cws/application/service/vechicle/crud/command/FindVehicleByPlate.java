package uo.ri.cws.application.service.vechicle.crud.command;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.VehicleRepository;
import uo.ri.cws.application.service.vechicle.crud.DtoAssembler;
import uo.ri.cws.application.service.vehicle.VehicleCrudService.VehicleDto;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Vehicle;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessException;

public class FindVehicleByPlate implements Command<Optional<VehicleDto>> {

	private String plate;
	private VehicleRepository repo = Factories.repository.forVehicle();
	
	public FindVehicleByPlate(String plate) {
		ArgumentChecks.isNotEmpty( plate );
		this.plate = plate;
	}

	@Override
	public Optional<VehicleDto> execute() throws BusinessException {
		Optional<Vehicle> ov = repo.findByPlate( plate );
		return ov.map( v  -> DtoAssembler.toDto( v ) );
	}

}
