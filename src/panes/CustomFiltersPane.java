package panes;


import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JCheckBox;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FlowLayout;
import java.awt.Color;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import core.Icons;
import core.Logger;
import core.SharedObjs;

import objects.CustomFilterItem;
import objects.CustomFiltersList;;


@SuppressWarnings("serial")
public class CustomFiltersPane extends JDialog
{
	private JTextField		  txtName;
	private JTextField		  txtRegex;
	private JTextField		  txtHeader;
	private JTextField		  textField;
	private JTextField		  textField_1;
	private JTextField		  textField_2;
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
	private JCheckBox		  checkBox_2;
	private JCheckBox		  checkBox_3;
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
	private JCheckBox		  checkBox_14;
	private JButton			  btnDone;
	private JButton			  btnAdd;
	private JButton			  btnDel;
	private JButton			  btnNew;
	private JButton			  button;
	private JButton			  button_1;
	private JButton			  button_2;
	private JButton			  button_3;
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
	
	// Create the dialog.
	public CustomFiltersPane()
	{
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setVisible(false);
		setTitle("filters Manager");
		setBounds(100, 100, 965, 307);
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {0, 0};
		gridBagLayout.rowHeights = new int[] {0, 0};
		gridBagLayout.columnWeights = new double[] {1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[] {0.0, 0.0};
		getContentPane().setLayout(gridBagLayout);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
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
		gbl_myFiltersPane.columnWidths = new int[] {0, 0, 0, 0, 0, 0};
		gbl_myFiltersPane.rowHeights = new int[] {0, 0, 0, 0, 0, 0, 0, 0};
		gbl_myFiltersPane.columnWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 1.0};
		gbl_myFiltersPane.rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		myFiltersPane.setLayout(gbl_myFiltersPane);
		
		JLabel lblFiltersList = new JLabel("filters list:");
		lblFiltersList.setHorizontalAlignment(SwingConstants.RIGHT);
		lblFiltersList.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblFiltersList.setPreferredSize(new Dimension(70, 23));
		GridBagConstraints gbc_lblFiltersList = new GridBagConstraints();
		gbc_lblFiltersList.anchor = GridBagConstraints.EAST;
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
				filtersList = SharedObjs.getCustomFiltersList();
				String aux = (String) comboBox.getSelectedItem();
				for (CustomFilterItem item : filtersList)
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
					}
				}
			}
		});
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.insets = new Insets(5, 0, 5, 5);
		gbc_comboBox.anchor = GridBagConstraints.WEST;
		gbc_comboBox.gridx = 1;
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
					filtersList = SharedObjs.getCustomFiltersList();
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
		gbc_btnDel.gridx = 2;
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
																							  : SharedObjs.crsManagerPane.getTextUsername()
																														 .getText(),
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
																	chckbxPublic.isSelected());
					filtersList = SharedObjs.getCustomFiltersList();
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
						SharedObjs.getCustomFiltersList().add(auxItem);
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
		gbc_btnAdd.gridx = 3;
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
		gbc_btnNew.gridx = 4;
		gbc_btnNew.gridy = 0;
		myFiltersPane.add(btnNew, gbc_btnNew);
		
		JLabel lblFilterName = new JLabel("Filter Name:");
		GridBagConstraints gbc_lblFilterName = new GridBagConstraints();
		gbc_lblFilterName.anchor = GridBagConstraints.EAST;
		gbc_lblFilterName.insets = new Insets(0, 0, 5, 5);
		gbc_lblFilterName.gridx = 0;
		gbc_lblFilterName.gridy = 1;
		myFiltersPane.add(lblFilterName, gbc_lblFilterName);
		
		txtName = new JTextField();
		txtName.setPreferredSize(new Dimension(6, 23));
		GridBagConstraints gbc_txtName = new GridBagConstraints();
		gbc_txtName.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtName.anchor = GridBagConstraints.WEST;
		gbc_txtName.gridwidth = 5;
		gbc_txtName.insets = new Insets(0, 0, 5, 0);
		gbc_txtName.gridx = 1;
		gbc_txtName.gridy = 1;
		myFiltersPane.add(txtName, gbc_txtName);
		
		txtName.setColumns(10);
		JLabel lblRegex = new JLabel("Regex:");
		GridBagConstraints gbc_lblRegex = new GridBagConstraints();
		gbc_lblRegex.anchor = GridBagConstraints.EAST;
		gbc_lblRegex.insets = new Insets(0, 0, 5, 5);
		gbc_lblRegex.gridx = 0;
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
		gbc_txtRegex.gridx = 1;
		gbc_txtRegex.gridy = 2;
		myFiltersPane.add(txtRegex, gbc_txtRegex);
		
		JLabel lblHeader = new JLabel("Header:");
		GridBagConstraints gbc_lblHeader = new GridBagConstraints();
		gbc_lblHeader.anchor = GridBagConstraints.EAST;
		gbc_lblHeader.insets = new Insets(0, 0, 5, 5);
		gbc_lblHeader.gridx = 0;
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
		gbc_txtHeader.gridx = 1;
		gbc_txtHeader.gridy = 3;
		myFiltersPane.add(txtHeader, gbc_txtHeader);
		
		JLabel lblWhere = new JLabel("Search at:");
		GridBagConstraints gbc_lblWhere = new GridBagConstraints();
		gbc_lblWhere.anchor = GridBagConstraints.EAST;
		gbc_lblWhere.insets = new Insets(0, 0, 5, 5);
		gbc_lblWhere.gridx = 0;
		gbc_lblWhere.gridy = 4;
		myFiltersPane.add(lblWhere, gbc_lblWhere);
		
		JPanel chkbxPanel = new JPanel();
		GridBagConstraints gbc_chkbxPanel = new GridBagConstraints();
		gbc_chkbxPanel.anchor = GridBagConstraints.WEST;
		gbc_chkbxPanel.gridwidth = 5;
		gbc_chkbxPanel.insets = new Insets(0, 0, 5, 0);
		gbc_chkbxPanel.gridx = 1;
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
		gbc_lblShared.gridx = 0;
		gbc_lblShared.gridy = 5;
		myFiltersPane.add(lblShared, gbc_lblShared);
		
		chckbxShared = new JCheckBox("");
		GridBagConstraints gbc_chckbxShared = new GridBagConstraints();
		gbc_chckbxShared.anchor = GridBagConstraints.WEST;
		gbc_chckbxShared.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxShared.gridx = 1;
		gbc_chckbxShared.gridy = 5;
		myFiltersPane.add(chckbxShared, gbc_chckbxShared);
		
		lblPublic = new JLabel("Public:");
		GridBagConstraints gbc_lblPublic = new GridBagConstraints();
		gbc_lblPublic.anchor = GridBagConstraints.EAST;
		gbc_lblPublic.insets = new Insets(0, 0, 0, 5);
		gbc_lblPublic.gridx = 0;
		gbc_lblPublic.gridy = 6;
		myFiltersPane.add(lblPublic, gbc_lblPublic);
		
		chckbxPublic = new JCheckBox("");
		GridBagConstraints gbc_chckbxPublic = new GridBagConstraints();
		gbc_chckbxPublic.anchor = GridBagConstraints.WEST;
		gbc_chckbxPublic.insets = new Insets(0, 0, 0, 5);
		gbc_chckbxPublic.gridx = 1;
		gbc_chckbxPublic.gridy = 6;
		myFiltersPane.add(chckbxPublic, gbc_chckbxPublic);
		
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
		GridBagConstraints gbc_comboBox_1 = new GridBagConstraints();
		gbc_comboBox_1.insets = new Insets(5, 0, 5, 5);
		gbc_comboBox_1.gridx = 1;
		gbc_comboBox_1.gridy = 0;
		sharedPane.add(comboBox_1, gbc_comboBox_1);
		
		button = new JButton("");
		button.setIcon(Icons.add);
		button.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
			}
		});
		button.setMinimumSize(new Dimension(25, 25));
		button.setPreferredSize(new Dimension(25, 25));
		button.setMargin(new Insets(2, 2, 2, 2));
		GridBagConstraints gbc_button = new GridBagConstraints();
		gbc_button.insets = new Insets(5, 0, 5, 5);
		gbc_button.gridx = 2;
		gbc_button.gridy = 0;
		sharedPane.add(button, gbc_button);
		
		button_1 = new JButton("");
		button_1.setIcon(Icons.delete);
		button_1.setMinimumSize(new Dimension(25, 25));
		button_1.setPreferredSize(new Dimension(25, 25));
		button_1.setMargin(new Insets(2, 2, 2, 2));
		GridBagConstraints gbc_button_1 = new GridBagConstraints();
		gbc_button_1.insets = new Insets(5, 0, 5, 5);
		gbc_button_1.gridx = 3;
		gbc_button_1.gridy = 0;
		sharedPane.add(button_1, gbc_button_1);
		
		label_1 = new JLabel("Filter Name:");
		GridBagConstraints gbc_label_1 = new GridBagConstraints();
		gbc_label_1.anchor = GridBagConstraints.EAST;
		gbc_label_1.insets = new Insets(0, 0, 5, 5);
		gbc_label_1.gridx = 0;
		gbc_label_1.gridy = 1;
		sharedPane.add(label_1, gbc_label_1);
		
		textField = new JTextField();
		textField.setPreferredSize(new Dimension(6, 23));
		textField.setColumns(10);
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.anchor = GridBagConstraints.WEST;
		gbc_textField.gridwidth = 4;
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 1;
		sharedPane.add(textField, gbc_textField);
		
		label_2 = new JLabel("Regex:");
		GridBagConstraints gbc_label_2 = new GridBagConstraints();
		gbc_label_2.anchor = GridBagConstraints.EAST;
		gbc_label_2.insets = new Insets(0, 0, 5, 5);
		gbc_label_2.gridx = 0;
		gbc_label_2.gridy = 2;
		sharedPane.add(label_2, gbc_label_2);
		
		textField_1 = new JTextField();
		textField_1.setPreferredSize(new Dimension(6, 23));
		textField_1.setColumns(10);
		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
		gbc_textField_1.anchor = GridBagConstraints.WEST;
		gbc_textField_1.gridwidth = 4;
		gbc_textField_1.insets = new Insets(0, 0, 5, 5);
		gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_1.gridx = 1;
		gbc_textField_1.gridy = 2;
		sharedPane.add(textField_1, gbc_textField_1);
		
		label_3 = new JLabel("Header:");
		GridBagConstraints gbc_label_3 = new GridBagConstraints();
		gbc_label_3.anchor = GridBagConstraints.EAST;
		gbc_label_3.insets = new Insets(0, 0, 5, 5);
		gbc_label_3.gridx = 0;
		gbc_label_3.gridy = 3;
		sharedPane.add(label_3, gbc_label_3);
		
		textField_2 = new JTextField();
		textField_2.setPreferredSize(new Dimension(6, 23));
		textField_2.setColumns(10);
		GridBagConstraints gbc_textField_2 = new GridBagConstraints();
		gbc_textField_2.anchor = GridBagConstraints.WEST;
		gbc_textField_2.gridwidth = 4;
		gbc_textField_2.insets = new Insets(0, 0, 5, 5);
		gbc_textField_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_2.gridx = 1;
		gbc_textField_2.gridy = 3;
		sharedPane.add(textField_2, gbc_textField_2);
		
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
		gbc_panel.insets = new Insets(0, 0, 5, 5);
		gbc_panel.gridx = 1;
		gbc_panel.gridy = 4;
		sharedPane.add(panel, gbc_panel);
		
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 1, 1));
		checkBox_2 = new JCheckBox("main");
		panel.add(checkBox_2);
		checkBox_3 = new JCheckBox("System");
		panel.add(checkBox_3);
		checkBox_4 = new JCheckBox("Kernel");
		panel.add(checkBox_4);
		checkBox_5 = new JCheckBox("Radio");
		panel.add(checkBox_5);
		checkBox_6 = new JCheckBox("Bugreport");
		panel.add(checkBox_6);
		checkBox_7 = new JCheckBox("ReportOutput");
		panel.add(checkBox_7);
		
		activePane = new JPanel();
		tabbedPane.addTab("Active filters", null, activePane, null);
		GridBagLayout gbl_activePane = new GridBagLayout();
		gbl_activePane.columnWidths = new int[] {0, 0, 0, 0, 0, 0};
		gbl_activePane.rowHeights = new int[] {0, 0, 0, 0, 0, 0, 0};
		gbl_activePane.columnWeights = new double[] {0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_activePane.rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
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
		GridBagConstraints gbc_comboBox_2 = new GridBagConstraints();
		gbc_comboBox_2.anchor = GridBagConstraints.WEST;
		gbc_comboBox_2.insets = new Insets(5, 0, 5, 5);
		gbc_comboBox_2.gridx = 1;
		gbc_comboBox_2.gridy = 0;
		activePane.add(comboBox_2, gbc_comboBox_2);
		
		button_2 = new JButton("");
		button_2.setIcon(Icons.add);
		button_2.setPreferredSize(new Dimension(25, 25));
		button_2.setMinimumSize(new Dimension(25, 25));
		button_2.setMargin(new Insets(2, 2, 2, 2));
		GridBagConstraints gbc_button_2 = new GridBagConstraints();
		gbc_button_2.insets = new Insets(5, 0, 5, 5);
		gbc_button_2.gridx = 2;
		gbc_button_2.gridy = 0;
		activePane.add(button_2, gbc_button_2);
		
		button_3 = new JButton("");
		button_3.setIcon(Icons.delete);
		button_3.setPreferredSize(new Dimension(25, 25));
		button_3.setMinimumSize(new Dimension(25, 25));
		button_3.setMargin(new Insets(2, 2, 2, 2));
		GridBagConstraints gbc_button_3 = new GridBagConstraints();
		gbc_button_3.insets = new Insets(5, 0, 5, 5);
		gbc_button_3.gridx = 3;
		gbc_button_3.gridy = 0;
		activePane.add(button_3, gbc_button_3);
		
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
		
		checkBox_14 = new JCheckBox("");
		GridBagConstraints gbc_checkBox_14 = new GridBagConstraints();
		gbc_checkBox_14.anchor = GridBagConstraints.WEST;
		gbc_checkBox_14.insets = new Insets(0, 0, 0, 5);
		gbc_checkBox_14.gridx = 1;
		gbc_checkBox_14.gridy = 5;
		activePane.add(checkBox_14, gbc_checkBox_14);
		
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
	}
	
	/**
	 * 
	 */
	public void loadFilters()
	{
		try
		{
			File xmlFile = new File("Data/cfgs/user_cfg.xml");
			filtersList = SharedObjs.getCustomFiltersList();
			SAXBuilder builder = new SAXBuilder();
			Document document = (Document) builder.build(xmlFile);
			Element filtersNode = document.getRootElement().getChild("custom_filters");
			List<Element> filtersElements = filtersNode.getChildren();
			comboBox.removeAllItems();
			filtersList.clear();
			
			if (!filtersElements.isEmpty())
			{
				for (Element filter : filtersElements)
				{
					filtersList.add(new CustomFilterItem(filter.getChildText("owner"),
														 filter.getName().replace("_", " "),
														 filter.getChildText("regex"),
														 filter.getChildText("header"),
														 Boolean.parseBoolean(filter.getChildText("main")),
														 Boolean.parseBoolean(filter.getChildText("system")),
														 Boolean.parseBoolean(filter.getChildText("kernel")),
														 Boolean.parseBoolean(filter.getChildText("radio")),
														 Boolean.parseBoolean(filter.getChildText("bugreport")),
														 Boolean.parseBoolean(filter.getChildText("routput")),
														 Boolean.parseBoolean(filter.getChildText("shared")),
														 Boolean.parseBoolean(filter.getChildText("editable"))));
					comboBox.insertItemAt(filter.getName().replace("_", " "), comboBox.getItemCount());
				}
			}
		}
		catch (IOException | JDOMException e)
		{
			e.printStackTrace();
			Logger.log(Logger.TAG_CUSTOMFILTER, e.getMessage());
		}
	}
	
	public void saveFilters()
	{
		try
		{
			File xmlFile = new File("Data/cfgs/user_cfg.xml");
			filtersList = SharedObjs.getCustomFiltersList();
			SAXBuilder builder = new SAXBuilder();
			Document document = (Document) builder.build(xmlFile);
			Element filtersNode = document.getRootElement().getChild("custom_filters");
			filtersNode.removeContent();
			
			for (CustomFilterItem e : filtersList)
			{
				Element filter = new Element("" + e.getName().replace(" ", "_"));
				filter.addContent(new Element("regex").setText(e.getRegex()));
				filter.addContent(new Element("header").setText(e.getHeader()));
				filter.addContent(new Element("owner").setText(e.getOwner()));
				filter.addContent(new Element("main").setText("" + e.isMain()));
				filter.addContent(new Element("system").setText("" + e.isSystem()));
				filter.addContent(new Element("kernel").setText("" + e.isKernel()));
				filter.addContent(new Element("radio").setText("" + e.isRadio()));
				filter.addContent(new Element("bugreport").setText("" + e.isBugreport()));
				filter.addContent(new Element("routput").setText("" + e.isRoutput()));
				filter.addContent(new Element("shared").setText("" + e.isShared()));
				filter.addContent(new Element("editable").setText("" + e.isEditable()));
				filtersNode.addContent(filter);
			}
			
			XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
			// For output generated xml to console for debugging
			// xmlOutputter.output(doc, System.out);
			
			xmlOutputter.output(document, new FileOutputStream(xmlFile));
			
			Logger.log(Logger.TAG_CUSTOMFILTER, "Options Saved");
			loadFilters();
		}
		catch (JDOMException | IOException e)
		{
			e.printStackTrace();
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
		txtName.setText("");
		txtRegex.setText("");
		txtHeader.setText("");
		chckbxMain.setSelected(false);
		chckbxSystem.setSelected(false);
		chckbxKernel.setSelected(false);
		chckbxRadio.setSelected(false);
		chckbxBugreport.setSelected(false);
		chckbxRoutput.setSelected(false);
		chckbxShared.setSelected(false);
		chckbxPublic.setSelected(false);
		comboBox.setSelectedIndex(-1);
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
