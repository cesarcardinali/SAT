package core;


import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.commons.io.FileUtils;

import main.SAT;
import objects.CrItem;
import objects.CustomFiltersList;
import objects.DBAdapter;
import panes.AdvancedOptionsPane;
import panes.CrsManagerPane;
import panes.CustomFiltersPane;
import panes.OptionsPane;
import panes.ParserPane;


/**
 * It contains all shared variables used by SAT
 */
public class SharedObjs
{
	/**
	 * Variables
	 */
	public static final String		  contentFolder	 = "Data/";
	public static final File		  sytemCfgFile	 = new File(contentFolder + "cfgs/system_cfg.xml");
	public static final File		  userCfgFile	 = new File(contentFolder + "cfgs/user_cfg.xml");
	public static final File		  messageCfgFile = new File(contentFolder + "cfgs/message.xml");
	public static final File		  filtersFile	 = new File(contentFolder + "cfgs/filters.xml");
	public static final File		  pwdFile		 = new File(contentFolder + "cfgs/pass.pwd");
	private static String			  crPath;
	private static String			  rootFolderPath;
	private static String			  downloadPath;
	private static String			  result;
	private static String			  user;
	private static String			  pass;
	public static String			  updateFolder1;
	public static String			  updateFolder2;
	private static ArrayList<CrItem>  crsList;
	private static Semaphore		  unzipSemaphore;
	private static CustomFiltersList  userFiltersList;
	private static CustomFiltersList  sharedFiltersList;
	private static CustomFiltersList  activeFiltersList;
	private static CustomFiltersPane  customFiltersPane;
	public static JTabbedPane		  tabbedPane;
	public static ParserPane		  parserPane;
	public static CrsManagerPane	  crsManagerPane;
	public static OptionsPane		  optionsPane;
	public static AdvancedOptionsPane advOptions;
	public static SAT				  satFrame;
	public static DBAdapter			  satDB;
	
	/**
	 * Initialize class variables
	 */
	public static void initClass()
	{
		// Set UI theme
		try
		{
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		}
		catch (ClassNotFoundException | InstantiationException | IllegalAccessException
			   | UnsupportedLookAndFeelException e)
		{
			e.printStackTrace();
		}
		
		// Initialize variables
		crPath = "";
		updateFolder1 = XmlMngr.getSystemValueOf(new String[] {"configs", "update_path1"});
		updateFolder2 = XmlMngr.getSystemValueOf(new String[] {"configs", "update_path2"});
		rootFolderPath = XmlMngr.getUserValueOf(new String[] {"parser_pane", "rootPath"});
		unzipSemaphore = new Semaphore(1, true);
		userFiltersList = new CustomFiltersList();
		sharedFiltersList = new CustomFiltersList();
		activeFiltersList = new CustomFiltersList();
		customFiltersPane = new CustomFiltersPane();
		crsList = new ArrayList<CrItem>();
		
		// Create Panes
		parserPane = new ParserPane();
		crsManagerPane = new CrsManagerPane();
		optionsPane = new OptionsPane();
		advOptions = new AdvancedOptionsPane();
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.addChangeListener(new ChangeListener()
		{
			@Override
			public void stateChanged(ChangeEvent e)
			{
			}
		});
		
		satFrame = new SAT();
		
		// Inserting tabs
		tabbedPane.addTab("<html><body leftmargin=15 topmargin=3 marginwidth=15 marginheight=5>Parser</body></html>",
						  parserPane);
		tabbedPane.addTab("<html><body leftmargin=15 topmargin=3 marginwidth=15 marginheight=5>CRs and Jira</body></html>",
						  crsManagerPane);
		tabbedPane.addTab("<html><body leftmargin=15 topmargin=3 marginwidth=15 marginheight=5>Options</body></html>",
						  optionsPane);
						  
		// Try to connect to DB
		try
		{
			satDB = new DBAdapter();
			optionsPane.setServerStatus(true);
		}
		catch (SQLException e1)
		{
			e1.printStackTrace();
			optionsPane.setServerStatus(false);
			Logger.log(Logger.TAG_SHAREDOBJS, "could not connect to SQL DB");
		}
	}
	
	/**
	 * Check if all folders exists. If any of them does not exist, create it.
	 */
	public void checkFolder()
	{
		// TODO
	}
	
	/**
	 * Getters and Setter
	 */
	// Getters:
	public static String getUser()
	{
		return user;
	}
	
	public static String getPass()
	{
		return pass;
	}
	
	public static String getCrPath()
	{
		return crPath;
	}
	
	public static String getResult()
	{
		return result;
	}
	
	public static String getDownloadPath()
	{
		return downloadPath;
	}
	
	public static CustomFiltersList getUserFiltersList()
	{
		return userFiltersList;
	}
	
	public static CustomFiltersList getSharedFiltersList()
	{
		return sharedFiltersList;
	}
	
	public static CustomFiltersList getActiveFiltersList()
	{
		return activeFiltersList;
	}
	
	public static CustomFiltersPane getCustomFiltersPane()
	{
		return customFiltersPane;
	}
	
	public ArrayList<CrItem> getCrsList()
	{
		return crsList;
	}
	
	public CrItem getCrByJira(String jiraID)
	{
		for (CrItem aux : crsList)
		{
			if (aux.getJiraID().equals(jiraID))
			{
				return aux;
			}
		}
		return null;
	}
	
	public CrItem getCrByB2g(String b2gID)
	{
		for (CrItem aux : crsList)
		{
			if (aux.getJiraID().equals(b2gID))
			{
				return aux;
			}
		}
		return null;
	}
	
	public static String getRootFolderPath()
	{
		return rootFolderPath;
	}
	
	// Setters:
	public static void setUser(String user)
	{
		SharedObjs.user = user;
	}
	
	public static void setPass(String pass)
	{
		SharedObjs.pass = pass;
	}
	
	public static void setResult(String result)
	{
		SharedObjs.result = result;
	}
	
	public static void setCrPath(String crPath)
	{
		SharedObjs.crPath = crPath;
	}
	
	public void setCrsList(ArrayList<CrItem> crsList)
	{
		SharedObjs.crsList = crsList;
	}
	
	public static void addCrToList(CrItem cr)
	{
		crsList.add(cr);
	}
	
	public void setCustomFiltersList(CustomFiltersList customFiltersList)
	{
		SharedObjs.userFiltersList = customFiltersList;
	}
	
	public static void setSharedFiltersList(CustomFiltersList sharedFiltersList)
	{
		SharedObjs.sharedFiltersList = sharedFiltersList;
	}
	
	public static void setActiveFiltersList(CustomFiltersList activeFiltersList)
	{
		SharedObjs.activeFiltersList = activeFiltersList;
	}
	
	public void setCustomFiltersPane(CustomFiltersPane customFiltersPane)
	{
		SharedObjs.customFiltersPane = customFiltersPane;
	}
	
	public static void setRootFolderPath(String rootFolderPath)
	{
		SharedObjs.rootFolderPath = rootFolderPath;
	}
	
	public static void setDownloadPath(String downloadPath)
	{
		SharedObjs.downloadPath = downloadPath;
	}
	
	// Static General Methods
	public static void copyScript(File source, File dest) throws IOException
	{
		FileUtils.copyFile(source, dest);
	}
	
	public static void acquireSemaphore() throws InterruptedException
	{
		unzipSemaphore.acquire();
	}
	
	public static void releaseSemaphore() throws InterruptedException
	{
		unzipSemaphore.release();
	}
	
}
