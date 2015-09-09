package supportive;


import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import javax.swing.JOptionPane;

import objects.CrItem;
import core.Logger;
import core.SharedObjs;


/**
 * This class monitor the files being downloaded.
 */
public class Bug2goDownloader implements Runnable
{
	/**
	 * Variables
	 */
	private static final String   BASE_LOGIN_LINK = "https://b2gadm-mcloud101-blur.svcmot.com/bugreport/report/verify.action";
	private static final String   LOGIN_PARAM     = "username=COREID&password=PASSWRD";
	private HttpURLConnection     connection;
	private OutputStream          out;
	private ArrayList<Bug2goItem> bug2goListSubmitted;
	private ArrayList<Bug2goItem> bug2goListInProgress;
	private ArrayList<Bug2goItem> bug2goListDone;
	private ArrayList<Bug2goItem> bug2goListFailed;
	private int                   errors;
	private boolean               overwrite;
	private Semaphore             semaphore;
	private ExecutorService       executor;
	
	/**
	 * Initialize class variables. The constructor is private in order to implement the Singleton design pattern
	 */
	private Bug2goDownloader()
	{
		overwrite = true;
		semaphore = new Semaphore(1);
		bug2goListSubmitted = new ArrayList<Bug2goItem>();
		bug2goListInProgress = new ArrayList<Bug2goItem>();
		bug2goListDone = new ArrayList<Bug2goItem>();
		bug2goListFailed = new ArrayList<Bug2goItem>();
		errors = 0;
		
		// Used to avoid the error: Security: Server SSL Error-handshake alert:unrecognized_name
		
		// You may get this SSL error if the server you are trying to access has not been properly configured.
		// For security reasons SNI extension has been enabled by default in Java 7. However, if you trust the server you are trying to
		// connect you may want to disable SNI extension.
		// Reference: http://forums.visokio.com/discussion/2614/security-server-ssl-error-handshake-alertunrecognized_name
		System.setProperty("jsse.enableSNIExtension", "false");
		
		// Used to maintain the session
		
		// CookieManager provides a concrete implementation of CookieHandler, which separates the storage of cookies from the policy
		// surrounding accepting and rejecting cookies.
		CookieManager cookieManager = new CookieManager();
		// CookieHandler is at the core of cookie management. User can call CookieHandler.setDefault to set a concrete CookieHanlder
		// implementation to be used.
		CookieHandler.setDefault(cookieManager);
		
	}
	
	/**
	 * Private inner static class
	 */
	private static class Bug2goDownloaderHolder
	{
		private static final Bug2goDownloader INSTANCE = new Bug2goDownloader();
	}
	
	/**
	 * Returns the unique instance of Bug2goDownloader
	 */
	public static Bug2goDownloader getInstance()
	{
		return Bug2goDownloaderHolder.INSTANCE;
	}
	
	/**
	 * Add Bug2go itens in the list to be downloaded
	 */
	public boolean addBugId(String[] bugIdList) throws InterruptedException
	{
		semaphore.acquire();
		for (String s : bugIdList)
		{
			if (!bug2goListSubmitted.add(new Bug2goItem(s)))
			{
				Logger.log(Logger.TAG_BUG2GODOWNLOADER, "Failed to add bugID " + s + " on BugIdList.");
			}
		}
		semaphore.release();
		return true;
	}
	
	/**
	 * Add Bug2go itens in the list to be downloaded
	 */
	public boolean addBugIdList(ArrayList<String> bugIdList) throws InterruptedException
	{
		semaphore.acquire();
		for (String s : bugIdList)
		{
			if (!bug2goListSubmitted.add(new Bug2goItem(s)))
			{
				Logger.log(Logger.TAG_BUG2GODOWNLOADER, "Failed to add bugID " + s + " on BugIdList.");
			}
		}
		semaphore.release();
		return true;
	}
	
	/**
	 * Remove a Bug2go item from one of the lists
	 */
	public boolean removeBugItem(Bug2goItem item)
	{
		boolean removed;
		
		removed = bug2goListInProgress.remove(item);
		if (removed)
		{
			if (item.getStatus() == Bug2goItem.DownloadStatus.DONE)
			{
				bug2goListDone.add(item);
			}
			
			else if (item.getStatus() == Bug2goItem.DownloadStatus.FAILED)
			{
				bug2goListFailed.add(item);
			}
			
		}
		
		Logger.log(Logger.TAG_BUG2GODOWNLOADER, "Was the item removed? " + removed);
		return removed;
	}
	
	/**
	 * Initiate the download execution
	 */
	public void execute()
	{
		if (executor == null)
		{
			new Thread(this).start();
		}
		else if (executor.isTerminated())
		{
			new Thread(this).start();
		}
	}
	
