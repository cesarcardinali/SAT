package core;


import java.io.File;
import java.io.IOException;
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
import panes.AdvancedOptionsPane;
import panes.CrsManager;
import panes.CustomFilters;
import panes.ParserPane;
import panes.OptionsPane;


public class SharedObjs {
	
	/**
	 * Variables
	 */
	public static final String contentFolder = "Data/";
	public static final File sytemCfgFile   = new File(contentFolder + "cfgs/system_cfg.xml");
	public static final File userCfgFile    = new File(contentFolder + "cfgs/user_cfg.xml");
	public static final File messageCfgFile = new File(contentFolder + "cfgs/message.xml");
	public static final File pwdFile = new File(contentFolder + "cfgs/pass.pwd");
	private static String crPath;
	
	public static String toolName;
	public static String toolVersion;
	public static String toolFile;
	public static String updaterFile;
	public static String picsFolder;
	public static String logsFolder;
	public static String updateFolder1;
	public static String updateFolder2;
	
	
	private static ArrayList<CrItem> crsList;
	private static Semaphore unzipSemaphore;

	private static CustomFiltersList customFiltersList;
	private static CustomFilters customFiltersPane;
	
	public static JTabbedPane tabbedPane;
	public static ParserPane parserPane;
	public static CrsManager crsManagerPane;
	public static OptionsPane optionsPane;
	public static AdvancedOptionsPane advOptions;
	public static SAT satFrame;
	
	
	/**
	 * Initialize class variables
	 */
	public static void initClass(){
		// Initialize variables
		toolName = XmlMngr.getSystemValueOf(new String[]{"configs","tool_name"});
		toolVersion = XmlMngr.getSystemValueOf(new String[]{"configs","tool_version"});
		toolFile = XmlMngr.getSystemValueOf(new String[]{"configs","tool_file"});
		updaterFile = XmlMngr.getSystemValueOf(new String[]{"configs","updater"});
		picsFolder = contentFolder + XmlMngr.getSystemValueOf(new String[]{"configs","pics_folder"});
		logsFolder = contentFolder + XmlMngr.getSystemValueOf(new String[]{"configs","logs_folder"});
		updateFolder1 = XmlMngr.getSystemValueOf(new String[]{"configs","update_path1"});
		updateFolder2 = XmlMngr.getSystemValueOf(new String[]{"configs","update_path2"});
		crPath = XmlMngr.getUserValueOf(new String[] {"parser_pane" , "path"});
		
		unzipSemaphore = new Semaphore(1, true);
		customFiltersList = new CustomFiltersList();
		customFiltersPane = new CustomFilters();
		customFiltersPane.loadFilters();
		crsList = new ArrayList<CrItem>();
		
		//Set UI theme
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}	
		
		// Create Panes
		parserPane = new ParserPane();
		crsManagerPane = new CrsManager();
		optionsPane = new OptionsPane();
		advOptions = new AdvancedOptionsPane();
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if(tabbedPane.getSelectedIndex() == 1)
					crsManagerPane.updateAllDataUI();
			}
		});
		satFrame = new SAT();
		
		//Inserting tabs
		tabbedPane.addTab("<html><body leftmargin=15 topmargin=3 marginwidth=15 marginheight=5>Parser</body></html>", parserPane);
		tabbedPane.addTab("<html><body leftmargin=15 topmargin=3 marginwidth=15 marginheight=5>CRs and Jira</body></html>", crsManagerPane);
		tabbedPane.addTab("<html><body leftmargin=15 topmargin=3 marginwidth=15 marginheight=5>Options</body></html>", optionsPane);
	}
	
	/**
	 * Check if all folders exists.
	 * If any of them does not exist, create it.
	 */
	public void checkFolder(){
		
	}
	
	
	
	
	
	/**
	 * Getters and Setters:
	 */
	public static String getCrPath() {
		return crPath;
	}

	public static void setCrPath(String crPath) {
		SharedObjs.crPath = crPath;
	}
	
	public ArrayList<CrItem> getCrsList() {
		return crsList;
	}

	public void setCrsList(ArrayList<CrItem> crsList) {
		SharedObjs.crsList = crsList;
	}
	
	public Semaphore getUnzipSemaphore() {
		return unzipSemaphore;
	}
	
	public CustomFiltersList getCustomFiltersList() {
		return customFiltersList;
	}
	
	public CustomFilters getCustomFiltersPane() {
		return customFiltersPane;
	}
	
	public void setCustomFiltersList(CustomFiltersList customFiltersList) {
		SharedObjs.customFiltersList = customFiltersList;
	}
	
	public void setCustomFiltersPane(CustomFilters customFiltersPane) {
		SharedObjs.customFiltersPane = customFiltersPane;
	}
	
	// Static General Methods
	public static void copyScript(File source, File dest)
			throws IOException {
		FileUtils.copyFile(source, dest);
	}
	
	public CrItem getCrByJira(String jiraID){
		for(CrItem aux : crsList){
			if(aux.getJiraID().equals(jiraID)){
				return aux;
			}
		}
		return null;
	}
	
	public CrItem getCrByB2g(String b2gID){
		for(CrItem aux : crsList){
			if(aux.getJiraID().equals(b2gID)){
				return aux;
			}
		}
		return null;
	}
}
