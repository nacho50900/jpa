package uo.ri.ui;

import uo.ri.conf.Factories;
import uo.ri.ui.manager.MainMenu;
import uo.ri.util.console.DefaultPrinter;

public class AdminMain {

	public static void main(String[] args) {
		new AdminMain()
			.configure()
			.run()
			.close();
	}

	private AdminMain configure() {
		return this;
	}

	private AdminMain run() {
		try {
			new MainMenu().execute();

		} catch (RuntimeException rte) {
			DefaultPrinter.printRuntimeError(rte);
		}
		return this;
	}

	private void close() {
		Factories.close();
	}

}
