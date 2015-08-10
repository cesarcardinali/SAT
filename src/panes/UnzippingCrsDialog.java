package panes;

import java.awt.BorderLayout;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextPane;
import javax.swing.UIManager;

import supportive.UnZip;
import main.SAT;

import java.awt.Font;
import java.util.List;

public class UnzippingCrsDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3530845640346420512L;
	private final JPanel contentPanel = new JPanel();
	private JTextPane textPane;
	
	List<String> fileList;
	static UnZip unZip;
	static Process p;
	
	String path;
	static String INPUT_ZIP_FOLDER;
	static String file;
	static SAT Parent;


	/**
	 * Create the dialog.
	 */
	public UnzippingCrsDialog(SAT parent, String path) {
		Parent = parent;
		setFont(new Font("Dialog", Font.PLAIN, 16));
		setTitle("Please wait ...");
		setBounds((int) parent.getFrame().getBounds().getCenterX() - 200, 150, 400, 210);
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
		
		new Thread(new UnZip(path, parent)).start();
		
		setModal(true);
		setVisible(true);
	}
	
	public void changeText(String text){
		textPane.setText(text);
	}
	
	public SAT getFrame(){
		return Parent;
	}
}
