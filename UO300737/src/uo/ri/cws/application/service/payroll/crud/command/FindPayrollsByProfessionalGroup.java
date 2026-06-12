package uo.ri.cws.application.service.payroll.crud.command;

import java.util.List;
import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.PayrollRepository;
import uo.ri.cws.application.repository.ProfessionalGroupRepository;
import uo.ri.cws.application.service.payroll.PayrollService.PayrollSummaryDto;
import uo.ri.cws.application.service.payroll.crud.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Payroll;
import uo.ri.cws.domain.ProfessionalGroup;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class FindPayrollsByProfessionalGroup implements Command<List<PayrollSummaryDto>> {

    private String groupName;
    private ProfessionalGroupRepository groupRepo = Factories.repository.forProfessionalGroup();
    private PayrollRepository payrollRepo = Factories.repository.forPayroll();

    public FindPayrollsByProfessionalGroup(String groupName) {
        ArgumentChecks.isNotBlank(groupName, "groupName cannot be blank");
        this.groupName = groupName;
    }

    @Override
    public List<PayrollSummaryDto> execute() throws BusinessException {
        Optional<ProfessionalGroup> og = groupRepo.findByName(groupName);
        BusinessChecks.exists(og, "Professional group does not exist");

        List<Payroll> payrolls = payrollRepo.findByProfessionalGroupName(groupName);
        return DtoAssembler.toSummaryDtoList(payrolls);
    }
}