package supportive;


import java.io.File;
import java.util.ArrayList;

import objects.CrItem;
import supportive.preanalyzers.btdparser.BtdParser;
import supportive.preanalyzers.logsparser.BugrepParser;
import supportive.preanalyzers.logsparser.MainParser;
import core.Logger;
import core.SharedObjs;


public class CrChecker
{
	private static final String INCOMPLETE = "Incomplete";
	private static final String DUPLICATE  = "Duplicate";
	private static final String CANCELLED  = "Cancelled";
	private static final String INVALID    = "Invalid";
	private String              crPath;
	private String              falsePositiveComment;
	private String              tetherComment;
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
	
	public CrChecker(String crPath)
	{
		this.crPath = crPath;
	}
	
	public boolean checkCR()
	{
		File file = new File(crPath);
		cr = SharedObjs.crsManagerPane.getCrsList().getCrByB2gId(file.getName());
		
		if (cr != null)
		{
			long start = System.currentTimeMillis();
			SharedObjs.crsManagerPane.addLogLine("Checking if incomplete ...");
			
			if (checkIfIncomplete())
			{
				JiraSatApi jira = new JiraSatApi(JiraSatApi.DEFAULT_JIRA_URL,
				                                 SharedObjs.getUser(),
				                                 SharedObjs.getPass());
				jira.assignIssue(cr.getJiraID());
				jira.closeIssue(cr.getJiraID(), JiraSatApi.INCOMPLETE,
				                "The text logs are missing. Could not perform a complete analysis.");
				jira.addLabel(cr.getJiraID(), "sat_closed");
				
				cr.setResolution(INCOMPLETE);
				cr.setAssignee(SharedObjs.getUser());
				SharedObjs.satDB.insertClosedCR(cr);
				
				SharedObjs.crsManagerPane.addLogLine("Logs are missing. Closing CR " + cr.getJiraID()
				                                     + " as incomplete");
				
				Logger.log(Logger.TAG_BUG2GODOWNLOADER, "Done for " + file.getAbsolutePath()
				                                        + ". Closed as incomplete");
				
				return true;
			}
			
			// Try to parse log files
			btdParser = new BtdParser(crPath);
			SharedObjs.crsManagerPane.addLogLine("Parsing BTD data ...");
			btdParsed = btdParser.parse();
			mainParser = new MainParser(crPath);
			SharedObjs.crsManagerPane.addLogLine("Parsing Main log data ...");
			mainParsed = mainParser.parse();
			bugrepParser = new BugrepParser(crPath);
			SharedObjs.crsManagerPane.addLogLine("Parsing Bugreport log data ...");
			bugrepParsed = bugrepParser.parse();
			
			SharedObjs.crsManagerPane.addLogLine("Checking for tethering ...");
			if (checkForTethering())
			{
				JiraSatApi jira = new JiraSatApi(JiraSatApi.DEFAULT_JIRA_URL,
				                                 SharedObjs.getUser(),
				                                 SharedObjs.getPass());
				
				jira.assignIssue(cr.getJiraID());
				jira.closeIssue(cr.getJiraID(), JiraSatApi.INVALID, tetherComment);
				jira.addLabel(cr.getJiraID(), "sat_closed");
				
				cr.setResolution(INVALID);
				cr.setAssignee(SharedObjs.getUser());
				SharedObjs.satDB.insertClosedCR(cr);
				
				SharedObjs.crsManagerPane.addLogLine("Tethering detected. Closing CR " + cr.getJiraID()
				                                     + " as invalid");
				
				Logger.log(Logger.TAG_BUG2GODOWNLOADER, "Done for " + file.getAbsolutePath()
				                                        + ". Closed as incomplete");
				
				return true;
			}
			
			SharedObjs.crsManagerPane.addLogLine("Checking if false positive ...");
			if (checkIfFalsePositive())
			{
				return true;
			}
			
			SharedObjs.crsManagerPane.addLogLine("Nothing was detected. "
			                                     + DateTimeOperator.getTimeStringFromMillis((System.currentTimeMillis() - start)));
			Logger.log(Logger.TAG_FALSE_POSITIVE,
			           DateTimeOperator.getTimeStringFromMillis((System.currentTimeMillis() - start)));
			
			return false;
		}
		
		Logger.log(Logger.TAG_BUG2GODOWNLOADER,
		           file.getAbsolutePath()
		                           + " was not pre analyzed because this CR is not on the downloaded CRs list");
		return false;
	}
	
