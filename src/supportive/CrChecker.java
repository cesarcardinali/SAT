package supportive;


import java.io.IOException;

import supportive.parsers.btdparser.BtdParser;
import supportive.parsers.logsparser.DateOperator;
import supportive.parsers.logsparser.MainParser;


public class CrChecker
{
	String crPath;
	boolean btdTether;
	boolean mainTether;
	
	public CrChecker(String crPath)
	{
		this.crPath = crPath;
	}
	
	public void checkCR()
	{
		long start = System.currentTimeMillis();
		
		System.out.println("Verifying BTD");
		
		BtdParser btdParser = new BtdParser(crPath);
		boolean ok = btdParser.parse();
		if (ok)
		{
			btdTether = btdParser.checkForTethering();
			System.out.println("Tethering issue? " + btdTether);
			btdParser.close();
		}
		else
		{
			System.out.println("Could not find BTD file");
		}
		
		System.out.println("\nBTD parse and tethering detection process took " + DateOperator.getDateStringFromBtdStringMillis((System.currentTimeMillis() - start)) + "\n");
		
		
		// Check for tethering
		MainParser mainParser = new MainParser(crPath);
		long now = System.currentTimeMillis();
		
		System.out.println("Verifying Main file");
		
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
				System.out.println("Could not find Main log");
			}
        }
        catch (IOException e1)
        {
	        e1.printStackTrace();
        }
		
		System.out.println("\nMain parse and tethering detection process took " + DateOperator.getDateStringFromBtdStringMillis((System.currentTimeMillis() - now)) + "\n");
		
		System.out.println("BTD Tether: " + btdTether);
		System.out.println("Main Tether: " + mainTether);
		if (btdTether || mainTether)
			System.out.println("CR would be closed");
		
		System.out.println();
		System.out.println("-----------------");
		System.out.println();
	}
}
