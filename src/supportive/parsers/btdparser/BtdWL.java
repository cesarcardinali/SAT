package supportive.parsers.btdparser;


public class BtdWL
{
	private String name;
	private long   activeSince;
	private long   totalTime;
	
	public BtdWL(String builder)
	{
		String[] pieces = builder.split(":");
		name = pieces[0];
		activeSince = Long.parseLong(pieces[4])/1000000;
		totalTime = Long.parseLong(pieces[5])/1000000;
	}
	
	// Getters and Setters
	public String getName()
	{
		return name;
	}
	
	public long getActiveSince()
	{
		return activeSince;
	}
	
	public long getTotalTime()
	{
		return totalTime;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public void setActiveSince(long activeSince)
	{
		this.activeSince = activeSince/1000000;
	}
	
	public void setActiveSince(String activeSince)
	{
		this.activeSince = Long.parseLong(activeSince)/1000000;
	}
	
	public void setTotalTime(long totalTime)
	{
		this.totalTime = totalTime/1000000;
	}
	
	public void setTotalTime(String totalTime)
	{
		this.totalTime = Long.parseLong(totalTime)/1000000;
	}
}