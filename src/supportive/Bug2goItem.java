package supportive;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import core.Logger;
import core.SharedObjs;


/**
 * This class downloads a B2G file.
 */
public class Bug2goItem implements Runnable
{
	/**
	 * Variables
	 */
	public static enum DownloadStatus
	{
		STOPPED, DOWNLOADING, DONE, FAILED
	};
	
	private static final String	BASE_DOWNLOAD_LINK = "https://b2gadm-mcloud101-blur.svcmot.com/bugreport/report/downloadlog.action";
	private static final String	BUG_ID_PARAM	   = "bg_id=BUGID";
	private HttpURLConnection	connection;
	private OutputStream		out;
	private String				bugId;
	private int					sizeOfFile;
	private int					downloadProgress;
	private boolean				running;
	private DownloadStatus		status;
	
	/**
	 * Initialize class variables
	 */
	public Bug2goItem(String bugNumber)
	{
		bugId = bugNumber;
		running = false;
		downloadProgress = 0;
		status = DownloadStatus.STOPPED;
	}
	
	/**
	 * Remove this current B2G item from the b2goListInProgress kept by Bug2goDownloader
	 */
	public void removeFromList(Bug2goItem item)
	{
		Bug2goDownloader b2gDownloader = Bug2goDownloader.getInstance();
		
		try
		{
			b2gDownloader.getSemaphore().acquire();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		
		b2gDownloader.removeBugItem(this);
		
		b2gDownloader.getSemaphore().release();
	}
	
	/**
	 * Start to download the B2G log
	 */
	@Override
	public void run()
	{
		int responseCode = -1;
		
		running = true;
		status = DownloadStatus.DOWNLOADING;
		
		FileOutputStream file = null;
		
		String bugNumber;
		bugNumber = BUG_ID_PARAM.replace("BUGID", bugId);
		
		// Open connection to download the CR
		URL urlDownload;
		try
		{
			urlDownload = new URL(BASE_DOWNLOAD_LINK);
			
			connection = (HttpURLConnection) urlDownload.openConnection();
			
			// For POST only - START
			// Use the URL connection for output
			connection.setDoOutput(true);
			out = connection.getOutputStream();
			out.write(bugNumber.getBytes());
			out.flush();
			out.close();
			// For POST only - END
			
			responseCode = connection.getResponseCode();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			running = false;
			status = DownloadStatus.FAILED;
			removeFromList(this);
			connection.disconnect();
			return;
		}
		
		Logger.log(Logger.TAG_BUG2GOITEM, "POST Response Code :: " + responseCode);
		Logger.log(Logger.TAG_BUG2GOITEM, "URL after download POST: " + connection.getURL());
		
		if (responseCode == HttpURLConnection.HTTP_OK
			&& !connection.getURL().toString()
						  .equals("https://b2gadm-mcloud101-blur.svcmot.com/bugreport/report/verify.action"))
		{
			File downloadFolder;
			
			// Get the file name to be downloaded
			String fileName = connection.getHeaderField("Content-Disposition");
			fileName = fileName.replace("\"", "");
			fileName = fileName.substring(fileName.lastIndexOf("=") + 1);
			
			sizeOfFile = connection.getContentLength();
			
			Logger.log(Logger.TAG_BUG2GOITEM,
					   "Size of file: " + String.valueOf(connection.getContentLength()));
			Logger.log(Logger.TAG_BUG2GOITEM, "File being downloaded: " + fileName);
			Logger.log(Logger.TAG_BUG2GOITEM, "File being saved in: " + SharedObjs.getDownloadPath());
			
			// Get download folder
			downloadFolder = new File(SharedObjs.getDownloadPath());
			
			// If user doesn't want to overwrite an existing file with the same name, abort download
			if (!Bug2goDownloader.getInstance().isOverwrite())
			{
				for (File f : downloadFolder.listFiles())
				{
					if (f.getName().equals(fileName))
					{
						Logger.log(Logger.TAG_BUG2GOITEM, "File already exists. Aborting download.");
						status = DownloadStatus.FAILED;
						running = false;
						removeFromList(this);
						connection.disconnect();
						return;
					}
				}
			}
			
			// Creates a new file
			try
			{
				file = new FileOutputStream(SharedObjs.getDownloadPath() + "\\" + fileName);
				// Buffer
				byte[] buffer = new byte[4096];
				int len;
				InputStream reader = connection.getInputStream();
				
				// Getting the file from server
				while ((len = reader.read(buffer)) > 0)
				{
					file.write(buffer, 0, len);
					downloadProgress += len;
				}
				
				reader.close();
				SharedObjs.crsManagerPane.addLogLine(fileName + " download finished");
				file.close();
				
				status = DownloadStatus.DONE;
				running = false;
				
				removeFromList(this);
			}
			catch (IOException e)
			{
				e.printStackTrace();
				status = DownloadStatus.FAILED;
				running = false;
				removeFromList(this);
				connection.disconnect();
				return;
			}
			
		}
		else
		{
			Logger.log(Logger.TAG_BUG2GOITEM, "POST request did not work");
			status = DownloadStatus.FAILED;
			running = false;
			removeFromList(this);
			connection.disconnect();
		}
		
		Logger.log(Logger.TAG_BUG2GOITEM, bugId + " Thread dead");
	}
	
	/**
	 * Getters and Setters
	 */
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
	
	public DownloadStatus getStatus()
	{
		return status;
	}
	
	public void setStatus(DownloadStatus status)
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
