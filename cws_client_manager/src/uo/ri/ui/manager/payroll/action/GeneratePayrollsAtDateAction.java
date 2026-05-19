package uo.ri.ui.manager.payroll.action;

import java.time.LocalDate;
import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.payroll.PayrollService;
import uo.ri.cws.application.service.payroll.PayrollService.PayrollDto;
import uo.ri.ui.util.Printer;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class GeneratePayrollsAtDateAction implements Action {
    private PayrollService service = Factories.service.forPayrollService();

    @Override
    public void execute() throws BusinessException {
        LocalDate date = Console.readDate("Date (yyyy-MM-dd)");
        
        List<PayrollDto> payrolls= service.generateForPreviousMonthOf(date);
        
        Console.println( payrolls.size() + " payrolls generated for the specified date");
        Printer.printPayrolls( payrolls );
   }
}