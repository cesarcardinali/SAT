package panes;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import core.SharedObjs;

import supportive.DiagCrsCloser;


public class ClosingDiagDialog extends JDialog {

	private static final long serialVersionUID = 7336911787362736864L;
	private final JPanel contentPanel = new JPanel();
	private JTextPane txtpnCheckingAllThe;

	public ClosingDiagDialog() {
		setType(Type.POPUP);
		setBackground(UIManager.getColor("Panel.background"));
		setTitle("Please Wait ...");
		getContentPane().setBackground(UIManager.getColor("Panel.background"));
		setBounds(100, 100, 500, 450);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(UIManager.getColor("Panel.background"));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new GridLayout(0, 1, 0, 0));
		
		txtpnCheckingAllThe = new JTextPane();
		txtpnCheckingAllThe.setEditable(false);
		txtpnCheckingAllThe.setBackground(UIManager.getColor("Panel.background"));
		txtpnCheckingAllThe.setPreferredSize(new Dimension(300, 100));
		txtpnCheckingAllThe.setMinimumSize(new Dimension(300, 100));
		txtpnCheckingAllThe.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtpnCheckingAllThe.setText("This process will check all the CRs(zip) in specified folder and close the DIAG_WS issues on Jira ...\n- It may take a while\n" +
				"_______________________________________\n\n");
		contentPanel.add(txtpnCheckingAllThe);
		
		setLocationRelativeTo(SharedObjs.satFrame);
		
		new Thread(new DiagCrsCloser(this)).start();
		
		setModal(true);
		setVisible(true);
	}

	
	// Getters and Setters
	public void setText(String text){
		txtpnCheckingAllThe.setText(text);
	}
	
	public String getText(){
		return txtpnCheckingAllThe.getText();
	}
}
