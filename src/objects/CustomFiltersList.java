package objects;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


/**
 * Defines a filters list
 */
public class CustomFiltersList extends ArrayList<CustomFilterItem>
{
	private static final long serialVersionUID = -170544270549969807L;
	
	public CustomFiltersList()
	{
		super();
	}
	
	/**
	 * Look for a filter index in the list
	 * 
	 * @param obj Filter item
	 * @return Index as {@link int}. -1 if not found
	 */
	public int indexOf(CustomFilterItem obj)
	{
		for (int i = 0; i < this.size(); i++)
		{
			if (obj.getName().equals(this.get(i).getName()))
			{
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Return the index of a filter with the name
	 * 
	 * @param name String that contains the filter name
	 * @return Index as {@link int}. -1 if not found
	 */
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
	
	/**
	 * Get the list of filters you own
	 * 
	 * @param user Your username (owner)
	 * @return List of filters that belongs to user
	 */
	public ArrayList<CustomFilterItem> myfilters(String user)
	{
		ArrayList<CustomFilterItem> yourFilters = new ArrayList<CustomFilterItem>();
		
		for (int i = 0; i < this.size(); i++)
		{
			if (user.equals(this.get(i).getOwner()))
			{
				yourFilters.add(this.get(i));
			}
		}
		return yourFilters;
	}
	
	/**
	 * FilterItem comparator
	 */
	public class itensComparator implements Comparator<CustomFilterItem>
	{
		public int compare(CustomFilterItem p1, CustomFilterItem p2)
		{
			return (int) p1.getName().compareTo(p2.getName());
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