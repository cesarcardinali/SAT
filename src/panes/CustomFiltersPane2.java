package panes;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import core.Icons;
import core.Logger;
import core.SharedObjs;
import core.XmlMngr;
import objects.CustomFilterItem;
import objects.CustomFiltersList;
import javax.swing.JList;
import javax.swing.JTable;;


@SuppressWarnings("serial")
public class CustomFiltersPane2 extends JDialog
{
	private JTextField		  txtName;
	private JTextField		  txtRegex;
	private JTextField		  txtHeader;
	private JTextField		  textNameS;
	private JTextField		  textRegexS;
	private JTextField		  textHeaderS;
	private JTextField		  textField_3;
	private JTextField		  textField_4;
	private JTextField		  textField_5;
	private JCheckBox		  chckbxRadio;
	private JCheckBox		  chckbxSystem;
	private JCheckBox		  chckbxBugreport;
	private JCheckBox		  chckbxMain;
	private JCheckBox		  chckbxKernel;
	private JCheckBox		  chckbxRoutput;
	private JCheckBox		  chckbxShared;
	private JCheckBox		  chckbxPublic;
	private JCheckBox		  chcbxMainS;
	private JCheckBox		  chcbxSystemS;
	private JCheckBox		  checkBox_4;
	private JCheckBox		  checkBox_5;
	private JCheckBox		  checkBox_6;
	private JCheckBox		  checkBox_7;
	private JCheckBox		  checkBox_8;
	private JCheckBox		  checkBox_9;
	private JCheckBox		  checkBox_10;
	private JCheckBox		  checkBox_11;
	private JCheckBox		  checkBox_12;
	private JCheckBox		  checkBox_13;
	private JCheckBox		  chcbxActiveA;
	private JButton			  btnDone;
	private JButton			  btnAdd;
	private JButton			  btnDel;
	private JButton			  btnNew;
	private JComboBox<String> comboBox;
	private JComboBox<String> comboBox_1;
	private JComboBox<String> comboBox_2;
	private JTabbedPane		  tabbedPane;
	private JPanel			  myFiltersPane;
	private JPanel			  sharedPane;
	private JPanel			  activePane;
	private JPanel			  panel;
	private JPanel			  panel_2;
	private JLabel			  lblShared;
	private JLabel			  lblPublic;
	private JLabel			  label;
	private JLabel			  label_1;
	private JLabel			  label_2;
	private JLabel			  label_3;
	private JLabel			  label_5;
	private JLabel			  label_6;
	private JLabel			  label_7;
	private JLabel			  label_8;
	private JLabel			  label_9;
	private JLabel			  label_10;
	private JLabel			  label_11;
	private CustomFiltersList filtersList;
	private JLabel			  label_4;
	private JCheckBox		  chckbxActiveM;
	private JLabel			  label_12;
	private JCheckBox		  chckbxActiveS;
	private JButton button;
	private JTable table;
	private JCheckBox chckbxNewCheckBox;
	
