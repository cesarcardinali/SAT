package core;

import java.io.File;

public class SharedObjs {
	
	/**
	 * Variables
	 */
	public static final String contentFolder = "Data/";
	public static final File sytemCfgFile = new File(contentFolder + "cfgs/system_cfg.xml");
	public static final File userCfgFile = new File(contentFolder + "cfgs/user_cfg.xml");
	public static final File pwdFile = new File(contentFolder + "cfgs/pass.pwd");
	public static String toolName;
	public static String toolVersion;
	public static String toolFile;
	public static String updaterFile;
	public static String picsFolder;
	public static String logsFolder;
	public static String updateFolder1;
	public static String updateFolder2;
	
	/**
	 * Initialize class variables
	 */
	public void initClass(){
		/*
				<tool_name>Search Analysis Tool</tool_name>
			    <tool_version>v2.0 alpha</tool_version>
			    <tool_file>BatteryTool.jar</tool_file>
			    <updater>Updater.jar</updater>
			    <pics_folder>/pics</pics_folder>
			    <logs_folder>/Logs</logs_folder>
			    <usr_config>/cfgs/user_cfg.xml</usr_config>
			    <pwd_config>/cfgs/pass.ini</pwd_config>
			    <update_path1>S:/Rio_Itu/SAT/</update_path1>
			    <update_path2>S:/Rio_Itu/Temporário/Cesar/</update_path2>
			    <debug_mode>false</debug_mode>
		 */
		toolFile = XmlMngr.getSystemValueOf(new String[]{"configs","tool_file"});
		toolName = XmlMngr.getSystemValueOf(new String[]{"configs","tool_name"});
		toolVersion = XmlMngr.getSystemValueOf(new String[]{"configs","updater"});
		updaterFile = XmlMngr.getSystemValueOf(new String[]{"configs","tool_file"});
		updateFolder1 = XmlMngr.getSystemValueOf(new String[]{"configs","update_path1"});
		updateFolder2 = XmlMngr.getSystemValueOf(new String[]{"configs","update_path2"});
	}
	
	/**
	 * Check if all folders exists.
	 * If any of them does not exist, create it.
	 */
	public void checkFolder(){
		
	}
}
