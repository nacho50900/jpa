package uo.ri.ui.manager.contracts.professionalgroup.action;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.professionalgroup.ProfessionalGroupCrudService;
import uo.ri.cws.application.service.professionalgroup.ProfessionalGroupCrudService.ProfessionalGroupDto;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class AddProfessionalGroupAction implements Action {
    private ProfessionalGroupCrudService service = Factories.service.forProfessionalGroupCrudService();

    @Override
    public void execute() throws BusinessException {
        ProfessionalGroupDto dto = new ProfessionalGroupDto();
        dto.name = Console.readString("Professional group name");
        dto.trienniumPayment = Console.readDouble("Triennium payment");
        dto.productivityRate = Console.readDouble("Productivity rate");
        
        service.create(dto);
        
        Console.println("Professional group registered");
    }
}