	// Checkers ------------------------------------------------------------------------------
	private boolean checkIfIncomplete()
	{
		incompleteFiles = new ArrayList<String>();
		filesNames = new ArrayList<String>();
		files = new ArrayList<File>();
		
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
		}
		
		// Check files
		if (files.contains(new File(crPath + "/aplogcat-main.txt")))
		{
			File f = new File(crPath + "/aplogcat-main.txt");
			if (f.length() / 1048576 < 2)
			{
				incompleteFiles.add("main");
				Logger.log(Logger.TAG_CR_CHECKER, "Main file is too short");
			}
		}
		else
		{
			incompleteFiles.add("main");
		}
		
		if (files.contains(new File(crPath + "/aplogcat-system.txt")))
		{
			File f = new File(crPath + "/aplogcat-system.txt");
			if (f.length() / 1048576 < 2)
			{
				incompleteFiles.add("system");
				Logger.log(Logger.TAG_CR_CHECKER, "System file is too short");
			}
		}
		else
		{
			incompleteFiles.add("system");
		}
		
		if (files.contains(new File(crPath + "/aplogcat-kernel.txt")))
		{
			File f = new File(crPath + "/aplogcat-kernel.txt");
			if (f.length() / 1048576 < 2)
			{
				incompleteFiles.add("kernel");
				Logger.log(Logger.TAG_CR_CHECKER, "Kernel file is too short");
			}
		}
		else
		{
			incompleteFiles.add("kernel");
		}
		
		if (files.contains(new File(crPath + "/aplogcat-radio.txt")))
		{
			File f = new File(crPath + "/aplogcat-radio.txt");
			if (f.length() / 1048576 < 2)
			{
				incompleteFiles.add("radio");
				Logger.log(Logger.TAG_CR_CHECKER, "Radio file is too short");
			}
		}
		else
		{
			incompleteFiles.add("radio");
		}
		
