package uo.ri.ui.manager.contracts.contracttype.action;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.contracttype.ContractTypeCrudService;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class DeleteContractTypeAction implements Action {

	private ContractTypeCrudService service = Factories.service.forContractTypeCrudService();

	@Override
	public void execute() throws BusinessException {
		String name = Console.readString("Contract type name");
		
		service.delete(name);
		
		Console.println("The contract type has been deleted");
	}

}