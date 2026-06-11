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

/**
 * Association mapping tests for the Employs relationship:
 * Mechanic - Contract - ContractType - ProfessionalGroup
 */
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

        mechanic     = new Mechanic("44444444D", "Bob", "Builder");
        contractType = new ContractType("PERMANENT_E", 1.35);
        group        = new ProfessionalGroup("GroupIII", 33.44, 0.03);
        contract     = new Contract(mechanic, contractType, group,
                LocalDate.now(), 22000.0);

        unitOfWork.persist(mechanic, contractType, group, contract);
    }

    @AfterEach
    void tearDown() {
        unitOfWork.remove(contract, mechanic, contractType, group);
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
     * Creating a second contract for the same mechanic terminates the first one.
     */
    @Test
    void testNewContractTerminatesPrevious() {
        ContractType type2 = new ContractType("SEASONAL_E", 3.25);
        Contract contract2 = new Contract(mechanic, type2, group,
                LocalDate.now().plusMonths(1), 25000.0);
        unitOfWork.persist(type2, contract2);

        Mechanic restored = unitOfWork.findById(Mechanic.class, mechanic.getId());
        assertTrue(restored.getContracts().stream()
                .anyMatch(c -> c.isInForce()));
        assertTrue(restored.getContracts().stream()
                .anyMatch(c -> c.isTerminated()));

        unitOfWork.remove(contract2, type2);
    }

}
