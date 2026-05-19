package uo.ri.cws.application.ui.manager;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import uo.ri.cws.application.ui.manager.contract.ContractPanel;
import uo.ri.cws.application.ui.manager.mechanic.MechanicsPanel;
import uo.ri.cws.application.ui.manager.payroll.PayrollPanel;
import uo.ri.cws.application.ui.manager.sparepart.SparePartPanel;
import uo.ri.cws.application.ui.manager.vehicletype.VehicleTypePanel;

public class MainWindow extends JFrame {
    
    private static final long serialVersionUID = 1L;
    
    public MainWindow() {
        super("CWS - Car Workshop System");
        initializeComponents();
    }
    
    private void initializeComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(720, 490);
        setLocationRelativeTo(null);
        
        setJMenuBar(createMenuBar());
        
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Mechanics", new MechanicsPanel());
        tabbedPane.addTab("Contracts", new ContractPanel());
        tabbedPane.addTab("Payrolls", new PayrollPanel());
        tabbedPane.addTab("Spare Parts", new SparePartPanel());
        tabbedPane.addTab("Vehicle Types", new VehicleTypePanel());
        
        getContentPane().add(tabbedPane, BorderLayout.CENTER);
    }
    
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to exit?",
                "Confirm Exit",
                JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
        
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                "Car Workshop System\nDone by Ignacio Hoyos "
                + "Diego with a little help of Oviedo university",
                "About CWS",
                JOptionPane.INFORMATION_MESSAGE);
        });
        helpMenu.add(aboutItem);
        menuBar.add(helpMenu);
        
        return menuBar;
    }
}