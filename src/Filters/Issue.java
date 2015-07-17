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
import java.util.regex.Pattern;

import Main.BatTracer;

public class Issue {
	
	static String result;
	
	public static void main(String[] args) {
		try {
			String res = ("");
			StringSelection stringSelection = new StringSelection(res);
			Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
			clpbrd.setContents(stringSelection, null);
			PrintWriter out;
			out = new PrintWriter("_Issue.txt");
			out.write(res);
			out.close();
			System.out.println(res);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		System.exit(0);
	}
	
	
	public static String makelog(String path, BatTracer BaseWindow) {
		BufferedReader br = null;
		result = "Error";
		
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
			String file_report = null;
			File folder = new File(path);
			File[] listOfFiles = folder.listFiles();

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
			
			// Try to open file
			if (file_report != null)
				br = new BufferedReader(new FileReader(file_report));
			else{
				result = "Arquivo nao encontrado";
				return "Arquivo nao encontrado";
			}

			
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
			
			try {
				if(br!=null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			
			
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

}
