package uo.ri.ui.manager.contracts.contract.action;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.contract.ContractCrudService;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class FinishContractAction implements Action {
	private ContractCrudService service = Factories.service.forContractCrudService();

	@Override
	public void execute() throws BusinessException {
		String id = Console.readString("Contract Id");
				
		service.terminate(id);
		
		Console.println("The contract has been terminated");
	}
}
