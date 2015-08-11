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
	
	public static ArrayList<CrItem> crsList;
	public static Semaphore unzipSemaphore;

	public static CustomFiltersList customFiltersList;
	public static CustomFilters customFiltersPane;
	
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
	
	
	// Static General Methods
	public static void copyScript(File source, File dest)
			throws IOException {
		FileUtils.copyFile(source, dest);
	}
}
