package tests.report;


import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

import core.Icons;
import core.XmlMngr;


public class ReportFrame extends JFrame
{
	private JPanel                   contentPane;
	private JPanel                   dataPane;
	private JPanel                   btnsPane;
	private JTextPane                txtpnHighlights;
	private JSeparator               separator;
	private JScrollPane              scrollPane;
	private JComboBox<ProductReport> comboBox;
	private JCheckBox                chckbxAddCharts;
	private JCheckBox                chckbxAddToHighlights;
	private JButton                  btnReset;
	private JButton                  btnSave;
	private JButton                  btnClose;
	private JButton                  btnAdd;
	private JButton                  btnDel;
	private JButton                  btnEdit;
	private JLabel                   lblProductsreports;
	private JLabel                   lblDashboardLink;
	private JLabel                   lblSpreadsheetLink;
	private JLabel                   lblTopIssues;
	private JLabel                   lblReleasesUnderAnalysis;
	private JTextField               txtReleases;
	private JTextField               txtTopIssuesLabel;
	private JTextField               txtDashboardLink;
	private JTextField               txtSpreadsheetLink;
	private JTextField               txtChartBuild;
	private ReportController         controller;
	private JPanel                   panel;
	private JScrollPane              scrollPane_1;
	private JTextPane                txtpnIssuesIdUserdebug;
	private int                      lastComboBoxIndex;
	private JPanel                   panel_1;
	private JButton                  btnGenerateReport;
	private JButton                  btnSendGeneratedReport;
	private JButton                  btnGenerateAndSend;
	private JLabel                   lblUserpass;
	private JTextField               txtUser;
	private JPasswordField           pwdPass;
	private JCheckBox                chckbxSeparateUserUserdebug;
	private JScrollPane              scrollPane_2;
	private JTextPane                txtpnIssuesIdUser;
	
	private ReportFrame              view;
	private ReportModel              pr;
	private ReportController         reportController;
	
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					ReportModel pr = new ReportModel();
					ReportFrame view = new ReportFrame();
					ReportController reportController = new ReportController();
					
					reportController.setFrame(view);
					reportController.setModel(pr);
					
					view.setController(reportController);
					
					XmlMngr.initClass();
					
