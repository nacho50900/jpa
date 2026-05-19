package uo.ri.cws.application.service.contracttype.add;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.contracttype.ContractTypeCrudService;
import uo.ri.cws.application.service.contracttype.ContractTypeCrudService.ContractTypeDto;
import uo.ri.cws.application.service.util.Asserts;
import uo.ri.cws.application.service.util.dbfixture.Db;
import uo.ri.cws.application.service.util.dbfixture.records.TContractTypesRecord;
import uo.ri.cws.application.service.util.dtobuilders.ContractTypeDtoBuilder;
import uo.ri.util.exception.BusinessException;

/**
 * Feature: Add a new contract type
 * Scenario: [Ct.A.1] Add a new contract type
 */
public class ScenarioCtA1 {
	private ContractTypeCrudService service = Factories.service.forContractTypeCrudService();
	private ContractTypeDto newContractType;
	private ContractTypeDto registered;

	@When("[Ct.A.1] I register a new contract type")
	public void whenIRegisterANewContractType() throws BusinessException {
		newContractType = new ContractTypeDtoBuilder().build();
		registered = service.create(newContractType);
	}

	@Then("[Ct.A.1] The contract type is added")
	public void thenTheContractTypeIsAdded() {
		// checks on the returned object
		assertNotNull( registered.id );
		assertFalse( registered.id.isEmpty() );
		assertEquals( newContractType.name, registered.name );
		assertEquals( newContractType.compensationDays, registered.compensationDays );
		
		// checks on the database
		TContractTypesRecord loaded = Db.findById(TContractTypesRecord.class, registered.id);
		Asserts.recordMatchesDto(registered, loaded);
		Asserts.isNow(loaded.createdAt);
		Asserts.isNow(loaded.updatedAt);
	}
}