	/**
	 * Try to login
	 */
	private boolean login() throws IOException
	{
		String login;
		
		// Create the login string
		login = LOGIN_PARAM.replace("COREID", SharedObjs.getUser());
		login = login.replace("PASSWRD", SharedObjs.getPass());
		
		// Open the connection with request method = POST
		URL url = new URL(BASE_LOGIN_LINK);
		connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("POST");
		
		// For POST only - START
		// Use the URL connection for output
		connection.setDoOutput(true);
		out = connection.getOutputStream();
		out.write(login.getBytes());
		out.flush();
		out.close();
		// For POST only - END
		
		int responseCode = connection.getResponseCode();
		Logger.log(Logger.TAG_BUG2GODOWNLOADER, "POST Response Code :: " + responseCode);
		
		if (responseCode == HttpURLConnection.HTTP_OK)
		{
			// If URL equals to https://b2gadm-mcloud101-blur.svcmot.com/bugreport/report/verify.action, it means that login failed.
			if (connection.getURL().toString().equals(BASE_LOGIN_LINK))
			{
				Logger.log(Logger.TAG_BUG2GODOWNLOADER, "Login Failed!");
				Logger.log(Logger.TAG_BUG2GODOWNLOADER, "URL after login POST: " + connection.getURL());
				return false;
			}
		}
		else
		{
			Logger.log(Logger.TAG_BUG2GODOWNLOADER, "POST request did not work");
			return false;
		}
		
		return true;
	}
	
