package supportive.preanalyzers.logsparser;


import java.util.Date;

import supportive.DateTimeOperator;
import supportive.preanalyzers.btdparser.BtdParser;


public class TestIt
{
	public static void main(String[] args)
	{
		String root = "C:/CRs/TetherStuck/";
		String crPath = root + "83226101";
		long now = System.currentTimeMillis();
		
//		BugrepParser brParser = new BugrepParser(root + "83226101");
//		brParser.parse();
//		System.out.println();
//		brParser.showData();
		
		
		
		
		//System.out.println(DateTimeOperator.getDateStringFromBtdStringMillis(148857));
		
		BtdParser btdParser = new BtdParser(crPath);
		System.out.println("Parsing BTD data ...");
		btdParser.parse();
		System.out.println("Done");
		MainParser mainParser = new MainParser(crPath);
		System.out.println("Parsing Main log data ...");
		mainParser.parse();
		mainParser.showAcquiredData();
		mainParser.checkForTethering();
		mainParser.showTetheringData();
		System.out.println("Done");
		BugrepParser bugrepParser = new BugrepParser(crPath);
		System.out.println("Parsing Bugreport log data ...");
		bugrepParser.parse();
		System.out.println("Done");
		
		
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
