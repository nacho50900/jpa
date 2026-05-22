package uo.ri.cws.application.ui.manager.contracttype;

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

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.contracttype.ContractTypeCrudService;
import uo.ri.cws.application.service.contracttype.ContractTypeCrudService.ContractTypeDto;
import uo.ri.cws.application.service.contracttype.ContractTypeCrudService.ContractTypeSummaryDto;
import uo.ri.util.exception.BusinessException;

public class ContractTypePanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTable table;
    private DefaultTableModel tableModel;
    private ContractTypeCrudService service;

    public ContractTypePanel() {
        this.service = Factories.service.forContractTypeCrudService();
        initUI();
        load();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(new JLabel("Contract Type Management"), BorderLayout.NORTH);

        String[] cols = {"ID", "Version", "Name", "Compensation Days/Year"};
        tableModel = new DefaultTableModel(cols, 0) {
            private static final long serialVersionUID = 1L;
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        hideCol(1);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buttons(), BorderLayout.EAST);
    }

    private void hideCol(int c) {
        table.getColumnModel().getColumn(c).setMinWidth(0);
        table.getColumnModel().getColumn(c).setMaxWidth(0);
    }

    private JPanel buttons() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        JButton add     = new JButton("Add");
        JButton upd     = new JButton("Update Days");
        JButton del     = new JButton("Delete");
        JButton summary = new JButton("Summary");
        JButton ref     = new JButton("Refresh");

        add.addActionListener(e     -> addType());
        upd.addActionListener(e     -> updateType());
        del.addActionListener(e     -> deleteType());
        summary.addActionListener(e -> showSummary());
        ref.addActionListener(e     -> load());

        for (JButton b : new JButton[]{add, upd, del, summary, ref}) {
            p.add(b);
            p.add(Box.createRigidArea(new Dimension(0, 5)));
        }
        p.add(Box.createVerticalGlue());
        return p;
    }

    private void load() {
        try {
            tableModel.setRowCount(0);
            List<ContractTypeDto> list = service.findAll();
            for (ContractTypeDto ct : list) {
                tableModel.addRow(new Object[]{
                        ct.id, ct.version, ct.name,
                        String.format("%.2f days", ct.compensationDays)
                });
            }
        } catch (BusinessException e) {
            err("Error loading: " + e.getMessage());
        }
    }

    private void addType() {
        JTextField name = new JTextField();
        JTextField days = new JTextField("0.0");
        Object[] msg = {"Name:", name, "Compensation Days/Year:", days};

        if (JOptionPane.showConfirmDialog(this, msg, "Add Contract Type",
                JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) {
			return;
		}

        if (name.getText().trim().isEmpty()) { err("Name is required"); return; }
        try {
            ContractTypeDto dto = new ContractTypeDto();
            dto.name = name.getText().trim();
            dto.compensationDays = Double.parseDouble(days.getText().trim());
            service.create(dto);
            load();
            info("Contract type added");
        } catch (NumberFormatException e) {
            err("Compensation days must be a valid number");
        } catch (BusinessException e) {
            err("Error: " + e.getMessage());
        }
    }

    private void updateType() {
        int row = table.getSelectedRow();
        if (row == -1) { warn("Select a contract type first"); return; }

        String id    = (String) tableModel.getValueAt(row, 0);
        long version = (Long)   tableModel.getValueAt(row, 1);
        String name  = (String) tableModel.getValueAt(row, 2);

        JTextField days = new JTextField();
        Object[] msg = {"New compensation days for " + name + ":", days};

        if (JOptionPane.showConfirmDialog(this, msg, "Update Contract Type",
                JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) {
			return;
		}

        try {
            ContractTypeDto dto = new ContractTypeDto();
            dto.id = id;
            dto.version = version;
            dto.name = name;
            dto.compensationDays = Double.parseDouble(days.getText().trim());
            service.update(dto);
            load();
            info("Updated — affects future contracts only");
        } catch (NumberFormatException e) {
            err("Must be a valid number");
        } catch (BusinessException e) {
            err("Error: " + e.getMessage());
        }
    }

    private void deleteType() {
        int row = table.getSelectedRow();
        if (row == -1) { warn("Select a contract type first"); return; }

        String id   = (String) tableModel.getValueAt(row, 0);
        String name = (String) tableModel.getValueAt(row, 2);

        if (JOptionPane.showConfirmDialog(this,
                "Delete contract type " + name + "?",
                "Confirm", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
			return;
		}

        try {
            service.delete(id);
            load();
            info("Contract type deleted");
        } catch (BusinessException e) {
            err("Error: " + e.getMessage());
        }
    }

    private void showSummary() {
        int row = table.getSelectedRow();
        String name;
        if (row >= 0) {
            name = (String) tableModel.getValueAt(row, 2);
        } else {
            name = JOptionPane.showInputDialog(this, "Enter contract type name:");
            if (name == null || name.isBlank()) {
				return;
			}
            name = name.trim();
        }
        try {
            ContractTypeSummaryDto s = service.getSummaryByName(name);
            String msg = String.format(
                    "Contract Type: %s%n" +
                    "Compensation days/year: %.2f%n%n" +
                    "Active employees: %d%n" +
                    "Accumulated annual salary: %.2f €",
                    s.name, s.compensationDays,
                    s.activeEmployees, s.accumulatedSalary);
            JOptionPane.showMessageDialog(this, msg, "Summary",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (BusinessException e) {
            err("Error: " + e.getMessage());
        }
    }

    private void err(String m)  { JOptionPane.showMessageDialog(this, m, "Error",   JOptionPane.ERROR_MESSAGE); }
    private void warn(String m) { JOptionPane.showMessageDialog(this, m, "Warning", JOptionPane.WARNING_MESSAGE); }
    private void info(String m) { JOptionPane.showMessageDialog(this, m, "Info",    JOptionPane.INFORMATION_MESSAGE); }
}