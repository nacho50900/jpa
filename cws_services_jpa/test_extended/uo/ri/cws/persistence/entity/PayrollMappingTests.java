package uo.ri.cws.persistence.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import uo.ri.cws.domain.Contract;
import uo.ri.cws.domain.ContractType;
import uo.ri.cws.domain.Mechanic;
import uo.ri.cws.domain.Payroll;
import uo.ri.cws.domain.ProfessionalGroup;
import uo.ri.cws.persistence.util.UnitOfWork;

/**
 * Persistence mapping tests for Payroll entity.
 */
class PayrollMappingTests {

    private Payroll payroll;
    private Contract contract;
    private Mechanic mechanic;
    private ContractType contractType;
    private ProfessionalGroup group;
    private UnitOfWork unitOfWork;
    private EntityManagerFactory factory;

    @BeforeEach
    void setUp() {
        factory = Persistence.createEntityManagerFactory("carworkshop");
        unitOfWork = UnitOfWork.over(factory);

        mechanic     = new Mechanic("33333333C", "Alice", "Wonderland");
        contractType = new ContractType("PERMANENT_P", 1.35);
        group        = new ProfessionalGroup("GroupII", 38.12, 0.045);

        // Contract started 2 months ago so payroll date is valid
        LocalDate startDate = LocalDate.now().minusMonths(2);
        contract = new Contract(mechanic, contractType, group,
                startDate, 20000.0);

        // Generate payroll for last month
        LocalDate lastMonth = LocalDate.now().minusMonths(1);
        payroll = contract.generatePayrollForMonth(lastMonth);

        unitOfWork.persist(mechanic, contractType, group, contract, payroll);
    }

    @AfterEach
    void tearDown() {
        unitOfWork.remove(payroll, contract, mechanic, contractType, group);
        factory.close();
    }

    /**
     * All numeric fields of Payroll are persisted properly.
     */
    @Test
    void testAllFieldsPersisted() {
        Payroll restored = unitOfWork.findById(Payroll.class, payroll.getId());

        assertEquals(payroll.getId(), restored.getId());
        assertEquals(payroll.getMonthlyBaseSalary(),
                restored.getMonthlyBaseSalary(), 0.001);
        assertEquals(payroll.getExtraSalary(),
                restored.getExtraSalary(), 0.001);
        assertEquals(payroll.getProductivityEarning(),
                restored.getProductivityEarning(), 0.001);
        assertEquals(payroll.getTrienniumEarning(),
                restored.getTrienniumEarning(), 0.001);
        assertEquals(payroll.getTaxDeduction(),
                restored.getTaxDeduction(), 0.001);
        assertEquals(payroll.getNicDeduction(),
                restored.getNicDeduction(), 0.001);
        assertNotNull(restored.getDate());
    }

    /**
     * Payroll recovers its contract.
     */
    @Test
    void testPayrollRecoversContract() {
        Payroll restored = unitOfWork.findById(Payroll.class, payroll.getId());
        assertEquals(contract, restored.getContract());
    }

}
