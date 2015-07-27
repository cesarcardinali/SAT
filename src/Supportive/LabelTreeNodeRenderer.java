package Supportive;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

@SuppressWarnings("serial")
public class LabelTreeNodeRenderer extends DefaultTreeCellRenderer{
	
	JLabel label;
	
	public LabelTreeNodeRenderer(){
		label = new JLabel();
		label.setOpaque(true);
		label.setPreferredSize(new Dimension(180, 20));
		label.setMinimumSize(new Dimension(100, 20));
	}
	
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {

		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
		
        if (node.isRoot()) {
            label.setIcon(new ImageIcon("Data\\pics\\root.png"));
            label.setText(node.toString());
            
        } else if(node.getLevel() == 1) {
	        	if (node.toString().contains("Alarms")) {
	        	label.setIcon(new ImageIcon("Data\\pics\\alarm.png"));
	            label.setText(node.toString());
	            
	        } else if (node.toString().contains("Bug2Go")) {
	        	label.setIcon(new ImageIcon("Data\\pics\\bug2go.png"));
	            label.setText(node.toString());
	            
	        } else if (node.toString().contains("High Consumption")) {
	        	label.setIcon(new ImageIcon("Data\\pics\\consumption.png"));
	            label.setText(node.toString());
	            
	        } else if (node.toString().contains("Diag")) {
	        	label.setIcon(new ImageIcon("Data\\pics\\diag.png"));
	            label.setText(node.toString());
	            
	        } else if (node.toString().contains("WakeLocks")) {
	        	label.setIcon(new ImageIcon("Data\\pics\\suspiciouswakelocks.png"));
	            label.setText(node.toString());
	            
	        } else if (node.toString().contains("Summary")) {
	        	label.setIcon(new ImageIcon("Data\\pics\\summary.png"));
	            label.setText(node.toString());
	            
	        } else if (node.toString().contains("Suspicious")) {
	        	label.setIcon(new ImageIcon("Data\\pics\\suspicious.png"));
	            label.setText(node.toString());
	            
	        } else if (node.toString().contains("Tethering")) {
	        	label.setIcon(new ImageIcon("Data\\pics\\tether.png"));
	            label.setText(node.toString());
	            
	        } else if (node.toString().equals("On Colors")) {
	        	label.setIcon(new ImageIcon("Data\\pics\\colors.png"));
	            label.setText(node.toString());
	            
	        } else {
	        	label.setIcon(new ImageIcon("Data\\pics\\custom.png"));
	        	label.setText(node.toString());
	        }
	        	
        } else if(node.getLevel() == 2 || node.getLevel() == 3) {
        	if (node.toString().equals("Screen ON")) {
            	label.setIcon(new ImageIcon("Data\\pics\\on.png"));
                label.setText(node.toString());
                
            } else if (node.toString().equals("Screen OFF")) {
            	label.setIcon(new ImageIcon("Data\\pics\\off.png"));
                label.setText(node.toString() + "     "
                		+ "                  ");
            } else if (node.toString().equals("Full Log")) {
            	label.setIcon(new ImageIcon("Data\\pics\\onoff.png"));
                label.setText(node.toString());
                
            } else if (node.toString().equals("On Colors")) {
            	label.setIcon(new ImageIcon("Data\\pics\\colors.png"));
                label.setText(node.toString());
                
            } else {
            	label.setIcon(new ImageIcon(""));
            	label.setText(node.toString());
            	
            }
        }
        

        if (sel) {
			label.setBackground(backgroundSelectionColor);
			label.setForeground(textSelectionColor);
			label.setFont(new Font("Tahoma", Font.BOLD, 11));
		}
		else {
			label.setBackground(backgroundNonSelectionColor);
			label.setForeground(textNonSelectionColor);
			label.setFont(new Font("Tahoma", Font.PLAIN, 11));
		}
        
        label.setPreferredSize(new Dimension(label.getText().length()*11, 20));
        if(label.getText().length() < 5)
        	label.setPreferredSize(new Dimension(120, 20));

        return label;
		
	}

}
