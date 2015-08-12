package filters;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.google.common.base.Throwables;

import core.Logger;


public class B2G {

	private static String result;
	private static boolean edited = false;
	private static boolean enabled = true;
	
	public static String makelog(String path) {
		BufferedReader br = null;
		result = "";
		
		String bug2goData = "";
		String sCurrentLine = "";
		
		try{
			// File seek and load configuration
			String file_report = "";
			File folder = new File(path);
			File[] listOfFiles = folder.listFiles();
			
			if(!folder.isDirectory()){
				result = "Not a directory";
				return result;
			}
			
			//Look for the file
			for (int i = 0; i < listOfFiles.length; i++) {
				if (listOfFiles[i].isFile()) {
					String files = listOfFiles[i].getName();
					Logger.log(Logger.TAG_B2G, "" + files);
					if (((files.endsWith(".txt")) || (files.endsWith(".TXT"))) && (files.contains("system"))) {
						if(path.equals("."))
							file_report = files;
						else
							file_report = path + "\\" + files;
						break;
					}
				}
			}
			Logger.log(Logger.TAG_B2G, "\n\nB2G: system file: " + file_report);
			
			// Try to open file
			if (!file_report.equals("")){
				br = new BufferedReader(new FileReader(file_report));
				
				while ((sCurrentLine = br.readLine()) != null) {
					if (sCurrentLine.contains("tag=\"BUG2GO-UploadWorker\"") || sCurrentLine.contains("tag=BUG2GO-UploadWorker")) {
						bug2goData = bug2goData + sCurrentLine + "\n";
					}
				}
				
				if (bug2goData.length() > 12){
					bug2goData = "{noformat}\n" + bug2goData + "{noformat}\n";
				}
				
				if(br != null)
					br.close();
			}
			
			file_report = "";
			if(bug2goData.length() < 2000){
				//Look for the file
				for (int i = 0; i < listOfFiles.length; i++) {
					if (listOfFiles[i].isFile()) {
						String files = listOfFiles[i].getName();
						Logger.log(Logger.TAG_B2G, "" + files);
						if (((files.endsWith(".txt")) || (files.endsWith(".TXT"))) && (files.contains("-main"))) {
							if(path.equals("."))
								file_report = files;
							else
								file_report = path + "\\" + files;
							break;
						}
					}
				}
				Logger.log(Logger.TAG_B2G, "\n\n" + file_report);
				
				// Try to open file
				if (file_report.equals("")){
					result = "";
					throw new FileNotFoundException();
				}

				br = new BufferedReader(new FileReader(file_report));
				
				String newData;
				newData = "";
				int ok = 0;
				
				while ((sCurrentLine = br.readLine()) != null) {
					if (sCurrentLine.contains("BUG2GO-DBAdapter: update")) {
						newData = newData + sCurrentLine + "\n";
						ok = 1;
					}
					if (sCurrentLine.contains("BUG2GO-DBAdapter:") && ok == 1) {
						newData = newData + sCurrentLine + "\n";
					}
				}
				if (newData.length() > 20){
					bug2goData = bug2goData + "{noformat}\n" + newData + "{noformat}\n";
				}
			}
			
			
			if(bug2goData.split("\n").length > 3)
				result = bug2goData;
			else
				result = "- No B2G evidences were found in text logs";
			
			return result;
		
		} catch (FileNotFoundException e){
			result = "FileNotFoundException\n" + Throwables.getStackTraceAsString(e);
			e.printStackTrace();
			return result;
			
		} catch (IOException e) {
			result = "IOException\n" + Throwables.getStackTraceAsString(e);
			e.printStackTrace();
			return result;
			
		} finally {
			try {
				if(br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	
	// Getters and Setters
	public static String getResult() {
		return result;
	}

	public static void updateResult(String editedResult) {
		result = editedResult;			
	}
	
	public static boolean isEdited() {
		return edited;
	}
	
	public static void setEdited(boolean value) {
		edited = value;	
	}

	public static boolean isEnabled(){
		return enabled;
	}
	
	public static void setEnabled(boolean onoff){
		enabled = onoff;
	}
}
