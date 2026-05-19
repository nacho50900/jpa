package uo.ri.ui.manager.spares;

import uo.ri.util.menu.BaseMenu;
import uo.ri.ui.manager.spares.order.OrdersMenu;
import uo.ri.ui.manager.spares.provider.ProvidersMenu;
import uo.ri.ui.manager.spares.sparepart.SparePartsMenu;
import uo.ri.ui.manager.spares.supply.SuppliesMenu;

public class SparePartsManagementMenu extends BaseMenu {

	public SparePartsManagementMenu() {
		menuOptions = new Object[][] {
			{"Manager > Parts management", null},

			{ "Spare parts management", SparePartsMenu.class },
			{ "Providers management", 	ProvidersMenu.class },
			{ "Supplies management", 	SuppliesMenu.class },
			{ "Orders management", 		OrdersMenu.class },
		};
	}

}
