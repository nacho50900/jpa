package uo.ri.ui.manager.payroll.action;

import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.payroll.PayrollService;
import uo.ri.cws.application.service.payroll.PayrollService.PayrollDto;
import uo.ri.ui.util.Printer;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class GeneratePayrollsTodayAction implements Action {
    private PayrollService service = Factories.service.forPayrollService();

    @Override
    public void execute() throws BusinessException {
    	
        List<PayrollDto> payrolls = service.generateForPreviousMonth();
        
        Console.println( payrolls.size() + " payrolls generated");
        Printer.printPayrolls( payrolls );
    }
}