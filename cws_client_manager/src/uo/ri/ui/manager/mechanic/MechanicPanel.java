package uo.ri.ui.manager.mechanic;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.mechanic.MechanicCrudService;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;

public class MechanicPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private MechanicCrudService service = 
    		Factories.service.forMechanicCrudService();

    private DefaultTableModel model;
    private JTable table;

    private static final String[] COLUMNS = { "ID", "NIF", "Name", "Surname" };

    public MechanicPanel() {
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

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buildButtonPanel(), BorderLayout.SOUTH);

        refresh();
    }

    private JPanel buildButtonPanel() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));

        JButton btnRefresh = new JButton("Refresh");
        JButton btnAdd     = new JButton("Add...");
        JButton btnEdit    = new JButton("Edit...");
        JButton btnDelete  = new JButton("Delete");

        p.add(btnRefresh);
        p.add(btnAdd);
        p.add(btnEdit);
        p.add(btnDelete);

        btnRefresh.addActionListener(e -> refresh());

        btnAdd.addActionListener(e -> {
            MechanicDto dto = new MechanicDto();
            if (showEditDialog(dto, "Add Mechanic")) {
                try {
                    service.create(dto);
                    refresh();
                } catch (Exception ex) {
                    showError(ex);
                }
            }
        });

        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(
            		this, "Select a mechanic first."); return; }
            String id = (String) model.getValueAt(row, 0);
            try {
                MechanicDto dto = service.findById(id).orElseThrow();
                if (showEditDialog(dto, "Edit Mechanic")) {
                    service.update(dto);
                    refresh();
                }
            } catch (Exception ex) {
                showError(ex);
            }
        });

        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this, "Select a mechanic first."); return; }
            String id    = (String) model.getValueAt(row, 0);
            String name  = (String) model.getValueAt(row, 2);
            int confirm = JOptionPane.showConfirmDialog(this,
                "Delete mechanic " + name + "?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    service.delete(id);
                    refresh();
                } catch (Exception ex) {
                    showError(ex);
                }
            }
        });

        return p;
    }

    private boolean showEditDialog(MechanicDto dto, String title) {
        JTextField fNif     = new JTextField(
        		dto.nif     != null ? dto.nif: "", 15);
        JTextField fName    = new JTextField(
        		dto.name    != null ? dto.name    : "", 15);
        JTextField fSurname = new JTextField(
        		dto.surname != null ? dto.surname : "", 15);

        // NIF is the natural key - only editable on creation
        fNif.setEditable(dto.nif == null || dto.nif.isEmpty());

        JPanel form = new JPanel(new GridLayout(0, 2, 5, 5));
        form.add(new JLabel("NIF:"));     form.add(fNif);
        form.add(new JLabel("Name:"));    form.add(fName);
        form.add(new JLabel("Surname:")); form.add(fSurname);

        int result = JOptionPane.showConfirmDialog(this, form, title,
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            dto.nif     = fNif.getText().trim();
            dto.name    = fName.getText().trim();
            dto.surname = fSurname.getText().trim();
            return true;
        }
        return false;
    }

    private void refresh() {
        model.setRowCount(0);
        try {
            List<MechanicDto> list = service.findAll();
            for (MechanicDto m : list) {
                model.addRow(new Object[]{ m.id, m.nif, m.name, m.surname });
            }
        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void showError(Exception ex) {
        JOptionPane.showMessageDialog(
        		this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
