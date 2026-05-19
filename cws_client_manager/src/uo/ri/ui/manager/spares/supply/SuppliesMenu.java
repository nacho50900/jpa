package uo.ri.ui.manager.spares.supply;

import uo.ri.util.menu.BaseMenu;
import uo.ri.util.menu.NotYetImplementedAction;

public class SuppliesMenu extends BaseMenu {

	public SuppliesMenu() {
		menuOptions = new Object[][] {
			{"Manager > Parts management > Supplies", null},

			{ "Add", 				NotYetImplementedAction.class },
			{ "Update", 			NotYetImplementedAction.class },
			{ "Remove", 			NotYetImplementedAction.class },
			{ "List by provider", 	NotYetImplementedAction.class },
			{ "List by spare part", NotYetImplementedAction.class },
		};
	}

}
