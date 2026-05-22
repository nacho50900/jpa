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

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.contract.ContractCrudService;
import uo.ri.cws.application.service.contract.ContractCrudService.ContractDto;
import uo.ri.cws.application.service.mechanic.MechanicCrudService;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.util.exception.BusinessException;

public class MechanicsPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTable table;
    private DefaultTableModel tableModel;
    private MechanicCrudService service;
    private ContractCrudService contractService;

    public MechanicsPanel() {
        this.service = Factories.service.forMechanicCrudService();
        this.contractService = Factories.service.forContractCrudService();
        initUI();
        loadAll();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(new JLabel("Mechanics Management"), BorderLayout.NORTH);

        String[] cols = {"ID", "Version", "NIF", "Name", "Surname"};
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

        JButton add         = new JButton("Add");
        JButton upd         = new JButton("Update");
        JButton del         = new JButton("Delete");
        JButton withContract = new JButton("With Valid Contract");
        JButton ref         = new JButton("Refresh All");

        add.addActionListener(e          -> addMechanic());
        upd.addActionListener(e          -> updateMechanic());
        del.addActionListener(e          -> deleteMechanic());
        withContract.addActionListener(e -> loadWithValidContract());
        ref.addActionListener(e          -> loadAll());

        for (JButton b : new JButton[]{add, upd, del, withContract, ref}) {
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
            for (MechanicDto m : service.findAll()) {
                tableModel.addRow(new Object[]{
                        m.id, m.version, m.nif, m.name, m.surname
                });
            }
        } catch (BusinessException e) {
            err("Error loading: " + e.getMessage());
        }
    }

    /** Lists only mechanics that have an active (IN_FORCE) contract. */
    private void loadWithValidContract() {
        try {
            tableModel.setRowCount(0);
            List<ContractDto> inForce = contractService.findInforceContracts();
            for (ContractDto c : inForce) {
                tableModel.addRow(new Object[]{
                        c.mechanic.id, "-",
                        c.mechanic.nif, c.mechanic.name, c.mechanic.surname
                });
            }
            if (inForce.isEmpty()) {
                info("No mechanics with an active contract found");
            }
        } catch (BusinessException e) {
            err("Error: " + e.getMessage());
        }
    }

    // ── Add ──────────────────────────────────────────────────────────────────

    private void addMechanic() {
        JTextField nif     = new JTextField();
        JTextField name    = new JTextField();
        JTextField surname = new JTextField();

        Object[] msg = {"NIF:", nif, "Name:", name, "Surname:", surname};
        if (JOptionPane.showConfirmDialog(this, msg, "Add Mechanic",
                JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) {
			return;
		}

        if (nif.getText().trim().isEmpty()
                || name.getText().trim().isEmpty()
                || surname.getText().trim().isEmpty()) {
            err("All fields are required"); return;
        }

        try {
            MechanicDto dto = new MechanicDto();
            dto.nif     = nif.getText().trim();
            dto.name    = name.getText().trim();
            dto.surname = surname.getText().trim();
            service.create(dto);
            loadAll();
            info("Mechanic added");
        } catch (BusinessException e) {
            err("Error: " + e.getMessage());
        }
    }

    // ── Update ────────────────────────────────────────────────────────────────

    private void updateMechanic() {
        int row = table.getSelectedRow();
        if (row == -1) { warn("Select a mechanic first"); return; }

        String id        = (String) tableModel.getValueAt(row, 0);
        Long version     = (Long)   tableModel.getValueAt(row, 1);
        String currentNif  = (String) tableModel.getValueAt(row, 2);
        String currentName = (String) tableModel.getValueAt(row, 3);
        String currentSurname = (String) tableModel.getValueAt(row, 4);

        JTextField name    = new JTextField(currentName);
        JTextField surname = new JTextField(currentSurname);

        Object[] msg = {
                "NIF: " + currentNif + " (cannot change)",
                "Name:", name, "Surname:", surname
        };

        if (JOptionPane.showConfirmDialog(this, msg, "Update Mechanic",
                JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) {
			return;
		}

        if (name.getText().trim().isEmpty() || surname.getText().trim().isEmpty()) {
            err("Name and surname are required"); return;
        }

        try {
            MechanicDto dto = new MechanicDto();
            dto.id      = id;
            dto.version = version;
            dto.nif     = currentNif;
            dto.name    = name.getText().trim();
            dto.surname = surname.getText().trim();
            service.update(dto);
            loadAll();
            info("Mechanic updated");
        } catch (BusinessException e) {
            err("Error: " + e.getMessage());
        }
    }

    // ── Delete ────────────────────────────────────────────────────────────────

    /**
     * Extended delete: mechanic cannot be deleted if he has work orders,
     * interventions or contracts (checked by the domain / service layer).
     */
    private void deleteMechanic() {
        int row = table.getSelectedRow();
        if (row == -1) { warn("Select a mechanic first"); return; }

        String id   = (String) tableModel.getValueAt(row, 0);
        String name = (String) tableModel.getValueAt(row, 3);

        if (JOptionPane.showConfirmDialog(this,
                "Delete mechanic " + name + "?\n"
                + "(Not allowed if the mechanic has work orders, interventions or contracts)",
                "Confirm", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
			return;
		}

        try {
            service.delete(id);
            loadAll();
            info("Mechanic deleted");
        } catch (BusinessException e) {
            err("Cannot delete: " + e.getMessage());
        }
    }

    private void err(String m)  { JOptionPane.showMessageDialog(this, m, "Error",   JOptionPane.ERROR_MESSAGE); }
    private void warn(String m) { JOptionPane.showMessageDialog(this, m, "Warning", JOptionPane.WARNING_MESSAGE); }
    private void info(String m) { JOptionPane.showMessageDialog(this, m, "Info",    JOptionPane.INFORMATION_MESSAGE); }
}