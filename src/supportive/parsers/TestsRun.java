package supportive.parsers;


import supportive.parsers.btdparser.BtdParser;


public class TestsRun
{
	@SuppressWarnings({"resource", "unused"})
	public static void main(String[] args)
	{
		long start = System.currentTimeMillis();
		//BtdParser btdParser = new BtdParser("btdTestData/BT10_311480147990712_20150908_192510.btd");
		//BtdParser btdParser = new BtdParser("C:/CRs/Debuging/80760131_B2G_2015-09-15_20-45-14/BT10_724101809975920_20150915_012914.btd");
		// semi tether
		//BtdParser btdParser = new BtdParser("C:/Users/cesar.cardinali/Desktop/semi_tethering/BT10_311480046739334_20150817_085617.btd");
		// tether
		BtdParser btdParser = new BtdParser("C:/Users/cesar.cardinali/Desktop/tethering/BT10_311480046745652_20150914_203849.btd");
		
		/*
		btdParser.getPeriods();
		SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm:ss");
		format.setTimeZone(TimeZone.getTimeZone(btdParser.getTimeZoneName()));
		
		for (BtdState item : btdParser.getStatesData())
		{
			
			System.out.println("Device was " + ((item.getStatus() == 1) ? "charging" : "discharging")
			                   + " for "
			                   + btdParser.timeDiff(item.getStart(), item.getEnd()));
			System.out.println("From " + format.format(new Date(item.getStart())) + " to "
			                   + format.format(new Date(item.getEnd())));
		}
		
		System.out.println();
		System.out.println();
		
		BtdState longerDischarge = btdParser.getLongerDischargingPeriod();
		System.out.println("The longer discharge period is from "
		                   + btdParser.formatDate(longerDischarge.getStartDate()) + " to "
		                   + btdParser.formatDate(longerDischarge.getEndDate()));
		
		*/
		
		/*
		for (int i : btdParser.timeParser("23h,47m,13s,157ms")) System.out.println(i); 
		System.out.println(btdParser.getMillisFromBtdStringDate("23h,47m,13s,157ms"));
		*/
		
		
		btdParser.parse();
		
		btdParser.close();
		
		System.out.println("\nAll done");
		System.out.println("It takes " + (System.currentTimeMillis() - start) + "ms");
		
		/*
		 * long start = System.currentTimeMillis();
		 * 
		 * 
		 * TimeZone.setDefault(TimeZone.getTimeZone("America/Los_Angeles")); String[][] results = btdParser.getMinMaxData(new
		 * String[]{"TEMP", "timestamp"}); System.out.println(results[0][0] + " - " + results[0][1] + "\n" + new
		 * Date(Long.parseLong(results[1][0])) + " - " + new Date(Long.parseLong(results[1][1])));
		 * 
		 * 
		 * System.out.println("It takes " + (System.currentTimeMillis() - start) + "ms");
		 */
		
		/*
		 * BtdRowsList btdRows = btdParser.getDischargeBtdData(btdParser.getTimeZoneMillis());
		 * 
		 * System.out.println(btdRows.getFirstItem().getDate()); System.out.println("Discharge starts at: " +
		 * btdRows.getFirstItem().getTimestamp() + " -> " + btdRows.getFirstItem().getDate()); System.out.println("Discharge ends at:   " +
		 * btdRows.getLastItem().getTimestamp() + " -> " + btdRows.getLastItem().getDate());
		 * 
		 * System.out.println(btdParser.getTimeZoneMillis());
		 */
		
		/*
		 * 
		 */
	}
	
}
