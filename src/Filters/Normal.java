package Filters;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class Normal {
	
	static String result = "";

	public static void main(String[] args) {
		try {
			String res = makeLog(".");
			StringSelection stringSelection = new StringSelection(res);
			Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
			clpbrd.setContents(stringSelection, null);
			PrintWriter out;
			out = new PrintWriter("_Normal.txt");
			out.print(res);
			out.close();
			System.out.println(res);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	

	public static String makeLog(String path){
		BufferedReader br = null;
		
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
			if(!str_report.equals("")){
				br = new BufferedReader(new FileReader(str_report));
			}
			else{
				result = "Arquivo nao encontrado";
				System.out.print(System.getProperty("user.dir"));
				return "Arquivo nao encontrado";
			}
			
			
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
		} catch (IOException e) {
			e.printStackTrace();
			try {
				br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
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
