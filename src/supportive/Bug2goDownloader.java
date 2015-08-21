package supportive;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import core.Logger;
import core.SharedObjs;


public class Bug2goDownloader implements Runnable
{
	private ArrayList<Bug2goItem> bug2goList;
	private boolean				  alldone = false;
	private String				  downloadPath;
	private int					  errors;
	
	public Bug2goDownloader(ArrayList<String> bugIdList, String downloadPath)
	{
		bug2goList = new ArrayList<Bug2goItem>();
		this.downloadPath = downloadPath;
		errors = 0;
		
		for (String s : bugIdList)
		{
			bug2goList.add(new Bug2goItem(s));
		}
	}
	
	public Bug2goDownloader(String[] bugIdList, String downloadPath)
	{
		bug2goList = new ArrayList<Bug2goItem>();
		this.downloadPath = downloadPath;
		errors = 0;
		
		for (String s : bugIdList)
		{
			bug2goList.add(new Bug2goItem(s));
		}
	}
	
	@Override
	public void run()
	{
		for (Bug2goItem b : bug2goList)
		{
			new Thread(b, b.getBugId()).start();
		}
		
		while (alldone == false)
		{
			alldone = true;
			Logger.log(Logger.TAG_BUG2GODOWNLOADER, "Checking...");
			
			for (Bug2goItem b : bug2goList)
			{
				Logger.log(Logger.TAG_BUG2GODOWNLOADER,
						   b.getBugId() + ": status > " + b.getStatus() + " | running > " + b.isRunning());
						   
				if (b.isRunning() == true && alldone == true)
				{
					alldone = false;
				}
				
				if (b.getStatus().contains("error") && errors == 0)
				{
					errors = 1;
				}
			}
			
			try
			{
				Thread.sleep(3000);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		
		Logger.log(Logger.TAG_BUG2GODOWNLOADER, "Downloads finished");
		
		for (Bug2goItem b : bug2goList)
		{
			Logger.log(Logger.TAG_BUG2GODOWNLOADER, b.getBugId() + ": " + b.getStatus());
		}
		
		if (errors == 1)
			JOptionPane.showMessageDialog(SharedObjs.satFrame,
										  "There were errors during the download. \nFiles may be missing or corrupted.");
										  
		// Ask if user wants to unzip them all
		int ans = JOptionPane.showOptionDialog(SharedObjs.crsManagerPane, "What do you want to do next?",
											   "Downloads completed", JOptionPane.YES_NO_OPTION,
											   JOptionPane.QUESTION_MESSAGE, null,
											   new Object[] {"Unzip and build report output",
															 "Just unzip them all",
															 "Nothing, I am ok"},
											   null);
		if (ans == 0)
		{
			File[] filesName = new File(downloadPath).listFiles();
			
			for (File file : filesName)
			{
				if (file.isFile() && file.getName().contains(".zip") && file.getName().contains("_B2G_"))
				{
					UnZip.unZipIt(file.getAbsolutePath(),
								  file.getAbsolutePath().substring(0, file.getAbsolutePath().length() - 28));
								  
					file = new File(file.getAbsolutePath().substring(0,
																	 file.getAbsolutePath().length() - 28));
																	 
					try
					{
						SharedObjs.crsManagerPane.runScript(file.getAbsolutePath());
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
					
					Logger.log(Logger.TAG_BUG2GODOWNLOADER, "Done for " + file.getAbsolutePath());
				}
			}
			
			JOptionPane.showMessageDialog(SharedObjs.crsManagerPane,
										  "All b2g files are unzipped and with report output generated.");
		}
		else if (ans == 1)
		{
			File[] filesName = new File(downloadPath).listFiles();
			
			for (File file : filesName)
			{
				if (file.isFile() && file.getName().contains(".zip") && file.getName().contains("_B2G_"))
				{
					UnZip.unZipIt(file.getAbsolutePath(),
								  file.getAbsolutePath().substring(0, file.getAbsolutePath().length() - 28));
								  
					file = new File(file.getAbsolutePath().substring(0,
																	 file.getAbsolutePath().length() - 28));
																	 
					Logger.log(Logger.TAG_BUG2GODOWNLOADER, "Done for " + file.getAbsolutePath());
				}
			}
			
			JOptionPane.showMessageDialog(SharedObjs.crsManagerPane, "All b2g files are unzipped.");
		}
	}
	
	public boolean isAlldone()
	{
		return alldone;
	}
	
	public void setAlldone(boolean alldone)
	{
		this.alldone = alldone;
	}
	
	public String getDownloadPath()
	{
		return downloadPath;
	}
	
	public void setDownloadPath(String downloadPath)
	{
		this.downloadPath = downloadPath;
	}
	
}
