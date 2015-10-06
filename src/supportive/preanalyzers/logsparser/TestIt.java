package supportive.preanalyzers.logsparser;


import java.util.Date;

import supportive.CrChecker;
import supportive.DateTimeOperator;


public class TestIt
{
	public static void main(String[] args)
	{
		String root = "C:/CRs/CancelledCommentErrors/";
		String crPath = root + "84082311_Phone";
		long now = System.currentTimeMillis();
		
//		BugrepParser brParser = new BugrepParser(root + "83226101");
//		brParser.parse();
//		System.out.println();
//		brParser.showData();
		
		
		
		
		//System.out.println(DateTimeOperator.getDateStringFromBtdStringMillis(148857));
		
		/*BtdParser btdParser = new BtdParser(crPath);
		System.out.println("Parsing BTD data ...");
		btdParser.parse();
		System.out.println("-- BTD Aquired data");
		btdParser.showParseResults();
		btdParser.showPeriods();
		System.out.println("-- BTD Tether data");
		btdParser.checkForTethering();
		System.out.println("\n" + btdParser.parseResult());
		System.out.println("Done");*/
		
		/*MainParser mainParser = new MainParser(crPath);
		System.out.println("Parsing Main log data ...");
		mainParser.parse();
		System.out.println("-- Main Aquired data");
		mainParser.showAcquiredData();
		mainParser.checkForTethering();
		System.out.println("-- Main Tether data");
		mainParser.showTetheringData();
		System.out.println("Done");*/
		
		/*BugrepParser bugrepParser = new BugrepParser(crPath);
		System.out.println("Parsing Bugreport log data ...");
		bugrepParser.parse();
		System.out.println(bugrepParser.currentDrainStatistics() + "\n" + bugrepParser.eblDecreasedReasons());
		System.out.println("Done");*/
		
		String a = "{panel:title=*Items that increases current drain and decreases EBL*|titleBGColor=#E9F2FF}\\n"
						+ "Phone signal quality was bad (None/Poor/Moderate) for 0d,8h,25m,58s,385ms (48,91%)\\n\\nScanning for better wifi network for 0d,12h,49m,30s,872ms (74,39%)\\n"
						+ "{panel}\\n";
		System.out.println(a);
		System.out.println(a.split("\\\\n|\\n|\n").length);
		
		
		System.out.println("\n\nIt took " + DateTimeOperator.getTimeStringFromMillis((System.currentTimeMillis() - now)));
		
		
		//System.out.println("Started at " + new Date(now) + " stopped at "
		//                   + new Date(System.currentTimeMillis()));
		/*
		try
		{
			
			System.out.println();System.out.println("-----------------");System.out.println();
			
			// File seek and load configuration
			File folder = new File(root);
			File[] listOfFiles = folder.listFiles();
			
			if (folder.isDirectory())
			{
				// Look for the file
				for (int i = 0; i < listOfFiles.length; i++)
				{
					// Logger.log(Logger.TAG_DIAG, folder.listFiles()[i]);
					if (listOfFiles[i].isDirectory())
					{
						String path = listOfFiles[i].getName();
						
						mainParser = new MainParser(root + path);
						mainParser.getMainData();
						mainParser.checkForTethering();
						mainParser.showTetheringData();
						
						System.out.println();System.out.println("-----------------");System.out.println();
						//break;
					}
				}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}*/
		
		System.out.println("\n\nIt took " + DateTimeOperator.getTimeStringFromMillis((System.currentTimeMillis() - now)));
		System.out.println("Started at " + new Date(now) + " stopped at "
		                   + new Date(System.currentTimeMillis()));
	}
}
