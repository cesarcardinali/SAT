package Filters;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Throwables;

import Main.BatTracer;
import Objects.AlarmItem;
import Objects.Alarms_List;

public class Alarm {


	static Alarms_List alarmList;
	static String result;
	static BufferedReader reader;
	static boolean enabled = true;

	
	public static String makelog(String path, BatTracer BaseWindow) {
		result = BaseWindow.getOptions().getTextAlarms() + "\n";

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
		
		if(!folder.isDirectory()){
			result = "Not a directory";
			return result;
		}
		
		for (int i = 0; i < listOfFiles.length; i++) {
			if ( listOfFiles[i].isFile() && ( listOfFiles[i].getName().toLowerCase().endsWith(".txt") && listOfFiles[i].getName().contains("system") ) )
			{
				file = listOfFiles[i].getName();
				if(!path.equals("."))
					file = path + listOfFiles[i].getName();
				break;
			}
		}

		try {
			reader = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			result = "FileNotFoundException\n" + Throwables.getStackTraceAsString(e);
			return result;
		}
		System.out.println("Log de sistema encontrado: " + file);
		
		int averageOccurrences = 1;
		Date parsedDate = null;
		SimpleDateFormat dateFormat = null;
		try {
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
							System.out.println("********Error: " + Throwables.getStackTraceAsString(e));
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
							System.out.println("********Error: " + Throwables.getStackTraceAsString(e));
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
		} catch (IOException e) {
			e.printStackTrace();
			result = "IOException\n" + Throwables.getStackTraceAsString(e);
			return result;
		}
		
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
	
	public static void updateResult(String editedResult) {
		result = editedResult;		
	}
	
	public static boolean isEnabled(){
		return enabled;
	}
	
	public static void setEnabled(boolean onoff){
		enabled = onoff;
	}
}