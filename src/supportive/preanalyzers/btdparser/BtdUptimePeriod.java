package supportive.preanalyzers.btdparser;


import supportive.DateTimeOperator;


public class BtdUptimePeriod
{
	private long start;
	private long end;
	private long duration;
	
	public BtdUptimePeriod()
	{
		start = -1;
		end = -1;
		duration = -1;
	}
	
	public BtdUptimePeriod(long begin)
	{
		start = -1;
		end = -1;
		duration = -1;
	}
	
	// Getters and Setters -----------------------------------------------------
	public long getStart()
	{
		return start;
	}
	
	public void setStart(long start)
	{
		this.start = start;
	}
	
	public long getEnd()
	{
		return end;
	}
	
	public void setEnd(long end)
	{
		this.end = end;
		duration = end - start;
	}
	
	public long getDuration()
	{
		duration = end - start;
		return duration;
	}
	
	public void setDuration(long duration)
	{
		this.duration = duration;
	}
	
	public String toString()
	{
		String tostring = "\tStarted: " + BtdParser.formatDate(BtdParser.generateDate(start)) + "\n\tEnded: "
		                  + BtdParser.formatDate(BtdParser.generateDate(end)) + "\n\tDuration: "
		                  + DateTimeOperator.getTimeStringFromMillis(duration);
		return tostring;
	}
	
	public String toJiraComment()
	{
		String tostring = "||Started|" + BtdParser.formatDate(BtdParser.generateDate(start)) + "|\\n||Ended|"
		                  + BtdParser.formatDate(BtdParser.generateDate(end)) + "|\\n||Duration|"
		                  + DateTimeOperator.getTimeStringFromMillis(duration) + "|\\n";
		return tostring;
	}
}
