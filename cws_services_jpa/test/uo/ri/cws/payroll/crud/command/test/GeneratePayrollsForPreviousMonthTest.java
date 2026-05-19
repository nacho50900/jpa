package uo.ri.cws.payroll.crud.command.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import uo.ri.cws.application.repository.ContractRepository;
import uo.ri.cws.application.repository.PayrollRepository;
import uo.ri.cws.application.service.payroll.PayrollService.PayrollDto;
import uo.ri.cws.application.service.payroll.crud.command.GeneratePayrollsForPreviousMonth;
import uo.ri.cws.domain.Contract;
import uo.ri.cws.domain.ContractType;
import uo.ri.cws.domain.Mechanic;
import uo.ri.cws.domain.Payroll;
import uo.ri.cws.domain.ProfessionalGroup;
import uo.ri.util.exception.BusinessException;

/**
 * Unit tests for GeneratePayrollsForPreviousMonth command.
 * Uses Mockito to simulate dependencies (repositories and domain objects).
 */
class GeneratePayrollsForPreviousMonthTest {

    private GeneratePayrollsForPreviousMonth command;
    private ContractRepository contractRepo;
    private PayrollRepository payrollRepo;
    private LocalDate referenceDate;

    @BeforeEach
    void setUp() {
        contractRepo = mock(ContractRepository.class);
        payrollRepo = mock(PayrollRepository.class);
        referenceDate = LocalDate.of(2024, 3, 15);
        
        // Inject mocked repositories via constructor
        command = new GeneratePayrollsForPreviousMonth(
            referenceDate, contractRepo, payrollRepo);
    }

    @Test
    void testGeneratePayrollsForActiveContracts() throws BusinessException {
        // Given: Two active contracts
        Contract contract1 = createMockContract("M1", LocalDate.of(2023, 1, 1), null);
        Contract contract2 = createMockContract("M2", LocalDate.of(2023, 6, 1), null);
        
        when(contractRepo.findAllInForce())
            .thenReturn(Arrays.asList(contract1, contract2));
        
        when(contract1.isActiveInMonth(any())).thenReturn(true);
        when(contract1.hasPayrollForMonth(any())).thenReturn(false);
        
        when(contract2.isActiveInMonth(any())).thenReturn(true);
        when(contract2.hasPayrollForMonth(any())).thenReturn(false);
        
        Payroll payroll1 = createMockPayroll(contract1, LocalDate.of(2024, 2, 28));
        Payroll payroll2 = createMockPayroll(contract2, LocalDate.of(2024, 2, 28));
        
        when(contract1.generatePayrollForMonth(any())).thenReturn(payroll1);
        when(contract2.generatePayrollForMonth(any())).thenReturn(payroll2);
        
        // When: Execute command
        List<PayrollDto> result = command.execute();
        
        // Then: Two payrolls should be generated
        assertEquals(2, result.size());
        verify(payrollRepo, times(2)).add(any(Payroll.class));
        verify(contract1).generatePayrollForMonth(LocalDate.of(2024, 2, 1));
        verify(contract2).generatePayrollForMonth(LocalDate.of(2024, 2, 1));
    }

    @Test
    void testNoPayrollsGeneratedWhenNoActiveContracts() throws BusinessException {
        // Given: No active contracts
        when(contractRepo.findAllInForce()).thenReturn(Collections.emptyList());
        
        // When: Execute command
        List<PayrollDto> result = command.execute();
        
        // Then: No payrolls generated
        assertTrue(result.isEmpty());
        verify(payrollRepo, never()).add(any(Payroll.class));
    }

    @Test
    void testPayrollNotGeneratedWhenAlreadyExists() throws BusinessException {
        // Given: Contract with existing payroll for the month
        Contract contract = createMockContract("M1", LocalDate.of(2023, 1, 1), null);
        
        when(contractRepo.findAllInForce()).thenReturn(Arrays.asList(contract));
        when(contract.isActiveInMonth(any())).thenReturn(true);
        when(contract.hasPayrollForMonth(any())).thenReturn(true); // Already exists
        
        // When: Execute command
        List<PayrollDto> result = command.execute();
        
        // Then: No payroll generated
        assertTrue(result.isEmpty());
        verify(contract, never()).generatePayrollForMonth(any());
        verify(payrollRepo, never()).add(any(Payroll.class));
    }

