package supportive;


import java.io.File;
import java.io.IOException;

import objects.CrItem;
import supportive.preanalyzers.CheckIfIncomplete;
import supportive.preanalyzers.btdparser.BtdParser;
import supportive.preanalyzers.logsparser.MainParser;
import core.Logger;
import core.SharedObjs;


public class CrChecker
{
	String  crPath;
	String  tetherComment;
	boolean btdTether;
	boolean mainTether;
	
	public CrChecker(String crPath)
	{
		this.crPath = crPath;
	}
	
	public boolean checkCR()
	{
		if (checkIfIncomplete())
		{
			File file = new File(crPath);
			CrItem cr = SharedObjs.crsManagerPane.getCrsList().getCrByB2gId(file.getName());
			
			if (cr != null)
			{
				JiraSatApi jira = new JiraSatApi(JiraSatApi.DEFAULT_JIRA_URL,
				                                 SharedObjs.getUser(),
				                                 SharedObjs.getPass());
				
				jira.closeIssue(cr.getJiraID(), JiraSatApi.INCOMPLETE,
				                "The text logs are missing/incomplete. Could not perform a complete analysis.");
				
				SharedObjs.crsManagerPane.addLogLine("Logs are missing. Closing CR " + cr.getJiraID()
				                                     + " as incomplete");
				
				Logger.log(Logger.TAG_BUG2GODOWNLOADER, "Done for " + file.getAbsolutePath()
				                                        + ". Closed as incomplete");
				
				return true;
			}
			else
			{
				Logger.log(Logger.TAG_BUG2GODOWNLOADER,
				           "Done for "
				                           + file.getAbsolutePath()
				                           + ". It is incomplete but the CR could not be found\nin crs list. It stills opened on Jira.");
			}
		}
		
		if (checkForTethering())
		{
			File file = new File(crPath);
			CrItem cr = SharedObjs.crsManagerPane.getCrsList().getCrByB2gId(file.getName());
			
			if (cr != null)
			{
				JiraSatApi jira = new JiraSatApi(JiraSatApi.DEFAULT_JIRA_URL,
				                                 SharedObjs.getUser(),
				                                 SharedObjs.getPass());
				
				jira.closeIssue(cr.getJiraID(), JiraSatApi.INVALID,
				                tetherComment);
				
				SharedObjs.crsManagerPane.addLogLine("Tethering detected. Closing CR " + cr.getJiraID()
				                                     + " as invalid");
				
				Logger.log(Logger.TAG_BUG2GODOWNLOADER, "Done for " + file.getAbsolutePath()
				                                        + ". Closed as incomplete");
				
				return true;
			}
			else
			{
				Logger.log(Logger.TAG_BUG2GODOWNLOADER,
				           "Done for "
				                           + file.getAbsolutePath()
				                           + ". It is a tethering case but the CR could not be found\nin crs list. It stills opened on Jira.");
			}
		}
		
		return false;
	}
	
	private boolean checkIfIncomplete()
	{
		CheckIfIncomplete incompleteChecker = new CheckIfIncomplete();
		return incompleteChecker.checkIt(crPath);
	}
	
	private boolean checkForTethering()
	{
		long start = System.currentTimeMillis();
		
		System.out.println();
		Logger.log(Logger.TAG_BUG2GODOWNLOADER, "-----------------");
		System.out.println();
		
		Logger.log(Logger.TAG_BUG2GODOWNLOADER, "Verifying BTD");
		
		BtdParser btdParser = new BtdParser(crPath);
		boolean ok = btdParser.parse();
		if (ok)
		{
			btdTether = btdParser.checkForTethering();
			Logger.log(Logger.TAG_BUG2GODOWNLOADER, "Tethering issue? " + btdTether);
			btdParser.close();
		}
		else
		{
			Logger.log(Logger.TAG_BUG2GODOWNLOADER, "Could not find BTD file");
		}
		
		Logger.log(Logger.TAG_BUG2GODOWNLOADER,
		           "\nBTD parse and tethering detection process took "
		                           + DateOperator.getDateStringFromBtdStringMillis((System.currentTimeMillis() - start))
		                           + "\n");
		
		// Check for tethering
		MainParser mainParser = new MainParser(crPath);
		long now = System.currentTimeMillis();
		
		Logger.log(Logger.TAG_BUG2GODOWNLOADER, "Verifying Main file");
		
		try
		{
			ok = mainParser.getMainData();
			if (ok)
			{
				mainParser.showAcquiredData();
				mainTether = mainParser.checkForTethering();
				mainParser.showTetheringData();
			}
			else
			{
				Logger.log(Logger.TAG_BUG2GODOWNLOADER, "Could not find Main log");
			}
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
		
		Logger.log(Logger.TAG_BUG2GODOWNLOADER,
		           "\nMain parse and tethering detection process took "
		                           + DateOperator.getDateStringFromBtdStringMillis((System.currentTimeMillis() - now))
		                           + "\n");
		
		tetherComment = "The user is *tethering a Wifi* network. Thus, this CR can be considered invalid for current drain analysis.\\n\\n";
		if(btdTether)
		{//that Wi-Fi tethering is enabled for 12% of the discharge time
			tetherComment = tetherComment + "- Following BTD file, SAT has detected that Wi-Fi tethering is enabled for "
							+ btdParser.getTetherPercentage() + "% of the discharge time.\\n\\n";
		}
		if(mainTether)
		{
			tetherComment = tetherComment + "- Following main log file, SAT has detected that Wi-Fi tethering is enabled for "
							+ mainParser.getTetherPercentage() + "% of the discharge time.\\n";
			tetherComment = tetherComment + "Tethering periods found in main log:\\n";
			for (int i = 0; i < mainParser.getWifiPeriods().size(); i++)
			{
				if (mainParser.getWifiPeriods().get(i).getDuration() > 0)
				{
					tetherComment = tetherComment + "Period " + (i+1) + ":\\n";
					tetherComment = tetherComment + "|" + mainParser.getWifiPeriods().get(i).startLine + "|\\n";
					tetherComment = tetherComment + "|" + mainParser.getWifiPeriods().get(i).endLine + "|\\n";
					tetherComment = tetherComment + "|Duration: " + DateOperator.getDateStringFromBtdStringMillis(mainParser.getWifiPeriods().get(i).getDuration()) + "|\\n";
				}
				tetherComment = tetherComment + "\\n";
			}
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
}
