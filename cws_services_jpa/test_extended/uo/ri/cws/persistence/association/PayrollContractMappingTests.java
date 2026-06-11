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

/**
 * Association mapping tests for the Payroll - Contract relationship.
 */
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

        mechanic     = new Mechanic("55555555E", "Carol", "Vorderman");
        contractType = new ContractType("PERMANENT_PC", 1.35);
        group        = new ProfessionalGroup("GroupIV", 28.85, 0.035);

        LocalDate startDate = LocalDate.now().minusMonths(3);
        contract = new Contract(mechanic, contractType, group,
                startDate, 18000.0);

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
     * Two payrolls can exist for the same contract in different months.
     */
    @Test
    void testTwoPayrollsForSameContract() {
        LocalDate twoMonthsAgo = LocalDate.now().minusMonths(2);
        Payroll payroll2 = contract.generatePayrollForMonth(twoMonthsAgo);
        unitOfWork.persist(payroll2);

        Contract restored = unitOfWork.findById(Contract.class, contract.getId());
        assertEquals(2, restored.getPayrolls().size());

        unitOfWork.remove(payroll2);
    }

}
