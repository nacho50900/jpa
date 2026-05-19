package uo.ri.cws.application.ui.manager.payroll;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.time.format.DateTimeFormatter;
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

import uo.ri.cws.application.service.payroll.PayrollService;
import uo.ri.cws.application.service.payroll.PayrollService.PayrollDto;
import uo.ri.cws.application.service.payroll.PayrollService.PayrollSummaryDto;
import uo.ri.cws.application.service.payroll.crud.PayrollServiceImpl;
import uo.ri.util.exception.BusinessException;

public class PayrollPanel extends JPanel {
    
    private static final long serialVersionUID = 1L;
    private JTable payrollTable;
    private DefaultTableModel tableModel;
    private PayrollService payrollService;
    private DateTimeFormatter dateFormatter;
    
    public PayrollPanel() {
        this.payrollService = new PayrollServiceImpl();
        this.dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        System.out.println("DEBUG: PayrollPanel initialized");
        initializeUI();
        loadPayrolls();
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        add(new JLabel("Payroll Management"), BorderLayout.NORTH);
        
        String[] columns = {"ID", "Date", "Net Salary"};
        tableModel = new DefaultTableModel(columns, 0) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        payrollTable = new JTable(tableModel);
        
        add(new JScrollPane(payrollTable), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.EAST);
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        JButton generateBtn = new JButton("Generate");
        JButton deleteLastBtn = new JButton("Delete Last");
        JButton deleteAllLastBtn = new JButton("Delete All Last");
        JButton viewDetailsBtn = new JButton("View Details");
        JButton filterByMechanicBtn = new JButton("By Mechanic");
        JButton filterByGroupBtn = new JButton("By Group");
        JButton refreshBtn = new JButton("Refresh");
        
        generateBtn.addActionListener(e -> generatePayrolls());
        deleteLastBtn.addActionListener(e -> deleteLastPayroll());
        deleteAllLastBtn.addActionListener(e -> deleteAllLastPayrolls());
        viewDetailsBtn.addActionListener(e -> viewPayrollDetails());
        filterByMechanicBtn.addActionListener(e -> filterByMechanic());
        filterByGroupBtn.addActionListener(e -> filterByProfessionalGroup());
        refreshBtn.addActionListener(e -> loadPayrolls());
        
        panel.add(generateBtn);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(deleteLastBtn);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(deleteAllLastBtn);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(viewDetailsBtn);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(filterByMechanicBtn);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(filterByGroupBtn);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(refreshBtn);
        panel.add(Box.createVerticalGlue());
        
        return panel;
    }
    
