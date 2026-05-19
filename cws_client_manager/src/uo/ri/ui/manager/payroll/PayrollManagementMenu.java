package uo.ri.ui.manager.payroll;

import uo.ri.ui.manager.payroll.action.DeleteLastMonthPayrollAction;
import uo.ri.ui.manager.payroll.action.DeleteLastMonthPayrollOfMechanicAction;
import uo.ri.ui.manager.payroll.action.GeneratePayrollsAtDateAction;
import uo.ri.ui.manager.payroll.action.GeneratePayrollsTodayAction;
import uo.ri.ui.manager.payroll.action.ListPayrollsOfMechanicAction;
import uo.ri.ui.manager.payroll.action.ListPayrollsOfProfGroupAction;
import uo.ri.ui.manager.payroll.action.ShowPayrollAction;
import uo.ri.util.menu.BaseMenu;

public class PayrollManagementMenu extends BaseMenu {{
	menuOptions = new Object[][] {
		{"Manager > Payrolls management", null},

		{ "Generate payrolls at a date", 		GeneratePayrollsAtDateAction.class },
		{ "Generate payrolls today", 			GeneratePayrollsTodayAction.class },
		{ "Delete last month payrolls", 		DeleteLastMonthPayrollAction.class },
		{ "Delete last month payroll of a mechanic", 	DeleteLastMonthPayrollOfMechanicAction.class },
		{ "Find a payroll by id", 				ShowPayrollAction.class },
		{ "List all payrolls of a mechanic", 	ListPayrollsOfMechanicAction.class },
		{ "List all payrolls of a professional group", 	ListPayrollsOfProfGroupAction.class },
	};
}}
