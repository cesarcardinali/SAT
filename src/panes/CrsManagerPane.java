package panes;


import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URI;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.TimeZone;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.json.simple.parser.ParseException;

import core.Logger;
import core.SharedObjs;
import core.XmlMngr;
import objects.CrItem;
import supportive.Bug2goDownloader;
import supportive.CrsCloser;
import supportive.JiraSatApi;


@SuppressWarnings("serial")
public class CrsManagerPane extends JPanel
{
	/**
	 * Global Variables
	 */
	private JTextArea	textDownload;
	private JTextField	textPath;
	private JTextPane	textLog;
	private JTextPane	textPane;
	private JCheckBox	chckbxAssign;
	private JCheckBox	chckbxLabels;
	private String		CRs[];
	private String		labels[];
	private JTextField	textLabel;
	private int errors;
	private JButton btnDownload;
	
	/**
	 * Create the panel.
	 */
	public CrsManagerPane()
	{
		setPreferredSize(new Dimension(632, 765));
		setMinimumSize(new Dimension(600, 950));
		
		// Panel construction
		JPanel contentPane = new JPanel();
		add(contentPane);
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {0, 0, 0};
		gridBagLayout.rowHeights = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[] {1.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[] {0.0,
												 0.0,
												 0.0,
												 0.0,
												 0.0,
												 0.0,
												 0.0,
												 1.0,
												 0.0,
												 Double.MIN_VALUE};
		contentPane.setLayout(gridBagLayout);
		
		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(10, 5, 5, 5);
		gbc_panel.fill = GridBagConstraints.VERTICAL;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		contentPane.add(panel, gbc_panel);
		
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] {30, 0};
		gbl_panel.rowHeights = new int[] {22, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panel.columnWeights = new double[] {0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		JLabel lblRootPath = new JLabel("Download path:");
		GridBagConstraints gbc_lblRootPath = new GridBagConstraints();
		gbc_lblRootPath.insets = new Insets(0, 0, 5, 0);
		gbc_lblRootPath.gridx = 0;
		gbc_lblRootPath.gridy = 0;
		panel.add(lblRootPath, gbc_lblRootPath);
		
		lblRootPath.setHorizontalAlignment(SwingConstants.CENTER);
		lblRootPath.setFont(new Font("Tahoma", Font.BOLD, 18));
		textPath = new JTextField();
		textPath.setToolTipText("Path to stock and read your CRs");
		textPath.setHorizontalAlignment(SwingConstants.CENTER);
		textPath.setBorder(new LineBorder(SystemColor.activeCaption));
		textPath.setMinimumSize(new Dimension(130, 20));
		textPath.setPreferredSize(new Dimension(150, 20));
		textPath.getDocument().addDocumentListener(new DocumentListener()
		{
			@Override
			public void removeUpdate(DocumentEvent arg0)
			{
				SharedObjs.setDownloadPath(textPath.getText());
			}
			
			@Override
			public void insertUpdate(DocumentEvent arg0)
			{
				SharedObjs.setDownloadPath(textPath.getText());
			}
			
			@Override
			public void changedUpdate(DocumentEvent arg0)
			{
			}
		});
		GridBagConstraints gbc_textPath = new GridBagConstraints();
		gbc_textPath.insets = new Insets(0, 5, 5, 0);
		gbc_textPath.gridx = 0;
		gbc_textPath.gridy = 1;
		panel.add(textPath, gbc_textPath);
		
		btnDownload = new JButton("Get the CRs");
		GridBagConstraints gbc_btnDownload = new GridBagConstraints();
		gbc_btnDownload.insets = new Insets(40, 0, 5, 0);
		gbc_btnDownload.gridx = 0;
		gbc_btnDownload.gridy = 2;
		panel.add(btnDownload, gbc_btnDownload);
		btnDownload.setToolTipText("Start to download the CRs on the list above");
		btnDownload.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				btnDownloadAction();
			}
		});
		btnDownload.setPreferredSize(new Dimension(113, 23));
		btnDownload.setMaximumSize(new Dimension(113, 23));
		btnDownload.setMinimumSize(new Dimension(113, 23));
		
