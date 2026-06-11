package uo.ri.ui.manager.contracts.contracttype;

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
import uo.ri.cws.application.service.contracttype.ContractTypeCrudService;
import uo.ri.cws.application.service.contracttype.ContractTypeCrudService.ContractTypeDto;

public class ContractTypePanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private ContractTypeCrudService service = 
    		Factories.service.forContractTypeCrudService();

    private DefaultTableModel model;
    private JTable table;

    private static final String[] COLUMNS = { "Name", "Compensation Days" };

    public ContractTypePanel() {
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
            ContractTypeDto dto = new ContractTypeDto();
            if (showEditDialog(dto, "Add Contract Type")) {
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
            		this, "Select a contract type first."); return; }
            String name = (String) model.getValueAt(row, 0);
            try {
                ContractTypeDto dto = service.findByName(name).orElseThrow();
                if (showEditDialog(dto, "Edit Contract Type")) {
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
            		this, "Select a contract type first."); return; }
            String name = (String) model.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this,
                "Delete contract type '" + name + "'?", 
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

    private boolean showEditDialog(ContractTypeDto dto, String title) {
        JTextField fName = new JTextField(dto.name != null ? dto.name : "", 15);
        JTextField fDays = new JTextField(
        		dto.compensationDays > 0 ? String.valueOf(dto.compensationDays) : "", 10);

        // Name is the PK - only editable on creation
        fName.setEditable(dto.name == null || dto.name.isEmpty());

        JPanel form = new JPanel(new GridLayout(0, 2, 5, 5));
        form.add(new JLabel("Name:"));              form.add(fName);
        form.add(new JLabel("Compensation days:")); form.add(fDays);

        int result = JOptionPane.showConfirmDialog(this, form, title,
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            dto.name = fName.getText().trim();
            try {
                dto.compensationDays = Double.parseDouble(fDays.getText().trim());
            } catch (NumberFormatException ex) {
                showError(new Exception("Invalid compensation days value")); 
                return false;
            }
            return true;
        }
        return false;
    }

    private void refresh() {
        model.setRowCount(0);
        try {
            List<ContractTypeDto> list = service.findAll();
            for (ContractTypeDto dto : list) {
                model.addRow(new Object[]{ dto.name, dto.compensationDays });
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
