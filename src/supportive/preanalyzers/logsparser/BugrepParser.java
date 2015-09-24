package supportive.preanalyzers.logsparser;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import supportive.DateOperator;
import core.Logger;


public class BugrepParser
{
	private class BugRepKernelWL
	{
		String name;
		long   duration;
		int    timesAcquired;
		
		public BugRepKernelWL(String name, String duration, String timesAcquired) throws ParseException
		{
			this.name = name.replaceAll(" +$", "");
			this.duration = DateOperator.getMillisFromBtdStringDate(duration);
			this.timesAcquired = Integer.parseInt(timesAcquired);
		}
		
		public String toString()
		{
			return "[" + "name=" + name + ", duration=" + duration + "ms > "
			       + DateOperator.getDateStringFromBtdStringMillis(duration) + ", timesAcquired="
			       + timesAcquired + "]";
		}
	}
	
	private class BugRepJavaWL
	{
		String name;
		String uid;
		long   duration;
		int    timesAcquired;
		
		public BugRepJavaWL(String uid, String name, String duration, String timesAcquired) throws ParseException
		{
			this.name = name.replaceAll(" +$", "");
			this.uid = uid;
			this.duration = DateOperator.getMillisFromBtdStringDate(duration);
			this.timesAcquired = Integer.parseInt(timesAcquired);
		}
		
		public String toString()
		{
			return "[" + "name=" + name + ", uid=" + uid + ", duration=" + duration + "ms > "
			       + DateOperator.getDateStringFromBtdStringMillis(duration) + ", timesAcquired="
			       + timesAcquired + "]";
		}
	}
	
	// All long variables represent time in millis
	private String                    path;
	private long                      timeOnBat;
	private long                      scOffTime;
	private long                      remTime;
	private long                      scDark;
	private long                      scDim;
	private long                      scMedium;
	private long                      scLight;
	private long                      scBright;
	private long                      signalNone;
	private long                      signalPoor;
	private long                      signalModerate;
	private long                      signalGood;
	private long                      signalGreat;
	private long                      signalScan;
	private long                      radioNone;
	private long                      radioGprs;
	private long                      radioUmts;
	private long                      radioEvdo;
	private long                      radio1xrtt;
	private long                      radioActive;
	private long                      wifiOn;
	private long                      wifiScan;
	private long                      wifiLevel0;
	private long                      wifiLevel1;
	private long                      wifiLevel2;
	private long                      wifiLevel3;
	private long                      wifiLevel4;
	private int                     connectChanges; // Times that radio connection changed
	private float                     dischargeAmount; // Percentage
	private float                     dischScOn;      // Percentage from discharge amount
	private float                     dischScOff;     // Percentage from discharge amount
	private float                     batCap;         // Total battery capacity
	private String                    result;
	private ArrayList<BugRepKernelWL> kernelWLs;
	private ArrayList<BugRepJavaWL>   javaWLs;
	
	public BugrepParser(String path)
	{
		this.path = path;
		kernelWLs = new ArrayList<BugRepKernelWL>();
		javaWLs = new ArrayList<BugRepJavaWL>();
	}
	
