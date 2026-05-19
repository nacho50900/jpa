package uo.ri.cws.application.service.payroll.findsummaries;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.payroll.PayrollService;
import uo.ri.cws.application.service.payroll.PayrollService.PayrollSummaryDto;
import uo.ri.cws.application.service.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.util.dbfixture.records.TProfessionalGroupsRecord;
import uo.ri.util.exception.BusinessException;

/**
 * Feature: Find payrolls all scenarios
 * Scenario: [P.Fs.10] Find payrolls for a professional group with no payroll
 */
public class ScenarioPFs10 {
	private PayrollService service = Factories.service.forPayrollService();
	private TProfessionalGroupsRecord professionalGroup;
	private List<PayrollSummaryDto> result;

	@Given("[P.Fs.10] a professional group with no payrolls")
	public void givenAProfessionalGroupWithNoPayrolls() {
		professionalGroup = DbFixtures.aProfessionalGroup();
	}

	@When("[P.Fs.10] I search payrolls for that professional group")
	public void whenISearchPayrollsForThatProfessionalGroup() throws BusinessException {
		result = service.findSummarizedByProfessionalGroupName(professionalGroup.name);
	}

	@Then("[P.Fs.10] the result is empty")
	public void thenTheResultIsEmpty() {
		assertTrue(result.isEmpty());
	}
}