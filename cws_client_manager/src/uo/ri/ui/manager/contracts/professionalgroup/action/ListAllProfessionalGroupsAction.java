package uo.ri.ui.manager.contracts.professionalgroup.action;

import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.professionalgroup.ProfessionalGroupCrudService;
import uo.ri.cws.application.service.professionalgroup.ProfessionalGroupCrudService.ProfessionalGroupDto;
import uo.ri.ui.util.Printer;
import uo.ri.util.console.Console;
import uo.ri.util.menu.Action;

public class ListAllProfessionalGroupsAction implements Action {
    private ProfessionalGroupCrudService service = Factories.service.forProfessionalGroupCrudService();

    @Override
    public void execute() throws Exception {
        List<ProfessionalGroupDto> groups = service.findAll();
        
        if (groups.isEmpty()) {
            Console.println("No professional groups found");
            return;
        }
        
        for (ProfessionalGroupDto dto : groups) {
            Printer.printProfessionalGroup(dto);
        }
    }
}