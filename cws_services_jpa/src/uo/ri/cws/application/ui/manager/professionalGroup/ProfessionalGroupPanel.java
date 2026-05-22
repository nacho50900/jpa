package uo.ri.cws.application.ui.manager.professionalgroup;

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
import uo.ri.cws.application.service.professionalgroup.ProfessionalGroupCrudService;
import uo.ri.cws.application.service.professionalgroup.ProfessionalGroupCrudService.ProfessionalGroupDto;
import uo.ri.util.exception.BusinessException;

public class ProfessionalGroupPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTable table;
    private DefaultTableModel tableModel;
    private ProfessionalGroupCrudService service;

    public ProfessionalGroupPanel() {
        this.service = Factories.service.forProfessionalGroupCrudService();
        initUI();
        load();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(new JLabel("Professional Group Management"), BorderLayout.NORTH);

        String[] cols = {"ID", "Version", "Name", "Triennium (€)", "Productivity (%)"};
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

        JButton add  = new JButton("Add");
        JButton upd  = new JButton("Update");
        JButton del  = new JButton("Delete");
        JButton ref  = new JButton("Refresh");

        add.addActionListener(e -> addGroup());
        upd.addActionListener(e -> updateGroup());
        del.addActionListener(e -> deleteGroup());
        ref.addActionListener(e -> load());

        for (JButton b : new JButton[]{add, upd, del, ref}) {
            p.add(b);
            p.add(Box.createRigidArea(new Dimension(0, 5)));
        }
        p.add(Box.createVerticalGlue());
        return p;
    }

    private void load() {
        try {
            tableModel.setRowCount(0);
            List<ProfessionalGroupDto> list = service.findAll();
            for (ProfessionalGroupDto g : list) {
                tableModel.addRow(new Object[]{
                        g.id, g.version, g.name,
                        String.format("%.2f €", g.trienniumPayment),
                        String.format("%.1f%%", g.productivityRate * 100)
                });
            }
        } catch (BusinessException e) {
            err("Error loading: " + e.getMessage());
        }
    }

    private void addGroup() {
        JTextField name        = new JTextField();
        JTextField triennium   = new JTextField("0.0");
        JTextField productivity = new JTextField("0.05");

        Object[] msg = {
                "Name:", name,
                "Triennium payment (€):", triennium,
                "Productivity rate (e.g. 0.05 = 5%):", productivity
        };

        if (JOptionPane.showConfirmDialog(this, msg, "Add Professional Group",
                JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) {
			return;
		}

        if (name.getText().trim().isEmpty()) { err("Name is required"); return; }

        try {
            ProfessionalGroupDto dto = new ProfessionalGroupDto();
            dto.name = name.getText().trim();
            dto.trienniumPayment = Double.parseDouble(triennium.getText().trim());
            dto.productivityRate = Double.parseDouble(productivity.getText().trim());
            service.create(dto);
            load();
            info("Professional group added");
        } catch (NumberFormatException e) {
            err("Numeric fields must be valid numbers");
        } catch (BusinessException e) {
            err("Error: " + e.getMessage());
        }
    }

    private void updateGroup() {
        int row = table.getSelectedRow();
        if (row == -1) { warn("Select a professional group first"); return; }

        String id    = (String) tableModel.getValueAt(row, 0);
        long version = (Long)   tableModel.getValueAt(row, 1);
        String name  = (String) tableModel.getValueAt(row, 2);

        JTextField triennium    = new JTextField();
        JTextField productivity = new JTextField();

        Object[] msg = {
                "New triennium for " + name + " (€):", triennium,
                "New productivity rate (e.g. 0.05):", productivity
        };

        if (JOptionPane.showConfirmDialog(this, msg, "Update Professional Group",
                JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) {
			return;
		}

        try {
            ProfessionalGroupDto dto = new ProfessionalGroupDto();
            dto.id = id;
            dto.version = version;
            dto.name = name;
            dto.trienniumPayment = Double.parseDouble(triennium.getText().trim());
            dto.productivityRate = Double.parseDouble(productivity.getText().trim());
            service.update(dto);
            load();
            info("Updated — affects future payrolls only");
        } catch (NumberFormatException e) {
            err("Must be valid numbers");
        } catch (BusinessException e) {
            err("Error: " + e.getMessage());
        }
    }

    private void deleteGroup() {
        int row = table.getSelectedRow();
        if (row == -1) { warn("Select a professional group first"); return; }

        String id   = (String) tableModel.getValueAt(row, 0);
        String name = (String) tableModel.getValueAt(row, 2);

        if (JOptionPane.showConfirmDialog(this,
                "Delete professional group " + name + "?",
                "Confirm", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
			return;
		}

        try {
            service.delete(name);
            load();
            info("Professional group deleted");
        } catch (BusinessException e) {
            err("Error: " + e.getMessage());
        }
    }

    private void err(String m)  { JOptionPane.showMessageDialog(this, m, "Error",   JOptionPane.ERROR_MESSAGE); }
    private void warn(String m) { JOptionPane.showMessageDialog(this, m, "Warning", JOptionPane.WARNING_MESSAGE); }
    private void info(String m) { JOptionPane.showMessageDialog(this, m, "Info",    JOptionPane.INFORMATION_MESSAGE); }
}