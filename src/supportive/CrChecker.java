package supportive;


import java.awt.image.Kernel;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import objects.CrItem;
import supportive.preanalyzers.btdparser.BtdParser;
import supportive.preanalyzers.btdparser.BtdUptimePeriod;
import supportive.preanalyzers.btdparser.BtdWL;
import supportive.preanalyzers.logsparser.BugRepJavaWL;
import supportive.preanalyzers.logsparser.BugRepKernelWL;
import supportive.preanalyzers.logsparser.BugrepParser;
import supportive.preanalyzers.logsparser.MainParser;
import tests.JiraUser;
import core.Logger;
import core.SharedObjs;


@SuppressWarnings("unused")
public class CrChecker
{
	private static final String INCOMPLETE = "Incomplete";
	private static final String DUPLICATE  = "Duplicate";
	private static final String CANCELLED  = "Cancelled";
	private static final String INVALID    = "Invalid";
	private String              crPath;
	private String              falsePositiveComment;
	private String              dupComment;
	private String              tetherComment;
	private String              uptimesComment;
	private String              wakelocksComment;
	private boolean             btdParsed;
	private boolean             mainParsed;
	private boolean             bugrepParsed;
	private boolean             btdTether;
	private boolean             mainTether;
	
	private ArrayList<String>   incompleteFiles;
	private ArrayList<String>   filesNames;
	private ArrayList<File>     files;
	
	private BtdParser           btdParser;
	private BugrepParser        bugrepParser;
	private MainParser          mainParser;
	
	private CrItem              cr;
	private JiraSatApi          jira;
	
	public CrChecker(String crPath)
	{
		this.crPath = crPath;
	}
	
