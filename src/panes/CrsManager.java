package panes;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URI;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.commons.io.FileUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

import core.Icons;
import supportive.CrsCloser;
import supportive.Encryptation;
import supportive.UnZip;
import main.SAT;
import supportive.DiagCrsCloser;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class CrsManager extends JPanel {

	private static final long serialVersionUID = -1072184113058141060L;
	
	/**
	 * Global Variables
	 */
	private JTextArea textDownload;
	private JTextField textPath;
	private JTextField textUsername;
	private JTextPane textLog;
	private JTextPane textPane;
	private JPasswordField textPassword;
	private JCheckBox chkbxRemember;
	private JCheckBox chckbxAssign;
	private JCheckBox chckbxLabels;
	private JCheckBox chkBoxSelectCRs;
	private JCheckBox chkBoxSelectZips;
	
	private JList<String> listDiag;
	private JList<String> listCRs;
	private JList<String> listZips;
	private DefaultListModel<String> listModel;
	
	private WebDriver driver;
	private FirefoxProfile profile;
	private String user, pass;
	private String CRs[];
	private String b2glink = "";
	private String actualCR = "";
	private HashMap<String, String> b2g_crid;
	private HashMap<String, String> b2g_analyzed;
	private SAT BaseWindow;
	private ArrayList<String> listZipNames;
	private ArrayList<String> listFoldersNames;
	

	/**
	 * Create the panel.
	 */
	public CrsManager(SAT parent) {
		setPreferredSize(new Dimension(632, 765));
		setMinimumSize(new Dimension(600, 950));
		
		
		BaseWindow = parent;
		
		
		//Panel construction
		JPanel contentPane = new JPanel();
		add(contentPane);
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gridBagLayout);
		
		JPanel panel_1 = new JPanel();
		panel_1.setMinimumSize(new Dimension(150, 100));
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.insets = new Insets(10, 5, 5, 5);
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 0;
		gbc_panel_1.gridy = 0;
		contentPane.add(panel_1, gbc_panel_1);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[]{0, 0, 0};
		gbl_panel_1.rowHeights = new int[]{14, 0, 0, 0, 0, 0};
		gbl_panel_1.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_panel_1.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		panel_1.setLayout(gbl_panel_1);
		
		JLabel lblNewLabel = new JLabel("User Data:");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.gridwidth = 2;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		panel_1.add(lblNewLabel, gbc_lblNewLabel);
		
		JLabel lblUsername = new JLabel("Username");
		lblUsername.setPreferredSize(new Dimension(60, 14));
		lblUsername.setMinimumSize(new Dimension(60, 14));
		lblUsername.setMaximumSize(new Dimension(60, 14));
		lblUsername.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_lblUsername = new GridBagConstraints();
		gbc_lblUsername.weighty = 1.0;
		gbc_lblUsername.weightx = 1.0;
		gbc_lblUsername.anchor = GridBagConstraints.EAST;
		gbc_lblUsername.gridx = 0;
		gbc_lblUsername.gridy = 1;
		panel_1.add(lblUsername, gbc_lblUsername);
		
		textUsername = new JTextField();
		textUsername.setToolTipText("Motorola username");
		textUsername.setPreferredSize(new Dimension(90, 20));
		textUsername.setMinimumSize(new Dimension(90, 20));
		GridBagConstraints gbc_textUsername = new GridBagConstraints();
		gbc_textUsername.anchor = GridBagConstraints.WEST;
		gbc_textUsername.gridx = 1;
		gbc_textUsername.gridy = 1;
		panel_1.add(textUsername, gbc_textUsername);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPassword.setPreferredSize(new Dimension(60, 14));
		lblPassword.setMinimumSize(new Dimension(60, 14));
		lblPassword.setMaximumSize(new Dimension(60, 14));
		GridBagConstraints gbc_lblPassword = new GridBagConstraints();
		gbc_lblPassword.anchor = GridBagConstraints.EAST;
		gbc_lblPassword.gridx = 0;
		gbc_lblPassword.gridy = 2;
		panel_1.add(lblPassword, gbc_lblPassword);
		
		textPassword = new JPasswordField();
		textPassword.setToolTipText("Motorola password");
		textPassword.setPreferredSize(new Dimension(90, 20));
		textPassword.setMinimumSize(new Dimension(90, 20));
		GridBagConstraints gbc_textPassword = new GridBagConstraints();
		gbc_textPassword.anchor = GridBagConstraints.WEST;
		gbc_textPassword.gridx = 1;
		gbc_textPassword.gridy = 2;
		panel_1.add(textPassword, gbc_textPassword);
		
		chkbxRemember = new JCheckBox("Remember?");
		chkbxRemember.setToolTipText("Remember your login and password");
		chkbxRemember.setSelected(true);
		GridBagConstraints gbc_chkbxRemember = new GridBagConstraints();
		gbc_chkbxRemember.gridwidth = 2;
		gbc_chkbxRemember.gridx = 0;
		gbc_chkbxRemember.gridy = 3;
		panel_1.add(chkbxRemember, gbc_chkbxRemember);
		
		JSeparator separator_2 = new JSeparator();
		separator_2.setOrientation(SwingConstants.VERTICAL);
		separator_2.setForeground(Color.DARK_GRAY);
		GridBagConstraints gbc_separator_2 = new GridBagConstraints();
		gbc_separator_2.fill = GridBagConstraints.VERTICAL;
		gbc_separator_2.gridheight = 6;
		gbc_separator_2.insets = new Insets(0, 0, 5, 5);
		gbc_separator_2.gridx = 1;
		gbc_separator_2.gridy = 0;
		contentPane.add(separator_2, gbc_separator_2);
		
		JPanel panel_3 = new JPanel();
		panel_3.setMinimumSize(new Dimension(150, 10));
		GridBagConstraints gbc_panel_3 = new GridBagConstraints();
		gbc_panel_3.gridheight = 6;
		gbc_panel_3.insets = new Insets(10, 5, 5, 5);
		gbc_panel_3.fill = GridBagConstraints.BOTH;
		gbc_panel_3.gridx = 2;
		gbc_panel_3.gridy = 0;
		contentPane.add(panel_3, gbc_panel_3);
		GridBagLayout gbl_panel_3 = new GridBagLayout();
		gbl_panel_3.columnWidths = new int[]{0, 0};
		gbl_panel_3.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panel_3.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_panel_3.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel_3.setLayout(gbl_panel_3);
		
		JLabel lblDownloader = new JLabel("Downloader:");
		lblDownloader.setHorizontalAlignment(SwingConstants.CENTER);
		lblDownloader.setFont(new Font("Tahoma", Font.BOLD, 18));
		GridBagConstraints gbc_lblDownloader = new GridBagConstraints();
		gbc_lblDownloader.insets = new Insets(0, 0, 5, 0);
		gbc_lblDownloader.gridx = 0;
		gbc_lblDownloader.gridy = 0;
		panel_3.add(lblDownloader, gbc_lblDownloader);
		
		chckbxAssign = new JCheckBox("Assign?");
		chckbxAssign.setMargin(new Insets(0, 2, 0, 2));
		chckbxAssign.setPreferredSize(new Dimension(61, 15));
		chckbxAssign.setMinimumSize(new Dimension(15, 20));
		chckbxAssign.setSelected(true);
		GridBagConstraints gbc_chckbxAssign = new GridBagConstraints();
		gbc_chckbxAssign.gridx = 0;
		gbc_chckbxAssign.gridy = 1;
		panel_3.add(chckbxAssign, gbc_chckbxAssign);
		
		chckbxLabels = new JCheckBox("Labels?");
		chckbxLabels.setMargin(new Insets(0, 2, 0, 2));
		chckbxLabels.setMinimumSize(new Dimension(61, 15));
		chckbxLabels.setPreferredSize(new Dimension(61, 15));
		chckbxLabels.setSelected(true);
		GridBagConstraints gbc_chckbxLabels = new GridBagConstraints();
		gbc_chckbxLabels.insets = new Insets(0, 0, 5, 0);
		gbc_chckbxLabels.gridx = 0;
		gbc_chckbxLabels.gridy = 2;
		panel_3.add(chckbxLabels, gbc_chckbxLabels);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setPreferredSize(new Dimension(140, 350));
		scrollPane_2.setBorder(new LineBorder(SystemColor.activeCaption));
		scrollPane_2.setMinimumSize(new Dimension(140, 300));
		GridBagConstraints gbc_scrollPane_2 = new GridBagConstraints();
		gbc_scrollPane_2.fill = GridBagConstraints.VERTICAL;
		gbc_scrollPane_2.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane_2.gridx = 0;
		gbc_scrollPane_2.gridy = 3;
		panel_3.add(scrollPane_2, gbc_scrollPane_2);
		
		textDownload = new JTextArea();
		textDownload.setToolTipText("List of CRs to be downloaded. Used to create the link between downloded CRs and its Jira ID too.");
		textDownload.setFont(new Font("Tahoma", Font.PLAIN, 11));
		scrollPane_2.setViewportView(textDownload);
		textDownload.setTabSize(4);
		textDownload.setBorder(null);
		
		JButton btnClear = new JButton("Clear");
		btnClear.setToolTipText("Clear the text area above");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnClearAction();
			}
		});
		btnClear.setPreferredSize(new Dimension(113, 23));
		btnClear.setMaximumSize(new Dimension(113, 23));
		btnClear.setMinimumSize(new Dimension(113, 23));
		GridBagConstraints gbc_btnClear = new GridBagConstraints();
		gbc_btnClear.insets = new Insets(0, 0, 5, 0);
		gbc_btnClear.gridx = 0;
		gbc_btnClear.gridy = 4;
		panel_3.add(btnClear, gbc_btnClear);
		
		JButton btnPaste = new JButton("Paste");
		btnPaste.setToolTipText("Paste from clipboard");
		btnPaste.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnPasteAction();
			}
		});
		btnPaste.setMaximumSize(new Dimension(113, 23));
		btnPaste.setPreferredSize(new Dimension(113, 23));
		btnPaste.setMinimumSize(new Dimension(113, 23));
		GridBagConstraints gbc_btnPaste = new GridBagConstraints();
		gbc_btnPaste.insets = new Insets(0, 0, 5, 0);
		gbc_btnPaste.gridx = 0;
		gbc_btnPaste.gridy = 5;
		panel_3.add(btnPaste, gbc_btnPaste);
		
		JButton btnDownload = new JButton("Download");
		btnDownload.setToolTipText("Start to download the CRs on the list above");
		btnDownload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnDownloadAction();
			}
		});
		btnDownload.setPreferredSize(new Dimension(113, 23));
		btnDownload.setMaximumSize(new Dimension(113, 23));
		btnDownload.setMinimumSize(new Dimension(113, 23));
		GridBagConstraints gbc_btnDownload = new GridBagConstraints();
		gbc_btnDownload.insets = new Insets(0, 0, 5, 0);
		gbc_btnDownload.gridx = 0;
		gbc_btnDownload.gridy = 6;
		panel_3.add(btnDownload, gbc_btnDownload);
		
		JButton btnOpenOnChrome = new JButton("Open on Chrome");
		btnOpenOnChrome.setToolTipText("Open the CRs on the list above on Chrome");
		btnOpenOnChrome.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnOpenAction();
			}
		});
		GridBagConstraints gbc_btnOpenOnChrome = new GridBagConstraints();
		gbc_btnOpenOnChrome.gridx = 0;
		gbc_btnOpenOnChrome.gridy = 7;
		panel_3.add(btnOpenOnChrome, gbc_btnOpenOnChrome);
		
		JSeparator separator_3 = new JSeparator();
		separator_3.setForeground(Color.DARK_GRAY);
		separator_3.setBackground(Color.WHITE);
		separator_3.setOrientation(SwingConstants.VERTICAL);
		GridBagConstraints gbc_separator_3 = new GridBagConstraints();
		gbc_separator_3.fill = GridBagConstraints.VERTICAL;
		gbc_separator_3.gridheight = 6;
		gbc_separator_3.insets = new Insets(0, 0, 5, 5);
		gbc_separator_3.gridx = 3;
		gbc_separator_3.gridy = 0;
		contentPane.add(separator_3, gbc_separator_3);
		
		JPanel panel_4 = new JPanel();
		GridBagConstraints gbc_panel_4 = new GridBagConstraints();
		gbc_panel_4.insets = new Insets(10, 5, 5, 0);
		gbc_panel_4.gridheight = 6;
		gbc_panel_4.fill = GridBagConstraints.BOTH;
		gbc_panel_4.gridx = 4;
		gbc_panel_4.gridy = 0;
		contentPane.add(panel_4, gbc_panel_4);
		GridBagLayout gbl_panel_4 = new GridBagLayout();
		gbl_panel_4.columnWidths = new int[]{0, 0};
		gbl_panel_4.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panel_4.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_panel_4.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel_4.setLayout(gbl_panel_4);
		
		JLabel lblDiagCloser = new JLabel("Diag Closer:");
		lblDiagCloser.setMinimumSize(new Dimension(60, 14));
		lblDiagCloser.setFont(new Font("Tahoma", Font.BOLD, 18));
		GridBagConstraints gbc_lblDiagCloser = new GridBagConstraints();
		gbc_lblDiagCloser.insets = new Insets(0, 0, 5, 0);
		gbc_lblDiagCloser.gridx = 0;
		gbc_lblDiagCloser.gridy = 0;
		panel_4.add(lblDiagCloser, gbc_lblDiagCloser);
		
		JScrollPane scrollPane_5 = new JScrollPane();
		scrollPane_5.setBorder(new LineBorder(SystemColor.activeCaption));
		scrollPane_5.setPreferredSize(new Dimension(220, 360));
		scrollPane_5.setMinimumSize(new Dimension(220, 360));
		GridBagConstraints gbc_scrollPane_5 = new GridBagConstraints();
		gbc_scrollPane_5.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_5.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane_5.gridx = 0;
		gbc_scrollPane_5.gridy = 1;
		panel_4.add(scrollPane_5, gbc_scrollPane_5);
		
		listDiag = new JList<String>();
		scrollPane_5.setViewportView(listDiag);
		listDiag.setToolTipText("List of CRs to be checked");
		listDiag.setMaximumSize(new Dimension(2000, 2000));
		listDiag.setVisibleRowCount(0);
		listDiag.setBorder(null);
		listDiag.setMinimumSize(new Dimension(220, 360));
		listDiag.setModel(new DefaultListModel<String>());
		
		JLabel lblBgId = new JLabel("   B2G ID   -    Jira ID  -  Diag Result");
		lblBgId.setBorder(new LineBorder(new Color(0, 0, 0)));
		scrollPane_5.setColumnHeaderView(lblBgId);
		
		JTextArea txtrInstructionsHere = new JTextArea();
		txtrInstructionsHere.setTabSize(4);
		txtrInstructionsHere.setWrapStyleWord(true);
		txtrInstructionsHere.setEditable(false);
		txtrInstructionsHere.setLineWrap(true);
		txtrInstructionsHere.setMargin(new Insets(2, 5, 2, 5));
		txtrInstructionsHere.setBorder(new LineBorder(Color.RED));
		txtrInstructionsHere.setFont(new Font("Tahoma", Font.PLAIN, 11));
		txtrInstructionsHere.setText("Click on HELP button to learn how to use it");
		txtrInstructionsHere.setBackground(UIManager.getColor("Panel.background"));
		txtrInstructionsHere.setPreferredSize(new Dimension(220, 17));
		txtrInstructionsHere.setMinimumSize(new Dimension(220, 17));
		GridBagConstraints gbc_txtrInstructionsHere = new GridBagConstraints();
		gbc_txtrInstructionsHere.insets = new Insets(0, 0, 5, 0);
		gbc_txtrInstructionsHere.gridx = 0;
		gbc_txtrInstructionsHere.gridy = 2;
		panel_4.add(txtrInstructionsHere, gbc_txtrInstructionsHere);
		
		JButton btnHelp = new JButton("Help");
		btnHelp.setEnabled(false);
		btnHelp.setPreferredSize(new Dimension(70, 23));
		btnHelp.setToolTipText("How to use DIAG CLOSER.");
		btnHelp.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null,"First, download the CRs using the \"Downloader\" option\n" +
						"After download is done, just click \"Run\" and follow the instructions on the screen\n\n" +
						"If you have downloaded the CRs already, you need to insert the path where the ZIP files are.\n" +
						"and insert the CRs on the textarea of the Downloader option on the left.\n" +
						"Then, just click \"RUN\"\n\n" +
						"If you dont have the ZIP files anymore, sorry, but the tool is not prepared to this situation yet.\n\n" +
						"More options and more flexibility coming soon\n\n\n" +
						"Thank you, have a good day Sir.");
			}
		});
		GridBagConstraints gbc_btnHelp = new GridBagConstraints();
		gbc_btnHelp.insets = new Insets(0, 0, 5, 0);
		gbc_btnHelp.gridx = 0;
		gbc_btnHelp.gridy = 3;
		panel_4.add(btnHelp, gbc_btnHelp);
		
		JButton btnrun = new JButton("Run");
		btnrun.setEnabled(false);
		btnrun.setPreferredSize(new Dimension(70, 23));
		btnrun.setToolTipText("Start to check CRs and close Diag ones. Please, read HELP");
		btnrun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnRunDiag();
			}
		});
		GridBagConstraints gbc_btnrun = new GridBagConstraints();
		gbc_btnrun.insets = new Insets(0, 0, 5, 0);
		gbc_btnrun.gridx = 0;
		gbc_btnrun.gridy = 4;
		panel_4.add(btnrun, gbc_btnrun);
		
		JButton btnOpenDiagsOn = new JButton("Open Diags on Chrome");
		btnOpenDiagsOn.setEnabled(false);
		btnOpenDiagsOn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				HashMap<String, String> b2g_crid = DiagCrsCloser.getB2g_crid();
				ArrayList<String> getDiagCRs = DiagCrsCloser.getDiagCRs();
				for(String b2gid : getDiagCRs){
					try {
						String s = b2g_crid.get(b2gid);
						s = s.replaceAll("\n", "");
						s = s.replaceAll("\r", "");
						s = s.replaceAll(" |  ", "");
						Desktop.getDesktop().browse(new URI("http://idart.mot.com/browse/" + s));
						Thread.sleep(600);
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(null, "Exception:\n" + ex.getMessage());
					}
				}
			}
		});
		btnOpenDiagsOn.setPreferredSize(new Dimension(165, 23));
		GridBagConstraints gbc_btnOpenDiagsOn = new GridBagConstraints();
		gbc_btnOpenDiagsOn.insets = new Insets(0, 0, 5, 0);
		gbc_btnOpenDiagsOn.gridx = 0;
		gbc_btnOpenDiagsOn.gridy = 5;
		panel_4.add(btnOpenDiagsOn, gbc_btnOpenDiagsOn);
		
		JButton btnOpenNondiagsOn = new JButton("Open Non-Diags on Chrome");
		btnOpenNondiagsOn.setEnabled(false);
		btnOpenNondiagsOn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				HashMap<String, String> b2g_crid = DiagCrsCloser.getB2g_crid();
				ArrayList<String> getUnknownCRs = DiagCrsCloser.getUnknownCRs();
				for(String b2gid : getUnknownCRs){
					try {
						String s = b2g_crid.get(b2gid);
						s = s.replaceAll("\n", "");
						s = s.replaceAll("\r", "");
						s = s.replaceAll(" |  ", "");
						Desktop.getDesktop().browse(new URI("http://idart.mot.com/browse/" + s));
						Thread.sleep(600);
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(null, "Exception:\n" + ex.getMessage());
					}
				}
			}
		});
		GridBagConstraints gbc_btnOpenNondiagsOn = new GridBagConstraints();
		gbc_btnOpenNondiagsOn.insets = new Insets(0, 0, 5, 0);
		gbc_btnOpenNondiagsOn.gridx = 0;
		gbc_btnOpenNondiagsOn.gridy = 6;
		panel_4.add(btnOpenNondiagsOn, gbc_btnOpenNondiagsOn);
		
		JButton btnCloseOld = new JButton("Close As Old");
		btnCloseOld.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new Thread(new CrsCloser(BaseWindow)).start();
			}
		});
		GridBagConstraints gbc_btnCloseOld = new GridBagConstraints();
		gbc_btnCloseOld.gridx = 0;
		gbc_btnCloseOld.gridy = 7;
		panel_4.add(btnCloseOld, gbc_btnCloseOld);
		
		JSeparator separator = new JSeparator();
		separator.setMaximumSize(new Dimension(130, 32767));
		separator.setForeground(Color.DARK_GRAY);
		GridBagConstraints gbc_separator = new GridBagConstraints();
		gbc_separator.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator.insets = new Insets(0, 1, 5, 5);
		gbc_separator.gridx = 0;
		gbc_separator.gridy = 1;
		contentPane.add(separator, gbc_separator);
		
		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 5, 5, 5);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 2;
		contentPane.add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{30, 0};
		gbl_panel.rowHeights = new int[]{22, 0, 0};
		gbl_panel.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		JLabel lblRootPath = new JLabel("Root path:");
		GridBagConstraints gbc_lblRootPath = new GridBagConstraints();
		gbc_lblRootPath.insets = new Insets(0, 0, 5, 0);
		gbc_lblRootPath.gridx = 0;
		gbc_lblRootPath.gridy = 0;
		panel.add(lblRootPath, gbc_lblRootPath);
		lblRootPath.setHorizontalAlignment(SwingConstants.CENTER);
		lblRootPath.setFont(new Font("Tahoma", Font.BOLD, 18));
		
		textPath = new JTextField();
		textPath.setToolTipText("Path to stock and read your CRs");
		GridBagConstraints gbc_textPath = new GridBagConstraints();
		gbc_textPath.insets = new Insets(0, 5, 0, 5);
		gbc_textPath.gridx = 0;
		gbc_textPath.gridy = 1;
		panel.add(textPath, gbc_textPath);
		textPath.setHorizontalAlignment(SwingConstants.CENTER);
		textPath.setBorder(new LineBorder(SystemColor.activeCaption));
		textPath.setMinimumSize(new Dimension(130, 20));
		textPath.setPreferredSize(new Dimension(150, 20));
		
		JSeparator separator_1 = new JSeparator();
		GridBagConstraints gbc_separator_1 = new GridBagConstraints();
		gbc_separator_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator_1.insets = new Insets(0, 1, 5, 5);
		gbc_separator_1.gridx = 0;
		gbc_separator_1.gridy = 3;
		contentPane.add(separator_1, gbc_separator_1);
		separator_1.setForeground(SystemColor.activeCaptionText);
		
		JPanel panel_2 = new JPanel();
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.gridheight = 2;
		gbc_panel_2.insets = new Insets(0, 5, 5, 5);
		gbc_panel_2.fill = GridBagConstraints.BOTH;
		gbc_panel_2.gridx = 0;
		gbc_panel_2.gridy = 4;
		contentPane.add(panel_2, gbc_panel_2);
		GridBagLayout gbl_panel_2 = new GridBagLayout();
		gbl_panel_2.columnWidths = new int[] {0};
		gbl_panel_2.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0};
		gbl_panel_2.columnWeights = new double[]{1.0};
		gbl_panel_2.rowWeights = new double[]{1.0, 0.0, 1.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		panel_2.setLayout(gbl_panel_2);
		
		JPanel panel_10 = new JPanel();
		GridBagConstraints gbc_panel_10 = new GridBagConstraints();
		gbc_panel_10.insets = new Insets(0, 0, 5, 0);
		gbc_panel_10.gridx = 0;
		gbc_panel_10.gridy = 0;
		panel_2.add(panel_10, gbc_panel_10);
		GridBagLayout gbl_panel_10 = new GridBagLayout();
		gbl_panel_10.columnWidths = new int[]{41, 0, 0, 0};
		gbl_panel_10.rowHeights = new int[]{22, 0};
		gbl_panel_10.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel_10.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel_10.setLayout(gbl_panel_10);
		
		JLabel lblCrs = new JLabel("CRs:");
		GridBagConstraints gbc_lblCrs = new GridBagConstraints();
		gbc_lblCrs.insets = new Insets(0, 0, 0, 5);
		gbc_lblCrs.anchor = GridBagConstraints.NORTHWEST;
		gbc_lblCrs.gridx = 0;
		gbc_lblCrs.gridy = 0;
		panel_10.add(lblCrs, gbc_lblCrs);
		lblCrs.setHorizontalAlignment(SwingConstants.CENTER);
		lblCrs.setFont(new Font("Tahoma", Font.BOLD, 18));
		
		final JLabel btnSelectAllCRs = new JLabel("select");
		btnSelectAllCRs.setFont(new Font("Tahoma", Font.PLAIN, 9));
		btnSelectAllCRs.setToolTipText("Select all CRs");
		
		btnSelectAllCRs.setIcon(null);
		btnSelectAllCRs.setMaximumSize(new Dimension(24, 24));
		btnSelectAllCRs.setPreferredSize(new Dimension(24, 24));
		btnSelectAllCRs.setMinimumSize(new Dimension(24, 24));
		GridBagConstraints gbc_btnSelectAllCRs = new GridBagConstraints();
		gbc_btnSelectAllCRs.insets = new Insets(0, 25, 0, 1);
		gbc_btnSelectAllCRs.gridx = 1;
		gbc_btnSelectAllCRs.gridy = 0;
		panel_10.add(btnSelectAllCRs, gbc_btnSelectAllCRs);
		
		chkBoxSelectCRs = new JCheckBox("");
		chkBoxSelectCRs.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				
				if (chkBoxSelectCRs.isSelected()) {
					
					// Number of items
					int size = listCRs.getHeight();
					// Indexes of all items:  [0, 1, 2, ...]
					int indexes[] = new int[size];
					
					// Filling indexes
					for (int i = 0; i < size; i++)
						indexes[i] = i;
										
					// Select All
					listCRs.setSelectedIndices(indexes);
					
				} else {
					
					// Select none
					listCRs.setSelectedIndices(new int[]{});
					
				}
					
				
			}
		});
		chkBoxSelectCRs.setToolTipText("Select none");
		chkBoxSelectCRs.setPreferredSize(new Dimension(24, 24));
		chkBoxSelectCRs.setMinimumSize(new Dimension(24, 24));
		chkBoxSelectCRs.setMaximumSize(new Dimension(24, 24));
		GridBagConstraints gbc_chkBoxSelectCRs = new GridBagConstraints();
		gbc_chkBoxSelectCRs.anchor = GridBagConstraints.SOUTH;
		gbc_chkBoxSelectCRs.gridx = 2;
		gbc_chkBoxSelectCRs.gridy = 0;
		panel_10.add(chkBoxSelectCRs, gbc_chkBoxSelectCRs);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBorder(new LineBorder(SystemColor.activeCaption));
		scrollPane_1.setPreferredSize(new Dimension(140, 150));
		scrollPane_1.setMinimumSize(new Dimension(140, 150));
		GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
		gbc_scrollPane_1.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane_1.fill = GridBagConstraints.VERTICAL;
		gbc_scrollPane_1.gridx = 0;
		gbc_scrollPane_1.gridy = 1;
		panel_2.add(scrollPane_1, gbc_scrollPane_1);
		
		listCRs = new JList<String>();
		listCRs.setToolTipText("CRs in Root Path folder");
		listCRs.setVisibleRowCount(30);
		scrollPane_1.setViewportView(listCRs);
		listCRs.setBorder(null);
		listCRs.setMinimumSize(new Dimension(100, 100));
		listCRs.setModel(new DefaultListModel<String>());
		
		JPanel panel_6 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_6.getLayout();
		flowLayout.setVgap(0);
		GridBagConstraints gbc_panel_6 = new GridBagConstraints();
		gbc_panel_6.anchor = GridBagConstraints.NORTH;
		gbc_panel_6.insets = new Insets(0, 0, 5, 0);
		gbc_panel_6.gridx = 0;
		gbc_panel_6.gridy = 2;
		panel_2.add(panel_6, gbc_panel_6);
		
		JButton btnDeleteFolder = new JButton("Delete");
		btnDeleteFolder.setToolTipText("Delete selected folders");
		btnDeleteFolder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for(String s : listCRs.getSelectedValuesList())
					delFolder(textPath.getText() + "\\" + s);
				updateAllDataUI();
				
				chkBoxSelectCRs.setSelected(false);
			}
		});
		panel_6.add(btnDeleteFolder);
		
		JButton btnRunScript = new JButton("RunScript");
		btnRunScript.setToolTipText("Run build_report.pl on selected folders");
		btnRunScript.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Thread t = new Thread(new Runnable() {
					@Override
					public void run() {
				
						List<String> selectedCRs = listCRs.getSelectedValuesList(); 
						chkBoxSelectCRs.setSelected(false);
						
						for(String s : selectedCRs)
							try {
								runScript(textPath.getText() + "\\" + s);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
					}
				});
				t.start();
				
			}
		});
		panel_6.add(btnRunScript);
		
		JPanel panel_8 = new JPanel();
		GridBagConstraints gbc_panel_8 = new GridBagConstraints();
		gbc_panel_8.insets = new Insets(0, 0, 5, 0);
		gbc_panel_8.gridx = 0;
		gbc_panel_8.gridy = 3;
		panel_2.add(panel_8, gbc_panel_8);
		GridBagLayout gbl_panel_8 = new GridBagLayout();
		gbl_panel_8.columnWidths = new int[]{43, 24, 0, 24, 0};
		gbl_panel_8.rowHeights = new int[]{24, 0};
		gbl_panel_8.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel_8.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel_8.setLayout(gbl_panel_8);
		
		JLabel label = new JLabel("Zips:");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font("Tahoma", Font.BOLD, 18));
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.anchor = GridBagConstraints.WEST;
		gbc_label.insets = new Insets(0, 0, 0, 5);
		gbc_label.gridx = 0;
		gbc_label.gridy = 0;
		panel_8.add(label, gbc_label);
		
		JLabel label_1 = new JLabel("select");
		label_1.setToolTipText("Select all CRs");
		label_1.setPreferredSize(new Dimension(24, 24));
		label_1.setMinimumSize(new Dimension(24, 24));
		label_1.setMaximumSize(new Dimension(24, 24));
		label_1.setFont(new Font("Tahoma", Font.PLAIN, 9));
		GridBagConstraints gbc_label_1 = new GridBagConstraints();
		gbc_label_1.insets = new Insets(0, 0, 0, 5);
		gbc_label_1.gridx = 2;
		gbc_label_1.gridy = 0;
		panel_8.add(label_1, gbc_label_1);
		
		chkBoxSelectZips = new JCheckBox("");
		chkBoxSelectZips.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				
				if (chkBoxSelectZips.isSelected()) {
					
					// Number of items
					int size = listZips.getHeight();
					// Indexes of all items:  [0, 1, 2, ...]
					int indexes[] = new int[size];
					
					// Filling indexes
					for (int i = 0; i < size; i++)
						indexes[i] = i;
										
					// Select All
					listZips.setSelectedIndices(indexes);
					
				} else {
					
					// Select none
					listZips.setSelectedIndices(new int[]{});
					
				}
				
			}
		});
		chkBoxSelectZips.setToolTipText("Select none");
		chkBoxSelectZips.setPreferredSize(new Dimension(24, 24));
		chkBoxSelectZips.setMinimumSize(new Dimension(24, 24));
		chkBoxSelectZips.setMaximumSize(new Dimension(24, 24));
		GridBagConstraints gbc_chkBoxSelectZips = new GridBagConstraints();
		gbc_chkBoxSelectZips.gridx = 3;
		gbc_chkBoxSelectZips.gridy = 0;
		panel_8.add(chkBoxSelectZips, gbc_chkBoxSelectZips);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBorder(new LineBorder(SystemColor.activeCaption));
		scrollPane.setPreferredSize(new Dimension(140, 150));
		scrollPane.setMinimumSize(new Dimension(140, 150));
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.fill = GridBagConstraints.VERTICAL;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 4;
		panel_2.add(scrollPane, gbc_scrollPane);
		
		listZips = new JList<String>();
		listZips.setToolTipText("Zip files in Root Path folder");
		listZips.setVisibleRowCount(30);
		listZips.setMaximumSize(new Dimension(200, 999999));
		scrollPane.setViewportView(listZips);
		listZips.setBorder(null);
		listZips.setMinimumSize(new Dimension(100, 100));
		listZips.setModel(new DefaultListModel<String>());
		
		JPanel panel_7 = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel_7.getLayout();
		flowLayout_1.setVgap(0);
		GridBagConstraints gbc_panel_7 = new GridBagConstraints();
		gbc_panel_7.fill = GridBagConstraints.BOTH;
		gbc_panel_7.gridx = 0;
		gbc_panel_7.gridy = 5;
		panel_2.add(panel_7, gbc_panel_7);
		
		JButton btnDeleteZip = new JButton("Delete");
		btnDeleteZip.setToolTipText("Delete selected zip files");
		btnDeleteZip.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for(String s : listZips.getSelectedValuesList())
					delFile(textPath.getText() + "\\" + s);
				updateAllDataUI();
				
				chkBoxSelectZips.setSelected(false);
			}
		});
		panel_7.add(btnDeleteZip);
		
		JButton btnUnzip = new JButton("Unzip");
		btnUnzip.setToolTipText("Unzip selected zip files and run build_report.pl");
		btnUnzip.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Making Unzip process sequencial
				new Thread(new Runnable() {
					@Override
					public void run() {
						//push test
						Object[] aux = listZips.getSelectedValuesList().toArray();
						chkBoxSelectZips.setSelected(false);
						
						System.out.println("Unzipping " + aux.length +" files and running build_report.pl\n" +
								"Please wait, it may take some minutes to finish." +
								"A message alerting you that the process is complete will be showed here.");
						BaseWindow.getCrsManager().addLogLine("Unzipping " + aux.length + " files and running build_report.pl\n" +
								"Please wait, it may take some minutes to finish.\n" +
								"A message alerting you that the process is complete will be showed here.\n");
						for(Object s : aux){
							try {
								BaseWindow.getUnzipSemaphore().acquire();
								System.out.println("Sending file: " + getRootPath() + s);
								//new Thread(new UnZip(getRootPath() + s, BaseWindow)).start();
								//UnZip unzipper = new UnZip(getRootPath() + s, BaseWindow);*
								new UnZip(getRootPath() + s, BaseWindow).unzipFile();
								//Thread.sleep(12);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						BaseWindow.getCrsManager().addLogLine("\nUnzipping process completed");
						System.out.println("Unzipping process completed");
					}
				}).start();
								
				JOptionPane.showMessageDialog(null, "The unziping and build_report running processes are running.\n" +
						"You can see the status on the Status window at the bottom part of this screen.\n" +
						"\nThis window can be closed anytime :D");
			}
		});
		panel_7.add(btnUnzip);
		
		JButton btnUpdateAll = new JButton("Click to Update everything");
		btnUpdateAll.setToolTipText("Update UI information");
		btnUpdateAll.setPreferredSize(new Dimension(159, 20));
		btnUpdateAll.setIcon(Icons.refresh);
		btnUpdateAll.setFont(new Font("Consolas", Font.BOLD, 12));
		btnUpdateAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateAllDataUI();
			}
		});
		
		JSeparator separator_4 = new JSeparator();
		separator_4.setForeground(new Color(0, 0, 0));
		GridBagConstraints gbc_separator_4 = new GridBagConstraints();
		gbc_separator_4.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator_4.gridwidth = 5;
		gbc_separator_4.insets = new Insets(10, 5, 5, 0);
		gbc_separator_4.gridx = 0;
		gbc_separator_4.gridy = 6;
		contentPane.add(separator_4, gbc_separator_4);
		GridBagConstraints gbc_btnUpdateAll = new GridBagConstraints();
		gbc_btnUpdateAll.insets = new Insets(10, 10, 10, 10);
		gbc_btnUpdateAll.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnUpdateAll.gridwidth = 5;
		gbc_btnUpdateAll.gridx = 0;
		gbc_btnUpdateAll.gridy = 7;
		contentPane.add(btnUpdateAll, gbc_btnUpdateAll);
		
		JPanel panel_5 = new JPanel();
		panel_5.setBorder(new LineBorder(new Color(240, 128, 128), 1, true));
		GridBagConstraints gbc_panel_5 = new GridBagConstraints();
		gbc_panel_5.gridwidth = 5;
		gbc_panel_5.gridx = 0;
		gbc_panel_5.gridy = 8;
		contentPane.add(panel_5, gbc_panel_5);
		GridBagLayout gbl_panel_5 = new GridBagLayout();
		gbl_panel_5.columnWidths = new int[]{0, 0, 0};
		gbl_panel_5.rowHeights = new int[]{0, 0, 0};
		gbl_panel_5.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_panel_5.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		panel_5.setLayout(gbl_panel_5);
		
		JLabel lblStatusLog = new JLabel("Status Log:");
		GridBagConstraints gbc_lblStatusLog = new GridBagConstraints();
		gbc_lblStatusLog.anchor = GridBagConstraints.WEST;
		gbc_lblStatusLog.insets = new Insets(0, 10, 5, 5);
		gbc_lblStatusLog.gridx = 0;
		gbc_lblStatusLog.gridy = 0;
		panel_5.add(lblStatusLog, gbc_lblStatusLog);
		
		JLabel lblLastAction = new JLabel("Last Action:");
		GridBagConstraints gbc_lblLastAction = new GridBagConstraints();
		gbc_lblLastAction.anchor = GridBagConstraints.WEST;
		gbc_lblLastAction.insets = new Insets(0, 10, 5, 0);
		gbc_lblLastAction.gridx = 1;
		gbc_lblLastAction.gridy = 0;
		panel_5.add(lblLastAction, gbc_lblLastAction);
		
		JScrollPane scrollPane_3 = new JScrollPane();
		scrollPane_3.setPreferredSize(new Dimension(400, 82));
		scrollPane_3.setMinimumSize(new Dimension(400, 82));
		scrollPane_3.setBorder(new LineBorder(new Color(128, 128, 128)));
		GridBagConstraints gbc_scrollPane_3 = new GridBagConstraints();
		gbc_scrollPane_3.insets = new Insets(0, 0, 0, 5);
		gbc_scrollPane_3.gridx = 0;
		gbc_scrollPane_3.gridy = 1;
		panel_5.add(scrollPane_3, gbc_scrollPane_3);
		
		textLog = new JTextPane();
		scrollPane_3.setViewportView(textLog);
		textLog.setEditable(false);
		textLog.setBorder(null);
		textLog.setPreferredSize(new Dimension(400, 82));
		textLog.setMinimumSize(new Dimension(400, 42));
		
		JScrollPane scrollPane_4 = new JScrollPane();
		scrollPane_4.setPreferredSize(new Dimension(100, 82));
		scrollPane_4.setMinimumSize(new Dimension(100, 82));
		scrollPane_4.setBorder(new LineBorder(new Color(128, 128, 128)));
		GridBagConstraints gbc_scrollPane_4 = new GridBagConstraints();
		gbc_scrollPane_4.gridx = 1;
		gbc_scrollPane_4.gridy = 1;
		panel_5.add(scrollPane_4, gbc_scrollPane_4);
		
		textPane = new JTextPane();
		scrollPane_4.setViewportView(textPane);
		textPane.setEditable(false);
		textPane.setBorder(null);
		textPane.setPreferredSize(new Dimension(100, 82));
		textPane.setMinimumSize(new Dimension(50, 42));
		
		//Initialization
		uiConfiguration();
		
		textPath.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent arg0) {
				updateAllDataUI();
			}
			
			@Override
			public void insertUpdate(DocumentEvent arg0) {
				updateAllDataUI();
			}

			@Override
			public void changedUpdate(DocumentEvent arg0) {}
		});
	}

	
	
	
	/**
	 * Download functions -------------------------------------------------------------------------------------------------------------------------------------------------
	 */
	private void initialize(){
		System.out.println("Generating Firefox profile");
		// Configure download local
		String content;
		Scanner scanner;
		try {
			scanner = new Scanner(new File("Data\\complements\\profiles\\y2fvgaq0.bot\\prefs_base.js"));
			content = scanner.useDelimiter("\\Z").next();
			content = content.replace("#path#", textPath.getText().replace("\\", "\\\\").replace("/", "\\\\"));
			PrintWriter out = new PrintWriter("Data\\complements\\profiles\\y2fvgaq0.bot\\prefs.js");
			out.println(content);
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		profile = new FirefoxProfile(new File("Data\\complements\\profiles\\y2fvgaq0.bot"));
		driver = new FirefoxDriver(profile);
		
		//Setup motorola user account
		updateUserdata();
	}
	
	private boolean openBrowser(){
		// Open up a browser
		System.out.println("Starting browser");
		driver.navigate().to("http://google.com");
		System.out.println("Done.");
		return true;
	}
	
	private boolean jiraLogin(){
		if(driver.getTitle().contains("Log")){
			//System.out.println("Trying to Log in");
			driver.findElement(By.name("os_username")).sendKeys(user);		
			driver.findElement(By.name("os_password")).sendKeys(pass);
			driver.findElement(By.name("os_cookie")).click();
			driver.findElement(By.name("login")).click();
			//sleep(1500);
			if(driver.getTitle().contains("Log")){
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
			int error = 0;
			while(!driver.getPageSource().contains("<span class=\"user-hover\" id=\"issue_summary_assignee_" + user + " rel=\"")){
				error++;
				System.out.println("Not assignned yet. Retrying");
				sleep(750);
				driver.findElement(By.className("issueaction-assign-to-me")).click();
				if(error > 3){
					JOptionPane.showMessageDialog(this, "Error trying to assign. Canceling process.");
					driver.close();
					break;
				}
				sleep(2000);
			}
		} catch (Exception e1) {
			System.out.println("CR assigned already");
		}
		return true;
	}
	
	private boolean addLabel(){
		try {
			System.out.println("Trying to insert label");
			if(!driver.getPageSource().contains("ll_prodteam_analyzed")){
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
			} else {
				System.out.println("Label already applied");
				return true;
			}
		} catch (Exception e1) {
			System.out.println("Label error");
			e1.printStackTrace();
			return false;
		}
	}
	
	
	private boolean openB2G(){
		try
		{
			System.out.println("Opening Bug2Go");
			System.out.println("Previous B2g page: " + b2glink);
			WebElement Element = driver.findElement(By.partialLinkText("b2gadm-mcloud101-blur"));
			
			String b2gID = Element.getText().substring(Element.getText().indexOf('=') + 1);
			System.out.println("\n-> B2g id clicked: " + b2gID + "\n-> CR being downlod: " + actualCR + "\n");
			b2g_crid.put(b2gID, actualCR);
			b2g_analyzed.put(b2gID, "Not analyzed");
			b2glink = Element.getText();
			System.out.println("New B2g link: " + b2glink);
			Element.click();
			//driver.navigate().to(b2glink);
			
			sleep(1000);
			
			while(driver.getTitle().contains("MOTOROLA")){
				System.out.println("Login to Bug2Go");
				driver.findElement(By.id("username")).sendKeys(user);		
				driver.findElement(By.id("password")).sendKeys(pass);
				driver.findElement(By.className("input_submit")).click();
				sleep(1000);
			}
			
			sleep(2000);
			return true;
		} catch (Exception e){
			e.printStackTrace();
			driver.navigate().back();
			return false;
		}
	}


	private boolean downloadCR(){
		while(!verifyDownloadPage()){
			System.out.println("Verify download - 1");
		}
		System.out.println("B2G page loaded");
		
		System.out.println("Clicking download");
		driver.findElement(By.className("bg_btn")).click();
		sleep(2500);
		while(driver.getTitle().contains("MOTOROLA")){
			System.out.println("Login to Bug2Go");
			driver.findElement(By.id("username")).sendKeys(user);		
			driver.findElement(By.id("password")).sendKeys(pass);
			driver.findElement(By.className("input_submit")).click();
			sleep(1000);
		}
		
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
		sleep(1000);
		return true;
	}
	
	/**
	 *  main download function
	 */
	private void downloadCRs() {
		//Initialize variables
		initialize();
		
		//Open up browser
		openBrowser();
		
		//Clean up the hashmaps
		b2g_crid = new HashMap<String, String>();
		b2g_analyzed = new HashMap<String, String>();
		
		//Start the download process
		CRs = textDownload.getText().replaceAll(" ", "").split("\n");
		System.out.println("CRs List:" + CRs.length);
		
		for (int i = 0; i < CRs.length; i++) {
			System.out.println("-" + CRs[i] + "-");
		}
		
		//boolean login=false, assign=false, label=false, download=false;
		int flow = 0;
		
		for (int i = 0; i < CRs.length; i++) {
			//Open page
			System.out.println("Opening CR page");
			driver.navigate().to("http://idart.mot.com/browse/" + CRs[i]);
			System.out.println("Done.");
			
			
			//Log in
			System.out.println("Trying to login.");
			jiraLogin();
			
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
			
			
			//Label
			if(chckbxLabels.isSelected()){
				while(!addLabel()){}
				System.out.println("Label Done");
			} else {
				System.out.println("Skipping Label Inserting");
			}
			
			sleep(1000);
			
			//Assign
			while(chckbxAssign.isSelected() && !assignCR()){}
			System.out.println("Assign Done");
			
			
			
			actualCR = CRs[i];
			while(!openB2G()){}
			System.out.println("Open B2G Done");
			
			while(!downloadCR()){}
			System.out.println("Going back to jira");
			
			sleep(300);
			driver.navigate().to("http://idart.mot.com/browse/" + CRs[i]);
			
			sleep(500);
			driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL + "t");
			
			sleep(1000);
		}
		driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL + "w");
		
		saveUserData();
		updateAllDataUI();
	}
	
	
	
		
	
	
	
	/**
	 *  Interface functions --------------------------------------------------------------------------------------------------------------------------------------------
	 */
	private void btnClearAction(){
		textDownload.setText("");
	}
	
	private void btnPasteAction() {
		textDownload.setText("");

		Scanner scanner;
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		try {
			String string = (String) clipboard.getData(DataFlavor.stringFlavor);

			scanner = new Scanner(string);
			String str;
			while (scanner.hasNext()) {
				str = scanner.nextLine();
				textDownload.setText(textDownload.getText() + str + "\n");
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Exception: " + ex.getMessage());
		}
	}
	
	private void btnDownloadAction(){
		//Clear UI data
		clearUIData();
		
		//Open and download the CRs
		downloadCRs();
	}
	
	private void btnOpenAction() {
		for (String s : textDownload.getText().split("\n")) {
			try {
				s = s.replaceAll("\n", "");
				s = s.replaceAll("\r", "");
				s = s.replaceAll(" ", "");
				Desktop.getDesktop().browse(new URI("http://idart.mot.com/browse/" + s));
				Thread.sleep(500);
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Exception: " + ex.getMessage());
			}
		}
	}
	
	private void btnRunDiag() {
		Object[] options = { "Go!", "Cancel" };
		b2g_analyzed = new HashMap<String, String>();
		if(b2g_crid == null || b2g_crid.size() < 1){
			b2g_crid = new HashMap<String, String>();
		}
		
		int n = JOptionPane.showOptionDialog(null,
				"Please, make sure that the following path is your CRs path. Otherwise, cancel this window and " +
						"put the correct path on \"CRs path\" located on the main window.\n" + textPath.getText(),
				"Warning",
				JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, options,
				options[0]);
		if(n == 0){
			new ClosingDiagDialog(BaseWindow);
		} else {
			//JOptionPane.showMessageDialog(null,"Selected option: " + n);
			System.out.println("Action cancelled");
		}
	}
	
	private void updateZipAndFoldersList(){
		listZipNames = new ArrayList<String>();
		listFoldersNames = new ArrayList<String>();
		
		String rootFolder = getRootPath();
		File rootPath = new File(rootFolder);
		if(rootPath.isDirectory()){
			for(String itemName : rootPath.list()){
				File actualFile = new File(rootFolder + itemName);
				if(actualFile.isDirectory()){
					//System.out.println("CR Folder: " + itemName);
					listFoldersNames.add(actualFile.getName());
				} else if(actualFile.getName().endsWith(".zip")){
					//System.out.println("Zip file: " + itemName);
					listZipNames.add(actualFile.getName());
				}
			}
			String[] aux1 = new String[listZipNames.size()];
			listZipNames.toArray(aux1);
			String[] aux2 = new String[listFoldersNames.size()];
			listFoldersNames.toArray(aux2);
			listZips.setListData(aux1);
			listCRs.setListData(aux2);
		}
	}
	
	public void updateDiagList(){
		listModel = (DefaultListModel<String>) listDiag.getModel();
		listModel.removeAllElements();
		
		if(b2g_crid != null && b2g_crid.size() > 0){
			for(String key : b2g_crid.keySet()){
				listModel.addElement(b2g_crid.get(key) + " - " + key + " - " + b2g_analyzed.get(key));
			}
		} else {
			for(Object key : listFoldersNames.toArray()){
				listModel.addElement((String)key  + " - " + "Unknown" + " - " + b2g_analyzed.get(key));
			}
		}
		updateUI();
		repaint();
		revalidate();
	}
	
	public void updateAllDataUI(){
		updateZipAndFoldersList();
		//updateDiagList(); Diag disabled
		
		// Solving --> CRs get unselected when window loses focus
		if (chkBoxSelectCRs.isSelected()) {
			chkBoxSelectCRs.setSelected(false);
			chkBoxSelectCRs.setSelected(true);
		}
		
		if (chkBoxSelectZips.isSelected()) {
			chkBoxSelectZips.setSelected(false);
			chkBoxSelectZips.setSelected(true);
		}
		
	}
	
	private void clearUIData(){
		
	}
	
	public void addLogLine(String line){
		if(textLog.getText().split("\n").length > 150){
			try {
				File f = new File("Data\\logs\\log_" + new Timestamp(System.currentTimeMillis()).toString().replace(":", "_") + ".txt");
				BufferedWriter bw = new BufferedWriter(new FileWriter(f));
				bw.write(textLog.getText());
				bw.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			textLog.setText("");
		}
		textLog.setText(textLog.getText() + line + "\n");
		textLog.setCaretPosition(textLog.getText().length());
	}
	
	
	
	
	
	
	/**
	 *  Aux functions ------------------------------------------------------------------------------------------------
	 */
	private void updateUserdata(){
		user = textUsername.getText();
		pass = String.copyValueOf(textPassword.getPassword());
	}
	
	private void sleep(int millis){
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void saveUserData(){
		try{
			//Abre o arquivo XML
			File xmlFile = new File("Data/cfgs/user_cfg.xml");
			
			//Cria o builder da estrutura XML
			SAXBuilder builder = new SAXBuilder();
			
			//Cria documento formatado de acordo com a lib XML
			Document document = (Document) builder.build(xmlFile);
			
			//Pega o nó raiz do XML
			Element satNode = document.getRootElement();
			
			//Gera lista de filhos do nó root
			//List<Element> satElements = satNode.getChildren();
			
			//Pega o nó referente ao option pane
			Element optionPaneNode = satNode.getChild("crs_jira_pane"); 
			for(Element e : optionPaneNode.getChildren()){
				if(e.getName().equals("path")){
					e.setText(textPath.getText());
					
				} else if(e.getName().equals("uname")){
					e.setText(textUsername.getText());
					
				} else if(e.getName().equals("encrypt_len")){
					File f = new File("Data\\cfgs\\pass.ini");
					BufferedOutputStream bout;
					try {
						bout = new BufferedOutputStream(new FileOutputStream(f));
						bout.write(Encryptation.encrypt(String.copyValueOf(textPassword.getPassword())));
						bout.close();
						//System.out.println("Pass saved\nPass: " + String.copyValueOf(passField.getPassword()));
					} catch(Exception e2){
						e2.printStackTrace();
					}
					e.setText("" + Encryptation.encrypt(String.copyValueOf(textPassword.getPassword())).length);
					
				} else if(e.getName().equals("remember")){
					e.setText(chkbxRemember.isSelected() + "");
					
				} else if(e.getName().equals("assign")){
					e.setText(chckbxAssign.isSelected() + "");
					
				} else if(e.getName().equals("label")){
					e.setText(chckbxLabels.isSelected() + "");
					
				}
			}
			
			//JDOM document is ready now, lets write it to file now
	        XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
	        //output xml to console for debugging
	        //xmlOutputter.output(doc, System.out);
	        xmlOutputter.output(document, new FileOutputStream(xmlFile));
	        
			System.out.println("Options Saved");
		} catch (JDOMException | IOException | InvalidKeyException 
				| InvalidKeySpecException | NoSuchAlgorithmException 
				| NoSuchPaddingException | IllegalBlockSizeException 
				| BadPaddingException e){
			e.printStackTrace();
		}
	}
	
	private void loadUserData(){
		try{
			//Abre o arquivo XML
			File xmlFile = new File("Data/cfgs/user_cfg.xml");
			
			//Cria o builder da estrutura XML
			SAXBuilder builder = new SAXBuilder();
			
			//Cria documento formatado de acordo com a lib XML
			Document document = (Document) builder.build(xmlFile);
	
			//Pega o nó raiz do XML
			Element satNode = document.getRootElement();
			
			//Gera lista de filhos do nó root
			//List<Element> satElements = satNode.getChildren();
			
			//Pega o nó referente ao option pane
			Element crs_jira_paneNode = satNode.getChild("crs_jira_pane");
			for(Element e : crs_jira_paneNode.getChildren()){
				if(e.getName().equals("path")){
					textPath.setText(e.getValue());
					
				} else if(e.getName().equals("uname")){
					textUsername.setText(e.getValue());
					
				} else if(e.getName().equals("encrypt_len")){
					File f = new File("Data\\cfgs\\pass.ini");
					BufferedInputStream bin;
					try {
						bin = new BufferedInputStream(new FileInputStream(f));
						byte[] toDecrypt = new byte[Integer.parseInt(e.getValue())];
						bin.read(toDecrypt);
						textPassword.setText(Encryptation.decrypt(toDecrypt));
						bin.close();
						//System.out.println("File saved\nDecrypted: " + Encryptation.decrypt(toDecrypt));
					} catch(Exception e2){
						e2.printStackTrace();
					}
					
				} else if(e.getName().equals("remember")){
					chkbxRemember.setSelected(Boolean.parseBoolean(e.getValue()));
					
				} else if(e.getName().equals("assign")){
					chckbxAssign.setSelected(Boolean.parseBoolean(e.getValue()));
					
				} else if(e.getName().equals("label")){
					chckbxLabels.setSelected(Boolean.parseBoolean(e.getValue()));
					
				}
			}
			System.out.println("Options Loaded");
		
		} catch (IOException | JDOMException e){
			e.printStackTrace();
		}
	}
	
	
	private void delFolder(String folder){
		File file = new File(folder);
		try {
			if(file.isDirectory()){
				System.out.println("Deleting " + folder);
				FileUtils.deleteDirectory(file);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void delFile(String folder){
		File file = new File(folder);
		if(file.isFile()){
			System.out.println("Deleting " + folder);
			file.delete();
		}
	}
	
	public void runScript(String folder) throws IOException {
		System.out.println("\nRunning script at: " + folder);
		addLogLine("Running script at: " + folder);
		
		// File seek and load configuration
		File f = new File(folder);
		File[] filesList = f.listFiles();
		String reportFile = null, sCurrentLine;

		// Look for the file
		for (int j = 0; j < filesList.length; j++) {
			if (filesList[j].isFile()) {
				String files = filesList[j].getName();
				if (files.toLowerCase().endsWith(".txt") && files.toLowerCase().contains("report_info")) {
					reportFile = folder + "\\" + files;
					break;
				}
			}
		}

		// Try to open file
		BufferedReader br = null;
		if (reportFile == null)
		{
			System.out.println("Log de sistema nao encontrado: " + reportFile);
		}
		else
		{
			br = new BufferedReader(new FileReader(reportFile));
		}
		
		
		// Parse file
		while ((sCurrentLine = br.readLine()) != null) {
			if(sCurrentLine.toLowerCase().contains("product"))
			{
				System.out.println("--- Initial line: " + sCurrentLine);
				sCurrentLine = sCurrentLine.replace("\"PRODUCT\": \"", "").replace(" ", "");
				sCurrentLine = sCurrentLine.substring(0, sCurrentLine.indexOf("_"));
				System.out.println(sCurrentLine);
				
				copyScript(new File("Data\\scripts\\_Base.pl") ,  new File(folder + "\\build_report.pl"));
				
				// Configure build report battery capacity
				try {
					@SuppressWarnings("resource")
					Scanner scanner = new Scanner(new File(folder + "\\build_report.pl")); 
					String content = scanner.useDelimiter("\\Z").next();
					content = content.replace("#bat_cap#", BaseWindow.getOptions().getAdvOptions().getBat_capNode().getChildText(sCurrentLine));
					PrintWriter out = new PrintWriter(folder + "\\build_report.pl");
					out.println(content);
					out.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}
		}

		br.close();
		
		ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "cd \"" + folder + "\" && build_report.pl");
        builder.redirectErrorStream(true);
        Process p = builder.start();
        
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        while (true) {
            line = r.readLine();
            if (line == null) { break; }
            //System.out.println(line);
        }
        addLogLine("Done running at: " + folder);
	}
	
	public void copyScript(File source, File dest) throws IOException {
		FileUtils.copyFile(source, dest);
	}
	
	
	
	
	
	
	//UI Initialization
	private void uiConfiguration(){
		b2g_analyzed = new HashMap<String, String>();
		b2g_crid = new HashMap<String, String>();
		
		loadUserData();
	}



	
	
	
	/**
	 * Getters -------------------------------------------------------------------------------------------------------
	 */
	public String[] getUserData() {
		updateUserdata();
		return new String[] {user, pass};
	}	
	
	public HashMap<String, String> getB2g_crid() {
		return b2g_crid;
	}
	
	public String getRootPath(){
		return textPath.getText().replace("\\", "\\\\").concat("\\\\");
	}
	
	public String[] getCrsToDownload(){
		return textDownload.getText().replace(" ", "").replace("\r", "").split("\n");
	}
	
	public HashMap<String, String> getB2g_analyzed(){
		return b2g_analyzed;
	}

	public JTextArea getTextDownload() {
		return textDownload;
	}

	public JTextField getTextPath() {
		return textPath;
	}

	public JTextField getTextUsername() {
		return textUsername;
	}

	public JPasswordField getTextPassword() {
		return textPassword;
	}

	public JCheckBox getChkbxRemember() {
		return chkbxRemember;
	}

	public JCheckBox getChckbxAssign() {
		return chckbxAssign;
	}

	public JCheckBox getChckbxLabels() {
		return chckbxLabels;
	}
	
	public String getActualCR() {
		return actualCR;
	}
	
	
}