		if (incompleteFiles.size() == 4)
			return true;
		else
			return false;
	}
	
	private boolean checkForTethering()
	{
		long start = System.currentTimeMillis();
		
		System.out.println();
		Logger.log(Logger.TAG_BUG2GODOWNLOADER, "-----------------");
		System.out.println();
		
		Logger.log(Logger.TAG_BUG2GODOWNLOADER, "Verifying BTD");
		
		if (btdParsed)
		{
			btdTether = btdParser.checkForTethering();
			Logger.log(Logger.TAG_BUG2GODOWNLOADER, "Tethering issue? " + btdTether);
			btdParser.close();
		}
		else
		{
			Logger.log(Logger.TAG_BUG2GODOWNLOADER, "Could not parse BTD file");
		}
		
		Logger.log(Logger.TAG_BUG2GODOWNLOADER,
		           "\nBTD parse and tethering detection process took "
		                           + DateTimeOperator.getTimeStringFromMillis((System.currentTimeMillis() - start))
		                           + "\n");
		
		// Check for tethering
		long now = System.currentTimeMillis();
		
		Logger.log(Logger.TAG_BUG2GODOWNLOADER, "Verifying Main file");
		
		if (mainParsed)
		{
			mainParser.showAcquiredData();
			mainTether = mainParser.checkForTethering();
			mainParser.showTetheringData();
		}
		else
		{
			Logger.log(Logger.TAG_BUG2GODOWNLOADER, "Could not parse Main log");
		}
		
		Logger.log(Logger.TAG_BUG2GODOWNLOADER,
		           "\nMain parse and tethering detection process took "
		                           + DateTimeOperator.getTimeStringFromMillis((System.currentTimeMillis() - now))
		                           + "\n");
		
		tetherComment = "The user is *tethering a Wifi* network. Thus, this CR can be considered invalid for current drain analysis.\\n\\n";
		
		// Wi-Fi tethering is enabled for 10% or more from the discharge time
		if (btdTether)
		{
			tetherComment = tetherComment
			                + "- Following BTD file, SAT has detected that Wi-Fi tethering is enabled for "
			                + btdParser.getTetherPercentage() + "% of the discharge time.\\n\\n";
		}
		// Wi-Fi tethering is enabled for 10% or more from the discharge time
		if (mainTether)
		{
			tetherComment = tetherComment
			                + "- Following main log file, SAT has detected that Wi-Fi tethering is enabled for "
			                + mainParser.getTetherPercentage() + "% of the discharge time.\\n";
			tetherComment = tetherComment + "Tethering periods found in main log:\\n";
			for (int i = 0; i < mainParser.getWifiPeriods().size(); i++)
			{
				if (mainParser.getWifiPeriods().get(i).getDuration() > 0)
				{
					tetherComment = tetherComment + "Period " + (i + 1) + ":\\n";
					tetherComment = tetherComment + "|" + mainParser.getWifiPeriods().get(i).startLine
					                + "|\\n";
					tetherComment = tetherComment + "|" + mainParser.getWifiPeriods().get(i).endLine + "|\\n";
					tetherComment = tetherComment
					                + "|Duration: "
					                + DateTimeOperator.getTimeStringFromMillis(mainParser.getWifiPeriods()
					                                                                     .get(i)
					                                                                     .getDuration())
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
	
	private boolean checkIfFalsePositive()
	{
		if (!btdParsed && !bugrepParsed)
		{
			return false;
		}
		
		boolean upTime = false;
		boolean ntTime = false;
		boolean lowEbl = false;
		
		for (String label : cr.getLabels())
		{
			if (label.equals("high_background_uptime_percentage")
			    || label.equals("high_background_uptime_percentage_btd"))
			{
				upTime = true;
			}
			
			if (label.equals("high_background_current_drain_btd")
			    || label.equals("high_background_current_drain"))
			{
				lowEbl = true;
			}
			
			if (label.equals("high_nt_current_drain"))
			{
				ntTime = true;
			}
		}
		
		// Get battery capacity from BTD file
		if (!bugrepParser.getCommentReport().equals(""))
			if (btdParsed && bugrepParsed)
			{
				bugrepParser.setBatCap(btdParser.getBatCap());
				
				if ((btdParser.getAverageconsumeOff() <= 110 && bugrepParser.getConsAvgOff() <= 110)
				    && upTime == false)
				{
					JiraSatApi jira = new JiraSatApi(JiraSatApi.DEFAULT_JIRA_URL,
					                                 SharedObjs.getUser(),
					                                 SharedObjs.getPass());
					
					jira.assignIssue(cr.getJiraID());
					jira.closeIssue(cr.getJiraID(), JiraSatApi.CANCELLED, bugrepParser.getCommentReport());// TODO add calling time
					jira.addLabel(cr.getJiraID(), "sat_closed");
					
					cr.setResolution(CANCELLED);
					cr.setAssignee(SharedObjs.getUser());
					SharedObjs.satDB.insertClosedCR(cr);
					
					System.out.println(bugrepParser.getCommentReport());
					
					SharedObjs.crsManagerPane.addLogLine("This CR is a false positive. Closing "
					                                     + cr.getJiraID() + " as cancelled");
					
					Logger.log(Logger.TAG_BUG2GODOWNLOADER,
					           "This CR is a false positive. Closing " + cr.getJiraID() + " as cancelled");
					
					return true;
				}
				else if (btdParser.getAverageconsumeOff() < 100 && bugrepParser.getConsAvgOff() < 100
				         && lowEbl && btdParser.getAverageconsumeOn() > 800
				         && bugrepParser.getConsAvgOn() > 800)
				{
					
				}
			}
			else if (bugrepParsed)
			{
				if (btdParser.getAverageconsumeOff() <= 110 && bugrepParser.getConsAvgOff() <= 110
				    && upTime == false)
				{
					JiraSatApi jira = new JiraSatApi(JiraSatApi.DEFAULT_JIRA_URL,
					                                 SharedObjs.getUser(),
					                                 SharedObjs.getPass());
					
					jira.assignIssue(cr.getJiraID());
					jira.closeIssue(cr.getJiraID(), JiraSatApi.CANCELLED, bugrepParser.getCommentReport());// TODO
					jira.addLabel(cr.getJiraID(), "sat_closed");
					
					cr.setResolution(CANCELLED);
					cr.setAssignee(SharedObjs.getUser());
					SharedObjs.satDB.insertClosedCR(cr);
					
					System.out.println(bugrepParser.getCommentReport());
					
					SharedObjs.crsManagerPane.addLogLine("This CR is a false positive. Closing "
					                                     + cr.getJiraID() + " as cancelled");
					
					Logger.log(Logger.TAG_BUG2GODOWNLOADER,
					           "This CR is a false positive. Closing " + cr.getJiraID() + " as cancelled");
					
					return true;
				}
				else if (btdParser.getAverageconsumeOff() < 100 && bugrepParser.getConsAvgOff() < 100
				         && lowEbl && btdParser.getAverageconsumeOn() > 800
				         && bugrepParser.getConsAvgOn() > 800)
				{
					
				}
			}
		
		return false;
	}
	
	// Getters and Setters
	public ArrayList<String> getIncompleteFiles()
	{
		return incompleteFiles;
	}
}
