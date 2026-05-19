package uo.ri.cws.application.service.payroll.find;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.payroll.PayrollService;
import uo.ri.cws.application.service.payroll.PayrollService.PayrollDto;
import uo.ri.util.exception.BusinessException;

/**
 * Feature: Find payrolls
 * Scenario: [P.F.2] Find a non existent payroll
 */
public class ScenarioPF2 {
	private PayrollService service = Factories.service.forPayrollService();
	private Optional<PayrollDto> result;

	@When("[P.F.2] I find a payroll with a non existent id")
	public void whenIFindAPayrollWithANonExistentId() throws BusinessException {
		result = service.findById("non-existent-payroll-id");
	}

	@Then("[P.F.2] nothing is found")
	public void thenNothingIsFound() {
		assertTrue(result.isEmpty());
	}
}