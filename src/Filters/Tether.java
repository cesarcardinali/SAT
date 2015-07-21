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

import Main.BatTracer;

public class Tether {
	
	static String result;
	
	public static void main(String[] args) {
		try {
			String res = "";//makeLog(".");
			StringSelection stringSelection = new StringSelection(res);
			Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
			clpbrd.setContents(stringSelection, null);
			PrintWriter out;
			out = new PrintWriter("_Tethering.txt");
			out.print(res);
			out.close();
			System.out.println(result);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		System.exit(0);
	}

	public static String makeLog(String path, BatTracer BaseWindow) {
		BufferedReader br = null;
		result = "";

		try {

			String wifitether = BaseWindow.getOptions().getTextTether();
			String wifitetherData1 = "";
			String wifitetherData2 = "";
			String sCurrentLine;
			
			// File seek and load configuration
			String file_report = null;
			File folder = new File(path);
			File[] listOfFiles = folder.listFiles();

			// Look for the file
			for (int i = 0; i < listOfFiles.length; i++) {
				if (listOfFiles[i].isFile()) {
					String files = listOfFiles[i].getName();
					System.out.println(listOfFiles[i].getName());
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
			if (file_report != null)
				br = new BufferedReader(new FileReader(file_report));
			else{
				result = "Log nao encontrado";
			}

			while ((sCurrentLine = br.readLine()) != null) {
				if (sCurrentLine.contains("WiFi Tethered already")) {
					wifitetherData1 = wifitetherData1 + sCurrentLine + "\n";
				}
			}
			System.out.println(wifitetherData1);
			
			if(br!=null)
				br.close();
			
			// Look for the file
			for (int i = 0; i < listOfFiles.length; i++) {
				if (listOfFiles[i].isFile()) {
					String files = listOfFiles[i].getName();
					System.out.println(listOfFiles[i].getName());
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
			if (file_report != null)
				br = new BufferedReader(new FileReader(file_report));
			else {
				result = "sem arquivo";
				return "Arquivo nao encontrado";
			}

			String startTether = "", stopTether = "";
			while ((sCurrentLine = br.readLine()) != null) {
				if ((sCurrentLine.contains("Tether") && sCurrentLine.contains("what=4")) || sCurrentLine.contains("processMsg: TetheredState")) {
					wifitetherData2 = wifitetherData2 + sCurrentLine + "\n";
				}
				else if (sCurrentLine.toLowerCase().contains("starting tether")){
					System.out.println(sCurrentLine);
					if(!startTether.equals(""))
						startTether = startTether + "\n" + sCurrentLine;
					else
						startTether = sCurrentLine;
				}
				else if (sCurrentLine.toLowerCase().contains("stopping tether")){
					System.out.println(sCurrentLine);
					if(!stopTether.equals(""))
						stopTether = stopTether + "\n" + sCurrentLine;
					else
						stopTether = sCurrentLine;
				}
			}
			
			System.out.println("\n\n\n" + startTether + "\n" + stopTether);
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
		System.out.println(result);
		return result;
	}

	public static void updateResult(String editedResult) {
		result = editedResult;		
	}
	
}
