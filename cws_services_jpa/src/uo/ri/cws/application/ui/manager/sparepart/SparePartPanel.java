package uo.ri.cws.application.ui.manager.sparepart;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class SparePartPanel extends JPanel {
    
    private static final long serialVersionUID = 1L;
    
    public SparePartPanel() {
        initializeUI();
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel label = new JLabel("Not yet implemented", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        
        add(label, BorderLayout.CENTER);
    }
}
