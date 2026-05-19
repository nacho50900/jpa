package uo.ri.ui.manager.contracts.contract.action;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.contract.ContractCrudService;
import uo.ri.cws.application.service.contract.ContractCrudService.ContractDto;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class AddContractAction implements Action {
	
	private ContractCrudService service = Factories.service.forContractCrudService();

	/**
	 * Creates a contract. The mechanic's NIF, contract type name, type name, 
	 * month and base salary are provided via console.
	 */
	@Override
	public void execute() throws BusinessException {

		ContractDto c = new ContractDto();
		c.mechanic.nif = Console.readString("Mechanic NIF");
		c.contractType.name = askForType();
		c.professionalGroup.name = askForPorfessionalGroup();
		c.annualBaseSalary = Console.readDouble("Annual base salary");
		if ("FIXED_TERM".equals(c.contractType.name)) {
			c.endDate = Console.readDate("Type end date");
		}

		service.create( c );
		
		Console.println("Contract registered");
	}

	private String askForPorfessionalGroup() {
		Console.println("Professional group");
		Console.println("I \t II \t III \t IV \t V \t VI \t VII");
		return Console.readString("Professional group name");
	}

	private String askForType() {
		Console.println("Contract type");
		Console.println("PERMANENT \t SEASONAL \t FIXED_TERM");
		return Console.readString("Contract type name");
	}

}