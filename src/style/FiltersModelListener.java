package style;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

public class FiltersModelListener implements TableModelListener
{

	@SuppressWarnings("unused")
	@Override
	public void tableChanged(TableModelEvent evt)
	{
		if (evt.getType() == TableModelEvent.UPDATE)
		{
			int column = evt.getColumn();
			int row = evt.getFirstRow();
			
			System.out.println("UPDATE: row: " + row + " column: " + column);
		}
		
		if (evt.getType() == TableModelEvent.INSERT)
		{
			int column = evt.getColumn();
			int row = evt.getFirstRow();
			
			//System.out.println("INSERT: row: " + row + " column: " + column);
		}
		
		if (evt.getType() == TableModelEvent.DELETE)
		{
			int column = evt.getColumn();
			int row = evt.getFirstRow();
			
			//System.out.println("DELETE: row: " + row + " column: " + column);
		}
	}

}