    @Test
    void testPayrollNotGeneratedForInactiveContract() throws BusinessException {
        // Given: Contract not active in previous month
        Contract contract = createMockContract("M1", 
            LocalDate.of(2024, 3, 1), null); // Started after previous month
        
        when(contractRepo.findAllInForce()).thenReturn(Arrays.asList(contract));
        when(contract.isActiveInMonth(any())).thenReturn(false); // Not active
        when(contract.hasPayrollForMonth(any())).thenReturn(false);
        
        // When: Execute command
        List<PayrollDto> result = command.execute();
        
        // Then: No payroll generated
        assertTrue(result.isEmpty());
        verify(contract, never()).generatePayrollForMonth(any());
        verify(payrollRepo, never()).add(any(Payroll.class));
    }

    @Test
    void testPayrollGeneratedForCorrectMonth() throws BusinessException {
        // Given: Active contract
        Contract contract = createMockContract("M1", LocalDate.of(2023, 1, 1), null);
        Payroll payroll = createMockPayroll(contract, LocalDate.of(2024, 2, 28));
        
        when(contractRepo.findAllInForce()).thenReturn(Arrays.asList(contract));
        when(contract.isActiveInMonth(any())).thenReturn(true);
        when(contract.hasPayrollForMonth(any())).thenReturn(false);
        when(contract.generatePayrollForMonth(any())).thenReturn(payroll);
        
        // When: Execute command with reference date March 15, 2024
        command.execute();
        
        // Then: Payroll generated for February 2024 (previous month)
        ArgumentCaptor<LocalDate> dateCaptor = ArgumentCaptor.forClass(LocalDate.class);
        verify(contract).generatePayrollForMonth(dateCaptor.capture());
        
        LocalDate capturedDate = dateCaptor.getValue();
        assertEquals(2, capturedDate.getMonthValue());
        assertEquals(2024, capturedDate.getYear());
    }

    @Test
    void testMixedScenario() throws BusinessException {
        // Given: Multiple contracts with different states
        Contract activeContract = createMockContract("M1", 
            LocalDate.of(2023, 1, 1), null);
        Contract inactiveContract = createMockContract("M2", 
            LocalDate.of(2024, 3, 1), null);
        Contract contractWithPayroll = createMockContract("M3", 
            LocalDate.of(2023, 1, 1), null);
        
        when(contractRepo.findAllInForce())
            .thenReturn(Arrays.asList(
                activeContract, inactiveContract, contractWithPayroll));
        
        // Active contract: should generate payroll
        when(activeContract.isActiveInMonth(any())).thenReturn(true);
        when(activeContract.hasPayrollForMonth(any())).thenReturn(false);
        Payroll payroll1 = createMockPayroll(activeContract, LocalDate.of(2024, 2, 28));
        when(activeContract.generatePayrollForMonth(any())).thenReturn(payroll1);
        
        // Inactive contract: should not generate payroll
        when(inactiveContract.isActiveInMonth(any())).thenReturn(false);
        when(inactiveContract.hasPayrollForMonth(any())).thenReturn(false);
        
        // Contract with existing payroll: should not generate
        when(contractWithPayroll.isActiveInMonth(any())).thenReturn(true);
        when(contractWithPayroll.hasPayrollForMonth(any())).thenReturn(true);
        
        // When: Execute command
        List<PayrollDto> result = command.execute();
        
        // Then: Only one payroll generated
        assertEquals(1, result.size());
        verify(payrollRepo, times(1)).add(any(Payroll.class));
        verify(activeContract).generatePayrollForMonth(any());
        verify(inactiveContract, never()).generatePayrollForMonth(any());
        verify(contractWithPayroll, never()).generatePayrollForMonth(any());
    }