		JButton btnCloseAsOld = new JButton("Close as Old");
		GridBagConstraints gbc_btnCloseAsOld = new GridBagConstraints();
		gbc_btnCloseAsOld.insets = new Insets(0, 0, 5, 0);
		gbc_btnCloseAsOld.gridx = 0;
		gbc_btnCloseAsOld.gridy = 3;
		panel.add(btnCloseAsOld, gbc_btnCloseAsOld);
		btnCloseAsOld.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				CrsCloser closer = new CrsCloser(textDownload.getText().split("\n"));
				new Thread(closer).start();
			}
		});
		btnCloseAsOld.setPreferredSize(new Dimension(113, 23));
		btnCloseAsOld.setMinimumSize(new Dimension(113, 23));
		btnCloseAsOld.setMaximumSize(new Dimension(113, 23));
		
		JButton btnUnassign = new JButton("Unassign CRs");
		GridBagConstraints gbc_btnUnassign = new GridBagConstraints();
		gbc_btnUnassign.insets = new Insets(0, 0, 5, 0);
		gbc_btnUnassign.gridx = 0;
		gbc_btnUnassign.gridy = 4;
		panel.add(btnUnassign, gbc_btnUnassign);
		btnUnassign.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SharedObjs.crsManagerPane.addLogLine("Unassigning CRs ...");
				new Thread(new Runnable()
				{
					@Override
					public void run()
					{
						String crs[] = textDownload.getText().split("\n");
						JiraSatApi jira = new JiraSatApi(JiraSatApi.DEFAULT_JIRA_URL, SharedObjs.getUser(), SharedObjs.getPass());
						for(String cr : crs)
						{
							jira.unassignIssue(cr);
						}
						SharedObjs.crsManagerPane.addLogLine("Unassigning CRs - DONE");
					}
				}).start();
			}
		});
		btnUnassign.setToolTipText("Start to download the CRs on the list above");
		btnUnassign.setPreferredSize(new Dimension(113, 23));
		btnUnassign.setMinimumSize(new Dimension(113, 23));
		btnUnassign.setMaximumSize(new Dimension(113, 23));
		
		JButton btnRemoveLabel = new JButton("Remove Labels");
		btnRemoveLabel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SharedObjs.crsManagerPane.addLogLine("Removing labels ...");
				new Thread(new Runnable()
				{
					@Override
					public void run()
					{
						String crs[] = textDownload.getText().split("\n");
						String labels[] = getLabels();
						JiraSatApi jira = new JiraSatApi(JiraSatApi.DEFAULT_JIRA_URL, SharedObjs.getUser(), SharedObjs.getPass());
						for(String cr : crs)
						{
							CrItem crData;
							try
							{
								crData = jira.getCrData(cr);
								System.out.println("Assign: " + crData.getAssignee());
								if(crData.getAssignee().equals("null"))
								{
									System.out.println("Assign: null");
									jira.assignIssue(cr);
									jira.removeLabel(cr, labels);
									jira.unassignIssue(cr);
								}
								else
								{
									jira.assignIssue(cr);
									jira.removeLabel(cr, labels);
								}
							}
							catch (ParseException e)
							{
								System.out.println("Unable to get CR data");
								e.printStackTrace();
							}
						}
						
						SharedObjs.crsManagerPane.addLogLine("Removing labels - DONE");
					}
				}).start();
			}
		});
		btnRemoveLabel.setToolTipText("Start to download the CRs on the list above");
		btnRemoveLabel.setPreferredSize(new Dimension(113, 23));
		btnRemoveLabel.setMinimumSize(new Dimension(113, 23));
		btnRemoveLabel.setMaximumSize(new Dimension(113, 23));
		GridBagConstraints gbc_btnRemoveLabel = new GridBagConstraints();
		gbc_btnRemoveLabel.insets = new Insets(0, 0, 5, 0);
		gbc_btnRemoveLabel.gridx = 0;
		gbc_btnRemoveLabel.gridy = 5;
		panel.add(btnRemoveLabel, gbc_btnRemoveLabel);
		
		JButton btnOpenOnChrome = new JButton("Open on Chrome");
		GridBagConstraints gbc_btnOpenOnChrome = new GridBagConstraints();
		gbc_btnOpenOnChrome.insets = new Insets(0, 0, 5, 0);
		gbc_btnOpenOnChrome.gridx = 0;
		gbc_btnOpenOnChrome.gridy = 6;
		panel.add(btnOpenOnChrome, gbc_btnOpenOnChrome);
		btnOpenOnChrome.setToolTipText("Open the CRs on the list above on Chrome");
		
		JButton btnLists = new JButton("Reopen Result Lists");
		btnLists.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SharedObjs.getClosedList().setVisible(true);
				SharedObjs.getOpenedList().setVisible(true);
			}
		});
		btnLists.setToolTipText("Open the CRs on the list above on Chrome");
		GridBagConstraints gbc_btnLists = new GridBagConstraints();
		gbc_btnLists.gridx = 0;
		gbc_btnLists.gridy = 8;
		panel.add(btnLists, gbc_btnLists);
		btnOpenOnChrome.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				btnOpenAction();
			}
		});
		
		JPanel panel_3 = new JPanel();
		panel_3.setMinimumSize(new Dimension(150, 10));
		GridBagConstraints gbc_panel_3 = new GridBagConstraints();
		gbc_panel_3.gridheight = 6;
		gbc_panel_3.insets = new Insets(10, 5, 5, 0);
		gbc_panel_3.fill = GridBagConstraints.VERTICAL;
		gbc_panel_3.gridx = 1;
		gbc_panel_3.gridy = 0;
		contentPane.add(panel_3, gbc_panel_3);
		
		GridBagLayout gbl_panel_3 = new GridBagLayout();
		gbl_panel_3.columnWidths = new int[] {0, 0};
		gbl_panel_3.rowHeights = new int[] {0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panel_3.columnWeights = new double[] {0.0, Double.MIN_VALUE};
		gbl_panel_3.rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel_3.setLayout(gbl_panel_3);
		JLabel lblDownloader = new JLabel("Downloader:");
		lblDownloader.setHorizontalAlignment(SwingConstants.CENTER);
		lblDownloader.setFont(new Font("Tahoma", Font.BOLD, 18));
		GridBagConstraints gbc_lblDownloader = new GridBagConstraints();
		gbc_lblDownloader.insets = new Insets(0, 0, 5, 0);
		gbc_lblDownloader.gridx = 0;
		gbc_lblDownloader.gridy = 0;
		panel_3.add(lblDownloader, gbc_lblDownloader);
		
		chckbxAssign = new JCheckBox("Assign?");
		chckbxAssign.setMargin(new Insets(0, 2, 0, 2));
		chckbxAssign.setPreferredSize(new Dimension(61, 15));
		chckbxAssign.setMinimumSize(new Dimension(15, 20));
		chckbxAssign.setSelected(true);
		GridBagConstraints gbc_chckbxAssign = new GridBagConstraints();
		gbc_chckbxAssign.insets = new Insets(0, 0, 5, 0);
		gbc_chckbxAssign.gridx = 0;
		gbc_chckbxAssign.gridy = 1;
		panel_3.add(chckbxAssign, gbc_chckbxAssign);
		
		chckbxLabels = new JCheckBox("Labels?");
		chckbxLabels.setMargin(new Insets(0, 2, 0, 2));
		chckbxLabels.setMinimumSize(new Dimension(61, 15));
		chckbxLabels.setPreferredSize(new Dimension(61, 15));
		chckbxLabels.setSelected(true);
		GridBagConstraints gbc_chckbxLabels = new GridBagConstraints();
		gbc_chckbxLabels.insets = new Insets(0, 0, 5, 0);
		gbc_chckbxLabels.gridx = 0;
		gbc_chckbxLabels.gridy = 2;
		panel_3.add(chckbxLabels, gbc_chckbxLabels);
		
		textLabel = new JTextField();
		textLabel.setText("ll_prodteam_analyzed");
		GridBagConstraints gbc_textLabel = new GridBagConstraints();
		gbc_textLabel.insets = new Insets(0, 10, 5, 10);
		gbc_textLabel.fill = GridBagConstraints.HORIZONTAL;
		gbc_textLabel.gridx = 0;
		gbc_textLabel.gridy = 3;
		panel_3.add(textLabel, gbc_textLabel);
		textLabel.setColumns(10);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setPreferredSize(new Dimension(140, 350));
		scrollPane_2.setBorder(new LineBorder(SystemColor.activeCaption));
		scrollPane_2.setMinimumSize(new Dimension(140, 300));
		GridBagConstraints gbc_scrollPane_2 = new GridBagConstraints();
		gbc_scrollPane_2.fill = GridBagConstraints.VERTICAL;
		gbc_scrollPane_2.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane_2.gridx = 0;
		gbc_scrollPane_2.gridy = 4;
		panel_3.add(scrollPane_2, gbc_scrollPane_2);
		
		textDownload = new JTextArea();
		textDownload.setToolTipText("List of CRs to be downloaded. Used to create the link between downloded CRs and its Jira ID too.");
		textDownload.setFont(new Font("Tahoma", Font.PLAIN, 11));
		scrollPane_2.setViewportView(textDownload);
		textDownload.setTabSize(4);
		textDownload.setBorder(null);
		
		JButton btnClear = new JButton("Clear");
		btnClear.setToolTipText("Clear the text area above");
		btnClear.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				btnClearAction();
			}
		});
		btnClear.setPreferredSize(new Dimension(113, 23));
		btnClear.setMaximumSize(new Dimension(113, 23));
		btnClear.setMinimumSize(new Dimension(113, 23));
		GridBagConstraints gbc_btnClear = new GridBagConstraints();
		gbc_btnClear.insets = new Insets(0, 0, 5, 0);
		gbc_btnClear.gridx = 0;
		gbc_btnClear.gridy = 5;
		panel_3.add(btnClear, gbc_btnClear);
		
		JButton btnPaste = new JButton("Paste");
		btnPaste.setToolTipText("Paste from clipboard");
		btnPaste.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				btnPasteAction();
			}
		});
		btnPaste.setMaximumSize(new Dimension(113, 23));
		btnPaste.setPreferredSize(new Dimension(113, 23));
		btnPaste.setMinimumSize(new Dimension(113, 23));
		GridBagConstraints gbc_btnPaste = new GridBagConstraints();
		gbc_btnPaste.gridx = 0;
		gbc_btnPaste.gridy = 6;
		panel_3.add(btnPaste, gbc_btnPaste);
		
		JSeparator separator_4 = new JSeparator();
		separator_4.setForeground(new Color(0, 0, 0));
		GridBagConstraints gbc_separator_4 = new GridBagConstraints();
		gbc_separator_4.gridwidth = 2;
		gbc_separator_4.insets = new Insets(10, 5, 5, 0);
		gbc_separator_4.gridx = 0;
		gbc_separator_4.gridy = 6;
		contentPane.add(separator_4, gbc_separator_4);
		
		JPanel panel_5 = new JPanel();
		panel_5.setBorder(new LineBorder(new Color(240, 128, 128), 1, true));
		GridBagConstraints gbc_panel_5 = new GridBagConstraints();
		gbc_panel_5.gridwidth = 2;
		gbc_panel_5.gridx = 0;
		gbc_panel_5.gridy = 8;
		contentPane.add(panel_5, gbc_panel_5);
		
		GridBagLayout gbl_panel_5 = new GridBagLayout();
		gbl_panel_5.columnWidths = new int[] {0, 0, 0};
		gbl_panel_5.rowHeights = new int[] {0, 0, 0};
		gbl_panel_5.columnWeights = new double[] {0.0, 0.0, Double.MIN_VALUE};
		gbl_panel_5.rowWeights = new double[] {0.0, 0.0, Double.MIN_VALUE};
		panel_5.setLayout(gbl_panel_5);
		
		JLabel lblStatusLog = new JLabel("Status Log:");
		GridBagConstraints gbc_lblStatusLog = new GridBagConstraints();
		gbc_lblStatusLog.anchor = GridBagConstraints.WEST;
		gbc_lblStatusLog.insets = new Insets(0, 10, 5, 5);
		gbc_lblStatusLog.gridx = 0;
		gbc_lblStatusLog.gridy = 0;
		panel_5.add(lblStatusLog, gbc_lblStatusLog);
		
		JLabel lblLastAction = new JLabel("Last Action:");
		GridBagConstraints gbc_lblLastAction = new GridBagConstraints();
		gbc_lblLastAction.anchor = GridBagConstraints.WEST;
		gbc_lblLastAction.insets = new Insets(0, 10, 5, 0);
		gbc_lblLastAction.gridx = 1;
		gbc_lblLastAction.gridy = 0;
		panel_5.add(lblLastAction, gbc_lblLastAction);
		
		JScrollPane scrollPane_3 = new JScrollPane();
		scrollPane_3.setPreferredSize(new Dimension(400, 82));
		scrollPane_3.setMinimumSize(new Dimension(400, 82));
		scrollPane_3.setBorder(new LineBorder(new Color(128, 128, 128)));
		GridBagConstraints gbc_scrollPane_3 = new GridBagConstraints();
		gbc_scrollPane_3.insets = new Insets(0, 0, 0, 5);
		gbc_scrollPane_3.gridx = 0;
		gbc_scrollPane_3.gridy = 1;
		panel_5.add(scrollPane_3, gbc_scrollPane_3);
		
		textLog = new JTextPane();
		scrollPane_3.setViewportView(textLog);
		textLog.setEditable(false);
		textLog.setBorder(null);
		textLog.setPreferredSize(new Dimension(400, 82));
		textLog.setMinimumSize(new Dimension(400, 42));
		
		JScrollPane scrollPane_4 = new JScrollPane();
		scrollPane_4.setPreferredSize(new Dimension(100, 82));
		scrollPane_4.setMinimumSize(new Dimension(100, 82));
		scrollPane_4.setBorder(new LineBorder(new Color(128, 128, 128)));
		
		GridBagConstraints gbc_scrollPane_4 = new GridBagConstraints();
		gbc_scrollPane_4.gridx = 1;
		gbc_scrollPane_4.gridy = 1;
		panel_5.add(scrollPane_4, gbc_scrollPane_4);
		
		textPane = new JTextPane();
		scrollPane_4.setViewportView(textPane);
		textPane.setEditable(false);
		textPane.setBorder(null);
		textPane.setPreferredSize(new Dimension(100, 82));
		textPane.setMinimumSize(new Dimension(50, 42));
		
		loadUserData();
		errors = 0;
	}
	
	/**
	 * Download CRs
	 * 
	 * main download function
	 * @throws ParseException
	 */
	private void downloadCRs() throws ParseException
	{
		//SharedObjs.crsManagerPane.addLogLine("Generating CRs list ...");
		errors = 0;
		
		// Setup jira connection
		JiraSatApi jira = new JiraSatApi(JiraSatApi.DEFAULT_JIRA_URL,
										 SharedObjs.getUser(),
										 SharedObjs.getPass());
										 
		// Get the CRs list
		CRs = textDownload.getText().replaceAll(" ", "").split("\n");
		
		Logger.log(Logger.TAG_CRSMANAGER, "CRs List:" + CRs.length);
		if (CRs.length == 0 || (CRs.length == 1 && !CRs[0].contains("-")))
		{
			SharedObjs.crsManagerPane.addLogLine("CRs list empty");
			btnDownload.setEnabled(true);
			return;
		}
		
		ArrayList<String> b2gList = new ArrayList<String>();
		
		// Get label list
		labels = textLabel.getText().split(" ");
		for (String s : labels)
		{
			Logger.log(Logger.TAG_CRSMANAGER, "Label entered: " + s);
		}
		
		SharedObjs.crsManagerPane.addLogLine("Acquiring CRs data ...");
		SharedObjs.getCrsList().clear();
		
		// Manage CR
		for (String crKey : CRs)
		{
			if (crKey.equals(""))
			{
				SharedObjs.crsManagerPane.addLogLine("CR list is empty");
				return;
			}
			
			CrItem crItem = jira.getCrData(crKey);
			
			if (crItem != null)
			{
				if (chckbxLabels.isSelected())
				{
					jira.assignIssue(crKey);
					jira.addLabel(crKey, labels);
					
					if (chckbxAssign.isSelected())
					{
						crItem.setAssignee(SharedObjs.getUser());
					}
					else
					{
						crItem.setAssignee("");
						jira.unassignIssue(crKey);
					}
				}
				else if (chckbxAssign.isSelected())
				{
					jira.assignIssue(crKey);
					crItem.setAssignee(SharedObjs.getUser());
				}
				
				SharedObjs.addCrToList(crItem);
				b2gList.add(crItem.getB2gID());
			}
			else
			{
				Logger.log(Logger.TAG_CRSMANAGER,
						   "CR KEY: " + crKey + " seems not to exist. Or your user/password is wrong");
				SharedObjs.crsManagerPane.addLogLine("CR KEY: " + crKey + " seems not to exist. Or your user/password is wrong");
				errors++;
			}
		}
		
		SharedObjs.crsManagerPane.addLogLine("Generating b2g list to download ...");
		
		if (b2gList.size() > 0)
		{
			// Configure the B2gDownloader
			Bug2goDownloader b2gDownloader = Bug2goDownloader.getInstance();
			try
			{
				b2gDownloader.addBugIdList(b2gList);
				b2gDownloader.setError(errors);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			
			// Start download thread
			b2gDownloader.execute();
		}
		else
		{
			JOptionPane.showMessageDialog(SharedObjs.crsManagerPane,
										  "There were errors during the b2g collection."
																	 + "\nWe could not get CRs data from Jira."
																	 + "\nYour pass or username may be wrong or "
																	 + "the CRs sent does not exist.");
		}
	}
	
	/**
	 * Interface functions -------------------------------
	 */
	private void btnClearAction()
	{
		textDownload.setText("");
	}
	
	private void btnPasteAction()
	{
		textDownload.setText("");
		Scanner scanner;
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		
		try
		{
			String string = (String) clipboard.getData(DataFlavor.stringFlavor);
			scanner = new Scanner(string);
			String str;
			
			while (scanner.hasNext())
			{
				str = scanner.nextLine();
				textDownload.setText(textDownload.getText() + str + "\n");
			}
		}
		catch (Exception ex)
		{
			JOptionPane.showMessageDialog(this, "Exception: " + ex.getMessage());
		}
	}
	
	private void btnDownloadAction()
	{
		btnDownload.setEnabled(false);
		new Thread(new Runnable()
		{
			
			@Override
			public void run()
			{
				try
				{
					
					downloadCRs();
				}
				catch (ParseException e)
				{
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	public void resetDonwloadBtn()
	{
		btnDownload.setEnabled(true);
	}
	
	private void btnOpenAction()
	{
		for (String s : textDownload.getText().split("\n"))
		{
			try
			{
				s = s.replaceAll("\n", "");
				s = s.replaceAll("\r", "");
				s = s.replaceAll(" ", "");
				Desktop.getDesktop().browse(new URI("http://idart.mot.com/browse/" + s));
				Thread.sleep(500);
			}
			catch (Exception ex)
			{
				JOptionPane.showMessageDialog(this, "Exception: " + ex.getMessage());
			}
		}
	}
	
	public void updateDiagList()
	{
		updateUI();
		repaint();
		revalidate();
	}
	
	public void addLogLine(String line)
	{
		if (textLog.getText().split("\n").length > 150)
		{
			try
			{
				File f = new File("Data\\logs\\log_"
								  + new Timestamp(System.currentTimeMillis()).toString().replace(":", "_")
								  + ".txt");
				BufferedWriter bw = new BufferedWriter(new FileWriter(f));
				bw.write(textLog.getText());
				bw.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			
			textLog.setText("");
		}
		
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		format.setTimeZone(TimeZone.getTimeZone("Brazil/East"));
		
		textLog.setText(textLog.getText() + format.format(date) + "\t" + line + "\n");
		textLog.setCaretPosition(textLog.getText().length());
	}
	
	/**
	 * Aux functions --------------------------------------------------------
	 */
	public void saveUserData()
	{
		String xmlPath[] = new String[] {"crs_jira_pane", ""};
		
		xmlPath[1] = "path";
		XmlMngr.setUserValueOf(xmlPath, textPath.getText());
		xmlPath[1] = "assign";
		XmlMngr.setUserValueOf(xmlPath, chckbxAssign.isSelected() + "");
		xmlPath[1] = "label";
		XmlMngr.setUserValueOf(xmlPath, chckbxLabels.isSelected() + "");
		xmlPath[1] = "labels";
		XmlMngr.setUserValueOf(xmlPath, textLabel.getText());
		
		Logger.log(Logger.TAG_CRSMANAGER, "CrsManagerPane data saved");
	}
	
	private void loadUserData()
	{
		String xmlPath[] = new String[] {"crs_jira_pane", ""};
		
		xmlPath[1] = "path";
		textPath.setText(XmlMngr.getUserValueOf(xmlPath));
		SharedObjs.setDownloadPath(textPath.getText());
		
		xmlPath[1] = "assign";
		chckbxAssign.setSelected(Boolean.parseBoolean(XmlMngr.getUserValueOf(xmlPath)));
		
		xmlPath[1] = "label";
		chckbxLabels.setSelected(Boolean.parseBoolean(XmlMngr.getUserValueOf(xmlPath)));
		
		xmlPath[1] = "labels";
		textLabel.setText(XmlMngr.getUserValueOf(xmlPath));
		
		Logger.log(Logger.TAG_CRSMANAGER, "CrsManagerPane variables Loaded");
	}
	
	public void runScript(String folder) throws IOException
	{
		Logger.log(Logger.TAG_CRSMANAGER, "Generating report output for " + folder);
		
		// File seek and load configuration
		File f = new File(folder);
		File[] filesList = f.listFiles();
		String reportFile = null, sCurrentLine;
		String bugreport = null;
		
		addLogLine("Generating bugreport for " + f.getName() + " ...");
		
		// Look for the file
		for (int j = 0; j < filesList.length; j++)
		{
			if (filesList[j].isFile())
			{
				String files = filesList[j].getName();
				if (files.toLowerCase().endsWith(".txt") && files.toLowerCase().contains("report_info"))
				{
					reportFile = folder + "\\" + files;
					break;
				}
			}
		}
		
		// Try to open file
		BufferedReader br = null;
		if (reportFile == null)
		{
			Logger.log(Logger.TAG_CRSMANAGER, "Log not found: " + reportFile);
			Logger.log(Logger.TAG_CRSMANAGER, "Not possible to find product name");
			JOptionPane.showMessageDialog(null, "Could not find product name! Report output not being generated for this CR");
			return;
		}
		else
		{
			br = new BufferedReader(new FileReader(reportFile));
		}
		
		// Parse file
		boolean parsed = false;
		while ((sCurrentLine = br.readLine()) != null)
		{
			if (sCurrentLine.toLowerCase().contains("product"))
			{
				Logger.log(Logger.TAG_CRSMANAGER, "--- Initial line: " + sCurrentLine);
				sCurrentLine = sCurrentLine.replace("\"PRODUCT\": \"", "").replace(" ", "");
				sCurrentLine = sCurrentLine.substring(0, sCurrentLine.indexOf("_"));
				Logger.log(Logger.TAG_CRSMANAGER, sCurrentLine);
				
				SharedObjs.copyScript(new File("Data\\scripts\\_Base.pl"),
									  new File(folder + "\\build_report.pl"));
									  
				// Configure build report battery capacity
				try
				{
					@SuppressWarnings("resource")
					Scanner scanner = new Scanner(new File(folder + "\\build_report.pl"));
					String content = scanner.useDelimiter("\\Z").next();
					content = content.replace("#bat_cap#",
											  SharedObjs.advOptions.getBatCapValue(sCurrentLine));
					PrintWriter out = new PrintWriter(folder + "\\build_report.pl");
					out.println(content);
					out.close();
					parsed = true;
				}
				catch (FileNotFoundException e)
				{
					e.printStackTrace();
				}
				break;
			}
		}
		
		if(!parsed)
		{
			try
			{
				Logger.log(Logger.TAG_CRSMANAGER, "Could not find product name or product battery capacity. Using 3000 as bat cap");
				JOptionPane.showMessageDialog(null, "Could not find product name or product battery capacity.\nUsing 3000 as battery capacity");
				@SuppressWarnings("resource")
				Scanner scanner = new Scanner(new File(folder + "\\build_report.pl"));
				String content = scanner.useDelimiter("\\Z").next();
				content = content.replace("#bat_cap#","3000");
				PrintWriter out = new PrintWriter(folder + "\\build_report.pl");
				out.println(content);
				out.close();
				parsed = true;
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}
		}
		
		br.close();
		
		for (File file : filesList)
		{
			if (file.getName().contains("bugreport"))
			{
				bugreport = file.getName();
			}
		}
		
		ProcessBuilder builder = new ProcessBuilder("cmd.exe",
													"/c",
													"cd \"" + folder + "\" && build_report.pl " + bugreport
														  + " > report-output.txt");
		Logger.log(Logger.TAG_CRSMANAGER, "Bugreport file: " + bugreport);
		for (String c : builder.command())
		{
			Logger.log(Logger.TAG_CRSMANAGER, "Commands: " + c);
		}
		builder.redirectErrorStream(true);
		Process p = builder.start();
		BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line = "";
		String output = ""; // workaround for report outout 0kb
		
		while (true)
		{
			line = r.readLine();
			if (line == null)
			{
				break;
			}
			output += line + "\n";
			Logger.log(Logger.TAG_CRSMANAGER, line);
		}
		
		if (new File(folder + "/report-output.txt").length() < 10)
		{
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(folder + "/report-output.txt")));
			bw.write(output);
			bw.close();
		}
		
		addLogLine("Report output generated for " + f.getName());
	}
	
	/**
	 * Getters ---------------------------------------------------
	 */
	public String getDownloadPath()
	{
		return textPath.getText().replace("\\", "\\\\").concat("\\\\");
	}
	
	public String[] getCrsToDownload()
	{
		return textDownload.getText().replace(" ", "").replace("\r", "").split("\n");
	}
	
	public String[] getLabels()
	{
		return textLabel.getText().split(" ");
	}
	
	public JTextArea getTextDownload()
	{
		return textDownload;
	}
	
	public JTextField getTextPath()
	{
		return textPath;
	}
	
	public JCheckBox getChckbxAssign()
	{
		return chckbxAssign;
	}
	
	public JCheckBox getChckbxLabels()
	{
		return chckbxLabels;
	}
}