	public boolean checkCR()
	{
		File file = new File(crPath);
		cr = SharedObjs.getCrsList().getCrByB2gId(file.getName());
		
		if (cr != null)
		{
			long start = System.currentTimeMillis();
			
			System.out.println("\n\n");
			SharedObjs.crsManagerPane.addLogLine("Adding pre analyzed label ...");
			jira = new JiraSatApi(JiraSatApi.DEFAULT_JIRA_URL, SharedObjs.getUser(), SharedObjs.getPass());
			SharedObjs.crsManagerPane.addLogLine("CR Assignee: " + cr.getAssignee());
			if (cr.getAssignee().equals(""))
			{
				jira.assignIssue(cr.getJiraID());
				jira.addLabel(cr.getJiraID(), "sat_pre_analyzed");
				jira.unassignIssue(cr.getJiraID());
				SharedObjs.crsManagerPane.addLogLine("Unassigning");
			}
			else
			{
				SharedObjs.crsManagerPane.addLogLine("Letting assigned");
				jira.addLabel(cr.getJiraID(), "sat_pre_analyzed");
			}
			
			System.out.println("\n\n");
			SharedObjs.crsManagerPane.addLogLine("Checking if incomplete ...");
			if (checkIfIncomplete())
			{
				jira.assignIssue(cr.getJiraID());
				jira.closeIssue(cr.getJiraID(), JiraSatApi.INCOMPLETE, "The text logs are missing. Could not perform a complete analysis.");
				jira.addLabel(cr.getJiraID(), "sat_closed");
				
				cr.setResolution(INCOMPLETE);
				cr.setAssignee(SharedObjs.getUser());
				if (SharedObjs.satDB != null)
					if (SharedObjs.satDB.existsAnalyzedCR(cr.getJiraID()) > 0)
					{
						SharedObjs.satDB.updateAnalyzedCR(cr);
					}
					else
					{
						SharedObjs.satDB.insertAnalyzedCR(cr);
					}
				
				SharedObjs.crsManagerPane.addLogLine("Logs are missing. Closing CR " + cr.getJiraID() + " as incomplete");
				
				Logger.log(Logger.TAG_BUG2GODOWNLOADER, "Done for " + file.getAbsolutePath() + ". Closed as incomplete");
				
				return true;
			}
			
			// Try to parse log files ---------------------------------------------------
			// Parse BTD
			btdParser = new BtdParser(crPath);
			System.out.println("\n\n");
			SharedObjs.crsManagerPane.addLogLine("Parsing BTD data ...");
			btdParsed = btdParser.parse();
			if (btdParsed)
			{
				SharedObjs.crsManagerPane.addLogLine("Done");
			}
			else
			{
				SharedObjs.crsManagerPane.addLogLine("Not possible to parse BTD");
			}
			
			// Parse Main
			mainParser = new MainParser(crPath);
			SharedObjs.crsManagerPane.addLogLine("Parsing Main log data ...");
			mainParsed = mainParser.parse();
			if (mainParsed)
			{
				SharedObjs.crsManagerPane.addLogLine("Done");
			}
			else
			{
				SharedObjs.crsManagerPane.addLogLine("Not possible to parse Main");
			}
			
			// Parse Bugreport
			bugrepParser = new BugrepParser(crPath);
			SharedObjs.crsManagerPane.addLogLine("Parsing Bugreport log data ...");
			bugrepParsed = bugrepParser.parse();
			if (bugrepParsed)
			{
				SharedObjs.crsManagerPane.addLogLine("Done");
				dupComment = "";
			}
			else
			{
				SharedObjs.crsManagerPane.addLogLine("Not possible to parse Bugreport");
			}
			
			System.out.println("\n\n");
			SharedObjs.crsManagerPane.addLogLine("Checking for wakelocks ...");
			Logger.log(Logger.TAG_BUG2GODOWNLOADER, "Checking for wakelocks");
			
			if (checkIfWakelocks())
			{
				if (dupComment.length() > 5)
				{
					cr.setResolution("Duplicated");
					cr.setStatus("Closed");
					cr.setAssignee(SharedObjs.getUser());
					
					return true;
				}
				
				if (checkIfUptime())
				{
					wakelocksComment += "\\n\\n" + uptimesComment;
				}
				
				jira.addComment(cr.getJiraID(), wakelocksComment);
				
				cr.setAssignee(SharedObjs.getUser());
				
				if (SharedObjs.satDB != null)
					if (SharedObjs.satDB.existsAnalyzedCR(cr.getJiraID()) > 0)
					{
						SharedObjs.satDB.updateAnalyzedCR(cr);
					}
					else
					{
						SharedObjs.satDB.insertAnalyzedCR(cr);
					}
				
				SharedObjs.crsManagerPane.addLogLine("Wakelocks detected. Needs manual analysis.");
				
				Logger.log(Logger.TAG_BUG2GODOWNLOADER, "Done for " + file.getAbsolutePath() + ". Wakelocks detected. Needs manual analysis.");
				
				return false;
			}
			
			System.out.println("\n\n");
			SharedObjs.crsManagerPane.addLogLine("Checking for tethering ...");
			Logger.log(Logger.TAG_BUG2GODOWNLOADER, "Checking for tethering");
			if (checkIfTethering())
			{
				jira.assignIssue(cr.getJiraID());
				jira.closeIssue(cr.getJiraID(), JiraSatApi.INVALID, tetherComment);
				jira.addLabel(cr.getJiraID(), "sat_closed");
				
				cr.setResolution(INVALID);
				cr.setAssignee(SharedObjs.getUser());
				if (SharedObjs.satDB != null)
					if (SharedObjs.satDB.existsAnalyzedCR(cr.getJiraID()) > 0)
					{
						SharedObjs.satDB.updateAnalyzedCR(cr);
					}
					else
					{
						SharedObjs.satDB.insertAnalyzedCR(cr);
					}
				
				SharedObjs.crsManagerPane.addLogLine("Tethering detected. Closing CR " + cr.getJiraID() + " as invalid");
				
				Logger.log(Logger.TAG_BUG2GODOWNLOADER, "Done for " + file.getAbsolutePath() + ". Closed as invalid");
				
				return true;
			}
			
			System.out.println("\n\n");
			SharedObjs.crsManagerPane.addLogLine("Checking for uptime ...");
			Logger.log(Logger.TAG_BUG2GODOWNLOADER, "Checking for uptime");
			if (checkIfUptime())
			{
				jira.addComment(cr.getJiraID(),
				                "Some long uptimes were detected. This CR shall be manually analized in order to ensure if there are issues or not in this CR.\\n\\n"
				                                + uptimesComment);
				
				cr.setAssignee(SharedObjs.getUser());
				if (SharedObjs.satDB != null)
					if (SharedObjs.satDB.existsAnalyzedCR(cr.getJiraID()) > 0)
					{
						SharedObjs.satDB.updateAnalyzedCR(cr);
					}
					else
					{
						SharedObjs.satDB.insertAnalyzedCR(cr);
					}
				
				SharedObjs.crsManagerPane.addLogLine("Uptimes detected. Needs manual analysis.");
				
				Logger.log(Logger.TAG_BUG2GODOWNLOADER, "Done for " + file.getAbsolutePath() + ". Uptimes detected. Needs manual analysis.");
				
				return false;
			}
			
			System.out.println("\n\n");
			SharedObjs.crsManagerPane.addLogLine("Checking if false positive ...");
			Logger.log(Logger.TAG_BUG2GODOWNLOADER, "Checking for false positive");
			if (checkIfFalsePositive())
			{
				return true;
			}
			
			Logger.log(Logger.TAG_BUG2GODOWNLOADER, "Nothing detected");
			SharedObjs.crsManagerPane.addLogLine("Nothing detected. " + DateTimeOperator.getTimeStringFromMillis((System.currentTimeMillis() - start)));
			
			String comment = "";
			String eblDecresed = "{panel:title=*Items that increases current drain and decreases EBL*|titleBGColor=#E9F2FF}\\n";
			if (bugrepParsed)
			{
				comment += bugrepParser.currentDrainStatistics();
				eblDecresed = eblDecresed + bugrepParser.eblDecreasedReasons();
			}
			if (btdParsed)
			{
				if (comment.equals(""))
					comment += btdParser.toJiraComment();
				
				eblDecresed = eblDecresed + btdParser.eblDecreasers();
			}
			eblDecresed = eblDecresed + "{panel}\\n";
			if (eblDecresed.split("\\\\n|\\n|\n").length > 2)
			{
				comment = comment + eblDecresed;
			}
			
			jira.addComment(cr.getJiraID(), comment);
			
			Logger.log(Logger.TAG_FALSE_POSITIVE, DateTimeOperator.getTimeStringFromMillis((System.currentTimeMillis() - start)));
			
			if (SharedObjs.satDB != null)
				if (SharedObjs.satDB.existsAnalyzedCR(cr.getJiraID()) > 0)
				{
					SharedObjs.satDB.updateAnalyzedCR(cr);
				}
				else
				{
					SharedObjs.satDB.insertAnalyzedCR(cr);
				}
			
			btdParser.close();
			
			return false;
		}
		
		Logger.log(Logger.TAG_BUG2GODOWNLOADER, file.getAbsolutePath() + " was not pre analyzed because this CR is not on the downloaded CRs list");
		return false;
	}
	
