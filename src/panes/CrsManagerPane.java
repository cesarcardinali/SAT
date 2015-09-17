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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import objects.CrItem;
import objects.CrItemsList;

import org.json.simple.parser.ParseException;

import supportive.Bug2goDownloader;
import supportive.CrsCloser;
import supportive.DiagCrsCloser;
import supportive.JiraSatApi;
import core.Logger;
import core.SharedObjs;
import core.XmlMngr;


@SuppressWarnings("serial")
public class CrsManagerPane extends JPanel
{
	/**
	 * Global Variables
	 */
	private JTextArea	  textDownload;
	private JTextField	  textPath;
	private JTextPane	  textLog;
	private JTextPane	  textPane;
	private JCheckBox	  chckbxAssign;
	private JCheckBox	  chckbxLabels;
	private JList<String> listDiag;
	private CrItemsList	  crsList;
	private String		  CRs[];
	
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
		gridBagLayout.columnWidths = new int[] {0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[] {1.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
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
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		contentPane.add(panel, gbc_panel);
		
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] {30, 0};
		gbl_panel.rowHeights = new int[] {22, 0, 0};
		gbl_panel.columnWeights = new double[] {0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[] {0.0, 0.0, Double.MIN_VALUE};
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
		gbc_textPath.insets = new Insets(0, 5, 0, 5);
		gbc_textPath.gridx = 0;
		gbc_textPath.gridy = 1;
		panel.add(textPath, gbc_textPath);
		
		JSeparator separator_2 = new JSeparator();
		separator_2.setOrientation(SwingConstants.VERTICAL);
		separator_2.setForeground(Color.DARK_GRAY);
		GridBagConstraints gbc_separator_2 = new GridBagConstraints();
		gbc_separator_2.fill = GridBagConstraints.VERTICAL;
		gbc_separator_2.gridheight = 6;
		gbc_separator_2.insets = new Insets(0, 0, 5, 5);
		gbc_separator_2.gridx = 1;
		gbc_separator_2.gridy = 0;
		contentPane.add(separator_2, gbc_separator_2);
		
		JPanel panel_3 = new JPanel();
		panel_3.setMinimumSize(new Dimension(150, 10));
		GridBagConstraints gbc_panel_3 = new GridBagConstraints();
		gbc_panel_3.gridheight = 6;
		gbc_panel_3.insets = new Insets(10, 5, 5, 5);
		gbc_panel_3.fill = GridBagConstraints.BOTH;
		gbc_panel_3.gridx = 2;
		gbc_panel_3.gridy = 0;
		contentPane.add(panel_3, gbc_panel_3);
		
		GridBagLayout gbl_panel_3 = new GridBagLayout();
		gbl_panel_3.columnWidths = new int[] {0, 0};
		gbl_panel_3.rowHeights = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panel_3.columnWeights = new double[] {0.0, Double.MIN_VALUE};
		gbl_panel_3.rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
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
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setPreferredSize(new Dimension(140, 350));
		scrollPane_2.setBorder(new LineBorder(SystemColor.activeCaption));
		scrollPane_2.setMinimumSize(new Dimension(140, 300));
		GridBagConstraints gbc_scrollPane_2 = new GridBagConstraints();
		gbc_scrollPane_2.fill = GridBagConstraints.VERTICAL;
		gbc_scrollPane_2.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane_2.gridx = 0;
		gbc_scrollPane_2.gridy = 3;
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
		gbc_btnClear.gridy = 4;
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
		gbc_btnPaste.insets = new Insets(0, 0, 5, 0);
		gbc_btnPaste.gridx = 0;
		gbc_btnPaste.gridy = 5;
		panel_3.add(btnPaste, gbc_btnPaste);
		
		JButton btnDownload = new JButton("Get the CRs");
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
		GridBagConstraints gbc_btnDownload = new GridBagConstraints();
		gbc_btnDownload.insets = new Insets(0, 0, 5, 0);
		gbc_btnDownload.gridx = 0;
		gbc_btnDownload.gridy = 6;
		panel_3.add(btnDownload, gbc_btnDownload);
		
		JButton btnOpenOnChrome = new JButton("Open on Chrome");
		btnOpenOnChrome.setToolTipText("Open the CRs on the list above on Chrome");
		btnOpenOnChrome.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				btnOpenAction();
			}
		});
		GridBagConstraints gbc_btnOpenOnChrome = new GridBagConstraints();
		gbc_btnOpenOnChrome.gridx = 0;
		gbc_btnOpenOnChrome.gridy = 7;
		panel_3.add(btnOpenOnChrome, gbc_btnOpenOnChrome);
		
		JSeparator separator_3 = new JSeparator();
		separator_3.setForeground(Color.DARK_GRAY);
		separator_3.setBackground(Color.WHITE);
		separator_3.setOrientation(SwingConstants.VERTICAL);
		GridBagConstraints gbc_separator_3 = new GridBagConstraints();
		gbc_separator_3.fill = GridBagConstraints.VERTICAL;
		gbc_separator_3.gridheight = 6;
		gbc_separator_3.insets = new Insets(0, 0, 5, 5);
		gbc_separator_3.gridx = 3;
		gbc_separator_3.gridy = 0;
		contentPane.add(separator_3, gbc_separator_3);
		
		JPanel panel_4 = new JPanel();
		GridBagConstraints gbc_panel_4 = new GridBagConstraints();
		gbc_panel_4.insets = new Insets(10, 5, 5, 0);
		gbc_panel_4.gridheight = 6;
		gbc_panel_4.fill = GridBagConstraints.BOTH;
		gbc_panel_4.gridx = 4;
		gbc_panel_4.gridy = 0;
		contentPane.add(panel_4, gbc_panel_4);
		
		GridBagLayout gbl_panel_4 = new GridBagLayout();
		gbl_panel_4.columnWidths = new int[] {0, 0};
		gbl_panel_4.rowHeights = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panel_4.columnWeights = new double[] {0.0, Double.MIN_VALUE};
		gbl_panel_4.rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel_4.setLayout(gbl_panel_4);
		JLabel lblDiagCloser = new JLabel("Diag Closer:");
		lblDiagCloser.setMinimumSize(new Dimension(60, 14));
		lblDiagCloser.setFont(new Font("Tahoma", Font.BOLD, 18));
		GridBagConstraints gbc_lblDiagCloser = new GridBagConstraints();
		gbc_lblDiagCloser.insets = new Insets(0, 0, 5, 0);
		gbc_lblDiagCloser.gridx = 0;
		gbc_lblDiagCloser.gridy = 0;
		panel_4.add(lblDiagCloser, gbc_lblDiagCloser);
		
		JScrollPane scrollPane_5 = new JScrollPane();
		scrollPane_5.setBorder(new LineBorder(SystemColor.activeCaption));
		scrollPane_5.setPreferredSize(new Dimension(220, 360));
		scrollPane_5.setMinimumSize(new Dimension(220, 360));
		GridBagConstraints gbc_scrollPane_5 = new GridBagConstraints();
		gbc_scrollPane_5.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_5.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane_5.gridx = 0;
		gbc_scrollPane_5.gridy = 1;
		panel_4.add(scrollPane_5, gbc_scrollPane_5);
		
		listDiag = new JList<String>();
		scrollPane_5.setViewportView(listDiag);
		listDiag.setToolTipText("List of CRs to be checked");
		listDiag.setMaximumSize(new Dimension(2000, 2000));
		listDiag.setVisibleRowCount(0);
		listDiag.setBorder(null);
		listDiag.setMinimumSize(new Dimension(220, 360));
		listDiag.setModel(new DefaultListModel<String>());
		
		JLabel lblBgId = new JLabel("   B2G ID   -    Jira ID  -  Diag Result");
		lblBgId.setBorder(new LineBorder(new Color(0, 0, 0)));
		scrollPane_5.setColumnHeaderView(lblBgId);
		
		JTextArea txtrInstructionsHere = new JTextArea();
		txtrInstructionsHere.setTabSize(4);
		txtrInstructionsHere.setWrapStyleWord(true);
		txtrInstructionsHere.setEditable(false);
		txtrInstructionsHere.setLineWrap(true);
		txtrInstructionsHere.setMargin(new Insets(2, 5, 2, 5));
		txtrInstructionsHere.setBorder(new LineBorder(Color.RED));
		txtrInstructionsHere.setFont(new Font("Tahoma", Font.PLAIN, 11));
		txtrInstructionsHere.setText("Click on HELP button to learn how to use it");
		txtrInstructionsHere.setBackground(UIManager.getColor("Panel.background"));
		txtrInstructionsHere.setPreferredSize(new Dimension(220, 17));
		txtrInstructionsHere.setMinimumSize(new Dimension(220, 17));
		GridBagConstraints gbc_txtrInstructionsHere = new GridBagConstraints();
		gbc_txtrInstructionsHere.insets = new Insets(0, 0, 5, 0);
		gbc_txtrInstructionsHere.gridx = 0;
		gbc_txtrInstructionsHere.gridy = 2;
		panel_4.add(txtrInstructionsHere, gbc_txtrInstructionsHere);
		
		JButton btnHelp = new JButton("Help");
		btnHelp.setEnabled(false);
		btnHelp.setPreferredSize(new Dimension(70, 23));
		btnHelp.setToolTipText("How to use DIAG CLOSER.");
		btnHelp.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnHelp.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				JOptionPane.showMessageDialog(null,
											  "First, download the CRs using the \"Downloader\" option\n"
													+ "After download is done, just click \"Run\" and follow the instructions on the screen\n\n"
													+ "If you have downloaded the CRs already, you need to insert the path where the ZIP files are.\n"
													+ "and insert the CRs on the textarea of the Downloader option on the left.\n"
													+ "Then, just click \"RUN\"\n\n"
													+ "If you dont have the ZIP files anymore, sorry, but the tool is not prepared to this situation yet.\n\n"
													+ "More options and more flexibility coming soon\n\n\n"
													+ "Thank you, have a good day Sir.");
			}
		});
		GridBagConstraints gbc_btnHelp = new GridBagConstraints();
		gbc_btnHelp.insets = new Insets(0, 0, 5, 0);
		gbc_btnHelp.gridx = 0;
		gbc_btnHelp.gridy = 3;
		panel_4.add(btnHelp, gbc_btnHelp);
		
		JButton btnrun = new JButton("Run");
		btnrun.setEnabled(false);
		btnrun.setPreferredSize(new Dimension(70, 23));
		btnrun.setToolTipText("Start to check CRs and close Diag ones. Please, read HELP");
		btnrun.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				btnRunDiag();
			}
		});
		GridBagConstraints gbc_btnrun = new GridBagConstraints();
		gbc_btnrun.insets = new Insets(0, 0, 5, 0);
		gbc_btnrun.gridx = 0;
		gbc_btnrun.gridy = 4;
		panel_4.add(btnrun, gbc_btnrun);
		
		JButton btnOpenDiagsOn = new JButton("Open Diags on Chrome");
		btnOpenDiagsOn.setEnabled(false);
		btnOpenDiagsOn.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				HashMap<String, String> b2g_crid = DiagCrsCloser.getB2g_crid();
				ArrayList<String> getDiagCRs = DiagCrsCloser.getDiagCRs();
				for (String b2gid : getDiagCRs)
				{
					try
					{
						String s = b2g_crid.get(b2gid);
						s = s.replaceAll("\n", "");
						s = s.replaceAll("\r", "");
						s = s.replaceAll(" |  ", "");
						Desktop.getDesktop().browse(new URI("http://idart.mot.com/browse/" + s));
						Thread.sleep(600);
					}
					catch (Exception ex)
					{
						JOptionPane.showMessageDialog(null, "Exception:\n" + ex.getMessage());
					}
				}
			}
		});
		btnOpenDiagsOn.setPreferredSize(new Dimension(165, 23));
		GridBagConstraints gbc_btnOpenDiagsOn = new GridBagConstraints();
		gbc_btnOpenDiagsOn.insets = new Insets(0, 0, 5, 0);
		gbc_btnOpenDiagsOn.gridx = 0;
		gbc_btnOpenDiagsOn.gridy = 5;
		panel_4.add(btnOpenDiagsOn, gbc_btnOpenDiagsOn);
		
		JButton btnOpenNondiagsOn = new JButton("Open Non-Diags on Chrome");
		btnOpenNondiagsOn.setEnabled(false);
		btnOpenNondiagsOn.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				HashMap<String, String> b2g_crid = DiagCrsCloser.getB2g_crid();
				ArrayList<String> getUnknownCRs = DiagCrsCloser.getUnknownCRs();
				for (String b2gid : getUnknownCRs)
				{
					try
					{
						String s = b2g_crid.get(b2gid);
						s = s.replaceAll("\n", "");
						s = s.replaceAll("\r", "");
						s = s.replaceAll(" |  ", "");
						Desktop.getDesktop().browse(new URI("http://idart.mot.com/browse/" + s));
						Thread.sleep(600);
					}
					catch (Exception ex)
					{
						JOptionPane.showMessageDialog(null, "Exception:\n" + ex.getMessage());
					}
				}
			}
		});
		GridBagConstraints gbc_btnOpenNondiagsOn = new GridBagConstraints();
		gbc_btnOpenNondiagsOn.insets = new Insets(0, 0, 5, 0);
		gbc_btnOpenNondiagsOn.gridx = 0;
		gbc_btnOpenNondiagsOn.gridy = 6;
		panel_4.add(btnOpenNondiagsOn, gbc_btnOpenNondiagsOn);
		
		JButton btnCloseOld = new JButton("Close As Old");
		btnCloseOld.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				new Thread(new CrsCloser()).start();
			}
		});
		GridBagConstraints gbc_btnCloseOld = new GridBagConstraints();
		gbc_btnCloseOld.gridx = 0;
		gbc_btnCloseOld.gridy = 7;
		panel_4.add(btnCloseOld, gbc_btnCloseOld);
		
		JSeparator separator = new JSeparator();
		separator.setMaximumSize(new Dimension(130, 32767));
		separator.setForeground(Color.DARK_GRAY);
		GridBagConstraints gbc_separator = new GridBagConstraints();
		gbc_separator.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator.insets = new Insets(0, 1, 5, 5);
		gbc_separator.gridx = 0;
		gbc_separator.gridy = 1;
		contentPane.add(separator, gbc_separator);
		
		JSeparator separator_4 = new JSeparator();
		separator_4.setForeground(new Color(0, 0, 0));
		GridBagConstraints gbc_separator_4 = new GridBagConstraints();
		gbc_separator_4.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator_4.gridwidth = 5;
		gbc_separator_4.insets = new Insets(10, 5, 5, 0);
		gbc_separator_4.gridx = 0;
		gbc_separator_4.gridy = 6;
		contentPane.add(separator_4, gbc_separator_4);
		
		JPanel panel_5 = new JPanel();
		panel_5.setBorder(new LineBorder(new Color(240, 128, 128), 1, true));
		GridBagConstraints gbc_panel_5 = new GridBagConstraints();
		gbc_panel_5.gridwidth = 5;
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
		
		crsList = new CrItemsList();
		
		loadUserData();
	}
	
	/**
	 * Download CRs
	 * 
	 * main download function
	 * @throws ParseException
	 */
	private void downloadCRs() throws ParseException
	{
		SharedObjs.crsManagerPane.addLogLine("Acquiring CRs data ...");
		
		// Setup jira connection
		JiraSatApi jira = new JiraSatApi(JiraSatApi.DEFAULT_JIRA_URL,
										 SharedObjs.getUser(),
										 SharedObjs.getPass());
										 
		// Get the CRs list
		CRs = textDownload.getText().replaceAll(" ", "").split("\n");
		
		Logger.log(Logger.TAG_CRSMANAGER, "CRs List:" + CRs.length);
		
		ArrayList<String> b2gList = new ArrayList<String>();
		
		SharedObjs.crsManagerPane.addLogLine("Generating b2g list to download ...");
		
		// Manage CR
		for (String crKey : CRs)
		{
			Logger.log(Logger.TAG_CRSMANAGER, "-" + crKey + "-");
			
			CrItem crItem = jira.getCrData(crKey);
			crsList.add(crItem);
			
			if (crItem != null)
			{
				SharedObjs.addCrToList(crItem);
				
				if (chckbxAssign.isSelected())
					jira.assignIssue(crKey);
					
				if (chckbxLabels.isSelected())
					jira.addLabel(crKey, "ll_prodteam_analyzed");
					
				b2gList.add(crItem.getB2gID());
			}
			else
			{
				Logger.log(Logger.TAG_CRSMANAGER,
						   "CR KEY: " + crKey + " seems not to exist. Or your user/password is wrong");
			}
		}
		
		if (b2gList.size() > 0)
		{
			// Configure the B2gDownloader
			Bug2goDownloader b2gDownloader = Bug2goDownloader.getInstance();
			try
			{
				b2gDownloader.addBugIdList(b2gList);
				b2gDownloader.setOverwrite(false);
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
	
	private void btnRunDiag()
	{
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
		
		textLog.setText(textLog.getText() + line + "\n");
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
		
		Logger.log(Logger.TAG_CRSMANAGER, "CrsManagerPane variables Loaded");
	}
	
	public void runScript(String folder) throws IOException
	{
		Logger.log(Logger.TAG_CRSMANAGER, "Generating bugreport for " + folder);
		
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
			Logger.log(Logger.TAG_CRSMANAGER, "Log de sistema nao encontrado: " + reportFile);
		}
		else
		{
			br = new BufferedReader(new FileReader(reportFile));
		}
		
		// Parse file
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
				}
				catch (FileNotFoundException e)
				{
					e.printStackTrace();
				}
				break;
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
		builder.redirectErrorStream(true);
		Process p = builder.start();
		BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line;
		
		while (true)
		{
			line = r.readLine();
			if (line == null)
			{
				break;
			}
			// Logger.log(Logger.TAG_CRSMANAGER, line);
		}
		
		addLogLine("Bugreport generated for " + f.getName());
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
	
	public CrItemsList getCrsList()
	{
		return crsList;
	}
}
