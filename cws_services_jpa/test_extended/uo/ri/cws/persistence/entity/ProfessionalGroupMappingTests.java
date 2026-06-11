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

class ProfessionalGroupMappingTests {

    private ProfessionalGroup group;
    private UnitOfWork unitOfWork;
    private EntityManagerFactory factory;

    @BeforeEach
    void setUp() {
        factory = Persistence.createEntityManagerFactory("carworkshop");
        unitOfWork = UnitOfWork.over(factory);
        // Use unique name to avoid clashing with pre-loaded DB data
        group = new ProfessionalGroup("TEST_GROUP_VIII", 46.74, 0.05);
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

        // ProfessionalGroup overrides getId() from BaseEntity with its own @Id
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
        ProfessionalGroup repeated =
                new ProfessionalGroup("TEST_GROUP_VIII", 10.0, 0.01);

        assertThrows(PersistenceException.class,
                () -> unitOfWork.persist(repeated));
    }

    /**
     * setTrienniumPayment and setProductivityRate work correctly.
     */
    @Test
    void testSetters() {
    	unitOfWork.persist(group);
    	
        group.setTrienniumPayment(99.0);
        group.setProductivityRate(0.10);

        assertEquals(99.0, group.getTrienniumPayment(), 0.001);
        assertEquals(0.10, group.getProductivityRate(), 0.001);
    }

}
