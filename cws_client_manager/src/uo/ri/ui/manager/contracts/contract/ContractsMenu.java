package uo.ri.ui.manager.contracts.contract;

import uo.ri.ui.manager.contracts.contract.action.AddContractAction;
import uo.ri.ui.manager.contracts.contract.action.DeleteContractAction;
import uo.ri.ui.manager.contracts.contract.action.FinishContractAction;
import uo.ri.ui.manager.contracts.contract.action.ListAllContractsAction;
import uo.ri.ui.manager.contracts.contract.action.ListContractsOfMechanicAction;
import uo.ri.ui.manager.contracts.contract.action.ShowContractDetailsAction;
import uo.ri.ui.manager.contracts.contract.action.UpdateContractAction;
import uo.ri.ui.manager.mechanic.action.ListMechanicsAction;
import uo.ri.util.menu.BaseMenu;

public class ContractsMenu extends BaseMenu {{
	menuOptions = new Object[][] { 
		{"Administrator > Contract management", null},
		
		{ "List mechanics", 	ListMechanicsAction.class },
		{ "Add contract", 		AddContractAction.class }, 
		{ "Update contract", 	UpdateContractAction.class }, 
		{ "Delete contract", 	DeleteContractAction.class }, 
		{ "Finish contract", 	FinishContractAction.class }, 
		{ "Show contract details", 	ShowContractDetailsAction.class }, 
		{ "List contracts of a mechanic", ListContractsOfMechanicAction.class },
		{ "List all contracts", ListAllContractsAction.class }
	};
}}