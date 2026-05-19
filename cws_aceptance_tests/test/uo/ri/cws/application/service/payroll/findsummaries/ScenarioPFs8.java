package uo.ri.cws.application.service.payroll.findsummaries;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.payroll.PayrollService;
import uo.ri.cws.application.service.payroll.PayrollService.PayrollSummaryDto;
import uo.ri.cws.application.service.util.Asserts;
import uo.ri.cws.application.service.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.util.dbfixture.records.TPayrollsRecord;
import uo.ri.cws.application.service.util.dbfixture.records.TProfessionalGroupsRecord;
import uo.ri.util.exception.BusinessException;

/**
 * Feature: Find payrolls all scenarios
 * Scenario: [P.Fs.8] Find payrolls for a professional group with one payroll
 */
public class ScenarioPFs8 {
	private PayrollService service = Factories.service.forPayrollService();
	private TProfessionalGroupsRecord professionalGroup;
	private TPayrollsRecord payroll;
	private List<PayrollSummaryDto> result;

	@Given("[P.Fs.8] a payroll from a contract with a professional group")
	public void givenAPayrollFromAContractWithAProfessionalGroup() {
		professionalGroup = DbFixtures.aProfessionalGroup();
		payroll = DbFixtures.aPayrollWithContractForProfGroup(professionalGroup.id);
	}

	@When("[P.Fs.8] I search payrolls by that professional group")
	public void whenISearchPayrollsByThatProfessionalGroup() throws BusinessException {
		result = service.findSummarizedByProfessionalGroupName(professionalGroup.name);
	}

	@Then("[P.Fs.8] the right payroll summary for that professional group is found")
	public void thenThePayrollSummaryForThatProfessionalGroupIsFound() {
		assertEquals(1, result.size());
		PayrollSummaryDto dto = result.get(0);
		
		Asserts.rightPayrollSummary(payroll, dto);
	}

}