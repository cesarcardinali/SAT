package Panes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
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

public class NewParserPane extends JPanel {
	

	private static final long serialVersionUID = 1L;
	
	static DefaultTreeModel treeModel;
	DefaultMutableTreeNode rootNode;
	
	BatTracer BaseWindow;
	String crPath, result;
	
	JLabel lblTitle;
	JTextPane textPane;
	JRadioButton rdBtnTextAnal;
	JRadioButton rdbtnNotepad;
	private UndoManager undoManager;
	private JTabbedPane esquerda;
	private FileTree fileTree;
	private JScrollPane filtersResultsTab;
	private FiltersResultsTree filtersResultsTree;

	/**
	 * Create the panel.
	 */
	public NewParserPane(BatTracer Parent) {
		BaseWindow = Parent;
		setMinimumSize(new Dimension(800, 600));
		
		GridBagLayout layout = new GridBagLayout();
		layout.columnWidths = new int[]{250, 600};
		layout.rowHeights = new int[]{35, 40, 600};
		layout.rowWeights = new double[]{1.0, 1.0, 1.0};
		layout.columnWeights = new double[]{1.0, 1.0};
		setLayout(layout);
		JPanel topright = new JPanel();
		topright.setPreferredSize(new Dimension(10, 30));
		topright.setMaximumSize(new Dimension(32767, 31));
		topright.setBorder(new LineBorder(UIManager.getColor("Button.light")));;
		topright.setMinimumSize(new Dimension(35, 30));
		
		ButtonGroup editorSelector = new ButtonGroup();
		
		JPanel LogsPane = new JPanel();
		LogsPane.setBorder(new LineBorder(UIManager.getColor("Button.light")));
		LogsPane.setMaximumSize(new Dimension(32767, 30));
		LogsPane.setPreferredSize(new Dimension(10, 30));
		LogsPane.setMinimumSize(new Dimension(10, 30));
		FlowLayout fl_LogsPane = (FlowLayout) LogsPane.getLayout();
		fl_LogsPane.setHgap(3);
		fl_LogsPane.setVgap(1);
		GridBagConstraints gbc_LogsPane = new GridBagConstraints();
		gbc_LogsPane.weighty = 1.0;
		gbc_LogsPane.weightx = 1.0;
		gbc_LogsPane.gridwidth = 2;
		gbc_LogsPane.insets = new Insets(0, 10, 5, 10);
		gbc_LogsPane.fill = GridBagConstraints.HORIZONTAL;
		gbc_LogsPane.gridx = 0;
		gbc_LogsPane.gridy = 0;
		add(LogsPane, gbc_LogsPane);
		
		rdBtnTextAnal = new JRadioButton("TextAnalysis");
		rdBtnTextAnal.setToolTipText("Use TextAnalysis tool as default text editor");
		LogsPane.add(rdBtnTextAnal);
		rdBtnTextAnal.setSelected(true);
		editorSelector.add(rdBtnTextAnal);
		
		rdbtnNotepad = new JRadioButton("Notepad++");
		rdbtnNotepad.setToolTipText("Use Notepad++ as default text editor");
		LogsPane.add(rdbtnNotepad);
		editorSelector.add(rdbtnNotepad);
		
		
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
		g1.gridy = 1;
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
		
		esquerda = new JTabbedPane(JTabbedPane.TOP);
		esquerda.setPreferredSize(new Dimension(250, 600));
		GridBagConstraints gbc_esquerda = new GridBagConstraints();
		gbc_esquerda.gridheight = 2;
		gbc_esquerda.insets = new Insets(5, 10, 10, 5);
		gbc_esquerda.fill = GridBagConstraints.BOTH;
		gbc_esquerda.gridx = 0;
		gbc_esquerda.gridy = 1;
		add(esquerda, gbc_esquerda);
		
		
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.anchor = GridBagConstraints.NORTHWEST;
		gbc_scrollPane.insets = new Insets(2, 10, 10, 10);
		gbc_scrollPane.weighty = 22.0;
		gbc_scrollPane.weightx = 15.0;
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 1;
		gbc_scrollPane.gridy = 2;
		add(scrollPane, gbc_scrollPane);
		
		

/**/	textPane = new NonWrappingTextPane();
		
		textPane.setToolTipText("Result of the selected parser item on the left");
		textPane.setContentType("text/plain");
		textPane.setMargin(new Insets(7, 2, 7, 2));
		textPane.setForeground(new Color(0, 0, 0));
		textPane.setFont(new Font("Consolas", Font.PLAIN, 11));
		textPane.setText("");
		undoManager = new UndoManager();
		textPane.getDocument().addUndoableEditListener(
				new UndoableEditListener() {

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
			public void keyTyped(KeyEvent arg0) {}

			@Override
			public void keyPressed(KeyEvent e) {
				if ((e.getKeyCode() == KeyEvent.VK_Z)
						&& ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
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
		
		
		fileTree = new FileTree(BaseWindow);
		esquerda.addTab("FileTree", null, fileTree, null);
		
		filtersResultsTab = new JScrollPane();
		esquerda.addTab("Filters and Results", null, filtersResultsTab, null);
		
		filtersResultsTree= new FiltersResultsTree(BaseWindow);
		filtersResultsTab.setViewportView(filtersResultsTree);
		
		result = "";
		textPane.setText("");
		lblTitle.setText("Run a parser or select a result on the left");
		loadPaneData();
	}


	
	
	
	
	//Node functions
	public void addConsumeNode(String node){
		DefaultMutableTreeNode HCNode = findNode("High Consume");
		if(HCNode == null){
			System.out.println("Could not find node");
		}
		DefaultMutableTreeNode HCitem = new DefaultMutableTreeNode(node);
		HCitem.add(new DefaultMutableTreeNode("Screen ON"));
		HCitem.add(new DefaultMutableTreeNode("Screen OFF"));
		HCitem.add(new DefaultMutableTreeNode("Full Log"));
		HCitem.add(new DefaultMutableTreeNode("On Colors"));
		HCNode.insert(HCitem, HCNode.getChildCount());
		treeModel.reload();
	}
	
	public void addWakeLocksNode(String node){
		DefaultMutableTreeNode WLNode = findNode("Suspicious Wake Locks");
		if(WLNode == null){
			System.out.println("Could not find node");
		}
		DefaultMutableTreeNode WLitem = new DefaultMutableTreeNode(node);
		WLitem.add(new DefaultMutableTreeNode("Formatted"));
		WLNode.insert(WLitem, WLNode.getChildCount());
		treeModel.reload();
	}
	
	public void addIssues(){
		DefaultMutableTreeNode INode = findNode("General Issues");
		if(INode == null){
			System.out.println("Could not find node");
			INode = findNode("General Issues");
			DefaultMutableTreeNode Iitem = new DefaultMutableTreeNode("");
			INode.insert(Iitem, 0);
			treeModel.reload();
		}
	}
	
	public void addBug2go(String node){
		DefaultMutableTreeNode BGNode = findNode("Bug2Go");
		if(BGNode == null){
			System.out.println("Could not find node");
		}
		BGNode.insert(new DefaultMutableTreeNode(""), BGNode.getChildCount());
		treeModel.reload();
	}
	
	public void addTether(String node){
		DefaultMutableTreeNode TNode = findNode("Tether");
		if(TNode == null){
			System.out.println("Could not find node");
		}
		if (Tether.getResult().contains("No tethering evidences were found in text logs"))
			TNode.insert(new DefaultMutableTreeNode("No tethering activity found"), TNode.getChildCount());
		else
			TNode.insert(new DefaultMutableTreeNode("Log result"), TNode.getChildCount());
		
		treeModel.reload();
	}
	
	public void addDiag(String node){
		DefaultMutableTreeNode TNode = findNode("Diag");
		if(TNode == null){
			System.out.println("Could not find node");
		}
		TNode.insert(new DefaultMutableTreeNode(""), TNode.getChildCount());
		treeModel.reload();
	}
	
	public void addAlarms(String node){
		DefaultMutableTreeNode ANode = findNode("Alarms");
		if(ANode == null){
			System.out.println("Could not find node");
		}
		DefaultMutableTreeNode Aitem = new DefaultMutableTreeNode(node);
		if(!node.contains("Nothing") && !(ANode.getUserObject().toString()).contains("Error"))
			Aitem.add(new DefaultMutableTreeNode("On Colors"));
		ANode.insert(Aitem, ANode.getChildCount());
	}
	
	public void addSummary(){
		DefaultMutableTreeNode TNode = findNode("Discharge Summary");
		if(TNode == null){
			System.out.println("Could not find node");
		}
		TNode.insert(new DefaultMutableTreeNode(""), TNode.getChildCount());
		treeModel.reload();
	}
	
	public DefaultMutableTreeNode findNode(String Node){
		int Length = rootNode.getChildCount();
		for(int i=0; i < Length; i++)
		{
			if(rootNode.getChildAt(i).toString().contains(Node)){
				return (DefaultMutableTreeNode) rootNode.getChildAt(i);
			}
			//textPane.setText(textPane.getText() + "\n" + rootNode.getChildAt(i));
		}	
		return null;
	}
	
	public int findNodeIndex(DefaultMutableTreeNode Node){
		System.out.println(rootNode.getIndex(Node));
		return rootNode.getIndex(Node);
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
					//salvar o último caminho selecionado na árvore
					e.setText(getRootPath());
					
				} else if(e.getName().equals("editor")){
					if(rdBtnTextAnal.isSelected())
						e.setText("0");
					else
						e.setText("1");
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
					
				} else if(e.getName().equals("editor")){
					if(e.getValue().equals("0"))
						rdBtnTextAnal.setSelected(true);
					else
						rdbtnNotepad.setSelected(true);
					
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
	
	public String getRootPath() {
		
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) fileTree.getFileTree().getLastSelectedPathComponent();
		File file = (File) node.getUserObject();
		
		if (!file.isDirectory()) {
			node = (DefaultMutableTreeNode) node.getParent();
			file = (File) node.getUserObject();
		}
		
		return file.getAbsolutePath();

	}
	
	public void setResultsText(String text){
		textPane.setText(text);
		textPane.setCaretPosition(0);
	}
	
	public void setTitle(String text){
		lblTitle.setText(text);
	}
	
	public JRadioButton getNotepad(){
		return rdbtnNotepad;
	}
	
	public JTextPane getTextPane(){
		return textPane;
	}
	
}
