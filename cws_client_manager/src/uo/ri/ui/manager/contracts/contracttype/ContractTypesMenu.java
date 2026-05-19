package uo.ri.ui.manager.contracts.contracttype;

import uo.ri.ui.manager.contracts.contracttype.action.AddContractTypeAction;
import uo.ri.ui.manager.contracts.contracttype.action.DeleteContractTypeAction;
import uo.ri.ui.manager.contracts.contracttype.action.FindContractTypeByNameAction;
import uo.ri.ui.manager.contracts.contracttype.action.ListAllContractTypesAction;
import uo.ri.ui.manager.contracts.contracttype.action.UpdateContractTypeAction;
import uo.ri.util.menu.BaseMenu;

public class ContractTypesMenu extends BaseMenu {{
	menuOptions = new Object[][] { 
		{"Manager > Contract management > Contract types", null},
		
		{ "Add contract type", 			AddContractTypeAction.class }, 
		{ "Update contract type", 		UpdateContractTypeAction.class }, 
		{ "Delete contract type", 		DeleteContractTypeAction.class }, 
		{ "Find contract type by name", FindContractTypeByNameAction.class },
		{ "List all contract types", 	ListAllContractTypesAction.class },
	};
}}