package uo.ri.cws.application.service.payroll.crud.command;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.ContractRepository;
import uo.ri.cws.application.repository.PayrollRepository;
import uo.ri.cws.application.service.payroll.PayrollService.PayrollDto;
import uo.ri.cws.application.service.payroll.crud.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Contract;
import uo.ri.cws.domain.Payroll;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessException;

public class GeneratePayrollsForPreviousMonth implements Command<List<PayrollDto>> {

    private LocalDate referenceDate;
    private ContractRepository contractRepo = Factories.repository.forContract();
    private PayrollRepository payrollRepo = Factories.repository.forPayroll();

    public GeneratePayrollsForPreviousMonth(LocalDate referenceDate) {
    	ArgumentChecks.isNotNull(referenceDate, "date can not be null");
        this.referenceDate = referenceDate;
    }

    /**
     * Just for testing proposes
     */
    public GeneratePayrollsForPreviousMonth(LocalDate referenceDate,
                                            ContractRepository contractRepo,
                                            PayrollRepository payrollRepo) {
        this.referenceDate = referenceDate;
        this.contractRepo = contractRepo;
        this.payrollRepo = payrollRepo;
    }

    @Override
    public List<PayrollDto> execute() throws BusinessException {
        LocalDate previousMonth = referenceDate.minusMonths(1).withDayOfMonth(1);
        List<Contract> allContracts = contractRepo.findAllInForce();
        List<PayrollDto> generatedPayrolls = new ArrayList<>();
        
        for (Contract contract : allContracts) {
            if (shouldGeneratePayroll(contract, previousMonth)) {
                Payroll payroll = contract.generatePayrollForMonth(previousMonth);
                payrollRepo.add(payroll);
                generatedPayrolls.add(DtoAssembler.toDto(payroll));
            }
        }
        
        return generatedPayrolls;
    }
    
    //Refactored in order to delegate the checks to the Contract domain class
    //instead of here (service layer) -> much better 
    private boolean shouldGeneratePayroll(Contract contract, LocalDate month) {
        return contract.isActiveInMonth(month) && 
               !contract.hasPayrollForMonth(month);
    }
    
}