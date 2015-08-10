package core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;

public class Logger {
	
	private static File logFile;
	private BufferedWriter logWriter;
	
	public Logger(){
		logFile = new File(SharedObjs.contentFolder + "/Logs/log_" + new Timestamp(System.currentTimeMillis()).toString().replace(":", "_") + ".log");
		try {
			logWriter = new BufferedWriter(new FileWriter(logFile));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
}
