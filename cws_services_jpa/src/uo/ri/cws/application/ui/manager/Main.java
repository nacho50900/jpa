package uo.ri.cws.application.ui.manager;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import uo.ri.cws.infrastructure.persistence.jpa.util.Jpa;

public class Main {
    
    public static void main(String[] args) {
        System.out.println("CWS - Car Workshop System - GUI Client\n");
        
        if (!initializePersistence()) {
            System.err.println("Error: Database could not be initialized");
            System.exit(1);
        }
        
        configureLookAndFeel();
        
        SwingUtilities.invokeLater(() -> {
            try {
                MainWindow window = new MainWindow();
                window.setVisible(true);
                System.out.println("GUI started successfully\n");
            } catch (Exception e) {
                e.printStackTrace();
                showError("Error starting application", e);
            }
        });
    }
    
    private static boolean initializePersistence() {
        try {
            System.out.print("Initializing database... ");
            Jpa.getManager();
            System.out.println("OK");
            return true;
        } catch (Exception e) {
            System.out.println("ERROR");
            e.printStackTrace();
            showError("Error connecting to database", e);
            return false;
        }
    }
    
    private static void configureLookAndFeel() {
        try {
            UIManager.setLookAndFeel(
                UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Using default look and feel");
        }
    }
    
    private static void showError(String message, Exception e) {
        String details = e.getMessage() != null ? 
            e.getMessage() : e.getClass().getSimpleName();
        
        JOptionPane.showMessageDialog(null,
            message + "\n\n" + details,
            "Startup Error",
            JOptionPane.ERROR_MESSAGE);
    }
}