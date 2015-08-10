package style;

import java.awt.Component;
import java.io.File;

import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

public class FileTreeNodeRenderer extends DefaultTreeCellRenderer{
	

	private static final long serialVersionUID = 6608252004318979840L;
	private FileSystemView fileSystemView;
	JLabel nodeLabel;
	
	
	public FileTreeNodeRenderer(){
		fileSystemView = FileSystemView.getFileSystemView();
		nodeLabel = new JLabel();
		nodeLabel.setOpaque(true);
	}
	
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel,
            boolean expanded, boolean leaf, int row, boolean hasFocus) {
		
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
        File file = (File)node.getUserObject();
        
		nodeLabel.setIcon(fileSystemView.getSystemIcon(file));
		nodeLabel.setText(fileSystemView.getSystemDisplayName(file));
		
		if (sel) {
			nodeLabel.setBackground(backgroundSelectionColor);
        	nodeLabel.setForeground(textSelectionColor);
		}
		else {
			nodeLabel.setBackground(backgroundNonSelectionColor);
    		nodeLabel.setForeground(textNonSelectionColor);
		}
		
		return nodeLabel;
		
	}

}
