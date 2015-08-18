package objects;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


@SuppressWarnings("serial")
public class CrItemList extends ArrayList<CrItem>
{
	public CrItemList(){
		super();
		this.clear();
	}
	
	/**
	 * Return the index of a CR with same JiraID of given CR
	 * 
	 * @param obj Cr item
	 * @return Index of a CR with same JiraID of given CR
	 */
	public int indexOf(CrItem obj)
	{
		for (int i = 0; i < this.size(); i++)
		{
			if (obj.getJiraID().equals(this.get(i).getJiraID()))
			{
				return i;
			}
		}
		
		return -1;
	}
	
	/**
	 * Return the index of a CR with having given JiraID
	 * 
	 * @param key JiraID
	 * @return Index of a CR with having given JiraID
	 */
	public int indexOf(String key)
	{
		for (int i = 0; i < this.size(); i++)
		{
			if (key.equals(this.get(i).getJiraID()))
			{
				return i;
			}
		}
		
		return -1;
	}
	
	/**
	 * CrItem comparator
	 */
	public class itensComparator implements Comparator<CrItem>
	{
		public int compare(CrItem p1, CrItem p2)
		{
			return (int) p1.getJiraID().compareTo(p2.getJiraID());
		}
	}
	
	/**
	 * Sort list
	 */
	public void sortItens()
	{
		Collections.sort(this, new itensComparator());
		Collections.reverse(this);
	}
}