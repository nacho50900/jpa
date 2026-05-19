package uo.ri.ui.manager.contracts.contracttype.action;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.contracttype.ContractTypeCrudService;
import uo.ri.cws.application.service.contracttype.ContractTypeCrudService.ContractTypeDto;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class UpdateContractTypeAction implements Action {
	private ContractTypeCrudService service = Factories.service.forContractTypeCrudService();

	@Override
	public void execute() throws BusinessException {
		String name = Console.readString("Contract type name");

		ContractTypeDto dto = service.findByName(name)
			.orElseThrow(() -> new BusinessException("Contract type not found"));

		dto.compensationDays = Console.readDouble("Compensation days");

		service.update(dto);

		Console.println("Contract type updated");
	}

}