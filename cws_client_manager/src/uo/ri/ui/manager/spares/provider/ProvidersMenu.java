package uo.ri.ui.manager.spares.provider;

import uo.ri.util.menu.BaseMenu;
import uo.ri.util.menu.NotYetImplementedAction;

public class ProvidersMenu extends BaseMenu {

	public ProvidersMenu() {
		menuOptions = new Object[][] {
			{"Manager > Parts management > Providers", null},

			{ "Add", 					NotYetImplementedAction.class },
			{ "Update", 				NotYetImplementedAction.class },
			{ "Remove", 				NotYetImplementedAction.class },
			{ "View provider detail", 	NotYetImplementedAction.class },
			{ "List by name", 			NotYetImplementedAction.class },
			{ "List by spare part code", NotYetImplementedAction.class },
		};
	}

}
