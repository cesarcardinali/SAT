package panes;


import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import core.SharedObjs;

import supportive.ColorPrinter;

import filters.Alarm;
import filters.B2G;
import filters.Consume;
import filters.Diag;
import filters.Issue;
import filters.Normal;
import filters.Suspicious;
import filters.Tether;

import objects.CustomFilterItem;

import style.LabelTreeNodeRenderer;


@SuppressWarnings("serial")
public class FiltersResultsTree extends JTree {
	
	// Global Variables
	private DefaultTreeModel treeModel;
	private DefaultMutableTreeNode rootNode;

	// Tree definition
	public FiltersResultsTree() {
		// Initializing variables
		treeModel = (DefaultTreeModel) getModel();
		rootNode = (DefaultMutableTreeNode) treeModel.getRoot();
		setToggleClickCount(1);
		
		// Setting up initial tree (needs to change for when custom filters enabled)
		rootNode.setUserObject("filters and Results");
		rootNode.removeAllChildren();
		initializeTree();
		
		// Configuring rows UI
		setCellRenderer(new LabelTreeNodeRenderer());
		setRowHeight(20);
		
		// Configuring tree selection listener
		addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				final DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) getLastSelectedPathComponent();
				
				if(selectedNode != null){
					
					if(!selectedNode.toString().contains("On Colors"))						
						ColorPrinter.resetPanelStyle(SharedObjs.parserPane.getTextPane());
					
					switch (selectedNode.getLevel()){
						case 0: // Root selected
							SharedObjs.parserPane.showAllResults();
							break;
							
						case 1: // Leaf filter selected
								if(selectedNode.toString().contains("Alarms")){
									if(selectedNode.getChildCount() == 0){
										new Thread(new Runnable() {
											@Override
											public void run() {
												alarmThread(selectedNode);
											}
										}).start();
									}  else {
										SharedObjs.parserPane.setTitle("Alarms resume:");
										SharedObjs.parserPane.setResultsText(Alarm.getResult());
									}
									
								} else if(selectedNode.toString().contains("Bug2Go")){
									if(selectedNode.getChildCount() == 0){
										new Thread(new Runnable() {
											@Override
											public void run() {
												bug2goThread(selectedNode);
											}
										}).start();
									}  else {
										SharedObjs.parserPane.setTitle("Bug2Go:");
										SharedObjs.parserPane.setResultsText(B2G.getResult());
									}
									
								} else if(selectedNode.toString().contains("Diag")){
									if(selectedNode.getChildCount() == 0){
										new Thread(new Runnable() {
											@Override
											public void run() {
												diagThread(selectedNode);
											}
										}).start();
									}  else {
										SharedObjs.parserPane.setTitle("Diag:");
										SharedObjs.parserPane.setResultsText(Diag.getResult());
									}
									
								} else if(selectedNode.toString().contains("WakeLocks")){
									if(selectedNode.getChildCount() == 0){
										new Thread(new Runnable() {
											@Override
											public void run() {
												wakelocksThread(selectedNode);
											}
										}).start();
									} else {
										SharedObjs.parserPane.setTitle("WakeLocks:");
										SharedObjs.parserPane.setResultsText(Issue.getResult());
									}
									
								} else if(selectedNode.toString().contains("High Consumption")){
									if(selectedNode.getChildCount() == 0){
										new Thread(new Runnable() {
											@Override
											public void run() {
												comsumptionThread(selectedNode);
											}
										}).start();	
									} else {
										SharedObjs.parserPane.setTitle("High Consume Apps:");
										SharedObjs.parserPane.setResultsText(Consume.getResult());
									}
									
								} else if(selectedNode.toString().contains("Summary")){
									if(selectedNode.getChildCount() == 0){
										new Thread(new Runnable() {
											@Override
											public void run() {
												summaryThread(selectedNode);
											}
										}).start();
									}  else {
										SharedObjs.parserPane.setTitle("Discharge Summary:");
										SharedObjs.parserPane.setResultsText(Normal.getResult());
									}
									
								} else if(selectedNode.toString().contains("Suspicious")){
									if(selectedNode.getChildCount() == 0){
										new Thread(new Runnable() {
											@Override
											public void run() {
												suspiciousThread(selectedNode);
											}
										}).start();
									}  else {
										SharedObjs.parserPane.setTitle("Suspicious:");
										SharedObjs.parserPane.setResultsText(Suspicious.getResult());
									}
									
								} else if(selectedNode.toString().contains("Tethering")){
									if(selectedNode.getChildCount() == 0){
										new Thread(new Runnable() {
											@Override
											public void run() {
												tetherThread(selectedNode);
											}
										}).start();
									}  else {
										SharedObjs.parserPane.setTitle("Tethering:");
										SharedObjs.parserPane.setResultsText(Tether.getResult());
									}
									
								} else {
									int index = SharedObjs.customFiltersList.indexOf(selectedNode.toString().replace(" - Done", "").replace(" - Error", ""));
									if(index >= 0){
										if(selectedNode.getChildCount() == 0){
											System.out.println("3");
											new Thread(new Runnable() {
												@Override
												public void run() {
													customThread(selectedNode);
												}
											}).start();
										}  else {
											SharedObjs.parserPane.setTitle(SharedObjs.customFiltersList.get(index).getName());
											SharedObjs.parserPane.setResultsText(SharedObjs.customFiltersList.get(index).getResult());
										}
									}
								}
							break;
							
						case 2: // A filter child selected
							DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) selectedNode.getParent();
							if(parentNode.toString().contains("Alarm")){
								SharedObjs.parserPane.setTitle("Alarms:");
								if(parentNode.toString().contains("Error")) {
									SharedObjs.parserPane.setResultsText(Alarm.getResult());
								} else {
									int nodeIndex = parentNode.getIndex(selectedNode);
									if(selectedNode.toString().contains("On Colors")){
										SharedObjs.parserPane.setTitle("Alarms On Colors:");
										ColorPrinter.colorsAlarm(SharedObjs.parserPane.getTextPane(), Alarm.getResult());
										SharedObjs.parserPane.getTextPane().setCaretPosition(0);
										break;
									} else {
										SharedObjs.parserPane.setTitle(selectedNode.toString() + " - " + Alarm.getList().get(nodeIndex-1).getAction());
										SharedObjs.parserPane.setResultsText(
												SharedObjs.optionsPane.getTextAlarms()
												.replace("#pname#", Alarm.getList().get(nodeIndex-1).getProcess())
												.replace("#log#", Alarm.getList().get(nodeIndex-1).toString())
												.replace("\\n", "\n"));
										break;
									}
								}
								
							} else if(parentNode.toString().contains("Bug2Go")){
								SharedObjs.parserPane.setTitle("Bug2go:");
								if(parentNode.toString().contains("Error")) {
									SharedObjs.parserPane.setResultsText(B2G.getResult());
								} else {
									SharedObjs.parserPane.setResultsText(B2G.getResult());
								}
								
							} else if(parentNode.toString().contains("Diag")){
								SharedObjs.parserPane.setTitle("Diag:");
								if(parentNode.toString().contains("Error")) {
									SharedObjs.parserPane.setResultsText(Diag.getResult());
								} else {
									SharedObjs.parserPane.setResultsText(Diag.getResult());
								}
								
							} else if(parentNode.toString().contains("WakeLocks")){
								SharedObjs.parserPane.setTitle("WakeLocks:");
								if(parentNode.toString().contains("Error")) {
									SharedObjs.parserPane.setResultsText(Issue.getResult());
								} else {
									SharedObjs.parserPane.setResultsText(Issue.getResult());
								}
								
							} else if(parentNode.toString().contains("High Consumption")){
								SharedObjs.parserPane.setTitle("Apps Consumption:");
								if(parentNode.toString().contains("Error")) {
									SharedObjs.parserPane.setResultsText(Consume.getResult());
								} else {
									int nodeIndex = parentNode.getIndex(selectedNode);
									if(selectedNode.toString().contains("On Colors")){
										SharedObjs.parserPane.setTitle("High Consumption Apps:");
										ColorPrinter.colorsApps(SharedObjs.parserPane.getTextPane(), Consume.getResult());
									} else {
										SharedObjs.parserPane.setTitle(Consume.getHCList().get(nodeIndex-1).getProcess());
										SharedObjs.parserPane.setResultsText("{panel}\n" + Consume.getHCList().get(nodeIndex-1).toString() + "{panel}");
									}
								}
								
							} else if(parentNode.toString().contains("Summary")){
								SharedObjs.parserPane.setTitle("Summary:");
								if(parentNode.toString().contains("Error")) {
									SharedObjs.parserPane.setResultsText(Normal.getResult());
								} else {
									SharedObjs.parserPane.setResultsText(Normal.getResult());
								}
								
							} else if(parentNode.toString().contains("Suspicious")){
								SharedObjs.parserPane.setTitle("Suspicious:");
								if(parentNode.toString().contains("Error")) {
									SharedObjs.parserPane.setResultsText(Suspicious.getResult());
								} else {
									int nodeIndex = parentNode.getIndex(selectedNode);
									SharedObjs.parserPane.setTitle(Suspicious.getWakeLocks().get(nodeIndex).getProcess());
									SharedObjs.parserPane.setResultsText(Suspicious.getWakeLocks().get(nodeIndex).toString());
								}
								
							} else if(parentNode.toString().contains("Tethering")){
								SharedObjs.parserPane.setTitle("Tethering:");
								if(parentNode.toString().contains("Error")) {
									SharedObjs.parserPane.setResultsText(Tether.getResult());
								} else {
									SharedObjs.parserPane.setResultsText(Tether.getResult());
								}
								
							} else {
								int index = SharedObjs.customFiltersList.indexOf(parentNode.toString().replace(" - Done", "").replace(" - Error", ""));
								if(index >= 0){
									SharedObjs.parserPane.setTitle(SharedObjs.customFiltersList.get(index).getName());
									SharedObjs.parserPane.setResultsText(SharedObjs.customFiltersList.get(index).getResult());
								}
							}
							break;
							
						case 3: // A filter child derivation selected
							parentNode = (DefaultMutableTreeNode) selectedNode.getParent().getParent();
							int nodeIndex = parentNode.getIndex(selectedNode.getParent());
							if(parentNode.toString().contains("Alarms")){
								SharedObjs.parserPane.setTitle(selectedNode.getParent().toString() + " - " + Alarm.getList().get(nodeIndex-1).getAction());
								ColorPrinter.colorsAlarm(SharedObjs.parserPane.getTextPane(), 
										SharedObjs.optionsPane.getTextAlarms()
										.replace("#pname#", Alarm.getList().get(nodeIndex - 1).getProcess())
										.replace("#log#", Alarm.getList().get(nodeIndex - 1).toString())
										.replace("\\n", "\n"));
								
							} else if(parentNode.toString().contains("High Consumption")){
								SharedObjs.parserPane.setTitle(Consume.getHCList().get(nodeIndex-1).getProcess());
								if (selectedNode.toString().contains("ON"))
									SharedObjs.parserPane.setResultsText(SharedObjs.optionsPane.getTextConsumeOn()
											.replaceAll("#pname#",Consume.getHCList().get(nodeIndex-1).getProcess())
											.replaceAll("#sconconsume#",String.valueOf(Consume.getHCList().get(nodeIndex-1).getScOnConsume()))
											.replaceAll("#logon#", Consume.getHCList().get(nodeIndex-1).getLogOn())
											.replace("\\n", "\n"));
								else 
									if (selectedNode.toString().contains("OFF"))
									SharedObjs.parserPane.setResultsText(SharedObjs.optionsPane.getTextConsumeOff()
											.replaceAll("#pname#",Consume.getHCList().get(nodeIndex-1).getProcess())
											.replaceAll("#scoffconsume#",String.valueOf(Consume.getHCList().get(nodeIndex-1).getScOffConsume()))
											.replaceAll("#logoff#", Consume.getHCList().get(nodeIndex-1).getLogOff())
											.replace("\\n", "\n"));
								else
									if (selectedNode.toString().contains("Full"))
									SharedObjs.parserPane.setResultsText(SharedObjs.optionsPane.getTextConsumeFull()
											.replaceAll("#pname#",Consume.getHCList().get(nodeIndex-1).getProcess())
											.replaceAll("#avgconsume#",String.valueOf(Consume.getHCList().get(nodeIndex-1).getConsumeAvg()))
											.replaceAll("#logfull#", Consume.getHCList().get(nodeIndex-1).getLog())
											.replace("\\n", "\n"));
								else
									ColorPrinter.colorsApps(SharedObjs.parserPane.getTextPane(), SharedObjs.optionsPane.getTextConsumeFull()
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
		updateResultTreeUI();
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

	public void addCustomResult(String node, String res){
		DefaultMutableTreeNode Node = findNode(node);
		if(Node == null){
			System.out.println("Could not find node. Adding new one.");
			Node = new DefaultMutableTreeNode(node);
			Node.add(new DefaultMutableTreeNode(res));
		}
		Node.add(new DefaultMutableTreeNode(res));
	}
	
	public void addCustomNode(String node){
		DefaultMutableTreeNode Node = findNode(node);
		if(Node == null){
			System.out.println("Could not find node. Adding new one.");
			Node = new DefaultMutableTreeNode(node);
			rootNode.add(Node);
		}
	}
	
	// Filter threads
	public void alarmThread(DefaultMutableTreeNode selectedNode){
		System.out.println("Alarms thread running");
		String alarmResult;

		String x = (String) selectedNode.getUserObject();
		x = ("Alarms - Running");
		selectedNode.setUserObject(x);
		updateResultTreeUI();
		
		alarmResult = Alarm.makelog(SharedObjs.crPath);
		
		if(alarmResult.contains("FileNotFoundException")){
			x = ("Alarms - Error");
			selectedNode.setUserObject(x);
			addAlarms("System log not found");
			updateResultTreeUI();
			
		} else if(alarmResult.contains("IOException")){
			x = ("Alarms - Error");
			selectedNode.setUserObject(x);
			addAlarms("IOException");
			updateResultTreeUI();
			
		} else if(alarmResult.contains("Error")){
			x = ("Alarms - Error");
			selectedNode.setUserObject(x);
			addAlarms("IOException");
			updateResultTreeUI();
			
		} else if(alarmResult.contains("Not a directory")){
			x = ("Alarms - Error");
			selectedNode.setUserObject(x);
			addAlarms("No directory selected");
			updateResultTreeUI();
			
		} else {
			if(Alarm.getListSize() == 0)
				addAlarms("Nothing found in the logs");
			else
			    for (int i = 0; i < Alarm.getListSize(); i++) {
					addAlarms(Alarm.getList().get(i).getProcess());
					System.out.println(Alarm.getList().get(i).getProcess());
				}
			
			
			SharedObjs.setResult(SharedObjs.getResult() + "\n\n\n======================= Alarms Resume =======================\n" + alarmResult);
			
			x = ("Alarms - Done");
			selectedNode.setUserObject(x);
		}
		
		System.out.println("Alarms thread finished");
		updateResultTreeUI();
		//expandPath(new TreePath(selectedNode.getPath()));
	}
	
	public void bug2goThread(DefaultMutableTreeNode selectedNode){
		System.out.println("Bug2go thread running");
		String b2gResult;
		
		String x = (String) selectedNode.getUserObject();
		x = ("Bug2Go - Running");
		selectedNode.setUserObject(x);
		updateResultTreeUI();
		
		b2gResult = B2G.makelog(SharedObjs.parserPane.getCrPath());
		
		if(b2gResult.contains("FileNotFoundException")){
			x = ("Bug2Go - Error");
			selectedNode.setUserObject(x);
			addBug2go("Logs not found");
			updateResultTreeUI();
			System.out.println("Bug2go thread error");
			
		} else if(b2gResult.contains("IOException")){
			x = ("Bug2Go - Error");
			selectedNode.setUserObject(x);
			addBug2go("IOException");
			updateResultTreeUI();
			System.out.println("Bug2go thread error");
			
		} else if(b2gResult.contains("Not a directory")){
			x = ("Bug2Go - Error");
			selectedNode.setUserObject(x);
			addBug2go("No directory selected");
			updateResultTreeUI();
			System.out.println("Bug2go thread error");
			
		} else {
			addBug2go("Result");
			
			SharedObjs.parserPane.setResult(SharedObjs.parserPane.getResult() + "\n\n\n========================= Bug2Go =========================\n" + b2gResult);
			
			x = ("Bug2Go - Done");
			selectedNode.setUserObject(x);
			updateResultTreeUI();
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
		updateResultTreeUI();
		
		diagResult = Diag.makelog(SharedObjs.parserPane.getCrPath());
		
		if(diagResult.contains("FileNotFoundException")){
			x = ("Diag - Error");
			selectedNode.setUserObject(x);
			addDiag("Logs not found");
			updateResultTreeUI();
			System.out.println("Diag thread error");
			
		} else if(diagResult.contains("IOException")){
			x = ("Diag - Error");
			selectedNode.setUserObject(x);
			addDiag("IOException");
			updateResultTreeUI();
			System.out.println("Diag thread error");
			
		} else if(diagResult.contains("JDOMException")){
			x = ("Diag - Error");
			selectedNode.setUserObject(x);
			addDiag("JDOM Error");
			updateResultTreeUI();
			System.out.println("Diag thread error");
			
		} else if(diagResult.contains("Not a directory")){
			x = ("Diag - Error");
			selectedNode.setUserObject(x);
			addDiag("No directory selected");
			updateResultTreeUI();
			System.out.println("Diag thread error");
			
		} else {
			addDiag("Result");
			
			SharedObjs.parserPane.setResult(SharedObjs.parserPane.getResult() + "\n\n\n======================= Diag Wake locks =======================\n" + diagResult);
			
			x = ("Diag - Done");
			selectedNode.setUserObject(x);
			updateResultTreeUI();
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
		updateResultTreeUI();
		
		wakelocksResult = Issue.makelog(SharedObjs.parserPane.getCrPath());
		
		if(wakelocksResult.contains("FileNotFoundException")){
			x = ("WakeLocks - Error");
			selectedNode.setUserObject(x);
			addIssues("Logs not found");
			updateResultTreeUI();
			System.out.println("Wakelocks thread error");
			
		} else if(wakelocksResult.contains("IOException")){
			x = ("WakeLocks - Error");
			selectedNode.setUserObject(x);
			addIssues("IOException");
			updateResultTreeUI();
			System.out.println("Wakelocks thread error");
			
		} else if(wakelocksResult.contains("Not a directory")){
			x = ("WakeLocks - Error");
			selectedNode.setUserObject(x);
			addIssues("No directory selected");
			updateResultTreeUI();
			System.out.println("Wakelocks thread error");
			
		} else {
			addIssues("Result");
			
			SharedObjs.parserPane.setResult(SharedObjs.parserPane.getResult() + "\n\n\n========================= Wakelocks =========================\n" + wakelocksResult);
			
			x = ("WakeLocks - Done");
			selectedNode.setUserObject(x);
			updateResultTreeUI();
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
		updateResultTreeUI();
		
		consumptionResult = Consume.makelog(SharedObjs.parserPane.getCrPath());
		
		if(consumptionResult.contains("FileNotFoundException")){
			x = ("High Consumption - Error");
			selectedNode.setUserObject(x);
			addConsumeNode("Logs not found");
			updateResultTreeUI();
			System.out.println("Consumption thread error");
			
		} else if(consumptionResult.contains("IOException")){
			x = ("High Consumption - Error");
			selectedNode.setUserObject(x);
			addConsumeNode("IOException");
			updateResultTreeUI();
			System.out.println("Consumption thread error");
			
		} else if(consumptionResult.contains("Not a directory")){
			x = ("High Consumption - Error");
			selectedNode.setUserObject(x);
			addConsumeNode("No directory selected");
			updateResultTreeUI();
			System.out.println("Consumption thread error");
			
		} else {
			for (int i = 0; i < Consume.getHCList().size(); i++) {
				addConsumeNode(Consume.getHCList().get(i).getProcess());
			}
			if(Consume.getHCList().size() == 0)
				addConsumeNode("Nothing found in logs");
			
			SharedObjs.parserPane.setResult(SharedObjs.parserPane.getResult() + "\n\n\n========================= High Consumption Apps =========================\n" + consumptionResult);
			
			x = ("High Consumption - Done");
			selectedNode.setUserObject(x);
			updateResultTreeUI();
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
		updateResultTreeUI();
		
		summaryResult = Normal.makeLog(SharedObjs.parserPane.getCrPath());
		
		if(summaryResult.contains("FileNotFoundException")){
			x = ("Summary - Error");
			selectedNode.setUserObject(x);
			addSummary("Logs not found");
			updateResultTreeUI();
			System.out.println("Summary thread error");
			
		} else if(summaryResult.contains("IOException")){
			x = ("Summary - Error");
			selectedNode.setUserObject(x);
			addSummary("IOException");
			updateResultTreeUI();
			System.out.println("Summary thread error");
			
		} else if(summaryResult.contains("Not a directory")){
			x = ("Summary - Error");
			selectedNode.setUserObject(x);
			addSummary("No directory selected");
			updateResultTreeUI();
			System.out.println("Summary thread error");
			
		} else {
			addSummary("Result");
			
			SharedObjs.parserPane.setResult(SharedObjs.parserPane.getResult() + "\n\n\n========================= Summary =========================\n" + summaryResult);
			
			x = ("Summary - Done");
			selectedNode.setUserObject(x);
			updateResultTreeUI();
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
		updateResultTreeUI();
		
		suspiciousResult = Suspicious.makelog(SharedObjs.parserPane.getCrPath());
		
		if(suspiciousResult.contains("FileNotFoundException")){
			x = ("Suspicious - Error");
			selectedNode.setUserObject(x);
			addWakeLocksNode("Logs not found");
			updateResultTreeUI();
			System.out.println("Suspicious thread error");
			
		} else if(suspiciousResult.contains("IOException")){
			x = ("Suspicious - Error");
			selectedNode.setUserObject(x);
			addWakeLocksNode("IOException");
			updateResultTreeUI();
			System.out.println("Suspicious thread error");
			
		} else if(suspiciousResult.contains("Not a directory")){
			x = ("Suspicious - Error");
			selectedNode.setUserObject(x);
			addWakeLocksNode("No directory selected");
			updateResultTreeUI();
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
			
			SharedObjs.parserPane.setResult(SharedObjs.parserPane.getResult() + "\n\n\n========================= Suspicious =========================\n" + suspiciousResult);
			
			x = ("Suspicious - Done");
			selectedNode.setUserObject(x);
			updateResultTreeUI();
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
		updateResultTreeUI();
		
		tetherResult = Tether.makeLog(SharedObjs.parserPane.getCrPath());
		
		if(tetherResult.contains("FileNotFoundException")){
			x = ("Tethering - Error");
			selectedNode.setUserObject(x);
			addTether("Logs not found");
			updateResultTreeUI();
			System.out.println("Tethering thread error");
			
		} else if(tetherResult.contains("IOException")){
			x = ("Tethering - Error");
			selectedNode.setUserObject(x);
			addTether("IOException");
			updateResultTreeUI();
			System.out.println("Tethering thread error");
			
		} else if(tetherResult.contains("Not a directory")){
			x = ("Tethering - Error");
			selectedNode.setUserObject(x);
			addTether("No directory selected");
			updateResultTreeUI();
			System.out.println("Tethering thread error");
			
		} else {
			addTether("Result");
			
			SharedObjs.parserPane.setResult(SharedObjs.parserPane.getResult() + "\n\n\n========================= Tethering =========================\n" + tetherResult);
			
			x = ("Tethering - Done");
			selectedNode.setUserObject(x);
			updateResultTreeUI();
			System.out.println("Tethering thread finished");
		}
		//expandPath(new TreePath(selectedNode.getPath()));
	}
	
	public void customThread(DefaultMutableTreeNode selectedNode){
		String result;
		String nodeName = selectedNode.toString();
		System.out.println(nodeName + " thread running");
		
		String x = (String) selectedNode.getUserObject();
		x = (nodeName + " - Running");
		selectedNode.setUserObject(x);
		updateResultTreeUI();
		
		int index = SharedObjs.customFiltersList.indexOf(nodeName);
		if(index >= 0){
			result = SharedObjs.customFiltersList.get(index).runFilter(SharedObjs.parserPane.getCrPath());
			
			if(result.contains(" log missing")){
				x = (nodeName + " - Error");
				selectedNode.setUserObject(x);
				addCustomResult(nodeName, "Logs not found");
				updateResultTreeUI();
				System.out.println(nodeName + " thread error");
				
			} else if(result.contains("SAT IOException")){
				x = (nodeName + " - Error");
				selectedNode.setUserObject(x);
				addCustomResult(nodeName, "IOException");
				updateResultTreeUI();
				System.out.println(nodeName + " thread error");
				
			} else if(result.contains("Not a directory")){
				x = (nodeName + " - Error");
				selectedNode.setUserObject(x);
				addCustomResult(nodeName, "No directory selected");
				updateResultTreeUI();
				System.out.println(nodeName + " thread error");
				
			} else {
				addCustomResult(nodeName, "Result");
				
				result = result + SharedObjs.customFiltersList.get(index).getHeader() + "\n";
				SharedObjs.parserPane.setResult(SharedObjs.parserPane.getResult() + "\n\n\n========================= Tethering =========================\n" + result);
				
				x = (nodeName + " - Done");
				selectedNode.setUserObject(x);
				updateResultTreeUI();
				System.out.println(nodeName + "thread finished");
			}
			//expandPath(new TreePath(selectedNode.getPath()));
		} else {
			x = (nodeName + " - Error");
			selectedNode.setUserObject(x);
			addCustomResult(nodeName, "Filter does not exists");
			updateResultTreeUI();
			System.out.println(nodeName + " thread error");
		}
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
		initializeTree();
	}
	
	public void updateResultTreeUI(){
		SwingUtilities.invokeLater(new Runnable() {
              public void run() {
                updateUI();
              }
          });
	}
	
	public void initializeTree(){
		if(Alarm.isEnabled()){
			rootNode.add(new DefaultMutableTreeNode("Alarms"));
		}
		if(B2G.isEnabled()){
			rootNode.add(new DefaultMutableTreeNode("Bug2Go"));
		}
		if(Diag.isEnabled()){
			rootNode.add(new DefaultMutableTreeNode("Diag"));
		}
		if(Issue.isEnabled()){
			rootNode.add(new DefaultMutableTreeNode("WakeLocks"));
		}
		if(Consume.isEnabled()){
			rootNode.add(new DefaultMutableTreeNode("High Consumption"));
		}
		if(Normal.isEnabled()){
			rootNode.add(new DefaultMutableTreeNode("Summary"));
		}
		if(Suspicious.isEnabled()){
			rootNode.add(new DefaultMutableTreeNode("Suspicious"));
		}
		if(Tether.isEnabled()){
			rootNode.add(new DefaultMutableTreeNode("Tethering"));
		}
		
		for(CustomFilterItem item : SharedObjs.customFiltersList){
			rootNode.add(new DefaultMutableTreeNode(item.getName()));
		}
		
		updateResultTreeUI();
	}
	
	public void addCustomFilters(String name){
		/*for(CustomFilterItem item : SharedObjs.customFiltersList){
			if(findNode(item.getName()) == null){
				rootNode.add(new DefaultMutableTreeNode(item.getName()));
				updateResultTreeUI();
			}
		}*/
		rootNode.add(new DefaultMutableTreeNode(name));
		updateResultTreeUI();
	}
	
	public void removeCustomNode(String name){
		for(int i=0; i < rootNode.getChildCount(); i++){
			System.out.println("No: " + rootNode.getChildAt(i).toString());
			if(rootNode.getChildAt(i).toString().replace(" - Done", "").replace(" - Error", "").replace(" - Running", "").equals(name)){
				System.out.println("achou");
				rootNode.remove(i);
				updateResultTreeUI();
			}
		}
	}
	
}
