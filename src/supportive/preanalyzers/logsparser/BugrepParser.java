package supportive.preanalyzers.logsparser;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import core.Logger;


public class BugrepParser
{
	private String       path;
	
	public BugrepParser(String path)
	{
		this.path = path;
	}
	
	public void parse()
	{
		String result;
		String sCurrentLine;
		String file_report = "";
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		BufferedReader br = null;
		
		if (!folder.isDirectory())
		{
			result = "Not a directory";
			return;
		}
		
		// Look for the file to be parsed
		for (int i = 0; i < listOfFiles.length; i++)
		{
			if (listOfFiles[i].isFile())
			{
				String files = listOfFiles[i].getName();
				
				if (((files.endsWith(".txt")) || (files.endsWith(".TXT"))) && (files.contains("bugreport")))
				{
					if (path.equals("."))
						file_report = files;
					else
						file_report = path + "\\" + files;
					
					break;
				}
			}
		}
		
		Logger.log(Logger.TAG_BUGREPORT_PARSER, "\nBugreport file: " + file_report);
		
		// Try to open file
		if (!file_report.equals(""))
		{
			try
			{
				// Regex configuration
				/*String rxTimeOnBattery = "";
				String rxTimeScOffBattery = "";
				String rxTimeOnBattery = "";
				
				Pattern patternBTT = Pattern.compile(regexBTT);
				Pattern patternBTToff = Pattern.compile(regexBTToff);
				Pattern patternScreen = Pattern.compile(regexScOnOff);*/
				
				Matcher matcherBTT = null;
				Matcher matcherScreen = null;
				
				br = new BufferedReader(new FileReader(file_report));
				
				// Search for b2g evidences
				while ((sCurrentLine = br.readLine()) != null)
				{
					if (sCurrentLine.contains("Statistics since "))
					{
						Logger.log(Logger.TAG_BUGREPORT_PARSER, "Statistics line found: " + sCurrentLine);
						
						// Read next line
						while ((sCurrentLine = br.readLine()) != null)
						{
							if (sCurrentLine.contains("Statistics since "))
							{
								// Time on battery\: (.*) \(.*\) realtime\,.*
							}
						}
					}
				}
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			finally
			{
				// Close file reader
				try
				{
					if (br != null)
						br.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
			
		}
		else
		{
			Logger.log(Logger.TAG_BUGREPORT_PARSER, "No statistics found inside bugreport");
			return;
		}
	}
}
