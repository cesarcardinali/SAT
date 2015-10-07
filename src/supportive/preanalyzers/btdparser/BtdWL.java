package supportive.preanalyzers.btdparser;

import supportive.DateTimeOperator;


public class BtdWL
{
	private String name;
	private long   wakeCount;
	private long   dischargeWakeCount;
	private long   activeSince;
	private long   totalTime;
	private long   totalDischargeTime;
	
	public BtdWL(String builder)
	{
		String[] pieces = builder.split(":");
		if (pieces.length < 6)
			System.out.println("-- ERROR: " + builder);
		name = pieces[0];
		wakeCount = Long.parseLong(pieces[3]);
		activeSince = Long.parseLong(pieces[4])/1000000;
		totalTime = Long.parseLong(pieces[5])/1000000;
		totalDischargeTime = 0;
	}
	
	public boolean update(BtdWL wl)
	{
		if(wl.getActiveSince() > activeSince)
		{
			activeSince = wl.getActiveSince();
		}
		totalDischargeTime = wl.getTotalTime() - totalTime;
		dischargeWakeCount = wl.getWakeCount() - wakeCount;
		
		return false;
	}
	
	public String toString()
	{
		String tostring = "------------------------------------\n"
						+ "Name: " + name + "\n"
						+ "Wakeups count: " + wakeCount + "\n"
						+ "Longer active time: " + DateTimeOperator.getTimeStringFromMillis(activeSince) + "(" + activeSince + "ms)\n"
						+ "Total discharge time: " + DateTimeOperator.getTimeStringFromMillis(totalDischargeTime) + "(" + totalDischargeTime + "ms)\n"
						+ "Total time: " + (totalDischargeTime+totalTime) + "\n"
						+ "------------------------------------";
		return tostring;
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
	
	public long getTotalDischargeTime()
	{
		return totalDischargeTime;
	}
	
	public long getWakeCount()
	{
		return wakeCount;
	}
	
	public long getDischargeWakeCount()
	{
		return dischargeWakeCount;
	}

	public void setDischargeWakeCount(long dischargeWakeCount)
	{
		this.dischargeWakeCount = dischargeWakeCount;
	}

	public void setTotalDischargeTime(long totalDischargeTime)
	{
		this.totalDischargeTime = totalDischargeTime;
	}

	public void setName(String name)
	{
		this.name = name;
	}
	
	public void setInitialWakeCount(long wakes)
	{
		wakeCount = wakes;
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
	
	public void setTotaDischargelTime(long totalTime)
	{
		this.totalDischargeTime = totalTime/1000000;
	}
	
	public void setTotalTime(String totalTime)
	{
		this.totalTime = Long.parseLong(totalTime)/1000000;
	}
	
	public void setTotaDischargelTime(String totalTime)
	{
		this.totalDischargeTime = Long.parseLong(totalTime)/1000000;
	}
}