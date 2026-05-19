package uo.ri.cws.infrastructure.persistence.jpa.repository;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.repository.PayrollRepository;
import uo.ri.cws.domain.Payroll;
import uo.ri.cws.infrastructure.persistence.jpa.util.BaseJpaRepository;
import uo.ri.cws.infrastructure.persistence.jpa.util.Jpa;

public class PayrollJpaRepository
        extends BaseJpaRepository<Payroll>
        implements PayrollRepository {

    @Override
    public List<Payroll> findByContract(String contractId) {
        return Jpa.getManager()
            .createNamedQuery(
            		"Payroll.findByContract",
            		Payroll.class
            )
            .setParameter("cid", contractId)
            .getResultList();
    }

    @Override
    public List<Payroll> findLastMonthPayrolls() {
        LocalDate now = LocalDate.now();
        LocalDate start = now.minusMonths(1).with(TemporalAdjusters.firstDayOfMonth());
        LocalDate end   = now.minusMonths(1).with(TemporalAdjusters.lastDayOfMonth());

        return Jpa.getManager()
            .createNamedQuery(
            		"Payroll.findLastMonthPayrolls",
            		Payroll.class
            )
            .setParameter("start", start)
            .setParameter("end", end)
            .getResultList();
    }

    @Override
    public Optional<Payroll> findLastPayrollByMechanicId(String mechanicId) {
        return Jpa.getManager()
            .createNamedQuery(
            		"Payroll.findLastPayrollByMechanicId",
            		Payroll.class
            )
            .setParameter("mid", mechanicId)
            .setMaxResults(1)
            .getResultStream()
            .findFirst();
    }

    @Override
    public List<Payroll> findByProfessionalGroupName(String name) {
        return Jpa.getManager()
            .createNamedQuery(
            		"Payroll.findByProfessionalGroupName",
            		Payroll.class
            )
            .setParameter("name", name)
            .getResultList();
    }

    @Override
    public List<Payroll> findByMechanicId(String mId) {
        return Jpa.getManager()
            .createNamedQuery(
            		"Payroll.findByMechanicId",
            		Payroll.class
            )
            .setParameter("mid", mId)
            .getResultList();
    }

    @Override
    public Optional<Payroll> findByContractIdAndDate(String id, LocalDate date) {
        return Jpa.getManager()
            .createNamedQuery(
            		"Payroll.findByContractIdAndDate",
            		Payroll.class
            )
            .setParameter("cid", id)
            .setParameter("date", date)
            .getResultStream()
            .findFirst();
    }
}