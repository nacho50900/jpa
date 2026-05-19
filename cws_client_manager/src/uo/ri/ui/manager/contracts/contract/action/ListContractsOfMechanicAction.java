package uo.ri.ui.manager.contracts.contract.action;

import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.contract.ContractCrudService;
import uo.ri.cws.application.service.contract.ContractCrudService.ContractSummaryDto;
import uo.ri.ui.util.Printer;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class ListContractsOfMechanicAction implements Action {
	private ContractCrudService as = Factories.service.forContractCrudService();

	@Override
	public void execute() throws BusinessException {
	
		String nif = Console.readString("Mechanic nif"); 
		
		List<ContractSummaryDto> contracts = as.findByMechanicNif( nif );
		
		for(ContractSummaryDto c: contracts) {
			Printer.printContractSummary( c );
		}
	}
}
