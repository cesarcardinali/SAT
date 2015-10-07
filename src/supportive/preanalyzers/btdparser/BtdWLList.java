package supportive.preanalyzers.btdparser;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class BtdWLList extends ArrayList<BtdWL>
{
	public boolean update(int index, BtdWL wl)
	{
		if(index >= 0)
		{
			return get(index).update(wl);
		}
		
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
	
	public int indexOf(BtdWL item)
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
	
	public class itensComparator implements Comparator<BtdWL>
	{
		public int compare(BtdWL p1, BtdWL p2)
		{
			return (int) (Float.compare(p1.getActiveSince(), p2.getActiveSince()));
		}
	}
	
	public void sortItens()
	{
		Collections.sort(this, new itensComparator());
		Collections.reverse(this);
	}
}
