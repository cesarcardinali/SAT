package Tests;

import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.Scanner;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import javax.swing.JRadioButton;
import java.awt.FlowLayout;

public class Chrome extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3126460141110108385L;
	private JPanel contentPane;
	
	WebDriver driver;
	String user, pass;
	String CRs[];
	JTextField userField;
	JPasswordField passField;
	JTextPane textPane;
	JCheckBox chckbxRemember;
	JCheckBox chckbxInsertLabels;
	JRadioButton rdbtnChrome;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Chrome frame = new Chrome();
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
	public Chrome() {

		setMinimumSize(new Dimension(200, 500));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 203, 697);
		contentPane = new JPanel();
		contentPane.setMaximumSize(new Dimension(300, 32767));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{179, 180, 0};
		gbl_contentPane.rowHeights = new int[] {22, 22, 22, 0, 18, 18, 18, 18, 18, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0, 1.0, 1.0, 1.0, 1.0, 10.0, 1.0};
		contentPane.setLayout(gbl_contentPane);
		
		JLabel lblNaoSeiNome = new JLabel("Insert the CRs");
		lblNaoSeiNome.setMinimumSize(new Dimension(190, 20));
		lblNaoSeiNome.setPreferredSize(new Dimension(185, 20));
		lblNaoSeiNome.setHorizontalAlignment(SwingConstants.CENTER);
		lblNaoSeiNome.setMaximumSize(new Dimension(191, 20));
		lblNaoSeiNome.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 24));
		GridBagConstraints gbc_lblNaoSeiNome = new GridBagConstraints();
		gbc_lblNaoSeiNome.anchor = GridBagConstraints.NORTH;
		gbc_lblNaoSeiNome.weighty = 1.0;
		gbc_lblNaoSeiNome.weightx = 1.0;
		gbc_lblNaoSeiNome.gridwidth = 2;
		gbc_lblNaoSeiNome.insets = new Insets(3, 2, 5, 2);
		gbc_lblNaoSeiNome.gridx = 0;
		gbc_lblNaoSeiNome.gridy = 0;
		contentPane.add(lblNaoSeiNome, gbc_lblNaoSeiNome);
		
		JLabel lblUsername = new JLabel("Username: ");
		lblUsername.setPreferredSize(new Dimension(100, 20));
		lblUsername.setMinimumSize(new Dimension(100, 20));
		lblUsername.setMaximumSize(new Dimension(100, 20));
		lblUsername.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_lblUsername = new GridBagConstraints();
		gbc_lblUsername.anchor = GridBagConstraints.EAST;
		gbc_lblUsername.weighty = 1.0;
		gbc_lblUsername.weightx = 1.0;
		gbc_lblUsername.insets = new Insets(1, 5, 0, 0);
		gbc_lblUsername.gridx = 0;
		gbc_lblUsername.gridy = 1;
		contentPane.add(lblUsername, gbc_lblUsername);
		
		userField = new JTextField();
		userField.setMargin(new Insets(0, 2, 0, 2));
		userField.setMaximumSize(new Dimension(101, 2147483647));
		userField.setHorizontalAlignment(SwingConstants.LEFT);
		userField.setPreferredSize(new Dimension(100, 20));
		userField.setMinimumSize(new Dimension(100, 20));
		GridBagConstraints gbc_userField = new GridBagConstraints();
		gbc_userField.anchor = GridBagConstraints.WEST;
		gbc_userField.insets = new Insets(1, 0, 0, 5);
		gbc_userField.weighty = 1.0;
		gbc_userField.weightx = 1.0;
		gbc_userField.gridx = 1;
		gbc_userField.gridy = 1;
		contentPane.add(userField, gbc_userField);
		userField.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password: ");
		lblPassword.setMaximumSize(new Dimension(100, 20));
		lblPassword.setPreferredSize(new Dimension(100, 20));
		lblPassword.setMinimumSize(new Dimension(100, 20));
		lblPassword.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_lblPassword = new GridBagConstraints();
		gbc_lblPassword.anchor = GridBagConstraints.EAST;
		gbc_lblPassword.insets = new Insets(0, 5, 0, 0);
		gbc_lblPassword.weighty = 1.0;
		gbc_lblPassword.weightx = 1.0;
		gbc_lblPassword.gridx = 0;
		gbc_lblPassword.gridy = 2;
		contentPane.add(lblPassword, gbc_lblPassword);
		
		passField = new JPasswordField();
		passField.setMargin(new Insets(0, 2, 0, 2));
		passField.setMaximumSize(new Dimension(101, 2147483647));
		passField.setHorizontalAlignment(SwingConstants.LEFT);
		passField.setPreferredSize(new Dimension(100, 20));
		passField.setMinimumSize(new Dimension(100, 20));
		GridBagConstraints gbc_passField = new GridBagConstraints();
		gbc_passField.anchor = GridBagConstraints.WEST;
		gbc_passField.insets = new Insets(0, 0, 0, 5);
		gbc_passField.weighty = 1.0;
		gbc_passField.weightx = 1.0;
		gbc_passField.gridx = 1;
		gbc_passField.gridy = 2;
		contentPane.add(passField, gbc_passField);
		passField.setColumns(10);
		
		JPanel chcbxPanel = new JPanel();
		GridBagConstraints gbc_chcbxPanel = new GridBagConstraints();
		gbc_chcbxPanel.gridwidth = 2;
		gbc_chcbxPanel.insets = new Insets(0, 0, 5, 0);
		gbc_chcbxPanel.fill = GridBagConstraints.BOTH;
		gbc_chcbxPanel.gridx = 0;
		gbc_chcbxPanel.gridy = 3;
		contentPane.add(chcbxPanel, gbc_chcbxPanel);
		chcbxPanel.setLayout(new BoxLayout(chcbxPanel, BoxLayout.Y_AXIS));
		
		chckbxRemember = new JCheckBox("Remember?");
		chckbxRemember.setMargin(new Insets(0, 2, 0, 2));
		chckbxRemember.setAlignmentX(Component.CENTER_ALIGNMENT);
		chcbxPanel.add(chckbxRemember);
		chckbxRemember.setMaximumSize(new Dimension(171, 23));
		chckbxRemember.setHorizontalAlignment(SwingConstants.CENTER);
		chckbxRemember.setPreferredSize(new Dimension(170, 20));
		chckbxRemember.setMinimumSize(new Dimension(169, 20));
		chckbxRemember.setSelected(true);
		
		chckbxInsertLabels = new JCheckBox("Insert Labels?");
		chckbxInsertLabels.setMargin(new Insets(0, 2, 0, 2));
		chckbxInsertLabels.setAlignmentX(Component.CENTER_ALIGNMENT);
		chcbxPanel.add(chckbxInsertLabels);
		chckbxInsertLabels.setSelected(true);
		chckbxInsertLabels.setHorizontalAlignment(SwingConstants.CENTER);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setMaximumSize(new Dimension(152, 900));
		scrollPane.setPreferredSize(new Dimension(151, 450));
		scrollPane.setMinimumSize(new Dimension(150, 400));
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridwidth = 2;
		gbc_scrollPane.fill = GridBagConstraints.VERTICAL;
		gbc_scrollPane.weighty = 1.0;
		gbc_scrollPane.weightx = 1.0;
		gbc_scrollPane.gridheight = 5;
		gbc_scrollPane.insets = new Insets(0, 5, 5, 5);
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 4;
		contentPane.add(scrollPane, gbc_scrollPane);
		
		textPane = new JTextPane();
		textPane.setMaximumSize(new Dimension(150, 2147483647));
		scrollPane.setViewportView(textPane);
		
		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.gridwidth = 2;
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 9;
		contentPane.add(panel, gbc_panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		JPanel panel_1 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_1.getLayout();
		flowLayout.setVgap(1);
		/*panel.add(panel_1);
		
		ButtonGroup browser = new ButtonGroup();
		
		rdbtnChrome = new JRadioButton("Chrome");
		panel_1.add(rdbtnChrome);
		
		JRadioButton rdbtnFirefox = new JRadioButton("Firefox");
		panel_1.add(rdbtnFirefox);
		
		browser.add(rdbtnChrome);
		browser.add(rdbtnFirefox);*/
		
		JButton btnClear = new JButton("Clear");
		btnClear.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.add(btnClear);
		btnClear.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnClear.setPreferredSize(new Dimension(100, 40));
		btnClear.setMaximumSize(new Dimension(100, 40));
		btnClear.setMinimumSize(new Dimension(100, 40));
		
		JButton button = new JButton("Paste");
		button.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.add(button);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnColarActionPerformed();
			}
		});
		button.setPreferredSize(new Dimension(100, 40));
		button.setMinimumSize(new Dimension(100, 40));
		button.setMaximumSize(new Dimension(100, 40));
		button.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		JButton btnOpen = new JButton("<html><center> Assign / <br>Download</center></html>");
		btnOpen.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.add(btnOpen);
		btnOpen.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnOpen.setMaximumSize(new Dimension(100, 40));
		btnOpen.setPreferredSize(new Dimension(100, 40));
		btnOpen.setMinimumSize(new Dimension(100, 40));
		
		JButton button_1 = new JButton("<html><center> Just <br>Open</center></html>");
		button_1.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.add(button_1);
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnJustOpenActionPerformed();
			}
		});
		button_1.setPreferredSize(new Dimension(100, 40));
		button_1.setMinimumSize(new Dimension(100, 40));
		button_1.setMaximumSize(new Dimension(100, 40));
		button_1.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Save login
				if(chckbxRemember.isSelected()){
					File f = new File("Data\\cfgs\\log.ini");
					BufferedWriter out;
					try {
						out = new BufferedWriter(new FileWriter(f));
						out.write(userField.getText());
						out.newLine();
						out.write(passField.getPassword());
						out.close();
						System.out.println("File saved ; )");
					} catch(Exception e){
						e.printStackTrace();
					}
				} else {
					File f = new File("Data\\cfgs\\log.ini");
					if(f.exists())
						f.delete();
				}
				
				//Initialize variables and configurations
				initialize();
				
				//Open Firefox browser
				openBrowser();
				
				//Open and download the CRs
				try {
					runCRs();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				//Save profile
				//saveProfile();
			}
		});
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cleanText();
			}
		});
		
		
		File f = new File("Data\\cfgs\\log.ini");
		BufferedReader in;
		try {
			in = new BufferedReader(new FileReader(f));
			userField.setText(in.readLine());
			passField.setText(in.readLine());
			in.close();
			System.out.println("File saved ; )");
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	
	
	/**
	 * Core functions
	 */
	private void initialize(){		
		System.out.println("Generating Chrome profile");

		// Optional, if not specified, WebDriver will search your path for chromedriver.
		System.setProperty("webdriver.chrome.driver", "Data\\complements\\chromedriver.exe");

		ChromeOptions options = new ChromeOptions();
		//options.addArguments("--user-data-dir=C:\\Users\\cesar.cardinali\\AppData\\Local\\Google\\Chrome\\User Data");
		//options.addArguments("--start-maximized");
		
		driver = new ChromeDriver(options);
		
		//Configure motorola user account
		updateUserdata();
	}
	
	private boolean openBrowser(){
		// Open up a browser
		System.out.println("Starting browser");
		driver.navigate().to("http://idart.mot.com/login.jsp");
		System.out.println("Done.");
		return true;
	}
	
	

	private void cleanText() {
		textPane.setText("");
	}
	
	private void updateUserdata(){
		user = userField.getText();
		pass = String.copyValueOf(passField.getPassword());
	}
	
	
	private void sleep(int millis){
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private boolean jiraLogin(){
		if(driver.getTitle().contains("Log in")){
			//System.out.println("Trying to Log in");
			driver.findElement(By.name("os_username")).sendKeys(user);		
			driver.findElement(By.name("os_password")).sendKeys(pass);
			driver.findElement(By.name("os_cookie")).click();
			driver.findElement(By.name("login")).click();
			//sleep(1500);
			if(driver.getTitle().contains("Log in")){
				return false;
			}
			else{
				//System.out.println("Done.");
				return true;
			}
		}
		else{
			System.out.println("Already logged in");
			return true;
		}
	}
	
	private boolean assignCR(){
		try {
			System.out.println("Trying to assign");
			driver.findElement(By.className("issueaction-assign-to-me")).click();
			System.out.println("Done.");
			sleep(1000);
		} catch (Exception e1) {
			System.out.println("CR assigned already");
		}
		return true;
	}
	
	private boolean addLabel(){
		try {
			System.out.println("Trying to insert label");
			
			//Firefox way to send a ESCAPE key
			driver.findElement(By.cssSelector("body")).sendKeys(Keys.ESCAPE);
			
			sleep(500);
			driver.findElement(By.cssSelector("body")).sendKeys(".");
			sleep(800);
			driver.findElement(By.id("shifter-dialog-field")).sendKeys("label");
			sleep(800);
			driver.findElement(By.id("shifter-dialog-field")).sendKeys(Keys.ENTER);
			sleep(1100);
			driver.findElement(By.id("labels-textarea")).sendKeys("ll_prodteam_analyzed");
			sleep(1100);
			driver.findElement(By.id("labels-textarea")).sendKeys(Keys.ENTER);
			sleep(1010);
			driver.findElement(By.id("issue-workflow-transition-submit")).click();
			
			System.out.println("Label inserted");
			sleep(1010);
			return true;
		} catch (Exception e1) {
			System.out.println("Label error");
			e1.printStackTrace();
			return false;
		}
	}
	
	String b2glink = "";
	private boolean openB2G(){
		try
		{
			System.out.println("Opening Bug2Go");
			sleep(500);
			String aux = b2glink;
			while(aux.equals(b2glink)){
				WebElement Element = driver.findElement(By.partialLinkText("b2gadm-mcloud101-blur"));
				System.out.println("B2g id: " + Element.getText().substring(Element.getText().indexOf('=') + 1));
				b2glink = Element.getText();
				System.out.println("B2g link: " + b2glink);
				Element.click();
			}
			
			if(driver.getTitle().contains("MOTOROLA")){
				System.out.println("Login to Bug2Go");
				driver.findElement(By.id("username")).sendKeys(user);		
				driver.findElement(By.id("password")).sendKeys(pass);
				driver.findElement(By.className("input_submit")).click();
			}
			
			return true;
		} catch (Exception e){
			e.printStackTrace();
			driver.navigate().back();
			return false;
		}
	}
	
	private boolean downloadCR(){
		while(!verifyDownloadPage()){
			System.out.println("verify download - 1");
		}
		System.out.println("B2G page loaded");
		
		System.out.println("Clicking download");
		driver.findElement(By.className("bg_btn")).click();
		sleep(2500);
		
		if(driver.getTitle().contains("Bug2Go--")){
			System.out.println("Download failed\nRetry...");
			return false;
		}
		System.out.println("Downloading");
		
		return true;
	}
	
	private boolean verifyDownloadPage(){
		System.out.println("Verifying download page");
		if(driver.getTitle().contains("Bug2Go--")){
			System.out.println("B2G page failed. Retrying ...");
			try {
				System.out.println("Refreshing page");
				driver.navigate().to(b2glink);
				return false;
			} catch (Exception e){
				System.out.println("Driver Error");
				return false;
			}
		}
		
		return true;
	}
	
	int issue;
	private void runCRs() throws IOException {
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(new File("log.txt")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		CRs = textPane.getText().split("\n");
		bw.write("CRs liste\n");
		for (int i = 0; i < CRs.length; i++) {
			CRs[i] = CRs[i].replaceAll("\n", "").replaceAll("\r", "");
			System.out.println("String: " + CRs[i]);
			bw.write(CRs[i] + "\n");
		}
		
		//boolean login=false, assign=false, label=false, download=false;
		int flow = 0;
		
		for (int i = 0; i < CRs.length; i++) {
			issue = 0;
			
			//Open page
			System.out.println("Opening CR page");
			driver.navigate().to("http://idart.mot.com/browse/" + CRs[i]);
			System.out.println("Navegando para: http://idart.mot.com/browse/" + CRs[i]);
			bw.write("Navegando para: http://idart.mot.com/browse/" + CRs[i]);
			System.out.println("Done.");
			
			
			//Log in
			System.out.println("Trying to login.");
			jiraLogin();
			
			//System.out.println(driver.getPageSource());
			
			//Check if login successful
			while(driver.getTitle().contains("Log")){
				System.out.println("Checking for log in error");
				if (driver.getPageSource().contains("Sorry, your username and password are incorrect - please try again.")){
					JOptionPane.showMessageDialog(this, "Username and password are incorrect.\nPlease correct them and try again\n" /*+ e.getMessage()*/);
					System.out.println("An error occurred. Canceling process.");
					flow++;
					break;
				} else {
					System.out.println("No login errors");
					//login = true;
				}
				System.out.println("Browser still loading at login page. Waiting a sec ...");
				sleep(1000);
			}
			if(flow != 0){
				driver.close();
				break;
			}
			System.out.println("Done.");
			
			
			//Assign
			while(!assignCR()){}
			System.out.println("Assign Done");
			
			if(chckbxInsertLabels.isSelected()){
				while(!addLabel()){}
				System.out.println("Label Done");
			} else {
				System.out.println("Skipping Label Inserting");
			}
			while(!openB2G()){}
			System.out.println("Open B2G Done");
			while(!downloadCR()){}
			System.out.println("Downloading CR");
			sleep(1000);
			
		}
		driver.navigate().to("http://done");
		System.out.println("All Finished");
		bw.flush();  
		bw.close();
	}
	
	
	private void btnColarActionPerformed() {
		textPane.setText(null);

		Scanner scanner;
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		try {
			String string = (String) clipboard.getData(DataFlavor.stringFlavor);

			scanner = new Scanner(string);
			String str;
			while (scanner.hasNext()) {
				str = scanner.nextLine();
				textPane.setText(textPane.getText() + str + "\n");
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Exception: " + ex.getMessage());
		}
	}
	
	
	private void btnJustOpenActionPerformed() {
		for (String s : textPane.getText().split("\n")) {
			try {
				s = s.replaceAll("\n", "");
				s = s.replaceAll("\r", "");
				System.out.println("String: " + s);
				Desktop.getDesktop().browse(new URI("http://idart.mot.com/browse/" + s));
				Thread.sleep(550);
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Exception: " + ex.getMessage());
			}
		}
	}
	
}