package supportive.parsers.logsparser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateOperator
{
	public static long getMillis(String sDate) throws ParseException
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Date date = dateFormat.parse(sDate);
		
		return date.getTime();
	}
	
	static public String getDateStringFromBtdStringMillis(long timestamp)
	{
		long days, hours, minutes, seconds, millis;
		
		millis = timestamp % 1000;
		seconds = (timestamp / 1000) % 60;
		minutes = (timestamp / (60*1000)) % 60;
		hours = (timestamp / (60*60*1000)) % 24;
		days = timestamp / (24*60*60*1000);
		
		return days + "d," + hours + "h," + minutes + "m," + seconds + "s," + millis + "ms";
	}
}