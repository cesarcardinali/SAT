package tests.planToJira.design;


import javax.swing.table.AbstractTableModel;

import core.SharedObjs;
import objects.CustomFilterItem;
import objects.CustomFiltersList;


@SuppressWarnings({"serial"})
public class CRsTableModel extends AbstractTableModel
{
	public static final int     ID_INDEX               = 0;
	public static final int     KEY_INDEX              = 1;
	public static final int     SUMMARY_INDEX          = 2;
	public static final int     STATUS_INDEX           = 3;
	public static final int     RESOLUTION_INDEX       = 4;
	public static final int     ASSIGNEE_INDEX         = 5;
	public static final int     DUPS_INDEX             = 6;
	public static final int     COMPONENT_INDEX        = 7;
	public static final int     LABELS_INDEX           = 8;
	public static final int     AFFECTED_VERSION_INDEX = 9;
	public static final int     CREATED_INDEX          = 10;
	public static final int     UPDATED_INDEX          = 11;
	public static final int     HIDDEN_INDEX           = 12;
	
	protected String[]          columnNames;
	protected CustomFiltersList dataVector;
	
	/**
	 * @param columnNames
	 */
	public CRsTableModel(String[] columnNames)
	{
		dataVector = new CustomFiltersList();
		this.columnNames = columnNames;
	}
	
	/**
	 * 
	 */
	public CRsTableModel()
	{
		dataVector = new CustomFiltersList();
		this.columnNames = new String[] {
		        "#", "Key", "Summary", "Status", "Resolution", "Assignee", "Dups", "Component", "Labels",
		        "Affects Version", "Created", "Updated", ""};
	}
	
	@Override
	public String getColumnName(int column)
	{
		return columnNames[column];
	}
	
	@Override
	public boolean isCellEditable(int row, int column)
	{
		if (column == SUMMARY_INDEX || column == STATUS_INDEX || column == ASSIGNEE_INDEX || column == LABELS_INDEX)
			return true;
		else 
			return false;
	}
	
	@Override
	public Class<?> getColumnClass(int column)
	{
		switch (column)
		{
			case ID_INDEX:
				return Integer.class;
			case 99:
				return Boolean.class;
			default:
				return String.class;
		}
	}
	
	@Override
	public Object getValueAt(int row, int column)
	{
		CustomFilterItem record = (CustomFilterItem) dataVector.get(row);
		switch (column)
		{
			case ID_INDEX:
				return dataVector.indexOf(record);
			case KEY_INDEX:
				return record.getName();
			case SUMMARY_INDEX:
				return record.getRegex();
			case STATUS_INDEX:
				return record.getHeader();
			case RESOLUTION_INDEX:
				return record.isMain();
			case ASSIGNEE_INDEX:
				return record.isSystem();
			case DUPS_INDEX:
				return record.isKernel();
			case COMPONENT_INDEX:
				return record.isRadio();
			case LABELS_INDEX:
				return record.isBugreport();
			case AFFECTED_VERSION_INDEX:
				return record.isRoutput();
			case CREATED_INDEX:
				return record.isShared();
			case UPDATED_INDEX:
				return record.isPublic();
			default:
				return new Object();
		}
	}
	
	@Override
	public void setValueAt(Object value, int row, int column)
	{
		CustomFilterItem record = (CustomFilterItem) dataVector.get(row);
		switch (column)
		{
			case ID_INDEX:
				dataVector.indexOf(record);
			case KEY_INDEX:
				record.setName((String) value);
				break;
			case SUMMARY_INDEX:
				record.setName((String) value);
				break;
			case STATUS_INDEX:
				record.setRegex((String) value);
				break;
			case RESOLUTION_INDEX:
				record.setHeader((String) value);
				break;
			case ASSIGNEE_INDEX:
				record.setMain((Boolean) value);
				break;
			case DUPS_INDEX:
				record.setSystem((Boolean) value);
				break;
			case COMPONENT_INDEX:
				record.setKernel((Boolean) value);
				break;
			case LABELS_INDEX:
				record.setRadio((Boolean) value);
				break;
			case AFFECTED_VERSION_INDEX:
				record.setBugreport((Boolean) value);
				break;
			case CREATED_INDEX:
				record.setRoutput((Boolean) value);
				break;
			case UPDATED_INDEX:
				record.setShared((Boolean) value);
				break;
			default:
				System.out.println("invalid index");
		}
		fireTableCellUpdated(row, column);
	}
	
	@Override
	public int getRowCount()
	{
		return dataVector.size();
	}
	
	@Override
	public int getColumnCount()
	{
		return columnNames.length;
	}
	
	public boolean hasEmptyRow()
	{
		if (dataVector.size() == 0)
			return false;
		
		CustomFilterItem filterItem = (CustomFilterItem) dataVector.get(dataVector.size() - 1);
		
		if (filterItem.getName().trim().equals(""))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public boolean hasFilterName(CustomFilterItem lookForFilter)
	{
		if (dataVector.size() == 0)
			return false;
		CustomFilterItem filter;
		
		for (int i = 0; i < dataVector.size() - 1; i++)
		{
			filter = dataVector.get(i);
			if (filter.getName().trim().equals(lookForFilter.getName())
			    && !filter.getLastUpdate().equals(lookForFilter.getLastUpdate()))
			{
				return true;
			}
		}
		
		return false;
	}
	
	public void addEmptyRow()
	{
		CustomFilterItem newItem = new CustomFilterItem();
		newItem.setOwner(SharedObjs.getUser());
		dataVector.add(newItem);
		fireTableRowsInserted(dataVector.size() - 1, dataVector.size() - 1);
	}
	
	public void addRow(CustomFilterItem filter)
	{
		dataVector.add(filter);
		fireTableRowsInserted(dataVector.size() - 1, dataVector.size() - 1);
	}
	
	public void removeRow(int id)
	{
		dataVector.remove(id);
		fireTableRowsDeleted(0, dataVector.size());
	}
	
	public CustomFilterItem getElementAt(int row)
	{
		return dataVector.get(row);
	}
	
	public CustomFiltersList getFilterElements()
	{
		return dataVector;
	}
}
