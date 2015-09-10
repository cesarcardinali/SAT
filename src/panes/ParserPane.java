package panes;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.UndoManager;

import style.NonWrappingTextPane;
import core.Logger;
import core.SharedObjs;
import core.XmlMngr;
import filters.Alarm;
import filters.B2G;
import filters.Consume;
import filters.Diag;
import filters.Issue;
import filters.Normal;
import filters.Suspicious;
import filters.Tether;


@SuppressWarnings("serial")
public class ParserPane extends JPanel
{
	private UndoManager         undoManager;
	private JSplitPane          splitPane;
	private FileTree            fileTree;
	private FiltersResultsTree  filtersResultsTree;
	private NonWrappingTextPane resultTxtPane;
	
	/**
	 * Create the panel.
	 */
	public ParserPane()
	{
		setMinimumSize(new Dimension(800, 600));
		GridBagLayout layout = new GridBagLayout();
		layout.columnWidths = new int[] {250, 600};
		layout.rowHeights = new int[] {30, 300};
		layout.rowWeights = new double[] {0.0, 1.0};
		layout.columnWeights = new double[] {1.0, 1.0};
		setLayout(layout);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setToolTipText("Result of the selected parser item on the left");
		scrollPane.setFont(new Font("Consolas", Font.PLAIN, 12));
		scrollPane.setBorder(new LineBorder(new Color(220, 220, 220)));
		
		scrollPane.setAutoscrolls(true);
		scrollPane.setRequestFocusEnabled(false);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setPreferredSize(new Dimension(500, 500));
		scrollPane.setMinimumSize(new Dimension(400, 400));
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);
		
