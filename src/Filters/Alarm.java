package Filters;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Main.BatTracer;
import Objects.AlarmItem;
import Objects.Alarms_List;

public class Alarm {

	static Alarms_List alarmList;
	static String result;
	static BufferedReader reader;

	public static void main(String[] args) {
		PrintWriter out;
		try {
			out = new PrintWriter("_Alarms.txt");
			//out.print(makelog("."));
			out.close();
			System.out.println(result);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	
	public static String makelog(String path, BatTracer BaseWindow) {
		result = BaseWindow.getOptions().getTextAlarms() + "\n";
		
		try {
			//File reader
			reader = null;
			
			// List of Apps with high consume
			alarmList = new Alarms_List();
			
			//Separators
			String panel = "{panel}\n";
			
			// Regex configuration
			String regexAlarmLine = "([0-9]{2}-[0-9]{2} [0-2][0-9]:[0-5][0-9]:[0-5][0-9]).*send.*\\*(.+)\\*:([a-z\\W_/$]+)([A-Z]+.+)\\}.*" +
					"|([0-9]{2}-[0-9]{2} [0-2][0-9]:[0-5][0-9]:[0-5][0-9]).*send.*\\*(.+)\\*:(.+)(\\}.*)";
			Pattern patternAlarmLine = Pattern.compile(regexAlarmLine);
			Matcher matcherAlarmLine = null;
			
			// Essential Variables
			String sCurrentLine;
			
			// Patch and Find configuration
			String file = "";
			
			
			// Find log file
			File folder = new File(path);
			File[] listOfFiles = folder.listFiles();
			for (int i = 0; i < listOfFiles.length; i++) {
				if ( listOfFiles[i].isFile() && ( listOfFiles[i].getName().toLowerCase().endsWith(".txt") && listOfFiles[i].getName().contains("system") ) )
				{
					file = listOfFiles[i].getName();
					if(!path.equals("."))
						file = path + listOfFiles[i].getName();
					break;
				}
			}

			if (file.equals(""))
			{
				System.out.print(System.getProperty("user.dir"));
				System.out.println("Log de sistema nao encontrado");
				return "Log de sistema nao encontrado";
			}
			else
			{
				reader = new BufferedReader(new FileReader(file));
				System.out.println("Log de sistema encontrado!" + file);
			}
			
			int averageOccurrences = 1;
			Date parsedDate = null;
			SimpleDateFormat dateFormat = null;
			while ((sCurrentLine = reader.readLine()) != null)
			{
				matcherAlarmLine = patternAlarmLine.matcher(sCurrentLine);
				if(matcherAlarmLine.matches() && !sCurrentLine.contains("TIME_TICK")){
					
					AlarmItem alarm;
					if(matcherAlarmLine.group(3) != null){
						try
						{
						    dateFormat = new SimpleDateFormat("MM-dd hh:mm:ss");
						    parsedDate = dateFormat.parse(matcherAlarmLine.group(1));
						}
						catch(Exception e)
						{
							System.out.println("Error: " + e.toString());
						}
						alarm = new AlarmItem(parsedDate, matcherAlarmLine.group(2), 
					    		matcherAlarmLine.group(3).substring(0, matcherAlarmLine.group(3).length()-1), matcherAlarmLine.group(4), matcherAlarmLine.group(0));
					} else{
						try
						{
						    dateFormat = new SimpleDateFormat("MM-dd hh:mm:ss");
						    parsedDate = dateFormat.parse(matcherAlarmLine.group(5));
						}
						catch(Exception e)
						{
							System.out.println("Error: " + e.toString());
						}
						if(Character.isUpperCase(matcherAlarmLine.group(7).charAt(0)))
					    	alarm = new AlarmItem(parsedDate, matcherAlarmLine.group(6), "Unknown", matcherAlarmLine.group(7), matcherAlarmLine.group(0));
					    else
					    	alarm = new AlarmItem(parsedDate, matcherAlarmLine.group(6), matcherAlarmLine.group(7), "Unknown", matcherAlarmLine.group(0));
					}
					int index = alarmList.alarmIndexOf(alarm);
					if(index == -1){
						alarmList.add(alarm);
					} else {
						// Atualiza o wakelock ja existente
						alarmList.get(index).alarmUpdate(parsedDate, sCurrentLine);
					}
					averageOccurrences++;
				}
			}
			reader.close();
			
			if(alarmList.size() > 0)
				averageOccurrences = averageOccurrences/alarmList.size();
			else
				averageOccurrences = 2;
			System.out.println(averageOccurrences);
			Iterator<AlarmItem> l = alarmList.listIterator();
			while (l.hasNext())
			{
				AlarmItem aux = l.next(); 
				if ((aux.getOccurences() < averageOccurrences))
				{
					l.remove();
				}
			}
			
			
			alarmList.sortItens();
			if(alarmList.size() > 0)
			{
				result = "- *Alarm overhead issues:*\n";
				for(int i=0; i<alarmList.size();i++){
					result = result + panel + alarmList.get(i).toString() + panel;
				}
			}
			else{
				result = "- No detailed alarm issue evidences were found in text logs";
			}
			
			//System.out.print(result);
			return result;

		} catch (Exception e){
			e.printStackTrace();
			if(reader != null)
				try {
					if(reader!=null)
						reader.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			return "alarm parser error";
		}
	}
	
	
	public static int getListSize(){
		return alarmList.size();
	}
	
	public static Alarms_List getList(){
		return alarmList;
	}


	public static String getResult() {
		return result;
	}
}