	// Checkers ------------------------------------------------------------------------------
	private boolean checkIfIncomplete()
	{
		incompleteFiles = new ArrayList<String>();
		filesNames = new ArrayList<String>();
		files = new ArrayList<File>();
		
		boolean btd = false;
		boolean bugrep = false;
		boolean main = false;
		boolean system = false;
		boolean kernel = false;
		
		File folder = new File(crPath);
		
		if (!folder.isDirectory())
		{
			Logger.log(Logger.TAG_CR_CHECKER, "Not a directory");
			return false;
		}
		
		for (File file : folder.listFiles())
		{
			filesNames.add(file.getName());
			files.add(file);
			System.out.println("file: " + file.getName() + " - " + file.length() / 1048576);
		}
		
		for (String f : filesNames)
		{
			if (f.contains(".btd"))
			{
				btd = true;
			}
			else if (f.contains("bugreport"))
			{
				bugrep = true;
			}
			else if (f.contains("aplogcat-main"))
			{
				main = true;
			}
			else if (f.contains("aplogcat-system"))
			{
				system = true;
			}
			else if (f.contains("aplogcat-kernel"))
			{
				kernel = true;
			}
		}
		
		if (!btd)
		{
			incompleteFiles.add("btd");
			Logger.log(Logger.TAG_CR_CHECKER, "BTD file is missing");
		}
		
		if (bugrep)
		{
			for (File f : files)
			{
				if (f.getName().contains("bugreport"))
				{
					if (f.length() / 1023999 < 3)
					{
						incompleteFiles.add("bugreport");
						Logger.log(Logger.TAG_CR_CHECKER, "Bugreport file is too short");
					}
					break;
				}
			}
		}
		else
		{
			incompleteFiles.add("bugreport");
			Logger.log(Logger.TAG_CR_CHECKER, "Bugreport file is missing");
		}
		
		if (main)
		{
			File f = new File(crPath + "/aplogcat-main.txt");
			if (f.length() / 1023999 < 2)
			{
				incompleteFiles.add("main");
				Logger.log(Logger.TAG_CR_CHECKER, "Main file is too short " + f.length());
			}
		}
		else
		{
			incompleteFiles.add("main");
			Logger.log(Logger.TAG_CR_CHECKER, "Main file is missing ");
		}
		
		if (system)
		{
			File f = new File(crPath + "/aplogcat-system.txt");
			if (f.length() / 1023999 < 2)
			{
				incompleteFiles.add("system");
				Logger.log(Logger.TAG_CR_CHECKER, "System file is too short");
			}
		}
		else
		{
			incompleteFiles.add("system");
			Logger.log(Logger.TAG_CR_CHECKER, "System file is missing");
		}
		
		if (kernel)
		{
			File f = new File(crPath + "/aplogcat-kernel.txt");
			if (f.length() / 1023999 < 2)
			{
				incompleteFiles.add("kernel");
				Logger.log(Logger.TAG_CR_CHECKER, "Kernel file is too short");
			}
		}
		else
		{
			incompleteFiles.add("kernel");
			Logger.log(Logger.TAG_CR_CHECKER, "Kernel file is missing");
		}
		
		if (incompleteFiles.size() >= 4)
			return true;
		else
			return false;
	}
	
