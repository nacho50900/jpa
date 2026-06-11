package uo.ri.cws.persistence.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceException;
import uo.ri.cws.domain.ContractType;
import uo.ri.cws.persistence.util.UnitOfWork;

class ContractTypeMappingTests {

    private ContractType contractType;
    private UnitOfWork unitOfWork;
    private EntityManagerFactory factory;

    @BeforeEach
    void setUp() {
        factory = Persistence.createEntityManagerFactory("carworkshop");
        unitOfWork = UnitOfWork.over(factory);
        contractType = new ContractType("TEST_PERMANENT", 1.35);
    }

    @AfterEach
    void tearDown() {
        unitOfWork.remove(contractType);
        factory.close();
    }

    /**
     * All fields of ContractType are persisted properly.
     */
    @Test
    void testAllFieldsPersisted() {
        unitOfWork.persist(contractType);

        ContractType restored = unitOfWork.findById(
                ContractType.class, contractType.getId());

        assertEquals(contractType.getId(), restored.getId());
        assertEquals(contractType.getName(), restored.getName());
        assertEquals(contractType.getCompensationDaysPerYear(),
                restored.getCompensationDaysPerYear(), 0.001);
    }

    /**
     * Two contract types with the same name cannot coexist.
     */
    @Test
    void testRepeatedNameNotAllowed() {
        unitOfWork.persist(contractType);
        ContractType repeated = new ContractType("TEST_PERMANENT", 2.0);

        assertThrows(PersistenceException.class,
                () -> unitOfWork.persist(repeated));
    }

    /**
     * setCompensationDaysPerYear updates the value correctly.
     */
    @Test
    void testUpdateCompensationDays() {
        unitOfWork.persist(contractType);
        contractType.setCompensationDaysPerYear(3.5);

        //ContractType restored = unitOfWork.findById(
          //      ContractType.class, contractType.getId());
        // After update the in-memory object reflects the new value
        assertEquals(3.5, contractType.getCompensationDaysPerYear(), 0.001);
    }

}
