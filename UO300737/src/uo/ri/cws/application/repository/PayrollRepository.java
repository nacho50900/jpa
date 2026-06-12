package uo.ri.cws.application.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import uo.ri.cws.domain.Payroll;

public interface PayrollRepository extends Repository<Payroll> {

	List<Payroll> findByContract(String contractId);

	List<Payroll> findLastMonthPayrolls();

	Optional<Payroll> findLastPayrollByMechanicId(String mechanicId);

	Optional<Payroll> findByContractIdAndDate(String id, LocalDate date);
	
	List<Payroll> findAll();

	List<Payroll> findByProfessionalGroupName(String name);

	List<Payroll> findByMechanicId(String id);

}