	// Create the dialog.
	public CustomFiltersPane2()
	{
		setModalityType(ModalityType.APPLICATION_MODAL);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setVisible(false);
		setTitle("Filters Manager");
		setBounds(100, 100, 965, 575);
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {0, 0};
		gridBagLayout.rowHeights = new int[] {0, 0};
		gridBagLayout.columnWeights = new double[] {1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[] {0.0, 0.0};
		getContentPane().setLayout(gridBagLayout);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.addChangeListener(new ChangeListener()
		{
			@Override
			public void stateChanged(ChangeEvent arg0)
			{
				System.out.println("Mudou tab: " + tabbedPane.getSelectedIndex());
			}
		});
		GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
		gbc_tabbedPane.anchor = GridBagConstraints.WEST;
		gbc_tabbedPane.insets = new Insets(5, 5, 5, 5);
		gbc_tabbedPane.fill = GridBagConstraints.BOTH;
		gbc_tabbedPane.gridx = 0;
		gbc_tabbedPane.gridy = 0;
		getContentPane().add(tabbedPane, gbc_tabbedPane);
		
		myFiltersPane = new JPanel();
		tabbedPane.addTab("My filters", null, myFiltersPane, null);
		GridBagLayout gbl_myFiltersPane = new GridBagLayout();
		gbl_myFiltersPane.columnWidths = new int[] {0, 0, 0, 0, 0, 0, 0};
		gbl_myFiltersPane.rowHeights = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_myFiltersPane.columnWeights = new double[] {1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0};
		gbl_myFiltersPane.rowWeights = new double[] {1.0,
													 1.0,
													 0.0,
													 0.0,
													 1.0,
													 0.0,
													 0.0,
													 0.0, 0.0,
													 Double.MIN_VALUE};
		myFiltersPane.setLayout(gbl_myFiltersPane);
		
		JLabel lblFiltersList = new JLabel("Filters list:");
		lblFiltersList.setHorizontalAlignment(SwingConstants.RIGHT);
		lblFiltersList.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblFiltersList.setPreferredSize(new Dimension(70, 23));
		GridBagConstraints gbc_lblFiltersList = new GridBagConstraints();
		gbc_lblFiltersList.insets = new Insets(5, 10, 5, 5);
		gbc_lblFiltersList.gridx = 0;
		gbc_lblFiltersList.gridy = 0;
		myFiltersPane.add(lblFiltersList, gbc_lblFiltersList);
		
		comboBox = new JComboBox<String>();
		comboBox.setPreferredSize(new Dimension(300, 23));
		comboBox.setMinimumSize(new Dimension(300, 23));
		comboBox.setMaximumRowCount(6);
		comboBox.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if(comboBox.getSelectedIndex() > -1)
					setMyFiltersFields();
			}
		});
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.insets = new Insets(5, 0, 5, 5);
		gbc_comboBox.anchor = GridBagConstraints.WEST;
		gbc_comboBox.gridx = 2;
		gbc_comboBox.gridy = 0;
		myFiltersPane.add(comboBox, gbc_comboBox);
		
		btnDel = new JButton("");
		btnDel.setMinimumSize(new Dimension(25, 25));
		btnDel.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				if (comboBox.getSelectedIndex() >= 0)
				{
					filtersList = SharedObjs.getUserFiltersList();
					CustomFilterItem aux = null;
					
					for (CustomFilterItem item : filtersList)
					{
						if (comboBox.getSelectedItem().equals(item.getName()))
						{
							aux = item;
							SharedObjs.parserPane.getFiltersResultsTree().removeCustomNode(item.getName());
							
							break;
						}
					}
					if (aux != null)
					{
						Logger.log(Logger.TAG_CUSTOMFILTER, aux.getName());
						
						filtersList.remove(aux);
						comboBox.removeItem(aux.getName());
					}
				}
			}
		});
		btnDel.setIcon(Icons.delete);
		btnDel.setPreferredSize(new Dimension(25, 25));
		btnDel.setMargin(new Insets(2, 2, 2, 2));
		GridBagConstraints gbc_btnDel = new GridBagConstraints();
		gbc_btnDel.insets = new Insets(5, 0, 5, 5);
		gbc_btnDel.gridx = 3;
		gbc_btnDel.gridy = 0;
		myFiltersPane.add(btnDel, gbc_btnDel);
		
		btnAdd = new JButton("");
		btnAdd.setMinimumSize(new Dimension(25, 25));
		btnAdd.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (!txtName.getText().equals("") && Character.isDigit(txtName.getText().toCharArray()[0]))
				{
					JOptionPane.showMessageDialog(SharedObjs.getCustomFiltersPane(),
												  "The filter name cannot begin with a digit nor be empty.");
				}
				else if (!txtName.getText().equals(""))
				{
					CustomFilterItem auxItem = new CustomFilterItem(chckbxPublic.isSelected() ? " "
																							  : SharedObjs.getUser(),
																	txtName.getText(),
																	txtRegex.getText(),
																	txtHeader.getText(),
																	chckbxMain.isSelected(),
																	chckbxSystem.isSelected(),
																	chckbxKernel.isSelected(),
																	chckbxRadio.isSelected(),
																	chckbxBugreport.isSelected(),
																	chckbxRoutput.isSelected(),
																	chckbxShared.isSelected(),
																	chckbxPublic.isSelected(),
																	chckbxActiveM.isSelected());
																	
					if (chckbxActiveM.isSelected())
					{
						int id = SharedObjs.getActiveFiltersList().indexOf(auxItem);
						if (id >= 0)
						{
							SharedObjs.getActiveFiltersList().set(id, auxItem);
						}
						else
						{
							SharedObjs.getActiveFiltersList().add(auxItem);
						}
					}
					else
					{
						int id = SharedObjs.getActiveFiltersList().indexOf(auxItem);
						if (id >= 0)
						{
							SharedObjs.getActiveFiltersList().remove(id);
						}
					}
					
					filtersList = SharedObjs.getUserFiltersList();
					int index = filtersList.indexOf(auxItem.getName());
					
					if (index >= 0)
					{
						int answ = JOptionPane.showConfirmDialog(SharedObjs.getCustomFiltersPane(),
																 "There is another filter using this name.\nDo you want to overwrite this filter?.");
						if (answ == 0)
						{
							filtersList.remove(index);
							filtersList.add(auxItem);
						}
					}
					else
					{
						SharedObjs.getUserFiltersList().add(auxItem);
						comboBox.insertItemAt(txtName.getText(), comboBox.getItemCount());
						SharedObjs.parserPane.getFiltersResultsTree().addCustomFilters(txtName.getText());
					}
					
					clearfields();
				}
				else
				{
					JOptionPane.showMessageDialog(SharedObjs.getCustomFiltersPane(),
												  "The filter name can not be empty.");
				}
			}
		});
		btnAdd.setPreferredSize(new Dimension(25, 25));
		btnAdd.setMargin(new Insets(2, 2, 2, 2));
		btnAdd.setIcon(Icons.add);
		GridBagConstraints gbc_btnAdd = new GridBagConstraints();
		gbc_btnAdd.insets = new Insets(5, 0, 5, 5);
		gbc_btnAdd.gridx = 4;
		gbc_btnAdd.gridy = 0;
		myFiltersPane.add(btnAdd, gbc_btnAdd);
		
		btnNew = new JButton("New");
		btnNew.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				clearfields();
			}
		});
		btnNew.setForeground(new Color(0, 51, 153));
		btnNew.setFont(new Font("Arial Black", Font.BOLD, 11));
		btnNew.setMargin(new Insets(0, 0, 0, 0));
		btnNew.setPreferredSize(new Dimension(36, 25));
		GridBagConstraints gbc_btnNew = new GridBagConstraints();
		gbc_btnNew.insets = new Insets(5, 0, 5, 5);
		gbc_btnNew.gridx = 5;
		gbc_btnNew.gridy = 0;
		myFiltersPane.add(btnNew, gbc_btnNew);
		
		table = new JTable();
		GridBagConstraints gbc_table = new GridBagConstraints();
		gbc_table.gridheight = 8;
		gbc_table.insets = new Insets(0, 0, 0, 5);
		gbc_table.fill = GridBagConstraints.BOTH;
		gbc_table.gridx = 0;
		gbc_table.gridy = 1;
		myFiltersPane.add(table, gbc_table);
		
		JLabel lblFilterName = new JLabel("Filter Name:");
		GridBagConstraints gbc_lblFilterName = new GridBagConstraints();
		gbc_lblFilterName.anchor = GridBagConstraints.EAST;
		gbc_lblFilterName.insets = new Insets(0, 0, 5, 5);
		gbc_lblFilterName.gridx = 1;
		gbc_lblFilterName.gridy = 1;
		myFiltersPane.add(lblFilterName, gbc_lblFilterName);
		
		txtName = new JTextField();
		txtName.setPreferredSize(new Dimension(6, 23));
		GridBagConstraints gbc_txtName = new GridBagConstraints();
		gbc_txtName.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtName.anchor = GridBagConstraints.WEST;
		gbc_txtName.gridwidth = 5;
		gbc_txtName.insets = new Insets(0, 0, 5, 0);
		gbc_txtName.gridx = 2;
		gbc_txtName.gridy = 1;
		myFiltersPane.add(txtName, gbc_txtName);
		
		txtName.setColumns(10);
		JLabel lblRegex = new JLabel("Regex:");
		GridBagConstraints gbc_lblRegex = new GridBagConstraints();
		gbc_lblRegex.anchor = GridBagConstraints.EAST;
		gbc_lblRegex.insets = new Insets(0, 0, 5, 5);
		gbc_lblRegex.gridx = 1;
		gbc_lblRegex.gridy = 2;
		myFiltersPane.add(lblRegex, gbc_lblRegex);
		
		txtRegex = new JTextField();
		txtRegex.setPreferredSize(new Dimension(6, 23));
		txtRegex.setColumns(10);
		GridBagConstraints gbc_txtRegex = new GridBagConstraints();
		gbc_txtRegex.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtRegex.anchor = GridBagConstraints.WEST;
		gbc_txtRegex.gridwidth = 5;
		gbc_txtRegex.insets = new Insets(0, 0, 5, 0);
		gbc_txtRegex.gridx = 2;
		gbc_txtRegex.gridy = 2;
		myFiltersPane.add(txtRegex, gbc_txtRegex);
		
		JLabel lblHeader = new JLabel("Header:");
		GridBagConstraints gbc_lblHeader = new GridBagConstraints();
		gbc_lblHeader.anchor = GridBagConstraints.EAST;
		gbc_lblHeader.insets = new Insets(0, 0, 5, 5);
		gbc_lblHeader.gridx = 1;
		gbc_lblHeader.gridy = 3;
		myFiltersPane.add(lblHeader, gbc_lblHeader);
		
		txtHeader = new JTextField();
		txtHeader.setMargin(new Insets(1, 1, 1, 1));
		txtHeader.setPreferredSize(new Dimension(6, 23));
		txtHeader.setColumns(10);
		GridBagConstraints gbc_txtHeader = new GridBagConstraints();
		gbc_txtHeader.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtHeader.anchor = GridBagConstraints.WEST;
		gbc_txtHeader.gridwidth = 5;
		gbc_txtHeader.insets = new Insets(0, 0, 5, 0);
		gbc_txtHeader.gridx = 2;
		gbc_txtHeader.gridy = 3;
		myFiltersPane.add(txtHeader, gbc_txtHeader);
		
		JLabel lblWhere = new JLabel("Search at:");
		GridBagConstraints gbc_lblWhere = new GridBagConstraints();
		gbc_lblWhere.anchor = GridBagConstraints.EAST;
		gbc_lblWhere.insets = new Insets(0, 0, 5, 5);
		gbc_lblWhere.gridx = 1;
		gbc_lblWhere.gridy = 4;
		myFiltersPane.add(lblWhere, gbc_lblWhere);
		
		JPanel chkbxPanel = new JPanel();
		GridBagConstraints gbc_chkbxPanel = new GridBagConstraints();
		gbc_chkbxPanel.anchor = GridBagConstraints.WEST;
		gbc_chkbxPanel.gridwidth = 5;
		gbc_chkbxPanel.insets = new Insets(0, 0, 5, 0);
		gbc_chkbxPanel.gridx = 2;
		gbc_chkbxPanel.gridy = 4;
		myFiltersPane.add(chkbxPanel, gbc_chkbxPanel);
		
		chckbxMain = new JCheckBox("main");
		chckbxSystem = new JCheckBox("System");
		chckbxKernel = new JCheckBox("Kernel");
		chkbxPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 1, 1));
		chkbxPanel.add(chckbxMain);
		chkbxPanel.add(chckbxSystem);
		chkbxPanel.add(chckbxKernel);
		chckbxRadio = new JCheckBox("Radio");
		chkbxPanel.add(chckbxRadio);
		chckbxBugreport = new JCheckBox("Bugreport");
		chkbxPanel.add(chckbxBugreport);
		chckbxRoutput = new JCheckBox("ReportOutput");
		chkbxPanel.add(chckbxRoutput);
		
		lblShared = new JLabel("Shared:");
		GridBagConstraints gbc_lblShared = new GridBagConstraints();
		gbc_lblShared.anchor = GridBagConstraints.EAST;
		gbc_lblShared.insets = new Insets(0, 0, 5, 5);
		gbc_lblShared.gridx = 1;
		gbc_lblShared.gridy = 5;
		myFiltersPane.add(lblShared, gbc_lblShared);
		
		chckbxShared = new JCheckBox("");
		GridBagConstraints gbc_chckbxShared = new GridBagConstraints();
		gbc_chckbxShared.anchor = GridBagConstraints.WEST;
		gbc_chckbxShared.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxShared.gridx = 2;
		gbc_chckbxShared.gridy = 5;
		myFiltersPane.add(chckbxShared, gbc_chckbxShared);
		
		lblPublic = new JLabel("Public:");
		GridBagConstraints gbc_lblPublic = new GridBagConstraints();
		gbc_lblPublic.anchor = GridBagConstraints.EAST;
		gbc_lblPublic.insets = new Insets(0, 0, 5, 5);
		gbc_lblPublic.gridx = 1;
		gbc_lblPublic.gridy = 6;
		myFiltersPane.add(lblPublic, gbc_lblPublic);
		
		chckbxPublic = new JCheckBox("");
		GridBagConstraints gbc_chckbxPublic = new GridBagConstraints();
		gbc_chckbxPublic.anchor = GridBagConstraints.WEST;
		gbc_chckbxPublic.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxPublic.gridx = 2;
		gbc_chckbxPublic.gridy = 6;
		myFiltersPane.add(chckbxPublic, gbc_chckbxPublic);
		
		label_4 = new JLabel("Active:");
		GridBagConstraints gbc_label_4 = new GridBagConstraints();
		gbc_label_4.anchor = GridBagConstraints.EAST;
		gbc_label_4.insets = new Insets(0, 0, 5, 5);
		gbc_label_4.gridx = 1;
		gbc_label_4.gridy = 7;
		myFiltersPane.add(label_4, gbc_label_4);
		
		chckbxActiveM = new JCheckBox("");
		GridBagConstraints gbc_chckbxActiveM = new GridBagConstraints();
		gbc_chckbxActiveM.anchor = GridBagConstraints.WEST;
		gbc_chckbxActiveM.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxActiveM.gridx = 2;
		gbc_chckbxActiveM.gridy = 7;
		myFiltersPane.add(chckbxActiveM, gbc_chckbxActiveM);
		
		chckbxNewCheckBox = new JCheckBox("New check box");
		GridBagConstraints gbc_chckbxNewCheckBox = new GridBagConstraints();
		gbc_chckbxNewCheckBox.insets = new Insets(0, 0, 0, 5);
		gbc_chckbxNewCheckBox.gridx = 2;
		gbc_chckbxNewCheckBox.gridy = 8;
		myFiltersPane.add(chckbxNewCheckBox, gbc_chckbxNewCheckBox);
		
		sharedPane = new JPanel();
		tabbedPane.addTab("Shared filters", null, sharedPane, null);
		GridBagLayout gbl_sharedPane = new GridBagLayout();
		gbl_sharedPane.columnWidths = new int[] {0, 0, 0, 0, 0, 0};
		gbl_sharedPane.rowHeights = new int[] {0, 0, 0, 0, 0, 0, 0};
		gbl_sharedPane.columnWeights = new double[] {0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_sharedPane.rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		sharedPane.setLayout(gbl_sharedPane);
		
		label = new JLabel("filters list:");
		label.setPreferredSize(new Dimension(70, 23));
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		label.setFont(new Font("Tahoma", Font.BOLD, 11));
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.insets = new Insets(5, 10, 5, 5);
		gbc_label.anchor = GridBagConstraints.EAST;
		gbc_label.gridx = 0;
		gbc_label.gridy = 0;
		sharedPane.add(label, gbc_label);
		
		comboBox_1 = new JComboBox<String>();
		comboBox_1.setPreferredSize(new Dimension(300, 23));
		comboBox_1.setMinimumSize(new Dimension(300, 23));
		comboBox_1.setMaximumRowCount(6);
		comboBox_1.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if(comboBox_1.getSelectedIndex() >= 0)
					setSharedFields();
			}
		});
		GridBagConstraints gbc_comboBox_1 = new GridBagConstraints();
		gbc_comboBox_1.insets = new Insets(5, 0, 5, 5);
		gbc_comboBox_1.gridx = 1;
		gbc_comboBox_1.gridy = 0;
		sharedPane.add(comboBox_1, gbc_comboBox_1);
		
		button = new JButton("Save");
		button.setPreferredSize(new Dimension(50, 25));
		button.setMargin(new Insets(0, 0, 0, 0));
		button.setForeground(new Color(0, 51, 153));
		button.setFont(new Font("Arial Black", Font.BOLD, 11));
		GridBagConstraints gbc_button = new GridBagConstraints();
		gbc_button.insets = new Insets(0, 0, 5, 5);
		gbc_button.gridx = 2;
		gbc_button.gridy = 0;
		sharedPane.add(button, gbc_button);
		
		label_1 = new JLabel("Filter Name:");
		GridBagConstraints gbc_label_1 = new GridBagConstraints();
		gbc_label_1.anchor = GridBagConstraints.EAST;
		gbc_label_1.insets = new Insets(0, 0, 5, 5);
		gbc_label_1.gridx = 0;
		gbc_label_1.gridy = 1;
		sharedPane.add(label_1, gbc_label_1);
		
		textNameS = new JTextField();
		textNameS.setPreferredSize(new Dimension(6, 23));
		textNameS.setColumns(10);
		GridBagConstraints gbc_textNameS = new GridBagConstraints();
		gbc_textNameS.anchor = GridBagConstraints.WEST;
		gbc_textNameS.gridwidth = 4;
		gbc_textNameS.insets = new Insets(0, 0, 5, 0);
		gbc_textNameS.fill = GridBagConstraints.HORIZONTAL;
		gbc_textNameS.gridx = 1;
		gbc_textNameS.gridy = 1;
		sharedPane.add(textNameS, gbc_textNameS);
		
		label_2 = new JLabel("Regex:");
		GridBagConstraints gbc_label_2 = new GridBagConstraints();
		gbc_label_2.anchor = GridBagConstraints.EAST;
		gbc_label_2.insets = new Insets(0, 0, 5, 5);
		gbc_label_2.gridx = 0;
		gbc_label_2.gridy = 2;
		sharedPane.add(label_2, gbc_label_2);
		
		textRegexS = new JTextField();
		textRegexS.setPreferredSize(new Dimension(6, 23));
		textRegexS.setColumns(10);
		GridBagConstraints gbc_textRegexS = new GridBagConstraints();
		gbc_textRegexS.anchor = GridBagConstraints.WEST;
		gbc_textRegexS.gridwidth = 4;
		gbc_textRegexS.insets = new Insets(0, 0, 5, 0);
		gbc_textRegexS.fill = GridBagConstraints.HORIZONTAL;
		gbc_textRegexS.gridx = 1;
		gbc_textRegexS.gridy = 2;
		sharedPane.add(textRegexS, gbc_textRegexS);
		
		label_3 = new JLabel("Header:");
		GridBagConstraints gbc_label_3 = new GridBagConstraints();
		gbc_label_3.anchor = GridBagConstraints.EAST;
		gbc_label_3.insets = new Insets(0, 0, 5, 5);
		gbc_label_3.gridx = 0;
		gbc_label_3.gridy = 3;
		sharedPane.add(label_3, gbc_label_3);
		
		textHeaderS = new JTextField();
		textHeaderS.setPreferredSize(new Dimension(6, 23));
		textHeaderS.setColumns(10);
		GridBagConstraints gbc_textHeaderS = new GridBagConstraints();
		gbc_textHeaderS.anchor = GridBagConstraints.WEST;
		gbc_textHeaderS.gridwidth = 4;
		gbc_textHeaderS.insets = new Insets(0, 0, 5, 0);
		gbc_textHeaderS.fill = GridBagConstraints.HORIZONTAL;
		gbc_textHeaderS.gridx = 1;
		gbc_textHeaderS.gridy = 3;
		sharedPane.add(textHeaderS, gbc_textHeaderS);
		
		label_5 = new JLabel("Search at:");
		GridBagConstraints gbc_label_5 = new GridBagConstraints();
		gbc_label_5.anchor = GridBagConstraints.EAST;
		gbc_label_5.insets = new Insets(0, 0, 5, 5);
		gbc_label_5.gridx = 0;
		gbc_label_5.gridy = 4;
		sharedPane.add(label_5, gbc_label_5);
		
		panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.gridwidth = 4;
		gbc_panel.anchor = GridBagConstraints.WEST;
		gbc_panel.insets = new Insets(0, 0, 5, 0);
		gbc_panel.gridx = 1;
		gbc_panel.gridy = 4;
		sharedPane.add(panel, gbc_panel);
		
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 1, 1));
		chcbxMainS = new JCheckBox("main");
		panel.add(chcbxMainS);
		chcbxSystemS = new JCheckBox("System");
		panel.add(chcbxSystemS);
		checkBox_4 = new JCheckBox("Kernel");
		panel.add(checkBox_4);
		checkBox_5 = new JCheckBox("Radio");
		panel.add(checkBox_5);
		checkBox_6 = new JCheckBox("Bugreport");
		panel.add(checkBox_6);
		checkBox_7 = new JCheckBox("ReportOutput");
		panel.add(checkBox_7);
		
		label_12 = new JLabel("Active:");
		GridBagConstraints gbc_label_12 = new GridBagConstraints();
		gbc_label_12.anchor = GridBagConstraints.EAST;
		gbc_label_12.insets = new Insets(0, 0, 0, 5);
		gbc_label_12.gridx = 0;
		gbc_label_12.gridy = 5;
		sharedPane.add(label_12, gbc_label_12);
		
		chckbxActiveS = new JCheckBox("");
		GridBagConstraints gbc_chckbxActiveS = new GridBagConstraints();
		gbc_chckbxActiveS.anchor = GridBagConstraints.WEST;
		gbc_chckbxActiveS.insets = new Insets(0, 0, 0, 5);
		gbc_chckbxActiveS.gridx = 1;
		gbc_chckbxActiveS.gridy = 5;
		sharedPane.add(chckbxActiveS, gbc_chckbxActiveS);
		
		activePane = new JPanel();
		tabbedPane.addTab("Active filters", null, activePane, null);
		GridBagLayout gbl_activePane = new GridBagLayout();
		gbl_activePane.columnWidths = new int[] {0, 0, 0, 0, 0, 0};
		gbl_activePane.rowHeights = new int[] {0, 0, 0, 0, 0, 0, 0};
		gbl_activePane.columnWeights = new double[] {0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_activePane.rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		activePane.setLayout(gbl_activePane);
		
		label_6 = new JLabel("filters list:");
		label_6.setPreferredSize(new Dimension(70, 23));
		label_6.setHorizontalAlignment(SwingConstants.RIGHT);
		label_6.setFont(new Font("Tahoma", Font.BOLD, 11));
		GridBagConstraints gbc_label_6 = new GridBagConstraints();
		gbc_label_6.anchor = GridBagConstraints.EAST;
		gbc_label_6.insets = new Insets(5, 10, 5, 5);
		gbc_label_6.gridx = 0;
		gbc_label_6.gridy = 0;
		activePane.add(label_6, gbc_label_6);
		
		comboBox_2 = new JComboBox<String>();
		comboBox_2.setPreferredSize(new Dimension(300, 23));
		comboBox_2.setMinimumSize(new Dimension(300, 23));
		comboBox_2.setMaximumRowCount(6);
		comboBox_2.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if(comboBox_2.getSelectedIndex() >= 0)
					setActiveFields();
			}
		});
		GridBagConstraints gbc_comboBox_2 = new GridBagConstraints();
		gbc_comboBox_2.anchor = GridBagConstraints.WEST;
		gbc_comboBox_2.insets = new Insets(5, 0, 5, 5);
		gbc_comboBox_2.gridx = 1;
		gbc_comboBox_2.gridy = 0;
		activePane.add(comboBox_2, gbc_comboBox_2);
		
		label_7 = new JLabel("Filter Name:");
		GridBagConstraints gbc_label_7 = new GridBagConstraints();
		gbc_label_7.anchor = GridBagConstraints.EAST;
		gbc_label_7.insets = new Insets(0, 0, 5, 5);
		gbc_label_7.gridx = 0;
		gbc_label_7.gridy = 1;
		activePane.add(label_7, gbc_label_7);
		
		textField_3 = new JTextField();
		textField_3.setPreferredSize(new Dimension(6, 23));
		textField_3.setColumns(10);
		GridBagConstraints gbc_textField_3 = new GridBagConstraints();
		gbc_textField_3.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_3.anchor = GridBagConstraints.WEST;
		gbc_textField_3.gridwidth = 4;
		gbc_textField_3.insets = new Insets(0, 0, 5, 5);
		gbc_textField_3.gridx = 1;
		gbc_textField_3.gridy = 1;
		activePane.add(textField_3, gbc_textField_3);
		
		label_8 = new JLabel("Regex:");
		GridBagConstraints gbc_label_8 = new GridBagConstraints();
		gbc_label_8.anchor = GridBagConstraints.EAST;
		gbc_label_8.insets = new Insets(0, 0, 5, 5);
		gbc_label_8.gridx = 0;
		gbc_label_8.gridy = 2;
		activePane.add(label_8, gbc_label_8);
		
		textField_4 = new JTextField();
		textField_4.setPreferredSize(new Dimension(6, 23));
		textField_4.setColumns(10);
		GridBagConstraints gbc_textField_4 = new GridBagConstraints();
		gbc_textField_4.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_4.anchor = GridBagConstraints.WEST;
		gbc_textField_4.gridwidth = 4;
		gbc_textField_4.insets = new Insets(0, 0, 5, 5);
		gbc_textField_4.gridx = 1;
		gbc_textField_4.gridy = 2;
		activePane.add(textField_4, gbc_textField_4);
		
		label_9 = new JLabel("Header:");
		GridBagConstraints gbc_label_9 = new GridBagConstraints();
		gbc_label_9.anchor = GridBagConstraints.EAST;
		gbc_label_9.insets = new Insets(0, 0, 5, 5);
		gbc_label_9.gridx = 0;
		gbc_label_9.gridy = 3;
		activePane.add(label_9, gbc_label_9);
		
		textField_5 = new JTextField();
		textField_5.setPreferredSize(new Dimension(6, 23));
		textField_5.setColumns(10);
		GridBagConstraints gbc_textField_5 = new GridBagConstraints();
		gbc_textField_5.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_5.anchor = GridBagConstraints.WEST;
		gbc_textField_5.gridwidth = 4;
		gbc_textField_5.insets = new Insets(0, 0, 5, 5);
		gbc_textField_5.gridx = 1;
		gbc_textField_5.gridy = 3;
		activePane.add(textField_5, gbc_textField_5);
		
		label_10 = new JLabel("Search at:");
		GridBagConstraints gbc_label_10 = new GridBagConstraints();
		gbc_label_10.anchor = GridBagConstraints.EAST;
		gbc_label_10.insets = new Insets(0, 0, 5, 5);
		gbc_label_10.gridx = 0;
		gbc_label_10.gridy = 4;
		activePane.add(label_10, gbc_label_10);
		
		panel_2 = new JPanel();
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.insets = new Insets(0, 0, 5, 0);
		gbc_panel_2.anchor = GridBagConstraints.WEST;
		gbc_panel_2.gridwidth = 4;
		gbc_panel_2.gridx = 1;
		gbc_panel_2.gridy = 4;
		activePane.add(panel_2, gbc_panel_2);
		
		panel_2.setLayout(new FlowLayout(FlowLayout.CENTER, 1, 1));
		checkBox_8 = new JCheckBox("main");
		panel_2.add(checkBox_8);
		checkBox_9 = new JCheckBox("System");
		panel_2.add(checkBox_9);
		checkBox_10 = new JCheckBox("Kernel");
		panel_2.add(checkBox_10);
		checkBox_11 = new JCheckBox("Radio");
		panel_2.add(checkBox_11);
		checkBox_12 = new JCheckBox("Bugreport");
		panel_2.add(checkBox_12);
		checkBox_13 = new JCheckBox("ReportOutput");
		panel_2.add(checkBox_13);
		
		label_11 = new JLabel("Active:");
		GridBagConstraints gbc_label_11 = new GridBagConstraints();
		gbc_label_11.anchor = GridBagConstraints.EAST;
		gbc_label_11.insets = new Insets(0, 0, 0, 5);
		gbc_label_11.gridx = 0;
		gbc_label_11.gridy = 5;
		activePane.add(label_11, gbc_label_11);
		
		chcbxActiveA = new JCheckBox("");
		GridBagConstraints gbc_chcbxActiveA = new GridBagConstraints();
		gbc_chcbxActiveA.anchor = GridBagConstraints.WEST;
		gbc_chcbxActiveA.insets = new Insets(0, 0, 0, 5);
		gbc_chcbxActiveA.gridx = 1;
		gbc_chcbxActiveA.gridy = 5;
		activePane.add(chcbxActiveA, gbc_chcbxActiveA);
		
		btnDone = new JButton("Save and Exit");
		btnDone.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				saveFilters();
				setVisible(false);
			}
		});
		btnDone.setMinimumSize(new Dimension(90, 23));
		GridBagConstraints gbc_btnDone = new GridBagConstraints();
		gbc_btnDone.insets = new Insets(5, 5, 5, 5);
		gbc_btnDone.gridx = 0;
		gbc_btnDone.gridy = 1;
		getContentPane().add(btnDone, gbc_btnDone);
		
		loadFilters();
	}
	
	/**
	 * 
	 */
	public void loadFilters()
	{
		comboBox.removeAllItems();
		comboBox_1.removeAllItems();
		comboBox_2.removeAllItems();
		
		// Load MyFilters
		for (CustomFilterItem filter : SharedObjs.getUserFiltersList())
		{
			comboBox.insertItemAt(filter.getName().replace("_", " "), comboBox.getItemCount());
		}
		
		// Load SharedFilters
		for (CustomFilterItem filter : SharedObjs.getSharedFiltersList())
		{
			comboBox_1.insertItemAt(filter.getName().replace("_", " "), comboBox_1.getItemCount());
		}
		
		// Load ActiveFilters
		for (CustomFilterItem filter : SharedObjs.getActiveFiltersList())
		{
			comboBox_2.insertItemAt(filter.getName().replace("_", " "), comboBox_2.getItemCount());
		}
		
		tabbedPane.updateUI();
	}
	
	public void saveFilters()
	{
		for (CustomFilterItem filter : filtersList)
		{
			XmlMngr.setMyFiltersValueOf(filter);
		}
	}
	
	//
	public void open()
	{
		clearfields();
		loadFilters();
		setLocationRelativeTo(SharedObjs.satFrame);
		setVisible(true);
	}
	
	private void clearfields()
	{
		comboBox.setSelectedIndex(-1);
		comboBox_1.setSelectedIndex(-1);
		comboBox_2.setSelectedIndex(-1);
		
		txtName.setText("");
		txtRegex.setText("");
		txtHeader.setText("");
		textNameS.setText("");
		textRegexS.setText("");
		textHeaderS.setText("");
		textField_3.setText("");
		textField_4.setText("");
		textField_5.setText("");
		
		button.setEnabled(false);
		
		chckbxMain.setSelected(false);
		chckbxSystem.setSelected(false);
		chckbxKernel.setSelected(false);
		chckbxRadio.setSelected(false);
		chckbxBugreport.setSelected(false);
		chckbxRoutput.setSelected(false);
		chckbxShared.setSelected(false);
		chckbxPublic.setSelected(false);
		chckbxActiveM.setSelected(false);
		
		chcbxMainS.setSelected(false);
		chcbxSystemS.setSelected(false);
		checkBox_4.setSelected(false);
		checkBox_5.setSelected(false);
		checkBox_6.setSelected(false);
		checkBox_7.setSelected(false);
		chckbxActiveS.setSelected(false);
		
		checkBox_8.setSelected(false);
		checkBox_9.setSelected(false);
		checkBox_10.setSelected(false);
		checkBox_11.setSelected(false);
		checkBox_12.setSelected(false);
		checkBox_13.setSelected(false);
		chcbxActiveA.setSelected(false);
	}
	
	public void setMyFiltersFields()
	{
		String aux = (String) comboBox.getSelectedItem();
		
		for (CustomFilterItem item : SharedObjs.getUserFiltersList())
		{
			if (aux != null && aux.equals(item.getName()))
			{
				txtName.setText(item.getName());
				txtRegex.setText(item.getRegex());
				txtHeader.setText(item.getHeader());
				chckbxMain.setSelected(item.isMain());
				chckbxSystem.setSelected(item.isSystem());
				chckbxKernel.setSelected(item.isKernel());
				chckbxRadio.setSelected(item.isRadio());
				chckbxBugreport.setSelected(item.isBugreport());
				chckbxRoutput.setSelected(item.isRoutput());
				chckbxShared.setSelected(item.isShared());
				chckbxPublic.setSelected(item.isEditable());
				chckbxActiveM.setSelected(item.isActive());
				break;
			}
		}
	}
	
	public void setSharedFields()
	{
		String aux = (String) comboBox_1.getSelectedItem();
		
		for (CustomFilterItem filter : SharedObjs.getSharedFiltersList())
		{
			if (aux != null && aux.equals(filter.getName()))
			{
				textNameS.setText(filter.getName());
				textRegexS.setText(filter.getRegex());
				textHeaderS.setText(filter.getHeader());
				
				chcbxMainS.setSelected(filter.isMain());
				chcbxSystemS.setSelected(filter.isSystem());
				checkBox_4.setSelected(filter.isKernel());
				checkBox_5.setSelected(filter.isRadio());
				checkBox_6.setSelected(filter.isBugreport());
				checkBox_7.setSelected(filter.isRoutput());
				chckbxActiveS.setSelected(filter.isActive());
			}
		}
	}
	
	public void setActiveFields()
	{
		String aux = (String) comboBox_2.getSelectedItem();
		
		for (CustomFilterItem filter : SharedObjs.getActiveFiltersList())
		{
			if (aux != null && aux.equals(filter.getName()))
			{
				textField_3.setText(filter.getName());
				textField_4.setText(filter.getRegex());
				textField_5.setText(filter.getHeader());
				checkBox_8.setSelected(filter.isMain());
				checkBox_9.setSelected(filter.isSystem());
				checkBox_10.setSelected(filter.isKernel());
				checkBox_11.setSelected(filter.isRadio());
				checkBox_12.setSelected(filter.isBugreport());
				checkBox_13.setSelected(filter.isRoutput());
				chcbxActiveA.setSelected(filter.isActive());
			}
		}
	}
	
	// Getters and Setters
	public JCheckBox getChckbxRadio()
	{
		return chckbxRadio;
	}
	
	public JCheckBox getChckbxSystem()
	{
		return chckbxSystem;
	}
	
	public JTextField getTxtHeader()
	{
		return txtHeader;
	}
	
	public JCheckBox getChckbxBugreport()
	{
		return chckbxBugreport;
	}
	
	public JCheckBox getChckbxMain()
	{
		return chckbxMain;
	}
	
	public JButton getBtnAdd()
	{
		return btnAdd;
	}
	
	public JTextField getTxtRegex()
	{
		return txtRegex;
	}
	
	public JComboBox<String> getComboBox()
	{
		return comboBox;
	}
	
	public JTextField getTxtName()
	{
		return txtName;
	}
	
	public JButton getBtnDel()
	{
		return btnDel;
	}
	
	public JCheckBox getChckbxKernel()
	{
		return chckbxKernel;
	}
	
	protected JCheckBox getChcbxPublic()
	{
		return chckbxPublic;
	}
	
	protected JCheckBox getChcbxShared()
	{
		return chckbxShared;
	}
}
