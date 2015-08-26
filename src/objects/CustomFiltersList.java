package objects;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


/**
 * Defines a filters list
 */
@SuppressWarnings("serial")
public class CustomFiltersList extends ArrayList<CustomFilterItem>
{
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
	 * @return
	 */
	public CustomFiltersList getActiveFilters(){
		CustomFiltersList aux = new CustomFiltersList();
		for (CustomFilterItem filter : this)
		{
			if (filter.isActive())
			{
				aux.add(filter);
			}
		}
		
		return aux;
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
	
	public String toString()
	{
		String toString = "";
		for (CustomFilterItem filter : this)
		{
			toString = toString + "--------------\n" + filter.toString() + "\n--------------\n";
		}
		return toString;
	}
}