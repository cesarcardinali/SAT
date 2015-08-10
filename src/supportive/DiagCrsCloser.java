package supportive;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.ui.Select;

import main.SAT;
import panes.ClosingDiagDialog;

public class DiagCrsCloser implements Runnable {

	static SAT BaseWindow;
	static ArrayList<String> diagCRs, unknownCRs;
	static HashMap<String, String> b2gs_results;
	static ClosingDiagDialog dialog;
	static HashMap<String, String> b2g_crid;
	
	
	
	public DiagCrsCloser(SAT parent, ClosingDiagDialog dialog){
		BaseWindow = parent;
		DiagCrsCloser.dialog = dialog;
	}
	
	
	private static void checkDiag(){
		
		String path = BaseWindow.getCrsManager().getRootPath();
		HashMap<String, String> b2g_analyzed = BaseWindow.getCrsManager().getB2g_analyzed();
		System.out.println("Path: " + path);
		BaseWindow.logWrite("Path: " + path);
		String sCurrentLine, result, crPath;
		BufferedReader br = null;
		
		String bugreport = null;
		String reportInfo = null;
		String btd = null;
		String file = null;
		String b2gID = null;
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		
		diagCRs = new ArrayList<String>();
		unknownCRs = new ArrayList<String>();
		b2gs_results = new HashMap<String, String>();
		
		
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile() && listOfFiles[i].getName().toLowerCase().endsWith(".zip")) {
				try {
					file = path + listOfFiles[i].getName();
					crPath = file.substring(0, file.length() - 28);
					b2gID = listOfFiles[i].getName().substring(0, listOfFiles[i].getName().length() - 28);
					System.out.println("---------------------\nFile to unzip: " + file + "\nOutput folder: " + crPath + "\nB2Gid: " + b2gID + "\nUnzipping:");
					UnZip.unZipIt(file, crPath);
					
					result = "DIAG_WS wakelock issue:\n" + "{panel}\n";
					bugreport = null;
					reportInfo = null;
					btd = null;
	
					// Look for the file
					File crFolder = new File(crPath);
					File[] crListOfFiles = crFolder.listFiles();
					System.out.println("Folder to look for files: " + crFolder);
					for (int a = 0; a < crListOfFiles.length; a++) {
						if (crListOfFiles[a].isFile()) {
							String files = crListOfFiles[a].getName();
							//System.out.println("Actual file: " + files);
							if (files.toLowerCase().endsWith(".txt") && (files.contains("bugreport"))) {
								bugreport = crPath + "\\" + files;
							} else if (files.toLowerCase().endsWith(".txt") && (files.contains("report_info"))) {
								reportInfo = crPath + "\\" + files;
							} else if (files.toLowerCase().endsWith(".btd")) {
								btd = crPath + "\\" + files;
							}
							if(reportInfo != null && bugreport != null && btd != null)
								break;
						}
					}
					
					if(bugreport == null){
						JOptionPane.showMessageDialog(null,"The bugreport file could not be located inside " + crPath +
								"\nPress OK to jump for the next CR.\n" +
								"At the end of entire process, a list with the CRs closed " +
								"and other with the CRs not diagnosed as DIAG or impossible to check for it will be displayed.");
						unknownCRs.add(listOfFiles[i].getName().substring(0,listOfFiles[i].getName().length()-28));
						b2g_analyzed.put(b2gID, "Incomplete");
						continue;
					}
					if(reportInfo == null){
						JOptionPane.showMessageDialog(null,"The report_info file could not be located inside " + crPath +
								"\nPress OK to jump for the next CR.\n" +
								"At the end of entire process, a list with the CRs closed " +
								"and other with the CRs not diagnosed as DIAG or impossible to check for it will be displayed.");
						unknownCRs.add(listOfFiles[i].getName().substring(0,listOfFiles[i].getName().length()-28));
						b2g_analyzed.put(b2gID, "Incomplete");
						continue;
					}
						
					
					br = new BufferedReader(new FileReader(bugreport));
					br.skip(300000);
					//br.skip((long) (new File(bugreport).length() * 0.55));
					
					// Parse file
					while ((sCurrentLine = br.readLine()) != null) {
						if (sCurrentLine.contains("All kernel wake locks")){
							sCurrentLine = br.readLine();
							if (sCurrentLine.contains("Kernel Wake lock DIAG_"))
							{
								int index = sCurrentLine.indexOf("h ");
								System.out.println("Index: " + index);
								BaseWindow.logWrite("Index: " + index);
								if(index > 0)
									result = result + sCurrentLine + "\n";
							} else {
								sCurrentLine = br.readLine();
								if (sCurrentLine.contains("Kernel Wake lock DIAG_"))
								{
									int index = sCurrentLine.indexOf("h ");
									System.out.println("Index: " + index);
									BaseWindow.logWrite("Index: " + index);
									if(index > 0)
										result = result + sCurrentLine + "\n";
								}
							}
						} else if (sCurrentLine.contains("DUMP OF SERVICE bluetooth_manager:")){
							break;
						}
					}
					br.close();
					
					long diagMs = 0;
					if(!result.contains("Kernel")){
						br = new BufferedReader(new FileReader(bugreport));
						String line = "";
						while ((line = br.readLine()) != null){
							if(line.contains("DIAG_WS") && !line.contains(",")) {
								String parts[] = line.split("\t\t|\t");
								
								for(String s : parts){
									System.out.println("Part:  " + s);
									BaseWindow.logWrite("Part:  " + s);
								}
								
								diagMs = Long.parseLong(parts[6]);
								int horas = (int) (diagMs/3600000);
								result = result + "DIAG_WS is held for more than " + horas + " hours:\n" +
										"||name		|active_count	|event_count	|wakeup_count	|expire_count	|active_since	|total_time	|max_time	|last_change | prevent_suspend_time|" +
										"\n||" + line.replaceAll("\t\t|\t", "|") + "|\n";
								break;
							}
						}
					}
					
					br.close();
					
					
	
					if(result.toLowerCase().contains("total_time") || result.toLowerCase().contains("kernel")){
						br = new BufferedReader(new FileReader(reportInfo));
						long duration = 0;
						// Parse file
						while ((sCurrentLine = br.readLine()) != null) {
							if(sCurrentLine.toLowerCase().contains("product"))
							{
								if(sCurrentLine.toLowerCase().contains("surnia") || sCurrentLine.toLowerCase().contains("otus")){
									result = result + "{panel}\n\nDuplicate of IKSWL-1063";
									//break;
								}
								else if(sCurrentLine.toLowerCase().contains("thea")){
									result = result + "{panel}\n\nDuplicate of IKVPREL1L-5208";
									//break;
								}
								else if(sCurrentLine.toLowerCase().contains("shamu")){
									result = result + "{panel}\n\nDuplicate of IKXP-3367";
									//break;
								}
								else if(sCurrentLine.toLowerCase().contains("titan") || sCurrentLine.toLowerCase().contains("ghost")
										|| sCurrentLine.toLowerCase().contains("victara") || sCurrentLine.toLowerCase().contains("peregrine")
										|| sCurrentLine.toLowerCase().contains("falcon") || sCurrentLine.toLowerCase().contains("condor")){
									result = result + "{panel}\n\nDuplicate of IKSWL-1063";
									//break;
								}
								else if(sCurrentLine.toLowerCase().contains("osprey") || sCurrentLine.toLowerCase().contains("lux")
										|| sCurrentLine.toLowerCase().contains("kinzie")){
									result = result + "{panel}\n\nDuplicate of IKSWL-1063";
									//break;
								}
								else if(sCurrentLine.toLowerCase().contains("quark")){
									result = result + "{panel}\n\nDuplicate of IKSWL-4100";
									//break;
								}
								else if(sCurrentLine.toLowerCase().contains("kinzie"))
									result = result + "{panel}\n\nDuplicate of IKSWL-1063";
								
								else if(sCurrentLine.toLowerCase().contains("clark"))
									result = result + "{panel}\n\nDuplicate of IKSWL-1063";
								
								else{
									result = result + "{panel}\n\nDuplicate of IKVPREL1L-10144";
									//break;
								}
							}
							else if(sCurrentLine.toLowerCase().contains("duration")){
								duration = Long.parseLong(sCurrentLine.substring(sCurrentLine.indexOf("duration=")+9, sCurrentLine.indexOf(";", sCurrentLine.indexOf("duration=")+9)));
								if(result.contains("active_count") && diagMs < duration*0.7){
									result = "Following logs, it is not a Diag issue";
								}
							}
						}
						br.close();
						
						if(result.contains("Following logs")){
							unknownCRs.add(b2gID);
							b2g_analyzed.put(b2gID, "Not Diag");
							System.out.println("\n\nNo DIAG_WS detected! \n");
							BaseWindow.logWrite("\n\nNo DIAG_WS detected! \n");
						} else if(result.toLowerCase().contains("duplicate")){
							diagCRs.add(b2gID);
							b2gs_results.put(b2gID, result);
							
							b2g_analyzed.put(b2gID, "Diag");
						}
						
					} else {
						unknownCRs.add(b2gID);
						b2g_analyzed.put(b2gID, "Not Diag");
						System.out.println("\n\nNo DIAG_WS detected! \n");
						BaseWindow.logWrite("\n\nNo DIAG_WS detected! \n");
					}
					System.out.println("\n\nResult:\n" + result + "\n");
					BaseWindow.logWrite("\n\nResult:\n" + result + "\n");
				} catch (FileNotFoundException e){
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (br != null)
						try {
							br.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
				}
			}
		}

