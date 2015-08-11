package core;

/**
 * This class is a keeper of all strings in the tool.
 * @author willian.marques
 *
 */
public class Strings 
{	
	/**
	 * System String Variables
	 */
	private static String toolName = getSystemString("tool_name");
	private static String toolVersion = getSystemString("tool_version");
	private static String toolFile = getSystemString("tool_file");
	private static String updaterFile = getSystemString("updater");
	private static String picsFolder = getSystemString("pics_folder");
	private static String logsFolder = getSystemString("logs_folder");
	private static String updateFolder1 = getSystemString("update_path1");
	private static String updateFolder2 = getSystemString("update_path2");
	private static String debugMode = getSystemString("debug_mode");
	
	/**
	 * Message Strings Variables
	 */
	private static String newVersion = getMessageString("new_version");
	
	
	/**
	 * Get Strings located in messages_cfg.xml
	 * @param name
	 * @return
	 */
	public static String getMessageString(String name)
	{
		
		String value = XmlMngr.getMessageValueOf(new String[] {"messages",name});
        return value;
	}
	
	/**
	 * Get Strings located in system_cfg.xml file
	 * @param name
	 * @return
	 */
	public static String getSystemString(String name)
	{
		
		String value = XmlMngr.getSystemValueOf(new String[]{"configs",name});
        return value;
	}

}
