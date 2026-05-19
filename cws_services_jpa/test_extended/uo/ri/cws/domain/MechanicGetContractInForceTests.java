package uo.ri.cws.domain;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uo.ri.util.ContractBuilder;
import uo.ri.util.MechanicBuilder;

/**
 * Scenarios:
 *  - Mechanic with one contract in force
 *  - Mechanic with no contract in force
 *  - Mechanic with two contracts, one terminated and one in force
 */
public class MechanicGetContractInForceTests {

    private Mechanic mechanic;

    @BeforeEach
    public void setUp() throws Exception {
        mechanic = new MechanicBuilder().build();
    }

    /**
     * GIVEN: A mechanic with one contract in force
     * THEN: getContractInForce() returns the active contract
     */
    @Test
    public void testOneContractInForce() {
        Contract contract = new ContractBuilder().forMechanic(mechanic).build();
        Optional<Contract> opt = mechanic.getContractInForce();

        assertTrue(opt.isPresent());
        assertTrue(opt.get() == contract);
    }

    /**
     * GIVEN: A mechanic with no contract in force
     * THEN: getContractInForce() returns Optional.empty if no contract in force
     */
    @Test
    public void testNoContractInForce() {
        Contract contract = new ContractBuilder().forMechanic(mechanic).build();
        contract.terminate(contract.getStartDate().plusDays(1));

        assertTrue(mechanic.getContractInForce().isEmpty());
    }

    /**
     * GIVEN: A mechanic with two contracts, one terminated and one in force
     * THEN: getContractInForce() returns the right contract in force
     */
    @Test
    public void testRightContractInForce() {
        LocalDate today = LocalDate.now();
        LocalDate inOneMonth = today.plusMonths(1);

        Contract contract = new ContractBuilder().forMechanic(mechanic)
                .withSigningDate(today).build();
        contract.terminate(contract.getStartDate().plusDays(1));

        Contract newContract = new ContractBuilder().forMechanic(mechanic)
                .withSigningDate(inOneMonth).build();
        
        Optional<Contract> opt = mechanic.getContractInForce();
        
        assertTrue(opt.isPresent());
        assertTrue(opt.get() == newContract);
    }

    /**
     * getContractInForce() returns the right contract in force
     */
    @Test
    public void testNewContractWithoutTerminate() {
        LocalDate today = LocalDate.now();
        LocalDate inOneMonth = today.plusMonths(1);

        Contract contract = new ContractBuilder().forMechanic(mechanic)
                .withSigningDate(today).build();

        Contract newContract = new ContractBuilder().forMechanic(mechanic)
                .withSigningDate(inOneMonth).build();
        Optional<Contract> opt = mechanic.getContractInForce();
        assertTrue(contract.isTerminated());
        assertTrue(opt.isPresent());
        assertTrue(opt.get() == newContract);
    }
}
