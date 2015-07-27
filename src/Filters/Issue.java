package Filters;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;

import com.google.common.base.Throwables;

import Main.BatTracer;

public class Issue {
	
	static String result;
	
	public static String makelog(String path, BatTracer BaseWindow) {
		BufferedReader br = null;
		result = "";
		
		try {
			// Output text configuration
			String screenOff = BaseWindow.getOptions().getTextHighCurrent() + "\n";			
			String screenOffData = "";
			String kernelWakelock = BaseWindow.getOptions().getTextKernel() + "\n";
			String kernelWakelockData = "";
			String javaWakelock = BaseWindow.getOptions().getTextJava() + "\n";
			String javaWakelockData = "";
			
			
			String regexHighCurrent = ".*off:.*=>.*[0-9]{3}.*";
			Pattern patternHC = Pattern.compile(regexHighCurrent);
			String sCurrentLine;
			boolean hc = false;
			
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
					if (((files.endsWith(".txt")) || (files.endsWith(".TXT")))
							&& (files.contains("report-output"))) {
						if(path.equals("."))
							file_report = files;
						else
							file_report = path + files;
						break;
					}
				}
			}
			
			if(file_report.equals("")){
				throw new FileNotFoundException();
			}
			br = new BufferedReader(new FileReader(file_report));

			// Parse file
			while ((sCurrentLine = br.readLine()) != null) {
				
				if (sCurrentLine.matches("^  Device battery use.*$"))
				{
					screenOffData = "{panel}\n";
					
					while (sCurrentLine.matches(".*[a-w].*"))
					{
						if(patternHC.matcher(sCurrentLine).matches())
						{
							hc = true;
							screenOffData = screenOffData + 
									sCurrentLine.replace("=> ", "=> *")
												.replace(" mA average", "* mA average") + "\n";
						}
						else
							screenOffData = screenOffData + sCurrentLine + "\n";
						
						sCurrentLine = br.readLine();
					}
					screenOffData = screenOffData + "{panel}\n";
				}
				
				while (sCurrentLine.matches(".*Kernel Wake lock.*: [3-5][0-9]m.*") || sCurrentLine.matches(".*Kernel Wake lock .*: [1-9]h.*")) 
				{
					kernelWakelockData = kernelWakelockData + "|" + sCurrentLine.replace(":", "|") + "|\n";
					sCurrentLine = br.readLine();
				}
				
				if (sCurrentLine.contains("Device is currently")) {
					screenOffData = screenOffData + "{panel}\n";
					
					while (sCurrentLine.matches(".*[a-w].*"))
					{
						if(patternHC.matcher(sCurrentLine).matches())
						{
							hc = true;
							screenOffData = screenOffData + 
									sCurrentLine.replace("=> ", "=> *")
												.replace(" mA average", "* mA average") + "\n";
						}
						else
							screenOffData = screenOffData + sCurrentLine + "\n";
						
						sCurrentLine = br.readLine();
					}
					
					if(hc == false)
						screenOffData = "";
					else
						screenOffData = screenOffData + "{panel}\n";
				}
				
				if (sCurrentLine.contains("Java wakelocks held")) {
					javaWakelockData = "{panel}\n";
					sCurrentLine = br.readLine();
					while (!sCurrentLine.contains("Java wakelocks held"))
					{
						if(!sCurrentLine.contains("Kernel"))
						{
							javaWakelockData = javaWakelockData + sCurrentLine + "\n";
						}
						else
						{
							kernelWakelockData = kernelWakelockData + "|" + sCurrentLine.replace(":", "|") + "|\n";
						}
						sCurrentLine = br.readLine();
					}
					if(javaWakelockData.split("\n").length < 3)
						javaWakelockData = "";
					else
						javaWakelockData = javaWakelockData + "{panel}";
				}
			}
			
			if(br != null)
				br.close();
			
			// Building results:
			result = "Issues seen in this CR:\n\n";
			if (hc){
				result = result + screenOff + screenOffData + "\n\n";
			}
			if (!kernelWakelockData.equals("")){
				result = result + kernelWakelock + kernelWakelockData + "\n\n";
			}
			if (javaWakelockData.contains("realtime")){
				result = result + javaWakelock + javaWakelockData + "\n\n";
			}
			
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
				if(br!=null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		return result;
	}


	public static String getResult() {
		return result;
	}

	public static void updateResult(String editedResult) {
		result = editedResult;		
	}
	
}
