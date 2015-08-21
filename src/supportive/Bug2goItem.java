package supportive;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import core.Logger;
import core.SharedObjs;


public class Bug2goItem implements Runnable
{
	public static final String genericCommand = "getbug -v --user USER --passwd PASSWD --overwrite BUGID";
	private String			   command;
	private String			   bugId;
	private int				   sizeOfFile;
	private int				   downloadProgress;
	private boolean			   running;
	private String			   status;
	
	public Bug2goItem(String bugNumber)
	{
		bugId = bugNumber;
		running = false;
		downloadProgress = 0;
		status = "stopped";
		buildCommand();
	}
	
	private String buildCommand()
	{
		command = genericCommand.replace("USER", SharedObjs.getUser());
		command = command.replace("PASSWD", SharedObjs.getPass());
		command = command.replace("BUGID", bugId);
		
		running = true;
		
		return command;
	}
	
	public boolean isDownloadComplete()
	{
		if (sizeOfFile == downloadProgress)
		{
			return true;
			
		}
		else
		{
			return false;
		}
	}
	
	@Override
	public void run()
	{
		status = "downloading";
		
		try
		{
			SharedObjs.copyScript(new File("Data/complements/b2g_script/getbug.py"),
								  new File(SharedObjs.getDownloadPath() + "\\getbug.py"));
			SharedObjs.copyScript(new File("Data/complements/b2g_script/__init__.py"),
								  new File(SharedObjs.getDownloadPath() + "\\__init__.py"));
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
			Logger.log(Logger.TAG_BUG2GOITEM, "Error trying to copy getbug.py");
			return;
		}
		
		ProcessBuilder builder = new ProcessBuilder("cmd.exe",
													"/c",
													"cd \"" + SharedObjs.getDownloadPath() + "\" && "
														  + command);
		builder.redirectErrorStream(true);
		Process p;
		
		try
		{
			p = builder.start();
			BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
			
			String line;
			Logger.log(Logger.TAG_BUG2GOITEM, bugId + " Thread started");
			
			while ((line = r.readLine()) != null)
			{
				Logger.log(Logger.TAG_BUG2GOITEM, line);
				
				if (line.contains("Login failed"))
				{
					Logger.log(Logger.TAG_BUG2GOITEM, "Error: CR couldn't be downloaded. Failed to login");
					running = false;
					status = "error: login failed";
				}
				else if (line.contains("DEBUG") && line.contains("POST /bugreport/report/downloadlog")
						 && Integer.parseInt(line.substring(line.lastIndexOf(" ") + 1)) != 0)
				{
					sizeOfFile = Integer.parseInt(line.substring(line.lastIndexOf(" ") + 1));
				}
				else if (line.contains("The system cannot find the path specified."))
				{
					status = "error: path does not exists";
					Logger.log(Logger.TAG_BUG2GOITEM, status);
					running = false;
				}
				
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		if(status.equals("downloading"))
			status = "done";
		running = false;
		
		SharedObjs.crsManagerPane.addLogLine(bugId + " status: " + status);
		Logger.log(Logger.TAG_BUG2GOITEM, bugId + " Thread dead");
	}
	
	public String getBugId()
	{
		return bugId;
	}
	
	public void setBugId(String bugId)
	{
		this.bugId = bugId;
	}
	
	public int getSizeOfFile()
	{
		return sizeOfFile;
	}
	
	public void setSizeOfFile(int sizeOfFile)
	{
		this.sizeOfFile = sizeOfFile;
	}
	
	public int getDownloadProgress()
	{
		return downloadProgress;
	}
	
	public void setDownloadProgress(int downloadProgress)
	{
		this.downloadProgress = downloadProgress;
	}
	
	public String getStatus()
	{
		return status;
	}
	
	public void setStatus(String status)
	{
		this.status = status;
	}
	
	public boolean isRunning()
	{
		return running;
	}
	
	public void setRunning(boolean run)
	{
		this.running = run;
	}
}
