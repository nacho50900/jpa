package uo.ri.ui.manager.payroll;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.time.LocalDate;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.payroll.PayrollService;
import uo.ri.cws.application.service.payroll.PayrollService.PayrollDto;
import uo.ri.cws.application.service.payroll.PayrollService.PayrollSummaryDto;

public class PayrollPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private PayrollService service = Factories.service.forPayrollService();

    private DefaultTableModel model;
    private JTable table;

    private static final String[] COLUMNS =
        { "ID", "Contract ID", "Date", "Gross Salary", "Deductions", 
        		"Net Salary" };

    public PayrollPanel() {
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        model = new DefaultTableModel(COLUMNS, 0) {
        	
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getColumnModel().getColumn(0).setPreferredWidth(220);
        table.getColumnModel().getColumn(1).setPreferredWidth(220);

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buildButtonPanel(), BorderLayout.SOUTH);

        loadSummaries(null);
    }

    private JPanel buildButtonPanel() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));

        JButton btnGenToday   = new JButton("Generate (today)");
        JButton btnGenDate    = new JButton("Generate at date...");
        JButton btnDetails    = new JButton("Details...");
        JButton btnByMechanic = new JButton("By mechanic...");
        JButton btnShowAll    = new JButton("Show all");
        JButton btnDeleteLast = new JButton("Delete last generated");

        p.add(btnGenToday);
        p.add(btnGenDate);
        p.add(btnDetails);
        p.add(btnByMechanic);
        p.add(btnShowAll);
        p.add(btnDeleteLast);

        // Generate for previous month of today - shows only those just generated
        btnGenToday.addActionListener(e -> {
            try {
                List<PayrollDto> generated = service.generateForPreviousMonth();
                loadGenerated(generated);
                JOptionPane.showMessageDialog(this,
                    generated.size() + " payroll(s) generated.", "Done", 
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                showError(ex);
            }
        });

        // Generate for previous month of a given date, shows only those generated
        btnGenDate.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(
            		this, "Enter date (yyyy-MM-dd):",
                "Generate at date", JOptionPane.PLAIN_MESSAGE);
            if (input == null || input.isBlank()) {
				return;
			}
            try {
                LocalDate date = LocalDate.parse(input.trim());
                List<PayrollDto> generated = 
                		service.generateForPreviousMonthOf(date);
                loadGenerated(generated);
                JOptionPane.showMessageDialog(this,
                    generated.size() + " payroll(s) generated.", "Done", 
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                showError(ex);
            }
        });

        // Show full details of the selected payroll
        btnDetails.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(
            		this, "Select a payroll first."); return; }
            String id = (String) model.getValueAt(row, 0);
            try {
                PayrollDto dto = service.findById(id).orElseThrow();
                showDetails(dto);
            } catch (Exception ex) {
                showError(ex);
            }
        });

        // Filter table by mechanic id
        btnByMechanic.addActionListener(e -> {
            String id = JOptionPane.showInputDialog(this, "Mechanic ID:",
                "Payrolls by mechanic", JOptionPane.PLAIN_MESSAGE);
            if (id == null || id.isBlank()) {
				return;
			}
            try {
                loadSummaries(id.trim());
            } catch (Exception ex) {
                showError(ex);
            }
        });

        // Reload all payrolls
        btnShowAll.addActionListener(e -> loadSummaries(null));

        // Delete last generated batch and reload
        btnDeleteLast.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Delete last generated payrolls?", "Confirm", 
                JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    service.deleteLastGenerated();
                    loadSummaries(null);
                    JOptionPane.showMessageDialog(this, "Last payrolls deleted.");
                } catch (Exception ex) {
                    showError(ex);
                }
            }
        });

        return p;
    }

    /** Fills the table with a list of PayrollDto (result of generate) */
    private void loadGenerated(List<PayrollDto> list) {
        model.setRowCount(0);
        for (PayrollDto dto : list) {
            model.addRow(new Object[]{
                dto.id, dto.contractId, dto.date,
                dto.grossSalary, dto.totalDeductions, dto.netSalary
            });
        }
    }

    /** Fills the table with summaries - all or filtered by mechanic */
    private void loadSummaries(String mechanicId) {
        model.setRowCount(0);
        try {
            List<PayrollSummaryDto> list = (mechanicId != null)
                ? service.findSummarizedByMechanicId(mechanicId)
                : service.findAllSummarized();
            for (PayrollSummaryDto dto : list) {
                model.addRow(new Object[]{
                    dto.id, "", dto.date, "", "", dto.netSalary
                });
            }
        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void showDetails(PayrollDto dto) {
        String msg = String.format(
            "ID:                  %s%n" +
            "Contract ID:         %s%n" +
            "Date:                %s%n" +
            "Base salary:         %.2f%n" +
            "Extra salary:        %.2f%n" +
            "Productivity:        %.2f%n" +
            "Triennium:           %.2f%n" +
            "Gross salary:        %.2f%n" +
            "Tax deduction:       %.2f%n" +
            "NIC deduction:       %.2f%n" +
            "Total deductions:    %.2f%n" +
            "Net salary:          %.2f",
            dto.id, dto.contractId, dto.date,
            dto.baseSalary, dto.extraSalary,
            dto.productivityEarning, dto.trienniumEarning,
            dto.grossSalary, dto.taxDeduction, dto.nicDeduction,
            dto.totalDeductions, dto.netSalary
        );
        JOptionPane.showMessageDialog(
        		this, msg, "Payroll Details", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showError(Exception ex) {
        JOptionPane.showMessageDialog(
        		this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
