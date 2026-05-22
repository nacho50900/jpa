package uo.ri.cws.application.ui.manager;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import uo.ri.cws.application.ui.manager.client.ClientsPanel;
import uo.ri.cws.application.ui.manager.contract.ContractPanel;
import uo.ri.cws.application.ui.manager.contractType.ContractTypePanel;
import uo.ri.cws.application.ui.manager.mechanic.MechanicsPanel;
import uo.ri.cws.application.ui.manager.payroll.PayrollPanel;
import uo.ri.cws.application.ui.manager.professionalGroup.ProfessionalGroupPanel;
import uo.ri.cws.application.ui.manager.sparepart.SparePartPanel;
import uo.ri.cws.application.ui.manager.vehicle.VehiclesPanel;
import uo.ri.cws.application.ui.manager.vehicletype.VehicleTypePanel;
import uo.ri.cws.application.ui.manager.workorder.WorkOrderPanel;

public class MainWindow extends JFrame {

    private static final long serialVersionUID = 1L;

    public MainWindow() {
        super("CWS - Car Workshop System");
        initializeComponents();
    }

    private void initializeComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setJMenuBar(createMenuBar());

        JTabbedPane tabs = new JTabbedPane();

        // ── Ejercicio 2 (tu parte original) ──────────────────────────────────
        tabs.addTab("Mechanics",          new MechanicsPanel());
        tabs.addTab("Professional Groups",new ProfessionalGroupPanel());
        tabs.addTab("Payrolls",           new PayrollPanel());

        // ── Ejercicio 0 ───────────────────────────────────────────────────────
        tabs.addTab("Contract Types",     new ContractTypePanel());
        tabs.addTab("Spare Parts",        new SparePartPanel());
        tabs.addTab("Vehicle Types",      new VehicleTypePanel());

        // ── Ejercicio 1 ───────────────────────────────────────────────────────
        tabs.addTab("Contracts",          new ContractPanel());

        // ── Base (Foreman / Mechanic / Cashier) ───────────────────────────────
        tabs.addTab("Clients",            new ClientsPanel());
        tabs.addTab("Vehicles",           new VehiclesPanel());
        tabs.addTab("Work Orders",        new WorkOrderPanel());

        getContentPane().add(tabs, BorderLayout.CENTER);
    }

    private JMenuBar createMenuBar() {
        JMenuBar bar = new JMenuBar();

        JMenu file = new JMenu("File");
        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to exit?",
                    "Confirm Exit", JOptionPane.YES_NO_OPTION)
                    == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        file.add(exit);
        bar.add(file);

        JMenu help = new JMenu("Help");
        JMenuItem about = new JMenuItem("About");
        about.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Car Workshop System\n" +
                "Universidad de Oviedo — EII RI 2024/25\n" +
                "UO300737",
                "About CWS", JOptionPane.INFORMATION_MESSAGE));
        help.add(about);
        bar.add(help);

        return bar;
    }
}