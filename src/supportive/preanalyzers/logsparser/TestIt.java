package supportive.preanalyzers.logsparser;


import java.util.Date;

import supportive.DateTimeOperator;


public class TestIt
{
	public static void main(String[] args)
	{
		String root = "C:/CRs/BugreportStuck/";
		long now = System.currentTimeMillis();
		
		BugrepParser brParser = new BugrepParser(root + "83430381");
		
		brParser.parse();
		System.out.println();
		//brParser.showData();
		
		
		/*
		MainParser mainParser = new MainParser(root + "80976191");
		
		//System.out.println(DateTimeOperator.getDateStringFromBtdStringMillis(148857));
		
		try
        {
			mainParser.getMainData();
			mainParser.checkForTethering();
			mainParser.showAcquiredData();
			mainParser.showTetheringData();
        }
        catch (IOException e1)
        {
	        // TODO Auto-generated catch block
	        e1.printStackTrace();
        }
		
		System.out.println("\n\nIt took " + DateTimeOperator.getDateStringFromBtdStringMillis((System.currentTimeMillis() - now)));
		*/
		
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
