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
	private String              tetherComment;
	private String              uptimesComment;
	private String              wakelocksComment;
	private boolean             btdParsed;
	private boolean             mainParsed;
	private boolean             bugrepParsed;
	private boolean             btdTether;
	private boolean             mainTether;
	private int                 batThreshold;
	
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
		batThreshold = 110;
	}
	
	public boolean checkCR()
	{
		File file = new File(crPath);
		cr = SharedObjs.getCrsList().getCrByB2gId(file.getName());
		
		if (cr != null)
		{
			long start = System.currentTimeMillis();
			
			SharedObjs.crsManagerPane.addLogLine("Adding pre analyzed label ...");
			jira = new JiraSatApi(JiraSatApi.DEFAULT_JIRA_URL, SharedObjs.getUser(), SharedObjs.getPass());
			jira.addLabel(cr.getJiraID(), "sat_pre_analyzed");
			
			SharedObjs.crsManagerPane.addLogLine("Checking if incomplete ...");
			if (checkIfIncomplete())
			{
				jira.assignIssue(cr.getJiraID());
				jira.closeIssue(cr.getJiraID(), JiraSatApi.INCOMPLETE,
				                "The text logs are missing. Could not perform a complete analysis.");
				jira.addLabel(cr.getJiraID(), "sat_closed");
				
				cr.setResolution(INCOMPLETE);
				cr.setAssignee(SharedObjs.getUser());
				SharedObjs.satDB.insertAnalyzedCR(cr);
				
				SharedObjs.crsManagerPane.addLogLine("Logs are missing. Closing CR " + cr.getJiraID()
				                                     + " as incomplete");
				
				Logger.log(Logger.TAG_BUG2GODOWNLOADER, "Done for " + file.getAbsolutePath()
				                                        + ". Closed as incomplete");
				
				return true;
			}
			
			// Try to parse log files ---------------------------------------------------
			// Parse BTD
			btdParser = new BtdParser(crPath);
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
			}
			else
			{
				SharedObjs.crsManagerPane.addLogLine("Not possible to parse Bugreport");
			}
			
			// Check for issues ---------------------------------------------------------------------------------------------------------
			if (cr.getLabels().contains("high_background_uptime_percentage")
			    || cr.getLabels().contains("high_background_uptime_percentage_btd"))
			{
				SharedObjs.crsManagerPane.addLogLine("Very high uptime case, SAT will list\n\tthe top wakelocks as a comment in the CR");
				
				int n = 0;
				String commentWakeLocks = "";
				
				if (bugrepParsed)
				{
					bugrepParser.checkIfWakelocks(false);
					if (bugrepParser.getKernelWLs().size() > 0)
					{
						commentWakeLocks += "\\n\\n" + bugrepParser.getWakelocksComment();
						commentWakeLocks += "\\n\\n";
					}
				}
				
				if (btdParsed && btdParser.getWakeLocks().size() > 0)
				{
					commentWakeLocks = commentWakeLocks
					                   + "{panel:title=*BTD kernel stuck wake locks:*|titleBGColor=#E9F2FF}\\n{html}\\n";
					
					String commentPattern1 = "<p> <b>Name: </b>#name# <br/> <b>Total time: </b>#ttime#<br/> <b>Longer Period: </b>#longer#</p>";
					String commentPattern2 = "<span style=\\\"padding-left:2em;\\\"> <b> Period ##: </b> <br/> </span><span style=\\\"padding-left:4em;\\\">"
					                         + "<b>Start:</b> #starttime#<br/> </span><span style=\\\"padding-left:4em;\\\"> <b>Stop:</b> #stoptime# <br/> </span>"
					                         + "<span style=\\\"padding-left:4em;\\\"> <b>Duration:</b> #duration# <br/> </span>";
					
					for (BtdWL wl : btdParser.getWakeLocks())
					{
						commentWakeLocks = commentWakeLocks
						                   + commentPattern1.replace("#name#", wl.getName())
						                                    .replace("#ttime#",
						                                             DateTimeOperator.getTimeStringFromMillis(wl.getTotalTime()))
						                                    .replace("#longer#",
						                                             DateTimeOperator.getTimeStringFromMillis(wl.getLongerPeriod()));
						for (int i = 0; i < wl.getDataList().size(); i++)
						{
							commentWakeLocks = commentWakeLocks
							                   + commentPattern2.replace("##", "" + (i + 1))
							                                    .replace("#starttime#",
							                                             ""
							                                                             + BtdParser.formatDate(BtdParser.generateDate(wl.getDataList()
							                                                                                                             .get(i)
							                                                                                                             .getStart())))
							                                    .replace("#stoptime#",
							                                             BtdParser.formatDate(BtdParser.generateDate(wl.getDataList()
							                                                                                           .get(i)
							                                                                                           .getStop())))
							                                    .replace("#duration#",
							                                             DateTimeOperator.getTimeStringFromMillis(wl.getDataList()
							                                                                                        .get(i)
							                                                                                        .getDuration()));
						}
						commentWakeLocks = commentWakeLocks + "\\n<hr>\\n";
					}
					commentWakeLocks = commentWakeLocks + "\\n{html}";
					commentWakeLocks = commentWakeLocks + "\\n{panel}\\n\\n\\n";
				}
				
				System.out.println("\n\n\n" + commentWakeLocks.replace("\\n", "\n") + "\n\n");
				
				if (commentWakeLocks.length() > 10)
				{
					jira.addComment(cr.getJiraID(), commentWakeLocks);
					Logger.log(Logger.TAG_CR_CHECKER, "Wake locks detected");
				}
				else
				{
					jira.addComment(cr.getJiraID(), "SAT could not find wake locks data");
					Logger.log(Logger.TAG_CR_CHECKER, "SAT could not find wake locks data");
				}
			}
			
			else
			{
				SharedObjs.crsManagerPane.addLogLine("Checking for wakelocks ...");
				if (checkIfWakelocks())
				{
					if (checkIfUptime())
					{
						wakelocksComment += "\\n\\n" + uptimesComment;
					}
					jira.addComment(cr.getJiraID(), wakelocksComment);
					
					cr.setAssignee(SharedObjs.getUser());
					SharedObjs.satDB.insertAnalyzedCR(cr);
					
					SharedObjs.crsManagerPane.addLogLine("Wakelocks detected. Needs manual analysis.");
					
					Logger.log(Logger.TAG_BUG2GODOWNLOADER, "Done for " + file.getAbsolutePath()
					                                        + ". Wakelocks detected. Needs manual analysis.");
					
					return false;
				}
				
				SharedObjs.crsManagerPane.addLogLine("Checking for tethering ...");
				if (checkIfTethering())
				{
					jira.assignIssue(cr.getJiraID());
					jira.closeIssue(cr.getJiraID(), JiraSatApi.INVALID, tetherComment);
					jira.addLabel(cr.getJiraID(), "sat_closed");
					
					cr.setResolution(INVALID);
					cr.setAssignee(SharedObjs.getUser());
					SharedObjs.satDB.insertAnalyzedCR(cr);
					
					SharedObjs.crsManagerPane.addLogLine("Tethering detected. Closing CR " + cr.getJiraID()
					                                     + " as invalid");
					
					Logger.log(Logger.TAG_BUG2GODOWNLOADER, "Done for " + file.getAbsolutePath()
					                                        + ". Closed as invalid");
					
					return true;
				}
				
				SharedObjs.crsManagerPane.addLogLine("Checking for uptime ...");
				if (checkIfUptime())
				{
					jira.addComment(cr.getJiraID(),
					                "Some long uptimes were detected. This CR shall be manually analized in order to ensure if there are issues or not in this CR.\\n\\n"
					                                + uptimesComment);
					
					cr.setAssignee(SharedObjs.getUser());
					SharedObjs.satDB.insertAnalyzedCR(cr);
					
					SharedObjs.crsManagerPane.addLogLine("Uptimes detected. Needs manual analysis.");
					
					Logger.log(Logger.TAG_BUG2GODOWNLOADER, "Done for " + file.getAbsolutePath()
					                                        + ". Uptimes detected. Needs manual analysis.");
					
					return false;
				}
				
				SharedObjs.crsManagerPane.addLogLine("Checking if false positive ...");
				if (checkIfFalsePositive())
				{
					return true;
				}
			}
			
			SharedObjs.crsManagerPane.addLogLine("Nothing was detected. "
			                                     + DateTimeOperator.getTimeStringFromMillis((System.currentTimeMillis() - start)));
			
			String comment = bugrepParser.currentDrainStatistics();
			String eblDecresed = "{panel:title=*Items that increases current drain and decreases EBL*|titleBGColor=#E9F2FF}\\n";
			eblDecresed = eblDecresed + bugrepParser.eblDecreasedReasons();
			eblDecresed = eblDecresed + btdParser.eblDecreasers();
			eblDecresed = eblDecresed + "{panel}\\n";
			if (eblDecresed.split("\\\\n|\\n|\n").length > 2)
			{
				comment = comment + eblDecresed;
			}
			
			jira.addComment(cr.getJiraID(), comment);
			
			Logger.log(Logger.TAG_FALSE_POSITIVE,
			           DateTimeOperator.getTimeStringFromMillis((System.currentTimeMillis() - start)));
			
			SharedObjs.satDB.insertAnalyzedCR(cr);
			
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
		           "\nBTD parse and tethering detection process took "
		                           + DateTimeOperator.getTimeStringFromMillis((System.currentTimeMillis() - start))
		                           + "\n");
		
		// Check for tethering
		long now = System.currentTimeMillis();
		
		Logger.log(Logger.TAG_BUG2GODOWNLOADER, "Verifying Main file");
		
		if (mainParsed)
		{
			Logger.log(Logger.TAG_BUG2GODOWNLOADER, "Verifying Main file");
			if (btdParsed
			    && mainParser.getTotalLogTime() < btdParser.getLongerDischargingPeriod().getDuration())
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
		           "\nMain parse and tethering detection process took "
		                           + DateTimeOperator.getTimeStringFromMillis((System.currentTimeMillis() - now))
		                           + "\n");
		
		tetherComment = "The user is *tethering a Wifi* network. Thus, this CR can be considered invalid for current drain analysis.\\n\\n";
		
		// Wi-Fi tethering is enabled for 10% or more from the discharge time
		if (btdTether)
		{
			tetherComment = tetherComment
			                + "- Following BTD file, SAT has detected that Wi-Fi tethering is enabled for "
			                + btdParser.tetherPercentage() + "% of the discharge time.\\n\\n";
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
	
	public boolean checkIfFalsePositive()
	{
		if (!btdParsed && !bugrepParsed)
		{
			Logger.log(Logger.TAG_CR_CHECKER, "Could not parse BTD nor Bugreport");
			return false;
		}
		
		boolean upTime = false;
		boolean ntTime = false;
		boolean lowEbl = false;
		boolean highCurr = false;
		
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
				highCurr = true;
			}
			
			if (label.equals("low_ebl_btd") || label.equals("low_ebl"))
			{
				lowEbl = true;
			}
			
			if (label.equals("high_nt_current_drain"))
			{
				ntTime = true;
			}
		}
		
		// Get battery capacity from BTD file
		
		if (btdParsed && bugrepParsed)
		{
			if (btdParser.getBatCap() > bugrepParser.getBatCap())
			{
				bugrepParser.setBatCap(btdParser.getBatCap());
			}
			
			if ((btdParser.getAverageconsumeOff() <= 115 && bugrepParser.getConsAvgOff() <= 115)
			    && (btdParser.eblDecreasers().length() > 10 || bugrepParser.eblDecreasedReasons().length() > 10))
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
				
				comment = comment
				          + "\\n- No current drain issues found in this CR.\\n\\nClosing as cancelled.";
				
				System.out.println("-- Comments:");
				System.out.println(comment.replaceAll("\\n", "\n"));
				System.out.println(btdParser.eblDecreasers());
				System.out.println(bugrepParser.eblDecreasedReasons());
				
				jira.assignIssue(cr.getJiraID());
				jira.closeIssue(cr.getJiraID(), JiraSatApi.CANCELLED, comment);
				jira.addLabel(cr.getJiraID(), "sat_closed");
				
				cr.setResolution(CANCELLED);
				cr.setAssignee(SharedObjs.getUser());
				SharedObjs.satDB.insertAnalyzedCR(cr);
				
				SharedObjs.crsManagerPane.addLogLine("This CR is a false positive. Closing " + cr.getJiraID()
				                                     + " as cancelled");
				
				Logger.log(Logger.TAG_BUG2GODOWNLOADER,
				           "This CR is a false positive. Closing " + cr.getJiraID() + " as cancelled");
				
				return true;
			}
			else if ((btdParser.getAverageconsumeOff() <= 125 && bugrepParser.getConsAvgOff() <= 125)
			         && btdParser.phoneCallPercentage() > 9)
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
				
				comment = comment
				          + "\\n- No current drain issues found in this CR.\\n\\nClosing as cancelled.";
				
				System.out.println("-- Comments:");
				System.out.println(comment.replaceAll("\\n", "\n"));
				System.out.println(btdParser.eblDecreasers());
				System.out.println(bugrepParser.eblDecreasedReasons());
				
				jira.assignIssue(cr.getJiraID());
				jira.closeIssue(cr.getJiraID(), JiraSatApi.CANCELLED, comment);
				jira.addLabel(cr.getJiraID(), "sat_closed");
				
				cr.setResolution(CANCELLED);
				cr.setAssignee(SharedObjs.getUser());
				SharedObjs.satDB.insertAnalyzedCR(cr);
				
				SharedObjs.crsManagerPane.addLogLine("This CR is a false positive. Closing " + cr.getJiraID()
				                                     + " as cancelled");
				
				Logger.log(Logger.TAG_BUG2GODOWNLOADER,
				           "This CR is a false positive. Closing " + cr.getJiraID() + " as cancelled");
			}
		}
		else if (bugrepParsed)
		{
			if (bugrepParser.getConsAvgOff() <= 110 && upTime == false)
			{
				String comment = bugrepParser.currentDrainStatistics();
				String eblDecresed = "{panel:title=*Items that increases current drain and decreases EBL*|titleBGColor=#E9F2FF}\\n";
				eblDecresed = eblDecresed + bugrepParser.eblDecreasedReasons();
				eblDecresed = eblDecresed + "{panel}\\n";
				
				if (eblDecresed.split("\\\\n|\\n|\n").length > 2)
				{
					comment = comment + eblDecresed;
				}
				
				comment = comment
				          + "\\n- No current drain issues found in this CR.\\n\\nClosing as cancelled.";
				
				jira.assignIssue(cr.getJiraID());
				jira.closeIssue(cr.getJiraID(), JiraSatApi.CANCELLED, comment);
				jira.addLabel(cr.getJiraID(), "sat_closed");
				
				cr.setResolution(CANCELLED);
				cr.setAssignee(SharedObjs.getUser());
				SharedObjs.satDB.insertAnalyzedCR(cr);
				
				System.out.println(comment);
				
				SharedObjs.crsManagerPane.addLogLine("This CR is a false positive. Closing " + cr.getJiraID()
				                                     + " as cancelled");
				
				Logger.log(Logger.TAG_BUG2GODOWNLOADER,
				           "This CR is a false positive. Closing " + cr.getJiraID() + " as cancelled");
				
				return true;
			}
		}
		else if (btdParsed)
		{
			if (btdParser.getAverageconsumeOff() <= 110 && upTime == false)
			{
				String comment = "Bugreport could not be parsed or does not have device statistics\\n\\n"
				                 + "{panel:title=*Items that increases current drain and decreases EBL*|titleBGColor=#E9F2FF}\\n";
				comment = comment + btdParser.parseResult().replaceAll("\n|\r", "\\n");
				comment = comment + "\\n{panel}\\n";
				
				String eblDecresed = "{panel:title=*Items that increases current drain and decreases EBL*|titleBGColor=#E9F2FF}\\n";
				eblDecresed = eblDecresed + btdParser.eblDecreasers();
				eblDecresed = eblDecresed + "{panel}\\n";
				
				if (eblDecresed.split("\\\\n|\\n|\n").length > 2)
				{
					comment = comment + eblDecresed;
				}
				else
				{
					Logger.log(Logger.TAG_BUG2GODOWNLOADER, "Could not detect EBL decreasers");
				}
				
				comment = comment
				          + "\\n- No current drain issues found in this CR.\\n\\nClosing as cancelled.";
				
				jira.assignIssue(cr.getJiraID());
				jira.closeIssue(cr.getJiraID(), JiraSatApi.CANCELLED, comment);
				jira.addLabel(cr.getJiraID(), "sat_closed");
				
				cr.setResolution(CANCELLED);
				cr.setAssignee(SharedObjs.getUser());
				SharedObjs.satDB.insertAnalyzedCR(cr);
				
				System.out.println(comment);
				
				SharedObjs.crsManagerPane.addLogLine("This CR is a false positive. Closing " + cr.getJiraID()
				                                     + " as cancelled");
				
				Logger.log(Logger.TAG_BUG2GODOWNLOADER,
				           "This CR is a false positive. Closing " + cr.getJiraID() + " as cancelled");
				
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
				if (btdParser.uptime())
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
					uptimesComment += "\\n\\n||TOTAL TIME|| "
					                  + DateTimeOperator.getTimeStringFromMillis(total) + "|";
					
					uptimesComment += "\\n{panel}\\n\\n";
				}
				
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
					uptimesComment += "\\n\\n||TOTAL TIME|| " + DateTimeOperator.getTimeStringFromMillis(total)
					                  + "|";
					
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
			wakelocksComment += "\\n\\n*TOTAL HELD TIME:* " + DateTimeOperator.getTimeStringFromMillis(total)
			                    + "\\n";
			
			wakelocksComment += "\\n{panel}\\n\\n";
			
			btdWake = true;
		}
		
		if (bugrepParser.checkIfWakelocks(btdWake))
		{
			wakelocksComment += "\\n\\n" + bugrepParser.getWakelocksComment();
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
