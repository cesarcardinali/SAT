package Panes;

import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.UndoManager;

import org.apache.commons.io.FileUtils;
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
import Supportive.ColorPrinter;
import javax.swing.JTabbedPane;
import javax.swing.JSeparator;

public class NewParserPane extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	static DefaultTreeModel TreeModel;
	DefaultMutableTreeNode RootNode;
	JTree tree;
	
	BatTracer BaseWindow;
	String crPath, rootPath, result;
	
	JLabel lblTitle;
	JTextPane textPane;
	JRadioButton rdBtnTextAnal;
	JRadioButton rdbtnNotepad;
	JButton btnSuspicious;
	JButton btnHighconsumeApps;
	JButton btnAlarmsOverhead;
	JButton btnDiag;
	JButton btnBug2go;
	JButton btnTethering;
	JButton btnIssues;
	JButton btnSummary;
	private JButton btnNewButton;
	private UndoManager undoManager;
	private JTabbedPane esquerda;
	private JScrollPane filtersPane;
	private JPanel panel;
	private JSeparator separator;
	private JButton btnCustom1;
	private JButton btnCustom2;
	private JButton btnCustom3;
	private JButton btnCustom4;
	private JButton btnCustom;
	private JButton btnAdd;
	private JButton btnRemove;
	private FileTree fileTree;

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
		
		btnNewButton = new JButton("Copy to Clipboard");
		btnNewButton.setToolTipText("Copy text below to clipboard");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				StringSelection stringSelection = new StringSelection(textPane.getText());
				Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
				clpbrd.setContents(stringSelection, null);
			}
		});
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.gridx = 2;
		gbc_btnNewButton.gridy = 0;
		topright.add(btnNewButton, gbc_btnNewButton);
		
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
		

		fileTree = new FileTree(BaseWindow);
		esquerda.addTab("FileTree", null, fileTree, null);
		
		
		
		
		
		filtersPane = new JScrollPane();
		filtersPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		filtersPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		esquerda.addTab("Filters", null, filtersPane, null);
		
		panel = new JPanel();
		panel.setBackground(Color.WHITE);
		filtersPane.setViewportView(panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{100, 100, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panel.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		
		btnSummary = new JButton("Summary");
		btnSummary.setMaximumSize(new Dimension(200, 23));
		btnSummary.setMinimumSize(new Dimension(150, 23));
		btnSummary.setPreferredSize(new Dimension(150, 23));
		GridBagConstraints gbc_btnSummary = new GridBagConstraints();
		gbc_btnSummary.gridwidth = 2;
		gbc_btnSummary.insets = new Insets(5, 0, 5, 0);
		gbc_btnSummary.gridx = 0;
		gbc_btnSummary.gridy = 0;
		panel.add(btnSummary, gbc_btnSummary);
		btnSummary.setToolTipText("Summarize the current drain overall status");
		
		
		btnIssues = new JButton("General Issues");
		btnIssues.setMaximumSize(new Dimension(200, 23));
		btnIssues.setMinimumSize(new Dimension(150, 23));
		btnIssues.setPreferredSize(new Dimension(150, 23));
		GridBagConstraints gbc_btnIssues = new GridBagConstraints();
		gbc_btnIssues.gridwidth = 2;
		gbc_btnIssues.insets = new Insets(0, 0, 5, 0);
		gbc_btnIssues.gridx = 0;
		gbc_btnIssues.gridy = 1;
		panel.add(btnIssues, gbc_btnIssues);
		btnIssues.setToolTipText("Shows issues as kernel wakelocks and high current drain level");
		
		
		btnSuspicious = new JButton("Suspicious");
		btnSuspicious.setMaximumSize(new Dimension(200, 23));
		btnSuspicious.setMinimumSize(new Dimension(150, 23));
		btnSuspicious.setPreferredSize(new Dimension(150, 23));
		GridBagConstraints gbc_btnSuspicious = new GridBagConstraints();
		gbc_btnSuspicious.gridwidth = 2;
		gbc_btnSuspicious.insets = new Insets(0, 0, 5, 0);
		gbc_btnSuspicious.gridx = 0;
		gbc_btnSuspicious.gridy = 2;
		panel.add(btnSuspicious, gbc_btnSuspicious);
		btnSuspicious.setToolTipText("Shows suspicioues wakelocks");
		
		btnHighconsumeApps = new JButton("HighConsume Apps");
		btnHighconsumeApps.setMaximumSize(new Dimension(200, 23));
		btnHighconsumeApps.setMinimumSize(new Dimension(150, 23));
		btnHighconsumeApps.setPreferredSize(new Dimension(150, 23));
		GridBagConstraints gbc_btnHighconsumeApps = new GridBagConstraints();
		gbc_btnHighconsumeApps.gridwidth = 2;
		gbc_btnHighconsumeApps.insets = new Insets(0, 0, 5, 0);
		gbc_btnHighconsumeApps.gridx = 0;
		gbc_btnHighconsumeApps.gridy = 3;
		panel.add(btnHighconsumeApps, gbc_btnHighconsumeApps);
		btnHighconsumeApps.setToolTipText("Shows the most frequent processes and their CPU consumption");
		
		btnAlarmsOverhead = new JButton("Alarms Overhead");
		btnAlarmsOverhead.setMaximumSize(new Dimension(200, 23));
		btnAlarmsOverhead.setMinimumSize(new Dimension(150, 23));
		btnAlarmsOverhead.setPreferredSize(new Dimension(150, 23));
		GridBagConstraints gbc_btnAlarmsOverhead = new GridBagConstraints();
		gbc_btnAlarmsOverhead.gridwidth = 2;
		gbc_btnAlarmsOverhead.insets = new Insets(0, 0, 5, 0);
		gbc_btnAlarmsOverhead.gridx = 0;
		gbc_btnAlarmsOverhead.gridy = 4;
		panel.add(btnAlarmsOverhead, gbc_btnAlarmsOverhead);
		btnAlarmsOverhead.setToolTipText("Shows processes that wake up AP more frequently");
		
		btnDiag = new JButton("Diag");
		btnDiag.setMaximumSize(new Dimension(200, 23));
		btnDiag.setMinimumSize(new Dimension(150, 23));
		btnDiag.setPreferredSize(new Dimension(150, 23));
		GridBagConstraints gbc_btnDiag = new GridBagConstraints();
		gbc_btnDiag.gridwidth = 2;
		gbc_btnDiag.insets = new Insets(0, 0, 5, 0);
		gbc_btnDiag.gridx = 0;
		gbc_btnDiag.gridy = 5;
		panel.add(btnDiag, gbc_btnDiag);
		btnDiag.setToolTipText("Shows info about DIAG_WS wakelock");
		
		btnBug2go = new JButton("Bug2Go");
		btnBug2go.setMaximumSize(new Dimension(200, 23));
		btnBug2go.setMinimumSize(new Dimension(150, 23));
		btnBug2go.setPreferredSize(new Dimension(150, 23));
		GridBagConstraints gbc_btnBug2go = new GridBagConstraints();
		gbc_btnBug2go.gridwidth = 2;
		gbc_btnBug2go.insets = new Insets(0, 0, 5, 0);
		gbc_btnBug2go.gridx = 0;
		gbc_btnBug2go.gridy = 6;
		panel.add(btnBug2go, gbc_btnBug2go);
		btnBug2go.setToolTipText("Shows info about Bug2Go process and uploads");
		
		btnTethering = new JButton("Tethering");
		btnTethering.setMaximumSize(new Dimension(200, 23));
		btnTethering.setMinimumSize(new Dimension(150, 23));
		btnTethering.setPreferredSize(new Dimension(150, 23));
		GridBagConstraints gbc_btnTethering = new GridBagConstraints();
		gbc_btnTethering.gridwidth = 2;
		gbc_btnTethering.insets = new Insets(0, 0, 5, 0);
		gbc_btnTethering.gridx = 0;
		gbc_btnTethering.gridy = 7;
		panel.add(btnTethering, gbc_btnTethering);
		btnTethering.setToolTipText("Shows info about Tethering usage");
		
		separator = new JSeparator();
		separator.setForeground(Color.DARK_GRAY);
		separator.setBackground(Color.BLACK);
		GridBagConstraints gbc_separator = new GridBagConstraints();
		gbc_separator.insets = new Insets(0, 0, 5, 0);
		gbc_separator.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator.gridwidth = 2;
		gbc_separator.gridx = 0;
		gbc_separator.gridy = 8;
		panel.add(separator, gbc_separator);
		
		btnCustom1 = new JButton("Custom 1");
		btnCustom1.setVisible(false);
		btnCustom1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
			}
		});
		
		btnAdd = new JButton("Add");
		btnAdd.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnAdd.setForeground(new Color(34, 139, 34));
		btnAdd.setMaximumSize(new Dimension(70, 23));
		btnAdd.setMinimumSize(new Dimension(65, 23));
		btnAdd.setPreferredSize(new Dimension(70, 23));
		GridBagConstraints gbc_btnAdd = new GridBagConstraints();
		gbc_btnAdd.anchor = GridBagConstraints.EAST;
		gbc_btnAdd.insets = new Insets(0, 0, 5, 5);
		gbc_btnAdd.gridx = 0;
		gbc_btnAdd.gridy = 9;
		panel.add(btnAdd, gbc_btnAdd);
		
		btnRemove = new JButton("Remove");
		btnRemove.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnRemove.setForeground(new Color(220, 20, 60));
		btnRemove.setMaximumSize(new Dimension(70, 23));
		btnRemove.setMinimumSize(new Dimension(65, 23));
		btnRemove.setMargin(new Insets(2, 5, 2, 5));
		btnRemove.setPreferredSize(new Dimension(70, 23));
		GridBagConstraints gbc_btnRemove = new GridBagConstraints();
		gbc_btnRemove.anchor = GridBagConstraints.WEST;
		gbc_btnRemove.insets = new Insets(0, 5, 5, 0);
		gbc_btnRemove.gridx = 1;
		gbc_btnRemove.gridy = 9;
		panel.add(btnRemove, gbc_btnRemove);
		btnCustom1.setMaximumSize(new Dimension(200, 23));
		btnCustom1.setMinimumSize(new Dimension(150, 23));
		btnCustom1.setPreferredSize(new Dimension(150, 23));
		GridBagConstraints gbc_btnCustom1 = new GridBagConstraints();
		gbc_btnCustom1.insets = new Insets(0, 0, 5, 0);
		gbc_btnCustom1.gridwidth = 2;
		gbc_btnCustom1.gridx = 0;
		gbc_btnCustom1.gridy = 10;
		panel.add(btnCustom1, gbc_btnCustom1);
		
		btnCustom2 = new JButton("Custom2");
		btnCustom2.setVisible(false);
		btnCustom2.setMaximumSize(new Dimension(200, 23));
		btnCustom2.setMinimumSize(new Dimension(150, 23));
		btnCustom2.setPreferredSize(new Dimension(150, 23));
		GridBagConstraints gbc_btnCustom2 = new GridBagConstraints();
		gbc_btnCustom2.insets = new Insets(0, 0, 5, 0);
		gbc_btnCustom2.gridwidth = 2;
		gbc_btnCustom2.gridx = 0;
		gbc_btnCustom2.gridy = 11;
		panel.add(btnCustom2, gbc_btnCustom2);
		
		btnCustom3 = new JButton("Custom 3");
		btnCustom3.setVisible(false);
		btnCustom3.setMaximumSize(new Dimension(200, 23));
		btnCustom3.setMinimumSize(new Dimension(150, 23));
		btnCustom3.setPreferredSize(new Dimension(150, 23));
		GridBagConstraints gbc_btnCustom3 = new GridBagConstraints();
		gbc_btnCustom3.insets = new Insets(0, 0, 5, 0);
		gbc_btnCustom3.gridwidth = 2;
		gbc_btnCustom3.gridx = 0;
		gbc_btnCustom3.gridy = 12;
		panel.add(btnCustom3, gbc_btnCustom3);
		
		btnCustom4 = new JButton("Custom 4");
		btnCustom4.setVisible(false);
		btnCustom4.setMaximumSize(new Dimension(200, 23));
		btnCustom4.setMinimumSize(new Dimension(150, 23));
		btnCustom4.setPreferredSize(new Dimension(150, 23));
		GridBagConstraints gbc_btnCustom4 = new GridBagConstraints();
		gbc_btnCustom4.insets = new Insets(0, 0, 5, 0);
		gbc_btnCustom4.gridwidth = 2;
		gbc_btnCustom4.gridx = 0;
		gbc_btnCustom4.gridy = 13;
		panel.add(btnCustom4, gbc_btnCustom4);
		
		btnCustom = new JButton("Custom 5");
		btnCustom.setVisible(false);
		btnCustom.setMaximumSize(new Dimension(200, 23));
		btnCustom.setMinimumSize(new Dimension(150, 23));
		btnCustom.setPreferredSize(new Dimension(150, 23));
		GridBagConstraints gbc_btnCustom = new GridBagConstraints();
		gbc_btnCustom.gridwidth = 2;
		gbc_btnCustom.gridx = 0;
		gbc_btnCustom.gridy = 14;
		panel.add(btnCustom, gbc_btnCustom);
		
		JScrollPane resultsPane = new JScrollPane();
		esquerda.addTab("Results", null, resultsPane, null);
		resultsPane.setToolTipText("List of parser results");
		resultsPane.setFont(new Font("Consolas", Font.PLAIN, 12));
		resultsPane.setBorder(new LineBorder(UIManager.getColor("Button.light")));
		resultsPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		resultsPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		resultsPane.setPreferredSize(new Dimension(250, 600));
		resultsPane.setMinimumSize(new Dimension(150, 500));
		resultsPane.setMaximumSize(new Dimension(250, 32767));
		
		tree = new JTree();
		tree.setToolTipText("List of parser results");
		tree.setFont(new Font("Consolas", Font.PLAIN, 11));
		tree.setModel(new DefaultTreeModel(
			new DefaultMutableTreeNode("Complete Results:") {
				private static final long serialVersionUID = 1L;
			}
		));
		TreeModel = (DefaultTreeModel) tree.getModel();
		RootNode = (DefaultMutableTreeNode) TreeModel.getRoot();
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent arg0) {
				nodeChangedAction();
			}
		});
		tree.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent arg0) {
				nodeChangedAction();
			}
		});
		resultsPane.setViewportView(tree);
		

		
		btnTethering.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(findNode("Tethering") == null) {
					btnTethering.setEnabled(false);
					Thread susp = new Thread() {
					    public void run() {
					    	try {
						        System.out.println("Tethering thread running");
		
						        
						        Tether.makeLog(crPath, BaseWindow);
								addTether("Tethering Info");
								btnTethering.setEnabled(true);
								result = (result + "\n\n\n======================== Wifi Tether =========================\n" + Tether.getResult());
								
								
								System.out.println("Tethering thread finished");
					    	} catch (Exception e) {
								e.printStackTrace();
								System.out.println("Tethering thread error");
								btnTethering.setEnabled(true);
								
							}
					    }  
					};
					susp.start();
				}
			}
		});
		btnBug2go.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(findNode("Bug2Go") == null) {
					btnBug2go.setEnabled(false);
					Thread susp = new Thread() {
					    public void run() {
					    	try {
						        System.out.println("Bug2go thread running");
		
						        
						        B2G.makelog(crPath);
								addBug2go("Bug2Go Info");
								btnBug2go.setEnabled(true);
								result = (result + "\n\n\n========================= Bug2Go =========================\n" + B2G.getResult());
								
								
								System.out.println("Bug2go thread finished");
					    	} catch (Exception e) {
								e.printStackTrace();
								System.out.println("Bug2go thread error");
								btnBug2go.setEnabled(true);
								
							}
					    }  
					};
					susp.start();
				}
			}
		});
		btnDiag.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(findNode("Diag") == null) {
					btnDiag.setEnabled(false);
					Thread susp = new Thread() {
					    public void run() {
					    	try {
						        System.out.println("Diag thread running");
		
						        
						        String SDiag = Diag.makelog(crPath, BaseWindow);
								if(SDiag.split("\n").length > 2)
									addDiag("Diag Wake Lock");
								else
									addDiag("Not a Diag issue");
								btnDiag.setEnabled(true);
								result = (result + "\n\n\n======================= Diag Wake locks =======================\n" + Diag.getResult());
								
								
								System.out.println("Diag thread finished");
					    	} catch (Exception e) {
								e.printStackTrace();
								System.out.println("Diag thread error");
								btnDiag.setEnabled(true);
								
							}
					    }  
					};
					susp.start();
				} else {
					btnDiag.setEnabled(false);
					Thread susp = new Thread() {
					    public void run() {
					    	try {
						        System.out.println("Diag thread running");
		
						        
						        Diag.makelog(crPath, BaseWindow);
								btnDiag.setEnabled(true);
								//result = (result + "\n\n\n======================= Diag Wake locks =======================\n" + Diag.getResult());
								
								
								System.out.println("Diag thread finished");
					    	} catch (Exception e) {
								e.printStackTrace();
								System.out.println("Diag thread error");
								btnDiag.setEnabled(true);
								
							}
					    }  
					};
					susp.start();
				}
			}
		});
		btnAlarmsOverhead.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(findNode("Alarms Overhead") == null) {
					btnAlarmsOverhead.setEnabled(false);
					Thread susp = new Thread() {
					    public void run() {
					    	try {
						        System.out.println("Alarms thread running");
		
						        
						        Alarm.makelog(crPath, BaseWindow);
								for (int i = 0; i < Alarm.getListSize(); i++) {
									addAlarms(Alarm.getList().get(i).getProcess());
								}
								if(Alarm.getListSize() == 0)
								addAlarms("Nothing found in the logs");
								
								btnAlarmsOverhead.setEnabled(true);
								result = (result + "\n\n\n======================= Alarms Resume =======================\n" + Alarm.getResult());
								
								
								System.out.println("Alarms thread finished");
					    	} catch (Exception e) {
								e.printStackTrace();
								System.out.println("Alarms thread error");
								btnAlarmsOverhead.setEnabled(true);
								
							}
					    }  
					};
					susp.start();
				}
			}
		});
		btnHighconsumeApps.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(findNode("High Consume") == null) {
					btnHighconsumeApps.setEnabled(false);
					Thread susp = new Thread() {
					    public void run() {
					    	try{
						        System.out.println("High consume thread running");
		
						        
						        Consume.makelog(crPath);
								for (int i = 0; i < Consume.getHCList().size(); i++) {
									addConsumeNode(Consume.getHCList().get(i).getProcess());
								}
								if(Consume.getHCList().size() == 0)
									addConsumeNode("Nothing found in logs");
								btnHighconsumeApps.setEnabled(true);
								result = (result + "\n\n\n======================= High consume =======================\n" + Consume.getResult());
								
								
								System.out.println("High consume thread finished");
					    	} catch (Exception e) {
								e.printStackTrace();
								System.out.println("High consume thread error");
								btnHighconsumeApps.setEnabled(true);
								
							}
					    }  
					};
					susp.start();
				}
			}
		});
		btnSuspicious.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(findNode("Suspicious") == null) {
					btnSuspicious.setEnabled(false);
					Thread susp = new Thread() {
					    public void run() {
							try {
								System.out.println("Suspicius thread running");
	
								
								Suspicious.makelog(crPath, BaseWindow);
								addIssues();
								if (Suspicious.getWakeLocks().size() != 0)
									for (int i = 0; i < Suspicious.getWakeLocks().size(); i++) {
										addWakeLocksNode(
												Suspicious.getWakeLocks().get(i).getProcess()+ " - "+ 
												Suspicious.getWakeLocks().get(i).getTag() + " - " +
												Suspicious.getWakeLocks().get(i).getDuration());
									}
								else{
									addWakeLocksNode("No suspicious found");
								}
								btnSuspicious.setEnabled(true);
								result = (
										result + "\n\n\n======================== Wake locks ========================\n"
										+ Suspicious.getResult());
								
	
								System.out.println("Suspicius thread finished");
							} catch (Exception e) {
								e.printStackTrace();
								System.out.println("Suspicius thread error");
								btnSuspicious.setEnabled(true);
								
							}
					    }  
					};
					susp.start();
				}
			}
		});
		btnIssues.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(findNode("General Issues") == null) {
					btnIssues.setEnabled(false);
					Thread susp = new Thread() {
					    public void run() {
							try {
								System.out.println("General thread running");
	
								
								Issue.makelog(crPath, BaseWindow);
								addIssues();
								btnIssues.setEnabled(true);
								result = (result
										+ "\n\n\n==================== General Issues ======================\n" + Issue
										.getResult());
								
	
								System.out.println("General thread finished");
							} catch (Exception e) {
								e.printStackTrace();
								System.out.println("General thread error");
								btnIssues.setEnabled(true);
								
							}
					    }
					};
					susp.start();
				}
			}
		});
		btnSummary.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(findNode("Summary") == null) {
					btnSummary.setEnabled(false);
					Thread susp = new Thread() {
					    public void run() {
							try {
								System.out.println("Normal thread running");
								
								Normal.makeLog(crPath);
								addSummary();
								btnSummary.setEnabled(true);
								result = (result
										+ "\n\n\n================== Battery Discharge Summary ====================\n" + Normal
										.getResult());
								
	
								System.out.println("Normal thread finished");
							} catch (Exception e) {
								e.printStackTrace();
								System.out.println("Normal thread error");
								btnSummary.setEnabled(true);
								
							}
					    }  
					};
					susp.start();
				}
			}
		});
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.anchor = GridBagConstraints.NORTHWEST;
		gbc_scrollPane.insets = new Insets(2, 10, 10, 10);
		gbc_scrollPane.weighty = 22.0;
		gbc_scrollPane.weightx = 15.0;
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 1;
		gbc_scrollPane.gridy = 2;
		add(scrollPane, gbc_scrollPane);
		
		textPane = new JTextPane();
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
			public void keyReleased(KeyEvent arg0) {}

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
		clearTree();
		result = "";
		textPane.setText("");
		lblTitle.setText("Run a parser or select a result on the left");
		loadPaneData();
	}


	
	
	
	
	//Node functions
	public void addConsumeNode(String node){
		DefaultMutableTreeNode HCNode = findNode("High Consume");
		if(HCNode == null){
			addNodeRoot("Apps with High Consume");
			HCNode = findNode("High Consume");
			TreeModel.reload();
		}
		DefaultMutableTreeNode HCitem = new DefaultMutableTreeNode(node);
		HCitem.add(new DefaultMutableTreeNode("Screen ON"));
		HCitem.add(new DefaultMutableTreeNode("Screen OFF"));
		HCitem.add(new DefaultMutableTreeNode("Full Log"));
		HCitem.add(new DefaultMutableTreeNode("On Colors"));
		HCNode.insert(HCitem, HCNode.getChildCount());
		TreeModel.reload();
	}
	
	
	public void addWakeLocksNode(String node){
		DefaultMutableTreeNode WLNode = findNode("Suspicious Wake Locks");
		if(WLNode == null){
			addNodeRoot("Suspicious Wake Locks");
			WLNode = findNode("Suspicious Wake Locks");
			TreeModel.reload();
		}
		DefaultMutableTreeNode WLitem = new DefaultMutableTreeNode(node);
		WLitem.add(new DefaultMutableTreeNode("Formatted"));
		WLNode.insert(WLitem, WLNode.getChildCount());
		TreeModel.reload();
	}
	
	
	public void addIssues(){
		DefaultMutableTreeNode INode = findNode("General Issues");
		if(INode == null){
			addNodeRoot("General Issues");
			INode = findNode("General Issues");
			DefaultMutableTreeNode Iitem = new DefaultMutableTreeNode("");
			INode.insert(Iitem, 0);
			TreeModel.reload();
		}
	}
	
	
	public void addBug2go(String node){
		DefaultMutableTreeNode BGNode = findNode("Bug2Go");
		if(BGNode == null){
			addNodeRoot("Bug2Go Info");
			BGNode = findNode("Bug2Go");
			TreeModel.reload();
		}
		BGNode.insert(new DefaultMutableTreeNode(""), BGNode.getChildCount());
		TreeModel.reload();
	}
	
	public void addTether(String node){
		DefaultMutableTreeNode TNode = findNode("Tether");
		if(TNode == null){
			addNodeRoot("Tethering Info");
			TNode = findNode("Tether");
			TreeModel.reload();
		}
		if (Tether.getResult().contains("No tethering evidences were found in text logs"))
			TNode.insert(new DefaultMutableTreeNode("No tethering activity found"), TNode.getChildCount());
		else
			TNode.insert(new DefaultMutableTreeNode("Log result"), TNode.getChildCount());
		
		TreeModel.reload();
	}
	
	public void addDiag(String node){
		DefaultMutableTreeNode TNode = findNode("Diag");
		if(TNode == null){
			addNodeRoot(node);
			TNode = findNode("Diag");
			TreeModel.reload();
		}
		TNode.insert(new DefaultMutableTreeNode(""), TNode.getChildCount());
		TreeModel.reload();
	}
	
	
	public void addAlarms(String node){
		DefaultMutableTreeNode ANode = findNode("Alarms Overhead");
		if(ANode == null){
			addNodeRoot("Alarms Overhead");
			ANode = findNode("Alarms Overhead");
			TreeModel.reload();
		}
		DefaultMutableTreeNode Aitem = new DefaultMutableTreeNode(node);
		Aitem.add(new DefaultMutableTreeNode("On Colors"));
		ANode.insert(Aitem, ANode.getChildCount());
		TreeModel.reload();
	}
	
	
	public void addSummary(){
		DefaultMutableTreeNode TNode = findNode("Discharge Summary");
		if(TNode == null){
			addNodeRoot("Discharge Summary");
			TNode = findNode("Discharge Summary");
			TreeModel.reload();
		}
		TNode.insert(new DefaultMutableTreeNode(""), TNode.getChildCount());
		TreeModel.reload();
	}
	


	public void addNodeRoot(String node){
		if(node.contains("Alarm")){
			DefaultMutableTreeNode newNode =  new DefaultMutableTreeNode(node);
			newNode.add(new DefaultMutableTreeNode("On Colors"));
			RootNode.insert(newNode, 0);
			TreeModel.reload();
		} else if(node.contains("Consum")){
			DefaultMutableTreeNode newNode =  new DefaultMutableTreeNode(node);
			newNode.add(new DefaultMutableTreeNode("On Colors"));
			RootNode.insert(newNode, 0);
			TreeModel.reload();
		} else {
			RootNode.insert(new DefaultMutableTreeNode(node), 0);
			TreeModel.reload();
		}
	}
	
	public DefaultMutableTreeNode findNode(String Node){
		int Length = RootNode.getChildCount();
		for(int i=0; i < Length; i++)
		{
			if(RootNode.getChildAt(i).toString().contains(Node)){
				return (DefaultMutableTreeNode) RootNode.getChildAt(i);
			}
			//textPane.setText(textPane.getText() + "\n" + RootNode.getChildAt(i));
		}	
		return null;
	}
	
	public int findNodeIndex(DefaultMutableTreeNode Node){
		return RootNode.getIndex(Node);
	}
	
	
	public void clearTree(){
		RootNode = (DefaultMutableTreeNode) TreeModel.getRoot();
		RootNode.removeAllChildren();
		TreeModel.reload();
	}
	
	
	
	private void nodeChangedAction() {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
		textPane.setCaretPosition(0);
		if(node != null)
		{				
			if(node.isRoot()){
				lblTitle.setText("Complete Results:");
				textPane.setText(result);
				textPane.setCaretPosition(0);
				return;
			}
			
			//Consume tree configuration
			if(node.toString().contains("High Consume")){
				lblTitle.setText("High Consume Apps:");
				textPane.setText(Consume.getResult());
				textPane.setCaretPosition(0);
				return;
			}
			if(node.getParent().toString().contains("High Consume")){
				if(node.toString().contains("On Colors")){
					lblTitle.setText("High Consume Apps:");
					ColorPrinter.colorsApps(textPane, Consume.getResult());
					textPane.setCaretPosition(0);
					return;
				} else {
					lblTitle.setText(Consume.getHCList().get(node.getParent().getIndex(node)-1).getProcess());
					textPane.setText("{panel}\n" + Consume.getHCList().get(node.getParent().getIndex(node)-1).toString() + "{panel}");
					textPane.setCaretPosition(0);
					return;
				}
			}
			if(node.getParent().getParent() != null){
				if(node.getParent().getParent().toString().contains("High Consume")){
					lblTitle.setText(Consume.getHCList().get(node.getParent().getParent().getIndex(node.getParent())-1).getProcess());
					if (node.toString().contains("ON"))
						textPane.setText(BaseWindow.getOptions().getTextConsumeOn()
								.replaceAll("#pname#",Consume.getHCList().get(node.getParent().getParent().getIndex(node.getParent())-1).getProcess())
								.replaceAll("#sconconsume#",String.valueOf(Consume.getHCList().get(node.getParent().getParent().getIndex(node.getParent())-1).getScOnConsume()))
								.replaceAll("#logon#", Consume.getHCList().get(node.getParent().getParent().getIndex(node.getParent())-1).getLogOn())
								.replace("\\n", "\n"));
					else 
						if (node.toString().contains("OFF"))
						textPane.setText(BaseWindow.getOptions().getTextConsumeOff()
								.replaceAll("#pname#",Consume.getHCList().get(node.getParent().getParent().getIndex(node.getParent())-1).getProcess())
								.replaceAll("#scoffconsume#",String.valueOf(Consume.getHCList().get(node.getParent().getParent().getIndex(node.getParent())-1).getScOffConsume()))
								.replaceAll("#logoff#", Consume.getHCList().get(node.getParent().getParent().getIndex(node.getParent())-1).getLogOff())
								.replace("\\n", "\n"));
					else
						if (node.toString().contains("Full"))
						textPane.setText(BaseWindow.getOptions().getTextConsumeFull()
								.replaceAll("#pname#",Consume.getHCList().get(node.getParent().getParent().getIndex(node.getParent())-1).getProcess())
								.replaceAll("#avgconsume#",String.valueOf(Consume.getHCList().get(node.getParent().getParent().getIndex(node.getParent())-1).getConsumeAvg()))
								.replaceAll("#logfull#", Consume.getHCList().get(node.getParent().getParent().getIndex(node.getParent())-1).getLog())
								.replace("\\n", "\n"));
					else
						ColorPrinter.colorsApps(textPane, BaseWindow.getOptions().getTextConsumeFull()
								.replaceAll("#pname#",Consume.getHCList().get(node.getParent().getParent().getIndex(node.getParent())-1).getProcess())
								.replaceAll("#avgconsume#",String.valueOf(Consume.getHCList().get(node.getParent().getParent().getIndex(node.getParent())-1).getConsumeAvg()))
								.replaceAll("#logfull#", Consume.getHCList().get(node.getParent().getParent().getIndex(node.getParent())-1).getLog())
								.replace("\\n", "\n"));
					
					textPane.setCaretPosition(0);
					return;
				}
			}
			
			
			//Suspicious tree configuration
			if(node.toString().contains("Wake Locks")){
				lblTitle.setText("Wake lock issues:");
				textPane.setText(Suspicious.getResult());
				textPane.setCaretPosition(0);
				return;
			}
			if(node.getParent().toString().contains("Wake Locks")){
				lblTitle.setText(Suspicious.getWakeLocks().get(node.getParent().getIndex(node)).getProcess());
				textPane.setText(Suspicious.getWakeLocks().get(node.getParent().getIndex(node)).toString());
				textPane.setCaretPosition(0);
				return;
			}
			if(node.getParent().getParent() != null){
				if(node.getParent().getParent().toString().contains("Wake Locks")){
					lblTitle.setText(Suspicious.getWakeLocks().get(node.getParent().getParent().getIndex(node.getParent())).getProcess());
					textPane.setText("{panel}\n" + Suspicious.getWakeLocks().get(node.getParent().getParent().getIndex(node.getParent())).toString() + "\n{panel}");
					//ColorPrinter.printcolors(textPane, Suspicious.getWakeLocks().get(node.getParent().getParent().getIndex(node.getParent())).toString());
					textPane.setCaretPosition(0);
					return;
				}
			}
			
			
			//Alarms tree configuration
			if(node.toString().contains("Alarms Overhead")){
				lblTitle.setText("Alarms Overhead Issues:");
				textPane.setText(Alarm.getResult());
				textPane.setCaretPosition(0);
				return;
			}
			if(node.getParent().toString().contains("Alarms Overhead")){
				if(node.toString().contains("On Colors")){
					lblTitle.setText("Alarms Overhead Issues:");
					ColorPrinter.colorsAlarm(textPane, Alarm.getResult());
					textPane.setCaretPosition(0);
					return;
				} else {
					lblTitle.setText(node.toString() + " - " + Alarm.getList().get(node.getParent().getIndex(node)-1).getAction());
					textPane.setText(BaseWindow.getOptions().getTextAlarms()
							.replace("#pname#", Alarm.getList().get(node.getParent().getIndex(node)-1).getProcess())
							.replace("#log#", Alarm.getList().get(node.getParent().getIndex(node)-1).toString())
							.replace("\\n", "\n"));
					textPane.setCaretPosition(0);
					return;
				}
			}
			if(node.getParent().getParent() != null){
				if(node.getParent().getParent().toString().contains("Alarms")){
					lblTitle.setText(node.getParent().toString() + " - " + Alarm.getList().get(node.getParent().getParent().getIndex(node.getParent())-1).getAction());
					ColorPrinter.colorsAlarm(textPane, 
							BaseWindow.getOptions().getTextAlarms()
							.replace("#pname#", Alarm.getList().get(node.getParent().getParent().getIndex(node.getParent()) - 1).getProcess())
							.replace("#log#", Alarm.getList().get(node.getParent().getParent().getIndex(node.getParent()) - 1).toString())
							.replace("\\n", "\n"));
					textPane.setCaretPosition(0);
					return;
				}
			}
			
			
			//B2G tree configuration
			if(node.toString().contains("Bug2Go")){
				lblTitle.setText("Bug2Go Summary:");
				if (B2G.getResult().contains("No B2G evidences were found in text logs")) {
					textPane.setText(B2G.getResult()
							.replace("\\n", "\n"));
					textPane.setCaretPosition(0);
				}
				
				else {
					textPane.setText(BaseWindow.getOptions().getTextB2g()
							.replaceAll("#log#", B2G.getResult())
							.replace("\\n", "\n"));
					textPane.setCaretPosition(0);
				}
				
				return;
			}
			
			if(node.getParent().toString().contains("Bug2Go")){
				lblTitle.setText("Bug2Go Summary:");
				
				if (B2G.getResult().contains("No B2G evidences were found in text logs")) {
					textPane.setText(B2G.getResult()
							.replace("\\n", "\n"));
					textPane.setCaretPosition(0);
				}
				
				else {
					textPane.setText(BaseWindow.getOptions().getTextB2g()
							.replaceAll("#log#", B2G.getResult())
							.replace("\\n", "\n"));
					textPane.setCaretPosition(0);
				}
				
				return;
			}				
			
			
			//Tether tree configuration
			if(node.toString().contains("Tether")){
				lblTitle.setText("Wifi Tethering Summary:");
				textPane.setText(Tether.getResult());
				textPane.setCaretPosition(0);
				return;
			}
			if(node.getParent().toString().contains("Tether")){
				lblTitle.setText("Wifi Tethering Summary:");
				textPane.setText(Tether.getResult());
				textPane.setCaretPosition(0);
				return;
			}
			
			
			//Diag tree configuration
			if(node.toString().contains("Diag")){
				lblTitle.setText("Diag Wake Lock");
				textPane.setText(Diag.getResult());
				textPane.setCaretPosition(0);
				return;
			}
			if(node.getParent().toString().contains("Diag")){
				lblTitle.setText("Diag Wake Lock");
				textPane.setText(Diag.getResult());
				textPane.setCaretPosition(0);
				return;
			}
			
			
			//General Issues tree configuration
			if(node.toString().contains("General Issues")){
				lblTitle.setText("General Issues Summary:");
				textPane.setText(Issue.getResult());
				textPane.setCaretPosition(0);
				return;
			}
			if(node.getParent().toString().contains("General Issues")){
				lblTitle.setText("General Issues Summary:");
				textPane.setText(Issue.getResult());
				textPane.setCaretPosition(0);
				return;
			}
			
			//Discharge Summary tree configuration
			if(node.toString().contains("Discharge Summary")){
				lblTitle.setText("Battery Discharge Summary:");
				textPane.setText(Normal.getResult());
				textPane.setCaretPosition(0);
				return;
			}
			if(node.getParent().toString().contains("Discharge Summary") && !lblTitle.getText().contains("Discharge Summary")){
				lblTitle.setText("Battery Discharge Summary:");
				textPane.setText(Normal.getResult());
				textPane.setCaretPosition(0);
				return;
			}
		}
	}
	
	
	/**
	 * Supportive functions
	 */
	public void saveState(){
		
	}
	
	public BatTracer getBaseWindow(){
		return BaseWindow; 
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
	
	public JRadioButton getNotepad(){
		return rdbtnNotepad;
	}
	
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






	public String getCrPath() {
		return crPath;
	}

	public void setCrPath(String crPath) {
		this.crPath = crPath;
	}
	
	
	
}
