package supportive.preanalyzers.logsparser;


import java.text.ParseException;

import supportive.DateTimeOperator;


public class BugRepJavaWL
{
	String name;
	String uid;
	long   duration;
	int    timesAcquired;
	
	public BugRepJavaWL(String uid, String name, String duration, String timesAcquired) throws ParseException
	{
		this.name = name.replaceAll(" +$", "");
		this.uid = uid;
		this.duration = DateTimeOperator.getMillisFromBtdStringDate(duration);
		this.timesAcquired = Integer.parseInt(timesAcquired);
	}
	
	public String toString()
	{
		return "[" + "name=" + name + ", uid=" + uid + ", duration=" + duration + "ms > "
		       + DateTimeOperator.getTimeStringFromMillis(duration) + ", timesAcquired=" + timesAcquired
		       + "]";
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getUid()
	{
		return uid;
	}

	public void setUid(String uid)
	{
		this.uid = uid;
	}

	public long getDuration()
	{
		return duration;
	}

	public void setDuration(long duration)
	{
		this.duration = duration;
	}

	public int getTimesAcquired()
	{
		return timesAcquired;
	}

	public void setTimesAcquired(int timesAcquired)
	{
		this.timesAcquired = timesAcquired;
	}
	
	
}
