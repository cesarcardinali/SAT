package tests.planToJira.design;


import javax.swing.table.AbstractTableModel;

import tests.planToJira.complexObjects.PlanRow;
import tests.planToJira.complexObjects.PlanRowList;


@SuppressWarnings({"serial"})
public class PlanTableModel extends AbstractTableModel
{
	public static final int ID_INDEX         = 0;
	public static final int KEY_INDEX        = 1;
	public static final int PRODUCT_INDEX    = 2;
	public static final int BUILD_INDEX      = 3;
	public static final int OWNER_INDEX      = 4;
	public static final int ISSUE_INDEX      = 5;
	public static final int RESOLUTION_INDEX = 6;
	public static final int DATE_INDEX       = 7;
	public static final int HIDDEN_INDEX     = 8;
	
	protected String[]      columnNames;
	protected PlanRowList   dataVector;
	
	/**
	 * @param columnNames
	 */
	public PlanTableModel(String[] columnNames)
	{
		dataVector = new PlanRowList();
		this.columnNames = columnNames;
	}
	
	public PlanTableModel()
	{
		dataVector = new PlanRowList();
		this.columnNames = new String[] {
		        "#", "Key", "Product", "Build", "Owner", "Issue", "Resolution", "Date", ""};
	}
	
	@Override
	public String getColumnName(int column)
	{
		return columnNames[column];
	}
	
	@Override
	public boolean isCellEditable(int row, int column)
	{
		return true;
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
		PlanRow record = (PlanRow) dataVector.get(row);
		switch (column)
		{
			case ID_INDEX:
				return dataVector.indexOf(record);
			case KEY_INDEX:
				return record.getKey();
			case PRODUCT_INDEX:
				return record.getProduct();
			case BUILD_INDEX:
				return record.getBuild();
			case OWNER_INDEX:
				return record.getOwner();
			case ISSUE_INDEX:
				return record.getIssue();
			case RESOLUTION_INDEX:
				return record.getResolution();
			case DATE_INDEX:
				return record.getDate();
			default:
				return new Object();
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void setValueAt(Object value, int row, int column)
	{
		PlanRow record = (PlanRow) dataVector.get(row);
		switch (column)
		{
			case ID_INDEX:
				dataVector.indexOf(record);
			case KEY_INDEX:
				record.setKey((String) value);
				break;
			case PRODUCT_INDEX:
				record.setProduct((String) value);
				break;
			case BUILD_INDEX:
				record.setBuild((String) value);
				break;
			case OWNER_INDEX:
				record.setOwner((String) value);
				break;
			case ISSUE_INDEX:
				record.setIssue((String) value);
				break;
			case RESOLUTION_INDEX:
				record.setResolution((String) value);
				break;
			case DATE_INDEX:
				record.setDate((String) value);
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
		
		PlanRow filterItem = (PlanRow) dataVector.get(dataVector.size() - 1);
		
		if (filterItem.getKey().trim().equals(""))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public void addEmptyRow()
	{
		PlanRow newItem = new PlanRow();
		dataVector.add(newItem);
		fireTableRowsInserted(dataVector.size() - 1, dataVector.size() - 1);
	}
	
	public void addRow(PlanRow filter)
	{
		dataVector.add(filter);
		fireTableRowsInserted(dataVector.size() - 1, dataVector.size() - 1);
	}
	
	public void removeRow(int id)
	{
		dataVector.remove(id);
		fireTableRowsDeleted(0, dataVector.size());
	}
	
	public PlanRow getElementAt(int row)
	{
		return dataVector.get(row);
	}
	
	public PlanRowList getFilterElements()
	{
		return dataVector;
	}
}
