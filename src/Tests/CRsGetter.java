package Tests;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.JLabel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JComboBox;
import java.awt.Insets;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JTable;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class CRsGetter extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -726674919023554964L;
	WebDriver driver;
	FirefoxProfile profile;
	private JPanel contentPane;
	private String[] ProductsArray;
	private JComboBox<String> products;
	private JButton btnAdd;
	private JButton btnRemove;
	private JTable table;
	private JLabel lblUserJiraLogin;
	private JTextField txtUsername;
	private JPasswordField passwordField;
	private JButton btnGetCrs;
	private JButton btnLoadCrs;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CRsGetter frame = new CRsGetter();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public CRsGetter() {
		ProductsArray = new String[10];
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 666, 519);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 100, 100, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, 1.0, 1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		JLabel lblProductList = new JLabel("Product List:");
		lblProductList.setFont(new Font("Tahoma", Font.PLAIN, 18));
		GridBagConstraints gbc_lblProductList = new GridBagConstraints();
		gbc_lblProductList.insets = new Insets(0, 0, 5, 5);
		gbc_lblProductList.gridx = 0;
		gbc_lblProductList.gridy = 0;
		contentPane.add(lblProductList, gbc_lblProductList);
		
		products = new JComboBox<String>();
		GridBagConstraints gbc_products = new GridBagConstraints();
		gbc_products.weighty = 1.0;
		gbc_products.weightx = 50.0;
		gbc_products.insets = new Insets(0, 0, 5, 5);
		gbc_products.fill = GridBagConstraints.HORIZONTAL;
		gbc_products.gridx = 0;
		gbc_products.gridy = 2;
		loadProducts();
		
		lblUserJiraLogin = new JLabel("Jira Login:");
		lblUserJiraLogin.setFont(new Font("Tahoma", Font.BOLD, 11));
		GridBagConstraints gbc_lblUserJiraLogin = new GridBagConstraints();
		gbc_lblUserJiraLogin.weighty = 1.0;
		gbc_lblUserJiraLogin.weightx = 1.0;
		gbc_lblUserJiraLogin.anchor = GridBagConstraints.EAST;
		gbc_lblUserJiraLogin.insets = new Insets(0, 0, 5, 5);
		gbc_lblUserJiraLogin.gridx = 0;
		gbc_lblUserJiraLogin.gridy = 1;
		contentPane.add(lblUserJiraLogin, gbc_lblUserJiraLogin);
		
		txtUsername = new JTextField();
		txtUsername.setText("Username");
		GridBagConstraints gbc_txtUsername = new GridBagConstraints();
		gbc_txtUsername.weightx = 1.0;
		gbc_txtUsername.weighty = 1.0;
		gbc_txtUsername.insets = new Insets(0, 0, 5, 5);
		gbc_txtUsername.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtUsername.gridx = 1;
		gbc_txtUsername.gridy = 1;
		contentPane.add(txtUsername, gbc_txtUsername);
		txtUsername.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setText("Password");
		GridBagConstraints gbc_passwordField = new GridBagConstraints();
		gbc_passwordField.weighty = 1.0;
		gbc_passwordField.weightx = 1.0;
		gbc_passwordField.insets = new Insets(0, 0, 5, 0);
		gbc_passwordField.fill = GridBagConstraints.HORIZONTAL;
		gbc_passwordField.gridx = 2;
		gbc_passwordField.gridy = 1;
		contentPane.add(passwordField, gbc_passwordField);
		contentPane.add(products, gbc_products);
		
		btnAdd = new JButton("Add...");
		GridBagConstraints gbc_btnAdd = new GridBagConstraints();
		gbc_btnAdd.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnAdd.weighty = 1.0;
		gbc_btnAdd.weightx = 1.0;
		gbc_btnAdd.insets = new Insets(0, 0, 5, 5);
		gbc_btnAdd.gridx = 1;
		gbc_btnAdd.gridy = 2;
		contentPane.add(btnAdd, gbc_btnAdd);
		
		btnRemove = new JButton("Remove");
		GridBagConstraints gbc_btnRemove = new GridBagConstraints();
		gbc_btnRemove.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnRemove.weighty = 1.0;
		gbc_btnRemove.weightx = 1.0;
		gbc_btnRemove.insets = new Insets(0, 0, 5, 0);
		gbc_btnRemove.gridx = 2;
		gbc_btnRemove.gridy = 2;
		contentPane.add(btnRemove, gbc_btnRemove);
		
		
		GridBagConstraints gbc_table = new GridBagConstraints();
		gbc_table.gridheight = 2;
		gbc_table.weightx = 1.0;
		gbc_table.weighty = 100.0;
		table = new JTable();
		gbc_table.insets = new Insets(0, 0, 0, 5);
		gbc_table.fill = GridBagConstraints.BOTH;
		gbc_table.gridx = 0;
		gbc_table.gridy = 3;
		
		contentPane.add(table, gbc_table);
		
		btnLoadCrs = new JButton("Load CRs");
		GridBagConstraints gbc_btnLoadCrs = new GridBagConstraints();
		gbc_btnLoadCrs.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnLoadCrs.insets = new Insets(0, 0, 5, 0);
		gbc_btnLoadCrs.gridx = 2;
		gbc_btnLoadCrs.gridy = 3;
		contentPane.add(btnLoadCrs, gbc_btnLoadCrs);
		
		btnGetCrs = new JButton("Open CRs");
		btnGetCrs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnGetCrs.setVerticalAlignment(SwingConstants.TOP);
		GridBagConstraints gbc_btnGetCrs = new GridBagConstraints();
		gbc_btnGetCrs.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnGetCrs.anchor = GridBagConstraints.NORTH;
		gbc_btnGetCrs.weightx = 1.0;
		gbc_btnGetCrs.weighty = 1.0;
		gbc_btnGetCrs.gridx = 2;
		gbc_btnGetCrs.gridy = 4;
		contentPane.add(btnGetCrs, gbc_btnGetCrs);
	}
	
	
	
	public void loadProducts(){
		File f = new File("Data\\cfgs\\products.db");
		if (f.isFile() && f.canRead()) {
			try {
				BufferedReader in = new BufferedReader(new FileReader(f));
				String load;
				int i = 0;
				while ((load = in.readLine()) != null) {
					products.addItem(load);
					load = in.readLine();
					ProductsArray[i] = load;
				}
				in.close();
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	
	
	@SuppressWarnings("unused")
	private void initialize(){
		System.out.println("Loading Profile ...");
		profile = new FirefoxProfile(new File("Data\\complements\\profiles\\y2fvgaq0.bot"));
		driver = new FirefoxDriver(profile);
		System.out.println("Done.");
	}

}
