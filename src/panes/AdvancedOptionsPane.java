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
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import org.jdom2.JDOMException;

import core.Icons;
import core.Logger;
import core.SharedObjs;
import core.XmlMngr;


@SuppressWarnings("serial")
public class AdvancedOptionsPane extends JFrame
{
	private JPanel                  contentPane;
	private JLabel                  lblTitle;
	private JPanel                  panel;
	private JLabel                  lblProduct;
	private JComboBox<String>       cbxDiagProd;
	private JLabel                  lblConfigureDiagwsDup;
	private JTextField              textDiag;
	private JSeparator              separator;
	private JLabel                  lblBatCap;
	private JLabel                  lblProduct2;
	private JComboBox<String>       cbxBatCap;
	private JTextField              textBatCap;
	private JSeparator              separator_1;
	private JPanel                  panel_1;
	private JButton                 btnOk;
	private HashMap<String, String> dupMap;
	private HashMap<String, String> bat_capMap;
	private JButton                 btnCancel;
	private JButton                 button;
	private JButton                 button_1;
	
	/**
	 * Create the frame.
	 */
	public AdvancedOptionsPane()
	{
		setTitle("Advanced options");
		setMinimumSize(new Dimension(400, 350));
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 444, 356);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		setLocationRelativeTo(SharedObjs.satFrame);
		
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] {0, 0};
		gbl_contentPane.rowHeights = new int[] {0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[] {1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[] {0.0, 1.0, 1.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		lblTitle = new JLabel("Advanced Options:");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setFont(new Font("Tahoma", Font.BOLD, 24));
		GridBagConstraints gbc_lblTitle = new GridBagConstraints();
		gbc_lblTitle.insets = new Insets(0, 0, 10, 0);
		gbc_lblTitle.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblTitle.gridx = 0;
		gbc_lblTitle.gridy = 0;
		contentPane.add(lblTitle, gbc_lblTitle);
		
		panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(5, 5, 5, 10);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 1;
		contentPane.add(panel, gbc_panel);
		
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] {0, 150, 0, 0, 0};
		gbl_panel.rowHeights = new int[] {0, 0, 0, 0, 0, 0, 0};
		gbl_panel.columnWeights = new double[] {0.0, 4.0, 3.0, 0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		lblConfigureDiagwsDup = new JLabel("Configure DIAG_WS Dup CR");
		lblConfigureDiagwsDup.setFont(new Font("Tahoma", Font.BOLD, 14));
		
		GridBagConstraints gbc_lblConfigureDiagwsDup = new GridBagConstraints();
		gbc_lblConfigureDiagwsDup.anchor = GridBagConstraints.WEST;
		gbc_lblConfigureDiagwsDup.gridwidth = 3;
		gbc_lblConfigureDiagwsDup.insets = new Insets(0, 0, 5, 5);
		gbc_lblConfigureDiagwsDup.gridx = 0;
		gbc_lblConfigureDiagwsDup.gridy = 0;
		panel.add(lblConfigureDiagwsDup, gbc_lblConfigureDiagwsDup);
		
		lblProduct = new JLabel("Product:");
		GridBagConstraints gbc_lblProduct = new GridBagConstraints();
		gbc_lblProduct.insets = new Insets(0, 10, 5, 5);
		gbc_lblProduct.gridx = 0;
		gbc_lblProduct.gridy = 1;
		panel.add(lblProduct, gbc_lblProduct);
		
		cbxDiagProd = new JComboBox<String>();
		cbxDiagProd.setMinimumSize(new Dimension(150, 20));
		cbxDiagProd.setPreferredSize(new Dimension(150, 20));
		GridBagConstraints gbc_cbxDiagProd = new GridBagConstraints();
		gbc_cbxDiagProd.fill = GridBagConstraints.HORIZONTAL;
		gbc_cbxDiagProd.insets = new Insets(0, 0, 5, 5);
		gbc_cbxDiagProd.gridx = 1;
		gbc_cbxDiagProd.gridy = 1;
		panel.add(cbxDiagProd, gbc_cbxDiagProd);
		
		textDiag = new JTextField();
		textDiag.setPreferredSize(new Dimension(110, 20));
		textDiag.setMinimumSize(new Dimension(110, 20));
		GridBagConstraints gbc_textDiag = new GridBagConstraints();
		gbc_textDiag.fill = GridBagConstraints.HORIZONTAL;
		gbc_textDiag.insets = new Insets(0, 0, 5, 5);
		gbc_textDiag.gridx = 2;
		gbc_textDiag.gridy = 1;
		panel.add(textDiag, gbc_textDiag);
		textDiag.setColumns(10);
		
		button = new JButton("");
		button.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				String pName = JOptionPane.showInputDialog("Type the product name");
				String dDup = JOptionPane.showInputDialog("Type the CR to dup for");
				dupMap.put(pName, dDup);
				setData();
				getData();
			}
		});
		button.setIcon(Icons.add);
		button.setPreferredSize(new Dimension(30, 25));
		button.setMinimumSize(new Dimension(30, 30));
		button.setMaximumSize(new Dimension(30, 30));
		GridBagConstraints gbc_button = new GridBagConstraints();
		gbc_button.insets = new Insets(0, 0, 5, 0);
		gbc_button.gridx = 3;
		gbc_button.gridy = 1;
		panel.add(button, gbc_button);
		
		separator = new JSeparator();
		separator.setBackground(Color.LIGHT_GRAY);
		separator.setForeground(Color.GRAY);
		separator.setPreferredSize(new Dimension(2, 2));
		GridBagConstraints gbc_separator = new GridBagConstraints();
		gbc_separator.insets = new Insets(10, 0, 10, 5);
		gbc_separator.fill = GridBagConstraints.BOTH;
		gbc_separator.gridwidth = 3;
		gbc_separator.gridx = 0;
		gbc_separator.gridy = 2;
		panel.add(separator, gbc_separator);
		
		lblBatCap = new JLabel("Configure battery capacities");
		lblBatCap.setFont(new Font("Tahoma", Font.BOLD, 14));
		GridBagConstraints gbc_lblBatCap = new GridBagConstraints();
		gbc_lblBatCap.insets = new Insets(0, 0, 5, 5);
		gbc_lblBatCap.anchor = GridBagConstraints.WEST;
		gbc_lblBatCap.gridwidth = 3;
		gbc_lblBatCap.gridx = 0;
		gbc_lblBatCap.gridy = 3;
		panel.add(lblBatCap, gbc_lblBatCap);
		
		lblProduct2 = new JLabel("Product:");
		GridBagConstraints gbc_lblProduct2 = new GridBagConstraints();
		gbc_lblProduct2.anchor = GridBagConstraints.EAST;
		gbc_lblProduct2.insets = new Insets(0, 5, 5, 5);
		gbc_lblProduct2.gridx = 0;
		gbc_lblProduct2.gridy = 4;
		panel.add(lblProduct2, gbc_lblProduct2);
		
		cbxBatCap = new JComboBox<String>();
		cbxBatCap.setMinimumSize(new Dimension(150, 20));
		cbxBatCap.setPreferredSize(new Dimension(150, 20));
		GridBagConstraints gbc_cbxBatCap = new GridBagConstraints();
		gbc_cbxBatCap.fill = GridBagConstraints.HORIZONTAL;
		gbc_cbxBatCap.insets = new Insets(0, 0, 5, 5);
		gbc_cbxBatCap.gridx = 1;
		gbc_cbxBatCap.gridy = 4;
		panel.add(cbxBatCap, gbc_cbxBatCap);
		
		textBatCap = new JTextField();
		textBatCap.setPreferredSize(new Dimension(110, 20));
		textBatCap.setMinimumSize(new Dimension(110, 20));
		GridBagConstraints gbc_textBatCap = new GridBagConstraints();
		gbc_textBatCap.fill = GridBagConstraints.HORIZONTAL;
		gbc_textBatCap.insets = new Insets(0, 0, 5, 5);
		gbc_textBatCap.gridx = 2;
		gbc_textBatCap.gridy = 4;
		panel.add(textBatCap, gbc_textBatCap);
		
		button_1 = new JButton("");
		button_1.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String pName = JOptionPane.showInputDialog("Type the product name");
				String bCap = JOptionPane.showInputDialog("Type the battery capacity");
				bat_capMap.put(pName, bCap);
				setData();
				getData();
			}
		});
		button_1.setIcon(Icons.add);
		button_1.setPreferredSize(new Dimension(30, 25));
		GridBagConstraints gbc_button_1 = new GridBagConstraints();
		gbc_button_1.insets = new Insets(0, 0, 5, 0);
		gbc_button_1.gridx = 3;
		gbc_button_1.gridy = 4;
		panel.add(button_1, gbc_button_1);
		
		separator_1 = new JSeparator();
		separator_1.setPreferredSize(new Dimension(2, 2));
		separator_1.setForeground(Color.GRAY);
		separator_1.setBackground(Color.LIGHT_GRAY);
		GridBagConstraints gbc_separator_1 = new GridBagConstraints();
		gbc_separator_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator_1.gridwidth = 3;
		gbc_separator_1.insets = new Insets(10, 0, 10, 5);
		gbc_separator_1.gridx = 0;
		gbc_separator_1.gridy = 5;
		panel.add(separator_1, gbc_separator_1);
		
		panel_1 = new JPanel();
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.anchor = GridBagConstraints.SOUTH;
		gbc_panel_1.insets = new Insets(0, 10, 0, 10);
		gbc_panel_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel_1.gridx = 0;
		gbc_panel_1.gridy = 2;
		contentPane.add(panel_1, gbc_panel_1);
		
		panel_1.setLayout(new FlowLayout(FlowLayout.CENTER, 1, 1));
		btnOk = new JButton("Save and Exit");
		panel_1.add(btnOk);
		
		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				dispose();
			}
		});
		panel_1.add(btnCancel);
		
		// Initialize variables
		dupMap = new HashMap<String, String>();
		dupMap.clear();
		bat_capMap = new HashMap<String, String>();
		bat_capMap.clear();
		
		// Load data
		getData();
		
		// Components action configuration
		cbxDiagProd.addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				textDiag.setText(dupMap.get(e.getItem()));
			}
		});
		
		cbxBatCap.addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				textBatCap.setText(bat_capMap.get(e.getItem()));
			}
		});
		
		textDiag.addFocusListener(new FocusListener()
		{
			@Override
			public void focusLost(FocusEvent arg0)
			{
				dupMap.put((String) cbxDiagProd.getSelectedItem(), textDiag.getText());
			}
			
			@Override
			public void focusGained(FocusEvent arg0)
			{
			}
		});
		
		textBatCap.addFocusListener(new FocusListener()
		{
			@Override
			public void focusLost(FocusEvent arg0)
			{
				bat_capMap.put((String) cbxBatCap.getSelectedItem(), textBatCap.getText());
			}
			
			@Override
			public void focusGained(FocusEvent arg0)
			{
			}
		});
		
		btnOk.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				try
				{
					setData();
					dispose();
				}
				catch (Throwable e1)
				{
					e1.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * @throws JDOMException
	 * @throws IOException
	 */
	public void getData()
	{
		dupMap.clear();
		bat_capMap.clear();
		cbxDiagProd.removeAllItems();
		cbxBatCap.removeAllItems();
		
		dupMap.putAll(XmlMngr.getDiagDupItems());
		System.out.println(dupMap);
		
		for (String value : dupMap.keySet())
		{
			cbxDiagProd.addItem(value);
		}
		
		textDiag.setText(dupMap.get((String) cbxDiagProd.getSelectedItem()));
		
		bat_capMap.putAll(XmlMngr.getBatteryCapacityItems());
		
		for (String value : bat_capMap.keySet())
		{
			cbxBatCap.addItem(value);
		}
		
		textBatCap.setText(bat_capMap.get((String) cbxBatCap.getSelectedItem()));
		
		Logger.log(Logger.TAG_OPTIONS, "Advanced options loaded");
	}
	
	/**
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws JDOMException
	 */
	public void setData()
	{
		XmlMngr.setBatteryCapacityItems(bat_capMap);
		XmlMngr.setDiagDupItems(dupMap);
		
		Logger.log(Logger.TAG_OPTIONS, "Advanced Options Saved");
	}
	
	// Getters and Setters
	public String getDupValue(String productName)
	{
		return dupMap.get(productName);
	}
	
	public String getBatCapValue(String productName)
	{
		return bat_capMap.get(productName);
	}
}
