package uo.ri.cws.application.ui.manager.mechanic;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import uo.ri.cws.application.service.mechanic.MechanicCrudService;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.cws.application.service.mechanic.crud.MechanicCrudServiceImpl;
import uo.ri.util.exception.BusinessException;

public class MechanicsPanel extends JPanel {
    
    private static final long serialVersionUID = 1L;
    private JTable mechanicsTable;
    private DefaultTableModel tableModel;
    private MechanicCrudService mechanicService;
    
    public MechanicsPanel() {
        this.mechanicService = new MechanicCrudServiceImpl();
        initializeUI();
        loadMechanics();
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        add(new JLabel("Mechanics Management"), BorderLayout.NORTH);
        
        String[] columns = {"ID", "Version", "NIF", "Name", "Surname"};
        tableModel = new DefaultTableModel(columns, 0) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        mechanicsTable = new JTable(tableModel);
        mechanicsTable.getColumnModel().getColumn(1).setMinWidth(0);
        mechanicsTable.getColumnModel().getColumn(1).setMaxWidth(0);
        
        add(new JScrollPane(mechanicsTable), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.EAST);
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        JButton addBtn = new JButton("Add");
        JButton updateBtn = new JButton("Update");
        JButton deleteBtn = new JButton("Delete");
        JButton refreshBtn = new JButton("Refresh");
        
        addBtn.addActionListener(e -> addMechanic());
        updateBtn.addActionListener(e -> updateMechanic());
        deleteBtn.addActionListener(e -> deleteMechanic());
        refreshBtn.addActionListener(e -> loadMechanics());
        
        panel.add(addBtn);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(updateBtn);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(deleteBtn);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(refreshBtn);
        panel.add(Box.createVerticalGlue());
        
        return panel;
    }
    
    private void loadMechanics() {
        try {
            tableModel.setRowCount(0);
            List<MechanicDto> mechanics = mechanicService.findAll();
            
            for (MechanicDto m : mechanics) {
                tableModel.addRow(new Object[]{
                    m.id, m.version, m.nif, m.name, m.surname
                });
            }
        } catch (BusinessException e) {
            showError("Error loading mechanics: " + e.getMessage());
        }
    }
    
    private void addMechanic() {
        JTextField nifField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField surnameField = new JTextField();
        
        Object[] message = {
            "NIF:", nifField,
            "Name:", nameField,
            "Surname:", surnameField
        };
        
        int option = JOptionPane.showConfirmDialog(
            this, message, "Add Mechanic", 
            JOptionPane.OK_CANCEL_OPTION);
        
        if (option == JOptionPane.OK_OPTION) {
            String nif = nifField.getText().trim();
            String name = nameField.getText().trim();
            String surname = surnameField.getText().trim();
            
            if (nif.isEmpty() || name.isEmpty() || surname.isEmpty()) {
                showError("All fields are required");
                return;
            }
            
            try {
                MechanicDto dto = new MechanicDto();
                dto.nif = nif;
                dto.name = name;
                dto.surname = surname;
                
                mechanicService.create(dto);
                loadMechanics();
                showInfo("Mechanic added successfully");
            } catch (BusinessException e) {
                showError("Error: " + e.getMessage());
            }
        }
    }
    
    private void updateMechanic() {
        int row = mechanicsTable.getSelectedRow();
        if (row == -1) {
            showWarning("Please select a mechanic");
            return;
        }
        
        String id = (String) tableModel.getValueAt(row, 0);
        Long version = (Long) tableModel.getValueAt(row, 1);
        String currentNif = (String) tableModel.getValueAt(row, 2);
        String currentName = (String) tableModel.getValueAt(row, 3);
        String currentSurname = (String) tableModel.getValueAt(row, 4);
        
        JTextField nameField = new JTextField(currentName);
        JTextField surnameField = new JTextField(currentSurname);
        
        Object[] message = {
            "NIF: " + currentNif + " (cannot be changed)",
            "Name:", nameField,
            "Surname:", surnameField
        };
        
        int option = JOptionPane.showConfirmDialog(
            this, message, "Update Mechanic", 
            JOptionPane.OK_CANCEL_OPTION);
        
        if (option == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String surname = surnameField.getText().trim();
            
            if (name.isEmpty() || surname.isEmpty()) {
                showError("Name and surname are required");
                return;
            }
            
            try {
                MechanicDto dto = new MechanicDto();
                dto.id = id;
                dto.version = version;
                dto.nif = currentNif;
                dto.name = name;
                dto.surname = surname;
                
                mechanicService.update(dto);
                loadMechanics();
                showInfo("Mechanic updated successfully");
            } catch (BusinessException e) {
                showError("Error: " + e.getMessage());
            }
        }
    }
    
    private void deleteMechanic() {
        int row = mechanicsTable.getSelectedRow();
        if (row == -1) {
            showWarning("Please select a mechanic");
            return;
        }
        
        String id = (String) tableModel.getValueAt(row, 0);
        String name = (String) tableModel.getValueAt(row, 3);
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Delete mechanic " + name + "?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                mechanicService.delete(id);
                loadMechanics();
                showInfo("Mechanic deleted successfully");
            } catch (BusinessException e) {
                showError("Error: " + e.getMessage());
            }
        }
    }
    
    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error",
            JOptionPane.ERROR_MESSAGE);
    }
    
    private void showWarning(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Warning",
            JOptionPane.WARNING_MESSAGE);
    }
    
    private void showInfo(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Information",
            JOptionPane.INFORMATION_MESSAGE);
    }
}