package uo.ri.cws.application.service.payroll.crud.command;

import java.util.List;
import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.MechanicRepository;
import uo.ri.cws.application.repository.PayrollRepository;
import uo.ri.cws.application.service.payroll.PayrollService.PayrollSummaryDto;
import uo.ri.cws.application.service.payroll.crud.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Mechanic;
import uo.ri.cws.domain.Payroll;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class FindPayrollsByMechanicId implements Command<List<PayrollSummaryDto>> {

    private String mechanicId;
    private MechanicRepository mechanicRepo = Factories.repository.forMechanic();
    private PayrollRepository payrollRepo = Factories.repository.forPayroll();

    public FindPayrollsByMechanicId(String mechanicId) {
        ArgumentChecks.isNotBlank(mechanicId, "mechanicId cannot be blank");
        this.mechanicId = mechanicId;
    }

    @Override
    public List<PayrollSummaryDto> execute() throws BusinessException {
        Optional<Mechanic> om = mechanicRepo.findById(mechanicId);
        BusinessChecks.exists(om, "Mechanic does not exist");

        List<Payroll> payrolls = payrollRepo.findByMechanicId(mechanicId);
        return DtoAssembler.toSummaryDtoList(payrolls);
    }
}