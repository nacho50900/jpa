package uo.ri.ui.manager.contracts.contract;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.time.LocalDate;
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
import uo.ri.cws.application.service.contract.ContractCrudService;
import uo.ri.cws.application.service.contract.ContractCrudService.ContractDto;
import uo.ri.cws.application.service.contract.ContractCrudService.ContractSummaryDto;

public class ContractPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private ContractCrudService service = 
    		Factories.service.forContractCrudService();

    private DefaultTableModel model;
    private JTable table;

    private static final String[] COLUMNS =
        { "ID", "Mechanic NIF", "Settlement", "# Payrolls", "State" };

    public ContractPanel() {
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

        JButton btnRefresh   = new JButton("Refresh");
        JButton btnAdd       = new JButton("Add...");
        JButton btnDetails   = new JButton("Details...");
        JButton btnUpdate    = new JButton("Update...");
        JButton btnTerminate = new JButton("Terminate");
        JButton btnDelete    = new JButton("Delete");

        p.add(btnRefresh);
        p.add(btnAdd);
        p.add(btnDetails);
        p.add(btnUpdate);
        p.add(btnTerminate);
        p.add(btnDelete);

        btnRefresh.addActionListener(e -> refresh());

        btnAdd.addActionListener(e -> {
            ContractDto dto = new ContractDto();
            if (showAddDialog(dto)) {
                try {
                    service.create(dto);
                    refresh();
                } catch (Exception ex) {
                    showError(ex);
                }
            }
        });

        btnDetails.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(
            		this, "Select a contract first."); return; }
            String id = (String) model.getValueAt(row, 0);
            try {
                ContractDto dto = service.findById(id).orElseThrow();
                showDetails(dto);
            } catch (Exception ex) {
                showError(ex);
            }
        });

        btnUpdate.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(
            		this, "Select a contract first."); return; }
            String id = (String) model.getValueAt(row, 0);
            try {
                ContractDto dto = service.findById(id).orElseThrow();
                if (showUpdateDialog(dto)) {
                    service.update(dto);
                    refresh();
                }
            } catch (Exception ex) {
                showError(ex);
            }
        });

        btnTerminate.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(
            		this, "Select a contract first."); return; }
            String id = (String) model.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this,
                "Terminate contract " + id + "?", "Confirm", 
                JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    service.terminate(id);
                    refresh();
                } catch (Exception ex) {
                    showError(ex);
                }
            }
        });

        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(
            		this, "Select a contract first."); return; }
            String id = (String) model.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this,
                "Delete contract " + id + "?", "Confirm", 
                JOptionPane.YES_NO_OPTION);
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

    private boolean showAddDialog(ContractDto dto) {
        JTextField fNif     = new JTextField(15);
        JTextField fType    = new JTextField("PERMANENT", 15);
        JTextField fGroup   = new JTextField("I", 10);
        JTextField fSalary  = new JTextField("20000.0", 10);
        JTextField fEndDate = new JTextField("", 12);

        JPanel form = new JPanel(new GridLayout(0, 2, 5, 5));
        form.add(new JLabel("Mechanic NIF:"));                    
        form.add(fNif);
        form.add(new JLabel("Contract type (PERMANENT/FIXED_TERM/SEASONAL):")); 
        form.add(fType);
        form.add(new JLabel("Professional group:"));              
        form.add(fGroup);
        form.add(new JLabel("Annual base salary:"));              
        form.add(fSalary);
        form.add(new JLabel("End date (yyyy-MM-dd, optional):")); 
        form.add(fEndDate);

        int result = JOptionPane.showConfirmDialog(this, form, "Add Contract",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            dto.mechanic.nif              = fNif.getText().trim();
            dto.contractType.name         = fType.getText().trim();
            dto.professionalGroup.name    = fGroup.getText().trim();
            try {
                dto.annualBaseSalary = Double.parseDouble(fSalary.getText().trim());
            } catch (NumberFormatException ex) {
                showError(new Exception("Invalid salary value")); return false;
            }
            String ed = fEndDate.getText().trim();
            if (!ed.isEmpty()) {
                try { dto.endDate = LocalDate.parse(ed); }
                catch (Exception ex) { showError(
                		new Exception("Invalid date (use yyyy-MM-dd)")); return false; }
            }
            return true;
        }
        return false;
    }

    private boolean showUpdateDialog(ContractDto dto) {
        JTextField fSalary  = new JTextField(String.valueOf(
        		dto.annualBaseSalary), 10);
        JTextField fEndDate = new JTextField(
        		dto.endDate != null ? dto.endDate.toString() : "", 12);

        JPanel form = new JPanel(new GridLayout(0, 2, 5, 5));
        form.add(new JLabel("Annual base salary:"));              
        form.add(fSalary);
        form.add(new JLabel("End date (yyyy-MM-dd, optional):")); 
        form.add(fEndDate);

        int result = JOptionPane.showConfirmDialog(this, form, "Update Contract",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                dto.annualBaseSalary = Double.parseDouble(fSalary.getText().trim());
            } catch (NumberFormatException ex) {
                showError(new Exception("Invalid salary value")); return false;
            }
            String ed = fEndDate.getText().trim();
            dto.endDate = ed.isEmpty() ? null : LocalDate.parse(ed);
            return true;
        }
        return false;
    }

    private void showDetails(ContractDto c) {
        String msg = String.format(
            "ID:              %s%n" +
            "Mechanic:        %s %s (NIF: %s)%n" +
            "Type:            %s%n" +
            "Prof. Group:     %s%n" +
            "Start date:      %s%n" +
            "End date:        %s%n" +
            "Base salary:     %.2f%n" +
            "Tax rate:        %.2f%n" +
            "Settlement:      %.2f%n" +
            "State:           %s",
            c.id,
            c.mechanic.name, c.mechanic.surname, c.mechanic.nif,
            c.contractType.name,
            c.professionalGroup.name,
            c.startDate,
            c.endDate != null ? c.endDate : "---",
            c.annualBaseSalary,
            c.taxRate,
            c.settlement,
            c.state
        );
        JOptionPane.showMessageDialog(
        		this, msg, "Contract Details", JOptionPane.INFORMATION_MESSAGE);
    }

    private void refresh() {
        model.setRowCount(0);
        try {
            List<ContractSummaryDto> list = service.findAll();
            for (ContractSummaryDto c : list) {
                model.addRow(new Object[]{ 
                		c.id, c.nif, c.settlement, c.numPayrolls, c.state });
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
