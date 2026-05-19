package uo.ri.cws.application.service.payroll.crud.command;

import java.time.LocalDate;
import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.MechanicRepository;
import uo.ri.cws.application.repository.PayrollRepository;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Mechanic;
import uo.ri.cws.domain.Payroll;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class DeleteLastPayrollOfMechanic implements Command<Void> {

    private String mechanicId;
    private MechanicRepository mechanicRepo = Factories.repository.forMechanic();
    private PayrollRepository payrollRepo = Factories.repository.forPayroll();

    public DeleteLastPayrollOfMechanic(String mechanicId) {
        ArgumentChecks.isNotBlank(mechanicId, "mechanicId cannot be blank");
        this.mechanicId = mechanicId;
    }

    @Override
    public Void execute() throws BusinessException {
        Optional<Mechanic> om = mechanicRepo.findById(mechanicId);
        BusinessChecks.exists(om, "Mechanic does not exist");

        Optional<Payroll> lastPayroll = payrollRepo.findLastPayrollByMechanicId(mechanicId);

        if (lastPayroll.isPresent()) {
        	Payroll payroll = lastPayroll.get();
            LocalDate lastMonth = LocalDate.now().minusMonths(1);
        	if(payroll.isFromMonth(lastMonth)) {
        		payrollRepo.remove(payroll);
        	}
        }
        return null;
    }
}