package tests.report;


import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
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
	private JPanel            contentPane;
	private JPanel            dataPane;
	private JPanel            btnsPane;
	private JTextPane         txtpnHighlights;
	private JSeparator        separator;
	private JScrollPane       scrollPane;
	private JComboBox<String> comboBox;
	private JCheckBox         chckbxAddCharts;
	private JCheckBox         chckbxAddToHighlights;
	private JButton           btnReset;
	private JButton           btnSave;
	private JButton           btnClose;
	private JButton           btnAdd;
	private JButton           btnDel;
	private JButton           btnEdit;
	private JLabel            lblProductsreports;
	private JLabel            lblDashboardLink;
	private JLabel            lblSpreadsheetLink;
	private JLabel            lblTopIssues;
	private JLabel            lblReleasesUnderAnalysis;
	private JTextField        txtReleases;
	private JTextField        txtTopIssuesLabel;
	private JTextField        txtDashboardLink;
	private JTextField        txtSpreadsheetLink;
	private JTextField        txtChartBuild;
	private ReportController  controller;
	private JPanel            panel;
	private JScrollPane       scrollPane_1;
	private JTextPane         txtpnIssuesId;
	
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					ReportModel pr = new ReportModel();
					ReportFrame frame = new ReportFrame();
					ReportController reportController = new ReportController();
					
					reportController.setFrame(frame);
					reportController.setModel(pr);
					
					frame.setController(reportController);
					
					XmlMngr.initClass();
					frame.init();
					
					frame.setVisible(true);
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
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 888, 344);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPane.rowHeights = new int[] {0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[] {0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[] {0.0, 1.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		lblProductsreports = new JLabel("Products/Reports:");
		lblProductsreports.setFont(new Font("Tahoma", Font.BOLD, 12));
		GridBagConstraints gbc_lblProductsreports = new GridBagConstraints();
		gbc_lblProductsreports.insets = new Insets(0, 0, 5, 5);
		gbc_lblProductsreports.anchor = GridBagConstraints.EAST;
		gbc_lblProductsreports.gridx = 0;
		gbc_lblProductsreports.gridy = 0;
		contentPane.add(lblProductsreports, gbc_lblProductsreports);
		
		comboBox = new JComboBox<String>();
		comboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"Bounce M", "Vector", "Afinity", "All top issues", "Issues not updated for long time"}));
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
		gbl_dataPane.rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		dataPane.setLayout(gbl_dataPane);
		
		lblReleasesUnderAnalysis = new JLabel("Releases Under Analysis:");
		lblReleasesUnderAnalysis.setPreferredSize(new Dimension(121, 23));
		GridBagConstraints gbc_lblReleasesUnderAnalysis = new GridBagConstraints();
		gbc_lblReleasesUnderAnalysis.anchor = GridBagConstraints.EAST;
		gbc_lblReleasesUnderAnalysis.insets = new Insets(0, 0, 5, 5);
		gbc_lblReleasesUnderAnalysis.gridx = 0;
		gbc_lblReleasesUnderAnalysis.gridy = 0;
		dataPane.add(lblReleasesUnderAnalysis, gbc_lblReleasesUnderAnalysis);
		
		txtReleases = new JTextField();
		txtReleases.setToolTipText("Separate the releases using \" \"");
		txtReleases.setText("Releases");
		GridBagConstraints gbc_txtReleases = new GridBagConstraints();
		gbc_txtReleases.insets = new Insets(0, 0, 5, 0);
		gbc_txtReleases.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtReleases.gridx = 1;
		gbc_txtReleases.gridy = 0;
		dataPane.add(txtReleases, gbc_txtReleases);
		txtReleases.setColumns(10);
		
		lblTopIssues = new JLabel("Top Issues Label:");
		lblTopIssues.setPreferredSize(new Dimension(84, 23));
		GridBagConstraints gbc_chckbxTopIssues = new GridBagConstraints();
		gbc_chckbxTopIssues.anchor = GridBagConstraints.WEST;
		gbc_chckbxTopIssues.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxTopIssues.gridx = 0;
		gbc_chckbxTopIssues.gridy = 1;
		dataPane.add(lblTopIssues, gbc_chckbxTopIssues);
		
		txtTopIssuesLabel = new JTextField();
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
		gbl_panel.columnWidths = new int[] {170, 0, 0};
		gbl_panel.rowHeights = new int[] {0, 0};
		gbl_panel.columnWeights = new double[] {0.0, 0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[] {1.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		txtChartBuild = new JTextField();
		txtChartBuild.setMinimumSize(new Dimension(170, 20));
		txtChartBuild.setPreferredSize(new Dimension(170, 20));
		GridBagConstraints gbc_txtChartBuild = new GridBagConstraints();
		gbc_txtChartBuild.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtChartBuild.anchor = GridBagConstraints.NORTH;
		gbc_txtChartBuild.insets = new Insets(0, 0, 0, 5);
		gbc_txtChartBuild.gridx = 0;
		gbc_txtChartBuild.gridy = 0;
		panel.add(txtChartBuild, gbc_txtChartBuild);
		txtChartBuild.setToolTipText("Separate the IDs using \";\"");
		txtChartBuild.setText("ChartBuild");
		txtChartBuild.setColumns(10);
		
		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBorder(UIManager.getBorder("TextField.border"));
		scrollPane_1.setPreferredSize(new Dimension(170, 50));
		scrollPane_1.setMinimumSize(new Dimension(170, 23));
		GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
		gbc_scrollPane_1.anchor = GridBagConstraints.NORTHWEST;
		gbc_scrollPane_1.fill = GridBagConstraints.VERTICAL;
		gbc_scrollPane_1.gridx = 1;
		gbc_scrollPane_1.gridy = 0;
		panel.add(scrollPane_1, gbc_scrollPane_1);
		
		txtpnIssuesId = new JTextPane();
		txtpnIssuesId.setBorder(null);
		txtpnIssuesId.setText("Issues ID");
		scrollPane_1.setViewportView(txtpnIssuesId);
		
		chckbxAddToHighlights = new JCheckBox("Add to Highlights:");
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
		scrollPane.setViewportView(txtpnHighlights);
		txtpnHighlights.setText("Highlights");
		txtpnHighlights.setBorder(null);
		scrollPane.setBorder(txtSpreadsheetLink.getBorder());
		
		btnsPane = new JPanel();
		GridBagConstraints gbc_btnsPane = new GridBagConstraints();
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
	}
	
	public void init()
	{
		loadProducts();
		setupBtnsAction();
	}
	
	private void setupBtnsAction()
	{
		btnAdd.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				controller.generateProductReport();
			}
		});
		
		btnEdit.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				controller.generateProductReport();
				controller.sendReportMail();
			}
		});
		
		btnDel.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				controller.sendReportMail();
			}
		});
		
		btnReset.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				resetFields();
			}
		});
		
		btnSave.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				
			}
		});
		
		btnClose.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				dispose();
			}
		});
		
		comboBox.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				controller.updateFieldsForSelectedProduct(comboBox.getSelectedIndex());
			}
		});
	}
	
	public void resetFields()
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
	
	public void setupFields(ProductReport pr)
	{
		setReleases(pr.getReleasesString());
		setTopIssuesLabel(pr.getTopIssueLabel());
		setDashboardLink(pr.getDashboardLink());
		setSpreadsheetLink(pr.getSpreadsheetLink());
		setAddChart(pr.getAddChart());
		setChartBuild(pr.getChartBuild());
		setChartIssues(pr.getChartIssues());
		setAddHighlights(pr.getAddHighlight());
		setHighlights(pr.getHighlights());
	}
	
	public ProductReport getFieldsAsProduct()
	{
		ProductReport pr = new ProductReport();
		
		pr.setName(getProductName());
		pr.setReleases(getReleases().split(" "));
		pr.setTopIssueLabel(getTopIssueLabel());
		pr.setDashboardLink(getDashboardLink());
		pr.setSpreadsheetLink(getSpreadsheetLink());
		pr.setAddChart(addChart());
		pr.setChartBuild(getChartBuilds());
		pr.setChartIssues(getChartIssues());
		pr.setAddHighlight(addHighlights());
		pr.setHighlights(getHighlights());
		
		JComboBox<ProductReport> getCom;
		
		return pr;
	}
	
	public void setupComboBox(ArrayList<ProductReport> products)
	{
		comboBox.removeAllItems();
		
		for (ProductReport pr : products)
		{
			comboBox.addItem(pr.getName());
		}
		
		if(comboBox.getItemCount() > 0)
			comboBox.setSelectedIndex(0);
	}
	
	// Getters and Setters
	public void setController(ReportController rc)
	{
		controller = rc;
	}
	
	public void setChartBuild(String text)
	{
		txtChartBuild.setText(text);
	}
	
	public void setChartIssues(String text)
	{
		txtpnIssuesId.setText(text);
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
	
	public String getChartIssues()
	{
		return txtpnIssuesId.getText();
	}
	
	public boolean addHighlights()
	{
		return chckbxAddToHighlights.isSelected();
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
		System.out.println(comboBox.getSelectedItem().toString());
		return comboBox.getSelectedItem().toString();
	}
	
	public JComboBox<String> getComboBox()
	{
		return comboBox;
	}
	
	public void setComboBox(JComboBox<String> comboBox)
	{
		this.comboBox = comboBox;
	}
}
