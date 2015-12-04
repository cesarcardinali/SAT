package tests.planToJira;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import tests.planToJira.design.CRsTableCheckboxCellRenderer;
import tests.planToJira.design.CRsTableIntCellRenderer;
import tests.planToJira.design.CRsTableStringCellRenderer;
import tests.planToJira.design.CRsTableModel;


@SuppressWarnings("serial")
public class JiraPane extends JPanel
{
    @SuppressWarnings("unused")
    private int           lastTab;
	private JScrollPane   scrollPaneTable1;
	private JPanel        myFiltersPane;
	private JTable        myFiltersTable;
	private CRsTableModel cRsTableModel;
	private JButton       btnDone;
	
	public JiraPane()
	{
		setBounds(100, 100, 1172, 441);
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {0, 0};
		gridBagLayout.rowHeights = new int[] {0, 0};
		gridBagLayout.columnWeights = new double[] {1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[] {1.0, 0.0};
		setLayout(gridBagLayout);
		
		myFiltersPane = new JPanel();
		// tabbedPane.addTab("My filters", null, myFiltersPane, null);
		GridBagLayout gbl_myFiltersPane = new GridBagLayout();
		gbl_myFiltersPane.columnWidths = new int[] {0};
		gbl_myFiltersPane.rowHeights = new int[] {0, 0, 0, 0, 0, 0, 0};
		gbl_myFiltersPane.columnWeights = new double[] {1.0};
		gbl_myFiltersPane.rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		myFiltersPane.setLayout(gbl_myFiltersPane);
		
		JLabel lblFiltersList = new JLabel("My Filters List:");
		GridBagConstraints gbc_lblFiltersList = new GridBagConstraints();
		gbc_lblFiltersList.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblFiltersList.insets = new Insets(2, 0, 5, 0);
		gbc_lblFiltersList.gridx = 0;
		gbc_lblFiltersList.gridy = 0;
		myFiltersPane.add(lblFiltersList, gbc_lblFiltersList);
		lblFiltersList.setHorizontalAlignment(SwingConstants.CENTER);
		lblFiltersList.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblFiltersList.setPreferredSize(new Dimension(70, 23));
		
		GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
		gbc_tabbedPane.insets = new Insets(5, 5, 5, 0);
		gbc_tabbedPane.fill = GridBagConstraints.BOTH;
		gbc_tabbedPane.gridx = 0;
		gbc_tabbedPane.gridy = 0;
		add(myFiltersPane, gbc_tabbedPane);
		
		scrollPaneTable1 = new JScrollPane();
		scrollPaneTable1.getViewport().setBackground(Color.white);
		GridBagConstraints gbc_scrollPaneTable1 = new GridBagConstraints();
		gbc_scrollPaneTable1.gridheight = 5;
		gbc_scrollPaneTable1.insets = new Insets(5, 5, 0, 0);
		gbc_scrollPaneTable1.fill = GridBagConstraints.BOTH;
		gbc_scrollPaneTable1.gridx = 0;
		gbc_scrollPaneTable1.gridy = 1;
		myFiltersPane.add(scrollPaneTable1, gbc_scrollPaneTable1);
		
		// My Filters Table Model
		cRsTableModel = new CRsTableModel();
		
		// My Filters Table Definition
		myFiltersTable = new JTable(cRsTableModel);
		myFiltersTable.setDefaultRenderer(String.class, new CRsTableStringCellRenderer());
		myFiltersTable.setDefaultRenderer(Boolean.class, new CRsTableCheckboxCellRenderer());
		myFiltersTable.setDefaultRenderer(Integer.class, new CRsTableIntCellRenderer());
		myFiltersTable.getColumnModel().setColumnMargin(1);
		myFiltersTable.getColumnModel().getColumn(0).setMinWidth(25);
		myFiltersTable.getColumnModel().getColumn(0).setPreferredWidth(30);
		myFiltersTable.getColumnModel().getColumn(1).setMinWidth(50);
		myFiltersTable.getColumnModel().getColumn(1).setPreferredWidth(100);
		myFiltersTable.getColumnModel().getColumn(2).setMinWidth(120);
		myFiltersTable.getColumnModel().getColumn(2).setPreferredWidth(250);
		myFiltersTable.getColumnModel().getColumn(3).setMinWidth(120);
		myFiltersTable.getColumnModel().getColumn(3).setPreferredWidth(290);
		myFiltersTable.getColumnModel().getColumn(4).setMinWidth(25);
		myFiltersTable.getColumnModel().getColumn(4).setPreferredWidth(40);
		myFiltersTable.getColumnModel().getColumn(5).setMinWidth(25);
		myFiltersTable.getColumnModel().getColumn(5).setPreferredWidth(40);
		myFiltersTable.getColumnModel().getColumn(6).setMinWidth(25);
		myFiltersTable.getColumnModel().getColumn(6).setPreferredWidth(40);
		myFiltersTable.getColumnModel().getColumn(7).setMinWidth(25);
		myFiltersTable.getColumnModel().getColumn(7).setPreferredWidth(40);
		myFiltersTable.getColumnModel().getColumn(8).setMinWidth(25);
		myFiltersTable.getColumnModel().getColumn(8).setPreferredWidth(40);
		myFiltersTable.getColumnModel().getColumn(9).setMinWidth(25);
		myFiltersTable.getColumnModel().getColumn(9).setPreferredWidth(40);
		myFiltersTable.getColumnModel().getColumn(10).setMinWidth(25);
		myFiltersTable.getColumnModel().getColumn(10).setPreferredWidth(40);
		myFiltersTable.getColumnModel().getColumn(11).setMinWidth(25);
		myFiltersTable.getColumnModel().getColumn(11).setPreferredWidth(40);
		myFiltersTable.getColumnModel().getColumn(12).setMinWidth(2);
		myFiltersTable.getColumnModel().getColumn(12).setMaxWidth(2);
		myFiltersTable.getColumnModel().getColumn(12).setPreferredWidth(2);
		myFiltersTable.setFillsViewportHeight(false);
		myFiltersTable.setSurrendersFocusOnKeystroke(true);
		myFiltersTable.setColumnSelectionAllowed(true);
		myFiltersTable.setCellSelectionEnabled(true);
		myFiltersTable.getTableHeader().setReorderingAllowed(true);
		myFiltersTable.setAutoCreateRowSorter(true);
		myFiltersTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		scrollPaneTable1.setViewportView(myFiltersTable);
		
		btnDone = new JButton("Save and Exit");
		btnDone.setMaximumSize(new Dimension(200, 23));
		btnDone.setPreferredSize(new Dimension(140, 23));
		btnDone.setMinimumSize(new Dimension(120, 23));
		GridBagConstraints gbc_btnDone = new GridBagConstraints();
		gbc_btnDone.insets = new Insets(5, 5, 0, 0);
		gbc_btnDone.gridx = 0;
		gbc_btnDone.gridy = 1;
		add(btnDone, gbc_btnDone);
		
		lastTab = -1;
	}
	
	public void open()
	{
		lastTab = -1;
		setVisible(true);
	}
}