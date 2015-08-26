package panes;


import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import core.Icons;
import core.SharedObjs;
import core.XmlMngr;
import objects.CustomFilterItem;
import objects.CustomFiltersList;
import style.FiltersModelListener;
import style.FiltersTableCheckboxCellRenderer;
import style.FiltersTableIntCellRenderer;
import style.FiltersTableStringCellRenderer;
import style.MyFiltersTableModel;
import style.SharedFiltersTableModel;
import java.awt.Color;;


@SuppressWarnings("serial")
public class CustomFiltersPane2 extends JDialog
{
	private int						lastTab;
	private JButton					btnDone;
	private JTabbedPane				tabbedPane;
	private JPanel					myFiltersPane;
	private JPanel					sharedPane;
	private CustomFiltersList		filtersList;
	private JScrollPane				scrollPaneTable1;
	private JButton					btnAdd;
	private JButton					btnDel;
	private JLabel					label;
	private JScrollPane				scrollPaneTable2;
	private JTable					myFiltersTable;
	private MyFiltersTableModel		myFiltersTableModel;
	private JTable					sharedFiltersTable;
	private SharedFiltersTableModel	sharedTableModel;
	
	// Create the dialog.
	public CustomFiltersPane2()
	{
		setModalityType(ModalityType.APPLICATION_MODAL);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setVisible(false);
		setTitle("Filters Manager");
		setBounds(100, 100, 1172, 441);
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {0, 0};
		gridBagLayout.rowHeights = new int[] {0, 0};
		gridBagLayout.columnWeights = new double[] {1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[] {1.0, 0.0};
		getContentPane().setLayout(gridBagLayout);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		lastTab = -1;
		tabbedPane.addChangeListener(new ChangeListener()
		{
			@Override
			public void stateChanged(ChangeEvent arg0)
			{
				if (arg0.getSource() != null && arg0.getSource().getClass() == JTabbedPane.class)
				{
					JTabbedPane tabPane = (JTabbedPane) arg0.getSource();
					if (tabPane.getSelectedIndex() != lastTab)
					{
						System.out.println("Pane2 Mudou tab de " + lastTab + " para "
										   + tabPane.getSelectedIndex());
										   
						switch (lastTab)
						{
							case 0:
								// TODO Save My Filters
								break;
							case 1:
								// TODO Save Shared Filters
								break;
							case 2:
								// TODO Save Active Filters
								break;
						}
						
						switch (tabPane.getSelectedIndex())
						{
							case 0:
								// TODO Load My Filters
								break;
							case 1:
								// TODO Load Shared Filters
								break;
							case 2:
								// TODO Load Active Filters
								break;
						}
						
						lastTab = tabPane.getSelectedIndex();
					}
				}
			}
		});
		GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
		gbc_tabbedPane.insets = new Insets(5, 5, 5, 5);
		gbc_tabbedPane.fill = GridBagConstraints.BOTH;
		gbc_tabbedPane.gridx = 0;
		gbc_tabbedPane.gridy = 0;
		getContentPane().add(tabbedPane, gbc_tabbedPane);
		
		myFiltersPane = new JPanel();
		tabbedPane.addTab("My filters", null, myFiltersPane, null);
		GridBagLayout gbl_myFiltersPane = new GridBagLayout();
		gbl_myFiltersPane.columnWidths = new int[] {0, 0};
		gbl_myFiltersPane.rowHeights = new int[] {0, 0, 0, 0, 0, 0, 0};
		gbl_myFiltersPane.columnWeights = new double[] {1.0, 0.0};
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
		
		sharedPane = new JPanel();
		tabbedPane.addTab("Shared filters", null, sharedPane, null);
		GridBagLayout gbl_sharedPane = new GridBagLayout();
		gbl_sharedPane.columnWidths = new int[] {0, 0, 0};
		gbl_sharedPane.rowHeights = new int[] {0, 0, 0, 0, 0};
		gbl_sharedPane.columnWeights = new double[] {1.0, 0.0, Double.MIN_VALUE};
		gbl_sharedPane.rowWeights = new double[] {0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		sharedPane.setLayout(gbl_sharedPane);
		
		label = new JLabel("My Filters List:");
		label.setPreferredSize(new Dimension(70, 23));
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font("Tahoma", Font.BOLD, 11));
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.fill = GridBagConstraints.HORIZONTAL;
		gbc_label.insets = new Insets(2, 0, 0, 5);
		gbc_label.gridx = 0;
		gbc_label.gridy = 0;
		sharedPane.add(label, gbc_label);
		
		scrollPaneTable2 = new JScrollPane();
		scrollPaneTable2.getViewport().setBackground(Color.white);
		GridBagConstraints gbc_scrollPaneTable2 = new GridBagConstraints();
		gbc_scrollPaneTable2.gridheight = 3;
		gbc_scrollPaneTable2.insets = new Insets(0, 0, 0, 5);
		gbc_scrollPaneTable2.fill = GridBagConstraints.BOTH;
		gbc_scrollPaneTable2.gridx = 0;
		gbc_scrollPaneTable2.gridy = 1;
		sharedPane.add(scrollPaneTable2, gbc_scrollPaneTable2);
		
		// Shared Filters Table Model
		sharedTableModel = new SharedFiltersTableModel();
		sharedTableModel.addTableModelListener(new FiltersModelListener());
		
		// Shared Filters Table Definition
		sharedFiltersTable = new JTable(sharedTableModel);
		sharedFiltersTable.setDefaultRenderer(String.class, new FiltersTableStringCellRenderer());
		sharedFiltersTable.setDefaultRenderer(Boolean.class, new FiltersTableCheckboxCellRenderer());
		sharedFiltersTable.setDefaultRenderer(Integer.class, new FiltersTableIntCellRenderer());
		sharedFiltersTable.getColumnModel().setColumnMargin(1);
		sharedFiltersTable.getColumnModel().getColumn(0).setMinWidth(20);
		sharedFiltersTable.getColumnModel().getColumn(0).setPreferredWidth(20);
		sharedFiltersTable.getColumnModel().getColumn(1).setMinWidth(60);
		sharedFiltersTable.getColumnModel().getColumn(1).setPreferredWidth(60);
		sharedFiltersTable.getColumnModel().getColumn(2).setMinWidth(100);
		sharedFiltersTable.getColumnModel().getColumn(2).setPreferredWidth(150);
		sharedFiltersTable.getColumnModel().getColumn(3).setMinWidth(120);
		sharedFiltersTable.getColumnModel().getColumn(3).setPreferredWidth(200);
		sharedFiltersTable.getColumnModel().getColumn(4).setMinWidth(200);
		sharedFiltersTable.getColumnModel().getColumn(4).setPreferredWidth(300);
		sharedFiltersTable.getColumnModel().getColumn(5).setMinWidth(25);
		sharedFiltersTable.getColumnModel().getColumn(5).setPreferredWidth(40);
		sharedFiltersTable.getColumnModel().getColumn(6).setMinWidth(25);
		sharedFiltersTable.getColumnModel().getColumn(6).setPreferredWidth(40);
		sharedFiltersTable.getColumnModel().getColumn(7).setMinWidth(25);
		sharedFiltersTable.getColumnModel().getColumn(7).setPreferredWidth(40);
		sharedFiltersTable.getColumnModel().getColumn(8).setMinWidth(25);
		sharedFiltersTable.getColumnModel().getColumn(8).setPreferredWidth(40);
		sharedFiltersTable.getColumnModel().getColumn(9).setMinWidth(25);
		sharedFiltersTable.getColumnModel().getColumn(9).setPreferredWidth(40);
		sharedFiltersTable.getColumnModel().getColumn(10).setMinWidth(25);
		sharedFiltersTable.getColumnModel().getColumn(10).setPreferredWidth(40);
		sharedFiltersTable.getColumnModel().getColumn(11).setMinWidth(25);
		sharedFiltersTable.getColumnModel().getColumn(11).setPreferredWidth(25);
		sharedFiltersTable.getColumnModel().getColumn(12).setMinWidth(60);
		sharedFiltersTable.getColumnModel().getColumn(12).setPreferredWidth(65);
		sharedFiltersTable.getColumnModel().getColumn(13).setMinWidth(2);
		sharedFiltersTable.getColumnModel().getColumn(13).setMaxWidth(2);
		sharedFiltersTable.getColumnModel().getColumn(13).setPreferredWidth(2);
		sharedFiltersTable.setFillsViewportHeight(false);
		sharedFiltersTable.setSurrendersFocusOnKeystroke(true);
		sharedFiltersTable.setColumnSelectionAllowed(true);
		sharedFiltersTable.setCellSelectionEnabled(true);
		sharedFiltersTable.getTableHeader().setReorderingAllowed(true);
		sharedFiltersTable.setAutoCreateRowSorter(true);
		sharedFiltersTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		sharedFiltersTable.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				if (e.getClickCount() == 1)
				{
					JTable target = (JTable) e.getSource();
					//int row = target.getSelectedRow();
					int column = target.getSelectedColumn();
					
					if (column == 0)
					{
						System.out.println("Bang!");
						// table.setRowSelectionInterval(row, row);
						sharedFiltersTable.setColumnSelectionInterval(sharedFiltersTable.getColumnCount() - 1,
																	  column);
					}
					/*
					 * table.changeSelection(row, column, false, false); table.requestFocus();
					 */
				}
			}
		});
		scrollPaneTable2.setViewportView(sharedFiltersTable);
		
		scrollPaneTable1 = new JScrollPane();
		scrollPaneTable1.getViewport().setBackground(Color.white);
		GridBagConstraints gbc_scrollPaneTable1 = new GridBagConstraints();
		gbc_scrollPaneTable1.gridheight = 5;
		gbc_scrollPaneTable1.insets = new Insets(0, 0, 0, 5);
		gbc_scrollPaneTable1.fill = GridBagConstraints.BOTH;
		gbc_scrollPaneTable1.gridx = 0;
		gbc_scrollPaneTable1.gridy = 1;
		myFiltersPane.add(scrollPaneTable1, gbc_scrollPaneTable1);
		
		// My Filters Table Model
		myFiltersTableModel = new MyFiltersTableModel();
		myFiltersTableModel.addTableModelListener(new FiltersModelListener());
		
		// My Filters Table Definition
		myFiltersTable = new JTable(myFiltersTableModel);
		myFiltersTable.setDefaultRenderer(String.class, new FiltersTableStringCellRenderer());
		myFiltersTable.setDefaultRenderer(Boolean.class, new FiltersTableCheckboxCellRenderer());
		myFiltersTable.setDefaultRenderer(Integer.class, new FiltersTableIntCellRenderer());
		myFiltersTable.getColumnModel().setColumnMargin(1);
		myFiltersTable.getColumnModel().getColumn(0).setMinWidth(20);
		myFiltersTable.getColumnModel().getColumn(0).setPreferredWidth(20);
		myFiltersTable.getColumnModel().getColumn(1).setMinWidth(60);
		myFiltersTable.getColumnModel().getColumn(1).setPreferredWidth(60);
		myFiltersTable.getColumnModel().getColumn(2).setMinWidth(120);
		myFiltersTable.getColumnModel().getColumn(2).setPreferredWidth(120);
		myFiltersTable.getColumnModel().getColumn(3).setMinWidth(180);
		myFiltersTable.getColumnModel().getColumn(3).setPreferredWidth(180);
		myFiltersTable.getColumnModel().getColumn(4).setMinWidth(240);
		myFiltersTable.getColumnModel().getColumn(4).setPreferredWidth(280);
		myFiltersTable.getColumnModel().getColumn(5).setMinWidth(40);
		myFiltersTable.getColumnModel().getColumn(5).setPreferredWidth(40);
		myFiltersTable.getColumnModel().getColumn(6).setMinWidth(40);
		myFiltersTable.getColumnModel().getColumn(6).setPreferredWidth(40);
		myFiltersTable.getColumnModel().getColumn(7).setMinWidth(40);
		myFiltersTable.getColumnModel().getColumn(7).setPreferredWidth(40);
		myFiltersTable.getColumnModel().getColumn(8).setMinWidth(40);
		myFiltersTable.getColumnModel().getColumn(8).setPreferredWidth(40);
		myFiltersTable.getColumnModel().getColumn(9).setMinWidth(40);
		myFiltersTable.getColumnModel().getColumn(9).setPreferredWidth(40);
		myFiltersTable.getColumnModel().getColumn(10).setMinWidth(40);
		myFiltersTable.getColumnModel().getColumn(10).setPreferredWidth(40);
		myFiltersTable.getColumnModel().getColumn(11).setMinWidth(40);
		myFiltersTable.getColumnModel().getColumn(11).setPreferredWidth(40);
		myFiltersTable.getColumnModel().getColumn(12).setMinWidth(40);
		myFiltersTable.getColumnModel().getColumn(12).setPreferredWidth(40);
		myFiltersTable.getColumnModel().getColumn(13).setMinWidth(40);
		myFiltersTable.getColumnModel().getColumn(13).setPreferredWidth(40);
		myFiltersTable.getColumnModel().getColumn(14).setMinWidth(2);
		myFiltersTable.getColumnModel().getColumn(14).setMaxWidth(2);
		myFiltersTable.getColumnModel().getColumn(14).setPreferredWidth(2);
		myFiltersTable.setFillsViewportHeight(false);
		myFiltersTable.setSurrendersFocusOnKeystroke(true);
		myFiltersTable.setColumnSelectionAllowed(true);
		myFiltersTable.setCellSelectionEnabled(true);
		myFiltersTable.getTableHeader().setReorderingAllowed(true);
		myFiltersTable.setAutoCreateRowSorter(true);
		myFiltersTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		myFiltersTable.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				if (e.getClickCount() == 1)
				{
					JTable target = (JTable) e.getSource();
					//int row = target.getSelectedRow();
					int column = target.getSelectedColumn();
					
					if (column == 0)
					{
						System.out.println("Bang!");
						// table.setRowSelectionInterval(row, row);
						myFiltersTable.setColumnSelectionInterval(myFiltersTable.getColumnCount() - 2,
																  column);
					}
					/*
					 * table.changeSelection(row, column, false, false); table.requestFocus();
					 */
				}
			}
		});
		
		scrollPaneTable1.setViewportView(myFiltersTable);
		
		btnAdd = new JButton("");
		btnAdd.setIcon(Icons.add);
		btnAdd.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				if (!myFiltersTableModel.hasEmptyRow())
				{
					myFiltersTableModel.addEmptyRow();
				}
				else
				{
					myFiltersTable.setColumnSelectionInterval(2, 2);
					int nRows = myFiltersTableModel.getRowCount() - 1;
					myFiltersTable.setRowSelectionInterval(myFiltersTable.convertRowIndexToView(nRows),
														   myFiltersTable.convertRowIndexToView(nRows));
					JOptionPane.showMessageDialog(SharedObjs.teste, "The filter name can not be empty");
					myFiltersTable.editCellAt(myFiltersTable.convertRowIndexToView(nRows), 2);
					myFiltersTable.transferFocus();
				}
			}
		});
		btnAdd.setPreferredSize(new Dimension(25, 25));
		btnAdd.setMinimumSize(new Dimension(25, 25));
		btnAdd.setMargin(new Insets(2, 2, 2, 2));
		GridBagConstraints gbc_btnAdd = new GridBagConstraints();
		gbc_btnAdd.insets = new Insets(0, 0, 5, 0);
		gbc_btnAdd.gridx = 1;
		gbc_btnAdd.gridy = 2;
		myFiltersPane.add(btnAdd, gbc_btnAdd);
		
		btnDel = new JButton("");
		btnDel.setIcon(Icons.delete);
		btnDel.setPreferredSize(new Dimension(25, 25));
		btnDel.setMinimumSize(new Dimension(25, 25));
		btnDel.setMargin(new Insets(2, 2, 2, 2));
		GridBagConstraints gbc_btnDel = new GridBagConstraints();
		gbc_btnDel.insets = new Insets(0, 0, 5, 0);
		gbc_btnDel.gridx = 1;
		gbc_btnDel.gridy = 3;
		myFiltersPane.add(btnDel, gbc_btnDel);
		
		btnDone = new JButton("Save and Exit");
		btnDone.setMaximumSize(new Dimension(200, 23));
		btnDone.setPreferredSize(new Dimension(140, 23));
		btnDone.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				saveFilters();
				setVisible(false);
			}
		});
		btnDone.setMinimumSize(new Dimension(120, 23));
		GridBagConstraints gbc_btnDone = new GridBagConstraints();
		gbc_btnDone.insets = new Insets(5, 5, 5, 5);
		gbc_btnDone.gridx = 0;
		gbc_btnDone.gridy = 1;
		getContentPane().add(btnDone, gbc_btnDone);
	}
	
	/**
	 * 
	 */
	public void updateFiltersData()
	{
		setMyFiltersFields();
		setSharedFields();
		
	}
	
	public void saveFilters()
	{
		for (CustomFilterItem filter : filtersList)
		{
			XmlMngr.setMyFiltersValueOf(filter);
		}
	}
	
	public void open()
	{
		updateFiltersData();
		setLocationRelativeTo(SharedObjs.satFrame);
		setVisible(true);
	}
	
	public void setMyFiltersFields()
	{
		while (myFiltersTableModel.getRowCount() > 0)
			myFiltersTableModel.removeRow(myFiltersTableModel.getRowCount() - 1);
			
		// Load MyFilters
		for (CustomFilterItem filter : SharedObjs.getUserFiltersList())
		{
			myFiltersTableModel.addRow(filter);
		}
		
	}
	
	public void setSharedFields()
	{
		while (sharedTableModel.getRowCount() > 0)
			sharedTableModel.removeRow(sharedTableModel.getRowCount() - 1);
			
		// Load SharedFilters
		for (CustomFilterItem filter : SharedObjs.getSharedFiltersList())
		{
			sharedTableModel.addRow(filter);
		}
	}
	
	public void setActiveFields()
	{
		/*
		 * while (sharedTableModel.getRowCount() > 0) sharedTableModel.removeRow(sharedTableModel.getRowCount() - 1);
		 * 
		 * // Load ActiveFilters for (CustomFilterItem filter : SharedObjs.getActiveFiltersList()) {
		 * 
		 * }
		 */
	}
}