    @Test
    void testRepositoryInteractionOrder() throws BusinessException {
        // Given: One active contract
        Contract contract = createMockContract("M1", LocalDate.of(2023, 1, 1), null);
        Payroll payroll = createMockPayroll(contract, LocalDate.of(2024, 2, 28));
        
        when(contractRepo.findAllInForce()).thenReturn(Arrays.asList(contract));
        when(contract.isActiveInMonth(any())).thenReturn(true);
        when(contract.hasPayrollForMonth(any())).thenReturn(false);
        when(contract.generatePayrollForMonth(any())).thenReturn(payroll);
        
        // When: Execute command
        command.execute();
        
        // Then: Verify interaction order
        var inOrder = inOrder(contractRepo, contract, payrollRepo);
        inOrder.verify(contractRepo).findAllInForce();
        inOrder.verify(contract).isActiveInMonth(any());
        inOrder.verify(contract).hasPayrollForMonth(any());
        inOrder.verify(contract).generatePayrollForMonth(any());
        inOrder.verify(payrollRepo).add(payroll);
    }

    @Test
    void testExceptionPropagation() throws BusinessException {
        // Given: Contract that throws exception when generating payroll
        Contract contract = createMockContract("M1", LocalDate.of(2023, 1, 1), null);
        
        when(contractRepo.findAllInForce()).thenReturn(Arrays.asList(contract));
        when(contract.isActiveInMonth(any())).thenReturn(true);
        when(contract.hasPayrollForMonth(any())).thenReturn(false);
        when(contract.generatePayrollForMonth(any()))
            .thenThrow(new IllegalStateException("Cannot generate payroll"));
        
        // When/Then: Exception should propagate
        assertThrows(IllegalStateException.class, () -> command.execute());
        verify(payrollRepo, never()).add(any(Payroll.class));
    }
    
    @Test
    void testRepositoryExceptionPropagation() throws BusinessException {
        // Given: Repository that throws exception
        when(contractRepo.findAllInForce())
            .thenThrow(new RuntimeException("Database connection error"));
        
        // When/Then: Exception should propagate
        assertThrows(RuntimeException.class, () -> command.execute());
        verify(payrollRepo, never()).add(any(Payroll.class));
    }

    // Helper method to create mock contracts
    private Contract createMockContract(String mechanicNif, 
            LocalDate startDate, LocalDate endDate) {
        
        Contract contract = mock(Contract.class);
        Mechanic mechanic = mock(Mechanic.class);
        ContractType type = mock(ContractType.class);
        ProfessionalGroup group = mock(ProfessionalGroup.class);
        
        when(mechanic.getNif()).thenReturn(mechanicNif);
        when(mechanic.getId()).thenReturn("mechanic-" + mechanicNif);
        when(contract.getId()).thenReturn("contract-" + mechanicNif);
        when(contract.getMechanic()).thenReturn(mechanic);
        when(contract.getStartDate()).thenReturn(startDate);
        when(contract.getEndDate()).thenReturn(endDate);
        when(contract.getContractType()).thenReturn(type);
        when(contract.getProfessionalGroup()).thenReturn(group);
        when(contract.getAnnualBaseSalary()).thenReturn(30000.0);
        when(contract._getPayrolls()).thenReturn(new HashSet<>());
        
        return contract;
    }

    // Helper method to create mock payrolls
    private Payroll createMockPayroll(Contract contract, LocalDate date) {
        Payroll payroll = mock(Payroll.class);
        when(payroll.getId()).thenReturn("payroll-" + date.toString());
        when(payroll.getContract()).thenReturn(contract);
        when(payroll.getDate()).thenReturn(date);
        when(payroll.getMonthlyBaseSalary()).thenReturn(2142.86);
        when(payroll.getExtraSalary()).thenReturn(0.0);
        when(payroll.getProductivityEarning()).thenReturn(0.0);
        when(payroll.getTrienniumEarning()).thenReturn(0.0);
        when(payroll.getTaxDeduction()).thenReturn(514.29);
        when(payroll.getNicDeduction()).thenReturn(125.0);
        when(payroll.getGrossSalary()).thenReturn(2142.86);
        when(payroll.getNetSalary()).thenReturn(1503.57);
        return payroll;
    }
}