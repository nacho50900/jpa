package uo.ri.cws.application.service.payroll.crud;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.payroll.PayrollService;
import uo.ri.cws.application.service.payroll.crud.command.DeleteAllLastPayrolls;
import uo.ri.cws.application.service.payroll.crud.command.DeleteLastPayrollOfMechanic;
import uo.ri.cws.application.service.payroll.crud.command.FindAllPayrollsSummarized;
import uo.ri.cws.application.service.payroll.crud.command.FindPayrollById;
import uo.ri.cws.application.service.payroll.crud.command.FindPayrollsByMechanicId;
import uo.ri.cws.application.service.payroll.crud.command.FindPayrollsByProfessionalGroup;
import uo.ri.cws.application.service.payroll.crud.command.GeneratePayrollsForPreviousMonth;
import uo.ri.cws.application.util.command.CommandExecutor;
import uo.ri.util.exception.BusinessException;

public class PayrollServiceImpl implements PayrollService {
	
	private CommandExecutor executor = Factories.executor.forExecutor();

	@Override
	public List<PayrollDto> generateForPreviousMonth() throws BusinessException {
		return executor.execute(new GeneratePayrollsForPreviousMonth(LocalDate.now()));
	}

	@Override
	public List<PayrollDto> generateForPreviousMonthOf(LocalDate present) throws BusinessException {
		return executor.execute(new GeneratePayrollsForPreviousMonth(present));
	}

	@Override
	public void deleteLastGeneratedOfMechanicId(String mechanicId) throws BusinessException {
		executor.execute(new DeleteLastPayrollOfMechanic(mechanicId));
	}

	@Override
	public int deleteLastGenerated() throws BusinessException {
		return executor.execute(new DeleteAllLastPayrolls());
	}

	@Override
	public Optional<PayrollDto> findById(String id) throws BusinessException {
		return executor.execute(new FindPayrollById(id));
	}

	@Override
	public List<PayrollSummaryDto> findAllSummarized() throws BusinessException {
		return executor.execute(new FindAllPayrollsSummarized());
	}

	@Override
	public List<PayrollSummaryDto> findSummarizedByMechanicId(String id) throws BusinessException {
		return executor.execute(new FindPayrollsByMechanicId(id));
	}

	@Override
	public List<PayrollSummaryDto> findSummarizedByProfessionalGroupName(String name) throws BusinessException {
		return executor.execute(new FindPayrollsByProfessionalGroup(name));
	}

}