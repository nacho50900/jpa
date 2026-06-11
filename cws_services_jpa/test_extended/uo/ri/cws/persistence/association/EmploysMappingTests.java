package uo.ri.cws.persistence.association;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import uo.ri.cws.domain.Contract;
import uo.ri.cws.domain.ContractType;
import uo.ri.cws.domain.Mechanic;
import uo.ri.cws.domain.ProfessionalGroup;
import uo.ri.cws.persistence.util.UnitOfWork;

class EmploysMappingTests {

    private UnitOfWork unitOfWork;
    private EntityManagerFactory factory;

    private Mechanic mechanic;
    private ContractType contractType;
    private ProfessionalGroup group;
    private Contract contract;

    @BeforeEach
    void setUp() {
        factory = Persistence.createEntityManagerFactory("carworkshop");
        unitOfWork = UnitOfWork.over(factory);

        mechanic     = new Mechanic("X9999994Z", "Test4", "Mechanic4");
        contractType = new ContractType("TEST_PERM_EM", 1.35);
        group        = new ProfessionalGroup("TEST_GRP_EM", 33.44, 0.03);

        // annualBaseSalary must be > 0
        contract = new Contract(mechanic, contractType, group,
                LocalDate.now(), 22000.0);

        unitOfWork.persist(mechanic, contractType, group, contract);
    }

    @AfterEach
    void tearDown() {
        if (contract != null) {
			unitOfWork.remove(contract);
		}
        if (mechanic != null) {
			unitOfWork.remove(mechanic);
		}
        if (contractType != null) {
			unitOfWork.remove(contractType);
		}
        if (group != null) {
			unitOfWork.remove(group);
		}
        factory.close();
    }

    /**
     * A mechanic recovers its contracts.
     */
    @Test
    void testMechanicRecoversContracts() {
        Mechanic restored = unitOfWork.findById(Mechanic.class, mechanic.getId());

        assertFalse(restored.getContracts().isEmpty());
        assertEquals(1, restored.getContracts().size());
    }

    /**
     * A contract recovers its mechanic.
     */
    @Test
    void testContractRecoversMechanic() {
        Contract restored = unitOfWork.findById(Contract.class, contract.getId());
        assertEquals(mechanic, restored.getMechanic());
    }

    /**
     * A contract recovers its contract type.
     */
    @Test
    void testContractRecoversContractType() {
        Contract restored = unitOfWork.findById(Contract.class, contract.getId());
        assertEquals(contractType, restored.getContractType());
    }

    /**
     * A contract recovers its professional group.
     */
    @Test
    void testContractRecoversProfessionalGroup() {
        Contract restored = unitOfWork.findById(Contract.class, contract.getId());
        assertEquals(group, restored.getProfessionalGroup());
    }

    /**
     * A contract type recovers its contracts.
     */
    @Test
    void testContractTypeRecoversContracts() {
        ContractType restored = unitOfWork.findById(
                ContractType.class, contractType.getId());

        assertFalse(restored.getContracts().isEmpty());
        assertEquals(1, restored.getContracts().size());
    }

    /**
     * A professional group recovers its contracts.
     */
    @Test
    void testProfessionalGroupRecoversContracts() {
        ProfessionalGroup restored = unitOfWork.findById(
                ProfessionalGroup.class, group.getId());

        assertFalse(restored.getContracts().isEmpty());
        assertEquals(1, restored.getContracts().size());
    }

    /**
     * Creating a second contract for the same mechanic automatically
     * terminates the first one (via Associations.Employs.link).
     */
    @Test
    void testNewContractTerminatesPreviousOne() {
        Mechanic m2  = new Mechanic("X9999995Z", "Test5", "Mechanic5");
        ContractType ct2 = new ContractType("TEST_SEAS_EM", 3.25);
        // Second contract starts next month
        Contract contract2 = new Contract(m2, ct2, group,
                LocalDate.now().plusMonths(1), 25000.0);
        unitOfWork.persist(m2, ct2, contract2);

        // The previous contract for m2 (none) — test that contract itself is IN_FORCE
        // Now create a second contract FOR THE SAME mechanic (mechanic, not m2)
        ContractType ct3 = new ContractType("TEST_SEAS2_EM", 3.25);
        Contract contract3 = new Contract(mechanic, ct3, group,
                LocalDate.now().plusMonths(1), 26000.0);
        unitOfWork.persist(ct3, contract3);

        // The original contract should now be TERMINATED
        Contract restoredFirst = unitOfWork.findById(
                Contract.class, contract.getId());
        assertTrue(restoredFirst.isTerminated());

        // The new contract should be IN_FORCE
        Contract restoredSecond = unitOfWork.findById(
                Contract.class, contract3.getId());
        assertTrue(restoredSecond.isInForce());

        unitOfWork.remove(contract3, ct3, contract2, m2, ct2);
    }

}
