package uo.ri.ui.manager.contracts.professionalgroup.action;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.professionalgroup.ProfessionalGroupCrudService;
import uo.ri.cws.application.service.professionalgroup.ProfessionalGroupCrudService.ProfessionalGroupDto;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class UpdateProfessionalGroupAction implements Action {
    private ProfessionalGroupCrudService service = Factories.service.forProfessionalGroupCrudService();

    @Override
    public void execute() throws BusinessException {
        String name = Console.readString("Professional group name");
        
        ProfessionalGroupDto dto = service.findByName(name)
            .orElseThrow(() -> new BusinessException("Professional group not found"));
        
        dto.trienniumPayment = Console.readDouble("Triennium payment");
        dto.productivityRate = Console.readDouble("Productivity rate");
        
        service.update(dto);
        
        Console.println("Professional group updated");
    }
}