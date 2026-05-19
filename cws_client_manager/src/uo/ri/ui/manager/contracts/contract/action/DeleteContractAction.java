package uo.ri.ui.manager.contracts.contract.action;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.contract.ContractCrudService;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class DeleteContractAction implements Action {

	@Override
	public void execute() throws BusinessException {

		String id = Console.readString("Contract id");
		
		ContractCrudService service = Factories.service.forContractCrudService();
		service.delete(id);
		
		Console.println("The contract has been deleted");
	}

}
