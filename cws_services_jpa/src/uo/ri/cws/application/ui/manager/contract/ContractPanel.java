package uo.ri.cws.application.ui.manager.contract;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.time.LocalDate;
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

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.contract.ContractCrudService;
import uo.ri.cws.application.service.contract.ContractCrudService.ContractDto;
import uo.ri.cws.application.service.contract.ContractCrudService.ContractSummaryDto;
import uo.ri.util.exception.BusinessException;

public class ContractPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTable table;
    private DefaultTableModel tableModel;
    private ContractCrudService service;
    private DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MM/yyyy");

    public ContractPanel() {
        this.service = Factories.service.forContractCrudService();
        initUI();
        loadAll();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(new JLabel("Contract Management"), BorderLayout.NORTH);

        String[] cols = {"ID", "NIF", "State", "Payrolls", "Settlement"};
        tableModel = new DefaultTableModel(cols, 0) {
            private static final long serialVersionUID = 1L;
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buttons(), BorderLayout.EAST);
    }

    private JPanel buttons() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        JButton add      = new JButton("Add");
        JButton upd      = new JButton("Update");
        JButton del      = new JButton("Delete");
        JButton term     = new JButton("Terminate");
        JButton view     = new JButton("View Details");
        JButton byMech   = new JButton("By Mechanic NIF");
        JButton inForce  = new JButton("In Force");
        JButton ref      = new JButton("Refresh All");

        add.addActionListener(e     -> addContract());
        upd.addActionListener(e     -> updateContract());
        del.addActionListener(e     -> deleteContract());
        term.addActionListener(e    -> terminateContract());
        view.addActionListener(e    -> viewDetails());
        byMech.addActionListener(e  -> filterByMechanic());
        inForce.addActionListener(e -> loadInForce());
        ref.addActionListener(e     -> loadAll());

        for (JButton b : new JButton[]{add, upd, del, term, view, byMech, inForce, ref}) {
            p.add(b);
            p.add(Box.createRigidArea(new Dimension(0, 5)));
        }
        p.add(Box.createVerticalGlue());
        return p;
    }

    // ── Load ─────────────────────────────────────────────────────────────────

    private void loadAll() {
        try {
            tableModel.setRowCount(0);
            List<ContractSummaryDto> list = service.findAll();
            for (ContractSummaryDto c : list) {
                tableModel.addRow(new Object[]{
                        c.id, c.nif, c.state, c.payrollCount,
                        String.format("%.2f €", c.settlement)
                });
            }
        } catch (BusinessException e) {
            err("Error loading: " + e.getMessage());
        }
    }

    private void loadInForce() {
        try {
            tableModel.setRowCount(0);
            List<ContractDto> list = service.findInforceContracts();
            for (ContractDto c : list) {
                tableModel.addRow(new Object[]{
                        c.id, c.mechanic.nif, c.state, "-",
                        String.format("%.2f €", c.settlement)
                });
            }
        } catch (BusinessException e) {
            err("Error: " + e.getMessage());
        }
    }

    private void filterByMechanic() {
        String nif = JOptionPane.showInputDialog(this, "Enter mechanic NIF:");
        if (nif == null || nif.isBlank()) {
			return;
		}
        try {
            tableModel.setRowCount(0);
            List<ContractSummaryDto> list = service.findByMechanicNif(nif.trim());
            for (ContractSummaryDto c : list) {
                tableModel.addRow(new Object[]{
                        c.id, c.nif, c.state, c.payrollCount,
                        String.format("%.2f €", c.settlement)
                });
            }
        } catch (BusinessException e) {
            err("Error: " + e.getMessage());
        }
    }

    // ── Add ──────────────────────────────────────────────────────────────────

    private void addContract() {
        JTextField nif       = new JTextField();
        JTextField typeName  = new JTextField();
        JTextField groupName = new JTextField();
        JTextField salary    = new JTextField();
        JTextField endDate   = new JTextField("leave blank if PERMANENT/SEASONAL");

        Object[] msg = {
                "Mechanic NIF:", nif,
                "Contract type (FIXED_TERM / PERMANENT / SEASONAL):", typeName,
                "Professional group name:", groupName,
                "Annual base salary (€):", salary,
                "End date if FIXED_TERM (yyyy-MM-dd):", endDate
        };

        if (JOptionPane.showConfirmDialog(this, msg, "Add Contract",
                JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) {
			return;
		}

        if (isBlank(nif, typeName, groupName, salary)) {
            err("NIF, type, group and salary are required"); return;
        }

        try {
            ContractDto dto = new ContractDto();
            dto.mechanic.nif = nif.getText().trim();
            dto.contractType.name = typeName.getText().trim();
            dto.professionalGroup.name = groupName.getText().trim();
            dto.annualBaseSalary = Double.parseDouble(salary.getText().trim());
            dto.startDate = LocalDate.now();

            String ed = endDate.getText().trim();
            if (!ed.isEmpty() && !ed.startsWith("leave")) {
                dto.endDate = LocalDate.parse(ed);
            }

            service.create(dto);
            loadAll();
            info("Contract created");
        } catch (NumberFormatException e) {
            err("Salary must be a valid number");
        } catch (BusinessException e) {
            err("Error: " + e.getMessage());
        }
    }

    // ── Update ────────────────────────────────────────────────────────────────

    private void updateContract() {
        int row = table.getSelectedRow();
        if (row == -1) { warn("Select a contract first"); return; }

        String id = (String) tableModel.getValueAt(row, 0);
        try {
            ContractDto current = service.findById(id)
                    .orElseThrow(() -> new BusinessException("Contract not found"));

            JTextField salary  = new JTextField(String.valueOf(current.annualBaseSalary));
            JTextField endDate = new JTextField(
                    current.endDate != null ? current.endDate.toString() : "");

            Object[] msg = {
                    "Annual base salary (€):", salary,
                    "End date (yyyy-MM-dd, FIXED_TERM only):", endDate
            };

            if (JOptionPane.showConfirmDialog(this, msg, "Update Contract",
                    JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) {
				return;
			}

            ContractDto dto = new ContractDto();
            dto.id = id;
            dto.version = current.version;
            dto.annualBaseSalary = Double.parseDouble(salary.getText().trim());
            String ed = endDate.getText().trim();
            dto.endDate = ed.isEmpty() ? null : LocalDate.parse(ed);

            service.update(dto);
            loadAll();
            info("Contract updated");
        } catch (NumberFormatException e) {
            err("Salary must be a valid number");
        } catch (BusinessException e) {
            err("Error: " + e.getMessage());
        }
    }

    // ── Delete ────────────────────────────────────────────────────────────────

    private void deleteContract() {
        int row = table.getSelectedRow();
        if (row == -1) { warn("Select a contract first"); return; }

        String id = (String) tableModel.getValueAt(row, 0);
        if (JOptionPane.showConfirmDialog(this,
                "Delete contract " + id + "?\n(Only allowed if no payrolls/work orders exist)",
                "Confirm", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
			return;
		}

        try {
            service.delete(id);
            loadAll();
            info("Contract deleted");
        } catch (BusinessException e) {
            err("Error: " + e.getMessage());
        }
    }

    // ── Terminate ─────────────────────────────────────────────────────────────

    private void terminateContract() {
        int row = table.getSelectedRow();
        String id;
        if (row >= 0) {
            id = (String) tableModel.getValueAt(row, 0);
        } else {
            id = JOptionPane.showInputDialog(this, "Enter contract ID to terminate:");
            if (id == null || id.isBlank()) {
				return;
			}
            id = id.trim();
        }

        if (JOptionPane.showConfirmDialog(this,
                "Terminate contract " + id + "?\nEnd date will be set to last day of current month.",
                "Confirm", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
			return;
		}

        try {
            service.terminate(id);
            loadAll();
            info("Contract terminated and settlement calculated");
        } catch (BusinessException e) {
            err("Error: " + e.getMessage());
        }
    }

    // ── View Details ──────────────────────────────────────────────────────────

    private void viewDetails() {
        int row = table.getSelectedRow();
        if (row == -1) { warn("Select a contract first"); return; }

        String id = (String) tableModel.getValueAt(row, 0);
        try {
            ContractDto c = service.findById(id)
                    .orElseThrow(() -> new BusinessException("Contract not found"));

            String details = String.format(
                    "=== CONTRACT DETAILS ===%n" +
                    "ID: %s%n" +
                    "State: %s%n" +
                    "Mechanic: %s %s (NIF: %s)%n%n" +
                    "Type: %s (%.2f days/year compensation)%n" +
                    "Group: %s%n%n" +
                    "Start: %s%n" +
                    "End: %s%n%n" +
                    "Annual salary: %.2f €%n" +
                    "Tax rate: %.0f%%%n" +
                    "Settlement: %.2f €",
                    c.id, c.state,
                    c.mechanic.name, c.mechanic.surname, c.mechanic.nif,
                    c.contractType.name, c.contractType.compensationDaysPerYear,
                    c.professionalGroup.name,
                    c.startDate != null ? c.startDate.format(fmt) : "N/A",
                    c.endDate != null ? c.endDate.format(fmt) : "Indefinite",
                    c.annualBaseSalary,
                    c.taxRate * 100,
                    c.settlement);

            JOptionPane.showMessageDialog(this, details, "Contract Details",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (BusinessException e) {
            err("Error: " + e.getMessage());
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private boolean isBlank(JTextField... fs) {
        for (JTextField f : fs) {
			if (f.getText().trim().isEmpty()) {
				return true;
			}
		}
        return false;
    }

    private void err(String m)  { JOptionPane.showMessageDialog(this, m, "Error",   JOptionPane.ERROR_MESSAGE); }
    private void warn(String m) { JOptionPane.showMessageDialog(this, m, "Warning", JOptionPane.WARNING_MESSAGE); }
    private void info(String m) { JOptionPane.showMessageDialog(this, m, "Info",    JOptionPane.INFORMATION_MESSAGE); }
}