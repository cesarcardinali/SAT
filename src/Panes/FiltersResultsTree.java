package Panes;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

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
import Supportive.LabelTreeNodeRenderer;

@SuppressWarnings("serial")
public class FiltersResultsTree extends JTree {
	
	// Global Variables
	private BatTracer BaseWindow;
	private DefaultTreeModel treeModel;
	private DefaultMutableTreeNode rootNode;
	private DefaultMutableTreeNode selectedNode;

	// Tree definition
	public FiltersResultsTree(BatTracer parent) {
		// Initializing variables
		BaseWindow = parent;
		treeModel = (DefaultTreeModel) getModel();
		rootNode = (DefaultMutableTreeNode) treeModel.getRoot();
		setToggleClickCount(1);
		
		// Setting up initial tree (needs to change for when custom filters enabled)
		rootNode.setUserObject("Filters and Results");
		rootNode.removeAllChildren();
		rootNode.add(new DefaultMutableTreeNode("Alarms"));
		rootNode.add(new DefaultMutableTreeNode("Bug2Go"));
		rootNode.add(new DefaultMutableTreeNode("Diag"));
		rootNode.add(new DefaultMutableTreeNode("WakeLocks"));
		rootNode.add(new DefaultMutableTreeNode("High Consumption"));
		rootNode.add(new DefaultMutableTreeNode("Summary"));
		rootNode.add(new DefaultMutableTreeNode("Suspicious"));
		rootNode.add(new DefaultMutableTreeNode("Tethering"));
		
		// Configuring rows UI
		setCellRenderer(new LabelTreeNodeRenderer());
		setRowHeight(20);
		
		// Configuring tree selection listener
		addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				selectedNode = (DefaultMutableTreeNode) getLastSelectedPathComponent();
				
				if(selectedNode != null){
					switch (selectedNode.getLevel()){
						case 0: // Root selected
							BaseWindow.getParser().showAllResults();
							break;
							
						case 1: // Leaf filter selected
								if(selectedNode.toString().toLowerCase().contains("alarms")){
									if(selectedNode.getChildCount() == 0){
										new Thread(new Runnable() {
											@Override
											public void run() {
												alarmThread(selectedNode);
											}
										}).start();
									}  else {
										BaseWindow.getParser().setTitle("Alarms resume:");
										BaseWindow.getParser().setResultsText(Alarm.getResult());
									}
									
								} else if(selectedNode.toString().toLowerCase().contains("bug2go")){
									if(selectedNode.getChildCount() == 0){
										new Thread(new Runnable() {
											@Override
											public void run() {
												bug2goThread(selectedNode);
											}
										}).start();
									}  else {
										BaseWindow.getParser().setTitle("Bug2go:");
										BaseWindow.getParser().setResultsText(B2G.getResult());
									}
									
								} else if(selectedNode.toString().toLowerCase().contains("diag")){
									if(selectedNode.getChildCount() == 0){
										new Thread(new Runnable() {
											@Override
											public void run() {
												diagThread(selectedNode);
											}
										}).start();
									}  else {
										BaseWindow.getParser().setTitle("Diag:");
										BaseWindow.getParser().setResultsText(Diag.getResult());
									}
									
								} else if(selectedNode.toString().toLowerCase().contains("wakelocks")){
									if(selectedNode.getChildCount() == 0){
										new Thread(new Runnable() {
											@Override
											public void run() {
												wakelocksThread(selectedNode);
											}
										}).start();
									} else {
										BaseWindow.getParser().setTitle("WakeLocks:");
										BaseWindow.getParser().setResultsText(Issue.getResult());
									}
									
								} else if(selectedNode.toString().toLowerCase().contains("consumption")){
									if(selectedNode.getChildCount() == 0){
										new Thread(new Runnable() {
											@Override
											public void run() {
												comsumptionThread(selectedNode);
											}
										}).start();	
									} else {
										BaseWindow.getParser().setTitle("High Consume Apps:");
										BaseWindow.getParser().setResultsText(Consume.getResult());
									}
									
								} else if(selectedNode.toString().toLowerCase().contains("summary")){
									if(selectedNode.getChildCount() == 0){
										new Thread(new Runnable() {
											@Override
											public void run() {
												summaryThread(selectedNode);
											}
										}).start();
									}  else {
										BaseWindow.getParser().setTitle("Discharge Summary:");
										BaseWindow.getParser().setResultsText(Normal.getResult());
									}
									
								} else if(selectedNode.toString().toLowerCase().contains("suspicious")){
									if(selectedNode.getChildCount() == 0){
										new Thread(new Runnable() {
											@Override
											public void run() {
												suspiciousThread(selectedNode);
											}
										}).start();
									}  else {
										BaseWindow.getParser().setTitle("Suspicious:");
										BaseWindow.getParser().setResultsText(Suspicious.getResult());
									}
									
								} else if(selectedNode.toString().toLowerCase().contains("tethering")){
									if(selectedNode.getChildCount() == 0){
										new Thread(new Runnable() {
											@Override
											public void run() {
												tetherThread(selectedNode);
											}
										}).start();
									}  else {
										BaseWindow.getParser().setTitle("Tethering:");
										BaseWindow.getParser().setResultsText(Tether.getResult());
									}
									
								} else {
									// If needed, do something here
								}
							break;
							
						case 2: // A filter child selected
							DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) selectedNode.getParent();
							if(selectedNode.getParent().toString().contains("Alarm")){
								BaseWindow.getParser().setTitle("Alarms:");
								if(parentNode.toString().contains("Error")) {
									BaseWindow.getParser().setResultsText(Alarm.getResult());
								} else {
									int nodeIndex = parentNode.getIndex(selectedNode);
									if(selectedNode.toString().contains("On Colors")){
										BaseWindow.getParser().setTitle("Alarms On Colors:");
										ColorPrinter.colorsAlarm(BaseWindow.getParser().getTextPane(), Alarm.getResult());
										BaseWindow.getParser().getTextPane().setCaretPosition(0);
										break;
									} else {
										BaseWindow.getParser().setTitle(selectedNode.toString() + " - " + Alarm.getList().get(nodeIndex-1).getAction());
										BaseWindow.getParser().setResultsText(
												BaseWindow.getOptions().getTextAlarms()
												.replace("#pname#", Alarm.getList().get(nodeIndex-1).getProcess())
												.replace("#log#", Alarm.getList().get(nodeIndex-1).toString())
												.replace("\\n", "\n"));
										break;
									}
								}
								
							} else if(selectedNode.getParent().toString().contains("Bug2Go")){
								BaseWindow.getParser().setTitle("Bug2go:");
								if(parentNode.toString().contains("Error")) {
									BaseWindow.getParser().setResultsText(B2G.getResult());
								} else {
									BaseWindow.getParser().setResultsText(B2G.getResult());
								}
								
							} else if(selectedNode.getParent().toString().contains("Diag")){
								BaseWindow.getParser().setTitle("Diag:");
								if(parentNode.toString().contains("Error")) {
									BaseWindow.getParser().setResultsText(Diag.getResult());
								} else {
									BaseWindow.getParser().setResultsText(Diag.getResult());
								}
								
							} else if(selectedNode.getParent().toString().contains("WakeLocks")){
								BaseWindow.getParser().setTitle("WakeLocks:");
								if(parentNode.toString().contains("Error")) {
									BaseWindow.getParser().setResultsText(Issue.getResult());
								} else {
									BaseWindow.getParser().setResultsText(Issue.getResult());
								}
								
							} else if(selectedNode.getParent().toString().contains("High Consumption")){
								BaseWindow.getParser().setTitle("Apps Consumption:");
								if(parentNode.toString().contains("Error")) {
									BaseWindow.getParser().setResultsText(Consume.getResult());
								} else {
									int nodeIndex = parentNode.getIndex(selectedNode);
									if(selectedNode.toString().contains("On Colors")){
										BaseWindow.getParser().setTitle("High Consumption Apps:");
										ColorPrinter.colorsApps(BaseWindow.getParser().getTextPane(), Consume.getResult());
									} else {
										BaseWindow.getParser().setTitle(Consume.getHCList().get(nodeIndex-1).getProcess());
										BaseWindow.getParser().setResultsText("{panel}\n" + Consume.getHCList().get(nodeIndex-1).toString() + "{panel}");
									}
								}
								
							} else if(selectedNode.getParent().toString().contains("Summary")){
								BaseWindow.getParser().setTitle("Summary:");
								if(parentNode.toString().contains("Error")) {
									BaseWindow.getParser().setResultsText(Normal.getResult());
								} else {
									BaseWindow.getParser().setResultsText(Normal.getResult());
								}
								
							} else if(selectedNode.getParent().toString().contains("Suspicious")){
								BaseWindow.getParser().setTitle("Suspicious:");
								if(parentNode.toString().contains("Error")) {
									BaseWindow.getParser().setResultsText(Suspicious.getResult());
								} else {
									int nodeIndex = parentNode.getIndex(selectedNode);
									BaseWindow.getParser().setTitle(Suspicious.getWakeLocks().get(nodeIndex).getProcess());
									BaseWindow.getParser().setResultsText(Suspicious.getWakeLocks().get(nodeIndex).toString());
								}
								
							} else if(selectedNode.getParent().toString().contains("Tethering")){
								BaseWindow.getParser().setTitle("Tethering:");
								if(parentNode.toString().contains("Error")) {
									BaseWindow.getParser().setResultsText(Tether.getResult());
								} else {
									BaseWindow.getParser().setResultsText(Tether.getResult());
								}
								
							}
							break;
							
						case 3: // A filter child derivation selected
							parentNode = (DefaultMutableTreeNode) selectedNode.getParent().getParent();
							int nodeIndex = parentNode.getIndex(selectedNode.getParent());
							if(parentNode.toString().contains("Alarms")){
								BaseWindow.getParser().setTitle(selectedNode.getParent().toString() + " - " + Alarm.getList().get(nodeIndex-1).getAction());
								ColorPrinter.colorsAlarm(BaseWindow.getParser().getTextPane(), 
										BaseWindow.getOptions().getTextAlarms()
										.replace("#pname#", Alarm.getList().get(nodeIndex - 1).getProcess())
										.replace("#log#", Alarm.getList().get(nodeIndex - 1).toString())
										.replace("\\n", "\n"));
								
							} else if(parentNode.toString().contains("High Consumption")){
								BaseWindow.getParser().setTitle(Consume.getHCList().get(nodeIndex-1).getProcess());
								if (selectedNode.toString().contains("ON"))
									BaseWindow.getParser().setResultsText(BaseWindow.getOptions().getTextConsumeOn()
											.replaceAll("#pname#",Consume.getHCList().get(nodeIndex-1).getProcess())
											.replaceAll("#sconconsume#",String.valueOf(Consume.getHCList().get(nodeIndex-1).getScOnConsume()))
											.replaceAll("#logon#", Consume.getHCList().get(nodeIndex-1).getLogOn())
											.replace("\\n", "\n"));
								else 
									if (selectedNode.toString().contains("OFF"))
									BaseWindow.getParser().setResultsText(BaseWindow.getOptions().getTextConsumeOff()
											.replaceAll("#pname#",Consume.getHCList().get(nodeIndex-1).getProcess())
											.replaceAll("#scoffconsume#",String.valueOf(Consume.getHCList().get(nodeIndex-1).getScOffConsume()))
											.replaceAll("#logoff#", Consume.getHCList().get(nodeIndex-1).getLogOff())
											.replace("\\n", "\n"));
								else
									if (selectedNode.toString().contains("Full"))
									BaseWindow.getParser().setResultsText(BaseWindow.getOptions().getTextConsumeFull()
											.replaceAll("#pname#",Consume.getHCList().get(nodeIndex-1).getProcess())
											.replaceAll("#avgconsume#",String.valueOf(Consume.getHCList().get(nodeIndex-1).getConsumeAvg()))
											.replaceAll("#logfull#", Consume.getHCList().get(nodeIndex-1).getLog())
											.replace("\\n", "\n"));
								else
									ColorPrinter.colorsApps(BaseWindow.getParser().getTextPane(), BaseWindow.getOptions().getTextConsumeFull()
											.replaceAll("#pname#",Consume.getHCList().get(nodeIndex-1).getProcess())
											.replaceAll("#avgconsume#",String.valueOf(Consume.getHCList().get(nodeIndex-1).getConsumeAvg()))
											.replaceAll("#logfull#", Consume.getHCList().get(nodeIndex-1).getLog())
											.replace("\\n", "\n"));
							}
							break;
							
						default: // Something different happened
							break;
					}
				}
			}
		});
		
		// Reloading tree architecture on the UI
		treeModel.reload();
	}
	
	
	
	
	// Node control methods
	public void addConsumeNode(String node){
		DefaultMutableTreeNode HCNode = findNode("High Consumption");
		DefaultMutableTreeNode HCitem = new DefaultMutableTreeNode(node);
		if(HCNode == null) {
			System.out.println("Could not find node");
		} else if(!HCNode.toString().contains("Error")){
			if(HCNode.getChildCount() == 0){
				HCNode.add(new DefaultMutableTreeNode("On Colors"));
			}
			HCitem.add(new DefaultMutableTreeNode("Screen ON"));
			HCitem.add(new DefaultMutableTreeNode("Screen OFF"));
			HCitem.add(new DefaultMutableTreeNode("Full Log"));
			HCitem.add(new DefaultMutableTreeNode("On Colors"));
		}
		HCNode.insert(HCitem, HCNode.getChildCount());
	}
	
	public void addWakeLocksNode(String node){
		DefaultMutableTreeNode SPNode = findNode("Suspicious");
		DefaultMutableTreeNode WLitem = new DefaultMutableTreeNode(node);
		if(SPNode == null){
			System.out.println("Could not find node");
		}
		SPNode.insert(WLitem, SPNode.getChildCount());
		
	}
		
	public void addIssues(String node){
		DefaultMutableTreeNode INode = findNode("WakeLocks");
		DefaultMutableTreeNode Iitem = new DefaultMutableTreeNode(node);
		if(INode == null){
			System.out.println("Could not find node");
			INode = new DefaultMutableTreeNode("WakeLocks");
		}
		INode.insert(Iitem, 0);
	}
	
	public void addBug2go(String node){
		DefaultMutableTreeNode BGNode = findNode("Bug2Go");
		if(BGNode == null){
			System.out.println("Could not find node");
			BGNode = new DefaultMutableTreeNode("Bug2Go");
		}
		BGNode.insert(new DefaultMutableTreeNode(node), 0);
	}
	
	public void addTether(String node){
		DefaultMutableTreeNode TNode = findNode("Tethering");
		if(TNode == null){
			System.out.println("Could not find node");
		}
		if (Tether.getResult().contains("No tethering evidences were found in text logs"))
			TNode.insert(new DefaultMutableTreeNode("No tethering activity found"), TNode.getChildCount());
		else
			TNode.insert(new DefaultMutableTreeNode(node), 0);
	}
	
	public void addDiag(String node){
		DefaultMutableTreeNode DNode = findNode("Diag");
		if(DNode == null){
			System.out.println("Could not find node");
		}
		DNode.insert(new DefaultMutableTreeNode(node), 0);
	}

	public void addAlarms(String node){
		DefaultMutableTreeNode ANode = findNode("Alarms");
		DefaultMutableTreeNode Aitem = new DefaultMutableTreeNode(node);
		if(ANode == null){
			System.out.println("Could not find node");
		} else if(!node.contains("Nothing") && !(ANode.getUserObject().toString()).contains("Error")) {
			if(ANode.getChildCount() == 0){
				ANode.add(new DefaultMutableTreeNode("On Colors"));
			}
			Aitem.add(new DefaultMutableTreeNode("On Colors"));
		}
		ANode.insert(Aitem, ANode.getChildCount());
	}
	
	public void addSummary(String node){
		DefaultMutableTreeNode SNode = findNode("Summary");
		if(SNode == null){
			System.out.println("Could not find node");
		}
		SNode.insert(new DefaultMutableTreeNode(node), 0);
	}

	
	// Filter threads
	public void alarmThread(DefaultMutableTreeNode selectedNode){
		System.out.println("Alarms thread running");
		String alarmResult;

		String x = (String) selectedNode.getUserObject();
		x = ("Alarms - Running");
		selectedNode.setUserObject(x);
		treeModel.reload(findNode("Alarms"));
		
		alarmResult = Alarm.makelog(BaseWindow.getParser().getCrPath(), BaseWindow);
		
		if(alarmResult.contains("FileNotFoundException")){
			x = ("Alarms - Error");
			selectedNode.setUserObject(x);
			addAlarms("System log not found");
			treeModel.reload(findNode("Alarms"));
			
		} else if(alarmResult.contains("IOException")){
			x = ("Alarms - Error");
			selectedNode.setUserObject(x);
			addAlarms("IOException");
			treeModel.reload(findNode("Alarms"));
			
		} else if(alarmResult.contains("Error")){
			x = ("Alarms - Error");
			selectedNode.setUserObject(x);
			addAlarms("IOException");
			treeModel.reload(findNode("Alarms"));
			
		} else if(alarmResult.contains("Not a directory")){
			x = ("Alarms - Error");
			selectedNode.setUserObject(x);
			addAlarms("No directory selected");
			treeModel.reload(findNode("Alarms"));
			
		} else {
			if(Alarm.getListSize() == 0)
				addAlarms("Nothing found in the logs");
			else
			    for (int i = 0; i < Alarm.getListSize(); i++) {
					addAlarms(Alarm.getList().get(i).getProcess());
					System.out.println(Alarm.getList().get(i).getProcess());
				}
			
			
			BaseWindow.getParser().setResult(BaseWindow.getParser().getResult() + "\n\n\n======================= Alarms Resume =======================\n" + alarmResult);
			
			x = ("Alarms - Done");
			selectedNode.setUserObject(x);
		}
		
		System.out.println("Alarms thread finished");
		treeModel.reload(findNode("Alarms"));
		//expandPath(new TreePath(selectedNode.getPath()));
	}
	
	public void bug2goThread(DefaultMutableTreeNode selectedNode){
		System.out.println("Bug2go thread running");
		String b2gResult;
		
		String x = (String) selectedNode.getUserObject();
		x = ("Bug2Go - Running");
		selectedNode.setUserObject(x);
		treeModel.reload(findNode("Bug2Go"));
		
		b2gResult = B2G.makelog(BaseWindow.getParser().getCrPath(), BaseWindow);
		
		if(b2gResult.contains("FileNotFoundException")){
			x = ("Bug2Go - Error");
			selectedNode.setUserObject(x);
			addBug2go("Logs not found");
			treeModel.reload(findNode("Bug2Go"));
			System.out.println("Bug2go thread error");
			
		} else if(b2gResult.contains("IOException")){
			x = ("Bug2Go - Error");
			selectedNode.setUserObject(x);
			addBug2go("IOException");
			treeModel.reload(findNode("Bug2Go"));
			System.out.println("Bug2go thread error");
			
		} else if(b2gResult.contains("Not a directory")){
			x = ("Bug2Go - Error");
			selectedNode.setUserObject(x);
			addBug2go("No directory selected");
			treeModel.reload(findNode("Bug2Go"));
			System.out.println("Bug2go thread error");
			
		} else {
			addBug2go("Result");
			
			BaseWindow.getParser().setResult(BaseWindow.getParser().getResult() + "\n\n\n========================= Bug2Go =========================\n" + b2gResult);
			
			x = ("Bug2Go - Done");
			selectedNode.setUserObject(x);
			treeModel.reload(findNode("Bug2Go"));
			System.out.println("Bug2go thread finished");
		}
		//expandPath(new TreePath(selectedNode.getPath()));
	}
			
	public void diagThread(DefaultMutableTreeNode selectedNode){
		System.out.println("Diag thread running");
		String diagResult;
		
		String x = (String) selectedNode.getUserObject();
		x = ("Diag - Running");
		selectedNode.setUserObject(x);
		treeModel.reload(findNode("Diag"));
		
		diagResult = Diag.makelog(BaseWindow.getParser().getCrPath(), BaseWindow);
		
		if(diagResult.contains("FileNotFoundException")){
			x = ("Diag - Error");
			selectedNode.setUserObject(x);
			addDiag("Logs not found");
			treeModel.reload(findNode("Diag"));
			System.out.println("Diag thread error");
			
		} else if(diagResult.contains("IOException")){
			x = ("Diag - Error");
			selectedNode.setUserObject(x);
			addDiag("IOException");
			treeModel.reload(findNode("Diag"));
			System.out.println("Diag thread error");
			
		} else if(diagResult.contains("JDOMException")){
			x = ("Diag - Error");
			selectedNode.setUserObject(x);
			addDiag("JDOM Error");
			treeModel.reload(findNode("Diag"));
			System.out.println("Diag thread error");
			
		} else if(diagResult.contains("Not a directory")){
			x = ("Diag - Error");
			selectedNode.setUserObject(x);
			addDiag("No directory selected");
			treeModel.reload(findNode("Diag"));
			System.out.println("Diag thread error");
			
		} else {
			addDiag("Result");
			
			BaseWindow.getParser().setResult(BaseWindow.getParser().getResult() + "\n\n\n======================= Diag Wake locks =======================\n" + diagResult);
			
			x = ("Diag - Done");
			selectedNode.setUserObject(x);
			treeModel.reload(findNode("Diag"));
			System.out.println("Diag thread finished");
		}
		//expandPath(new TreePath(selectedNode.getPath()));
	}

	public void wakelocksThread(DefaultMutableTreeNode selectedNode){
		System.out.println("Wakelocks thread running");
		String wakelocksResult;
		
		String x = (String) selectedNode.getUserObject();
		x = ("WakeLocks - Running");
		selectedNode.setUserObject(x);
		treeModel.reload(findNode("Wakelocks"));
		
		wakelocksResult = Issue.makelog(BaseWindow.getParser().getCrPath(), BaseWindow);
		
		if(wakelocksResult.contains("FileNotFoundException")){
			x = ("WakeLocks - Error");
			selectedNode.setUserObject(x);
			addIssues("Logs not found");
			treeModel.reload(findNode("WakeLocks"));
			System.out.println("Wakelocks thread error");
			
		} else if(wakelocksResult.contains("IOException")){
			x = ("WakeLocks - Error");
			selectedNode.setUserObject(x);
			addIssues("IOException");
			treeModel.reload(findNode("WakeLocks"));
			System.out.println("Wakelocks thread error");
			
		} else if(wakelocksResult.contains("Not a directory")){
			x = ("WakeLocks - Error");
			selectedNode.setUserObject(x);
			addIssues("No directory selected");
			treeModel.reload(findNode("WakeLocks"));
			System.out.println("Wakelocks thread error");
			
		} else {
			addIssues("Result");
			
			BaseWindow.getParser().setResult(BaseWindow.getParser().getResult() + "\n\n\n========================= Wakelocks =========================\n" + wakelocksResult);
			
			x = ("WakeLocks - Done");
			selectedNode.setUserObject(x);
			treeModel.reload(findNode("WakeLocks"));
			System.out.println("Wakelocks thread finished");
		}
		//expandPath(new TreePath(selectedNode.getPath()));
	}
	
	public void comsumptionThread(DefaultMutableTreeNode selectedNode){
		System.out.println("Consumption thread running");
		String consumptionResult;
		
		String x = (String) selectedNode.getUserObject();
		x = ("High Consumption - Running");
		selectedNode.setUserObject(x);
		treeModel.reload(findNode("High Consumption"));
		
		consumptionResult = Consume.makelog(BaseWindow.getParser().getCrPath(), BaseWindow);
		
		if(consumptionResult.contains("FileNotFoundException")){
			x = ("High Consumption - Error");
			selectedNode.setUserObject(x);
			addConsumeNode("Logs not found");
			treeModel.reload(findNode("High Consumption"));
			System.out.println("Consumption thread error");
			
		} else if(consumptionResult.contains("IOException")){
			x = ("High Consumption - Error");
			selectedNode.setUserObject(x);
			addConsumeNode("IOException");
			treeModel.reload(findNode("High Consumption"));
			System.out.println("Consumption thread error");
			
		} else if(consumptionResult.contains("Not a directory")){
			x = ("High Consumption - Error");
			selectedNode.setUserObject(x);
			addConsumeNode("No directory selected");
			treeModel.reload(findNode("High Consumption"));
			System.out.println("Consumption thread error");
			
		} else {
			for (int i = 0; i < Consume.getHCList().size(); i++) {
				addConsumeNode(Consume.getHCList().get(i).getProcess());
			}
			if(Consume.getHCList().size() == 0)
				addConsumeNode("Nothing found in logs");
			
			BaseWindow.getParser().setResult(BaseWindow.getParser().getResult() + "\n\n\n========================= High Consumption Apps =========================\n" + consumptionResult);
			
			x = ("High Consumption - Done");
			selectedNode.setUserObject(x);
			treeModel.reload(findNode("High Consumption"));
			System.out.println("Consumption thread finished");
		}
		//expandPath(new TreePath(selectedNode.getPath()));
	}

	public void summaryThread(DefaultMutableTreeNode selectedNode){
		System.out.println("Summary thread running");
		String summaryResult;
		
		String x = (String) selectedNode.getUserObject();
		x = ("Summary - Running");
		selectedNode.setUserObject(x);
		treeModel.reload(findNode("Summary"));
		
		summaryResult = Normal.makeLog(BaseWindow.getParser().getCrPath(), BaseWindow);
		
		if(summaryResult.contains("FileNotFoundException")){
			x = ("Summary - Error");
			selectedNode.setUserObject(x);
			addSummary("Logs not found");
			treeModel.reload(findNode("Summary"));
			System.out.println("Summary thread error");
			
		} else if(summaryResult.contains("IOException")){
			x = ("Summary - Error");
			selectedNode.setUserObject(x);
			addSummary("IOException");
			treeModel.reload(findNode("Summary"));
			System.out.println("Summary thread error");
			
		} else if(summaryResult.contains("Not a directory")){
			x = ("Summary - Error");
			selectedNode.setUserObject(x);
			addSummary("No directory selected");
			treeModel.reload(findNode("Summary"));
			System.out.println("Summary thread error");
			
		} else {
			addSummary("Result");
			
			BaseWindow.getParser().setResult(BaseWindow.getParser().getResult() + "\n\n\n========================= Summary =========================\n" + summaryResult);
			
			x = ("Summary - Done");
			selectedNode.setUserObject(x);
			treeModel.reload(findNode("Summary"));
			System.out.println("Summary thread finished");
		}
		//expandPath(new TreePath(selectedNode.getPath()));
	}
	
	public void suspiciousThread(DefaultMutableTreeNode selectedNode){
		System.out.println("Suspicious thread running");
		String suspiciousResult;
		
		String x = (String) selectedNode.getUserObject();
		x = ("Suspicious - Running");
		selectedNode.setUserObject(x);
		treeModel.reload(findNode("Suspicious"));
		
		suspiciousResult = Suspicious.makelog(BaseWindow.getParser().getCrPath(), BaseWindow);
		
		if(suspiciousResult.contains("FileNotFoundException")){
			x = ("Suspicious - Error");
			selectedNode.setUserObject(x);
			addWakeLocksNode("Logs not found");
			treeModel.reload(findNode("Suspicious"));
			System.out.println("Suspicious thread error");
			
		} else if(suspiciousResult.contains("IOException")){
			x = ("Suspicious - Error");
			selectedNode.setUserObject(x);
			addWakeLocksNode("IOException");
			treeModel.reload(findNode("Suspicious"));
			System.out.println("Suspicious thread error");
			
		} else if(suspiciousResult.contains("Not a directory")){
			x = ("Suspicious - Error");
			selectedNode.setUserObject(x);
			addWakeLocksNode("No directory selected");
			treeModel.reload(findNode("Suspicious"));
			System.out.println("Suspicious thread error");
			
		} else {
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
			
			BaseWindow.getParser().setResult(BaseWindow.getParser().getResult() + "\n\n\n========================= Suspicious =========================\n" + suspiciousResult);
			
			x = ("Suspicious - Done");
			selectedNode.setUserObject(x);
			treeModel.reload(findNode("Suspicious"));
			System.out.println("Suspicious thread finished");
		}
		//expandPath(new TreePath(selectedNode.getPath()));
	}
	
	public void tetherThread(DefaultMutableTreeNode selectedNode){
		System.out.println("Summary thread running");
		String tetherResult;
		
		String x = (String) selectedNode.getUserObject();
		x = ("Tethering - Running");
		selectedNode.setUserObject(x);
		treeModel.reload(findNode("Tethering"));
		
		tetherResult = Tether.makeLog(BaseWindow.getParser().getCrPath(), BaseWindow);
		
		if(tetherResult.contains("FileNotFoundException")){
			x = ("Tethering - Error");
			selectedNode.setUserObject(x);
			addTether("Logs not found");
			treeModel.reload(findNode("Tethering"));
			System.out.println("Tethering thread error");
			
		} else if(tetherResult.contains("IOException")){
			x = ("Tethering - Error");
			selectedNode.setUserObject(x);
			addTether("IOException");
			treeModel.reload(findNode("Tethering"));
			System.out.println("Tethering thread error");
			
		} else if(tetherResult.contains("Not a directory")){
			x = ("Tethering - Error");
			selectedNode.setUserObject(x);
			addTether("No directory selected");
			treeModel.reload(findNode("Tethering"));
			System.out.println("Tethering thread error");
			
		} else {
			addTether("Result");
			
			BaseWindow.getParser().setResult(BaseWindow.getParser().getResult() + "\n\n\n========================= Tethering =========================\n" + tetherResult);
			
			x = ("Tethering - Done");
			selectedNode.setUserObject(x);
			treeModel.reload(findNode("Tethering"));
			System.out.println("Tethering thread finished");
		}
		//expandPath(new TreePath(selectedNode.getPath()));
	}


	// Tree supportive methods
	public DefaultMutableTreeNode findNode(String Node){
		int Length = rootNode.getChildCount();
		for(int i=0; i < Length; i++)
		{
			if(rootNode.getChildAt(i).toString().contains(Node)){
				return (DefaultMutableTreeNode) rootNode.getChildAt(i);
			}
		}	
		return null;
	}
	
	public int findNodeIndex(DefaultMutableTreeNode Node){
		System.out.println(rootNode.getIndex(Node));
		return rootNode.getIndex(Node);
	}
	
	public void clearTree(){
		rootNode.removeAllChildren();
		rootNode.add(new DefaultMutableTreeNode("Alarms"));
		rootNode.add(new DefaultMutableTreeNode("Bug2Go"));
		rootNode.add(new DefaultMutableTreeNode("Diag"));
		rootNode.add(new DefaultMutableTreeNode("WakeLocks"));
		rootNode.add(new DefaultMutableTreeNode("High Consumption"));
		rootNode.add(new DefaultMutableTreeNode("Summary"));
		rootNode.add(new DefaultMutableTreeNode("Suspicious"));
		rootNode.add(new DefaultMutableTreeNode("Tethering"));
		treeModel.reload();
	}
	
}
