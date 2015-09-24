package supportive.preanalyzers.btdparser;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


@SuppressWarnings("resource")
public class BtdParser
{
	private Connection    c;
	private Statement     stmt;
	private ResultSet     rs;
	private String        path;
	private int           status;
	private BtdRow        btdRow;
	private BtdRowsList   btdRows;
	private BtdState      finalState;
	private BtdStatesData statesData;
	private long[]        screenData;     // 0- dark, 1- dim, 2- medium, 3- light, 4- bright
	private long[]        signalData;     // 0- none, 1- poor, 2- moderate, 3- good, 4- great
	private float[]       cpuTempData;    // 0- min, 1- max, 2- avg
	private float[]       deviceTempData; // 0- min, 1- max, 2- avg
	private int           batCap; // Battery capacity
	private int           bttDischarged[]; // Battery discharge from, to.
	private long          cellTX;         // Total cell data sent
	private long          cellRX;         // Total cell data received
	private long          wifiTX;         // Total wifi data sent
	private long          wifiRX;         // Total wifi data received
	private long          gpsLocation;    // Total GPS location count
	private long          networkLocation; // Total GPS location count
	private long          consumeOn  = 0;
	private long          consumeOff = 0;
	private long          timeOff    = 0;
	private long          timeOn     = 0;
	private long          wifiOnTime;
	private long          wifiRunningTime;
	private long          realTimeOnBatt;
	private long          awakeTimeOnBatt;
	private long          phoneCall;
	private long          tetheringTime;
	