	public boolean parse()
	{
		String sCurrentLine;
		String sLastLine = "";
		String file_report = "";
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		BufferedReader br = null;
		
		if (!folder.isDirectory())
		{
			result = "Not a directory";
			return false;
		}
		
		// Look for the file to be parsed
		for (int i = 0; i < listOfFiles.length; i++)
		{
			if (listOfFiles[i].isFile())
			{
				String files = listOfFiles[i].getName();
				
				if (((files.endsWith(".txt")) || (files.endsWith(".TXT"))) && (files.contains("bugreport")))
				{
					if (path.equals("."))
						file_report = files;
					else
						file_report = path + "/" + files;
					
					break;
				}
			}
		}
		
		Logger.log(Logger.TAG_BUGREPORT_PARSER, "\nBugreport file: " + file_report);
		
		// Try to open file
		if (!file_report.equals(""))
		{
			try
			{
				// Regex configuration
				Pattern ptTimeOnBat = Pattern.compile("Time on battery: (.+) \\((.+)%\\) realtime, (.+) \\((.+)%\\) uptime");
				Pattern ptScOffTime = Pattern.compile("Time on battery screen off: (.+) \\((.+)%\\) realtime, (.+) \\((.+)%\\) uptime");
				Pattern ptRemTime = Pattern.compile("Charge time remaining: (.+)");
				Pattern ptScDark = Pattern.compile("dark (.+) \\((.+)%\\)");
				Pattern ptScDim = Pattern.compile("dim (.+) \\((.+)%\\)");
				Pattern ptScMedium = Pattern.compile("medium (.+) \\((.+)%\\)");
				Pattern ptScLight = Pattern.compile("light (.+) \\((.+)%\\)");
				Pattern ptScBright = Pattern.compile("bright (.+) \\((.+)%\\)");
				Pattern ptConnectChanges = Pattern.compile("Connectivity changes: (.+)");
				Pattern ptSignalNone = Pattern.compile("none (.+) \\((.+)%\\)");
				Pattern ptSignalPoor = Pattern.compile("poor (.+) \\((.+)%\\)");
				Pattern ptSignalModerate = Pattern.compile("moderate (.+) \\((.+)%\\)");
				Pattern ptSignalGood = Pattern.compile("good (.+) \\((.+)%\\)");
				Pattern ptSignalGreat = Pattern.compile("great (.+) \\((.+)%\\)");
				Pattern ptSignalScan = Pattern.compile("Signal scanning time: (.+)");
				Pattern ptRadioNone = Pattern.compile("none (.+) \\((.+)%\\) ");
				Pattern ptRadioGprs = Pattern.compile("gprs (.+) \\((.+)%\\) ");
				Pattern ptRadioUmts = Pattern.compile("umts (.+) \\((.+)%\\) ");
				Pattern ptRadioEvdo = Pattern.compile("Evdo (.+) \\((.+)%\\) ");
				Pattern ptRadio1xrtt = Pattern.compile("1xrtt (.+) \\((.+)%\\) ");
				Pattern ptRadioActive = Pattern.compile("Mobile radio active time: (.+) \\((.+)%\\) ");
				Pattern ptWifiOn = Pattern.compile("Wifi on: (.+) \\((.+)%\\), ");
				Pattern ptWifiScan = Pattern.compile("scanning (.+) \\((.+)%\\) ");
				Pattern ptWifiLevel0 = Pattern.compile("level\\(0\\) (.+) \\((.+)%\\) ");
				Pattern ptWifiLevel1 = Pattern.compile("level\\(1\\) (.+) \\((.+)%\\) ");
				Pattern ptWifiLevel2 = Pattern.compile("level\\(2\\) (.+) \\((.+)%\\) ");
				Pattern ptWifiLevel3 = Pattern.compile("level\\(3\\) (.+) \\((.+)%\\) ");
				Pattern ptWifiLevel4 = Pattern.compile("level\\(4\\) (.+) \\((.+)%\\) ");
				Pattern ptDischargeAmount = Pattern.compile("Amount discharged \\(upper bound\\): (\\d+)");
				Pattern ptDischScOn = Pattern.compile("Amount discharged while screen on: (\\d+)");
				Pattern ptDischScOff = Pattern.compile("Amount discharged while screen off: (\\d+)");
				Pattern ptBatCap = Pattern.compile("Capacity: (\\d+), Computed drain");
				Pattern ptKernelWL = Pattern.compile("Kernel Wake lock (.+): (.+) \\((.+) times\\)");
				Pattern ptJavaWL = Pattern.compile("Wake lock (.+\\d) (.+): (.+) \\((.+) times\\)");
				
				Matcher matcher;
				
				br = new BufferedReader(new FileReader(file_report));
				
				// Search for b2g evidences
				while ((sCurrentLine = br.readLine()) != null)
				{
					if (sCurrentLine.contains("Statistics since last charge:"))
					{
						Logger.log(Logger.TAG_BUGREPORT_PARSER, "Statistics line found: " + sCurrentLine);
						
						// Read next line
						while ((sCurrentLine = br.readLine()) != null)
						{
							if (sCurrentLine.contains("Cell standby: "))
							{
								break;
							}
							else
							{
								
								matcher = ptTimeOnBat.matcher(sCurrentLine);
								if (matcher.find())
								{
									timeOnBat = DateOperator.getMillisFromBtdStringDate(matcher.group(1));
									System.out.println("1 ptTimeOnBat " + timeOnBat);
									continue;
								}
								
								matcher = ptScOffTime.matcher(sCurrentLine);
								if (matcher.find())
								{
									scOffTime = DateOperator.getMillisFromBtdStringDate(matcher.group(1));
									System.out.println("2 ptScOffTime " + scOffTime);
									continue;
								}
								
								matcher = ptRemTime.matcher(sCurrentLine);
								if (matcher.find())
								{
									remTime = DateOperator.getMillisFromBtdStringDate(matcher.group(1));
									System.out.println("3 ptRemTime " + remTime);
									continue;
								}
								
								matcher = ptScDark.matcher(sCurrentLine);
								if (matcher.find())
								{
									scDark = DateOperator.getMillisFromBtdStringDate(matcher.group(1));
									System.out.println("4 ptScDark " + scDark);
									continue;
								}
								
								matcher = ptScDim.matcher(sCurrentLine);
								if (matcher.find())
								{
									scDim = DateOperator.getMillisFromBtdStringDate(matcher.group(1));
									System.out.println("5 ptScDim " + scDim);
									continue;
								}
								
								matcher = ptScMedium.matcher(sCurrentLine);
								if (matcher.find())
								{
									scMedium = DateOperator.getMillisFromBtdStringDate(matcher.group(1));
									System.out.println("6 ptScMedium " + scMedium);
									continue;
								}
								
								matcher = ptScLight.matcher(sCurrentLine);
								if (matcher.find())
								{
									scLight = DateOperator.getMillisFromBtdStringDate(matcher.group(1));
									System.out.println("7 ptScLight " + scLight);
									continue;
								}
								
								matcher = ptScBright.matcher(sCurrentLine);
								if (matcher.find())
								{
									scBright = DateOperator.getMillisFromBtdStringDate(matcher.group(1));
									System.out.println("8 ptScBright " + scBright);
									continue;
								}
								
								matcher = ptConnectChanges.matcher(sCurrentLine);
								if (matcher.find())
								{
									connectChanges = Integer.parseInt(matcher.group(1));
									System.out.println("9 ptConnectChanges " + connectChanges);
									continue;
								}
								
								matcher = ptSignalNone.matcher(sCurrentLine);
								if (matcher.find() && sLastLine.contains("Phone"))
								{
									signalNone = DateOperator.getMillisFromBtdStringDate(matcher.group(1));
									System.out.println("10 ptSignalNone " + signalNone);
									continue;
								}
								
								matcher = ptSignalPoor.matcher(sCurrentLine);
								if (matcher.find())
								{
									signalPoor = DateOperator.getMillisFromBtdStringDate(matcher.group(1));
									System.out.println("11 ptSignalPoor " + signalPoor);
									continue;
								}
								
								matcher = ptSignalModerate.matcher(sCurrentLine);
								if (matcher.find())
								{
									signalModerate = DateOperator.getMillisFromBtdStringDate(matcher.group(1));
									System.out.println("12 ptSignalModerate " + signalModerate);
									continue;
								}
								
								matcher = ptSignalGood.matcher(sCurrentLine);
								if (matcher.find())
								{
									signalGood = DateOperator.getMillisFromBtdStringDate(matcher.group(1));
									System.out.println("13 ptSignalGood " + signalGood);
									continue;
								}
								
								matcher = ptSignalGreat.matcher(sCurrentLine);
								if (matcher.find())
								{
									signalGreat = DateOperator.getMillisFromBtdStringDate(matcher.group(1));
									System.out.println("14 ptSignalGreat " + signalGreat);
									continue;
								}
								
								matcher = ptSignalScan.matcher(sCurrentLine);
								if (matcher.find())
								{
									signalScan = DateOperator.getMillisFromBtdStringDate(matcher.group(1));
									System.out.println("15 ptSignalScan " + signalScan);
									continue;
								}
								
								matcher = ptRadioNone.matcher(sCurrentLine);
								if (matcher.find() && sLastLine.contains("Radio"))
								{
									radioNone = DateOperator.getMillisFromBtdStringDate(matcher.group(1));
									System.out.println("16 ptRadioNone " + radioNone);
									continue;
								}
								
								matcher = ptRadioGprs.matcher(sCurrentLine);
								if (matcher.find())
								{
									radioGprs = DateOperator.getMillisFromBtdStringDate(matcher.group(1));
									System.out.println("17 ptRadioGprs " + radioGprs);
									continue;
								}
								
								matcher = ptRadioUmts.matcher(sCurrentLine);
								if (matcher.find())
								{
									radioUmts = DateOperator.getMillisFromBtdStringDate(matcher.group(1));
									System.out.println("18 ptRadioUmts " + radioUmts);
									continue;
								}
								
								matcher = ptRadio1xrtt.matcher(sCurrentLine);
								if (matcher.find())
								{
									radio1xrtt = DateOperator.getMillisFromBtdStringDate(matcher.group(1));
									System.out.println("19 ptRadioLxrtt " + radio1xrtt);
									continue;
								}
								
								matcher = ptRadioEvdo.matcher(sCurrentLine);
								if (matcher.find())
								{
									radioEvdo = DateOperator.getMillisFromBtdStringDate(matcher.group(1));
									System.out.println("20 ptRadioEvdo " + radioEvdo);
									continue;
								}
								
								matcher = ptRadioActive.matcher(sCurrentLine);
								if (matcher.find())
								{
									radioActive = DateOperator.getMillisFromBtdStringDate(matcher.group(1));
									System.out.println("21 ptRadioActive " + radioActive);
									continue;
								}
								
								matcher = ptWifiOn.matcher(sCurrentLine);
								if (matcher.find())
								{
									wifiOn = DateOperator.getMillisFromBtdStringDate(matcher.group(1));
									System.out.println("22 ptWifiOn " + wifiOn);
									continue;
								}
								
								matcher = ptWifiScan.matcher(sCurrentLine);
								if (matcher.find())
								{
									wifiScan = DateOperator.getMillisFromBtdStringDate(matcher.group(1));
									System.out.println("23 ptWifiScan " + wifiScan);
									continue;
								}
								
								matcher = ptWifiLevel0.matcher(sCurrentLine);
								if (matcher.find())
								{
									wifiLevel0 = DateOperator.getMillisFromBtdStringDate(matcher.group(1));
									System.out.println("24 ptWifiLevel0 " + wifiLevel0);
									continue;
								}
								
								matcher = ptWifiLevel1.matcher(sCurrentLine);
								if (matcher.find())
								{
									wifiLevel1 = DateOperator.getMillisFromBtdStringDate(matcher.group(1));
									System.out.println("25 ptWifiLevel1 " + wifiLevel1);
									continue;
								}
								
								matcher = ptWifiLevel2.matcher(sCurrentLine);
								if (matcher.find())
								{
									wifiLevel2 = DateOperator.getMillisFromBtdStringDate(matcher.group(1));
									System.out.println("26 ptWifiLevel2 " + wifiLevel2);
									continue;
								}
								
								matcher = ptWifiLevel3.matcher(sCurrentLine);
								if (matcher.find())
								{
									wifiLevel3 = DateOperator.getMillisFromBtdStringDate(matcher.group(1));
									System.out.println("27 ptWifiLevel3 " + wifiLevel3);
									continue;
								}
								
								matcher = ptWifiLevel4.matcher(sCurrentLine);
								if (matcher.find())
								{
									wifiLevel4 = DateOperator.getMillisFromBtdStringDate(matcher.group(1));
									System.out.println("28 ptWifiLevel4 " + wifiLevel4);
									continue;
								}
								
								matcher = ptDischargeAmount.matcher(sCurrentLine);
								if (matcher.find())
								{
									dischargeAmount = Float.parseFloat(matcher.group(1));
									System.out.println("29 ptDischargeAmount " + dischargeAmount);
									continue;
								}
								
								matcher = ptDischScOn.matcher(sCurrentLine);
								if (matcher.find())
								{
									dischScOn = Float.parseFloat(matcher.group(1));
									System.out.println("30 ptDischScOn " + dischScOn);
									continue;
								}
								
								matcher = ptDischScOff.matcher(sCurrentLine);
								if (matcher.find())
								{
									dischScOff = Float.parseFloat(matcher.group(1));
									System.out.println("31 ptDischScOff " + dischScOff);
									continue;
								}
								
								matcher = ptBatCap.matcher(sCurrentLine);
								if (matcher.find())
								{
									batCap = Float.parseFloat(matcher.group(1));
									System.out.println("32 ptBatCap " + batCap);
									continue;
								}
							}
							sLastLine = sCurrentLine;
						}
					}
					
					if (sCurrentLine.contains("All kernel wake locks:"))
					{
						for (int i = 0; i < 5; i++)
						{
							sCurrentLine = br.readLine();
							matcher = ptKernelWL.matcher(sCurrentLine);
							if (matcher.find())
							{
								try
								{
									kernelWLs.add(new BugRepKernelWL(matcher.group(1),
									                                 matcher.group(2),
									                                 matcher.group(3)));
									System.out.println("33 ptKernelWL " + kernelWLs.get(kernelWLs.size() - 1));
								}
								catch (ParseException e)
								{
									e.printStackTrace();
								}
							}
							else
							{
								break;
							}
						}
					}
					
					if (sCurrentLine.contains("All partial wake locks:"))
					{
						for (int i = 0; i < 5; i++)
						{
							sCurrentLine = br.readLine();
							matcher = ptJavaWL.matcher(sCurrentLine);
							if (matcher.find())
							{
								try
								{
									javaWLs.add(new BugRepJavaWL(matcher.group(1),
									                             matcher.group(2),
									                             matcher.group(3),
									                             matcher.group(4)));
									System.out.println("34 ptJavaWL " + javaWLs.get(javaWLs.size() - 1));
								}
								catch (ParseException e)
								{
									e.printStackTrace();
								}
							}
							else
							{
								break;
							}
						}
					}
					
					if (sCurrentLine.contains("All wakeup reasons:"))
					{
						break;
					}
				}
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
				return false;
			}
			catch (IOException e)
			{
				e.printStackTrace();
				return false;
			}
			finally
			{
				// Close file reader
				try
				{
					if (br != null)
						br.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
			
			return true;
		}
		else
		{
			Logger.log(Logger.TAG_BUGREPORT_PARSER, "No bugreport file found");
			return false;
		}
	}
	
	// Issue related methods
	public boolean checkForHighCurrentScOff()
	{
		if (getConsAvgOff() > 100.0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	// Show all acquired data
	public void showData()
	{
		Logger.log(Logger.TAG_BUGREPORT_PARSER, "Avg sc off consume: " + formatNumber(getConsAvgOff()));
	}
	
	// Supportive methods
	private String formatNumber(float number)
	{
		DecimalFormat df = new DecimalFormat("##.##");
		df.setRoundingMode(RoundingMode.HALF_UP);
		return df.format(number); 
	}
	
	// Get specific data
	public float getConsAvgOff()
	{
		return (float) (dischScOff * (batCap / 100.0) / (scOffTime / 3600000.0));
	}
}
