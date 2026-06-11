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
import uo.ri.cws.domain.ProfessionalGroup;
import uo.ri.cws.persistence.util.UnitOfWork;

/**
 * Persistence mapping tests for Contract entity.
 */
class ContractMappingTests {

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

        mechanic     = new Mechanic("11111111A", "John", "Doe");
        contractType = new ContractType("PERMANENT", 1.35);
        group        = new ProfessionalGroup("GroupI", 46.74, 0.05);
        contract     = new Contract(mechanic, contractType, group,
                LocalDate.now(), 24000.0);

        unitOfWork.persist(mechanic, contractType, group, contract);
    }

    @AfterEach
    void tearDown() {
        unitOfWork.remove(contract, mechanic, contractType, group);
        factory.close();
    }

    /**
     * All natural fields of Contract are persisted properly.
     */
    @Test
    void testAllFieldsPersisted() {
        Contract restored = unitOfWork.findById(Contract.class, contract.getId());

        assertEquals(contract.getId(), restored.getId());
        assertEquals(contract.getAnnualBaseSalary(),
                restored.getAnnualBaseSalary(), 0.001);
        assertEquals(contract.getTaxRate(), restored.getTaxRate(), 0.001);
        assertEquals(contract.getSettlement(), restored.getSettlement(), 0.001);
        assertEquals(contract.getState(), restored.getState());
        assertNotNull(restored.getStartDate());
    }

    /**
     * Contract recovers its mechanic, contract type and professional group.
     */
    @Test
    void testContractRecoversAssociations() {
        Contract restored = unitOfWork.findById(Contract.class, contract.getId());

        assertEquals(mechanic,     restored.getMechanic());
        assertEquals(contractType, restored.getContractType());
        assertEquals(group,        restored.getProfessionalGroup());
    }

    /**
     * A new FIXED_TERM contract has an end date adjusted to last day of month.
     */
    @Test
    void testFixedTermContractEndDateAdjusted() {
        ContractType fixedType = new ContractType("FIXED_TERM", 4.2);
        LocalDate endDate = LocalDate.now().plusMonths(6);
        Contract fixed = new Contract(
                new Mechanic("22222222B", "Jane", "Smith"),
                fixedType, group,
                LocalDate.now(), endDate, 18000.0);

        unitOfWork.persist(fixedType, fixed.getMechanic(), fixed);

        Contract restored = unitOfWork.findById(Contract.class, fixed.getId());

        assertEquals(endDate.withDayOfMonth(endDate.lengthOfMonth()),
                restored.getEndDate());

        unitOfWork.remove(fixed, fixed.getMechanic(), fixedType);
    }

}