	public BtdParser(String path)
	{
		this.path = path;
		System.out.println("CR path: " + path);
		
		try
		{
			// File seek and load configuration
			String file_report = "";
			File folder = new File(path);
			File[] listOfFiles = folder.listFiles();
			
			if (folder.isDirectory())
			{
				// Look for the file
				for (int i = 0; i < listOfFiles.length; i++)
				{
					// Logger.log(Logger.TAG_DIAG, folder.listFiles()[i]);
					if (listOfFiles[i].isFile())
					{
						String file = listOfFiles[i].getName();
						if (file.endsWith(".btd") && listOfFiles[i].length() > 5000000)
						{
							file_report = "/" + file;
						}
					}
				}
			}
			
			if (!file_report.equals(""))
			{
				System.out.println("Using file: " + file_report);
				Class.forName("org.sqlite.JDBC");
				c = DriverManager.getConnection("jdbc:sqlite:" + path + file_report);
				stmt = c.createStatement();
				
				statesData = new BtdStatesData();
				btdRows = null;
				
				status = 1;
				
				System.out.println("Opened database successfully");
			}
			else
			{
				status = 0;
				System.out.println("Failed to open DB\nPath: " + file_report);
				System.out.println("BTD file is too short or does not exists");
			}
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	public boolean parse()
	{
		if (status == 1)
		{
			bttDischarged = new int[2];
			
			// Get charge and discharge periods
			getPeriods();
			
			// Get the longer discharge period
			finalState = getLongerDischargingPeriod();
			
			getDischargeInternetData(finalState);
			
			getDischargeGeneralData(finalState);
			
			getDischargeBatteryData(finalState);
			
			getDischargePhoneSignalData(finalState);
			
			getDischargeScreenBrightData(finalState);
			
			getDischargeTemperatureData(finalState);
			
			getTetheringTime();
			
			// Show acquired data
			//showParseResults();
			
			// Print acquired data
			try
			{
				printResultToFile();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			
			return true;
		}
		else
		{
			System.out.println("Was not possible to find BTD file or the existent is too short");
			return false;
		}
	}
	
	public void checkForIssuesIndications()
	{
		
	}
	
	public boolean checkForTethering()
	{
		System.out.println("Checking for tethering resolution:\nTotal on battery time: " + realTimeOnBatt
		                   + "\nTotal tethering time: " + tetheringTime + "\nProportion: "
		                   + (100.0 * tetheringTime / realTimeOnBatt) + "%");
		if (tetheringTime >= realTimeOnBatt * 0.10)
			return true;
		else
			return false;
	}
	
	private long getTetheringTime()
	{
		long lastCTX, actualCTX, lastWRX, actualWRX;
		long lastTime, actualTime, cumulativeTime = 0;
		
		try
		{
			rs = execQuery("select CELL_TX, WIFI_RX, timestamp from t_fgdata where timestamp BETWEEN "
			               + finalState.getStart() + " AND " + finalState.getEnd()
			               + " AND WIFI_LABEL = ''  AND CELL_LABEL != '';");
			
			lastCTX = rs.getLong(1);
			lastWRX = rs.getLong(2);
			lastTime = rs.getLong(3);
			//int i =0;
			
			while (rs.next())
			{
				//i++;
				actualCTX = rs.getLong(1);
				actualWRX = rs.getLong(2);
				actualTime = rs.getLong(3);
				int timeCorrection = (int) ((actualTime - lastTime) / 10000);
				
				 //System.out.println(i + " - " + lastTime);
				 //System.out.println((actualCTX - lastCTX));
				 //System.out.println((actualWRX - lastWRX));
				 //System.out.println(timeCorrection);
				
				if ((actualCTX - lastCTX) > 10000 * timeCorrection
				    && (actualWRX - lastWRX) > 10000 * timeCorrection)
				{
					cumulativeTime = cumulativeTime + actualTime - lastTime;
					 //System.out.println("tethering: " + (actualTime - lastTime));
				}
				else
				{
					// System.out.println("not tethering");
				}
				
				 //System.out.println();
				
				lastCTX = actualCTX;
				lastWRX = actualWRX;
				lastTime = actualTime;
			}
			
			tetheringTime = cumulativeTime;
			
			return cumulativeTime;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return -1;
		}
	}
	
	public void showParseResults()
	{
		System.out.println("The longer discharge period is from "
		                   + finalState.getStart()
		                   + " ("
		                   + formatDate(finalState.getStartDate())
		                   + ") to "
		                   + finalState.getEnd()
		                   + " ("
		                   + formatDate(finalState.getEndDate())
		                   + ")\nA total time of "
		                   + dateDiff(finalState.getStartDate(), finalState.getEndDate())
		                   + " or "
		                   + getMillisFromBtdStringDate(dateDiff(finalState.getStartDate(),
		                                                         finalState.getEndDate())) + "ms\n");
		System.out.println("Cell Rx: " + cellRX + " KBytes  ||  Cell TX: " + cellTX + " KBytes");
		System.out.println("Wifi Rx: " + wifiRX + " KBytes  ||  Wifi TX: " + wifiTX + " KBytes");
		System.out.println("Btt: " + bttDischarged[0] + "% --> " + bttDischarged[1] + "%");
		System.out.println("GPS Location: " + gpsLocation);
		System.out.println("Network Location: " + networkLocation);
		System.out.println("Screen ON: " + getDateStringFromBtdStringMillis(timeOn) + " or " + timeOn + "ms");
		System.out.println("Screen OFF: " + getDateStringFromBtdStringMillis(timeOff) + " or " + timeOff
		                   + "ms");
		System.out.println("Total time: " + getDateStringFromBtdStringMillis(finalState.getDuration())
		                   + " or " + finalState.getDuration() + "ms");
		System.out.println("Phonecalls time: " + getDateStringFromBtdStringMillis(phoneCall) + " - "
		                   + phoneCall + "ms");
		System.out.println("Time on battery: " + getDateStringFromBtdStringMillis(realTimeOnBatt) + " - "
		                   + realTimeOnBatt + "ms");
		System.out.println("Time awake: " + getDateStringFromBtdStringMillis(awakeTimeOnBatt) + " - "
		                   + awakeTimeOnBatt + "ms");
		System.out.println("Wifi On time: " + getDateStringFromBtdStringMillis(wifiOnTime) + " - "
		                   + wifiOnTime + "ms");
		System.out.println("Wifi Running time: " + getDateStringFromBtdStringMillis(wifiRunningTime) + " - "
		                   + wifiRunningTime + "ms");
		
		System.out.println("Total discharge ms: " + (timeOn + timeOff));
		System.out.println("Total discharge hours: " + (millisToHours(timeOn) + millisToHours(timeOff)));
		System.out.println("Total discharge capacity: " + (consumeOn + consumeOff));
		
		System.out.print("Total On mAh: " + consumeOn + " - Total ms: " + timeOn);
		System.out.println(" - Average On mAh: " + getAverageconsumeOn() + " - " + millisToHours(timeOn));
		
		System.out.print("Total Off mAh: " + consumeOff + " - Total ms: " + timeOff);
		System.out.println(" - Average Off mAh: " + getAverageconsumeOff() + " - " + millisToHours(timeOff));
		
		System.out.println("Signal data:");
		System.out.println("\tnone: \t\t" + millisToHours(signalData[0]) + "\t"
		                   + getDateStringFromBtdStringMillis(signalData[0]));
		System.out.println("\tpoor: \t\t" + millisToHours(signalData[1]) + "\t"
		                   + getDateStringFromBtdStringMillis(signalData[1]));
		System.out.println("\tmoderate:\t" + millisToHours(signalData[2]) + "\t"
		                   + getDateStringFromBtdStringMillis(signalData[2]));
		System.out.println("\tgood: \t\t" + millisToHours(signalData[3]) + "\t"
		                   + getDateStringFromBtdStringMillis(signalData[3]));
		System.out.println("\tgreat: \t\t" + millisToHours(signalData[4]) + "\t"
		                   + getDateStringFromBtdStringMillis(signalData[4]));
		
		System.out.println("Screen brightnesses:");
		System.out.println("\tdark: \t\t" + millisToHours(screenData[0]) + "\t"
		                   + getDateStringFromBtdStringMillis(screenData[0]));
		System.out.println("\tdim: \t\t" + millisToHours(screenData[1]) + "\t"
		                   + getDateStringFromBtdStringMillis(screenData[1]));
		System.out.println("\tmedium:\t\t" + millisToHours(screenData[2]) + "\t"
		                   + getDateStringFromBtdStringMillis(screenData[2]));
		System.out.println("\tlight: \t\t" + millisToHours(screenData[3]) + "\t"
		                   + getDateStringFromBtdStringMillis(screenData[3]));
		System.out.println("\tbright:\t\t" + millisToHours(screenData[4]) + "\t"
		                   + getDateStringFromBtdStringMillis(screenData[4]));
		
		// Specific detections
		System.out.println("Tethering time: " + tetheringTime + " "
		                   + getDateStringFromBtdStringMillis(tetheringTime) + "\n");
	}
	
	public void printResultToFile() throws IOException
	{
		File output = new File(path + "/BTD output.txt");
		BufferedWriter br = new BufferedWriter(new FileWriter(output));
		
		br.write("The longer discharge period is from " + finalState.getStart() + " ("
		         + formatDate(finalState.getStartDate()) + ") to " + finalState.getEnd() + " ("
		         + formatDate(finalState.getEndDate()) + ")\nA total time of "
		         + dateDiff(finalState.getStartDate(), finalState.getEndDate()) + " or "
		         + getMillisFromBtdStringDate(dateDiff(finalState.getStartDate(), finalState.getEndDate()))
		         + "ms\n" + "\n");
		br.write("Cell Rx: " + cellRX + " KBytes  ||  Cell TX: " + cellTX + " KBytes" + "\n");
		br.write("Wifi Rx: " + wifiRX + " KBytes  ||  Wifi TX: " + wifiTX + " KBytes" + "\n");
		br.write("Btt: " + bttDischarged[0] + "% --> " + bttDischarged[1] + "%" + "\n");
		br.write("GPS Location: " + gpsLocation + "\n");
		br.write("Network Location: " + networkLocation + "\n");
		br.write("Screen ON: " + getDateStringFromBtdStringMillis(timeOn) + " or " + timeOn + "ms" + "\n");
		br.write("Screen OFF: " + getDateStringFromBtdStringMillis(timeOff) + " or " + timeOff + "ms" + "\n");
		br.write("Total time: " + getDateStringFromBtdStringMillis(finalState.getDuration()) + " or "
		         + finalState.getDuration() + "ms" + "\n");
		br.write("Phonecalls time: " + getDateStringFromBtdStringMillis(phoneCall) + " - " + phoneCall + "ms" + "\n");
		br.write("Time on battery: " + getDateStringFromBtdStringMillis(realTimeOnBatt) + " - "
		         + realTimeOnBatt + "ms" + "\n");
		br.write("Time awake: " + getDateStringFromBtdStringMillis(awakeTimeOnBatt) + " - " + awakeTimeOnBatt
		         + "ms" + "\n");
		br.write("Wifi On time: " + getDateStringFromBtdStringMillis(wifiOnTime) + " - " + wifiOnTime + "ms" + "\n");
		br.write("Wifi Running time: " + getDateStringFromBtdStringMillis(wifiRunningTime) + " - "
		         + wifiRunningTime + "ms" + "\n");
		
		br.write("Total discharge ms: " + (timeOn + timeOff) + "\n");
		br.write("Total discharge hours: " + (millisToHours(timeOn) + millisToHours(timeOff)) + "\n");
		br.write("Total discharge capacity: " + (consumeOn + consumeOff) + "\n");
		
		br.write("Total On mAh: " + consumeOn + " - Total ms: " + timeOn + "\n");
		br.write("Average On mAh: " + getAverageconsumeOn() + " - " + millisToHours(timeOn) + "\n");
		
		br.write("Total Off mAh: " + consumeOff + " - Total ms: " + timeOff + "\n");
		br.write("Average Off mAh: " + getAverageconsumeOff() + " - " + millisToHours(timeOff) + "\n");
		
		br.write("Signal data:" + "\n");
		br.write("\tnone: \t\t" + millisToHours(signalData[0]) + "\t"
		         + getDateStringFromBtdStringMillis(signalData[0]) + "\n");
		br.write("\tpoor: \t\t" + millisToHours(signalData[1]) + "\t"
		         + getDateStringFromBtdStringMillis(signalData[1]) + "\n");
		br.write("\tmoderate:\t" + millisToHours(signalData[2]) + "\t"
		         + getDateStringFromBtdStringMillis(signalData[2]) + "\n");
		br.write("\tgood: \t\t" + millisToHours(signalData[3]) + "\t"
		         + getDateStringFromBtdStringMillis(signalData[3]) + "\n");
		br.write("\tgreat: \t\t" + millisToHours(signalData[4]) + "\t"
		         + getDateStringFromBtdStringMillis(signalData[4]) + "\n");
		
		br.write("Screen brightnesses:" + "\n");
		br.write("\tdark: \t\t" + millisToHours(screenData[0]) + "\t"
		         + getDateStringFromBtdStringMillis(screenData[0]) + "\n");
		br.write("\tdim: \t\t" + millisToHours(screenData[1]) + "\t"
		         + getDateStringFromBtdStringMillis(screenData[1]) + "\n");
		br.write("\tmedium:\t\t" + millisToHours(screenData[2]) + "\t"
		         + getDateStringFromBtdStringMillis(screenData[2]) + "\n");
		br.write("\tlight: \t\t" + millisToHours(screenData[3]) + "\t"
		         + getDateStringFromBtdStringMillis(screenData[3]) + "\n");
		br.write("\tbright:\t\t" + millisToHours(screenData[4]) + "\t"
		         + getDateStringFromBtdStringMillis(screenData[4]) + "\n");
		
		// Specific detections
		br.write("Tethering time: " + tetheringTime + " " + getDateStringFromBtdStringMillis(tetheringTime) + "\n");
		
		br.close();
	}
	
	private boolean getDischargeBatteryData(BtdState finalState)
	{
		int lastRM, actualRM;
		long lastTime, actualTime;
		long lastScreenStatus;
		
		try
		{
			// Get all BTD data -------------------------------------
			rs = stmt.executeQuery("SELECT RM, timestamp, ScreenOn FROM t_fgdata where timestamp BETWEEN "
			                       + finalState.getStart() + " AND " + finalState.getEnd() + ";");
			
			lastRM = rs.getInt(1);
			lastTime = rs.getLong(2);
			lastScreenStatus = rs.getInt(3);
			
			while (rs.next())
			{
				actualRM = rs.getInt(1);
				actualTime = rs.getLong(2);
				
				if (lastScreenStatus == 0) // If screen is Off
				{
					consumeOn = consumeOn + (lastRM - actualRM);
					timeOn = timeOn + (actualTime - lastTime);
					lastRM = actualRM;
				}
				else
				{
					consumeOff = consumeOff + (lastRM - actualRM);
					timeOff = timeOff + (actualTime - lastTime);
					lastRM = actualRM;
				}
				
				lastRM = actualRM;
				lastTime = actualTime;
				lastScreenStatus = rs.getInt(3);
			}
			
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	private void getDischargeInternetData(BtdState finalState)
	{
		String[][] results = getMinMaxDiffData(new String[] {
		        "CELL_TX",
		        "CELL_RX",
		        "WIFI_TX",
		        "WIFI_RX",
		        "GpsLocCount",
		        "NetworkLocCount"}, new long[] {finalState.getStart(), finalState.getEnd()});
		
		// RX/TX data in BTD is swapped, so, we need to swap them here
		cellRX = Long.parseLong(results[0][2]) / 1024; // divided by 1024 to change from Bytes to KBytes
		cellTX = Long.parseLong(results[1][2]) / 1024;
		wifiTX = Long.parseLong(results[2][2]) / 1024;
		wifiRX = Long.parseLong(results[3][2]) / 1024;
		gpsLocation = Long.parseLong(results[4][2]);
		networkLocation = Long.parseLong(results[5][2]);
	}
	
	private void getDischargeGeneralData(BtdState finalState)
	{
		String[][] results = getTopBottomData(new String[] {
		        "BATTERY_LEVEL",
		        "ActivePhoneCallTime",
		        "RealTimeOnBatt",
		        "AwakeTimeOnBatt",
		        "WifiOnTime",
		        "WifiRunningTime",
		        "FCC"}, new long[] {finalState.getStart(), finalState.getEnd()});
		
		bttDischarged[0] = Integer.parseInt(results[0][0]);
		bttDischarged[1] = Integer.parseInt(results[0][1]);
		phoneCall = getMillisFromBtdStringDate(results[1][1]) - getMillisFromBtdStringDate(results[1][0]);
		realTimeOnBatt = getMillisFromBtdStringDate(results[2][1])
		                 - getMillisFromBtdStringDate(results[2][0]);
		awakeTimeOnBatt = getMillisFromBtdStringDate(results[3][1])
		                  - getMillisFromBtdStringDate(results[3][0]);
		wifiOnTime = getMillisFromBtdStringDate(results[4][1]) - getMillisFromBtdStringDate(results[4][0]);
		wifiRunningTime = getMillisFromBtdStringDate(results[5][1])
		                  - getMillisFromBtdStringDate(results[5][0]);
		batCap = Integer.parseInt(results[6][0]);
	}
	
	private void getDischargePhoneSignalData(BtdState finalState)
	{
		String[][] results = getTopBottomData(new String[] {"SignalLevels"},
		                                      new long[] {finalState.getStart(), finalState.getEnd()});
		
		String[] intialSignalParts = results[0][0].split(", ");
		String[] finalSignalParts = results[0][1].split(", ");
		
		signalData = new long[5];
		
		for (String s : finalSignalParts)
		{
			String[] data = s.split(" ");
			switch (data[0])
			{
				case "none":
					signalData[0] = getMillisFromBtdStringDate(data[1]);
					break;
				case "poor":
					signalData[1] = getMillisFromBtdStringDate(data[1]);
					break;
				case "moderate":
					signalData[2] = getMillisFromBtdStringDate(data[1]);
					break;
				case "good":
					signalData[3] = getMillisFromBtdStringDate(data[1]);
					break;
				case "great":
					signalData[4] = getMillisFromBtdStringDate(data[1]);
					break;
				default:
					break;
			}
		}
		
		for (String s : intialSignalParts)
		{
			String[] data = s.split(" ");
			switch (data[0])
			{
				case "none":
					signalData[0] = signalData[0] - getMillisFromBtdStringDate(data[1]);
					break;
				case "poor":
					signalData[1] = signalData[1] - getMillisFromBtdStringDate(data[1]);
					break;
				case "moderate":
					signalData[2] = signalData[2] - getMillisFromBtdStringDate(data[1]);
					break;
				case "good":
					signalData[3] = signalData[3] - getMillisFromBtdStringDate(data[1]);
					break;
				case "great":
					signalData[4] = signalData[4] - getMillisFromBtdStringDate(data[1]);
					break;
				default:
					break;
			}
		}
		/*
		 * for (long d : signalData) { System.out.println(millisToHours(d)); }
		 */
	}
	
	private void getDischargeScreenBrightData(BtdState finalState)
	{
		String[][] results = getTopBottomData(new String[] {"ScreenBrightnesses"},
		                                      new long[] {finalState.getStart(), finalState.getEnd()});
		
		// dark 6s,472ms (0,9%), dim 11m,27s,264ms (99,1%)
		String[] intialScreenParts = results[0][0].split(", ");
		String[] finalScreenParts = results[0][1].split(", ");
		
		screenData = new long[5];
		
		for (String s : finalScreenParts)
		{
			String[] data = s.split(" ");
			switch (data[0])
			{
				case "dark":
					screenData[0] = getMillisFromBtdStringDate(data[1]);
					break;
				case "dim":
					screenData[1] = getMillisFromBtdStringDate(data[1]);
					break;
				case "medium":
					screenData[2] = getMillisFromBtdStringDate(data[1]);
					break;
				case "light":
					screenData[3] = getMillisFromBtdStringDate(data[1]);
					break;
				case "bright":
					screenData[4] = getMillisFromBtdStringDate(data[1]);
					break;
				default:
					break;
			}
		}
		
		for (String s : intialScreenParts)
		{
			String[] data = s.split(" ");
			switch (data[0])
			{
				case "none":
					screenData[0] = screenData[0] - getMillisFromBtdStringDate(data[1]);
					break;
				case "poor":
					screenData[1] = screenData[1] - getMillisFromBtdStringDate(data[1]);
					break;
				case "moderate":
					screenData[2] = screenData[2] - getMillisFromBtdStringDate(data[1]);
					break;
				case "good":
					screenData[3] = screenData[3] - getMillisFromBtdStringDate(data[1]);
					break;
				case "great":
					screenData[4] = screenData[4] - getMillisFromBtdStringDate(data[1]);
					break;
				default:
					break;
			}
		}
		/*
		 * for (long d : screenData) { System.out.println(millisToHours(d)); }
		 */
	}
	
	private void getDischargeTemperatureData(BtdState finalState)
	{
		try
		{
			rs = stmt.executeQuery("SELECT MAX(TEMP), MIN(TEMP), AVG(TEMP), MAX(TEMP_1), MIN(TEMP_1), AVG(TEMP_1), MAX(TEMP_2), MIN(TEMP_2), AVG(TEMP_2) FROM t_fgdata where timestamp BETWEEN "
			                       + finalState.getStart() + " AND " + finalState.getEnd() + ";");
			
			cpuTempData = new float[3];
			deviceTempData = new float[3];
			
			cpuTempData[0] = rs.getFloat(2);
			cpuTempData[1] = rs.getFloat(1);
			cpuTempData[2] = rs.getFloat(3);
			
			deviceTempData[0] = rs.getFloat(5);
			deviceTempData[1] = rs.getFloat(4);
			deviceTempData[2] = rs.getFloat(6);
			/*
			 * for (float f : cpuTempData) { System.out.println(f); }
			 * 
			 * System.out.println();
			 * 
			 * for (float f : cpuTempData) { System.out.println(f); }
			 */
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	public long getMillisFromBtdStringDate(String time)
	{
		int[] parts = timeParser(time);
		long millis = parts[0];
		
		try
		{
			millis = millis + parts[1] * 1000;
			millis = millis + parts[2] * 1000 * 60;
			millis = millis + parts[3] * 1000 * 60 * 60;
			millis = millis + parts[4] * 1000 * 60 * 60 * 24;
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
			// e.printStackTrace();
		}
		
		return millis;
	}
	
	public BtdRowsList getDischargeBtdData(long timezone)
	{
		btdRows = new BtdRowsList();
		
		try
		{
			// Get all BTD data -------------------------------------
			rs = stmt.executeQuery("SELECT rowid, * FROM t_fgdata WHERE t_fgdata.BATTERY_STATUS = 3 ORDER BY rowid DESC;");
			
			btdRows = new BtdRowsList();
			
			while (rs.next())
			{
				btdRow = new BtdRow();
				setupBtdRow(btdRow, rs, timezone);
				btdRows.add(btdRow);
			}
			
			// rs.close();
			// stmt.close();
		}
		catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		
		return btdRows;
	}
	
	public BtdRowsList getDischargeBtdData()
	{
		btdRows = new BtdRowsList();
		
		try
		{
			// Get all BTD data -------------------------------------
			rs = stmt.executeQuery("SELECT rowid, * FROM t_fgdata WHERE t_fgdata.BATTERY_STATUS = 3 ORDER BY rowid DESC;");
			
			btdRows = new BtdRowsList();
			
			while (rs.next())
			{
				btdRow = new BtdRow();
				setupBtdRow(btdRow, rs);
				btdRows.add(btdRow);
			}
			
			// rs.close();
			// stmt.close();
		}
		catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		
		return btdRows;
	}
	
	public String[][] getMinMaxDiffData(String[] minsMaxs, long[] fromToTimestamp)
	{
		String minMaxResults[][] = new String[minsMaxs.length + 1][3];
		int id = 0;
		
		try
		{
			for (String field : minsMaxs)
			{
				if (fromToTimestamp != null)
					rs = stmt.executeQuery("SELECT MIN(" + field + "), MAX(" + field + "), (MAX(" + field
					                       + ") - MIN(" + field
					                       + ")) as Diff FROM t_fgdata WHERE timestamp BETWEEN "
					                       + fromToTimestamp[0] + " AND " + fromToTimestamp[1] + ";");
				else
					rs = stmt.executeQuery("SELECT MIN(" + field + "), MAX(" + field + ") FROM t_fgdata;");
				
				while (rs.next())
				{
					/*
					 * System.out.println("Campo: " + field); System.out.println("Min= " + rs.getString(1)); System.out.println("Max= " +
					 * rs.getString(2));
					 */
					
					minMaxResults[id][0] = rs.getString(1);
					minMaxResults[id][1] = rs.getString(2);
					minMaxResults[id][2] = rs.getString(3);
				}
				// System.out.println();
				
				id++;
			}
			
			// rs.close();
			// stmt.close();
			return minMaxResults;
		}
		catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			return null;
		}
	}
	
	public String[][] getTopBottomData(String[] topBottoms, long[] fromToTimestamp)
	{
		String topBottomResults[][] = new String[topBottoms.length + 1][2];
		int id = 0;
		
		try
		{
			for (String field : topBottoms)
			{
				
				rs = stmt.executeQuery("SELECT " + field + " FROM t_fgdata WHERE timestamp = "
				                       + fromToTimestamp[0] + " OR timestamp = " + fromToTimestamp[1] + ";");
				
				// System.out.println("Campo: " + field);
				
				rs.next();
				// System.out.println("Top= " + rs.getString(1));
				topBottomResults[id][0] = rs.getString(1);
				
				rs.next();
				// System.out.println("Bottom= " + rs.getString(1));
				topBottomResults[id][1] = rs.getString(1);
				
				// System.out.println();
				
				id++;
			}
			
			// rs.close();
			// stmt.close();
			return topBottomResults;
		}
		catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			return null;
		}
	}
	
	public String dateDiff(Date dateStart, Date dateEnd)
	{
		long timestampDiff = dateEnd.getTime() - dateStart.getTime();
		
		return getDateStringFromBtdStringMillis(timestampDiff);
	}
	
	public String timeDiff(long dateStart, long dateEnd)
	{
		long timestampDiff = dateEnd - dateStart;
		
		return getDateStringFromBtdStringMillis(timestampDiff);
	}
	
	public String getDateStringFromBtdStringMillis(long timestamp)
	{
		long days, hours, minutes, seconds, millis;
		
		millis = timestamp % 1000;
		seconds = (timestamp / 1000) % 60;
		minutes = (timestamp / (60*1000)) % 60;
		hours = (timestamp / (60*60*1000)) % 24;
		days = timestamp / (24*60*60*1000);
		
		return days + "d," + hours + "h," + minutes + "m," + seconds + "s," + millis + "ms";
	}
	
	public Date generateDate(long timestamp)
	{
		return new Date(timestamp);
	}
	
	public String formatDate(Date date)
	{
		SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm:ss");
		format.setTimeZone(TimeZone.getTimeZone(getTimeZoneName()));
		return format.format(date);
	}
	
	public int[] timeParser(String stime)
	{
		stime = stime.replaceAll(" \\(.+", "");
		String timeParts[] = stime.split(",");
		int time[] = new int[timeParts.length];
		
		for (int i = 0; i < timeParts.length; i++)
		{
			time[timeParts.length - i - 1] = Integer.parseInt(timeParts[i].replaceAll("[a-z]", ""));
		}
		
		return time;
	}
	
	public void getPeriods()
	{
		try
		{
			BtdState btdState = new BtdState();
			rs = null;
			
			do
			{
				// Pega valor inicial
				rs = execQuery("select timestamp, PLUG_TYPE from t_fgdata where timestamp > "
				               + btdState.getEnd() + " LIMIT 1;");
				btdState = new BtdState();
				btdState.setStart(rs.getLong(1));
				btdState.setStatus(rs.getInt(2));
				
				// Busca uma mudança de estado
				if (btdState.getStatus() == 0)
				{
					rs = execQuery("select timestamp from t_fgdata where rowid = (select (rowid-1) from t_fgdata where timestamp > "
					               + btdState.getStart() + " AND PLUG_TYPE = 1 LIMIT 1);");
				}
				else
				{
					rs = execQuery("select timestamp from t_fgdata where rowid = (select (rowid-1) from t_fgdata where timestamp > "
					               + btdState.getStart() + " AND PLUG_TYPE = 0 LIMIT 1);");
				}
				
				// Se existe mudança, pega o valor final do estado atual, adiciona o estado na lista e busca pelo novo estado.
				if (!rs.isClosed())
				{
					btdState.setEnd(rs.getLong(1));
					statesData.add(btdState);
				}
				// Se nao existir mais mudanças de estado, finaliza o atual com o ponto final do log e para a busca.
				else
				{
					rs = execQuery("select timestamp from t_fgdata where rowid = (select MAX(rowid) from t_fgdata);");
					btdState.setEnd(rs.getLong(1));
					statesData.add(btdState);
					// rs.close();
					rs = null;
				}
				
			}
			while (rs != null);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		/*
		 * for (BtdState btdState : statesData) { System.out.println("From: " + btdState.getStartDate() + " To: " + btdState.getEndDate() +
		 * " Status: " + btdState.getStatus()); }
		 */
	}
	
	public BtdState getLongerDischargingPeriod()
	{
		if (statesData.size() == 0)
			getPeriods();
		
		return statesData.getLongerDischargingPeriod();
	}
	
	public long getTimeZoneMillis()
	{
		try
		{
			long timezone = -1;
			
			rs = stmt.executeQuery("SELECT VALUE FROM t_phoneinfo WHERE NAME LIKE 'TZ_CURRENT_OFFSET';");
			
			while (rs.next())
			{
				timezone = rs.getLong(1);
			}
			
			// rs.close();
			// stmt.close();
			
			return timezone;
		}
		catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		
		return -1;
	}
	
	public String getTimeZoneName()
	{
		try
		{
			String timezone;
			
			rs = stmt.executeQuery("SELECT VALUE FROM t_phoneinfo WHERE NAME LIKE 'TZ_ID';");
			timezone = rs.getString(1);
			
			return timezone;
		}
		catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			return null;
		}
	}
	
	public ResultSet execQuery(String query)
	{
		try
		{
			rs = stmt.executeQuery(query);
			
			return rs;
		}
		catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			return null;
		}
	}
	
	public void close()
	{
		try
		{
			rs.close();
			stmt.close();
			c.close();
		}
		catch (SQLException e)
		{
			// e.printStackTrace();
		}
	}
	
	public void setupBtdRow(BtdRow btd, ResultSet rs) throws SQLException
	{
		btd.setActiveKernels(rs.getString("ActiveKernelWL"));
		btd.setAudioTime(rs.getString("AudioTime"));
		btd.setAwakeTimeOnBattery(rs.getString("AwakeTimeOnBatt"));
		btd.setAwakeTimeTotal(rs.getString("AwakeTimeTotal"));
		btd.setBacklightIntensity(rs.getInt("BacklightIntensity"));
		btd.setBatteryCapacity(rs.getInt("FCC"));
		btd.setBatteryLevel(rs.getInt("BATTERY_LEVEL"));
		btd.setChargerPlugged(rs.getInt("PLUG_TYPE"));
		btd.setDate(new Date(rs.getLong("timestamp")));
		btd.setForegroundActivity(rs.getString("FgActivity"));
		btd.setForegroundPackage(rs.getString("FgPackage"));
		btd.setGpsLocationUpdates(rs.getLong("GpsLocCount"));
		btd.setInstantCurrent(rs.getLong("CurrentNow"));
		btd.setNetworkLocationUpdates(rs.getLong("NetworkLocCount"));
		btd.setPerUidData(rs.getString("PerUidData"));
		btd.setPhoneCallTime(rs.getString("ActivePhoneCallTime"));
		btd.setRealTimeOnBattery(rs.getString("RealTimeOnBatt"));
		btd.setRealTimeTotal(rs.getString("RealTimeTotal"));
		btd.setScreenBrightnesses(rs.getString("ScreenBrightnesses"));
		btd.setScreenOn(rs.getInt("ScreenOn"));
		btd.setScreenOnTime(rs.getString("ScreenOnTime"));
		btd.setSignalLevels(rs.getString("SignalLevels"));
		btd.setTemperature(rs.getInt("TEMP"));
		btd.setTimestamp(rs.getLong("timestamp"));
		btd.setTopProcesses(rs.getString("TopData"));
		btd.setWifiOnTime(rs.getString("WifiOnTime"));
		btd.setWifirunningTime(rs.getString("WifiRunningTime"));
	}
	
	public void setupBtdRow(BtdRow btd, ResultSet rs, long timezone) throws SQLException
	{
		btd.setActiveKernels(rs.getString("ActiveKernelWL"));
		btd.setAudioTime(rs.getString("AudioTime"));
		btd.setAwakeTimeOnBattery(rs.getString("AwakeTimeOnBatt"));
		btd.setAwakeTimeTotal(rs.getString("AwakeTimeTotal"));
		btd.setBacklightIntensity(rs.getInt("BacklightIntensity"));
		btd.setBatteryCapacity(rs.getInt("FCC"));
		btd.setBatteryLevel(rs.getInt("BATTERY_LEVEL"));
		btd.setChargerPlugged(rs.getInt("PLUG_TYPE"));
		btd.setDate(new Date(rs.getLong("timestamp") + timezone));
		btd.setForegroundActivity(rs.getString("FgActivity"));
		btd.setForegroundPackage(rs.getString("FgPackage"));
		btd.setGpsLocationUpdates(rs.getLong("GpsLocCount"));
		btd.setInstantCurrent(rs.getLong("CurrentNow"));
		btd.setNetworkLocationUpdates(rs.getLong("NetworkLocCount"));
		btd.setPerUidData(rs.getString("PerUidData"));
		btd.setPhoneCallTime(rs.getString("ActivePhoneCallTime"));
		btd.setRealTimeOnBattery(rs.getString("RealTimeOnBatt"));
		btd.setRealTimeTotal(rs.getString("RealTimeTotal"));
		btd.setScreenBrightnesses(rs.getString("ScreenBrightnesses"));
		btd.setScreenOn(rs.getInt("ScreenOn"));
		btd.setScreenOnTime(rs.getString("ScreenOnTime"));
		btd.setSignalLevels(rs.getString("SignalLevels"));
		btd.setTemperature(rs.getInt("TEMP"));
		btd.setTimestamp(rs.getLong("timestamp") + timezone);
		btd.setTopProcesses(rs.getString("TopData"));
		btd.setWifiOnTime(rs.getString("WifiOnTime"));
		btd.setWifirunningTime(rs.getString("WifiRunningTime"));
	}
	
	// Getters and Setters
	public BtdRowsList getBtdRows()
	{
		return btdRows;
	}
	
	public BtdStatesData getStatesData()
	{
		if (statesData.size() == 0)
			getPeriods();
		
		return statesData;
	}
	
	public int getAverageconsumeOn()
	{
		return (int) (consumeOn / (timeOn / 3600000.0));
	}
	
	public int getAverageconsumeOff()
	{
		return (int) (consumeOff / (timeOff / 3600000.0));
	}
	
	public String getTetherPercentage()
	{
		DecimalFormat df = new DecimalFormat("##.##");
		df.setRoundingMode(RoundingMode.DOWN);
		return df.format(100.0 * tetheringTime / realTimeOnBatt);
	}
	
	public double millisToHours(long millis)
	{
		return (double) (millis / 3600000.0);
	}

	public int getStatus()
	{
		return status;
	}

	public BtdState getFinalState()
	{
		return finalState;
	}

	public long[] getScreenData()
	{
		return screenData;
	}

	public long[] getSignalData()
	{
		return signalData;
	}

	public float[] getCpuTempData()
	{
		return cpuTempData;
	}

	public float[] getDeviceTempData()
	{
		return deviceTempData;
	}

	public int[] getBttDischarged()
	{
		return bttDischarged;
	}

	public long getCellTX()
	{
		return cellTX;
	}

	public long getCellRX()
	{
		return cellRX;
	}

	public long getWifiTX()
	{
		return wifiTX;
	}

	public long getWifiRX()
	{
		return wifiRX;
	}

	public long getGpsLocation()
	{
		return gpsLocation;
	}

	public long getNetworkLocation()
	{
		return networkLocation;
	}

	public long getConsumeOn()
	{
		return consumeOn;
	}

	public long getConsumeOff()
	{
		return consumeOff;
	}

	public long getTimeOff()
	{
		return timeOff;
	}

	public long getTimeOn()
	{
		return timeOn;
	}

	public long getWifiOnTime()
	{
		return wifiOnTime;
	}

	public long getWifiRunningTime()
	{
		return wifiRunningTime;
	}

	public long getRealTimeOnBatt()
	{
		return realTimeOnBatt;
	}

	public long getAwakeTimeOnBatt()
	{
		return awakeTimeOnBatt;
	}

	public long getPhoneCall()
	{
		return phoneCall;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}

	public void setBtdRows(BtdRowsList btdRows)
	{
		this.btdRows = btdRows;
	}

	public void setFinalState(BtdState finalState)
	{
		this.finalState = finalState;
	}

	public void setStatesData(BtdStatesData statesData)
	{
		this.statesData = statesData;
	}

	public void setScreenData(long[] screenData)
	{
		this.screenData = screenData;
	}

	public void setSignalData(long[] signalData)
	{
		this.signalData = signalData;
	}

	public void setCpuTempData(float[] cpuTempData)
	{
		this.cpuTempData = cpuTempData;
	}

	public void setDeviceTempData(float[] deviceTempData)
	{
		this.deviceTempData = deviceTempData;
	}

	public void setBttDischarged(int[] bttDischarged)
	{
		this.bttDischarged = bttDischarged;
	}

	public void setCellTX(long cellTX)
	{
		this.cellTX = cellTX;
	}

	public void setCellRX(long cellRX)
	{
		this.cellRX = cellRX;
	}

	public void setWifiTX(long wifiTX)
	{
		this.wifiTX = wifiTX;
	}

	public void setWifiRX(long wifiRX)
	{
		this.wifiRX = wifiRX;
	}

	public void setGpsLocation(long gpsLocation)
	{
		this.gpsLocation = gpsLocation;
	}

	public void setNetworkLocation(long networkLocation)
	{
		this.networkLocation = networkLocation;
	}

	public void setConsumeOn(long consumeOn)
	{
		this.consumeOn = consumeOn;
	}

	public void setConsumeOff(long consumeOff)
	{
		this.consumeOff = consumeOff;
	}

	public void setTimeOff(long timeOff)
	{
		this.timeOff = timeOff;
	}

	public void setTimeOn(long timeOn)
	{
		this.timeOn = timeOn;
	}

	public void setWifiOnTime(long wifiOnTime)
	{
		this.wifiOnTime = wifiOnTime;
	}

	public void setWifiRunningTime(long wifiRunningTime)
	{
		this.wifiRunningTime = wifiRunningTime;
	}

	public void setRealTimeOnBatt(long realTimeOnBatt)
	{
		this.realTimeOnBatt = realTimeOnBatt;
	}

	public void setAwakeTimeOnBatt(long awakeTimeOnBatt)
	{
		this.awakeTimeOnBatt = awakeTimeOnBatt;
	}

	public void setPhoneCall(long phoneCall)
	{
		this.phoneCall = phoneCall;
	}

	public void setTetheringTime(long tetheringTime)
	{
		this.tetheringTime = tetheringTime;
	}

	public int getBatCap()
	{
		return batCap;
	}

	public void setBatCap(int batCap)
	{
		this.batCap = batCap;
	}
}
