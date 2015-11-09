package supportive.preanalyzers.btdparser;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import core.Logger;


public class BtdAppList extends ArrayList<BtdAppInfo>
{
	long totalRx, totalTx;
	
	public BtdAppList()
	{
		totalRx = 0;
		totalTx = 0;
	}
	
	public boolean update(String name, String uid, long cpu, long rx, long tx)
	{
		for (int i = 0; i < this.size(); i++)
		{
			if (name.equals(this.get(i).getName()))
			{
				get(i).update(cpu, rx, tx);
				//Logger.log("BtdAppsList", "Updating item");
				return true;
			}
		}
		
		BtdAppInfo app = new BtdAppInfo(name, uid, cpu, rx, tx);
		add(app);
		//Logger.log("BtdAppsList", "Adding new item");
		
		return false;
	}
	
	public int indexOf(String name)
	{
		for (int i = 0; i < this.size(); i++)
		{
			if (name.equals(this.get(i).getName()))
			{
				return i;
			}
		}
		
		return -1;
	}
	
	public int indexOf(BtdAppInfo item)
	{
		for (int i = 0; i < this.size(); i++)
		{
			if (item.getName().equals(this.get(i).getName()))
			{
				return i;
			}
		}
		
		return -1;
	}
	
	public BtdAppInfo get(String name)
	{
		for (int i = 0; i < this.size(); i++)
		{
			if (name.equals(this.get(i).getName()))
			{
				return this.get(i);
			}
		}
		
		return null;
	}
	
	public BtdAppInfo get(BtdAppInfo item)
	{
		for (int i = 0; i < this.size(); i++)
		{
			if (item.getName().equals(this.get(i).getName()))
			{
				return this.get(i);
			}
		}
		
		return null;
	}
	
	public long getTotalRx()
	{
		if (totalRx > 0)
			return totalRx;
		
		for (BtdAppInfo a : this)
		{
			totalRx += a.getDeltaTx();
		}
		
		return totalRx;
	}
	
	
	// Comparator and sort
	public class compareByName implements Comparator<BtdAppInfo>
	{
		public int compare(BtdAppInfo p1, BtdAppInfo p2)
		{
			return (int) (p1.getName().toLowerCase().compareTo(p2.getName().toLowerCase()));
		}
	}
	
	public class compareByCpuTime implements Comparator<BtdAppInfo>
	{
		public int compare(BtdAppInfo p1, BtdAppInfo p2)
		{
			return (int) (Float.compare(p1.getDeltaCpuTime(), p2.getDeltaCpuTime()));
		}
	}
	
	public class compareByDataRx implements Comparator<BtdAppInfo>
	{
		public int compare(BtdAppInfo p1, BtdAppInfo p2)
		{
			return (int) (Float.compare(p1.getDeltaRx(), p2.getDeltaRx()));
		}
	}
	
	public class compareByDataTx implements Comparator<BtdAppInfo>
	{
		public int compare(BtdAppInfo p1, BtdAppInfo p2)
		{
			return (int) (Float.compare(p1.getDeltaTx(), p2.getDeltaTx()));
		}
	}
	
	public class compareByTotalData implements Comparator<BtdAppInfo>
	{
		public int compare(BtdAppInfo p1, BtdAppInfo p2)
		{
			return (int) (Float.compare(p1.getDeltaTx() + p1.getDeltaRx(), p2.getDeltaTx() + p1.getDeltaRx()));
		}
	}
	
	public void sortByName()
	{
		Collections.sort(this, new compareByName());
		Collections.reverse(this);
	}
	
	public void sortByCpu()
	{
		Collections.sort(this, new compareByCpuTime());
		Collections.reverse(this);
	}
	
	public void sortByDataRx()
	{
		Collections.sort(this, new compareByDataRx());
		Collections.reverse(this);
	}
	
	public void sortByDataTx()
	{
		Collections.sort(this, new compareByDataTx());
		Collections.reverse(this);
	}
	
	public void sortByTotalData()
	{
		Collections.sort(this, new compareByTotalData());
		Collections.reverse(this);
	}
}
