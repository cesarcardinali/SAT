package Panes;

import java.awt.EventQueue;

import javax.swing.JDialog;

import java.awt.GridBagLayout;

import javax.swing.JLabel;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.SwingConstants;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import javax.swing.JPanel;

import javax.swing.JCheckBox;
import javax.swing.BoxLayout;

import Main.BatTracer;
import Objects.CustomFilterItem;

public class CustomFilters extends JDialog {
	
	private static final long serialVersionUID = 4568861765364938399L;
	private JTextField txtName;
	private JTextField txtRegex;
	private JTextField txtHeader;
	private BatTracer BaseWindow;
	private JCheckBox chckbxRadio;
	private JCheckBox chckbxSystem;
	private JCheckBox chckbxBugreport;
	private JCheckBox chckbxMain;
	private JButton btnAdd;
	private JComboBox<String> comboBox;
	private JButton btnDel;
	private JCheckBox chckbxKernel;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CustomFilters dialog = new CustomFilters(null);
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the dialog.
	 */
	public CustomFilters(BatTracer parent) {
		BaseWindow = parent;
		
		setVisible(false);
		setTitle("Custom Filters Manager");
		setBounds(100, 100, 782, 448);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		
		JLabel lblFiltersList = new JLabel("Filters list:");
		lblFiltersList.setHorizontalAlignment(SwingConstants.RIGHT);
		lblFiltersList.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblFiltersList.setPreferredSize(new Dimension(70, 23));
		GridBagConstraints gbc_lblFiltersList = new GridBagConstraints();
		gbc_lblFiltersList.anchor = GridBagConstraints.WEST;
		gbc_lblFiltersList.insets = new Insets(5, 10, 5, 5);
		gbc_lblFiltersList.gridx = 0;
		gbc_lblFiltersList.gridy = 0;
		getContentPane().add(lblFiltersList, gbc_lblFiltersList);
		
		comboBox = new JComboBox<String>();
		comboBox.setPreferredSize(new Dimension(300, 23));
		comboBox.setMinimumSize(new Dimension(300, 23));
		comboBox.setMaximumRowCount(6);
		comboBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String aux = (String) comboBox.getSelectedItem();
				//pegar a lista de filtros custom.
				//encontrar o item clicado.
				//preencher os campos do panel com os valores desse filtro.
			}
		});
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.insets = new Insets(5, 0, 5, 5);
		gbc_comboBox.anchor = GridBagConstraints.WEST;
		gbc_comboBox.gridx = 1;
		gbc_comboBox.gridy = 0;
		getContentPane().add(comboBox, gbc_comboBox);
		
		btnDel = new JButton("");
		btnDel.setIcon(new ImageIcon("C:\\Users\\cesar.cardinali\\workspace\\BatteryTool\\Data\\pics\\delete2.png"));
		btnDel.setPreferredSize(new Dimension(25, 25));
		btnDel.setMargin(new Insets(2, 2, 2, 2));
		GridBagConstraints gbc_btnDel = new GridBagConstraints();
		gbc_btnDel.insets = new Insets(5, 0, 5, 5);
		gbc_btnDel.gridx = 2;
		gbc_btnDel.gridy = 0;
		getContentPane().add(btnDel, gbc_btnDel);
		
		btnAdd = new JButton("");
		btnAdd.setPreferredSize(new Dimension(25, 25));
		btnAdd.setMargin(new Insets(2, 2, 2, 2));
		btnAdd.setIcon(new ImageIcon("C:\\Users\\cesar.cardinali\\workspace\\BatteryTool\\Data\\pics\\add2.png"));
		GridBagConstraints gbc_btnAdd = new GridBagConstraints();
		gbc_btnAdd.insets = new Insets(5, 0, 5, 5);
		gbc_btnAdd.gridx = 3;
		gbc_btnAdd.gridy = 0;
		getContentPane().add(btnAdd, gbc_btnAdd);
		
		JLabel lblFilterName = new JLabel("Filter Name:");
		GridBagConstraints gbc_lblFilterName = new GridBagConstraints();
		gbc_lblFilterName.anchor = GridBagConstraints.EAST;
		gbc_lblFilterName.insets = new Insets(0, 0, 5, 5);
		gbc_lblFilterName.gridx = 0;
		gbc_lblFilterName.gridy = 1;
		getContentPane().add(lblFilterName, gbc_lblFilterName);
		
		txtName = new JTextField();
		txtName.setPreferredSize(new Dimension(6, 23));
		GridBagConstraints gbc_txtName = new GridBagConstraints();
		gbc_txtName.gridwidth = 4;
		gbc_txtName.insets = new Insets(0, 0, 5, 5);
		gbc_txtName.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtName.gridx = 1;
		gbc_txtName.gridy = 1;
		getContentPane().add(txtName, gbc_txtName);
		txtName.setColumns(10);
		
		JLabel lblRegex = new JLabel("Regex:");
		GridBagConstraints gbc_lblRegex = new GridBagConstraints();
		gbc_lblRegex.anchor = GridBagConstraints.EAST;
		gbc_lblRegex.insets = new Insets(0, 0, 5, 5);
		gbc_lblRegex.gridx = 0;
		gbc_lblRegex.gridy = 2;
		getContentPane().add(lblRegex, gbc_lblRegex);
		
		txtRegex = new JTextField();
		txtRegex.setPreferredSize(new Dimension(6, 23));
		GridBagConstraints gbc_txtRegex = new GridBagConstraints();
		gbc_txtRegex.gridwidth = 4;
		gbc_txtRegex.insets = new Insets(0, 0, 5, 5);
		gbc_txtRegex.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtRegex.gridx = 1;
		gbc_txtRegex.gridy = 2;
		getContentPane().add(txtRegex, gbc_txtRegex);
		txtRegex.setColumns(10);
		
		JLabel lblWhere = new JLabel("Search at:");
		GridBagConstraints gbc_lblWhere = new GridBagConstraints();
		gbc_lblWhere.anchor = GridBagConstraints.EAST;
		gbc_lblWhere.insets = new Insets(0, 0, 5, 5);
		gbc_lblWhere.gridx = 0;
		gbc_lblWhere.gridy = 3;
		getContentPane().add(lblWhere, gbc_lblWhere);
		
		JPanel chkbxPanel = new JPanel();
		GridBagConstraints gbc_chkbxPanel = new GridBagConstraints();
		gbc_chkbxPanel.insets = new Insets(0, 0, 5, 5);
		gbc_chkbxPanel.gridx = 1;
		gbc_chkbxPanel.gridy = 3;
		getContentPane().add(chkbxPanel, gbc_chkbxPanel);
		
		chckbxMain = new JCheckBox("Main");
		
		chckbxSystem = new JCheckBox("System");
		
		chckbxKernel = new JCheckBox("Kernel");
		chkbxPanel.setLayout(new BoxLayout(chkbxPanel, BoxLayout.X_AXIS));
		chkbxPanel.add(chckbxMain);
		chkbxPanel.add(chckbxSystem);
		chkbxPanel.add(chckbxKernel);
		
		chckbxRadio = new JCheckBox("Radio");
		chkbxPanel.add(chckbxRadio);
		
		chckbxBugreport = new JCheckBox("Bugreport");
		chkbxPanel.add(chckbxBugreport);
		
		JLabel lblHeader = new JLabel("Message:");
		GridBagConstraints gbc_lblHeader = new GridBagConstraints();
		gbc_lblHeader.anchor = GridBagConstraints.EAST;
		gbc_lblHeader.insets = new Insets(0, 0, 5, 5);
		gbc_lblHeader.gridx = 0;
		gbc_lblHeader.gridy = 4;
		getContentPane().add(lblHeader, gbc_lblHeader);
		
		txtHeader = new JTextField();
		txtHeader.setPreferredSize(new Dimension(6, 23));
		GridBagConstraints gbc_txtHeader = new GridBagConstraints();
		gbc_txtHeader.gridwidth = 4;
		gbc_txtHeader.insets = new Insets(0, 0, 5, 5);
		gbc_txtHeader.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtHeader.gridx = 1;
		gbc_txtHeader.gridy = 4;
		getContentPane().add(txtHeader, gbc_txtHeader);
		txtHeader.setColumns(10);
		
	}
	
	
	
	public void loadFilters(ArrayList<CustomFilterItem> customFiltersList){
		try{
			File xmlFile = new File("Data/cfgs/user_cfg.xml");
			
			SAXBuilder builder = new SAXBuilder();
			
			Document document = (Document) builder.build(xmlFile);
			
			Element filtersNode = document.getRootElement().getChild("parser_pane").getChild("custom_filters");
			List<Element> filtersElements = filtersNode.getChildren();
			
			if(!filtersElements.isEmpty()){
				for(Element filter : filtersElements){
					customFiltersList.add(new CustomFilterItem(BaseWindow, filter.getName(), filter.getAttributeValue("regex"), filter.getAttributeValue("header"), filter.getAttributeValue("wheretosearch")));
				}
			}
		} catch(IOException | JDOMException e){
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}

	
	public void saveFilters(ArrayList<CustomFilterItem> customFilters) {
		try{
			File xmlFile = new File("Data/cfgs/user_cfg.xml");
			
			SAXBuilder builder = new SAXBuilder();
			
			Document document = (Document) builder.build(xmlFile);
			
			Element satNode = document.getRootElement();
			
			Element filtersNode = document.getRootElement().getChild("parser_pane").getChild("custom_filters");
			/*
			for(Element e : optionPaneNode.getChildren()){
				if(e.getName().equals("path")){
					e.setText(folder.getText());
					
				} else if(e.getName().equals("editor")){
					if(rdBtnTextAnal.isSelected())
						e.setText("0");
					else
						e.setText("1");
				}
			}*/
			


	        XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
	        //For output generated xml to console for debugging
	        //xmlOutputter.output(doc, System.out);
	        xmlOutputter.output(document, new FileOutputStream(xmlFile));
	        
			System.out.println("Options Saved");
		} catch (JDOMException | IOException e){
			e.printStackTrace();
		}
	}

	public void open(ArrayList<CustomFilterItem> customFilters) {
		comboBox.removeAllItems();
		for(CustomFilterItem customfilter : customFilters)
			comboBox.addItem(customfilter.getName());

		setVisible(true);
	}
	
	
	
	
	
	
	public JCheckBox getChckbxRadio() {
		return chckbxRadio;
	}
	public JCheckBox getChckbxSystem() {
		return chckbxSystem;
	}
	public JTextField getTxtHeader() {
		return txtHeader;
	}
	public JCheckBox getChckbxBugreport() {
		return chckbxBugreport;
	}
	public JCheckBox getChckbxMain() {
		return chckbxMain;
	}
	public JButton getBtnAdd() {
		return btnAdd;
	}
	public JTextField getTxtRegex() {
		return txtRegex;
	}
	public JComboBox getComboBox() {
		return comboBox;
	}
	public JTextField getTxtName() {
		return txtName;
	}
	public JButton getBtnDel() {
		return btnDel;
	}
	public JCheckBox getChckbxKernel() {
		return chckbxKernel;
	}
}
