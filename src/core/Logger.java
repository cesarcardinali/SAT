package core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;

public class Logger {
	
	/**
	 * Variables
	 */
	private static File logFile;
	private BufferedWriter logWriter;
	private boolean logCreated;
	public static final String TAG_SAT = "SAT";
	public static final String TAG_PARSER = "PARSER";
	public static final String TAG_CRSMANAGER = "CRS MANAGER";
	public static final String TAG_OPTIONS = "OPTIONS";
	
	/**
	 * Initialize class variables
	 */
	public void initClass(){
		logFile = new File(SharedObjs.contentFolder + "logs/log_"
						+ new Timestamp(System.currentTimeMillis()).toString().replace(":", "_")
						+ ".log");
		try {
			logCreated = true;
			logWriter = new BufferedWriter(new FileWriter(logFile));
		} catch (IOException e1) {
			logCreated = false;
			e1.printStackTrace();
		}
	}
	
	/**
	 * Write a line in log file
	 * if log file exists.
	 */
	public void log(String tag, String text){
		if(logCreated){
			try {
				logWriter.write(tag + ": " + text);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	// Getters and Setters
	public boolean isLogCreated(){
		return logCreated;
	}
	
}
