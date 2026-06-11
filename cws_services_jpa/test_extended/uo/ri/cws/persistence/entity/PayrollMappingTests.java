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

        mechanic     = new Mechanic("X9999993Z", "Test3", "Mechanic3");
        contractType = new ContractType("TEST_PERM_PM", 1.35);
        group        = new ProfessionalGroup("TEST_GRP_PM", 38.12, 0.045);

        // Contract started 3 months ago so last month payroll is valid
        contract = new Contract(mechanic, contractType, group,
                LocalDate.now().minusMonths(3), 20000.0);

        // Generate payroll for last month (safe: contract started 3 months ago)
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
     * All stored fields of Payroll are persisted properly.
     * Note: grossSalary, totalDeductions, netSalary are computed (not stored).
     */
    @Test
    void testAllStoredFieldsPersisted() {
        Payroll restored = unitOfWork.findById(Payroll.class, payroll.getId());

        assertEquals(payroll.getId(), restored.getId());
        assertNotNull(restored.getDate());
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
    }

    /**
     * Payroll date is set to last day of the month.
     */
    @Test
    void testPayrollDateIsLastDayOfMonth() {
        Payroll restored = unitOfWork.findById(Payroll.class, payroll.getId());
        LocalDate date = restored.getDate();
        assertEquals(date.lengthOfMonth(), date.getDayOfMonth());
    }

    /**
     * Payroll recovers its contract.
     */
    @Test
    void testPayrollRecoversContract() {
        Payroll restored = unitOfWork.findById(Payroll.class, payroll.getId());
        assertEquals(contract, restored.getContract());
    }

    /**
     * Computed fields are derived correctly from stored ones.
     */
    @Test
    void testComputedFieldsConsistency() {
        Payroll restored = unitOfWork.findById(Payroll.class, payroll.getId());

        double expectedGross = restored.getMonthlyBaseSalary()
                + restored.getExtraSalary()
                + restored.getProductivityEarning()
                + restored.getTrienniumEarning();

        double expectedDeductions = restored.getTaxDeduction()
                + restored.getNicDeduction();

        assertEquals(expectedGross, restored.getGrossSalary(), 0.001);
        assertEquals(expectedDeductions, restored.getTotalDeductions(), 0.001);
        assertEquals(expectedGross - expectedDeductions,
                restored.getNetSalary(), 0.001);
    }

}
