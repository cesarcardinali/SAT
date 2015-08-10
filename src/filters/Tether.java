package filters;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.google.common.base.Throwables;

import core.SharedObjs;


public class Tether {
	
	static String result;
	private static boolean enabled = true;

	public static String makeLog(String path) {
		BufferedReader br = null;
		result = "";

		try {
			String wifitether = SharedObjs.optionsPane.getTextTether();
			String wifitetherData1 = "";
			String wifitetherData2 = "";
			String sCurrentLine;
			
			// File seek and load configuration
			String file_report = "";
			File folder = new File(path);
			File[] listOfFiles = folder.listFiles();

			if(!folder.isDirectory()){
				result = "Not a directory";
				return result;
			}
			
			// Look for the file
			for (int i = 0; i < listOfFiles.length; i++) {
				if (listOfFiles[i].isFile()) {
					String files = listOfFiles[i].getName();
					//System.out.println(listOfFiles[i].getName());
					if (((files.endsWith(".txt")) || (files.endsWith(".TXT")))
							&& (files.contains("system"))) {
						if(path.equals("."))
							file_report = files;
						else
							file_report = path + "\\" + files;
						break;
					}
				}
			}
			
			// Try to open file
			if (file_report.equals("")) {
				result = "system not found";
			}
			else {
				br = new BufferedReader(new FileReader(file_report));
	
				while ((sCurrentLine = br.readLine()) != null) {
					if (sCurrentLine.contains("WiFi Tethered already")) {
						wifitetherData1 = wifitetherData1 + sCurrentLine + "\n";
					}
				}
				//System.out.println(wifitetherData1);
				
				if(br != null)
					br.close();
			}
			
			// Look for a file
			file_report = "";
			for (int i = 0; i < listOfFiles.length; i++) {
				if (listOfFiles[i].isFile()) {
					String files = listOfFiles[i].getName();
					//System.out.println(listOfFiles[i].getName());
					if (((files.endsWith(".txt")) || (files.endsWith(".TXT")))
							&& (files.contains("main"))) {
						if(path.equals("."))
							file_report = files;
						else
							file_report = path + "\\" + files;
						break;
					}
				}
			}
			
			// Try to open file
			if (file_report.equals("") && result.equals("system not found"))
				throw new FileNotFoundException();
			else
				br = new BufferedReader(new FileReader(file_report));
			
			result = "";

			String startTether = "", stopTether = "";
			while ((sCurrentLine = br.readLine()) != null) {
				if ((sCurrentLine.contains("Tether") && sCurrentLine.contains("what=4")) || sCurrentLine.contains("processMsg: TetheredState")) {
					wifitetherData2 = wifitetherData2 + sCurrentLine + "\n";
				}
				else if (sCurrentLine.toLowerCase().contains("starting tether")){
					//System.out.println(sCurrentLine);
					if(!startTether.equals(""))
						startTether = startTether + "\n" + sCurrentLine;
					else
						startTether = sCurrentLine;
				}
				else if (sCurrentLine.toLowerCase().contains("stopping tether")){
					//System.out.println(sCurrentLine);
					if(!stopTether.equals(""))
						stopTether = stopTether + "\n" + sCurrentLine;
					else
						stopTether = sCurrentLine;
				}
			}
			
			//System.out.println("\n\n\n" + startTether + "\n" + stopTether);
			result = wifitether;
			
			if(!startTether.equals("") || !stopTether.equals("")){
				if(startTether.length() > stopTether.length()){
					stopTether = stopTether + "\nUnknown";
					String starts[] = startTether.split("\n");
					String stops[] = stopTether.split("\n");
					for (int i=0;i < starts.length;i++){
						result = result + "\n|Starting Tethering at| " + starts[i] + "|";
						result = result + "\n|Stopping Tethering at| " + stops[i] + "|";
					}
				} else if(startTether.length() < stopTether.length()){
					startTether = startTether + "Unknown";
					String starts[] = startTether.split("\n");
					String stops[] = stopTether.split("\n");
					for (int i=0;i < starts.length;i++){
						result = result + "\n|Starting Tethering at| " + starts[i] + "|";
						result = result + "\n|Stopping Tethering at| " + stops[i] + "|";
					}
				} else {
					String starts[] = startTether.split("\n");
					String stops[] = stopTether.split("\n");
					for (int i=0;i < starts.length;i++){
						result = result + "\n|Starting Tethering at| " + starts[i] + "|";
						result = result + "\n|Stopping Tethering at| " + stops[i] + "|";
					}
				}
			}
			if(wifitetherData1.split("\n").length > 1){
				result = result + "\n{noformat}\n" + wifitetherData1 + "{noformat}";
			}
			if(wifitetherData2.split("\n").length > 6){
				result = result + "\n{noformat}\n" + wifitetherData2 + "{noformat}";
			}
			if (result.split("\n").length < 12){
				result = "- No tethering evidences were found in text logs";
			}
			
			try {
				br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			
			//System.out.println(result);

		} catch (FileNotFoundException e) {
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
				return result;
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		return result;
	}

	public static String getResult() {
		//System.out.println(result);
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
