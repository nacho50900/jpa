package uo.ri.ui.manager.payroll.action;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.payroll.PayrollService;
import uo.ri.cws.application.service.payroll.PayrollService.PayrollDto;
import uo.ri.ui.util.Printer;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class ShowPayrollAction implements Action {
    private PayrollService service = Factories.service.forPayrollService();

    @Override
    public void execute() throws BusinessException {
        String payrollId = Console.readString("Payroll id");
        
        PayrollDto dto = service.findById(payrollId)
            .orElseThrow(() -> new BusinessException("Payroll not found"));
        
        Printer.printPayrollDetails(dto);
    }
}