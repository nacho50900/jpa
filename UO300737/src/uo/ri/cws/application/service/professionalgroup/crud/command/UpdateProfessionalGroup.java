package uo.ri.cws.application.service.professionalgroup.crud.command;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.ProfessionalGroupRepository;
import uo.ri.cws.application.service.professionalgroup.ProfessionalGroupCrudService.ProfessionalGroupDto;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.ProfessionalGroup;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class UpdateProfessionalGroup implements Command<Void> {

	private ProfessionalGroupDto dto;
	private ProfessionalGroupRepository repo = Factories.repository.forProfessionalGroup();

	public UpdateProfessionalGroup(ProfessionalGroupDto dto) {
		ArgumentChecks.isNotNull(dto, "dto can not be null");
		ArgumentChecks.isNotBlank(dto.id, "id can not be null");
		ArgumentChecks.isNotBlank(dto.name, "name can not be null");
		ArgumentChecks.isTrue(dto.trienniumPayment >= 0 , 
				"trienniumSalary must be >= 0");
		ArgumentChecks.isTrue(dto.productivityRate >= 0 , 
				"productivityRate must be >= 0");
		this.dto = dto;
	}

	@Override
	public Void execute() throws BusinessException {
		
		Optional<ProfessionalGroup> opg = repo.findByName(dto.name);
		BusinessChecks.exists(opg, "Professional group does not exist");
		
		ProfessionalGroup pg = opg.get();
		
		// Optimistic locking
		BusinessChecks.hasVersion(dto.version, pg.getVersion());
		
		// Solo se pueden modificar trienniumSalary y productivityRate
		// NO se puede modificar el name ni el id
		pg.setTrienniumPayment(dto.trienniumPayment);
		pg.setProductivityRate(dto.productivityRate);
		
		 pg.updatedNow(); // Important every time an entity is updated
		
		return null;
	}

}