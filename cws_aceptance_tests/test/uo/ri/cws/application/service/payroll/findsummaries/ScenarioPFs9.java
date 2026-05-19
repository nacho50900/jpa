package uo.ri.cws.application.service.payroll.findsummaries;

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
 * Scenario: [P.Fs.9] Find payrolls for a professional group with several payrolls
 */
public class ScenarioPFs9 {
	private PayrollService service = Factories.service.forPayrollService();

	private TProfessionalGroupsRecord professionalGroup;
	private List<TPayrollsRecord> payrolls;
	
	private List<PayrollSummaryDto> result;

	@Given("[P.Fs.9] some payrolls of some contracts of some professional groups")
	public void givenSeveralProfessionalGroups() {
		professionalGroup = DbFixtures.aProfessionalGroup();
		payrolls = DbFixtures.somePayrollsWithContractsForProfessionalGroup( professionalGroup.id );
		DbFixtures.somePayrollsWithContractsWithProfessionalGroup();
		DbFixtures.somePayrollsWithContractsWithProfessionalGroup();
	}

	@When("[P.Fs.9] I search payrolls for one of those professional groups")
	public void whenISearchPayrollsForOneOfThoseProfessionalGroups() throws BusinessException {
		result = service.findSummarizedByProfessionalGroupName(professionalGroup.name);
	}

	@Then("[P.Fs.9] all payroll summaries for that professional group are found")
	public void thenAllPayrollSummariesForThatProfessionalGroupAreFound() {
		Asserts.rightPayrollSummaries( payrolls, result );
	}
}