	private boolean checkIfTethering()
	{
		long start = System.currentTimeMillis();
		
		System.out.println();
		Logger.log(Logger.TAG_BUG2GODOWNLOADER, "-----------------");
		System.out.println();
		
		Logger.log(Logger.TAG_BUG2GODOWNLOADER, "Verifying BTD");
		
		if (btdParsed)
		{
			btdTether = btdParser.tethering();
			Logger.log(Logger.TAG_BUG2GODOWNLOADER, "Tethering issue? " + btdTether);
		}
		else
		{
			btdTether = false;
			Logger.log(Logger.TAG_BUG2GODOWNLOADER, "Could not parse BTD file");
		}
		
		Logger.log(Logger.TAG_BUG2GODOWNLOADER,
		           "\nBTD parse and tethering detection process took " + DateTimeOperator.getTimeStringFromMillis((System.currentTimeMillis() - start)) + "\n");
		
		// Check for tethering
		long now = System.currentTimeMillis();
		
		Logger.log(Logger.TAG_BUG2GODOWNLOADER, "Verifying Main file");
		
		if (mainParsed)
		{
			Logger.log(Logger.TAG_BUG2GODOWNLOADER, "Verifying Main file");
			if (btdParsed && mainParser.getTotalLogTime() < btdParser.getLongerDischargingPeriod().getDuration())
			{
				mainTether = false;
			}
			else
			{
				mainTether = mainParser.checkForTethering();
			}
			mainParser.showTetheringData();
		}
		else
		{
			Logger.log(Logger.TAG_BUG2GODOWNLOADER, "Could not parse Main log");
		}
		
		Logger.log(Logger.TAG_BUG2GODOWNLOADER,
		           "\nMain parse and tethering detection process took " + DateTimeOperator.getTimeStringFromMillis((System.currentTimeMillis() - now)) + "\n");
		
		tetherComment = "The user is *tethering a Wifi* network. Thus, this CR can be considered invalid for current drain analysis.\\n\\n";
		
		// Wi-Fi tethering is enabled for 10% or more from the discharge time
		if (btdTether)
		{
			tetherComment = tetherComment + "- Following BTD file, SAT has detected that Wi-Fi tethering is enabled for " + btdParser.tetherPercentage()
			                + "% of the discharge time.\\n\\n";
		}
		// Wi-Fi tethering is enabled for 10% or more from the discharge time
		if (mainTether)
		{
			tetherComment = tetherComment + "- Following main log file, SAT has detected that Wi-Fi tethering is enabled for "
			                + mainParser.getTetherPercentage() + "% of the discharge time.\\n";
			tetherComment = tetherComment + "Tethering periods found in main log:\\n";
			for (int i = 0; i < mainParser.getWifiPeriods().size(); i++)
			{
				if (mainParser.getWifiPeriods().get(i).getDuration() > 0)
				{
					tetherComment = tetherComment + "Period " + (i + 1) + ":\\n";
					tetherComment = tetherComment + "|" + mainParser.getWifiPeriods().get(i).startLine + "|\\n";
					tetherComment = tetherComment + "|" + mainParser.getWifiPeriods().get(i).endLine + "|\\n";
					tetherComment = tetherComment + "|Duration: " + DateTimeOperator.getTimeStringFromMillis(mainParser.getWifiPeriods().get(i).getDuration())
					                + "|\\n";
				}
			}
			
			tetherComment = tetherComment + "\\n";
		}
		
		Logger.log(Logger.TAG_BUG2GODOWNLOADER, "BTD Tether: " + btdTether);
		Logger.log(Logger.TAG_BUG2GODOWNLOADER, "Main Tether: " + mainTether);
		
		if (btdTether || mainTether)
		{
			Logger.log(Logger.TAG_BUG2GODOWNLOADER, "Closed as tethering");
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public boolean checkIfFalsePositive()
	{
		if (!btdParsed && !bugrepParsed)
		{
			Logger.log(Logger.TAG_CR_CHECKER, "Could not parse BTD nor Bugreport");
			return false;
		}
		
		System.out.println("btd parsed?: " + btdParsed);
		System.out.println("bugrep parsed?: " + bugrepParsed);
		boolean btdUptime = false;
		
		if (btdParsed)
		{
			btdUptime = btdParser.uptime();
			System.out.println("btd uptime: " + btdUptime);
			System.out.println("cd btd: " + btdParser.getAverageconsumeOff());
			System.out.println("len btd: " + btdParser.eblDecreasers().length());
			System.out.println("phone btd: " + btdParser.phoneCallPercentage());
		}
		if (bugrepParsed)
		{
			System.out.println("bugrep uptime: " + bugrepParser.checkIfWakelocks(btdUptime));
			System.out.println("cd btd: " + bugrepParser.getConsAvgOff());
			System.out.println("len btd: " + bugrepParser.eblDecreasedReasons().length());
		}
		
		// Get battery capacity from BTD file
		if (btdParsed && bugrepParsed)
		{
			if (btdParser.getBatCap() > bugrepParser.getBatCap())
			{
				bugrepParser.setBatCap(btdParser.getBatCap());
			}
			
			int cdThreashold = 100;
			String bugrepEblDrecresers = bugrepParser.eblDecreasedReasons();
			String btdEblDrecresers = bugrepParser.eblDecreasedReasons();
			Logger.log(Logger.TAG_CR_CHECKER, "BTD Threshold: " + btdParser.getThresholdInc());
			Logger.log(Logger.TAG_CR_CHECKER, "Bugrep Threshold: " + bugrepParser.getThresholdInc());
			cdThreashold += btdParser.getThresholdInc();
			cdThreashold += bugrepParser.getThresholdInc();
			if (cdThreashold > 155)
			{
				Logger.log(Logger.TAG_CR_CHECKER, "Calculated Threshold: " + cdThreashold);
				cdThreashold = 155;
				Logger.log(Logger.TAG_CR_CHECKER, "Threshold limited to 155");
			}
			else if (cdThreashold < 100)
			{
				Logger.log(Logger.TAG_CR_CHECKER, "Calculated Threshold: " + cdThreashold);
				cdThreashold = 115;
				Logger.log(Logger.TAG_CR_CHECKER, "Threshold set to 115");
			}
			else
			{
				Logger.log(Logger.TAG_CR_CHECKER, "Calculated Threshold: " + cdThreashold);
			}
			
			if ((btdParser.getAverageconsumeOff() <= cdThreashold || bugrepParser.getConsAvgOff() <= cdThreashold)
			    && (btdEblDrecresers.length() > 10 || bugrepEblDrecresers.length() > 10))
			{
				String comment = bugrepParser.currentDrainStatistics();
				comment += "\\n\\n" + btdParser.currentDrainStatistics();
				String eblDecresed = "\\n{panel:title=*Items that increases current drain and decreases EBL*|titleBGColor=#E9F2FF}\\n";
				eblDecresed += bugrepParser.eblDecreasedReasons();
				eblDecresed += btdParser.eblDecreasers();
				eblDecresed += "{panel}\\n";
				
				if (eblDecresed.split("\\\\n|\\n|\n").length > 2)
				{
					comment = comment + eblDecresed;
				}
				
				comment += "\\n- No current drain issues found in this CR.\\n\\nClosing as cancelled.";
				
				System.out.println("-- Comments:");
				System.out.println(comment.replaceAll("\\n", "\n"));
				System.out.println(btdParser.eblDecreasers());
				System.out.println(bugrepParser.eblDecreasedReasons());
				
				jira.assignIssue(cr.getJiraID());
				jira.addLabel(cr.getJiraID(), "sat_closed");
				jira.closeIssue(cr.getJiraID(), JiraSatApi.CANCELLED, comment);
				
				cr.setResolution(CANCELLED);
				cr.setAssignee(SharedObjs.getUser());
				if (SharedObjs.satDB != null)
					if (SharedObjs.satDB.existsAnalyzedCR(cr.getJiraID()) > 0)
					{
						SharedObjs.satDB.updateAnalyzedCR(cr);
					}
					else
					{
						SharedObjs.satDB.insertAnalyzedCR(cr);
					}
				
				SharedObjs.crsManagerPane.addLogLine("This CR is a false positive. Closing " + cr.getJiraID() + " as cancelled");
				
				Logger.log(Logger.TAG_BUG2GODOWNLOADER, "This CR is a false positive. Closing " + cr.getJiraID() + " as cancelled");
				
				return true;
			}
			else if ((btdParser.getAverageconsumeOff() <= 125 && bugrepParser.getConsAvgOff() <= 125) && btdParser.phoneCallPercentage() > 9)
			{
				String comment = bugrepParser.currentDrainStatistics();
				String eblDecresed = "{panel:title=*Items that increases current drain and decreases EBL*|titleBGColor=#E9F2FF}\\n";
				eblDecresed = eblDecresed + bugrepParser.eblDecreasedReasons();
				eblDecresed = eblDecresed + btdParser.eblDecreasers();
				eblDecresed = eblDecresed + "{panel}\\n";
				
				if (eblDecresed.split("\\\\n|\\n|\n").length > 2)
				{
					comment = comment + eblDecresed;
				}
				
				comment = comment + "\\n- No current drain issues found in this CR.\\n\\nClosing as cancelled.";
				
				System.out.println("-- Comments:");
				System.out.println(comment.replaceAll("\\n", "\n"));
				System.out.println(btdParser.eblDecreasers());
				System.out.println(bugrepParser.eblDecreasedReasons());
				
				jira.assignIssue(cr.getJiraID());
				jira.addLabel(cr.getJiraID(), "sat_closed");
				jira.closeIssue(cr.getJiraID(), JiraSatApi.CANCELLED, comment);
				
				cr.setResolution(CANCELLED);
				cr.setAssignee(SharedObjs.getUser());
				if (SharedObjs.satDB != null)
					if (SharedObjs.satDB.existsAnalyzedCR(cr.getJiraID()) > 0)
					{
						SharedObjs.satDB.updateAnalyzedCR(cr);
					}
					else
					{
						SharedObjs.satDB.insertAnalyzedCR(cr);
					}
				
				SharedObjs.crsManagerPane.addLogLine("This CR is a false positive. Closing " + cr.getJiraID() + " as cancelled");
				
				Logger.log(Logger.TAG_BUG2GODOWNLOADER, "This CR is a false positive. Closing " + cr.getJiraID() + " as cancelled");
				
				return true;
			}
			else if ((btdParser.getAverageconsumeOff() <= 70 || bugrepParser.getConsAvgOff() <= 70) && btdParser.uptime() == false
			         && bugrepParser.checkIfWakelocks(false) == false)
			{
				String comment = bugrepParser.currentDrainStatistics();
				
				comment = comment + "\\n- No current drain issues found in this CR.\\n\\nClosing as cancelled.";
				
				// System.out.println("-- Comments:");
				// System.out.println(comment.replaceAll("\\n", "\n"));
				
				jira.assignIssue(cr.getJiraID());
				jira.addLabel(cr.getJiraID(), "sat_closed");
				jira.closeIssue(cr.getJiraID(), JiraSatApi.CANCELLED, comment);
				
				cr.setResolution(CANCELLED);
				cr.setAssignee(SharedObjs.getUser());
				if (SharedObjs.satDB != null)
					if (SharedObjs.satDB.existsAnalyzedCR(cr.getJiraID()) > 0)
					{
						SharedObjs.satDB.updateAnalyzedCR(cr);
					}
					else
					{
						SharedObjs.satDB.insertAnalyzedCR(cr);
					}
				
				SharedObjs.crsManagerPane.addLogLine("This CR is a false positive. Closing " + cr.getJiraID() + " as cancelled");
				
				Logger.log(Logger.TAG_BUG2GODOWNLOADER, "This CR is a false positive. Closing " + cr.getJiraID() + " as cancelled");
				
				return true;
			}
		}
		else if (bugrepParsed)
		{
			if (bugrepParser.getConsAvgOff() <= 115 && bugrepParser.eblDecreasedReasons().length() > 10)
			{
				String comment = bugrepParser.currentDrainStatistics();
				String eblDecresed = "{panel:title=*Items that increases current drain and decreases EBL*|titleBGColor=#E9F2FF}\\n";
				eblDecresed = eblDecresed + bugrepParser.eblDecreasedReasons();
				eblDecresed = eblDecresed + "{panel}\\n";
				
				if (eblDecresed.split("\\\\n|\\n|\n").length > 2)
				{
					comment = comment + eblDecresed;
				}
				
				comment = comment + "\\n- No current drain issues found in this CR.\\n\\nClosing as cancelled.";
				
				jira.assignIssue(cr.getJiraID());
				jira.addLabel(cr.getJiraID(), "sat_closed");
				jira.closeIssue(cr.getJiraID(), JiraSatApi.CANCELLED, comment);
				
				cr.setResolution(CANCELLED);
				cr.setAssignee(SharedObjs.getUser());
				if (SharedObjs.satDB != null)
					if (SharedObjs.satDB.existsAnalyzedCR(cr.getJiraID()) > 0)
					{
						SharedObjs.satDB.updateAnalyzedCR(cr);
					}
					else
					{
						SharedObjs.satDB.insertAnalyzedCR(cr);
					}
				
				System.out.println("-- Comments:");
				System.out.println(comment.replaceAll("\\n", "\n"));
				
				SharedObjs.crsManagerPane.addLogLine("This CR is a false positive. Closing " + cr.getJiraID() + " as cancelled");
				
				Logger.log(Logger.TAG_BUG2GODOWNLOADER, "This CR is a false positive. Closing " + cr.getJiraID() + " as cancelled");
				
				return true;
			}
		}
		else if (btdParsed)
		{
			int cdThreashold = 110;
			String btdEblDrecresers = btdParser.eblDecreasers();
			Logger.log(Logger.TAG_CR_CHECKER, "BTD Threshold: " + btdParser.getThresholdInc());
			cdThreashold += btdParser.getThresholdInc();
			if (cdThreashold > 155)
			{
				Logger.log(Logger.TAG_CR_CHECKER, "Calculated Threshold: " + cdThreashold);
				cdThreashold = 155;
				Logger.log(Logger.TAG_CR_CHECKER, "Threshold limited to 155");
			}
			else if (cdThreashold < 100)
			{
				Logger.log(Logger.TAG_CR_CHECKER, "Calculated Threshold: " + cdThreashold);
				cdThreashold = 115;
				Logger.log(Logger.TAG_CR_CHECKER, "Threshold set to 115");
			}
			else
			{
				Logger.log(Logger.TAG_CR_CHECKER, "Calculated Threshold: " + cdThreashold);
			}
			
			if (btdParser.getAverageconsumeOff() <= cdThreashold && btdEblDrecresers.length() > 10)
			{
				String comment = "Bugreport could not be parsed or does not have device statistics\\n\\n";
				comment += btdParser.toJiraComment();
				comment += btdParser.currentDrainStatistics().replace("\n", "\\n");
				
				String eblDecresed = "{panel:title=*Items that increases current drain and decreases EBL*|titleBGColor=#E9F2FF}\\n";
				eblDecresed += btdParser.eblDecreasers().replace("\n", "\\n");
				eblDecresed += "{panel}\\n";
				
				if (eblDecresed.split("\\\\n|\\n|\n").length > 2)
				{
					comment = comment + eblDecresed;
				}
				else
				{
					Logger.log(Logger.TAG_BUG2GODOWNLOADER, "Could not detect EBL decreasers");
				}
				
				comment += "\\n- No current drain issues found in this CR.\\n\\nClosing as cancelled.";
				
				jira.assignIssue(cr.getJiraID());
				String output = jira.closeIssue(cr.getJiraID(), JiraSatApi.CANCELLED, comment);
				if (output.contains("Illegal"))
				{
					Logger.log(Logger.TAG_BUG2GODOWNLOADER, "Error trying to close CR");
					return false;
				}
				jira.addLabel(cr.getJiraID(), "sat_closed");
				
				cr.setResolution(CANCELLED);
				cr.setAssignee(SharedObjs.getUser());
				if (SharedObjs.satDB != null)
					if (SharedObjs.satDB.existsAnalyzedCR(cr.getJiraID()) > 0)
					{
						SharedObjs.satDB.updateAnalyzedCR(cr);
					}
					else
					{
						SharedObjs.satDB.insertAnalyzedCR(cr);
					}
				
				SharedObjs.crsManagerPane.addLogLine("This CR is a false positive. Closing " + cr.getJiraID() + " as cancelled");
				
				Logger.log(Logger.TAG_BUG2GODOWNLOADER, "1 This CR is a false positive. Closing " + cr.getJiraID() + " as cancelled");
				
				return true;
			}
			else if (btdParser.getAverageconsumeOff() <= 125 && btdParser.phoneCallPercentage() > 9)
			{
				String comment = btdParser.toJiraComment();
				String eblDecresed = "{panel:title=*Items that increases current drain and decreases EBL*|titleBGColor=#E9F2FF}\\n";
				eblDecresed += btdParser.eblDecreasers();
				eblDecresed += "{panel}\\n";
				
				if (eblDecresed.split("\\\\n|\\n|\n").length > 2)
				{
					comment = comment + eblDecresed;
				}
				
				comment += "\\n- No current drain issues found in this CR.\\n\\nClosing as cancelled.";
				
				System.out.println("-- Comments:");
				System.out.println(comment.replaceAll("\\n", "\n"));
				System.out.println(btdParser.eblDecreasers());
				
				jira.assignIssue(cr.getJiraID());
				jira.addLabel(cr.getJiraID(), "sat_closed");
				jira.closeIssue(cr.getJiraID(), JiraSatApi.CANCELLED, comment);
				
				cr.setResolution(CANCELLED);
				cr.setAssignee(SharedObjs.getUser());
				if (SharedObjs.satDB != null)
					if (SharedObjs.satDB.existsAnalyzedCR(cr.getJiraID()) > 0)
					{
						SharedObjs.satDB.updateAnalyzedCR(cr);
					}
					else
					{
						SharedObjs.satDB.insertAnalyzedCR(cr);
					}
				
				SharedObjs.crsManagerPane.addLogLine("This CR is a false positive. Closing " + cr.getJiraID() + " as cancelled");
				
				Logger.log(Logger.TAG_BUG2GODOWNLOADER, "2 This CR is a false positive. Closing " + cr.getJiraID() + " as cancelled");
				
				return true;
			}
			else if (btdParser.getAverageconsumeOff() <= 70 && btdParser.uptime() == false)
			{
				String comment = btdParser.toJiraComment();
				String eblDecresed = "{panel:title=*Items that increases current drain and decreases EBL*|titleBGColor=#E9F2FF}\\n";
				eblDecresed += btdParser.eblDecreasers();
				eblDecresed += "{panel}\\n";
				
				if (eblDecresed.split("\\\\n|\\n|\n").length > 2)
				{
					comment += eblDecresed;
				}
				
				comment += "\\n- No current drain issues found in this CR.\\n\\nClosing as cancelled.";
				
				// System.out.println("-- Comments:");
				// System.out.println(comment.replaceAll("\\n", "\n"));
				
				jira.assignIssue(cr.getJiraID());
				jira.addLabel(cr.getJiraID(), "sat_closed");
				jira.closeIssue(cr.getJiraID(), JiraSatApi.CANCELLED, comment);
				
				cr.setResolution(CANCELLED);
				cr.setAssignee(SharedObjs.getUser());
				if (SharedObjs.satDB != null)
					if (SharedObjs.satDB.existsAnalyzedCR(cr.getJiraID()) > 0)
					{
						SharedObjs.satDB.updateAnalyzedCR(cr);
					}
					else
					{
						SharedObjs.satDB.insertAnalyzedCR(cr);
					}
				
				SharedObjs.crsManagerPane.addLogLine("This CR is a false positive. Closing " + cr.getJiraID() + " as cancelled");
				
				Logger.log(Logger.TAG_BUG2GODOWNLOADER, "3 This CR is a false positive. Closing " + cr.getJiraID() + " as cancelled");
				
				return true;
			}
		}
		
		return false;
	}
	
	public boolean checkIfUptime()
	{
		uptimesComment = "";
		if (btdParsed)
		{
			if (mainParsed && mainParser.getTotalTetherTime() >= btdParser.getUptimes().getTotalTime() * 0.7)
			{
				return false;
			}
			else
			{
				if (btdParser.uptimeScOff())
				{
					uptimesComment = "{panel:title=*Long uptimes while screen continuously OFF*|titleBGColor=#E9F2FF}\\n";
					int i = 1;
					long total = 0;
					
					for (BtdUptimePeriod ut : btdParser.getUptimesScOff())
					{
						uptimesComment += "- *Uptime " + i + "*\\n";
						uptimesComment += ut.toJiraComment();
						total = total + ut.getDuration();
						i++;
					}
					uptimesComment += "\\n\\n||TOTAL TIME|| " + DateTimeOperator.getTimeStringFromMillis(total) + "|";
					
					uptimesComment += "\\n{panel}\\n\\n";
				}
				else if (btdParser.uptime())
				{
					uptimesComment = "{panel:title=*All long uptimes detected*|titleBGColor=#E9F2FF}\\n";
					int i = 1;
					long total = 0;
					
					for (BtdUptimePeriod ut : btdParser.getUptimes())
					{
						uptimesComment += "- *Uptime " + i + "*\\n";
						uptimesComment += ut.toJiraComment();
						total = total + ut.getDuration();
						i++;
					}
					uptimesComment += "\\n\\n||TOTAL TIME|| " + DateTimeOperator.getTimeStringFromMillis(total) + "|";
					
					uptimesComment += "\\n{panel}\\n\\n";
				}
			}
			
			if (uptimesComment.length() > 80)
			{
				return true;
			}
		}
		
		return false;
	}
	
	public boolean checkIfWakelocks()
	{
		wakelocksComment = "";
		boolean btdWake = false;
		
		if (btdParsed && btdParser.wakeLocks())
		{
			wakelocksComment = "{panel:title=*BTD wake locks detected*|titleBGColor=#E9F2FF}\\n";
			int i = 1;
			long total = 0;
			
			for (BtdWL ut : btdParser.getWakeLocks())
			{
				wakelocksComment += "- *Wakelock " + i + "*\\n";
				wakelocksComment += ut.toJiraComment();
				total = total + ut.getTotalTime();
				i++;
			}
			wakelocksComment += "\\n\\n*TOTAL HELD TIME:* " + DateTimeOperator.getTimeStringFromMillis(total) + "\\n";
			
			wakelocksComment += "\\n{panel}\\n\\n";
			
			btdWake = true;
		}
		
		boolean bugrepWakelocksDetected = bugrepParser.checkIfWakelocks(btdWake);
		
		if (bugrepWakelocksDetected)
		{
			wakelocksComment += "\\n\\n" + bugrepParser.getWakelocksComment();
			ArrayList<BugRepJavaWL> javaWkls = bugrepParser.getJavaWLs();
			ArrayList<BugRepKernelWL> kernelWkls = bugrepParser.getKernelWLs();
			
			// Wakelock dup
			if (wakelocksComment.length() > 50)
			{
				SharedObjs.crsManagerPane.addLogLine("Wakelock detected. Searching for similar issue ...");
				String dupCRs = "";
				dupComment = "";
				
				if (wakelocksComment.contains("Bugreport Java wake locks"))
				{
					for (int i = 0; i < 2; i++)
					{
						BugRepJavaWL wl = javaWkls.get(i);
						String jSONOutput = jira.query("project = IKSWM AND summary ~ \\\"" + wl.getProcessName() + "\\\" AND summary ~ \\\""
						                               + wl.getName().replace("*", "") + "\\\"");
						JiraQueryResult jqr = new JiraQueryResult(jSONOutput);
						
						if (jqr.getResultCount() == 1)
						{
							if (dupCRs.length() > 5)
							{
								dupCRs += ", " + jqr.getItems().get(0).getKey();
								dupComment += "\\n\\n" + wl.toJiraComment() + "Duplicated of " + jqr.getItems().get(0).getKey();
							}
							else
							{
								dupCRs = jqr.getItems().get(0).getKey();
								dupComment = "*Wakelock detected*\\n\\n" + wl.toJiraComment() + "Duplicated of " + jqr.getItems().get(0).getKey();
							}
						}
					}
				}
				
				if (wakelocksComment.contains("Bugreport Kernel wake locks"))
				{
					for (int i = 0; i < 2; i++)
					{
						BugRepKernelWL wl = kernelWkls.get(i);
						String jSONOutput = jira.query("project = IKSWM AND summary ~ \\\"" + wl.getName() + "\\\"");
						JiraQueryResult jqr = new JiraQueryResult(jSONOutput);
						
						if (jqr.getResultCount() == 1)
						{
							if (dupCRs.length() > 5)
							{
								dupCRs += ", " + jqr.getItems().get(0).getKey();
								dupComment += "\\n\\n" + wl.toJiraComment() + "Duplicated of " + jqr.getItems().get(0).getKey();
							}
							else
							{
								dupCRs = jqr.getItems().get(0).getKey();
								dupComment = "*Wakelock detected*\\n\\n" + wl.toJiraComment() + "Duplicated of " + jqr.getItems().get(0).getKey();
							}
						}
					}
				}
				
				if (dupCRs.length() > 5)
				{
					SharedObjs.crsManagerPane.addLogLine("Wakelock root detected, duplicating CR ...");
					jira.assignIssue(cr.getJiraID());
					jira.addLabel(cr.getJiraID(), "cd_auto");
					jira.addLabel(cr.getJiraID(), "sat_dupped");
					jira.dupIssue(cr.getJiraID(), dupCRs, dupComment);
					SharedObjs.crsManagerPane.addLogLine("CR duplicated to " + dupCRs);
					
					return true;
				}
			}
		}
		
		if (wakelocksComment.length() > 100)
		{
			return true;
		}
		
		return false;
	}
	
	// Getters and Setters
	public ArrayList<String> getIncompleteFiles()
	{
		return incompleteFiles;
	}
}
