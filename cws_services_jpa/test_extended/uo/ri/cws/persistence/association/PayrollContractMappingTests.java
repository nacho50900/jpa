package uo.ri.cws.persistence.association;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

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

class PayrollContractMappingTests {

    private UnitOfWork unitOfWork;
    private EntityManagerFactory factory;

    private Mechanic mechanic;
    private ContractType contractType;
    private ProfessionalGroup group;
    private Contract contract;
    private Payroll payroll;

    @BeforeEach
    void setUp() {
        factory = Persistence.createEntityManagerFactory("carworkshop");
        unitOfWork = UnitOfWork.over(factory);

        mechanic     = new Mechanic("X9999996Z", "Test6", "Mechanic6");
        contractType = new ContractType("TEST_PERM_PCM", 1.35);
        group        = new ProfessionalGroup("TEST_GRP_PCM", 28.85, 0.035);

        // Contract started 3 months ago — payroll for last month is valid
        contract = new Contract(mechanic, contractType, group,
                LocalDate.now().minusMonths(3), 18000.0);

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
     * A contract recovers its payrolls.
     * Note: Contract uses FetchType.EAGER for payrolls so they are loaded.
     */
    @Test
    void testContractRecoversPayrolls() {
        Contract restored = unitOfWork.findById(Contract.class, contract.getId());

        assertFalse(restored.getPayrolls().isEmpty());
        assertEquals(1, restored.getPayrolls().size());
    }

    /**
     * A payroll recovers its contract.
     */
    @Test
    void testPayrollRecoversContract() {
        Payroll restored = unitOfWork.findById(Payroll.class, payroll.getId());
        assertEquals(contract, restored.getContract());
    }

    /**
     * Two payrolls for the same contract in different months are both persisted.
     */
    @Test
    void testTwoPayrollsForSameContract() {
        // Generate payroll for two months ago (also valid)
        LocalDate twoMonthsAgo = LocalDate.now().minusMonths(2);
        Payroll payroll2 = contract.generatePayrollForMonth(twoMonthsAgo);
        unitOfWork.persist(payroll2);

        Contract restored = unitOfWork.findById(Contract.class, contract.getId());
        assertEquals(2, restored.getPayrolls().size());

        // Cleanup
        unitOfWork.remove(payroll2);
    }

}
