package uo.ri.ui.manager.payroll.action;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.payroll.PayrollService;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class DeleteLastMonthPayrollAction implements Action {
    private PayrollService service = Factories.service.forPayrollService();

    @Override
    public void execute() throws BusinessException {
        service.deleteLastGenerated();
        
        Console.println("Last month's payrolls deleted");
    }
}