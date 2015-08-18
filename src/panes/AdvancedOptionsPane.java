package panes;


import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JSeparator;
import javax.swing.JButton;

import java.awt.GridBagLayout;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import core.Icons;
import core.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.HashMap;


public class AdvancedOptionsPane extends JFrame
{
	private static final long		serialVersionUID = 3696506987866824851L;
	private JPanel					contentPane;
	private JLabel					lblTitle;
	private JPanel					panel;
	private JLabel					lblProduct;
	private JComboBox<String>		cbxDiagProd;
	private JLabel					lblConfigureDiagwsDup;
	private JTextField				textDiag;
	private JSeparator				separator;
	private JLabel					lblBatCap;
	private JLabel					lblProduct2;
	private JComboBox<String>		cbxBatCap;
	private JTextField				textBatCap;
	private JSeparator				separator_1;
	private JPanel					panel_1;
	private JButton					btnOk;
	private HashMap<String, String>	dupMap;
	private HashMap<String, String>	bat_capMap;
	private JButton					btnSet;
	private JButton					btnSet2;
	private Element					diag_dupNode;
	private Element					bat_capNode;
	private File					xmlFile;
	private SAXBuilder				builder;
	private Document				document;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					AdvancedOptionsPane frame = new AdvancedOptionsPane();
					frame.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Create the frame.
	 */
	public AdvancedOptionsPane()
	{
		setTitle("Advanced options");
		setResizable(false);
		setMinimumSize(new Dimension(400, 350));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 429, 356);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
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
		gbl_panel.columnWidths = new int[] {0, 0, 0, 0, 0};
		gbl_panel.rowHeights = new int[] {0, 0, 0, 0, 0, 0, 0};
		gbl_panel.columnWeights = new double[] {0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		lblConfigureDiagwsDup = new JLabel("Configure DIAG_WS Dup CR");
		lblConfigureDiagwsDup.setFont(new Font("Tahoma", Font.BOLD, 14));
		GridBagConstraints gbc_lblConfigureDiagwsDup = new GridBagConstraints();
		gbc_lblConfigureDiagwsDup.anchor = GridBagConstraints.WEST;
		gbc_lblConfigureDiagwsDup.gridwidth = 4;
		gbc_lblConfigureDiagwsDup.insets = new Insets(0, 0, 5, 0);
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
		gbc_cbxDiagProd.insets = new Insets(0, 0, 5, 5);
		gbc_cbxDiagProd.anchor = GridBagConstraints.WEST;
		gbc_cbxDiagProd.gridx = 1;
		gbc_cbxDiagProd.gridy = 1;
		panel.add(cbxDiagProd, gbc_cbxDiagProd);
		textDiag = new JTextField();
		textDiag.setPreferredSize(new Dimension(150, 20));
		textDiag.setMinimumSize(new Dimension(150, 20));
		GridBagConstraints gbc_textDiag = new GridBagConstraints();
		gbc_textDiag.insets = new Insets(0, 0, 5, 5);
		gbc_textDiag.fill = GridBagConstraints.HORIZONTAL;
		gbc_textDiag.gridx = 2;
		gbc_textDiag.gridy = 1;
		panel.add(textDiag, gbc_textDiag);
		textDiag.setColumns(10);
		btnSet = new JButton("Set");
		btnSet.setPreferredSize(new Dimension(49, 20));
		btnSet.setMinimumSize(new Dimension(49, 20));
		btnSet.setIcon(Icons.ok);
		btnSet.setMargin(new Insets(2, 2, 2, 2));
		GridBagConstraints gbc_btnSet = new GridBagConstraints();
		gbc_btnSet.insets = new Insets(0, 0, 5, 0);
		gbc_btnSet.gridx = 3;
		gbc_btnSet.gridy = 1;
		panel.add(btnSet, gbc_btnSet);
		separator = new JSeparator();
		separator.setBackground(Color.LIGHT_GRAY);
		separator.setForeground(Color.GRAY);
		separator.setPreferredSize(new Dimension(2, 2));
		GridBagConstraints gbc_separator = new GridBagConstraints();
		gbc_separator.insets = new Insets(10, 0, 10, 0);
		gbc_separator.fill = GridBagConstraints.BOTH;
		gbc_separator.gridwidth = 4;
		gbc_separator.gridx = 0;
		gbc_separator.gridy = 2;
		panel.add(separator, gbc_separator);
		lblBatCap = new JLabel("Configure battery capacities");
		lblBatCap.setFont(new Font("Tahoma", Font.BOLD, 14));
		GridBagConstraints gbc_lblBatCap = new GridBagConstraints();
		gbc_lblBatCap.insets = new Insets(0, 0, 5, 0);
		gbc_lblBatCap.anchor = GridBagConstraints.WEST;
		gbc_lblBatCap.gridwidth = 4;
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
		gbc_cbxBatCap.anchor = GridBagConstraints.WEST;
		gbc_cbxBatCap.insets = new Insets(0, 0, 5, 5);
		gbc_cbxBatCap.gridx = 1;
		gbc_cbxBatCap.gridy = 4;
		panel.add(cbxBatCap, gbc_cbxBatCap);
		textBatCap = new JTextField();
		textBatCap.setPreferredSize(new Dimension(150, 20));
		textBatCap.setMinimumSize(new Dimension(150, 20));
		textBatCap.setColumns(10);
		GridBagConstraints gbc_textBatCap = new GridBagConstraints();
		gbc_textBatCap.insets = new Insets(0, 0, 5, 5);
		gbc_textBatCap.fill = GridBagConstraints.HORIZONTAL;
		gbc_textBatCap.gridx = 2;
		gbc_textBatCap.gridy = 4;
		panel.add(textBatCap, gbc_textBatCap);
		btnSet2 = new JButton("Set");
		btnSet2.setPreferredSize(new Dimension(49, 20));
		btnSet2.setMinimumSize(new Dimension(49, 20));
		btnSet2.setMargin(new Insets(2, 2, 2, 2));
		btnSet2.setIcon(new ImageIcon("Data\\pics\\Ok.png"));
		GridBagConstraints gbc_btnSet2 = new GridBagConstraints();
		gbc_btnSet2.insets = new Insets(0, 0, 5, 0);
		gbc_btnSet2.gridx = 3;
		gbc_btnSet2.gridy = 4;
		panel.add(btnSet2, gbc_btnSet2);
		separator_1 = new JSeparator();
		separator_1.setPreferredSize(new Dimension(2, 2));
		separator_1.setForeground(Color.GRAY);
		separator_1.setBackground(Color.LIGHT_GRAY);
		GridBagConstraints gbc_separator_1 = new GridBagConstraints();
		gbc_separator_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator_1.gridwidth = 4;
		gbc_separator_1.insets = new Insets(10, 0, 10, 0);
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
		// Initialize variables
		dupMap = new HashMap<String, String>();
		dupMap.clear();
		bat_capMap = new HashMap<String, String>();
		bat_capMap.clear();
		// Load data
		try
		{
			getData();
		}
		catch (JDOMException | IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		btnSet.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				diag_dupNode.getChild((String) cbxDiagProd.getSelectedItem()).setText(textDiag.getText());
				dupMap.put((String) cbxDiagProd.getSelectedItem(), textDiag.getText());
			}
		});
		btnSet2.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				bat_capNode.getChild((String) cbxBatCap.getSelectedItem()).setText(textBatCap.getText());
				bat_capMap.put((String) cbxBatCap.getSelectedItem(), textBatCap.getText());
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
	
	public void getData() throws JDOMException, IOException
	{
		// Abre o arquivo XML
		xmlFile = new File("Data/cfgs/user_cfg.xml");
		// Cria o builder da estrutura XML
		builder = new SAXBuilder();
		// Cria documento formatado de acordo com a lib XML
		document = (Document) builder.build(xmlFile);
		// Pega o nó raiz do XML
		Element satNode = document.getRootElement();
		// Pega os nós
		diag_dupNode = satNode.getChild("diag_dup");
		bat_capNode = satNode.getChild("bat_cap");
		// Limpa os maps
		dupMap.clear();
		bat_capMap.clear();
		// Carrega lista de dup
		for (Element e : diag_dupNode.getChildren())
		{
			cbxDiagProd.addItem(e.getName());
			dupMap.put(e.getName(), e.getValue());
		}
		textDiag.setText(dupMap.get((String) cbxDiagProd.getSelectedItem()));
		// Carrega lista de bat_cap
		for (Element e : bat_capNode.getChildren())
		{
			cbxBatCap.addItem(e.getName());
			bat_capMap.put(e.getName(), e.getValue());
		}
		textBatCap.setText(bat_capMap.get((String) cbxBatCap.getSelectedItem()));
	}
	
	public void setData() throws FileNotFoundException, IOException, JDOMException
	{
		// Assuming that JDOM document is ready, here we format it to
		// XML
		XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
		// Output xml to console for debugging
		// xmlOutputter.output(document, System.out);
		// Print the file
		xmlOutputter.output(document, new FileOutputStream(xmlFile));
		Logger.log(Logger.TAG_OPTIONS, "Options Saved");
	}
	
	// Getters and Setters
	public Element getDupNode()
	{
		return diag_dupNode;
	}
	
	public Element getBat_capNode()
	{
		return bat_capNode;
	}
}
