package uo.ri.ui;

import javax.swing.*;
import java.awt.*;
import uo.ri.conf.Factories;
import uo.ri.ui.manager.mechanic.MechanicPanel;
import uo.ri.ui.manager.contracts.contract.ContractPanel;
import uo.ri.ui.manager.contracts.contracttype.ContractTypePanel;
import uo.ri.ui.manager.contracts.professionalgroup.ProfessionalGroupPanel;
import uo.ri.ui.manager.payroll.PayrollPanel;

public class SwingMain extends JFrame {

    private static final long serialVersionUID = 1L;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            new SwingMain().setVisible(true);
        });
    }

    public SwingMain() {
        setTitle("CWS - Car Workshop System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Mechanics",            new MechanicPanel());
        tabs.addTab("Contracts",            new ContractPanel());
        tabs.addTab("Contract Types",       new ContractTypePanel());
        tabs.addTab("Professional Groups",  new ProfessionalGroupPanel());
        tabs.addTab("Payrolls",             new PayrollPanel());

        getContentPane().add(tabs, BorderLayout.CENTER);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                Factories.close();
            }
        });
    }
}
