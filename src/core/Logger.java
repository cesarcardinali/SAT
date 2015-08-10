package core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;

/**
 * @author cesarc
 *
 */
/**
 * @author cesarc
 *
 */
public class Logger {
	
	/**
	 * Variables
	 */
	private static File logFile;
	private static BufferedWriter logWriter;
	private static boolean logCreated;
	
	public static final String TAG_SAT = "SAT";
	public static final String TAG_PARSER = "PARSER";
	public static final String TAG_CRSMANAGER = "CRS MANAGER";
	public static final String TAG_OPTIONS = "OPTIONS";
	
	/**
	 * Initialize class variables
	 */
	public static void initClass(){
		// Generate log file
		if (new File(SharedObjs.logsFolder).exists())
		{
			logFile = new File(SharedObjs.logsFolder + "log_"
					+ new Timestamp(System.currentTimeMillis()).toString().replace(":", "_")
					+ ".log");
			System.out.println("Logs folder exists");
		} 
		else
		{
			new File(SharedObjs.logsFolder).mkdirs();
			logFile = new File(SharedObjs.logsFolder + "log_"
					+ new Timestamp(System.currentTimeMillis()).toString().replace(":", "_")
					+ ".log");
			System.out.println("Logs folder created");
		}
		
		// Start log writer
		try
		{
			logCreated = true;
			logWriter = new BufferedWriter(new FileWriter(logFile));
		} 
		catch (IOException e1)
		{
			logCreated = false;
			System.out.println("Log file could not be created");
			e1.printStackTrace();
		}
	}
	
	/**
	 * Write a line in log file
	 * if log file exists.
	 * @param tag String containing the tag for the logged text
	 * @param text String containing the text to be logged
	 */
	public static void log(String tag, String text){
		if(logCreated){
			try {
				logWriter.write(tag + ": " + text);
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Log file does not exist");
			}
		}
	}
	
	// Getters and Setters
	public boolean isLogCreated(){
		return logCreated;
	}

	
	/**
	 * Closes the log file.
	 */
	public static void close() {
		if(logCreated){
			try {
				logWriter.write("LOGGER: Closing file");
				logWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Log file does not exist");
			}
		}
	}
	
}
