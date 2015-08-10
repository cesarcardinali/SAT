package supportive;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;

import main.SAT;
import panes.UnzippingCrsDialog;


public class UnZip implements Runnable{
	
	List<String> fileList;
	UnzippingCrsDialog dialog;
	static SAT BaseWindow;
	static Process p;
	static String file;
	
	String path;
	
	
	public UnZip(String path, SAT parent){
		dialog = null;
		BaseWindow = parent;
		this.path = path;
	}

	

	@Override
	public void run() {
		File fileFolder = new File(path);
		if(fileFolder.isDirectory()){
			JOptionPane.showMessageDialog(null, "Avisa o rapaizin responsavel por essa SAT aki q deu biziu em alguma coisa na hora de extrair ae\n" +
					"Mas pera, se vc nao rodou com logs nem precisa avisar, tem nada q da pra fazer nesse caso nao negao");
			
		} else if(fileFolder.isFile()){
			if (fileFolder.getName().toLowerCase().endsWith(".zip")) {
				System.out.println("File to unzip: " + path);
				BaseWindow.logWrite("File to unzip: " + path);
				
				file = fileFolder.getAbsolutePath();
				String fileName = getFileName(file);
				String fileFolderPath = getFileFolders(file);
				String outputFolder = fileFolderPath + fileName;
				
				System.out.println("File full path: " + file + 
						"\nFile Name: " + fileName +
						"\nFile folder path: " + fileFolderPath +
						"\nOutput folder: " + fileFolderPath + fileName);
				
				try {
					BaseWindow.getCrsManager().addLogLine("Unzipping file to " + fileName);
					unZipIt(file, outputFolder);
					BaseWindow.getUnzipSemaphore().release();
					
					String sCurrentLine;
					BufferedReader br = null;
					// File seek and load configuration
					String npath = outputFolder;
					fileFolder = new File(npath);
					File[] filesList = fileFolder.listFiles();

					// Look for the file
					for (int j = 0; j < filesList.length; j++) {
						System.out.println(fileFolder.listFiles()[j]);
						BaseWindow.logWrite("" + fileFolder.listFiles()[j]);
						if (filesList[j].isFile()) {
							String files = filesList[j].getName();
							if (files.toLowerCase().endsWith(".txt") && files.toLowerCase().contains("report_info")) {
								npath = npath + "\\" + files;
								break;
							}
						}
					}

					// Try to open file
					if (npath.equals(outputFolder))
					{
						System.out.println("Log de sistema nao encontrado em " + outputFolder);
						BaseWindow.logWrite("Log de sistema nao encontrado em " + outputFolder);
					}
					else
					{
						br = new BufferedReader(new FileReader(npath));
						System.out.println("Log de sistema encontrado!" + npath);
						BaseWindow.logWrite("Log de sistema encontrado!" + npath);
					}
					
					// Parse file
					while ((sCurrentLine = br.readLine()) != null) {
						if(sCurrentLine.toLowerCase().contains("product"))
						{
							System.out.println("--- Initial line: " + sCurrentLine);
							sCurrentLine = sCurrentLine.replace("\"PRODUCT\": \"", "").replace(" ", "");
							sCurrentLine = sCurrentLine.substring(0, sCurrentLine.indexOf("_"));
							System.out.println(sCurrentLine);
							
							copyScript(new File("Data\\scripts\\_Base.pl") ,  new File(outputFolder + "\\build_report.pl"));
							
							// Configure build report battery capacity
							try {
								@SuppressWarnings("resource")
								Scanner scanner = new Scanner(new File(outputFolder + "\\build_report.pl"));
								String content = scanner.useDelimiter("\\Z").next();
								content = content.replace("#bat_cap#", BaseWindow.getOptions().getAdvOptions().getBat_capNode().getChildText(sCurrentLine));
								PrintWriter out = new PrintWriter(outputFolder + "\\build_report.pl");
								out.println(content);
								out.close();
							} catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							break;
						}
					}

					br.close();
					
					ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "cd " + outputFolder + " && build_report.pl");
			        builder.redirectErrorStream(true);
			        p = builder.start();
			        
			        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
			        String line;
			        
			        BaseWindow.getCrsManager().addLogLine("Running build_report.pl at " + fileName);
			        while (true) {
			            line = r.readLine();
			            if (line == null) { break; }
			            System.out.println(line);
						BaseWindow.logWrite(line);
			            //BaseWindow.getCrsManager().addLogLine(line);
			        }
			        
			        BaseWindow.getCrsManager().addLogLine("Finished unzipping and running Bulid_report.pl of " + fileName);
					
				} catch (IOException e) {
					e.printStackTrace();
				}
								
				BaseWindow.getCrsManager().updateAllDataUI();
				if(dialog != null){
					dialog.dispose();
					dialog.setVisible(false);
				}
				
		        System.out.println(fileName + " Successfully extracted");
				BaseWindow.logWrite(fileName + " Successfully extracted");
			} else {
				System.out.println("It is not a ZIP file. Action cancelled.");
				BaseWindow.logWrite("It is not a ZIP file. Action cancelled.");
			}
		}
	}

	
	
	
	
	//Not a runnable but same function of runnable one
	public void unzipFile() {
		File fileFolder = new File(path);
		if(fileFolder.isDirectory()){
			JOptionPane.showMessageDialog(null, "Avisa o rapaizin responsavel por essa SAT aki q deu biziu em alguma coisa na hora de extrair ae\n" +
					"Mas pera, se vc nao rodou com logs nem precisa avisar, tem nada q da pra fazer nesse caso nao negao");
			
		} else if(fileFolder.isFile()){
			if (fileFolder.getName().toLowerCase().endsWith(".zip")) {
				System.out.println("File to unzip: " + path);
				BaseWindow.logWrite("File to unzip: " + path);
				
				file = fileFolder.getAbsolutePath();
				String fileName = getFileName(file);
				String fileFolderPath = getFileFolders(file);
				String outputFolder = fileFolderPath + fileName;
				
				System.out.println("File full path: " + file + 
						"\nFile Name: " + fileName +
						"\nFile folder path: " + fileFolderPath +
						"\nOutput folder: " + fileFolderPath + fileName);
				BaseWindow.logWrite("File full path: " + file + 
						"\nFile Name: " + fileName +
						"\nFile folder path: " + fileFolderPath +
						"\nOutput folder: " + fileFolderPath + fileName);
				try {
					BaseWindow.getCrsManager().addLogLine("Unzipping file to " + fileName);
					unZipIt(file, outputFolder);
					BaseWindow.getUnzipSemaphore().release();
					
					String sCurrentLine;
					BufferedReader br = null;
					// File seek and load configuration
					String npath = outputFolder;
					fileFolder = new File(npath);
					File[] filesList = fileFolder.listFiles();

					// Look for the file
					for (int j = 0; j < filesList.length; j++) {
						System.out.println(fileFolder.listFiles()[j]);
						BaseWindow.logWrite("" + fileFolder.listFiles()[j]);
						if (filesList[j].isFile()) {
							String files = filesList[j].getName();
							if (files.toLowerCase().endsWith(".txt") && files.toLowerCase().contains("report_info")) {
								npath = npath + "\\" + files;
								break;
							}
						}
					}

					// Try to open file
					if (npath.equals(outputFolder))
					{
						System.out.println("Log de sistema nao encontrado em " + outputFolder);
						BaseWindow.logWrite("Log de sistema nao encontrado em " + outputFolder);
					}
					else
					{
						br = new BufferedReader(new FileReader(npath));
						System.out.println("Log de sistema encontrado!" + npath);
						BaseWindow.logWrite("Log de sistema encontrado!" + npath);
					}
					
					// Parse file
					while ((sCurrentLine = br.readLine()) != null) {
						if(sCurrentLine.toLowerCase().contains("product"))
						{
							System.out.println("--- Initial line: " + sCurrentLine);
							sCurrentLine = sCurrentLine.replace("\"PRODUCT\": \"", "").replace(" ", "");
							sCurrentLine = sCurrentLine.substring(0, sCurrentLine.indexOf("_"));
							System.out.println(sCurrentLine);
							
							copyScript(new File("Data\\scripts\\_Base.pl") ,  new File(outputFolder + "\\build_report.pl"));
							
							// Configure build report battery capacity
							try {
								@SuppressWarnings("resource")
								Scanner scanner = new Scanner(new File(outputFolder + "\\build_report.pl"));
								String content = scanner.useDelimiter("\\Z").next();
								content = content.replace("#bat_cap#", BaseWindow.getOptions().getAdvOptions().getBat_capNode().getChildText(sCurrentLine));
								PrintWriter out = new PrintWriter(outputFolder + "\\build_report.pl");
								out.println(content);
								out.close();
							} catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							break;
						}
					}

					br.close();
					
					ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "cd " + outputFolder + " && build_report.pl");
			        builder.redirectErrorStream(true);
			        p = builder.start();
			        
			        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
			        String line;
			        
			        BaseWindow.getCrsManager().addLogLine("Running build_report.pl at " + fileName);
			        while (true) {
			            line = r.readLine();
			            if (line == null) { break; }
			            System.out.println(line);
						BaseWindow.logWrite(line);
			            //BaseWindow.getCrsManager().addLogLine(line);
			        }
			        
			        BaseWindow.getCrsManager().addLogLine("Finished unzipping and running Bulid_report.pl of " + fileName);
					
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				BaseWindow.getCrsManager().updateAllDataUI();
				if(dialog != null){
					dialog.dispose();
					dialog.setVisible(false);
				}
				
		        System.out.println(fileName + " Successfully extracted");
				BaseWindow.logWrite(fileName + " Successfully extracted");
			} else {
				System.out.println("It is not a ZIP file. Action cancelled.");
				BaseWindow.logWrite("It is not a ZIP file. Action cancelled.");
			}
		}
	}
	
	
	
	
	
	//supportive functions -----------------------------------------------------------------------------------------------------------------------
	
	public void delZips(String file){
		try {
			System.out.println("File to delete: " + file);
			BaseWindow.logWrite("File to delete: " + file);
			ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "del \"" + file + "\"");
	        builder.redirectErrorStream(true);
			p = builder.start();
			
			BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
	        String line;
	        while (true) {
	            line = r.readLine();
	            if (line == null) { break; }
	            System.out.println(line);
				BaseWindow.logWrite(line);
	        }
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void delZips(){
		ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "cd " + file.substring(0, file.length() - 28) + "&& cd .. && del *.zip");
        builder.redirectErrorStream(true);
        try {
			p = builder.start();
			BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
	        String line;
	        while (true) {
	            line = r.readLine();
	            if (line == null) { break; }
	            System.out.println(line);
				BaseWindow.logWrite(line);
	        }
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void copyScript(File source, File dest)
			throws IOException {
		FileUtils.copyFile(source, dest);
	}
	
	
	
	private String getFileName(String fullPath){
		String [] folders = fullPath.split("\\\\");
		System.out.println("File name: " + folders[folders.length-1] +
				"\nSubstring: " + folders[folders.length-1].subSequence(0, 8));
		BaseWindow.logWrite("File name: " + folders[folders.length-1] +
				"\nSubstring: " + folders[folders.length-1].subSequence(0, 8));
		return (String) folders[folders.length-1].subSequence(0, 8);
	}
	
	private String getFileFolders(String fullPath){
		String [] folders = fullPath.split("\\\\");
		String path = "";
		for(String s : folders){
			if(!s.contains(".zip"))
				path = path + s + File.separator;
		}
		System.out.println("File path: " + path);
		BaseWindow.logWrite("File path: " + path);
		return path;
	}
	
	
	
	
	
	
	/**
	 * Unzip it
	 * 
	 * @param zipFile
	 *            input zip file
	 * @param output
	 *            zip file output fileFolder
	 */
	public static void unZipIt(String zipFile, String outputFolder) {

		byte[] buffer = new byte[1024];

		try {

			// create output directory is not exists
			File fileFolder = new File(outputFolder);
			if (!fileFolder.exists()) {
				fileFolder.mkdir();
			}

			// get the zip file content
			ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));
			// get the zipped file list entry
			ZipEntry ze = zis.getNextEntry();

			while (ze != null) {
				String fileName = ze.getName();
				File newFile = new File(outputFolder + File.separator + fileName);

				System.out.println("file unzip : " + newFile.getAbsoluteFile());

				// create all non exists folders
				// else you will hit FileNotFoundException for compressed fileFolder
				new File(newFile.getParent()).mkdirs();

				FileOutputStream fos = new FileOutputStream(newFile);

				int len;
				while ((len = zis.read(buffer)) > 0) {
					fos.write(buffer, 0, len);
				}

				fos.close();
				ze = zis.getNextEntry();
			}

			zis.closeEntry();
			zis.close();

			System.out.println(zipFile + " extraction succeful\n");

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
