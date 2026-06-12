package uo.ri.cws.application.service.payroll.crud.command;

import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.PayrollRepository;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Payroll;
import uo.ri.util.exception.BusinessException;

public class DeleteAllLastPayrolls implements Command<Integer> {

    private PayrollRepository payrollRepo = Factories.repository.forPayroll();

    @Override
    public Integer execute() throws BusinessException {
        List<Payroll> lastPayrolls = payrollRepo.findLastMonthPayrolls();
        
        for (Payroll payroll : lastPayrolls) {
            payrollRepo.remove(payroll);
        }
        
        return lastPayrolls.size();
    }
}