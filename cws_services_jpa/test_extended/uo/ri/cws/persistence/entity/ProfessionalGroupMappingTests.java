package uo.ri.cws.persistence.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceException;
import uo.ri.cws.domain.ProfessionalGroup;
import uo.ri.cws.persistence.util.UnitOfWork;

/**
 * Persistence mapping tests for ProfessionalGroup entity.
 */
class ProfessionalGroupMappingTests {

    private ProfessionalGroup group;
    private UnitOfWork unitOfWork;
    private EntityManagerFactory factory;

    @BeforeEach
    void setUp() {
        factory = Persistence.createEntityManagerFactory("carworkshop");
        unitOfWork = UnitOfWork.over(factory);

        group = new ProfessionalGroup("GroupI", 46.74, 0.05);
    }

    @AfterEach
    void tearDown() {
        unitOfWork.remove(group);
        factory.close();
    }

    /**
     * All fields of ProfessionalGroup are persisted properly.
     */
    @Test
    void testAllFieldsPersisted() {
        unitOfWork.persist(group);

        ProfessionalGroup restored = unitOfWork.findById(
                ProfessionalGroup.class, group.getId());

        assertEquals(group.getId(), restored.getId());
        assertEquals(group.getName(), restored.getName());
        assertEquals(group.getTrienniumPayment(),
                restored.getTrienniumPayment(), 0.001);
        assertEquals(group.getProductivityRate(),
                restored.getProductivityRate(), 0.001);
    }

    /**
     * Two professional groups with the same name cannot coexist.
     */
    @Test
    void testRepeatedNameNotAllowed() {
        unitOfWork.persist(group);
        ProfessionalGroup repeated = new ProfessionalGroup("GroupI", 10.0, 0.01);

        assertThrows(PersistenceException.class,
                () -> unitOfWork.persist(repeated));
    }

}
