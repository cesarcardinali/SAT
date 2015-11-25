package tests.planToJira.design;


import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import objects.CrItem;
import objects.CrItemsList;


@SuppressWarnings({"serial"})
public class CRsTableModel extends AbstractTableModel
{
	public static final int ID_INDEX               = 0;
	public static final int KEY_INDEX              = 1;
	public static final int SUMMARY_INDEX          = 2;
	public static final int STATUS_INDEX           = 3;
	public static final int RESOLUTION_INDEX       = 4;
	public static final int ASSIGNEE_INDEX         = 5;
	public static final int DUPS_INDEX             = 6;
	public static final int COMPONENT_INDEX        = 7;
	public static final int LABELS_INDEX           = 8;
	public static final int AFFECTED_VERSION_INDEX = 9;
	public static final int CREATED_INDEX          = 10;
	public static final int UPDATED_INDEX          = 11;
	public static final int HIDDEN_INDEX           = 12;
	
	protected String[]      columnNames;
	protected CrItemsList   dataVector;
	
	/**
	 * @param columnNames
	 */
	public CRsTableModel(String[] columnNames)
	{
		dataVector = new CrItemsList();
		this.columnNames = columnNames;
	}
	
	/**
	 * 
	 */
	public CRsTableModel()
	{
		dataVector = new CrItemsList();
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
		if (column == SUMMARY_INDEX || column == STATUS_INDEX || column == ASSIGNEE_INDEX
		    || column == LABELS_INDEX)
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
		CrItem record = (CrItem) dataVector.get(row);
		switch (column)
		{
			case ID_INDEX:
				return dataVector.indexOf(record);
			case KEY_INDEX:
				return record.getJiraID();
			case SUMMARY_INDEX:
				return record.getSummary();
			case STATUS_INDEX:
				return record.getStatus();
			case RESOLUTION_INDEX:
				return record.getResolution();
			case ASSIGNEE_INDEX:
				return record.getAssignee();
			case DUPS_INDEX:
				return record.getDup();
			case COMPONENT_INDEX:
				return record.getComponent();
			case LABELS_INDEX:
				return record.getLabels();
			case AFFECTED_VERSION_INDEX:
				return record.getAffectedVersion();
			case CREATED_INDEX:
				return record.getCreated();
			case UPDATED_INDEX:
				return record.getUpdated();
			default:
				return new Object();
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void setValueAt(Object value, int row, int column)
	{
		CrItem record = (CrItem) dataVector.get(row);
		switch (column)
		{
			case ID_INDEX:
				dataVector.indexOf(record);
			case KEY_INDEX:
				record.setJiraID((String) value);
				break;
			case SUMMARY_INDEX:
				record.setSummary((String) value);
				break;
			case STATUS_INDEX:
				record.setStatus((String) value);
				break;
			case RESOLUTION_INDEX:
				record.setResolution((String) value);
				break;
			case ASSIGNEE_INDEX:
				record.setAssignee((String) value);
				break;
			case DUPS_INDEX:
				record.setDup((String) value);
				break;
			case COMPONENT_INDEX:
				record.setComponent((String) value);
				break;
			case LABELS_INDEX:
				record.setLabels((ArrayList<String>) value);
				break;
			case AFFECTED_VERSION_INDEX:
				record.setAffectedVersion((String) value);
				break;
			case CREATED_INDEX:
				record.setCreated((String) value);
				break;
			case UPDATED_INDEX:
				record.setUpdated((String) value);
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
		
		CrItem filterItem = (CrItem) dataVector.get(dataVector.size() - 1);
		
		if (filterItem.getJiraID().trim().equals(""))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public boolean hasFilterName(CrItem lookForFilter)
	{
		if (dataVector.size() == 0)
			return false;
		CrItem filter;
		
		for (int i = 0; i < dataVector.size() - 1; i++)
		{
			filter = dataVector.get(i);
			if (filter.getJiraID().trim().equals(lookForFilter.getJiraID())
			    && !filter.getSummary().equals(lookForFilter.getSummary()))
			{
				return true;
			}
		}
		
		return false;
	}
	
	public void addEmptyRow()
	{
		CrItem newItem = new CrItem();
		dataVector.add(newItem);
		fireTableRowsInserted(dataVector.size() - 1, dataVector.size() - 1);
	}
	
	public void addRow(CrItem filter)
	{
		dataVector.add(filter);
		fireTableRowsInserted(dataVector.size() - 1, dataVector.size() - 1);
	}
	
	public void removeRow(int id)
	{
		dataVector.remove(id);
		fireTableRowsDeleted(0, dataVector.size());
	}
	
	public CrItem getElementAt(int row)
	{
		return dataVector.get(row);
	}
	
	public CrItemsList getFilterElements()
	{
		return dataVector;
	}
}
