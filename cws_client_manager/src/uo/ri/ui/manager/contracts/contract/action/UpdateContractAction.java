package uo.ri.ui.manager.contracts.contract.action;

import java.time.LocalDate;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.contract.ContractCrudService;
import uo.ri.cws.application.service.contract.ContractCrudService.ContractDto;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class UpdateContractAction implements Action {
	private ContractCrudService service = Factories.service.forContractCrudService();

	@Override
	public void execute() throws BusinessException {

		String id = Console.readString("Contract id");
		// Find contract by id
		ContractDto c = service.findById(id).orElseThrow(
			() -> new BusinessException("Contract not found")
		);

		c.endDate = askOptionalForDate("End date");
		c.annualBaseSalary = Console.readDouble("Annual base salary");

		service.update(c);

		Console.println("Contract updated");
	}

	private LocalDate askOptionalForDate(String msg) {
		while (true) {
			try {
				Console.print(msg + " [optional]: ");
				String asString = Console.readString();
				return ("".equals(asString)) ? null : LocalDate.parse(asString);
			} catch (Exception e) {
				Console.println("--> Invalid date");
			}
		}
	}
}