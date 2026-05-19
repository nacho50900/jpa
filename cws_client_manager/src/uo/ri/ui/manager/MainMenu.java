package uo.ri.ui.manager;

import uo.ri.ui.manager.contracts.ContractsManagementMenu;
import uo.ri.ui.manager.mechanic.MechanicsMenu;
import uo.ri.ui.manager.payroll.PayrollManagementMenu;
import uo.ri.ui.manager.spares.SparePartsManagementMenu;
import uo.ri.ui.manager.vehicletype.VehicleTypesMenu;
import uo.ri.util.menu.BaseMenu;

public class MainMenu extends BaseMenu {{
		menuOptions = new Object[][] {
			{ "Manager", null },

			{ "Mechanics management", 		MechanicsMenu.class },
			{ "Contracts management", 		ContractsManagementMenu.class },
			{ "Payrolls management", 		PayrollManagementMenu.class },
			{ "Spare parts management", 	SparePartsManagementMenu.class },
			{ "Vehicle types management", 	VehicleTypesMenu.class },
		};
}}