		System.out.println("\n------- CRs nao diag -------");
		BaseWindow.logWrite("\n------- CRs nao diag -------");
		for(String s : unknownCRs){
			System.out.println(s);
			BaseWindow.logWrite(s);
		}
		
		System.out.println("\n------- CRs Diag -------");
		BaseWindow.logWrite("\n------- CRs Diag -------");
		for(String s : diagCRs){
			System.out.println(s);
			BaseWindow.logWrite(s);
		}
		
	}
	
	
	/*
	 * closeDiagCrs
	 */
	public static void closeDiagCrs(){
		
		Object[] options = { "It's OK. Go!", "Cancel, I need to check" };
		int n = JOptionPane.showOptionDialog(BaseWindow.getCrsManager(),
				"Please, make sure that your username and password are set correctly at \"CRs and Jira\" tab. Otherwise, cancel this window and " +
						"check your login data.",
				"Warning",
				JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, options,
				options[1]);
		
		System.out.println("Resposta: " + n);
		BaseWindow.logWrite("Resposta: " + n);
		
		if(n == 0){
			try{
				WebDriver driver;
				FirefoxProfile profile;
				
				String user, pass;
				
				//Configuring Firefox
				System.out.println("Generating Firefox profile");
				BaseWindow.logWrite("Generating Firefox profile");
				profile = new FirefoxProfile(new File("Data\\complements\\profiles\\y2fvgaq0.bot"));
				driver = new FirefoxDriver(profile);
				user = BaseWindow.getCrsManager().getUserData()[0];
				pass = BaseWindow.getCrsManager().getUserData()[1];
		
				// Open up a browser
				System.out.println("Starting browser");
				driver.navigate().to("http://google.com");
				
				Object[] option = { "Yes", "No" };
				n = JOptionPane.showOptionDialog(BaseWindow.getCrsManager(),
						"Put DIAG_WS and krnl_wkl labels?",
						"Warning",
						JOptionPane.YES_NO_CANCEL_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, option,
						option[0]);
				
				
				for (int i = 0; i < diagCRs.size(); i++) {
					if(diagCRs.size() == 0)
						break;
					
					//Open CR page
					System.out.println("Opening CR page");
					System.out.println("CR: " + b2g_crid.get(diagCRs.get(i)));
					driver.navigate().to("http://idart.mot.com/browse/" + b2g_crid.get(diagCRs.get(i)));
					
					//Jira Login
					while(driver.getTitle().contains("Log")){
						System.out.println("Trying to Log in");
						driver.findElement(By.name("os_username")).sendKeys(user);		
						driver.findElement(By.name("os_password")).sendKeys(pass);
						//driver.findElement(By.name("os_cookie")).click();
						driver.findElement(By.name("login")).click();
						sleep(500);
					}
					
					//If New Projects, add labels
					if(n == 0){
						Exception e = new Exception();
						while(e != null){
							e = null;
							try {
								System.out.println("Trying to insert label");
								while(!driver.getPageSource().contains("<span>krnl_wkl</span>") && !driver.getPageSource().contains("<span>DIAG_WS</span>")){
									driver.findElement(By.cssSelector("body")).sendKeys(Keys.ESCAPE);
									sleep(100);
									driver.findElement(By.cssSelector("body")).sendKeys(Keys.ESCAPE);
									sleep(100);
									driver.findElement(By.cssSelector("body")).sendKeys(Keys.ESCAPE);
									sleep(500);
									driver.findElement(By.cssSelector("body")).sendKeys(".");
									sleep(800);
									driver.findElement(By.id("shifter-dialog-field")).sendKeys("label");
									sleep(1500);
									driver.findElement(By.id("shifter-dialog-field")).sendKeys(Keys.ENTER);
									sleep(1500);
									
									if(!driver.getPageSource().contains("<span>krnl_wkl</span>")){
										sleep(100);
										driver.findElement(By.id("labels-textarea")).sendKeys("krnl_wkl");
										sleep(250);
										driver.findElement(By.id("labels-textarea")).sendKeys(Keys.TAB);
										sleep(250);
									}
									if(!driver.getPageSource().contains("<span>DIAG_WS</span>")){
										driver.findElement(By.id("labels-textarea")).sendKeys("DIAG_WS");
										sleep(250);
										driver.findElement(By.id("labels-textarea")).sendKeys(Keys.TAB);
										sleep(2000);
									}
									
									driver.findElement(By.id("issue-workflow-transition-submit")).click();
									System.out.println("Label inserted");
									driver.get(driver.getCurrentUrl());
									sleep(2000);
								}
							} catch (Exception e1) {
								System.out.println("Label error");
								driver.get(driver.getCurrentUrl());
								e1.printStackTrace();
								e = e1;
							}
						}
					}
					
					System.out.println("Clicking CLOSE");
					driver.findElement(By.id("action_id_21")).click();
					sleep(2000);
					System.out.println("Setting as DUP");
					Exception e = new Exception();
					while(e != null){
						e = null;
						try{
							Select select = new Select(driver.findElement(By.id("resolution")));
							select.selectByVisibleText("Duplicate");
							sleep(600);
						} catch (Exception e1){
							e1.printStackTrace();
							e=e1;
						}
					}
					
					e = new Exception();
					while(e != null){
						e = null;
						try{
							System.out.println("Inserting root CR on Duplicated field: "
									+ b2gs_results.get(diagCRs.get(i)).substring(b2gs_results.get(diagCRs.get(i)).indexOf(" IK"), b2gs_results.get(diagCRs.get(i)).length()));
							driver.findElement(By.id("customfield_10622")).clear();
							sleep(600);
							driver.findElement(By.id("customfield_10622")).sendKeys(""+
									b2gs_results.get(diagCRs.get(i)).substring(b2gs_results.get(diagCRs.get(i)).indexOf(" IK"), b2gs_results.get(diagCRs.get(i)).length()));
							sleep(600);
							driver.findElement(By.id("customfield_10622")).sendKeys(Keys.TAB);
							
						} catch(Exception e1){
							e1.printStackTrace();
							e=e1;
						}
					}
					
					e = new Exception();
					while(e != null){
						e = null;
						try{
							System.out.println("Inserting comment");
							driver.findElement(By.xpath("//div[@id=\"workflow-transition-21-dialog\"]//textarea[@id='comment']")).sendKeys("" + b2gs_results.get(diagCRs.get(i)).replace("\t", ""));
							sleep(500);
							
							System.out.println("Closing");
							driver.findElement(By.id("issue-workflow-transition-submit")).submit();
							sleep(500);
							
						} catch(Exception e1){
							e1.printStackTrace();
							e=e1;
						}
					}
					
					System.out.println("Opening new tab");
					sleep(500);
					driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL + "t");
				}
				
				driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL + "w");
				System.out.println("Done");
				
				
				
				finalizeCRs(driver);
				
				
				
			} catch (Exception e){
				e.printStackTrace();
			}
		} else {
			System.out.println("Action cancelled");
		}
	}
	

	/*
	 * connectB2gidToJiraid
	 */
	private static void connectB2gidToJiraid(){
		try{
			WebDriver driver;
			FirefoxProfile profile;
			
			String user, pass;
			String[] CRs = BaseWindow.getCrsManager().getCrsToDownload();
			System.out.println("CRs: " + CRs.length);
			
			//Configuring Firefox
			System.out.println("Generating Firefox profile");
			profile = new FirefoxProfile(new File("Data\\complements\\profiles\\y2fvgaq0.bot"));
			driver = new FirefoxDriver(profile);
			user = BaseWindow.getCrsManager().getUserData()[0];
			pass = BaseWindow.getCrsManager().getUserData()[1];
	
			// Open up a browser
			System.out.println("Starting browser");
			driver.navigate().to("http://google.com");
			System.out.println("Done.");
			
			for (int i = 0; i < CRs.length; i++) {
				//Open CR page
				System.out.println("Opening CR page");
				System.out.println("CR: " + CRs[i]);
				driver.navigate().to("http://idart.mot.com/browse/" + CRs[i]);
				System.out.println("Done.");
				
				//Jira Login
				while(driver.getTitle().contains("Log")){
					//System.out.println("Trying to Log in");
					driver.findElement(By.name("os_username")).sendKeys(user);		
					driver.findElement(By.name("os_password")).sendKeys(pass);
					driver.findElement(By.name("os_cookie")).click();
					driver.findElement(By.name("login")).click();
					sleep(500);
				}
				
				
				String crID = driver.getTitle().substring(1, 12); 
				System.out.println("CR id: " + crID);
				
				WebElement Element = driver.findElement(By.partialLinkText("b2gadm-mcloud101-blur"));
				String b2gID = Element.getText().substring(Element.getText().indexOf('=') + 1);
				System.out.println("B2G id: " + b2gID);
				
				
				b2g_crid.put(b2gID, crID);
						
				
				System.out.println("\n\nOpening new tab");
				sleep(500);
				driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL + "t");
			}
			
			driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL + "w");
			driver.close();
			System.out.println("Done");
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	
	
	
	private static void finalizeCRs(WebDriver driver){
		
		Object[] options = new Object[]{ "Close Firefox", "Close Firefox and Open CRs on Chrome", "Nothing =)" };
		int n = JOptionPane.showOptionDialog(BaseWindow.getCrsManager(),
				"What do you want to do next?\n",
				"Warning",
				JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, options,
				options[1]);
		if(n == 0){
			System.out.println("Closing Firefox");
			if(driver != null)
				driver.close();
		} else if(n == 1){
			System.out.println("Closing Firefox and opening CRs on Chrome");
			b2g_crid.values().toArray();
			try{
				driver.close();
			} catch (NullPointerException e){
				e.printStackTrace();
			}
			for (Object value : b2g_crid.values().toArray()) {
				String s = value.toString();
				try {
					s = s.replaceAll("\n", "");
					s = s.replaceAll("\r", "");
					s = s.replaceAll(" ", "");
					Desktop.getDesktop().browse(new URI("http://idart.mot.com/browse/" + s));
					Thread.sleep(500);
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null, "Exception: " + ex.getMessage());
				}
			}
			//JOptionPane.showMessageDialog(null, "Feature not working yet");
		}
		
		
		options = new Object[]{ "Yes", "No" };
		n = JOptionPane.showOptionDialog(BaseWindow.getCrsManager(),
				"Do you want to delete the DIAG_WS CRs in your CRs folder?\n",
				"Warning",
				JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, options,
				options[1]);
		if(n == 0){
			
			String path = BaseWindow.getCrsManager().getRootPath();
			String folder = null;
			
			// Look for the file
			File rFolder = new File(path);
			File[] listOfFiles = rFolder.listFiles();
			System.out.println("Folder to look for files: " + rFolder);
			for (int a = 0; a < listOfFiles.length; a++) {
				folder = listOfFiles[a].getName();
				if (listOfFiles[a].isDirectory() && diagCRs.contains(folder)) {
					//System.out.println("Actual folder: " + folder);
					//System.out.println("Contains?: " + diagCRs.contains(folder));
					delCRs(folder);
				} else if(listOfFiles[a].isFile() && diagCRs.contains(folder) && listOfFiles[a].getName().endsWith(".zip")){
					delCRs(folder);
				}
			}
		}
		
		
		
		n = JOptionPane.showOptionDialog(BaseWindow.getCrsManager(),
				"Do you want to generate report_output on each remaining CR in your CRs folder?\n",
				"Warning",
				JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, options,
				options[1]);
		if(n == 0){
			dialog.setText("This dialog can be closed anytime you want. It will not stop the build reports work.\n" + 
					"Is recommended you wait them to be finished before doing something else.\n\n" + 
					"\n\nRunning build report at:");
			String crFolder;
			
			File folder = new File(BaseWindow.getCrsManager().getRootPath());
			System.out.println("Folder: " + folder);
			File[] listOfFiles = folder.listFiles();
			
			for (int i = 0; i < listOfFiles.length; i++) {
				folder = new File(BaseWindow.getCrsManager().getRootPath());
				listOfFiles = folder.listFiles();
				if (listOfFiles[i].isDirectory()) {
					try {
						crFolder = folder.getPath() + "\\" + listOfFiles[i].getName();
						System.out.println("File: " + crFolder);
						dialog.setText(dialog.getText() + "\n" + crFolder);
						
						String sCurrentLine;
						String file = null;
						BufferedReader br = null;
						// File seek and load configuration
						folder = new File(crFolder);
						File[] filesList = folder.listFiles();

						// Look for the file
						for (int j = 0; j < filesList.length; j++) {
							System.out.println(folder.listFiles()[j]);
							if (filesList[j].isFile()) {
								String files = filesList[j].getName();
								if (files.toLowerCase().endsWith(".txt") && files.toLowerCase().contains("report_info")) {
									file = crFolder + "\\" + files;
									break;
								}
							}
						}

						// Try to open file
						if (file == null)
						{
							System.out.println("Log de sistema nao encontrado: " + file);
						}
						else
						{
							br = new BufferedReader(new FileReader(file));
							System.out.println("Log de sistema encontrado!" + file);
						}
						
						// Parse file
						while ((sCurrentLine = br.readLine()) != null) {
							if(sCurrentLine.toLowerCase().contains("product"))
							{
								if(sCurrentLine.toLowerCase().contains("product"))
								{
									System.out.println(sCurrentLine);
									if(sCurrentLine.toLowerCase().contains("condor")){
										copyScript(new File("Data\\scripts\\Condor.pl") ,  new File(folder + "\\build_report.pl"));
										break;
									}
									else if(sCurrentLine.toLowerCase().contains("falcon") || sCurrentLine.toLowerCase().contains("peregrine") || sCurrentLine.toLowerCase().contains("titan") ){
										copyScript(new File("Data\\scripts\\Falcon_Peregrine_Titan.pl") ,  new File(folder + "\\build_report.pl"));
										break;
									}
									else if(sCurrentLine.toLowerCase().contains("ghost")){
										copyScript(new File("Data\\scripts\\Ghost.pl") ,  new File(folder + "\\build_report.pl"));
										break;
									}
									else if(sCurrentLine.toLowerCase().contains("osprey")){
										copyScript(new File("Data\\scripts\\Osprey.pl") ,  new File(folder + "\\build_report.pl"));
										break;
									}
									else if(sCurrentLine.toLowerCase().contains("surnia") || sCurrentLine.toLowerCase().contains("otus") || sCurrentLine.toLowerCase().contains("thea")){
										copyScript(new File("Data\\scripts\\Otus_Surnia_Thea.pl") ,  new File(folder + "\\build_report.pl"));
										break;
									}
									else if(sCurrentLine.toLowerCase().contains("quantum")){
										copyScript(new File("Data\\scripts\\Quantum.pl") ,  new File(folder + "\\build_report.pl"));
										break;
									}
									else if(sCurrentLine.toLowerCase().contains("shamu")){
										copyScript(new File("Data\\scripts\\Shamu.pl") ,  new File(folder + "\\build_report.pl"));
										break;
									}
									else if(sCurrentLine.toLowerCase().contains("victara")){
										copyScript(new File("Data\\scripts\\Victara.pl") ,  new File(folder + "\\build_report.pl"));
										break;
									}
									else if(sCurrentLine.toLowerCase().contains("quark")){
										copyScript(new File("Data\\scripts\\Quark.pl") ,  new File(folder + "\\build_report.pl"));
										break;
									}
									else if(sCurrentLine.toLowerCase().contains("kinzie")){
										copyScript(new File("Data\\scripts\\Kinzie.pl") ,  new File(folder + "\\build_report.pl"));
									}
									else if(sCurrentLine.toLowerCase().contains("clark")){
										copyScript(new File("Data\\scripts\\Clark.pl") ,  new File(folder + "\\build_report.pl"));
									}
									else if(sCurrentLine.toLowerCase().contains("lux")){
										copyScript(new File("Data\\scripts\\Lynx.pl") ,  new File(folder + "\\build_report.pl"));
										break;
									}
									else{
										JOptionPane.showMessageDialog(null, "Product name not found. Copying default build report (2200mah)");
										copyScript(new File("Data\\scripts\\build_report.pl") ,  new File(folder + "\\build_report.pl"));
										break;
									}
								}
							}
						}

						br.close();
						
						ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "cd " + crFolder + " && build_report.pl");
				        builder.redirectErrorStream(true);
				        Process p = builder.start();
				        BaseWindow.getCrsManager().addLogLine("Build report started at " + crFolder);
				        
				        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
				        //String line;
				        
				        while (true) {
				            //line = r.readLine();
				            if (r.readLine() == null) { break; }
				            //System.out.println(line);
				        }
				        r.close();
				        BaseWindow.getCrsManager().addLogLine("Build report script done at " + crFolder);
						
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		System.out.println("All done");
	}

	
	public static void copyScript(File source, File dest)
			throws IOException {
		FileUtils.copyFile(source, dest);
	}
	
	
	private static void delCRs(String dir){
		String folder = BaseWindow.getCrsManager().getRootPath() + dir;
		BaseWindow.getCrsManager().addLogLine("Trying to delete " + dir);
		File file = new File(folder);
		try {
			if(file.isDirectory()){
				System.out.println("Folder to be deleted: " + folder);
				FileUtils.deleteDirectory(file);
			} else if(file.isFile() && file.getName().endsWith(".zip")){
				System.out.println("Zip to be deleted: " + folder);
				file.delete();
			}
			BaseWindow.getCrsManager().addLogLine(dir + " deleted");
		} catch (IOException e) {
			BaseWindow.getCrsManager().addLogLine("Could not delete " + dir);
			e.printStackTrace();
		}
	}
		
	private static void sleep(int millis){
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	@Override
	public void run() {
		int step = 1;
		
		while(step != 10){
			if(step == 1){
				if(BaseWindow.getCrsManager().getB2g_crid() != null && BaseWindow.getCrsManager().getB2g_crid().size() > 0){
					b2g_crid = BaseWindow.getCrsManager().getB2g_crid();
					System.out.println("Geeting CRs/B2G IDs from the download usage\n" +
							"b2g_crid: " + b2g_crid.size());
					dialog.setText(dialog.getText() + "\nChecking the CRs ...");
					step = 2;
				}
				else {
					if(BaseWindow.getCrsManager().getCrsToDownload().length < 1){
						JOptionPane.showMessageDialog(null, "The connection between CR Jira IDs and CR B2G IDs were not found.\n" +
								"You can paste the CRs list on the text area below and try again.\n" +
								"This way, the tool will be able to generate this connection if needed.");
						step = 10;
						dialog.setText(dialog.getText() + "\nCanceling ...");
					} else {
						Object[] options = { "OK. Go!", "Cancel" };
						int n = JOptionPane.showOptionDialog(BaseWindow.getCrsManager(),
								"The connection between Jira IDs and B2G IDs were not found.\n" +
								"Click \"OK\" to the Tool generate this connection or \"Cancel\" to cacel the entire process",
								"Warning",
								JOptionPane.YES_NO_CANCEL_OPTION,
								JOptionPane.QUESTION_MESSAGE, null, options,
								options[1]);
						System.out.println("Resposta: " + n);
						
						if(n == 0){
							dialog.setText(dialog.getText() + "\nCreating the link between b2g ids and Jira ids ...");
							System.out.println("Connecting Jira IDs and B2G IDs");
							b2g_crid = BaseWindow.getCrsManager().getB2g_crid();
							connectB2gidToJiraid();
							System.out.println("b2g_crid: " + b2g_crid.size());
							System.out.println("Going to step 2");
							step = 2;
						} else {
							step = 10;
							dialog.setText(dialog.getText() + "\nCanceling ...");
						}
					}
				}
				
			} else if(step == 2){
				System.out.println("Searching for DIAG_WS issues");
				dialog.setText(dialog.getText() + "\nSearching for DIAG_WS issues in zip files ...");
				checkDiag();
				System.out.println("diagCRs: " + diagCRs.size() + "\nDiagList Updated");
				step = 3;
				
			} else if(step == 3){
				System.out.println("Closing DIAG_WS issues");
				dialog.setText(dialog.getText() + "\nClosing DIAG_WS issues on Jira...");
				BaseWindow.getCrsManager().updateAllDataUI();
				closeDiagCrs();
				step = 4;
				
			} else if(step == 4){
				dialog.dispose();
				dialog.setVisible(false);
				step = 10;
			} 
		}
		dialog.dispose();
		dialog.setVisible(false);
		BaseWindow.getCrsManager().updateAllDataUI();
		System.out.println(">>>>DiagList Updated");
	}



	public static ArrayList<String> getDiagCRs() {
		return diagCRs;
	}



	public static ArrayList<String> getUnknownCRs() {
		return unknownCRs;
	}



	public static HashMap<String, String> getB2g_crid() {
		return b2g_crid;
	}



	public static void setDiagCRs(ArrayList<String> diagCRs) {
		DiagCrsCloser.diagCRs = diagCRs;
	}



	public static void setUnknownCRs(ArrayList<String> unknownCRs) {
		DiagCrsCloser.unknownCRs = unknownCRs;
	}



	public static void setB2g_crid(HashMap<String, String> b2g_crid) {
		DiagCrsCloser.b2g_crid = b2g_crid;
	}
	
	
	
}
