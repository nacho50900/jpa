package uo.ri.cws.application.service.professionalgroup.crud;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.professionalgroup.ProfessionalGroupCrudService;
import uo.ri.cws.application.service.professionalgroup.crud.command.AddProfessionalGroup;
import uo.ri.cws.application.service.professionalgroup.crud.command.DeleteProfessionalGroup;
import uo.ri.cws.application.service.professionalgroup.crud.command.FindAllProfessionalGroups;
import uo.ri.cws.application.service.professionalgroup.crud.command.FindProfessionalGroupByName;
import uo.ri.cws.application.service.professionalgroup.crud.command.UpdateProfessionalGroup;
import uo.ri.cws.application.util.command.CommandExecutor;
import uo.ri.util.exception.BusinessException;

public class ProfessionalGroupCrudServiceImpl implements ProfessionalGroupCrudService {

    private final CommandExecutor executor = Factories.executor.forExecutor();

    @Override
    public ProfessionalGroupDto create(ProfessionalGroupDto dto) throws BusinessException {
        return executor.execute(new AddProfessionalGroup(dto));
    }

    @Override
    public void delete(String name) throws BusinessException {
        executor.execute(new DeleteProfessionalGroup(name));
    }

    @Override
    public void update(ProfessionalGroupDto dto) throws BusinessException {
        executor.execute(new UpdateProfessionalGroup(dto));
    }

    @Override
    public Optional<ProfessionalGroupDto> findByName(String id) throws BusinessException {
        // Según tu dominio, este "id" es el "name" del grupo profesional
        // El comando devuelve Optional<ProfessionalGroupDto>.
        Optional<ProfessionalGroupDto> result =
                executor.execute(new FindProfessionalGroupByName(id));
        // Blindaje anti-nulls por si la implementación del executor/command devolviera null
        return Optional.ofNullable(result).orElse(Optional.empty());
    }

    @Override
    public List<ProfessionalGroupDto> findAll() throws BusinessException {
        List<ProfessionalGroupDto> result =
                executor.execute(new FindAllProfessionalGroups());
        return (result != null) ? result : Collections.emptyList();
    }
}
