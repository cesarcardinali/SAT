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

public class B2G {

	static String result;
	
	
	public static void main(String[] args) {
		try {
			String bug2go = "- The average current drain while screen is OFF is high. However, *Bug2Go* held the PowerManagerService, contributing to the high current. This is a WAD behavior, since Bug2Go won't be delivered to the end user.\n";
			String res = makelog(".");
			StringSelection stringSelection = new StringSelection(bug2go + res);
			Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
			clpbrd.setContents(stringSelection, null);
			PrintWriter out;
			out = new PrintWriter("_B2G.txt");
			out.print(bug2go + res);
			out.close();
			System.out.println(result);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		System.exit(0);
	}
	
	
	public static String makelog(String path) {
		BufferedReader br = null;
		result = "";
		
		try {
			String bug2goData = "";
			String sCurrentLine;

			
			// File seek and load configuration
			String file_report = "";
			File folder = new File(path);
			File[] listOfFiles = folder.listFiles();
			
			//Look for the file
			for (int i = 0; i < listOfFiles.length; i++) {
				if (listOfFiles[i].isFile()) {
					String files = listOfFiles[i].getName();
					System.out.println("" + files);
					if (((files.endsWith(".txt")) || (files.endsWith(".TXT"))) && (files.contains("system"))) {
						if(path.equals("."))
							file_report = files;
						else
							file_report = path + "\\" + files;
						break;
					}
				}
			}
			System.out.println("\n\n" + file_report);
			
			// Try to open file
			if (!file_report.equals(""))
				br = new BufferedReader(new FileReader(file_report));
			else{
				result = "Arquivo nao encontrado";
				return result;
			}

			while ((sCurrentLine = br.readLine()) != null) {
				if (sCurrentLine.contains("tag=\"BUG2GO-UploadWorker\"") || sCurrentLine.contains("tag=BUG2GO-UploadWorker")) {
					bug2goData = bug2goData + sCurrentLine + "\n";
				}
			}
			if (bug2goData.length() > 12){
				bug2goData = "{noformat}\n" +bug2goData + "{noformat}\n";
			}
			
			if(bug2goData.length() < 2000){
				//Look for the file
				for (int i = 0; i < listOfFiles.length; i++) {
					if (listOfFiles[i].isFile()) {
						String files = listOfFiles[i].getName();
						System.out.println("" + files);
						if (((files.endsWith(".txt")) || (files.endsWith(".TXT"))) && (files.contains("-main"))) {
							if(path.equals("."))
								file_report = files;
							else
								file_report = path + "\\" + files;
							break;
						}
					}
				}
				System.out.println("\n\n" + file_report);
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				// Try to open file
				if (!file_report.equals(""))
					br = new BufferedReader(new FileReader(file_report));
				else{
					result = "sem arquivo";
				}
				
				
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
				
			//System.out.println(result);
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
