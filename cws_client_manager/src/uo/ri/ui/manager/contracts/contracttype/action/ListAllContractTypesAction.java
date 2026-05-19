package uo.ri.ui.manager.contracts.contracttype.action;

import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.contracttype.ContractTypeCrudService;
import uo.ri.cws.application.service.contracttype.ContractTypeCrudService.ContractTypeDto;
import uo.ri.ui.util.Printer;
import uo.ri.util.console.Console;
import uo.ri.util.menu.Action;

public class ListAllContractTypesAction implements Action {
	private ContractTypeCrudService service = Factories.service.forContractTypeCrudService();

	@Override
	public void execute() throws Exception {
		List<ContractTypeDto> types = service.findAll();
		if (types.isEmpty()) {
			Console.println("No contract types found");
			return;
		}
		
		for (ContractTypeDto dto : types) {
			Printer.printContractType(dto);
		}
	}

}