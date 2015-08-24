package supportive;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import core.Logger;
import core.SharedObjs;


public class GetBug
{
	
	private static final String	BASE_LOGIN_LINK	   = "https://b2gadm-mcloud101-blur.svcmot.com/bugreport/report/verify.action";
	private static final String	BASE_DOWNLOAD_LINK = "https://b2gadm-mcloud101-blur.svcmot.com/bugreport/report/downloadlog.action";
	private static final String	LOGIN_PARAM		   = "username=COREID&password=PASSWRD";
	private static final String	BUG_ID_PARAM	   = "bg_id=BUGID";
	private HttpURLConnection	connection;
	private OutputStream		out;
	private List<String>		bugIdsToDownload;
	private boolean				overwrite;
	
	public GetBug(List<String> bugIds)
	{
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
		
		bugIdsToDownload = bugIds;
		
		overwrite = true;
	}
	
	public GetBug(List<String> bugIds, boolean replace)
	{
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
		
		bugIdsToDownload = bugIds;
		
		overwrite = replace;
	}
	
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
		Logger.log(Logger.TAG_GETBUG, "POST Response Code :: " + responseCode);
		
		if (responseCode == HttpURLConnection.HTTP_OK)
		{
			// If URL equals to https://b2gadm-mcloud101-blur.svcmot.com/bugreport/report/verify.action, it means that login failed.
			if (connection.getURL().toString()
						  .equals("https://b2gadm-mcloud101-blur.svcmot.com/bugreport/report/verify.action"))
			{
				Logger.log(Logger.TAG_GETBUG, "Login Failed!");
				Logger.log(Logger.TAG_GETBUG, "URL after login POST: " + connection.getURL());
				return false;
			}
		}
		else
		{
			Logger.log(Logger.TAG_GETBUG, "POST request did not work");
			return false;
		}
		
		return true;
	}
	
	private boolean downloadBug(String id) throws IOException
	{
		
		FileOutputStream file = null;
		
		String bugId;
		bugId = BUG_ID_PARAM.replace("BUGID", id);
		
		// Open connection to download the CR
		URL urlDownload = new URL(BASE_DOWNLOAD_LINK);
		connection = (HttpURLConnection) urlDownload.openConnection();
		
		// For POST only - START
		// Use the URL connection for output
		connection.setDoOutput(true);
		out = connection.getOutputStream();
		out.write(bugId.getBytes());
		out.flush();
		out.close();
		// For POST only - END
		
		int responseCode = connection.getResponseCode();
		Logger.log(Logger.TAG_GETBUG, "POST Response Code :: " + responseCode);
		
		if (responseCode == HttpURLConnection.HTTP_OK)
		{
			File downloadFolder;
			
			// Get the file name to be downloaded
			String fileName = connection.getHeaderField("Content-Disposition");
			fileName = fileName.replace("\"", "");
			fileName = fileName.substring(fileName.lastIndexOf("=") + 1);
			
			Logger.log(Logger.TAG_GETBUG, "SIZE OF FILE: " + String.valueOf(connection.getContentLength()));
			Logger.log(Logger.TAG_GETBUG, "File being downloaded: " + fileName);
			Logger.log(Logger.TAG_GETBUG, "File being saved in: " + SharedObjs.getDownloadPath());
			
			// Get download folder
			downloadFolder = new File(SharedObjs.getDownloadPath());
			
			// If user doesn't want to overwrite an existing file with the same name, abort download
			if (!overwrite)
			{
				for (File f : downloadFolder.listFiles())
				{
					if (f.getName().equals(fileName))
					{
						Logger.log(Logger.TAG_GETBUG, "File already exists. Aborting download.");
						return false;
					}
				}
			}
			
			// Creates a new file
			file = new FileOutputStream(SharedObjs.getDownloadPath() + "\\" + fileName);
			
			// Buffer
			byte[] buffer = new byte[4096];
			int len;
			InputStream reader = connection.getInputStream();
			
			// Getting the file from server
			while ((len = reader.read(buffer)) > 0)
			{
				file.write(buffer, 0, len);
			}
			reader.close();
		}
		else
		{
			Logger.log(Logger.TAG_GETBUG, "POST request did not work");
		}
		Logger.log(Logger.TAG_GETBUG, "URL after download POST: " + connection.getURL());
		return true;
		
	}
	
	public void getBug() throws IOException
	{
		if (login())
		{
			Logger.log(Logger.TAG_GETBUG, "Login on bug2go server successfull");
			for (String id : bugIdsToDownload)
				downloadBug(id);
		}
		else
		{
			Logger.log(Logger.TAG_GETBUG, "Login failed! Check your username and password.");
		}
		
	}
}
