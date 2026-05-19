package uo.ri.cws.application.service.payroll.crud.command;

import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.PayrollRepository;
import uo.ri.cws.application.service.payroll.PayrollService.PayrollSummaryDto;
import uo.ri.cws.application.service.payroll.crud.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Payroll;
import uo.ri.util.exception.BusinessException;

public class FindAllPayrollsSummarized implements Command<List<PayrollSummaryDto>> {

    private PayrollRepository payrollRepo = Factories.repository.forPayroll();

    @Override
    public List<PayrollSummaryDto> execute() throws BusinessException {
        List<Payroll> payrolls = payrollRepo.findAll();
        return DtoAssembler.toSummaryDtoList(payrolls);
    }
}