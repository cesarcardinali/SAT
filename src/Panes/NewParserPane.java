package Panes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.UndoManager;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import Filters.Alarm;
import Filters.B2G;
import Filters.Consume;
import Filters.Diag;
import Filters.Issue;
import Filters.Normal;
import Filters.Suspicious;
import Filters.Tether;
import Main.BatTracer;

import Supportive.NonWrappingTextPane;
import javax.swing.JSplitPane;

public class NewParserPane extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	BatTracer BaseWindow;
	String crPath, result;
	
	JLabel lblTitle;
	NonWrappingTextPane textPane;
	private UndoManager undoManager;
	private JSplitPane splitPane;
	private FileTree fileTree;
	private FiltersResultsTree filtersResultsTree;

	/**
	 * Create the panel.
	 */
	public NewParserPane(BatTracer Parent) {
		BaseWindow = Parent;
		setMinimumSize(new Dimension(800, 600));
		
		GridBagLayout layout = new GridBagLayout();
		layout.columnWidths = new int[]{250, 600};
		layout.rowHeights = new int[]{40, 300};
		layout.rowWeights = new double[]{1.0, 1.0};
		layout.columnWeights = new double[]{1.0, 1.0};
		setLayout(layout);
		JPanel topright = new JPanel();
		topright.setPreferredSize(new Dimension(10, 30));
		topright.setMaximumSize(new Dimension(32767, 31));
		topright.setBorder(new LineBorder(UIManager.getColor("Button.light")));;
		topright.setMinimumSize(new Dimension(35, 30));		
		
		GridBagLayout tr = new GridBagLayout();
		tr.rowWeights = new double[]{0.0};
		tr.rowHeights = new int[]{0};
		topright.setLayout(tr);
		
		GridBagConstraints g1 = new GridBagConstraints();
		g1.fill = GridBagConstraints.HORIZONTAL;
		g1.weightx = 20.0;
		g1.weighty = 1.0;
		g1.insets = new Insets(5, 10, 5, 10);
		g1.gridx = 1;
		g1.gridy = 0;
		add(topright, g1);
		lblTitle = new JLabel("Select a result folder on the left panel");
		lblTitle.setMaximumSize(new Dimension(2000, 31));
		lblTitle.setHorizontalTextPosition(SwingConstants.LEFT);
		lblTitle.setHorizontalAlignment(SwingConstants.LEFT);
		lblTitle.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 20));
		lblTitle.setPreferredSize(new Dimension(35, 30));
		lblTitle.setMinimumSize(new Dimension(400, 30));
		GridBagConstraints gbc_lblTitle = new GridBagConstraints();
		gbc_lblTitle.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblTitle.insets = new Insets(0, 0, 0, 5);
		gbc_lblTitle.anchor = GridBagConstraints.WEST;
		gbc_lblTitle.weighty = 1.0;
		gbc_lblTitle.weightx = 5.0;
		gbc_lblTitle.gridy = 0;
		gbc_lblTitle.gridx = 0;
		topright.add(lblTitle, gbc_lblTitle);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setToolTipText("Result of the selected parser item on the left");
		scrollPane.setFont(new Font("Consolas", Font.PLAIN, 12));
		scrollPane.setBorder(new LineBorder(new Color(220, 220, 220)));;
		scrollPane.setAutoscrolls(true);
		scrollPane.setRequestFocusEnabled(false);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setPreferredSize(new Dimension(500, 500));
		scrollPane.setMinimumSize(new Dimension(400, 400));
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);
		
		splitPane = new JSplitPane();
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		fileTree = new FileTree(BaseWindow);
		splitPane.setRightComponent(fileTree);
		JScrollPane scrollFiltersResults = new JScrollPane();
		filtersResultsTree = new FiltersResultsTree(BaseWindow);
		scrollFiltersResults.setViewportView(filtersResultsTree);
		scrollFiltersResults.setMinimumSize(new Dimension(200, 150));
		splitPane.setLeftComponent(scrollFiltersResults);
		GridBagConstraints gbc_splitPane = new GridBagConstraints();
		gbc_splitPane.insets = new Insets(0, 0, 0, 5);
		gbc_splitPane.fill = GridBagConstraints.BOTH;
		gbc_splitPane.gridx = 0;
		gbc_splitPane.gridy = 1;
		add(splitPane, gbc_splitPane);

		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(2, 10, 10, 10);
		gbc_scrollPane.weighty = 22.0;
		gbc_scrollPane.weightx = 15.0;
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 1;
		gbc_scrollPane.gridy = 1;
		add(scrollPane, gbc_scrollPane);
		
		textPane = new NonWrappingTextPane();
		textPane.setToolTipText("Result of the selected parser item on the left");
		textPane.setContentType("text/plain");
		textPane.setMargin(new Insets(7, 2, 7, 2));
		textPane.setForeground(new Color(0, 0, 0));
		textPane.setFont(new Font("Consolas", Font.PLAIN, 11));
		textPane.setText("");
		undoManager = new UndoManager();
		textPane.getDocument().addUndoableEditListener(new UndoableEditListener() {
			@Override
			public void undoableEditHappened(UndoableEditEvent e) {
				undoManager.addEdit(e.getEdit());
			}
		});
		textPane.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				saveTextChanges(filtersResultsTree.getLastSelectedPathComponent(), textPane.getText());
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if ((e.getKeyCode() == KeyEvent.VK_Z) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
					// textPane.setText("woot!");
					try {
						undoManager.undo();
					} catch (CannotRedoException cre) {
						cre.printStackTrace();
					}
				}
			}
		});
		scrollPane.setViewportView(textPane);
		
		result = "";
		textPane.setText("");
		lblTitle.setText("Run a parser or select a result on the left");
		loadPaneData();
	}
	
	
	
	
	/**
	 * Supportive methods
	 */
	// Save pane data
	public void savePaneData(){
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
			Element optionPaneNode = satNode.getChild("parser_pane"); 
			for(Element e : optionPaneNode.getChildren()){
				if(e.getName().equals("path")){
					//salvar a root folder usada para criar a file tree
					e.setText(fileTree.getRootFolderPath());
					
				}
			}
			
			//JDOM document is ready now, lets write it to file now
	        XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
	        //output xml to console for debugging
	        //xmlOutputter.output(doc, System.out);
	        xmlOutputter.output(document, new FileOutputStream(xmlFile));
	        
			System.out.println("Options Saved");
		} catch (JDOMException | IOException e){
			e.printStackTrace();
		}
	}
	
	// Load pane data
	private void loadPaneData(){
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
			Element crs_jira_paneNode = satNode.getChild("parser_pane");
			for(Element e : crs_jira_paneNode.getChildren()){
				if(e.getName().equals("path")){
					crPath = (e.getValue());
					//selecionar na JTree o último arquivo que estava selecionado
					//folder.setText(getRootPath());
					
				} else if(e.getName().equals("tree_breakdown")){
					filtersResultsTree.setToggleClickCount(Integer.parseInt(e.getValue()));
				}
			}
			System.out.println("Options Loaded");
		
		} catch (IOException | JDOMException e){
			e.printStackTrace();
		}
	}

	// Save textPane text user edited
	private void saveTextChanges(Object node, String text) {
						
		String selectedNode = node.toString().toLowerCase();
		String selectedNodeParent = ((DefaultMutableTreeNode) node).getParent().toString().toLowerCase();
		
		if ((selectedNode.contains("colors") && selectedNodeParent.contains("alarms ")) || selectedNode.contains("alarms "))
			Alarm.updateResult(text);
		
		if ((selectedNode.contains("colors") && selectedNodeParent.contains(" consum")) || selectedNode.contains(" consum"))
			Consume.updateResult(text);
		
		if (selectedNode.contains("diag ") || selectedNodeParent.contains("diag "))
			Diag.updateResult(text);
		
		if (selectedNode.contains("suspicious"))
			Suspicious.updateResult(text);
		
		if (selectedNode.contains("tether") || selectedNodeParent.contains("tether"))
			Tether.updateResult(text);
		
		if (selectedNode.contains("summary") || selectedNodeParent.contains("summary"))
			Normal.updateResult(text);
		
		if (selectedNode.contains(" issues") || selectedNodeParent.contains(" issues"))
			Issue.updateResult(text);
		
		if (selectedNode.contains("bug2go") || selectedNodeParent.contains("bug2go")) {
			B2G.updateResult(text);
			B2G.setEdited(true);
		}
		
	} // end saveTextChanges()


	// Getters and Setters
	public FiltersResultsTree getFiltersResultsTree() {
		return filtersResultsTree;
	}

	public String getCrPath() {
		return crPath;
	}

	public void setCrPath(String crPath) {
		this.crPath = crPath;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
	public void showAllResults() {
		textPane.setText("Results Compilation:" + result);
	}
	
	public String getResult(){
		return result;
	}
	
	
	public void setResultsText(String text){
		textPane.setText(text);
		textPane.setCaretPosition(0);
	}
	
	public void setTitle(String text){
		lblTitle.setText(text);
	}
	
	public NonWrappingTextPane getTextPane(){
		return textPane;
	}
	
}