	/**
	 * Monitor the downloads
	 */
	@Override
	public void run()
	{
		try
		{
			if (!login())
			{
				SharedObjs.crsManagerPane.addLogLine("Bug2Go login failed");
				return;
			}
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
		
		executor = Executors.newFixedThreadPool(5);
		SharedObjs.crsManagerPane.addLogLine("Downloading b2g files ...");
		
		while (!bug2goListSubmitted.isEmpty() || !bug2goListInProgress.isEmpty())
		{
			if (!bug2goListSubmitted.isEmpty())
			{
				try
				{
					semaphore.acquire();
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
				for (Bug2goItem b : bug2goListSubmitted)
				{
					executor.execute(b);
					bug2goListInProgress.add(b);
				}
				
				bug2goListSubmitted.clear();
				semaphore.release();
			}
			
			Logger.log(Logger.TAG_BUG2GODOWNLOADER, "Checking...");
			
			for (Bug2goItem b : bug2goListInProgress)
			{
				Logger.log(Logger.TAG_BUG2GODOWNLOADER, b.getBugId() + ": status > " + b.getStatus()
				                                        + " | size > " + b.getSizeOfFile()
				                                        + " | downloaded > " + b.getDownloadProgress()
				                                        + " | running > " + b.isRunning());
				
				if (b.getStatus() == Bug2goItem.DownloadStatus.FAILED && errors == 0)
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
		
		executor.shutdown();
		
		if (bug2goListInProgress.isEmpty())
		{
			Logger.log(Logger.TAG_BUG2GODOWNLOADER, "bug2goList In Progress is Empty.");
		}
		else
		{
			for (Bug2goItem i : bug2goListInProgress)
			{
				Logger.log(Logger.TAG_BUG2GODOWNLOADER, "bug2goList In Progress: " + i.getBugId());
			}
		}
		
		if (bug2goListDone.isEmpty())
		{
			Logger.log(Logger.TAG_BUG2GODOWNLOADER, "bug2goList Done is Empty.");
		}
		else
		{
			for (Bug2goItem i : bug2goListDone)
			{
				Logger.log(Logger.TAG_BUG2GODOWNLOADER, "bug2goList Done: " + i.getBugId());
			}
		}
		
		if (bug2goListFailed.isEmpty())
		{
			Logger.log(Logger.TAG_BUG2GODOWNLOADER, "bug2goList Failed is Empty.");
		}
		else
		{
			for (Bug2goItem i : bug2goListFailed)
			{
				Logger.log(Logger.TAG_BUG2GODOWNLOADER, "bug2goList Failed: " + i.getBugId());
			}
		}
		
		Logger.log(Logger.TAG_BUG2GODOWNLOADER, "Downloads finished");
		SharedObjs.crsManagerPane.addLogLine("Downloads finished");
		
		if (errors == 1)
			JOptionPane.showMessageDialog(SharedObjs.satFrame,
			                              "There were errors during the download. \nFiles may be missing or corrupted.");
		
		// Ask if user wants to unzip them all
		int ans = JOptionPane.showOptionDialog(SharedObjs.crsManagerPane,
		                                       "What do you want to do next?\n"
		                                                       + "Note: If you choose option 1 or 2, the SAT will also search for incomplete CRs\n"
		                                                       + "and close them on Jira!",
		                                       "Downloads completed", JOptionPane.YES_NO_OPTION,
		                                       JOptionPane.QUESTION_MESSAGE, null,
		                                       new Object[] {"Unzip/Build report",
		                                               "Just unzip them all",
		                                               "Nothing, I am ok"}, null);
		if (ans == 0)
		{
			File[] filesName = new File(SharedObjs.getDownloadPath()).listFiles();
			
			for (File file : filesName)
			{
				if (file.isFile() && file.getName().contains(".zip") && file.getName().contains("_B2G_"))
				{
					SharedObjs.crsManagerPane.addLogLine("Unzipping " + file.getName() + " ...");
					UnZip.unZipIt(file.getAbsolutePath(),
					              file.getAbsolutePath().substring(0, file.getAbsolutePath().length() - 28));
					
					file = new File(file.getAbsolutePath().substring(0, file.getAbsolutePath().length() - 28));
					SharedObjs.crsManagerPane.addLogLine("Unzipping done for " + file.getName());
					
					SharedObjs.crsManagerPane.addLogLine("Checking if CR is incomplete ...");
					CheckIfIncomplete incompleteChecker = new CheckIfIncomplete();
					if (incompleteChecker.checkIt(file.getAbsolutePath()).contains("Incomplete:"))
					{
						CrItem cr = SharedObjs.crsManagerPane.getCrsList().getCrByB2gId(file.getName());
						if (cr != null)
						{
							JiraSatApi jira = new JiraSatApi(JiraSatApi.DEFAULT_JIRA_URL,
							                                 SharedObjs.getUser(),
							                                 SharedObjs.getPass());
							
							jira.closeIssue(cr.getJiraID(), JiraSatApi.INCOMPLETE,
							                "The text logs are missing/incomplete. Could not perform a complete analysis.");
							
							SharedObjs.crsManagerPane.addLogLine("Closing CR " + cr.getJiraID()
							                                     + " as incomplete");
							
							Logger.log(Logger.TAG_BUG2GODOWNLOADER, "Done for " + file.getAbsolutePath()
							                                        + ". Closed as incomplete");
						}
						else
						{
							Logger.log(Logger.TAG_BUG2GODOWNLOADER,
							           "Done for "
							                           + file.getAbsolutePath()
							                           + ". It is incomplete but it could not be found\nin crs list. It stills opened on Jira.");
						}
					}
					else
					{
						SharedObjs.crsManagerPane.addLogLine("CR is OK");
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
					
					SharedObjs.crsManagerPane.addLogLine("All done for " + file.getName() + "\n");
				}
			}
			
			JOptionPane.showMessageDialog(SharedObjs.crsManagerPane,
			                              "All b2g files are unzipped and with report output generated.");
		}
		else if (ans == 1)
		{
			File[] filesName = new File(SharedObjs.getDownloadPath()).listFiles();
			
			for (File file : filesName)
			{
				if (file.isFile() && file.getName().contains(".zip") && file.getName().contains("_B2G_"))
				{
					UnZip.unZipIt(file.getAbsolutePath(),
					              file.getAbsolutePath().substring(0, file.getAbsolutePath().length() - 28));
					
					file = new File(file.getAbsolutePath().substring(0, file.getAbsolutePath().length() - 28));
					
					CheckIfIncomplete incompleteChecker = new CheckIfIncomplete();
					if (incompleteChecker.checkIt(file.getAbsolutePath()).contains("Incomplete:"))
					{
						CrItem cr = SharedObjs.crsManagerPane.getCrsList().getCrByB2gId(file.getName());
						if (cr != null)
						{
							JiraSatApi jira = new JiraSatApi(JiraSatApi.DEFAULT_JIRA_URL,
							                                 SharedObjs.getUser(),
							                                 SharedObjs.getPass());
							jira.closeIssue(cr.getJiraID(), JiraSatApi.INCOMPLETE,
							                "The text logs are missing/incomplete. Could not perform a complete analysis.");
							
							SharedObjs.crsManagerPane.addLogLine("Closing CR " + cr.getJiraID()
							                                     + " as incomplete");
							
							Logger.log(Logger.TAG_BUG2GODOWNLOADER, "Done for " + file.getAbsolutePath()
							                                        + ". Closed as incomplete");
						}
						else
						{
							Logger.log(Logger.TAG_BUG2GODOWNLOADER,
							           "Done for "
							                           + file.getAbsolutePath()
							                           + ". It is incomplete but it could not be found\nin crs list. It stills opened on Jira.");
						}
					}
					else
					{
						Logger.log(Logger.TAG_BUG2GODOWNLOADER, "Done for " + file.getAbsolutePath());
					}
					
					SharedObjs.crsManagerPane.addLogLine("All done for " + file.getName() + "\n");
				}
			}
			
			JOptionPane.showMessageDialog(SharedObjs.crsManagerPane, "All b2g files are unzipped.");
		}
		
		SharedObjs.crsManagerPane.getCrsList().clear();
	}
	
	/**
	 * Getters and Setters
	 */
	public Semaphore getSemaphore()
	{
		return semaphore;
	}
	
	public void setSemaphore(Semaphore semaphore)
	{
		this.semaphore = semaphore;
	}
	
	public boolean isOverwrite()
	{
		return overwrite;
	}
	
	public void setOverwrite(boolean overwrite)
	{
		this.overwrite = overwrite;
	}
}
