package panes;


import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import core.SharedObjs;

import java.util.List;

import supportive.UnZip;


public class UnzippingCrsDialog extends JDialog {

	private static final long serialVersionUID = -3530845640346420512L;
	private final JPanel contentPanel = new JPanel();
	private JTextPane textPane;
	
	List<String> fileList;
	static UnZip unZip;
	static Process p;
	
	String path;
	static String INPUT_ZIP_FOLDER;
	static String file;


	/**
	 * Create the dialog.
	 */
	public UnzippingCrsDialog(String path) {
		setFont(new Font("Dialog", Font.PLAIN, 16));
		setTitle("Please wait ...");
		setBounds((int) SharedObjs.satFrame.getBounds().getCenterX() - 200, 150, 400, 210);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		textPane = new JTextPane();
		textPane.setBounds(10, 29, 364, 81);
		textPane.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textPane.setEditable(false);
		textPane.setBackground(UIManager.getColor("Button.background"));
		textPane.setText("Tool is extracting the CRs and running the build_report.pl inside each one\nIt may take a while, please wait ...");
		contentPanel.add(textPane);
		
		System.out.println("Unzipping start process.");
		
		new Thread(new UnZip(path)).start();
		
		setModal(true);
		setVisible(true);
	}
	
	public void changeText(String text){
		textPane.setText(text);
	}
}
