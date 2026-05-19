package uo.ri.ui.manager.spares.sparepart;

import uo.ri.util.menu.BaseMenu;
import uo.ri.util.menu.NotYetImplementedAction;

public class SparePartsMenu extends BaseMenu {

	public SparePartsMenu() {
		menuOptions = new Object[][] {
			{"Manager > Parts management > Spare parts", null},

			{ "Add", 					NotYetImplementedAction.class },
			{ "Update", 				NotYetImplementedAction.class },
			{ "Remove", 				NotYetImplementedAction.class },
			{ "View detail", 			NotYetImplementedAction.class },
			{ "List by description", 	NotYetImplementedAction.class },
			{ "List under stock", 		NotYetImplementedAction.class },
		};
	}

}
