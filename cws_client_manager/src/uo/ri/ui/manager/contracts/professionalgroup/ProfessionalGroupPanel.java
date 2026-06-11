package uo.ri.ui.manager.contracts.professionalgroup;

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
import uo.ri.cws.application.service.professionalgroup.ProfessionalGroupCrudService;
import uo.ri.cws.application.service.professionalgroup.ProfessionalGroupCrudService.ProfessionalGroupDto;

public class ProfessionalGroupPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private ProfessionalGroupCrudService service = 
    		Factories.service.forProfessionalGroupCrudService();

    private DefaultTableModel model;
    private JTable table;

    private static final String[] COLUMNS = { 
    		"Name", "Triennium Payment", "Productivity Rate" };

    public ProfessionalGroupPanel() {
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        model = new DefaultTableModel(COLUMNS, 0) {
            private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

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
            ProfessionalGroupDto dto = new ProfessionalGroupDto();
            if (showEditDialog(dto, "Add Professional Group")) {
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
            		this, "Select a group first."); return; }
            String name = (String) model.getValueAt(row, 0);
            try {
                ProfessionalGroupDto dto = service.findByName(name).orElseThrow();
                if (showEditDialog(dto, "Edit Professional Group")) {
                    service.update(dto);
                    refresh();
                }
            } catch (Exception ex) {
                showError(ex);
            }
        });

        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(
            		this, "Select a group first."); return; }
            String name = (String) model.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this,
                "Delete professional group '" + name + "'?",
                "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    service.delete(name);
                    refresh();
                } catch (Exception ex) {
                    showError(ex);
                }
            }
        });

        return p;
    }

    private boolean showEditDialog(ProfessionalGroupDto dto, String title) {
        JTextField fName  = new JTextField(
        		dto.name != null ? dto.name : "", 10);
        JTextField fTrien = new JTextField(
        		dto.trienniumPayment > 0 ? String.valueOf(
        				dto.trienniumPayment) : "", 10);
        JTextField fProd  = new JTextField(
        		dto.productivityRate > 0  ? String.valueOf(
        				dto.productivityRate)  : "", 10);

        // Name is the PK - only editable on creation
        fName.setEditable(dto.name == null || dto.name.isEmpty());

        JPanel form = new JPanel(new GridLayout(0, 2, 5, 5));
        form.add(new JLabel("Name:"));              form.add(fName);
        form.add(new JLabel("Triennium payment:")); form.add(fTrien);
        form.add(new JLabel("Productivity rate:")); form.add(fProd);

        int result = JOptionPane.showConfirmDialog(this, form, title,
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            dto.name = fName.getText().trim();
            try {
                dto.trienniumPayment = Double.parseDouble(fTrien.getText().trim());
                dto.productivityRate = Double.parseDouble(fProd.getText().trim());
            } catch (NumberFormatException ex) {
                showError(new Exception("Invalid numeric value")); return false;
            }
            return true;
        }
        return false;
    }

    private void refresh() {
        model.setRowCount(0);
        try {
            List<ProfessionalGroupDto> list = service.findAll();
            for (ProfessionalGroupDto dto : list) {
                model.addRow(new Object[]{ 
                		dto.name, dto.trienniumPayment, dto.productivityRate });
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