					view.init();
					view.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}
	
	public ReportFrame()
	{
		// Set UI theme
		try
		{
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		}
		catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e)
		{
			e.printStackTrace();
		}
		
		setTitle("Reports Management");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 888, 438);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPane.rowHeights = new int[] {0, 0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[] {0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[] {0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		lblProductsreports = new JLabel("Products/Reports:");
		lblProductsreports.setFont(new Font("Tahoma", Font.BOLD, 12));
		GridBagConstraints gbc_lblProductsreports = new GridBagConstraints();
		gbc_lblProductsreports.insets = new Insets(0, 0, 5, 5);
		gbc_lblProductsreports.anchor = GridBagConstraints.EAST;
		gbc_lblProductsreports.gridx = 0;
		gbc_lblProductsreports.gridy = 0;
		contentPane.add(lblProductsreports, gbc_lblProductsreports);
		
		comboBox = new JComboBox<ProductReport>();
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.gridwidth = 4;
		gbc_comboBox.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.gridx = 1;
		gbc_comboBox.gridy = 0;
		contentPane.add(comboBox, gbc_comboBox);
		
		btnAdd = new JButton("");
		btnAdd.setMargin(new Insets(0, 0, 0, 0));
		btnAdd.setPreferredSize(new Dimension(23, 23));
		btnAdd.setIcon(Icons.add);
		GridBagConstraints gbc_btnAdd = new GridBagConstraints();
		gbc_btnAdd.insets = new Insets(0, 0, 5, 5);
		gbc_btnAdd.gridx = 5;
		gbc_btnAdd.gridy = 0;
		contentPane.add(btnAdd, gbc_btnAdd);
		
		btnEdit = new JButton("");
		btnEdit.setPreferredSize(new Dimension(23, 23));
		btnEdit.setMargin(new Insets(0, 0, 0, 0));
		btnEdit.setIcon(Icons.edit);
		GridBagConstraints gbc_btnEdit = new GridBagConstraints();
		gbc_btnEdit.insets = new Insets(0, 0, 5, 5);
		gbc_btnEdit.gridx = 6;
		gbc_btnEdit.gridy = 0;
		contentPane.add(btnEdit, gbc_btnEdit);
		
		btnDel = new JButton("");
		btnDel.setMargin(new Insets(0, 0, 0, 0));
		btnDel.setPreferredSize(new Dimension(23, 23));
		btnDel.setIcon(Icons.delete);
		GridBagConstraints gbc_btnDel = new GridBagConstraints();
		gbc_btnDel.insets = new Insets(0, 0, 5, 0);
		gbc_btnDel.gridx = 7;
		gbc_btnDel.gridy = 0;
		contentPane.add(btnDel, gbc_btnDel);
		
		dataPane = new JPanel();
		GridBagConstraints gbc_dataPane = new GridBagConstraints();
		gbc_dataPane.gridwidth = 8;
		gbc_dataPane.insets = new Insets(10, 15, 5, 0);
		gbc_dataPane.fill = GridBagConstraints.BOTH;
		gbc_dataPane.gridx = 0;
		gbc_dataPane.gridy = 1;
		contentPane.add(dataPane, gbc_dataPane);
		GridBagLayout gbl_dataPane = new GridBagLayout();
		gbl_dataPane.columnWidths = new int[] {0, 0, 0};
		gbl_dataPane.rowHeights = new int[] {0, 0, 0, 0, 0, 0, 0, 0};
		gbl_dataPane.columnWeights = new double[] {0.0, 1.0, Double.MIN_VALUE};
		gbl_dataPane.rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 3.0, 1.0, Double.MIN_VALUE};
		dataPane.setLayout(gbl_dataPane);
		
		lblReleasesUnderAnalysis = new JLabel("Releases Under Analysis:");
		lblReleasesUnderAnalysis.setToolTipText("Separate the releases using a blank space \" \"");
		lblReleasesUnderAnalysis.setPreferredSize(new Dimension(121, 23));
		GridBagConstraints gbc_lblReleasesUnderAnalysis = new GridBagConstraints();
		gbc_lblReleasesUnderAnalysis.anchor = GridBagConstraints.EAST;
		gbc_lblReleasesUnderAnalysis.insets = new Insets(0, 0, 5, 5);
		gbc_lblReleasesUnderAnalysis.gridx = 0;
		gbc_lblReleasesUnderAnalysis.gridy = 0;
		dataPane.add(lblReleasesUnderAnalysis, gbc_lblReleasesUnderAnalysis);
		
		txtReleases = new JTextField();
		txtReleases.setToolTipText("Separate the releases using a blank space \" \"");
		txtReleases.setText("Releases");
		GridBagConstraints gbc_txtReleases = new GridBagConstraints();
		gbc_txtReleases.insets = new Insets(0, 0, 5, 0);
		gbc_txtReleases.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtReleases.gridx = 1;
		gbc_txtReleases.gridy = 0;
		dataPane.add(txtReleases, gbc_txtReleases);
		txtReleases.setColumns(10);
		
		lblTopIssues = new JLabel("Top Issues Label:");
		lblTopIssues.setToolTipText("Separate the releases using a blank space \" \"");
		lblTopIssues.setPreferredSize(new Dimension(84, 23));
		GridBagConstraints gbc_chckbxTopIssues = new GridBagConstraints();
		gbc_chckbxTopIssues.anchor = GridBagConstraints.WEST;
		gbc_chckbxTopIssues.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxTopIssues.gridx = 0;
		gbc_chckbxTopIssues.gridy = 1;
		dataPane.add(lblTopIssues, gbc_chckbxTopIssues);
		
		txtTopIssuesLabel = new JTextField();
		txtTopIssuesLabel.setToolTipText("Separate the releases using a blank space \" \"");
		txtTopIssuesLabel.setText("Top Issues Label");
		GridBagConstraints gbc_txtTopIssuesLabel = new GridBagConstraints();
		gbc_txtTopIssuesLabel.insets = new Insets(0, 0, 5, 0);
		gbc_txtTopIssuesLabel.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtTopIssuesLabel.gridx = 1;
		gbc_txtTopIssuesLabel.gridy = 1;
		dataPane.add(txtTopIssuesLabel, gbc_txtTopIssuesLabel);
		txtTopIssuesLabel.setColumns(10);
		
		lblDashboardLink = new JLabel("Dashboard Link:");
		lblDashboardLink.setPreferredSize(new Dimension(77, 23));
		GridBagConstraints gbc_chckbxDashboardLink = new GridBagConstraints();
		gbc_chckbxDashboardLink.anchor = GridBagConstraints.WEST;
		gbc_chckbxDashboardLink.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxDashboardLink.gridx = 0;
		gbc_chckbxDashboardLink.gridy = 2;
		dataPane.add(lblDashboardLink, gbc_chckbxDashboardLink);
		
		txtDashboardLink = new JTextField();
		txtDashboardLink.setText("Dashboard Link");
		GridBagConstraints gbc_txtDashboardLink = new GridBagConstraints();
		gbc_txtDashboardLink.insets = new Insets(0, 0, 5, 0);
		gbc_txtDashboardLink.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtDashboardLink.gridx = 1;
		gbc_txtDashboardLink.gridy = 2;
		dataPane.add(txtDashboardLink, gbc_txtDashboardLink);
		txtDashboardLink.setColumns(10);
		
		lblSpreadsheetLink = new JLabel("Spreadsheet Link:");
		lblSpreadsheetLink.setPreferredSize(new Dimension(86, 23));
		GridBagConstraints gbc_chckbxSpreadsheetLink = new GridBagConstraints();
		gbc_chckbxSpreadsheetLink.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxSpreadsheetLink.anchor = GridBagConstraints.WEST;
		gbc_chckbxSpreadsheetLink.gridx = 0;
		gbc_chckbxSpreadsheetLink.gridy = 3;
		dataPane.add(lblSpreadsheetLink, gbc_chckbxSpreadsheetLink);
		
		txtSpreadsheetLink = new JTextField();
		txtSpreadsheetLink.setText("Spreadsheet Link");
		GridBagConstraints gbc_txtSpreadsheetLink = new GridBagConstraints();
		gbc_txtSpreadsheetLink.insets = new Insets(0, 0, 5, 0);
		gbc_txtSpreadsheetLink.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtSpreadsheetLink.gridx = 1;
		gbc_txtSpreadsheetLink.gridy = 3;
		dataPane.add(txtSpreadsheetLink, gbc_txtSpreadsheetLink);
		txtSpreadsheetLink.setColumns(10);
		
		separator = new JSeparator();
		GridBagConstraints gbc_separator = new GridBagConstraints();
		gbc_separator.insets = new Insets(5, 0, 5, 0);
		gbc_separator.gridwidth = 2;
		gbc_separator.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator.gridx = 0;
		gbc_separator.gridy = 4;
		dataPane.add(separator, gbc_separator);
		
		chckbxAddCharts = new JCheckBox("Add Charts:");
		chckbxAddCharts.setMargin(new Insets(2, 0, 2, 2));
		GridBagConstraints gbc_chckbxAddCharts = new GridBagConstraints();
		gbc_chckbxAddCharts.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxAddCharts.anchor = GridBagConstraints.NORTHWEST;
		gbc_chckbxAddCharts.gridx = 0;
		gbc_chckbxAddCharts.gridy = 5;
		dataPane.add(chckbxAddCharts, gbc_chckbxAddCharts);
		
		panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.anchor = GridBagConstraints.SOUTHWEST;
		gbc_panel.insets = new Insets(0, 0, 5, 0);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 1;
		gbc_panel.gridy = 5;
		dataPane.add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] {170, 0, 0, 0};
		gbl_panel.rowHeights = new int[] {0, 0, 0};
		gbl_panel.columnWeights = new double[] {0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[] {0.0, 3.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		txtChartBuild = new JTextField();
		txtChartBuild.setMinimumSize(new Dimension(170, 20));
		txtChartBuild.setPreferredSize(new Dimension(170, 20));
		GridBagConstraints gbc_txtChartBuild = new GridBagConstraints();
		gbc_txtChartBuild.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtChartBuild.anchor = GridBagConstraints.NORTH;
		gbc_txtChartBuild.insets = new Insets(0, 0, 5, 5);
		gbc_txtChartBuild.gridx = 0;
		gbc_txtChartBuild.gridy = 0;
		panel.add(txtChartBuild, gbc_txtChartBuild);
		txtChartBuild.setToolTipText("Separate the releases using a blank space \" \"");
		txtChartBuild.setText("ChartBuild");
		txtChartBuild.setColumns(10);
		
		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBorder(UIManager.getBorder("TextField.border"));
		scrollPane_1.setPreferredSize(new Dimension(170, 50));
		scrollPane_1.setMinimumSize(new Dimension(170, 23));
		GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
		gbc_scrollPane_1.gridheight = 2;
		gbc_scrollPane_1.insets = new Insets(0, 0, 0, 5);
		gbc_scrollPane_1.anchor = GridBagConstraints.NORTHWEST;
		gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_1.gridx = 1;
		gbc_scrollPane_1.gridy = 0;
		panel.add(scrollPane_1, gbc_scrollPane_1);
		
		txtpnIssuesIdUserdebug = new JTextPane();
		txtpnIssuesIdUserdebug.setToolTipText("Put the top issues for userdebug builds");
		txtpnIssuesIdUserdebug.setBorder(null);
		txtpnIssuesIdUserdebug.setText("Issues ID Userdebug/All");
		scrollPane_1.setViewportView(txtpnIssuesIdUserdebug);
		
		scrollPane_2 = new JScrollPane();
		scrollPane_2.setPreferredSize(new Dimension(170, 50));
		scrollPane_2.setMinimumSize(new Dimension(170, 23));
		scrollPane_2.setBorder(UIManager.getBorder("TextField.border"));
		GridBagConstraints gbc_scrollPane_2 = new GridBagConstraints();
		gbc_scrollPane_2.anchor = GridBagConstraints.NORTHWEST;
		gbc_scrollPane_2.gridheight = 2;
		gbc_scrollPane_2.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_2.gridx = 2;
		gbc_scrollPane_2.gridy = 0;
		panel.add(scrollPane_2, gbc_scrollPane_2);
		
		txtpnIssuesIdUser = new JTextPane();
		txtpnIssuesIdUser.setToolTipText("Put the top issues for user builds");
		txtpnIssuesIdUser.setText("Issues ID User");
		txtpnIssuesIdUser.setBorder(null);
		scrollPane_2.setViewportView(txtpnIssuesIdUser);
		
		chckbxSeparateUserUserdebug = new JCheckBox("Separate userdebug/user charts?");
		chckbxSeparateUserUserdebug.setMargin(new Insets(2, 0, 2, 2));
		GridBagConstraints gbc_chckbxSeparateUserUserdebug = new GridBagConstraints();
		gbc_chckbxSeparateUserUserdebug.anchor = GridBagConstraints.NORTHWEST;
		gbc_chckbxSeparateUserUserdebug.insets = new Insets(0, 0, 0, 5);
		gbc_chckbxSeparateUserUserdebug.gridx = 0;
		gbc_chckbxSeparateUserUserdebug.gridy = 1;
		panel.add(chckbxSeparateUserUserdebug, gbc_chckbxSeparateUserUserdebug);
		
		chckbxAddToHighlights = new JCheckBox("Add to Highlights:");
		chckbxAddToHighlights.setToolTipText("Add these highlights to the mail highlights?");
		chckbxAddToHighlights.setMargin(new Insets(2, 0, 2, 2));
		GridBagConstraints gbc_chckbxAddToHighlights = new GridBagConstraints();
		gbc_chckbxAddToHighlights.anchor = GridBagConstraints.NORTHWEST;
		gbc_chckbxAddToHighlights.insets = new Insets(0, 0, 0, 5);
		gbc_chckbxAddToHighlights.gridx = 0;
		gbc_chckbxAddToHighlights.gridy = 6;
		dataPane.add(chckbxAddToHighlights, gbc_chckbxAddToHighlights);
		
		scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 1;
		gbc_scrollPane.gridy = 6;
		dataPane.add(scrollPane, gbc_scrollPane);
		
		txtpnHighlights = new JTextPane();
		txtpnHighlights.setToolTipText("Put here the highlights related to this product");
		scrollPane.setViewportView(txtpnHighlights);
		txtpnHighlights.setText("Highlights");
		txtpnHighlights.setBorder(null);
		scrollPane.setBorder(txtSpreadsheetLink.getBorder());
		
		btnsPane = new JPanel();
		GridBagConstraints gbc_btnsPane = new GridBagConstraints();
		gbc_btnsPane.insets = new Insets(0, 0, 5, 0);
		gbc_btnsPane.gridwidth = 8;
		gbc_btnsPane.fill = GridBagConstraints.BOTH;
		gbc_btnsPane.gridx = 0;
		gbc_btnsPane.gridy = 2;
		contentPane.add(btnsPane, gbc_btnsPane);
		GridBagLayout gbl_btnsPane = new GridBagLayout();
		gbl_btnsPane.columnWidths = new int[] {0, 0, 0, 0, 0, 0};
		gbl_btnsPane.rowHeights = new int[] {0, 0};
		gbl_btnsPane.columnWeights = new double[] {1.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_btnsPane.rowWeights = new double[] {0.0, Double.MIN_VALUE};
		btnsPane.setLayout(gbl_btnsPane);
		
		btnReset = new JButton("Reset");
		GridBagConstraints gbc_btnReset = new GridBagConstraints();
		gbc_btnReset.insets = new Insets(0, 0, 0, 5);
		gbc_btnReset.gridx = 1;
		gbc_btnReset.gridy = 0;
		btnsPane.add(btnReset, gbc_btnReset);
		
		btnSave = new JButton("Save");
		btnSave.setPreferredSize(new Dimension(61, 23));
		GridBagConstraints gbc_btnSave = new GridBagConstraints();
		gbc_btnSave.insets = new Insets(0, 0, 0, 5);
		gbc_btnSave.gridx = 2;
		gbc_btnSave.gridy = 0;
		btnsPane.add(btnSave, gbc_btnSave);
		
		btnClose = new JButton("Close");
		btnClose.setPreferredSize(new Dimension(61, 23));
		GridBagConstraints gbc_btnClose = new GridBagConstraints();
		gbc_btnClose.insets = new Insets(0, 0, 0, 5);
		gbc_btnClose.gridx = 3;
		gbc_btnClose.gridy = 0;
		btnsPane.add(btnClose, gbc_btnClose);
		
		panel_1 = new JPanel();
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.gridwidth = 8;
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 0;
		gbc_panel_1.gridy = 3;
		contentPane.add(panel_1, gbc_panel_1);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[] {0, 0, 0, 0, 0, 0};
		gbl_panel_1.rowHeights = new int[] {0, 0, 0};
		gbl_panel_1.columnWeights = new double[] {1.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_panel_1.rowWeights = new double[] {0.0, 0.0, Double.MIN_VALUE};
		panel_1.setLayout(gbl_panel_1);
		
		lblUserpass = new JLabel("User/Pass");
		GridBagConstraints gbc_lblUserpass = new GridBagConstraints();
		gbc_lblUserpass.anchor = GridBagConstraints.EAST;
		gbc_lblUserpass.insets = new Insets(0, 0, 5, 5);
		gbc_lblUserpass.gridx = 1;
		gbc_lblUserpass.gridy = 0;
		panel_1.add(lblUserpass, gbc_lblUserpass);
		
		txtUser = new JTextField();
		txtUser.setText("user");
		GridBagConstraints gbc_txtUser = new GridBagConstraints();
		gbc_txtUser.insets = new Insets(0, 0, 5, 5);
		gbc_txtUser.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtUser.gridx = 2;
		gbc_txtUser.gridy = 0;
		panel_1.add(txtUser, gbc_txtUser);
		txtUser.setColumns(10);
		
		pwdPass = new JPasswordField();
		pwdPass.setText("pass");
		GridBagConstraints gbc_pwdPass = new GridBagConstraints();
		gbc_pwdPass.insets = new Insets(0, 0, 5, 5);
		gbc_pwdPass.fill = GridBagConstraints.HORIZONTAL;
		gbc_pwdPass.gridx = 3;
		gbc_pwdPass.gridy = 0;
		panel_1.add(pwdPass, gbc_pwdPass);
		
		btnGenerateReport = new JButton("Generate Report");
		GridBagConstraints gbc_btnGenerateReport = new GridBagConstraints();
		gbc_btnGenerateReport.insets = new Insets(0, 0, 0, 5);
		gbc_btnGenerateReport.gridx = 1;
		gbc_btnGenerateReport.gridy = 1;
		panel_1.add(btnGenerateReport, gbc_btnGenerateReport);
		
		btnSendGeneratedReport = new JButton("Send Generated Report");
		GridBagConstraints gbc_btnSendGeneratedReport = new GridBagConstraints();
		gbc_btnSendGeneratedReport.insets = new Insets(0, 0, 0, 5);
		gbc_btnSendGeneratedReport.gridx = 2;
		gbc_btnSendGeneratedReport.gridy = 1;
		panel_1.add(btnSendGeneratedReport, gbc_btnSendGeneratedReport);
		
		btnGenerateAndSend = new JButton("Generate and Send Report");
		GridBagConstraints gbc_btnGenerateAndSend = new GridBagConstraints();
		gbc_btnGenerateAndSend.insets = new Insets(0, 0, 0, 5);
		gbc_btnGenerateAndSend.gridx = 3;
		gbc_btnGenerateAndSend.gridy = 1;
		panel_1.add(btnGenerateAndSend, gbc_btnGenerateAndSend);
		
		view = this;
	}
	
	public void init()
	{
		loadProducts();
		setupActions();
		setupCloseAction();
	}
	
	private void setupCloseAction()
	{
		addWindowListener(new java.awt.event.WindowAdapter()
		{
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent)
			{
				System.out.println("Closed");
				// TODO Save settings
			}
		});
	}
	
	private void setupActions()
	{
		btnAdd.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				controller.addNewProduct();
			}
		});
		
		btnEdit.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				controller.editProductName((ProductReport) comboBox.getSelectedItem(), JOptionPane.showInputDialog(view, "Type the new product name:"));
			}
		});
		
		btnDel.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				controller.removeProduct(comboBox.getSelectedIndex());
			}
		});
		
		btnReset.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				controller.setupViewFields((ProductReport) comboBox.getSelectedItem());
			}
		});
		
		btnSave.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				controller.updateProductFields((ProductReport) comboBox.getSelectedItem());
			}
		});
		
		btnClose.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				controller.updateProductFields((ProductReport) comboBox.getSelectedItem());
				controller.saveProductsToXML();
				XmlMngr.closeXmls();
				dispose();
			}
		});
		
		btnGenerateReport.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				controller.generateProductReport(separateChartBuilds());
			}
		});
		
		btnSendGeneratedReport.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				controller.sendReportMail();
			}
		});
		
		btnGenerateAndSend.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				controller.updateProductFields((ProductReport) comboBox.getSelectedItem());
				controller.generateProductReport(separateChartBuilds());
				controller.sendReportMail();
			}
		});
		
		comboBox.addMouseListener(new MouseListener()
		{
			@Override
			public void mouseReleased(MouseEvent e)
			{
				
			}
			
			@Override
			public void mousePressed(MouseEvent e)
			{
				controller.updateProductFields((ProductReport) comboBox.getSelectedItem());
				lastComboBoxIndex = comboBox.getSelectedIndex();
				System.out.println("Products updated/saved");
			}
			
			@Override
			public void mouseExited(MouseEvent e)
			{
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e)
			{
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e)
			{
				
			}
		});
		
		comboBox.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (lastComboBoxIndex != comboBox.getSelectedIndex())
				{
					controller.updateViewFieldsForSelectedProduct(comboBox.getSelectedIndex());
					lastComboBoxIndex = comboBox.getSelectedIndex();
				}
			}
		});
		
		chckbxSeparateUserUserdebug.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (chckbxSeparateUserUserdebug.isSelected())
				{
					setUserIssuesFieldVisible(true);
				}
				else
				{
					setUserIssuesFieldVisible(false);
				}
			}
		});
	}
	
	public void clearFields()
	{
		txtDashboardLink.setText("");
		txtpnHighlights.setText("");
		txtReleases.setText("");
		txtSpreadsheetLink.setText("");
		txtTopIssuesLabel.setText("");
		chckbxAddCharts.setSelected(false);
		chckbxAddToHighlights.setSelected(false);
	}
	
	public void loadProducts()
	{
		controller.loadProducts();
	}
	
	public ProductReport getSelectedProduct()
	{
		return (ProductReport) comboBox.getSelectedItem();
	}
	
	public void setupComboBox(ArrayList<ProductReport> products)
	{
		comboBox.removeAllItems();
		
		for (ProductReport pr : products)
		{
			comboBox.addItem(pr);
		}
		
		setComboboxItem(0);
	}
	
	public void setComboboxItem(int index)
	{
		if (comboBox.getItemCount() > 0 && index >= 0)
			comboBox.setSelectedIndex(index);
	}
	
	// Getters and Setters ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	public void setController(ReportController rc)
	{
		controller = rc;
	}
	
	public void setChartBuild(String text)
	{
		txtChartBuild.setText(text);
	}
	
	public void setChartUserdebugIssues(String text)
	{
		txtpnIssuesIdUserdebug.setText(text);
	}
	
	public void setChartUserIssues(String text)
	{
		txtpnIssuesIdUser.setText(text);
	}
	
	public void setReleases(String text)
	{
		txtReleases.setText(text);
	}
	
	public void setTopIssuesLabel(String text)
	{
		txtTopIssuesLabel.setText(text);
	}
	
	public void setDashboardLink(String text)
	{
		txtDashboardLink.setText(text);
	}
	
	public void setSpreadsheetLink(String text)
	{
		txtSpreadsheetLink.setText(text);
	}
	
	public void setHighlights(String text)
	{
		txtpnHighlights.setText(text);
	}
	
	public void setAddChart(Boolean bool)
	{
		chckbxAddCharts.setSelected(bool);
	}
	
	public void setAddHighlights(Boolean bool)
	{
		chckbxAddToHighlights.setSelected(bool);
	}
	
	public boolean addChart()
	{
		return chckbxAddCharts.isSelected();
	}
	
	public String getChartBuilds()
	{
		return txtChartBuild.getText();
	}
	
	public String getChartUserdebugIssues()
	{
		return txtpnIssuesIdUserdebug.getText();
	}
	
	public String getChartUserIssues()
	{
		return txtpnIssuesIdUser.getText();
	}
	
	public boolean addHighlights()
	{
		return chckbxAddToHighlights.isSelected();
	}
	
	public boolean separateChartBuilds()
	{
		return chckbxSeparateUserUserdebug.isSelected();
	}
	
	public String getHighlights()
	{
		return txtpnHighlights.getText();
	}
	
	public String getProductIDs()
	{
		return txtChartBuild.getText();
	}
	
	public String getReleases()
	{
		return txtReleases.getText();
	}
	
	public String getTopIssueLabel()
	{
		return txtTopIssuesLabel.getText();
	}
	
	public String getDashboardLink()
	{
		return txtDashboardLink.getText();
	}
	
	public String getSpreadsheetLink()
	{
		return txtSpreadsheetLink.getText();
	}
	
	public String getProductName()
	{
		return comboBox.getSelectedItem().toString();
	}
	
	public String getUser()
	{
		return txtUser.getText();
	}
	
	public String getPass()
	{
		return String.copyValueOf(pwdPass.getPassword());
	}
	
	public JComboBox<ProductReport> getComboBox()
	{
		return comboBox;
	}
	
	public void setComboBox(JComboBox<ProductReport> comboBox)
	{
		this.comboBox = comboBox;
	}
	
	public void setSeparatedCharts(boolean bool)
	{
		chckbxSeparateUserUserdebug.setSelected(bool);
	}
	
	public void setUserIssuesFieldVisible(Boolean bool)
	{
		scrollPane_2.setVisible(bool);
		revalidate();
		repaint();
	}
	
	public boolean getSeparatedCharts()
	{
		return chckbxSeparateUserUserdebug.isSelected();
	}
}
