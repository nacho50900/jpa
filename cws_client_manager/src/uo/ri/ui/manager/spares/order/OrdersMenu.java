package uo.ri.ui.manager.spares.order;

import uo.ri.util.menu.BaseMenu;
import uo.ri.util.menu.NotYetImplementedAction;

public class OrdersMenu extends BaseMenu {

	public OrdersMenu() {
		menuOptions = new Object[][] {
			{"Manager > Parts management > Orders", null},

			{ "Generate", 			NotYetImplementedAction.class },
			{ "View order detail", 	NotYetImplementedAction.class },
			{ "List by provider", 	NotYetImplementedAction.class },
			{ "Receive order", 		NotYetImplementedAction.class },
		};
	}

}
