package uo.ri.cws.persistence.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

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

        // Use names/NIFs unlikely to clash with existing test DB data
        mechanic     = new Mechanic("X9999991Z", "Test", "Mechanic");
        contractType = new ContractType("TEST_PERM_CM", 1.35);
        group        = new ProfessionalGroup("TEST_GRP_CM", 46.74, 0.05);

        // annualBaseSalary must be > 0 (not >= 0)
        contract = new Contract(mechanic, contractType, group,
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
     * A PERMANENT contract has no end date.
     */
    @Test
    void testPermanentContractHasNoEndDate() {
        Contract restored = unitOfWork.findById(Contract.class, contract.getId());
        assertNull(restored.getEndDate());
    }

    /**
     * A FIXED_TERM contract end date is adjusted to last day of its month.
     */
    @Test
    void testFixedTermEndDateAdjustedToLastDayOfMonth() {
        Mechanic m2 = null;
        ContractType ft = null;
        Contract fixed = null;

        try {
            m2 = new Mechanic("X9999992Z", "Test2", "Mechanic2");
            ft = new ContractType("FIXED_TERM", 4.2);
            LocalDate endDate = LocalDate.of(2026, 12, 15);

            fixed = new Contract(m2, ft, group,
                    LocalDate.now(), endDate, 20000.0);

            unitOfWork.persist(m2, ft, fixed);

            Contract restored = unitOfWork.findById(Contract.class, fixed.getId());

            // December has 31 days, not 30
            assertEquals(LocalDate.of(2026, 12, 31), restored.getEndDate());

        } finally {
            if (fixed != null) {
				unitOfWork.remove(fixed);
			}
            if (m2 != null) {
				unitOfWork.remove(m2);
			}
            if (ft != null) {
				unitOfWork.remove(ft);
			}
        }
    }

    /**
     * Contract recovers its associations.
     */
    @Test
    void testContractRecoversAssociations() {
        Contract restored = unitOfWork.findById(Contract.class, contract.getId());

        assertEquals(mechanic,     restored.getMechanic());
        assertEquals(contractType, restored.getContractType());
        assertEquals(group,        restored.getProfessionalGroup());
    }

    /**
     * A new IN_FORCE contract starts with zero settlement.
     */
    @Test
    void testNewContractHasZeroSettlement() {
        Contract restored = unitOfWork.findById(Contract.class, contract.getId());
        assertEquals(0.0, restored.getSettlement(), 0.001);
    }

}
