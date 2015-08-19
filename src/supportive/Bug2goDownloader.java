package supportive;


import java.util.ArrayList;

import core.Logger;


public class Bug2goDownloader implements Runnable
{
	private ArrayList<Bug2goItem> bug2goList;
	private boolean				  alldone = false;
	
	public Bug2goDownloader(ArrayList<String> bugIdList)
	{
		bug2goList = new ArrayList<Bug2goItem>();
		for (String s : bugIdList)
		{
			bug2goList.add(new Bug2goItem(s));
		}
	}
	
	public Bug2goDownloader(String[] bugIdList)
	{
		bug2goList = new ArrayList<Bug2goItem>();
		for (String s : bugIdList)
		{
			bug2goList.add(new Bug2goItem(s));
		}
	}
	
	@Override
	public void run()
	{
		for (Bug2goItem b : bug2goList)
		{
			new Thread(b, b.getBugId()).start();
		}
		
		while (alldone == false)
		{
			alldone = true;
			Logger.log(Logger.TAG_BUG2GODOWNLOADER, "Checking...");
			
			for (Bug2goItem b : bug2goList)
			{
				Logger.log(Logger.TAG_BUG2GODOWNLOADER,
						   b.getBugId() + " status: " + b.getStatus() + " running: " + b.isRunning());
				if (b.isRunning() == true && alldone == true)
				{
					alldone = false;
				}
			}
			
			try
			{
				Thread.sleep(3000);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		
		Logger.log(Logger.TAG_BUG2GODOWNLOADER, "Downloads finished");
		
		for (Bug2goItem b : bug2goList)
		{
			Logger.log(Logger.TAG_BUG2GODOWNLOADER, b.getBugId() + ": " + b.getStatus());
		}
		
	}
	
	public boolean isAlldone()
	{
		return alldone;
	}
	
	public void setAlldone(boolean alldone)
	{
		this.alldone = alldone;
	}
	
}
