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
	JLabel lblCrsInThis;
	JTextPane textPane;
	JTextField folder;
	JRadioButton rdBtnTextAnal;
	JRadioButton rdbtnNotepad;
	JComboBox<String> crsList;
	JButton btnSuspicious;
	JButton btnHighconsumeApps;
	JButton btnAlarmsOverhead;
	JButton btnDiag;
	JButton btnBug2go;
	JButton btnTethering;
	JButton btnViewSystem;
	JButton btnViewKernel;
	JButton btnViewRadio;
	JButton btnViewBugreport;
	JButton btnViewReportOutput;
	JButton btnBTD;
	JButton btnIssues;
	JButton btnSummary;
	private JButton btnOpenfolder;
	private JButton btnNewButton;
	private JButton button;
	private JButton btnBack;
	private JButton btnDel;
	private JButton btnRefresh;
	private UndoManager undoManager;

	/**
	 * Create the panel.
	 */
	public NewParserPane(BatTracer Parent) {
		BaseWindow = Parent;
		setMinimumSize(new Dimension(800, 600));
		
		GridBagLayout layout = new GridBagLayout();
		layout.columnWidths = new int[]{250, 600};
		layout.rowHeights = new int[]{35, 35, 35, 86, 600};
		layout.rowWeights = new double[]{1.0, 1.0, 1.0, 1.0, 1.0};
		layout.columnWeights = new double[]{1.0, 1.0};
		setLayout(layout);
		
		JScrollPane esquerda = new JScrollPane();
		esquerda.setToolTipText("List of parser results");
		esquerda.setFont(new Font("Consolas", Font.PLAIN, 12));
		esquerda.setBorder(new LineBorder(UIManager.getColor("Button.light")));
		esquerda.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		esquerda.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		esquerda.setPreferredSize(new Dimension(250, 600));
		esquerda.setMinimumSize(new Dimension(150, 500));
		esquerda.setMaximumSize(new Dimension(250, 32767));
		JPanel topright = new JPanel();
		topright.setPreferredSize(new Dimension(10, 30));
		topright.setMaximumSize(new Dimension(32767, 31));
		topright.setBorder(new LineBorder(UIManager.getColor("Button.light")));;
		topright.setMinimumSize(new Dimension(35, 30));
		
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
		
		JPanel directoryPane = new JPanel();
		directoryPane.setPreferredSize(new Dimension(400, 30));
		directoryPane.setMinimumSize(new Dimension(700, 30));
		directoryPane.setMaximumSize(new Dimension(32767, 30));
		directoryPane.setBorder(new LineBorder(UIManager.getColor("Button.light")));
		GridBagConstraints gbc_directoryPane = new GridBagConstraints();
		gbc_directoryPane.weighty = 1.0;
		gbc_directoryPane.weightx = 1.0;
		gbc_directoryPane.gridwidth = 2;
		gbc_directoryPane.insets = new Insets(10, 10, 0, 10);
		gbc_directoryPane.fill = GridBagConstraints.HORIZONTAL;
		gbc_directoryPane.gridx = 0;
		gbc_directoryPane.gridy = 0;
		add(directoryPane, gbc_directoryPane);
		directoryPane.setLayout(new FlowLayout(FlowLayout.CENTER, 1, 1));
		
		button = new JButton("");
		button.setToolTipText("Get selected folder on the dropbox and send the path to the CRs Path");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				char[] aux = getRootPath().toCharArray();
				String path;
				if(crsList.getItemAt(crsList.getSelectedIndex()) != null){
					if(aux[aux.length-1] == '\\')
						path = getRootPath() + crsList.getItemAt(crsList.getSelectedIndex());
					else
						path = getRootPath() + "\\" + crsList.getItemAt(crsList.getSelectedIndex());
					setRootPath(path);
				}
			}
		});
		
		btnBack = new JButton("");
		btnBack.setToolTipText("Go one folder level back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String[] aux = getRootPath().split("\\\\");
				String newPath = "";
				for(int i=0; i < aux.length-1; i++){
					System.out.println(aux[i]);
					newPath = newPath + aux[i] + "\\";
				}
				setRootPath(newPath);
			}
		});
		btnBack.setMargin(new Insets(5, 4, 4, 4));
		btnBack.setPreferredSize(new Dimension(25, 23));
		btnBack.setMinimumSize(new Dimension(25, 23));
		btnBack.setMaximumSize(new Dimension(25, 23));
		btnBack.setIcon(new ImageIcon("Data\\pics\\left.png"));
		directoryPane.add(btnBack);
		button.setPreferredSize(new Dimension(23, 23));
		button.setMinimumSize(new Dimension(23, 23));
		button.setMaximumSize(new Dimension(23, 23));
		button.setMargin(new Insets(5, 5, 2, 2));
		button.setIcon(new ImageIcon("Data\\pics\\down-right-22.png"));
		directoryPane.add(button);
		
		JLabel lblCrsFolder = new JLabel("CRs Path:");
		lblCrsFolder.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblCrsFolder.setPreferredSize(new Dimension(70, 20));
		lblCrsFolder.setMinimumSize(new Dimension(70, 20));
		lblCrsFolder.setMaximumSize(new Dimension(70, 20));
		lblCrsFolder.setFont(new Font("Tahoma", Font.BOLD, 12));
		directoryPane.add(lblCrsFolder);
		
		folder = new JTextField();
		folder.setToolTipText("Path used to search for CRs folders");
		folder.setMargin(new Insets(2, 4, 2, 0));
		folder.setMaximumSize(new Dimension(600, 20));
		folder.setPreferredSize(new Dimension(600, 20));
		folder.setMinimumSize(new Dimension(600, 20));
		folder.setBorder(new LineBorder(Color.BLACK));
		folder.setText("C:\\CRs");
		directoryPane.add(folder);
		
		ButtonGroup editorSelector = new ButtonGroup();
		
		JPanel parserPane = new JPanel();
		parserPane.setBorder(new LineBorder(UIManager.getColor("Button.light")));
		parserPane.setBackground(UIManager.getColor("Button.background"));
		parserPane.setMaximumSize(new Dimension(32767, 30));
		parserPane.setMinimumSize(new Dimension(10, 30));
		parserPane.setPreferredSize(new Dimension(10, 30));
		GridBagConstraints gbc_parserPane = new GridBagConstraints();
		gbc_parserPane.weighty = 1.0;
		gbc_parserPane.weightx = 1.0;
		gbc_parserPane.gridwidth = 2;
		gbc_parserPane.insets = new Insets(0, 10, 0, 10);
		gbc_parserPane.fill = GridBagConstraints.HORIZONTAL;
		gbc_parserPane.gridx = 0;
		gbc_parserPane.gridy = 1;
		add(parserPane, gbc_parserPane);
		parserPane.setLayout(new FlowLayout(FlowLayout.CENTER, 3, 1));
		
		btnDel = new JButton("Del");
		btnDel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				delFolder( folder.getText() + "\\" + crsList.getItemAt(crsList.getSelectedIndex()) );
				updateCrList();
			}
		});
		
		btnRefresh = new JButton("");
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateCrList();
			}
		});
		btnRefresh.setPreferredSize(new Dimension(25, 23));
		btnRefresh.setMinimumSize(new Dimension(25, 23));
		btnRefresh.setMaximumSize(new Dimension(25, 23));
		btnRefresh.setIcon(new ImageIcon("Data\\pics\\Refresh-24.png"));
		btnRefresh.setMargin(new Insets(2, 2, 2, 2));
		btnRefresh.setForeground(Color.RED);
		btnRefresh.setFont(new Font("Tahoma", Font.BOLD, 10));
		parserPane.add(btnRefresh);
		btnDel.setFont(new Font("Tahoma", Font.BOLD, 10));
		btnDel.setForeground(Color.RED);
		btnDel.setMargin(new Insets(2, 2, 2, 2));
		parserPane.add(btnDel);
		
		lblCrsInThis = new JLabel("CRs in this folder:");
		parserPane.add(lblCrsInThis);
		
		crsList = new JComboBox<String>();
		crsList.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent ie) {
				if(crsList.getSelectedIndex() != ie.getID())
				{
					crPath = folder.getText().replace("\\", "\\\\").concat("\\\\") + crsList.getItemAt(crsList.getSelectedIndex()) + "\\";
					System.out.println("Parsed path: " + crPath);
					clearTree();
					textPane.setText("");
					lblTitle.setText("Run a parser or select a result on the left");
					result = "";
				}
			}
		});
		crsList.setPreferredSize(new Dimension(190, 20));
		crsList.setMinimumSize(new Dimension(180, 20));
		parserPane.add(crsList);
		
		
		btnSummary = new JButton("Summary");
		btnSummary.setToolTipText("Summarize the current drain overall status");
		btnSummary.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(findNode("Summary") == null) {
					btnSummary.setEnabled(false);
					Thread susp = new Thread() {
					    public void run() {
							try {
								System.out.println("Normal thread running");
								crsList.setEnabled(false);
								Normal.makeLog(crPath);
								addSummary();
								btnSummary.setEnabled(true);
								result = (result
										+ "\n\n\n================== Battery Discharge Summary ====================\n" + Normal
										.getResult());
								crsList.setEnabled(true);
	
								System.out.println("Normal thread finished");
							} catch (Exception e) {
								e.printStackTrace();
								System.out.println("Normal thread error");
								btnSummary.setEnabled(true);
								crsList.setEnabled(true);
							}
					    }  
					};
					susp.start();
				}
			}
		});
		parserPane.add(btnSummary);
		
		
		btnIssues = new JButton("General Issues");
		btnIssues.setToolTipText("Shows issues as kernel wakelocks and high current drain level");
		btnIssues.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(findNode("General Issues") == null) {
					btnIssues.setEnabled(false);
					Thread susp = new Thread() {
					    public void run() {
							try {
								System.out.println("General thread running");
	
								crsList.setEnabled(false);
								Issue.makelog(crPath, BaseWindow);
								addIssues();
								btnIssues.setEnabled(true);
								result = (result
										+ "\n\n\n==================== General Issues ======================\n" + Issue
										.getResult());
								crsList.setEnabled(true);
	
								System.out.println("General thread finished");
							} catch (Exception e) {
								e.printStackTrace();
								System.out.println("General thread error");
								btnIssues.setEnabled(true);
								crsList.setEnabled(true);
							}
					    }
					};
					susp.start();
				}
			}
		});
		parserPane.add(btnIssues);
		
		
		btnSuspicious = new JButton("Suspicious");
		btnSuspicious.setToolTipText("Shows suspicioues wakelocks");
		btnSuspicious.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(findNode("Suspicious") == null) {
					btnSuspicious.setEnabled(false);
					Thread susp = new Thread() {
					    public void run() {
							try {
								System.out.println("Suspicius thread running");
	
								crsList.setEnabled(false);
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
								crsList.setEnabled(true);
	
								System.out.println("Suspicius thread finished");
							} catch (Exception e) {
								e.printStackTrace();
								System.out.println("Suspicius thread error");
								btnSuspicious.setEnabled(true);
								crsList.setEnabled(true);
							}
					    }  
					};
					susp.start();
				}
			}
		});
		parserPane.add(btnSuspicious);
		
		btnHighconsumeApps = new JButton("HighConsume Apps");
		btnHighconsumeApps.setToolTipText("Shows the most frequent processes and their CPU consumption");
		btnHighconsumeApps.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(findNode("High Consume") == null) {
					btnHighconsumeApps.setEnabled(false);
					Thread susp = new Thread() {
					    public void run() {
					    	try{
						        System.out.println("High consume thread running");
		
						        crsList.setEnabled(false);
						        Consume.makelog(crPath);
								for (int i = 0; i < Consume.getHCList().size(); i++) {
									addConsumeNode(Consume.getHCList().get(i).getProcess());
								}
								if(Consume.getHCList().size() == 0)
									addConsumeNode("Nothing found in logs");
								btnHighconsumeApps.setEnabled(true);
								result = (result + "\n\n\n======================= High consume =======================\n" + Consume.getResult());
								crsList.setEnabled(true);
								
								System.out.println("High consume thread finished");
					    	} catch (Exception e) {
								e.printStackTrace();
								System.out.println("High consume thread error");
								btnHighconsumeApps.setEnabled(true);
								crsList.setEnabled(true);
							}
					    }  
					};
					susp.start();
				}
			}
		});
		parserPane.add(btnHighconsumeApps);
		
		btnAlarmsOverhead = new JButton("Alarms Overhead");
		btnAlarmsOverhead.setToolTipText("Shows processes that wake up AP more frequently");
		btnAlarmsOverhead.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(findNode("Alarms Overhead") == null) {
					btnAlarmsOverhead.setEnabled(false);
					Thread susp = new Thread() {
					    public void run() {
					    	try {
						        System.out.println("Alarms thread running");
		
						        crsList.setEnabled(false);
						        Alarm.makelog(crPath, BaseWindow);
								for (int i = 0; i < Alarm.getListSize(); i++) {
									addAlarms(Alarm.getList().get(i).getProcess());
								}
								if(Alarm.getListSize() == 0)
								addAlarms("Nothing found in the logs");
								
								btnAlarmsOverhead.setEnabled(true);
								result = (result + "\n\n\n======================= Alarms Resume =======================\n" + Alarm.getResult());
								crsList.setEnabled(true);
								
								System.out.println("Alarms thread finished");
					    	} catch (Exception e) {
								e.printStackTrace();
								System.out.println("Alarms thread error");
								btnAlarmsOverhead.setEnabled(true);
								crsList.setEnabled(true);
							}
					    }  
					};
					susp.start();
				}
			}
		});
		parserPane.add(btnAlarmsOverhead);
		
		btnDiag = new JButton("Diag");
		btnDiag.setToolTipText("Shows info about DIAG_WS wakelock");
		btnDiag.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(findNode("Diag") == null) {
					btnDiag.setEnabled(false);
					Thread susp = new Thread() {
					    public void run() {
					    	try {
						        System.out.println("Diag thread running");
		
						        crsList.setEnabled(false);
						        String SDiag = Diag.makelog(crPath, BaseWindow);
								if(SDiag.split("\n").length > 2)
									addDiag("Diag Wake Lock");
								else
									addDiag("Not a Diag issue");
								btnDiag.setEnabled(true);
								result = (result + "\n\n\n======================= Diag Wake locks =======================\n" + Diag.getResult());
								crsList.setEnabled(true);
								
								System.out.println("Diag thread finished");
					    	} catch (Exception e) {
								e.printStackTrace();
								System.out.println("Diag thread error");
								btnDiag.setEnabled(true);
								crsList.setEnabled(true);
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
		
						        crsList.setEnabled(false);
						        Diag.makelog(crPath, BaseWindow);
								btnDiag.setEnabled(true);
								//result = (result + "\n\n\n======================= Diag Wake locks =======================\n" + Diag.getResult());
								crsList.setEnabled(true);
								
								System.out.println("Diag thread finished");
					    	} catch (Exception e) {
								e.printStackTrace();
								System.out.println("Diag thread error");
								btnDiag.setEnabled(true);
								crsList.setEnabled(true);
							}
					    }  
					};
					susp.start();
				}
			}
		});
		parserPane.add(btnDiag);
		
		btnBug2go = new JButton("Bug2Go");
		btnBug2go.setToolTipText("Shows info about Bug2Go process and uploads");
		btnBug2go.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(findNode("Bug2Go") == null) {
					btnBug2go.setEnabled(false);
					Thread susp = new Thread() {
					    public void run() {
					    	try {
						        System.out.println("Bug2go thread running");
		
						        crsList.setEnabled(false);
						        B2G.makelog(crPath);
								addBug2go("Bug2Go Info");
								btnBug2go.setEnabled(true);
								result = (result + "\n\n\n========================= Bug2Go =========================\n" + B2G.getResult());
								crsList.setEnabled(true);
								
								System.out.println("Bug2go thread finished");
					    	} catch (Exception e) {
								e.printStackTrace();
								System.out.println("Bug2go thread error");
								btnBug2go.setEnabled(true);
								crsList.setEnabled(true);
							}
					    }  
					};
					susp.start();
				}
			}
		});
		parserPane.add(btnBug2go);
		
		btnTethering = new JButton("Tethering");
		btnTethering.setToolTipText("Shows info about Tethering usage");
		btnTethering.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(findNode("Tethering") == null) {
					btnTethering.setEnabled(false);
					Thread susp = new Thread() {
					    public void run() {
					    	try {
						        System.out.println("Tethering thread running");
		
						        crsList.setEnabled(false);
						        Tether.makeLog(crPath, BaseWindow);
								addTether("Tethering Info");
								btnTethering.setEnabled(true);
								result = (result + "\n\n\n======================== Wifi Tether =========================\n" + Tether.getResult());
								crsList.setEnabled(true);
								
								System.out.println("Tethering thread finished");
					    	} catch (Exception e) {
								e.printStackTrace();
								System.out.println("Tethering thread error");
								btnTethering.setEnabled(true);
								crsList.setEnabled(true);
							}
					    }  
					};
					susp.start();
				}
			}
		});
		parserPane.add(btnTethering);
		
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
		gbc_LogsPane.gridy = 2;
		add(LogsPane, gbc_LogsPane);
		
		JButton btnViewMain = new JButton("View Main");
		btnViewMain.setToolTipText("Open logcat main log");
		btnViewMain.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					if(rdBtnTextAnal.isSelected())
						Runtime.getRuntime().exec(new String[] {"Data\\complements\\TextAnalysisTool.exe ", crPath + "\\aplogcat-main.txt"});
					else
						Runtime.getRuntime().exec(new String[] {"C:\\Program Files (x86)\\Notepad++\\notepad++.exe ", crPath + "\\aplogcat-main.txt"});
			    } catch (IOException e) {
			        e.printStackTrace();
			        System.out.println("Error opening file");
			    }
			}
		});
		
		rdBtnTextAnal = new JRadioButton("TextAnalysis");
		rdBtnTextAnal.setToolTipText("Use TextAnalysis tool as default text editor");
		LogsPane.add(rdBtnTextAnal);
		rdBtnTextAnal.setSelected(true);
		editorSelector.add(rdBtnTextAnal);
		
		rdbtnNotepad = new JRadioButton("Notepad++");
		rdbtnNotepad.setToolTipText("Use Notepad++ as default text editor");
		LogsPane.add(rdbtnNotepad);
		editorSelector.add(rdbtnNotepad);
		LogsPane.add(btnViewMain);
		
		btnViewSystem = new JButton("View System");
		btnViewSystem.setToolTipText("Open logcat system log");
		btnViewSystem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					if(rdBtnTextAnal.isSelected())
						Runtime.getRuntime().exec(new String[] {"Data\\complements\\TextAnalysisTool.exe ", crPath + "\\aplogcat-system.txt"});
					else
						Runtime.getRuntime().exec(new String[] {"C:\\Program Files (x86)\\Notepad++\\notepad++.exe ", crPath + "\\aplogcat-system.txt"});
			    } catch (IOException e) {
			        e.printStackTrace();
			        System.out.println("Error opening file");
			    }
			}
		});
		LogsPane.add(btnViewSystem);
		
		btnViewKernel = new JButton("View Kernel");
		btnViewKernel.setToolTipText("Open logcat kernel log");
		btnViewKernel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					if(rdBtnTextAnal.isSelected())
						Runtime.getRuntime().exec(new String[] {"Data\\complements\\TextAnalysisTool.exe ", crPath + "\\aplogcat-kernel.txt"});
					else
						Runtime.getRuntime().exec(new String[] {"C:\\Program Files (x86)\\Notepad++\\notepad++.exe ", crPath + "\\aplogcat-kernel.txt"});
			    } catch (IOException e) {
			        e.printStackTrace();
			        System.out.println("Error opening file");
			    }
			}
		});
		LogsPane.add(btnViewKernel);
		
		btnViewRadio = new JButton("View Radio");
		btnViewRadio.setToolTipText("Open logcat radio log");
		btnViewRadio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					if(rdBtnTextAnal.isSelected())
						Runtime.getRuntime().exec(new String[] {"Data\\complements\\TextAnalysisTool.exe ", crPath + "\\aplogcat-radio.txt"});
					else
						Runtime.getRuntime().exec(new String[] {"C:\\Program Files (x86)\\Notepad++\\notepad++.exe ", crPath + "\\aplogcat-radio.txt"});
			    } catch (IOException e) {
			        e.printStackTrace();
			        System.out.println("Error opening file");
			    }
			}
		});
		LogsPane.add(btnViewRadio);
		
		btnViewBugreport = new JButton("View Bugreport");
		btnViewBugreport.setToolTipText("Open logcat bugreport log");
		btnViewBugreport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					File folder = new File(crPath);
					File[] listOfFiles = folder.listFiles();

					// Look for the file
					for (int i = 0; i < listOfFiles.length; i++) {
						if (listOfFiles[i].isFile()) {
							String file = listOfFiles[i].getName();
							if ( file.toLowerCase().endsWith(".txt") && file.contains("bugreport") ) {
								if(rdBtnTextAnal.isSelected())
									Runtime.getRuntime().exec(new String[] {"Data\\complements\\TextAnalysisTool.exe ", crPath + "\\" + file});
								else
									Runtime.getRuntime().exec(new String[] {"C:\\Program Files (x86)\\Notepad++\\notepad++.exe ", crPath + "\\" + file});
								break;
							}
						}
					}
			    } catch (IOException e) {
			        e.printStackTrace();
			        System.out.println("Error opening file");
			    }
			}
		});
		LogsPane.add(btnViewBugreport);
		
		btnViewReportOutput = new JButton("Report Output");
		btnViewReportOutput.setToolTipText("Open report output log");
		btnViewReportOutput.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					if(rdBtnTextAnal.isSelected())
						Runtime.getRuntime().exec(new String[] {"Data\\complements\\TextAnalysisTool.exe ", crPath + "\\report-output_v1.5.txt"});
					else
						Runtime.getRuntime().exec(new String[] {"C:\\Program Files (x86)\\Notepad++\\notepad++.exe ", crPath + "\\report-output_v1.5.txt"});
			    } catch (IOException e) {
			        e.printStackTrace();
			        System.out.println("Error opening file");
			    }
			}
		});
		LogsPane.add(btnViewReportOutput);
		
		btnBTD = new JButton("Open BTD");
		btnBTD.setToolTipText("Open BTD graphic");
		btnBTD.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					File folder = new File(crPath);
					File[] listOfFiles = folder.listFiles();
					String file = null;
					// Look for the file
					for (int i = 0; i < listOfFiles.length; i++) {
						if (listOfFiles[i].isFile()) {
							if ( listOfFiles[i].getName().toLowerCase().endsWith(".btd") ) {
								file = listOfFiles[i].getName();
							}
						}
					}
					Runtime.getRuntime().exec(new String[] {"C:\\Program Files (x86)\\BTDashboard\\BTDashboardv2.7.exe ", crPath + "\\" + file});
			    } catch (IOException e) {
			        e.printStackTrace();
			        System.out.println("Error opening file");
			    }
			}
		});
		LogsPane.add(btnBTD);
		
		btnOpenfolder = new JButton("Open Folder");
		btnOpenfolder.setToolTipText("Open CR folder");
		btnOpenfolder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					String aux = (String) crsList.getSelectedItem();
					if(aux != null && !aux.equals(""))
							Desktop.getDesktop().open(new File(crPath));
				} catch (IOException e) {
					e.printStackTrace();
					System.out.println("Error opening folder");
				}
			}
		});
		LogsPane.add(btnOpenfolder);
		esquerda.setViewportView(tree);
		
		
		GridBagConstraints gbC = new GridBagConstraints();
		gbC.weighty = 3.0;
		gbC.weightx = 1.0;
		gbC.insets = new Insets(2, 10, 10, 10);
		gbC.fill = GridBagConstraints.BOTH;
		gbC.gridx = 0;
		gbC.gridy = 3;
		gbC.gridheight = 2;
		add(esquerda, gbC);
		
		
		GridBagLayout tr = new GridBagLayout();
		tr.rowWeights = new double[]{0.0};
		tr.rowHeights = new int[]{0};
		topright.setLayout(tr);
		
		GridBagConstraints g1 = new GridBagConstraints();
		g1.fill = GridBagConstraints.HORIZONTAL;
		g1.weightx = 20.0;
		g1.weighty = 1.0;
		g1.insets = new Insets(1, 10, 1, 10);
		g1.gridx = 1;
		g1.gridy = 3;
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
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.anchor = GridBagConstraints.NORTHWEST;
		gbc_scrollPane.insets = new Insets(2, 10, 10, 10);
		gbc_scrollPane.weighty = 22.0;
		gbc_scrollPane.weightx = 15.0;
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 1;
		gbc_scrollPane.gridy = 4;
		add(scrollPane, gbc_scrollPane);
		
		textPane = new JTextPane();
		textPane.setToolTipText("Result of the selected parser item on the left");
		textPane.setContentType("text/plain");
		textPane.setMargin(new Insets(7,7,7,7));
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
		
		folder.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent arg0) {
				rootPath = folder.getText().replace("\\", "\\\\").concat("\\\\");
				clearTree();
				result = "";
				crsList.removeAllItems();
				File actualPath = new File(rootPath);
				File[] listOfFiles = actualPath.listFiles();
				if(actualPath.isDirectory()){
					//List files
					for (int i = 0; i < listOfFiles.length; i++) {
						if (listOfFiles[i].isDirectory()) {
							crsList.addItem(listOfFiles[i].getName());
						}
					}
				}
				textPane.setText("");
				lblTitle.setText("Run a parser or select a result on the left");
				crsList.updateUI();
			}
			
			@Override
			public void insertUpdate(DocumentEvent arg0) {
				rootPath = folder.getText().replace("\\", "\\\\").concat("\\\\");
				clearTree();
				result = "";
				System.out.println("Parsed path: " + rootPath);
				crsList.removeAllItems();
				File actualPath = new File(rootPath);
				File[] listOfFiles = actualPath.listFiles();
				if(actualPath.isDirectory()){
					//List files
					for (int i = 0; i < listOfFiles.length; i++) {
						if (listOfFiles[i].isDirectory()) {
							crsList.addItem(listOfFiles[i].getName());
						}
					}
				}
				textPane.setText("");
				lblTitle.setText("Run a parser or select a result on the left");
				crsList.updateUI();
			}

			@Override
			public void changedUpdate(DocumentEvent arg0) {}
		});
		rootPath = folder.getText().replace("\\", "\\\\").concat("\\\\");
		clearTree();
		result = "";
		crsList.removeAllItems();
		File actualPath = new File(rootPath);
		File[] listOfFiles = actualPath.listFiles();
		if(actualPath.isDirectory()){
			//List files
			for (int i = 0; i < listOfFiles.length; i++) {
				if (listOfFiles[i].isDirectory()) {
					crsList.addItem(listOfFiles[i].getName());
				}
			}
		}
		textPane.setText("");
		lblTitle.setText("Run a parser or select a result on the left");
		crsList.updateUI();
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
	
	
	public void updateCrList() {
		crsList.removeAllItems();
		File actualPath = new File(rootPath);
		File[] listOfFiles = actualPath.listFiles();
		if(actualPath.isDirectory()){
			//List files
			for (int i = 0; i < listOfFiles.length; i++) {
				if (listOfFiles[i].isDirectory()) {
					crsList.addItem(listOfFiles[i].getName());
				}
			}
		}
		//textPane.setText("");
		lblTitle.setText("Run a parser or select a result on the left");
		crsList.updateUI();
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
		return folder.getText();
	}
	
	public void setRootPath(String path) {
		rootPath = path.replace("\\", "\\\\");
		folder.setText(path);
	}

	public void setResultsText(String text){
		textPane.setText(text);
		textPane.setCaretPosition(0);
	}
	
	public JRadioButton getNotepad(){
		return rdbtnNotepad;
	}
	
	private void delFolder(String folder){
		File file = new File(folder);
		try {
			if(file.isDirectory()){
				System.out.println("Deleting " + folder);
				FileUtils.deleteDirectory(file);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
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
					e.setText(folder.getText());
					
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
					folder.setText(crPath);
					
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
	
}