		splitPane = new JSplitPane();
		splitPane.setDividerSize(8);
		splitPane.setBorder(new LineBorder(Color.LIGHT_GRAY));
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		
		fileTree = new FileTree();
		fileTree.setBorder(new LineBorder(new Color(192, 192, 192)));
		splitPane.setRightComponent(fileTree);
		JScrollPane scrollFiltersResults = new JScrollPane();
		scrollFiltersResults.setBorder(null);
		filtersResultsTree = new FiltersResultsTree();
		filtersResultsTree.setBorder(new LineBorder(Color.LIGHT_GRAY));
		scrollFiltersResults.setViewportView(filtersResultsTree);
		scrollFiltersResults.setMinimumSize(new Dimension(150, 150));
		splitPane.setLeftComponent(scrollFiltersResults);
		GridBagConstraints gbc_splitPane = new GridBagConstraints();
		gbc_splitPane.insets = new Insets(0, 0, 0, 5);
		gbc_splitPane.fill = GridBagConstraints.BOTH;
		gbc_splitPane.gridx = 0;
		gbc_splitPane.gridy = 1;
		add(splitPane, gbc_splitPane);
		splitPane.setDividerLocation(300);
		
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 10, 10, 10);
		gbc_scrollPane.weighty = 22.0;
		gbc_scrollPane.weightx = 15.0;
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 1;
		gbc_scrollPane.gridy = 1;
		add(scrollPane, gbc_scrollPane);
		
		resultTxtPane = new NonWrappingTextPane();
		resultTxtPane.setBorder(new LineBorder(Color.LIGHT_GRAY));
		resultTxtPane.setToolTipText("Result of the selected parser item on the left");
		resultTxtPane.setContentType("text/plain");
		resultTxtPane.setMargin(new Insets(7, 2, 7, 2));
		resultTxtPane.setForeground(new Color(0, 0, 0));
		resultTxtPane.setFont(new Font("Consolas", Font.PLAIN, 11));
		resultTxtPane.setText("");
		
		undoManager = new UndoManager();
		resultTxtPane.getDocument().addUndoableEditListener(new UndoableEditListener()
		{
			@Override
			public void undoableEditHappened(UndoableEditEvent e)
			{
				undoManager.addEdit(e.getEdit());
			}
		});
		
		resultTxtPane.addKeyListener(new KeyListener()
		{
			@Override
			public void keyReleased(KeyEvent arg0)
			{
				saveTextChanges(filtersResultsTree.getLastSelectedPathComponent(), resultTxtPane.getText());
			}
			
			@Override
			public void keyTyped(KeyEvent arg0)
			{
			}
			
			@Override
			public void keyPressed(KeyEvent e)
			{
				if (e.isControlDown() && e.isShiftDown() && e.getKeyCode() == 67)
				{
					copyAll();
				}
				
				if ((e.getKeyCode() == KeyEvent.VK_Z) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0))
				{
					try
					{
						undoManager.undo();
					}
					catch (CannotRedoException cre)
					{
						cre.printStackTrace();
					}
				}
			}
		});
		
		resultTxtPane.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				if (SwingUtilities.isRightMouseButton(e))
				{
				}
			}
		});
		
		scrollPane.setViewportView(resultTxtPane);
		SharedObjs.setResult("");
		resultTxtPane.setText("");
		
		loadPaneData();
	}
	
	/**
	 * Save pane data
	 */
	public void savePaneData()
	{
		XmlMngr.setUserValueOf(new String[] {"parser_pane", "rootPath"}, SharedObjs.getRootFolderPath());
		
		Logger.log(Logger.TAG_PARSER, "Parser data saved");
	}
	
	/**
	 * 
	 */
	private void loadPaneData()
	{
		
	}
	
	/**
	 * Save textPane text user edited
	 * 
	 * @param node
	 * @param text
	 */
	private void saveTextChanges(Object node, String text)
	{
		String selectedNode = node.toString().toLowerCase();
		String selectedNodeParent = ((DefaultMutableTreeNode) node).getParent().toString().toLowerCase();
		
		if ((selectedNode.contains("colors") && selectedNodeParent.contains("alarms "))
		    || selectedNode.contains("alarms "))
			Alarm.updateResult(text);
		if ((selectedNode.contains("colors") && selectedNodeParent.contains(" consum"))
		    || selectedNode.contains(" consum"))
			Consume.updateResult(text);
		if (selectedNode.contains("diag ") || selectedNodeParent.contains("diag "))
			Diag.updateResult(text);
		if (selectedNode.contains("suspicious"))
			Suspicious.updateResult(text);
		if (selectedNode.contains("tether") || selectedNodeParent.contains("tether"))
			Tether.updateResult(text);
		if (selectedNode.contains("summary") || selectedNodeParent.contains("summary"))
			Normal.updateResult(text);
		if (selectedNode.contains(" issues") || selectedNodeParent.contains(" issues"))
			Issue.updateResult(text);
		if (selectedNode.contains("bug2go") || selectedNodeParent.contains("bug2go"))
		{
			B2G.updateResult(text);
			B2G.setEdited(true);
		}
	}
	
	/**
	 * Reset pane UI to initial state
	 */
	public void clearPane()
	{
		filtersResultsTree.clearTree();
		resultTxtPane.setText(""); // reset the text pane
		SharedObjs.setResult(""); // reset the result for the filters
	}
	
	private void copyAll()
	{
		Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
		StringSelection stringSelection = new StringSelection(resultTxtPane.getText());
		clpbrd.setContents(stringSelection, null);
	}
	
	/**
	 * Show all log results on the results pane
	 */
	public void showAllLogResults()
	{
		while (SharedObjs.getResult().charAt(0) == '\n')
		{
			SharedObjs.setResult(SharedObjs.getResult().substring(1));
		}
		resultTxtPane.setText(SharedObjs.getResult());
		resultTxtPane.setCaretPosition(0);
	}
	
	// Getters and Setters
	public FiltersResultsTree getFiltersResultsTree()
	{
		return filtersResultsTree;
	}
	
	public void setResultsPaneTxt(String text)
	{
		resultTxtPane.setText(text);
		resultTxtPane.setCaretPosition(0);
	}
	
	public NonWrappingTextPane getResultsTxtPane()
	{
		return resultTxtPane;
	}
}
