package Filters;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Throwables;

import Main.BatTracer;
import Objects.HighConsume_List;
import Objects.highConsumeItem;

public class Consume {
	
	static HighConsume_List hcList;
	static String result;
	static int totalOccurrences;
	static BatTracer BaseWindow;

	/*
	System.out.println("Mes:\t\t" + matcher.group(1));
	System.out.println("Dia:\t\t" + matcher.group(2));
	System.out.println("Hora:\t\t" + matcher.group(3));
	System.out.println("Minuto:\t\t" + matcher.group(4));
	System.out.println("Segundo:\t" + matcher.group(5));
	System.out.println("Consumo:\t" + matcher.group(6));
	System.out.println("PID:\t\t" + matcher.group(7));
	System.out.println("Process:\t" + matcher.group(8));
	*/
	

	public static String makelog(String path, BatTracer parent) {
		result = "- *The following processes are consuming too much CPU and draining battery:*\n";
		totalOccurrences = 0;
		
		//File reader
		BufferedReader reader = null;

		try {
			// List of Apps with high consume
			hcList = new HighConsume_List();
			
			//Separators
			String panel = "{panel}\n";
			
			// Regex configuration
			String regexBTT = "([0-9]{2})-([0-9]{2}).*([0-2][0-9]):([0-5][0-9]):([0-5][0-9]).*BTTopWriter: ([1-9][0-9].*)\\%.*PID:(.+).*\\((.+)\\)";
			String regexBTToff = "([0-9]{2})-([0-9]{2}).*([0-2][0-9]):([0-5][0-9]):([0-5][0-9]).*BTTopWriter: ([1-9][\\.|\\,].*)\\%.*PID:(.+).*\\((.+)\\)";
			//String regexScOnOff = ".*android.intent.action.SCREEN_(.+).*";
			
			String regexScOnOff = ".*BatteryTracerSvc: Data collection.*Screen.*";
			Pattern patternBTT = Pattern.compile(regexBTT);
			Pattern patternBTToff = Pattern.compile(regexBTToff);
			Matcher matcherBTT = null;
			Pattern patternScreen = Pattern.compile(regexScOnOff);
			Matcher matcherScreen = null;
			
			// Essential Variables
			String sCurrentLine;
			String screenStatus = "Unknown   : ";

			// Patch and Find configuration
			String file = "";
			
			// Find log file
			File folder = new File(path);
			File[] listOfFiles = folder.listFiles();
			
			if(!folder.isDirectory()){
				result = "Not a directory";
				return result;
			}
			
			for (int i = 0; i < listOfFiles.length; i++) {
				if ( listOfFiles[i].isFile() && ( listOfFiles[i].getName().toLowerCase().endsWith(".txt") && listOfFiles[i].getName().contains("main") ) )
				{
					file = listOfFiles[i].getName();
					if(!path.equals("."))
						file = path + listOfFiles[i].getName();
					break;
				}
			}

			if(file.equals("")){
				throw new FileNotFoundException();
			}
			else
			{
				reader = new BufferedReader(new FileReader(file));
				System.out.println("Log de sistema encontrado!" + file);
			}

			
			
			System.out.println("Parser running ...");
			
			while ((sCurrentLine = reader.readLine()) != null)
			{
				// Screen ON/OFF parsing
				matcherScreen = patternScreen.matcher(sCurrentLine);
				if(matcherScreen.matches()){
					if (sCurrentLine.contains("Screen on")){
						screenStatus = "Screen ON : ";
					} else if (sCurrentLine.contains("Screen off")){
						screenStatus = "Screen OFF: ";
					}
				}
				// Consume line parsing:
				else
				{
					if(screenStatus.contains("OFF") || screenStatus.contains("Unknown")){
						matcherBTT = patternBTToff.matcher(sCurrentLine);
						if (!matcherBTT.matches())
							matcherBTT = patternBTT.matcher(sCurrentLine);
					}
					else
						matcherBTT = patternBTT.matcher(sCurrentLine);
					
					if (matcherBTT.matches())
					{
						if(matcherBTT.group(8).contains("kworker"))
						{
							int index = hcList.indexOf("kworker");
							if(index == -1)
							{
								highConsumeItem hcItem = new highConsumeItem("kworker", matcherBTT.group(7), Float.parseFloat(matcherBTT.group(6).replace(",", ".")), screenStatus + sCurrentLine);
								hcList.add(hcItem);
							}
							else 
							{
								highConsumeItem hcItem = hcList.get(index); 
								hcItem.updateItem(Float.parseFloat(matcherBTT.group(6).replace(",", ".")), screenStatus + sCurrentLine);
								hcList.set(index, hcItem);
							}
						}
						else
						{
							int index = hcList.indexOf(matcherBTT.group(8));
							if(index == -1)
							{
								highConsumeItem hcItem = new highConsumeItem(matcherBTT.group(8), matcherBTT.group(7), Float.parseFloat(matcherBTT.group(6).replace(",", ".")), screenStatus + sCurrentLine);
								hcList.add(hcItem);
								totalOccurrences++;
							}
							else 
							{
								highConsumeItem hcItem = hcList.get(index); 
								hcItem.updateItem(Float.parseFloat(matcherBTT.group(6).replace(",", ".")), screenStatus + sCurrentLine);
								hcList.set(index, hcItem);
								totalOccurrences++;
							}
						}
					}
				}
			}
			
			System.out.println("Parser terminated.\n\n");
			try {
				if(reader != null)
					reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
			int hcitems = 0;
			Iterator<highConsumeItem> l = hcList.listIterator();
			while (l.hasNext())
			{
				highConsumeItem aux = l.next();
				if (100.0*(float)aux.getOccurencesTotal()/(float)totalOccurrences > 1 && !aux.getProcess().contains("motorola.tools.batterytracer"))
				{
					hcitems++;
				}
				else
				{
					l.remove();
				}
			}
			
			//System.out.println("A: " + a + "\nB: " + b);
			
			hcList.sortItens();
			for(int i=0; i < hcList.size();i++){
				result = result + panel + hcList.get(i).toString() + panel;
			}
			
			if(hcList.size() == 0){
				result = "- No app high consume evidences were found in text logs";
			}
			
			//System.out.println(result);
			//System.out.println("Apps detected: " + hcList.size());
			System.out.println("Apps high: " + hcitems + " - " + hcList.size());
		
		} catch (FileNotFoundException e) {
			result = "FileNotFoundException\n" + Throwables.getStackTraceAsString(e);
			e.printStackTrace();
			return result;
			
		} catch (IOException e) {
			result = "FileNotFoundException\n" + Throwables.getStackTraceAsString(e);
			e.printStackTrace();
			return result;
			
		} finally {
			try {
				if(reader != null)
					reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		//System.out.println(result);
		return result;
	}
	
	
	public static HighConsume_List getHCList(){
		return hcList;
	}
	
	public static String getResult(){
		return result;
	}

	public static void updateResult(String editedResult) {
		result = editedResult;		
	}
	
}