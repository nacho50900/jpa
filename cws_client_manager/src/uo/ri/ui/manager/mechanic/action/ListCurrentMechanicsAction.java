package uo.ri.ui.manager.mechanic.action;

import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.contract.ContractCrudService;
import uo.ri.cws.application.service.contract.ContractCrudService.ContractDto;
import uo.ri.ui.util.Printer;
import uo.ri.util.menu.Action;

public class ListCurrentMechanicsAction implements Action {
	private ContractCrudService service = Factories.service.forContractCrudService();

	@Override
	public void execute() throws Exception {
		List<ContractDto> contracts = service.findInforceContracts();
		
		if (contracts.isEmpty()) {
			System.out.println("No in force contracts have found");
			return;
		}
		
		for (ContractDto c : contracts) {
			Printer.printSumarizedContract( c );
		}
	}

}