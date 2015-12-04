package tests.planToJira.design;


import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableCellRenderer;

import style.Colors;


@SuppressWarnings("serial")
public class CRsTableStringCellRenderer extends DefaultTableCellRenderer
{
	
	public CRsTableStringCellRenderer()
	{
		super();
	}
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
												   boolean hasFocus, int row, int column)
	{
		Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		JLabel val = (JLabel) c;
		
		if (isSelected)
		{
			setForeground(table.getSelectionForeground());
			setBackground(table.getSelectionBackground());
		}
		else
		{
			if (row % 2 == 1)
			{
				setForeground(table.getForeground());
				setBackground(Colors.cinzaClaro);
			}
			else
			{
				setForeground(table.getForeground());
				setBackground(table.getBackground());
			}
		}
		
		// Exclusive mods for tables using CRsTableModel
//		if (table.getModel().getClass().equals(PlanTableModel.class))
//		{
//			if (isSelected && hasFocus && column == PlanTableModel.HIDDEN_INDEX)
//			{
//				if ((table.getModel().getRowCount() - 1) == row
//					&& !((PlanTableModel) table.getModel()).hasEmptyRow())
//				{
//					((PlanTableModel) table.getModel()).addEmptyRow();
//					table.setRowSelectionInterval(table.getRowCount() - 1, table.getRowCount() - 1);
//					table.setColumnSelectionInterval(table.getColumnCount() - 1, 0);
//				}
//			}
//		}
		
		return c;
	}
	
	public void tableChanged(TableModelEvent evt)
	{
		if (evt.getType() == TableModelEvent.UPDATE)
		{
			int column = evt.getColumn();
			int row = evt.getFirstRow();
			
			System.out.println("row: " + row + " column: " + column);
			
			/*
			 * Focus next cell table.setColumnSelectionInterval(column + 1, column + 1); table.setRowSelectionInterval(row, row);
			 */
			
		}
	}
	
}