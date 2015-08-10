package filters;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.google.common.base.Throwables;


public class Normal {
	
	static String result;
	private static boolean enabled = true;
	

	public static String makeLog(String path){
		BufferedReader br = null;
		result = "";
		
		try {
			String bugReport = "*Bugreport usage info:*";
			String dataNbatteryUsage = "*Data usage and battery drain info:*";
			String noFormat = "{panel}";

			String bugReportData = "";
			String dataNbatteryData = "";
			
			result = bugReport + "\n{panel}\n" + bugReportData + "\n{panel}\n" + dataNbatteryUsage + "\n{panel}\n" + dataNbatteryData + "\n{panel}\n";

			String str_report = "";
			File folder = new File(path);
			File[] listOfFiles = folder.listFiles();

			if(!folder.isDirectory()){
				result = "Not a directory";
				return result;
			}
			
			for (int i = 0; i < listOfFiles.length; i++) {
				if (listOfFiles[i].isFile()) {
					String files = listOfFiles[i].getName();
					//System.out.println("" + files);
					if (((files.endsWith(".txt")) || (files.endsWith(".TXT"))) && (files.contains("report-output"))) {
						if(path.equals("."))
							str_report = files;
						else
							str_report = path + "\\" + files;
						break;
					}
				}
			}
			
			// Try to open file
			if(str_report.equals("")){
				throw new FileNotFoundException();
			}
			br = new BufferedReader(new FileReader(str_report));
			
			String sCurrentLine;
			String secondaryData1 = "";
			String secondaryData2 = "";
			
			while ((sCurrentLine = br.readLine()) != null) {
				if (sCurrentLine.contains("Statistics since last unplugged:"))
				{
					System.out.println("bugReportData - 1");
					bugReportData = "\n" + noFormat + "\n" + sCurrentLine + "\n";
					sCurrentLine = br.readLine();
					
					while (!sCurrentLine.contains("Mobile total received"))
					{
						bugReportData = bugReportData + sCurrentLine + "\n";
						sCurrentLine = br.readLine();
					}
					bugReportData = bugReportData + noFormat + "\n";
				}
				
				if (sCurrentLine.contains("obile total receiv"))
				{
					System.out.println("dataNbatteryData - 2");
					dataNbatteryData = "\n" + noFormat+ "\n" + sCurrentLine + "\n";
					sCurrentLine = br.readLine();
					
					//Device battery use since last full charge
					do
					{
						dataNbatteryData = dataNbatteryData + sCurrentLine + "\n";
						sCurrentLine = br.readLine();
					} while  ((sCurrentLine).indexOf("Full Charge Battery Capacity") < 0);
					
					dataNbatteryData = dataNbatteryData + noFormat + "\n";
					
				}
				
				else if(sCurrentLine.equals("Discharging"))
				{
					System.out.println("Secondary - 4");
					secondaryData1 = "*Battery Discharging Summary*\n{panel}\n";
					sCurrentLine = br.readLine();
					while(!sCurrentLine.contains("-------") && !sCurrentLine.equals(""))
					{
						secondaryData1 = secondaryData1 + sCurrentLine + "\n";
						sCurrentLine = br.readLine();
					}
					secondaryData1 = secondaryData1 + "{panel}";
				}

				else if(sCurrentLine.toLowerCase().equals("summary"))
				{
					System.out.println("Secondary - 5");
					secondaryData2 = "\n*General Battery Summary*\n{panel}\n";
					sCurrentLine = br.readLine();
					if(sCurrentLine.equals("================="))
						sCurrentLine = br.readLine();
					while(!sCurrentLine.contains("<END_BTD_FILE_") && !sCurrentLine.equals(""))
					{
						secondaryData2 = secondaryData2 + sCurrentLine + "\n";
						sCurrentLine = br.readLine();
					}
					secondaryData2 = secondaryData2 + "{panel}";
				}
			}
			
			try {
				br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			
			result = bugReport + bugReportData + dataNbatteryUsage + dataNbatteryData;
			
			if(result.split("\n").length < 10){
				result = secondaryData1 + "\n" + secondaryData2;
			}
			result = result + "\n- No current drain issues found in this CR.\n\n??Closed as normal use??";
			
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

	public static boolean isEnabled(){
		return enabled;
	}
	
	public static void setEnabled(boolean onoff){
		enabled = onoff;
	}
	
}