    private void loadPayrolls() {
        try {
            tableModel.setRowCount(0);
            List<PayrollSummaryDto> payrolls = payrollService.findAllSummarized();
            
            System.out.println("DEBUG: Found " + payrolls.size() + " payrolls");
            
            for (PayrollSummaryDto p : payrolls) {
                System.out.println("DEBUG: Payroll - ID: " + p.id + ", Date: "
            + p.date + ", NetSalary: " + p.netSalary);
                tableModel.addRow(new Object[]{
                    p.id,
                    p.date != null ? p.date.format(dateFormatter) : "N/A",
                    String.format("%.2f €", p.netSalary)
                });
            }
        } catch (BusinessException e) {
            e.printStackTrace();
            showError("Error loading payrolls: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            showError("Unexpected error: " + e.getMessage());
        }
    }
    
    //No payrolls are generated but its okay, no one should(as in PG1 test),
    //and Contracts are out of my implementation, so its fine like that.
    private void generatePayrolls() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Generate payrolls for the previous month?",
            "Confirm Generation",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                System.out.println("DEBUG: Attempting to generate payrolls...");
                List<PayrollDto> generated = payrollService.generateForPreviousMonth();
                System.out.println("DEBUG: Generated " + generated.size() + " payrolls");
                
                if (generated.isEmpty()) {
                    showWarning("0 payrolls generated.\n\n" +
                        "Possible reasons:\n" +
                        "- No mechanics with active contracts\n" +
                        "- Contracts not valid for previous month\n" +
                        "- Payrolls already generated for this period\n\n" +
                        "Check that mechanics have contracts in the system.");
                } else {
                    showInfo(generated.size() + " payroll(s) generated successfully");
                }
                
                loadPayrolls();
            } catch (BusinessException e) {
                e.printStackTrace();
                showError("Error generating payrolls: " + e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
                showError("Unexpected error: " + e.getMessage());
            }
        }
    }
    
    private void deleteLastPayroll() {
        JTextField mechanicIdField = new JTextField();
        
        Object[] message = {
            "Enter Mechanic ID:", mechanicIdField
        };
        
        int option = JOptionPane.showConfirmDialog(
            this, message, "Delete Last Payroll of Mechanic", 
            JOptionPane.OK_CANCEL_OPTION);
        
        if (option == JOptionPane.OK_OPTION) {
            String mechanicId = mechanicIdField.getText().trim();
            
            if (mechanicId.isEmpty()) {
                showError("Mechanic ID is required");
                return;
            }
            
            int confirm = JOptionPane.showConfirmDialog(this,
                "Delete last payroll of mechanic " + mechanicId + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    payrollService.deleteLastGeneratedOfMechanicId(mechanicId);
                    loadPayrolls();
                    showInfo("Payroll deleted successfully");
                } catch (BusinessException e) {
                    showError("Error: " + e.getMessage());
                }
            }
        }
    }
    
    private void deleteAllLastPayrolls() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Delete all last generated payrolls?\nThis action cannot be undone.",
            "Confirm Delete All",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int deleted = payrollService.deleteLastGenerated();
                loadPayrolls();
                showInfo(deleted + " payroll(s) deleted successfully");
            } catch (BusinessException e) {
                showError("Error deleting payrolls: " + e.getMessage());
            }
        }
    }
    
    private void viewPayrollDetails() {
        int row = payrollTable.getSelectedRow();
        if (row == -1) {
            showWarning("Please select a payroll");
            return;
        }
        
        String payrollId = (String) tableModel.getValueAt(row, 0);
        
        try {
            var payrollOpt = payrollService.findById(payrollId);
            
            if (payrollOpt.isPresent()) {
                PayrollDto payroll = payrollOpt.get();
                
                StringBuilder details = new StringBuilder();
                details.append("=== PAYROLL DETAILS ===\n\n");
                details.append("ID: ").append(payroll.id).append("\n");
                details.append("Version: ").append(payroll.version).append("\n");
                details.append("Contract ID: ").append(payroll.contractId).append("\n");
                details.append("Date: ").append(
                    payroll.date != null ? payroll.date.format(dateFormatter) : "N/A"
                ).append("\n\n");
                
                details.append("--- EARNINGS ---\n");
                details.append("Base Salary: ").append(String.format(
                		"%.2f €", payroll.baseSalary)).append("\n");
                details.append("Extra Salary: ").append(String.format(
                		"%.2f €", payroll.extraSalary)).append("\n");
                details.append("Productivity Earning: ").append(String.format(
                		"%.2f €", payroll.productivityEarning)).append("\n");
                details.append("Triennium Earning: ").append(String.format(
                		"%.2f €", payroll.trienniumEarning)).append("\n");
                details.append("Gross Salary: ").append(String.format(
                		"%.2f €", payroll.grossSalary)).append("\n\n");
                
                details.append("--- DEDUCTIONS ---\n");
                details.append("Tax Deduction (IRPF): ").append(String.format(
                		"%.2f €", payroll.taxDeduction)).append("\n");
                details.append("NIC Deduction: ").append(String.format(
                		"%.2f €", payroll.nicDeduction)).append("\n");
                details.append("Total Deductions: ").append(String.format(
                		"%.2f €", payroll.totalDeductions)).append("\n\n");
                
                details.append("=========================\n");
                details.append("NET SALARY: ").append(String.format(
                		"%.2f €", payroll.netSalary)).append("\n");
                details.append("=========================");
                
                JOptionPane.showMessageDialog(this, details.toString(),
                    "Payroll Details", JOptionPane.INFORMATION_MESSAGE);
            } else {
                showError("Payroll not found");
            }
        } catch (BusinessException e) {
            showError("Error loading payroll details: " + e.getMessage());
        }
    }
    
    private void filterByMechanic() {
        JTextField mechanicIdField = new JTextField();
        
        Object[] message = {
            "Enter Mechanic ID:", mechanicIdField
        };
        
        int option = JOptionPane.showConfirmDialog(
            this, message, "Filter by Mechanic", 
            JOptionPane.OK_CANCEL_OPTION);
        
        if (option == JOptionPane.OK_OPTION) {
            String mechanicId = mechanicIdField.getText().trim();
            
            if (mechanicId.isEmpty()) {
                showError("Mechanic ID is required");
                return;
            }
            
            try {
                tableModel.setRowCount(0);
                List<PayrollSummaryDto> payrolls = 
                    payrollService.findSummarizedByMechanicId(mechanicId);
                
                for (PayrollSummaryDto p : payrolls) {
                    tableModel.addRow(new Object[]{
                        p.id,
                        p.date != null ? p.date.format(dateFormatter) : "N/A",
                        String.format("%.2f €", p.netSalary)
                    });
                }
                
                if (payrolls.isEmpty()) {
                    showInfo("No payrolls found for mechanic " + mechanicId);
                }
            } catch (BusinessException e) {
                showError("Error: " + e.getMessage());
            }
        }
    }
    
    private void filterByProfessionalGroup() {
        JTextField groupNameField = new JTextField();
        
        Object[] message = {
            "Enter Professional Group Name:", groupNameField
        };
        
        int option = JOptionPane.showConfirmDialog(
            this, message, "Filter by Professional Group", 
            JOptionPane.OK_CANCEL_OPTION);
        
        if (option == JOptionPane.OK_OPTION) {
            String groupName = groupNameField.getText().trim();
            
            if (groupName.isEmpty()) {
                showError("Professional Group name is required");
                return;
            }
            
            try {
                tableModel.setRowCount(0);
                List<PayrollSummaryDto> payrolls = 
                    payrollService.findSummarizedByProfessionalGroupName(groupName);
                
                for (PayrollSummaryDto p : payrolls) {
                    tableModel.addRow(new Object[]{
                        p.id,
                        p.date != null ? p.date.format(dateFormatter) : "N/A",
                        String.format("%.2f €", p.netSalary)
                    });
                }
                
                if (payrolls.isEmpty()) {
                    showInfo("No payrolls found for group " + groupName);
                }
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