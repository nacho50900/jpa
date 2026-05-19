package uo.ri.ui.manager.contracts.contracttype.action;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.contracttype.ContractTypeCrudService;
import uo.ri.cws.application.service.contracttype.ContractTypeCrudService.ContractTypeDto;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class AddContractTypeAction implements Action {

	private ContractTypeCrudService service = Factories.service.forContractTypeCrudService();

	@Override
	public void execute() throws BusinessException {
		ContractTypeDto dto = new ContractTypeDto();
		dto.name = Console.readString("Contract type name");
		dto.compensationDays = Console.readDouble("Compensation days");
		
		service.create(dto);
		
		Console.println("Contract type registered");
	}

}