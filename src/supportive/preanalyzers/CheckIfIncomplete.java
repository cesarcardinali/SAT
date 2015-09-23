package supportive.preanalyzers;


import java.io.File;
import java.util.ArrayList;

import core.Logger;


public class CheckIfIncomplete
{
	String            folderPath;
	ArrayList<String> incompleteFiles;
	ArrayList<String> filesNames;
	ArrayList<File>   files;
	
	/**
	 * @param crPath
	 * @return
	 */
	public boolean checkIt(String crPath)
	{
		folderPath = crPath;
		incompleteFiles = new ArrayList<String>();
		filesNames = new ArrayList<String>();
		files = new ArrayList<File>();
		
		File folder = new File(folderPath);
		
		if (!folder.isDirectory())
		{
			Logger.log(Logger.TAG_CR_CHECKER, "Not a directory");
			return false;
		}
		
		for (File file : folder.listFiles())
		{
			filesNames.add(file.getName());
			files.add(file);
		}
		
		// Check files
		if (files.contains(new File(folderPath + "/aplogcat-main.txt")))
		{
			File f = new File(folderPath + "/aplogcat-main.txt");
			if (f.length() / 1048576 < 2)
			{
				incompleteFiles.add("main");
				Logger.log(Logger.TAG_CR_CHECKER, "Main file is too short");
			}
		}
		else
		{
			incompleteFiles.add("main");
		}
		
		if (files.contains(new File(folderPath + "/aplogcat-system.txt")))
		{
			File f = new File(folderPath + "/aplogcat-system.txt");
			if (f.length() / 1048576 < 2)
			{
				incompleteFiles.add("system");
				Logger.log(Logger.TAG_CR_CHECKER, "System file is too short");
			}
		}
		else
		{
			incompleteFiles.add("system");
		}
		
		if (files.contains(new File(folderPath + "/aplogcat-kernel.txt")))
		{
			File f = new File(folderPath + "/aplogcat-kernel.txt");
			if (f.length() / 1048576 < 2)
			{
				incompleteFiles.add("kernel");
				Logger.log(Logger.TAG_CR_CHECKER, "Kernel file is too short");
			}
		}
		else
		{
			incompleteFiles.add("kernel");
		}
		
		if (files.contains(new File(folderPath + "/aplogcat-radio.txt")))
		{
			File f = new File(folderPath + "/aplogcat-radio.txt");
			if (f.length() / 1048576 < 2)
			{
				incompleteFiles.add("radio");
				Logger.log(Logger.TAG_CR_CHECKER, "Radio file is too short");
			}
		}
		else
		{
			incompleteFiles.add("radio");
		}
		
		if (incompleteFiles.size() == 4)
			return true;
		else
			return false;
	}
	
	public ArrayList<String> getIncompleteFiles()
	{
		return incompleteFiles;
	}
	
	public void setIncompleteFiles(ArrayList<String> incompleteFiles)
	{
		this.incompleteFiles = incompleteFiles;
	}
}
