package Panes;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Component;

import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JSeparator;
import java.awt.Dimension;
import java.awt.Color;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import Main.BatTracer;

import javax.swing.JRadioButton;
import java.awt.FlowLayout;
import javax.swing.JCheckBox;

@SuppressWarnings("serial")
public class OptionsPane extends JPanel {

	private JTextField textConsumeOn;
	private JTextField textConsumeOff;
	private JTextField textConsumeFull;
	private JTextField textSuspicious;
	private JTextField textAlarms;
	private JTextField textB2g;
	private JTextField textTether;
	private JTextField textDiag;
	private JTextField textHighCurrent;
	private JTextField textKernelWake;
	private JTextField textJavaWake;
	private JTextField textSuspiciousHeader;
	private AdvancedOptionsPane advOptions;
	private BatTracer BaseWindow;
	private JRadioButton rdbtnNotepad;
	private JCheckBox chkTextWrap;
	private JRadioButton rdbtnTAnalisys;

	/**
	 * Create the panel.
	 */
	public OptionsPane(BatTracer parent) {
		setMinimumSize(new Dimension(800, 600));
		BaseWindow= parent;
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {918};
		gridBagLayout.rowHeights = new int[] {669, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 1.0};
		setLayout(gridBagLayout);
		
		JPanel commentsPanel = new JPanel();
		commentsPanel.setBorder(null);
		GridBagConstraints gbc_commentsPanel = new GridBagConstraints();
		gbc_commentsPanel.insets = new Insets(0, 0, 5, 0);
		gbc_commentsPanel.anchor = GridBagConstraints.NORTH;
		gbc_commentsPanel.fill = GridBagConstraints.BOTH;
		gbc_commentsPanel.gridx = 0;
		gbc_commentsPanel.gridy = 0;
		add(commentsPanel, gbc_commentsPanel);
		GridBagLayout gbl_commentsPanel = new GridBagLayout();
		gbl_commentsPanel.columnWidths = new int[] {100, 450, 30};
		gbl_commentsPanel.rowHeights = new int[]{0, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 20, 0};
		gbl_commentsPanel.columnWeights = new double[]{1.0, 1.0, 0.0};
		gbl_commentsPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		commentsPanel.setLayout(gbl_commentsPanel);
		
		JLabel label_14 = new JLabel("Options:");
		label_14.setFont(new Font("Tahoma", Font.BOLD, 24));
		GridBagConstraints gbc_label_14 = new GridBagConstraints();
		gbc_label_14.gridwidth = 3;
		gbc_label_14.insets = new Insets(10, 10, 10, 10);
		gbc_label_14.gridx = 0;
		gbc_label_14.gridy = 0;
		commentsPanel.add(label_14, gbc_label_14);
		
		JLabel lblCommentsPersonalization = new JLabel("Comments personalization:");
		lblCommentsPersonalization.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblCommentsPersonalization.setFont(new Font("Tahoma", Font.BOLD, 16));
		GridBagConstraints gbc_lblCommentsPersonalization = new GridBagConstraints();
		gbc_lblCommentsPersonalization.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblCommentsPersonalization.anchor = GridBagConstraints.WEST;
		gbc_lblCommentsPersonalization.gridwidth = 2;
		gbc_lblCommentsPersonalization.insets = new Insets(5, 5, 5, 5);
		gbc_lblCommentsPersonalization.gridx = 0;
		gbc_lblCommentsPersonalization.gridy = 1;
		commentsPanel.add(lblCommentsPersonalization, gbc_lblCommentsPersonalization);
		
		JLabel lblHighConsumeApps = new JLabel("High consumption apps:");
		lblHighConsumeApps.setMinimumSize(new Dimension(96, 18));
		lblHighConsumeApps.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblHighConsumeApps.setFont(new Font("Tahoma", Font.ITALIC, 14));
		GridBagConstraints gbc_lblHighConsumeApps = new GridBagConstraints();
		gbc_lblHighConsumeApps.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblHighConsumeApps.anchor = GridBagConstraints.WEST;
		gbc_lblHighConsumeApps.gridwidth = 2;
		gbc_lblHighConsumeApps.insets = new Insets(10, 25, 5, 5);
		gbc_lblHighConsumeApps.gridx = 0;
		gbc_lblHighConsumeApps.gridy = 2;
		commentsPanel.add(lblHighConsumeApps, gbc_lblHighConsumeApps);
		
		JLabel lblFullLog = new JLabel("Full log:");
		lblFullLog.setMaximumSize(new Dimension(100, 14));
		lblFullLog.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_lblFullLog = new GridBagConstraints();
		gbc_lblFullLog.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblFullLog.anchor = GridBagConstraints.EAST;
		gbc_lblFullLog.insets = new Insets(0, 0, 5, 5);
		gbc_lblFullLog.gridx = 0;
		gbc_lblFullLog.gridy = 3;
		commentsPanel.add(lblFullLog, gbc_lblFullLog);
		
		textConsumeFull = new JTextField();
		textConsumeFull.setMinimumSize(new Dimension(850, 20));
		GridBagConstraints gbc_textConsumeFull = new GridBagConstraints();
		gbc_textConsumeFull.weightx = 3.0;
		gbc_textConsumeFull.fill = GridBagConstraints.HORIZONTAL;
		gbc_textConsumeFull.insets = new Insets(0, 0, 5, 7);
		gbc_textConsumeFull.gridx = 1;
		gbc_textConsumeFull.gridy = 3;
		commentsPanel.add(textConsumeFull, gbc_textConsumeFull);
		textConsumeFull.setColumns(1000);
		
		JButton button = new JButton("?");
		button.setMargin(new Insets(2, 8, 2, 8));
		GridBagConstraints gbc_button = new GridBagConstraints();
		gbc_button.insets = new Insets(0, 0, 5, 0);
		gbc_button.gridx = 2;
		gbc_button.gridy = 3;
		commentsPanel.add(button, gbc_button);
		
		JLabel lblScreenOff = new JLabel("Screen Off:");
		lblScreenOff.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_lblScreenOff = new GridBagConstraints();
		gbc_lblScreenOff.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblScreenOff.insets = new Insets(0, 0, 5, 5);
		gbc_lblScreenOff.gridx = 0;
		gbc_lblScreenOff.gridy = 4;
		commentsPanel.add(lblScreenOff, gbc_lblScreenOff);
		
		textConsumeOff = new JTextField();
		textConsumeOff.setPreferredSize(new Dimension(500, 20));
		textConsumeOff.setMinimumSize(new Dimension(500, 20));
		GridBagConstraints gbc_textConsumeOff = new GridBagConstraints();
		gbc_textConsumeOff.fill = GridBagConstraints.HORIZONTAL;
		gbc_textConsumeOff.insets = new Insets(0, 0, 5, 7);
		gbc_textConsumeOff.gridx = 1;
		gbc_textConsumeOff.gridy = 4;
		commentsPanel.add(textConsumeOff, gbc_textConsumeOff);
		textConsumeOff.setColumns(200);
		
		JButton button_1 = new JButton("?");
		button_1.setMargin(new Insets(2, 8, 2, 8));
		GridBagConstraints gbc_button_1 = new GridBagConstraints();
		gbc_button_1.insets = new Insets(0, 0, 5, 0);
		gbc_button_1.gridx = 2;
		gbc_button_1.gridy = 4;
		commentsPanel.add(button_1, gbc_button_1);
		
		JLabel lblScreenOn = new JLabel("Screen On:");
		lblScreenOn.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_lblScreenOn = new GridBagConstraints();
		gbc_lblScreenOn.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblScreenOn.insets = new Insets(0, 0, 5, 5);
		gbc_lblScreenOn.gridx = 0;
		gbc_lblScreenOn.gridy = 5;
		commentsPanel.add(lblScreenOn, gbc_lblScreenOn);
		
		textConsumeOn = new JTextField();
		textConsumeOn.setPreferredSize(new Dimension(500, 20));
		textConsumeOn.setMinimumSize(new Dimension(500, 20));
		GridBagConstraints gbc_textConsumeOn = new GridBagConstraints();
		gbc_textConsumeOn.fill = GridBagConstraints.HORIZONTAL;
		gbc_textConsumeOn.insets = new Insets(0, 0, 5, 7);
		gbc_textConsumeOn.gridx = 1;
		gbc_textConsumeOn.gridy = 5;
		commentsPanel.add(textConsumeOn, gbc_textConsumeOn);
		textConsumeOn.setColumns(200);
		
		JButton button_2 = new JButton("?");
		button_2.setMargin(new Insets(2, 8, 2, 8));
		GridBagConstraints gbc_button_2 = new GridBagConstraints();
		gbc_button_2.insets = new Insets(0, 0, 5, 0);
		gbc_button_2.gridx = 2;
		gbc_button_2.gridy = 5;
		commentsPanel.add(button_2, gbc_button_2);
		
		JSeparator separator_8 = new JSeparator();
		separator_8.setPreferredSize(new Dimension(500, 1));
		separator_8.setMinimumSize(new Dimension(3, 2));
		separator_8.setForeground(Color.LIGHT_GRAY);
		separator_8.setBackground(Color.WHITE);
		GridBagConstraints gbc_separator_8 = new GridBagConstraints();
		gbc_separator_8.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator_8.gridwidth = 2;
		gbc_separator_8.insets = new Insets(0, 12, 5, 5);
		gbc_separator_8.gridx = 0;
		gbc_separator_8.gridy = 6;
		commentsPanel.add(separator_8, gbc_separator_8);
		
		JLabel label_15 = new JLabel("General Issues:");
		label_15.setMinimumSize(new Dimension(100, 18));
		label_15.setFont(new Font("Tahoma", Font.ITALIC, 14));
		GridBagConstraints gbc_label_15 = new GridBagConstraints();
		gbc_label_15.fill = GridBagConstraints.HORIZONTAL;
		gbc_label_15.gridwidth = 2;
		gbc_label_15.insets = new Insets(0, 25, 5, 5);
		gbc_label_15.gridx = 0;
		gbc_label_15.gridy = 7;
		commentsPanel.add(label_15, gbc_label_15);
		
		JLabel label_16 = new JLabel("High Current:");
		label_16.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_label_16 = new GridBagConstraints();
		gbc_label_16.anchor = GridBagConstraints.EAST;
		gbc_label_16.insets = new Insets(0, 0, 5, 5);
		gbc_label_16.gridx = 0;
		gbc_label_16.gridy = 8;
		commentsPanel.add(label_16, gbc_label_16);
		
		textHighCurrent = new JTextField();
		textHighCurrent.setPreferredSize(new Dimension(500, 20));
		textHighCurrent.setMinimumSize(new Dimension(500, 20));
		textHighCurrent.setColumns(10);
		GridBagConstraints gbc_textHighCurrent = new GridBagConstraints();
		gbc_textHighCurrent.insets = new Insets(0, 0, 5, 7);
		gbc_textHighCurrent.fill = GridBagConstraints.HORIZONTAL;
		gbc_textHighCurrent.gridx = 1;
		gbc_textHighCurrent.gridy = 8;
		commentsPanel.add(textHighCurrent, gbc_textHighCurrent);
		
		JButton button_4 = new JButton("?");
		button_4.setMargin(new Insets(2, 8, 2, 8));
		GridBagConstraints gbc_button_4 = new GridBagConstraints();
		gbc_button_4.insets = new Insets(0, 0, 5, 0);
		gbc_button_4.gridx = 2;
		gbc_button_4.gridy = 8;
		commentsPanel.add(button_4, gbc_button_4);
		
		JLabel label_17 = new JLabel("Kernel Wakelocks:");
		label_17.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_label_17 = new GridBagConstraints();
		gbc_label_17.anchor = GridBagConstraints.EAST;
		gbc_label_17.insets = new Insets(0, 0, 5, 5);
		gbc_label_17.gridx = 0;
		gbc_label_17.gridy = 9;
		commentsPanel.add(label_17, gbc_label_17);
		
		textKernelWake = new JTextField();
		textKernelWake.setPreferredSize(new Dimension(500, 20));
		textKernelWake.setMinimumSize(new Dimension(500, 20));
		textKernelWake.setColumns(10);
		GridBagConstraints gbc_textKernelWake = new GridBagConstraints();
		gbc_textKernelWake.insets = new Insets(0, 0, 5, 7);
		gbc_textKernelWake.fill = GridBagConstraints.HORIZONTAL;
		gbc_textKernelWake.gridx = 1;
		gbc_textKernelWake.gridy = 9;
		commentsPanel.add(textKernelWake, gbc_textKernelWake);
		
		JButton button_5 = new JButton("?");
		button_5.setMargin(new Insets(2, 8, 2, 8));
		GridBagConstraints gbc_button_5 = new GridBagConstraints();
		gbc_button_5.insets = new Insets(0, 0, 5, 0);
		gbc_button_5.gridx = 2;
		gbc_button_5.gridy = 9;
		commentsPanel.add(button_5, gbc_button_5);
		
		JLabel label_18 = new JLabel("Java Wakelocks:");
		label_18.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_label_18 = new GridBagConstraints();
		gbc_label_18.anchor = GridBagConstraints.EAST;
		gbc_label_18.insets = new Insets(0, 0, 5, 5);
		gbc_label_18.gridx = 0;
		gbc_label_18.gridy = 10;
		commentsPanel.add(label_18, gbc_label_18);
		
		textJavaWake = new JTextField();
		textJavaWake.setPreferredSize(new Dimension(500, 20));
		textJavaWake.setMinimumSize(new Dimension(500, 20));
		textJavaWake.setColumns(10);
		GridBagConstraints gbc_textJavaWake = new GridBagConstraints();
		gbc_textJavaWake.insets = new Insets(0, 0, 5, 7);
		gbc_textJavaWake.fill = GridBagConstraints.HORIZONTAL;
		gbc_textJavaWake.gridx = 1;
		gbc_textJavaWake.gridy = 10;
		commentsPanel.add(textJavaWake, gbc_textJavaWake);
		
		JButton button_10 = new JButton("?");
		button_10.setMargin(new Insets(2, 8, 2, 8));
		GridBagConstraints gbc_button_10 = new GridBagConstraints();
		gbc_button_10.insets = new Insets(0, 0, 5, 0);
		gbc_button_10.gridx = 2;
		gbc_button_10.gridy = 10;
		commentsPanel.add(button_10, gbc_button_10);
		
		JSeparator separator = new JSeparator();
		separator.setMinimumSize(new Dimension(3, 2));
		separator.setBackground(Color.WHITE);
		separator.setForeground(Color.LIGHT_GRAY);
		separator.setPreferredSize(new Dimension(500, 1));
		GridBagConstraints gbc_separator = new GridBagConstraints();
		gbc_separator.insets = new Insets(0, 12, 5, 5);
		gbc_separator.gridwidth = 2;
		gbc_separator.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator.gridx = 0;
		gbc_separator.gridy = 11;
		commentsPanel.add(separator, gbc_separator);
		
		JLabel label = new JLabel("Suspicious:");
		label.setMinimumSize(new Dimension(100, 18));
		label.setFont(new Font("Tahoma", Font.ITALIC, 14));
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.fill = GridBagConstraints.HORIZONTAL;
		gbc_label.gridwidth = 2;
		gbc_label.anchor = GridBagConstraints.WEST;
		gbc_label.insets = new Insets(0, 25, 5, 5);
		gbc_label.gridx = 0;
		gbc_label.gridy = 12;
		commentsPanel.add(label, gbc_label);
		
		JLabel label_1 = new JLabel("Header:");
		label_1.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_label_1 = new GridBagConstraints();
		gbc_label_1.insets = new Insets(0, 0, 5, 5);
		gbc_label_1.anchor = GridBagConstraints.EAST;
		gbc_label_1.gridx = 0;
		gbc_label_1.gridy = 13;
		commentsPanel.add(label_1, gbc_label_1);
		
		textSuspiciousHeader = new JTextField();
		textSuspiciousHeader.setPreferredSize(new Dimension(500, 20));
		textSuspiciousHeader.setMinimumSize(new Dimension(500, 20));
		textSuspiciousHeader.setColumns(10);
		GridBagConstraints gbc_textSuspiciousHeader = new GridBagConstraints();
		gbc_textSuspiciousHeader.insets = new Insets(0, 0, 5, 5);
		gbc_textSuspiciousHeader.fill = GridBagConstraints.HORIZONTAL;
		gbc_textSuspiciousHeader.gridx = 1;
		gbc_textSuspiciousHeader.gridy = 13;
		commentsPanel.add(textSuspiciousHeader, gbc_textSuspiciousHeader);
		
		JButton button_3 = new JButton("?");
		button_3.setMargin(new Insets(2, 8, 2, 8));
		GridBagConstraints gbc_button_3 = new GridBagConstraints();
		gbc_button_3.insets = new Insets(0, 0, 5, 0);
		gbc_button_3.gridx = 2;
		gbc_button_3.gridy = 13;
		commentsPanel.add(button_3, gbc_button_3);
		
		JLabel label_2 = new JLabel("Comment:");
		label_2.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_label_2 = new GridBagConstraints();
		gbc_label_2.anchor = GridBagConstraints.EAST;
		gbc_label_2.insets = new Insets(0, 0, 5, 5);
		gbc_label_2.gridx = 0;
		gbc_label_2.gridy = 14;
		commentsPanel.add(label_2, gbc_label_2);
		
		textSuspicious = new JTextField();
		textSuspicious.setPreferredSize(new Dimension(500, 20));
		textSuspicious.setMinimumSize(new Dimension(500, 20));
		textSuspicious.setColumns(10);
		GridBagConstraints gbc_textSuspicious = new GridBagConstraints();
		gbc_textSuspicious.fill = GridBagConstraints.HORIZONTAL;
		gbc_textSuspicious.insets = new Insets(0, 0, 5, 7);
		gbc_textSuspicious.gridx = 1;
		gbc_textSuspicious.gridy = 14;
		commentsPanel.add(textSuspicious, gbc_textSuspicious);
		
		JButton button_11 = new JButton("?");
		button_11.setMargin(new Insets(2, 8, 2, 8));
		GridBagConstraints gbc_button_11 = new GridBagConstraints();
		gbc_button_11.insets = new Insets(0, 0, 5, 0);
		gbc_button_11.gridx = 2;
		gbc_button_11.gridy = 14;
		commentsPanel.add(button_11, gbc_button_11);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setPreferredSize(new Dimension(500, 1));
		separator_1.setMinimumSize(new Dimension(3, 2));
		separator_1.setForeground(Color.LIGHT_GRAY);
		separator_1.setBackground(Color.WHITE);
		GridBagConstraints gbc_separator_1 = new GridBagConstraints();
		gbc_separator_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator_1.gridwidth = 2;
		gbc_separator_1.insets = new Insets(0, 12, 5, 5);
		gbc_separator_1.gridx = 0;
		gbc_separator_1.gridy = 15;
		commentsPanel.add(separator_1, gbc_separator_1);
		
		JLabel label_6 = new JLabel("Alarms: ");
		label_6.setMinimumSize(new Dimension(125, 18));
		label_6.setFont(new Font("Tahoma", Font.ITALIC, 14));
		GridBagConstraints gbc_label_6 = new GridBagConstraints();
		gbc_label_6.fill = GridBagConstraints.HORIZONTAL;
		gbc_label_6.anchor = GridBagConstraints.WEST;
		gbc_label_6.gridwidth = 2;
		gbc_label_6.insets = new Insets(0, 25, 5, 5);
		gbc_label_6.gridx = 0;
		gbc_label_6.gridy = 16;
		commentsPanel.add(label_6, gbc_label_6);
		
		JLabel label_7 = new JLabel("Comment:");
		label_7.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_label_7 = new GridBagConstraints();
		gbc_label_7.anchor = GridBagConstraints.EAST;
		gbc_label_7.insets = new Insets(0, 0, 5, 5);
		gbc_label_7.gridx = 0;
		gbc_label_7.gridy = 17;
		commentsPanel.add(label_7, gbc_label_7);
		
		textAlarms = new JTextField();
		textAlarms.setPreferredSize(new Dimension(500, 20));
		textAlarms.setMinimumSize(new Dimension(500, 20));
		textAlarms.setColumns(200);
		GridBagConstraints gbc_textAlarms = new GridBagConstraints();
		gbc_textAlarms.insets = new Insets(0, 0, 5, 7);
		gbc_textAlarms.fill = GridBagConstraints.HORIZONTAL;
		gbc_textAlarms.gridx = 1;
		gbc_textAlarms.gridy = 17;
		commentsPanel.add(textAlarms, gbc_textAlarms);
		
		JButton button_6 = new JButton("?");
		button_6.setMargin(new Insets(2, 8, 2, 8));
		GridBagConstraints gbc_button_6 = new GridBagConstraints();
		gbc_button_6.insets = new Insets(0, 0, 5, 0);
		gbc_button_6.gridx = 2;
		gbc_button_6.gridy = 17;
		commentsPanel.add(button_6, gbc_button_6);
		
		JSeparator separator_4 = new JSeparator();
		separator_4.setPreferredSize(new Dimension(500, 1));
		separator_4.setMinimumSize(new Dimension(3, 2));
		separator_4.setForeground(Color.LIGHT_GRAY);
		separator_4.setBackground(Color.WHITE);
		GridBagConstraints gbc_separator_4 = new GridBagConstraints();
		gbc_separator_4.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator_4.gridwidth = 2;
		gbc_separator_4.insets = new Insets(0, 12, 5, 5);
		gbc_separator_4.gridx = 0;
		gbc_separator_4.gridy = 18;
		commentsPanel.add(separator_4, gbc_separator_4);
		
		JLabel label_8 = new JLabel("Bug2Go: ");
		label_8.setMinimumSize(new Dimension(125, 18));
		label_8.setFont(new Font("Tahoma", Font.ITALIC, 14));
		GridBagConstraints gbc_label_8 = new GridBagConstraints();
		gbc_label_8.fill = GridBagConstraints.HORIZONTAL;
		gbc_label_8.anchor = GridBagConstraints.WEST;
		gbc_label_8.gridwidth = 2;
		gbc_label_8.insets = new Insets(0, 25, 5, 5);
		gbc_label_8.gridx = 0;
		gbc_label_8.gridy = 19;
		commentsPanel.add(label_8, gbc_label_8);
		
		JLabel label_9 = new JLabel("Comment:");
		label_9.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_label_9 = new GridBagConstraints();
		gbc_label_9.anchor = GridBagConstraints.EAST;
		gbc_label_9.insets = new Insets(0, 0, 5, 5);
		gbc_label_9.gridx = 0;
		gbc_label_9.gridy = 20;
		commentsPanel.add(label_9, gbc_label_9);
		
		textB2g = new JTextField();
		textB2g.setPreferredSize(new Dimension(500, 20));
		textB2g.setMinimumSize(new Dimension(500, 20));
		textB2g.setColumns(200);
		GridBagConstraints gbc_textB2g = new GridBagConstraints();
		gbc_textB2g.insets = new Insets(0, 0, 5, 7);
		gbc_textB2g.fill = GridBagConstraints.HORIZONTAL;
		gbc_textB2g.gridx = 1;
		gbc_textB2g.gridy = 20;
		commentsPanel.add(textB2g, gbc_textB2g);
		
		
		JButton button_7 = new JButton("?");
		button_7.setMargin(new Insets(2, 8, 2, 8));
		GridBagConstraints gbc_button_7 = new GridBagConstraints();
		gbc_button_7.insets = new Insets(0, 0, 5, 0);
		gbc_button_7.gridx = 2;
		gbc_button_7.gridy = 20;
		commentsPanel.add(button_7, gbc_button_7);
		
		JSeparator separator_5 = new JSeparator();
		separator_5.setPreferredSize(new Dimension(500, 1));
		separator_5.setMinimumSize(new Dimension(3, 2));
		separator_5.setForeground(Color.LIGHT_GRAY);
		separator_5.setBackground(Color.WHITE);
		GridBagConstraints gbc_separator_5 = new GridBagConstraints();
		gbc_separator_5.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator_5.gridwidth = 2;
		gbc_separator_5.insets = new Insets(0, 12, 5, 5);
		gbc_separator_5.gridx = 0;
		gbc_separator_5.gridy = 21;
		commentsPanel.add(separator_5, gbc_separator_5);
		
		JLabel label_10 = new JLabel("Tethering: ");
		label_10.setMinimumSize(new Dimension(125, 18));
		label_10.setFont(new Font("Tahoma", Font.ITALIC, 14));
		GridBagConstraints gbc_label_10 = new GridBagConstraints();
		gbc_label_10.fill = GridBagConstraints.HORIZONTAL;
		gbc_label_10.anchor = GridBagConstraints.WEST;
		gbc_label_10.gridwidth = 2;
		gbc_label_10.insets = new Insets(0, 25, 5, 5);
		gbc_label_10.gridx = 0;
		gbc_label_10.gridy = 22;
		commentsPanel.add(label_10, gbc_label_10);
		
		JLabel label_11 = new JLabel("Comment:");
		label_11.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_label_11 = new GridBagConstraints();
		gbc_label_11.anchor = GridBagConstraints.EAST;
		gbc_label_11.insets = new Insets(0, 0, 5, 5);
		gbc_label_11.gridx = 0;
		gbc_label_11.gridy = 23;
		commentsPanel.add(label_11, gbc_label_11);
		
		textTether = new JTextField();
		textTether.setPreferredSize(new Dimension(500, 20));
		textTether.setMinimumSize(new Dimension(500, 20));
		textTether.setColumns(200);
		GridBagConstraints gbc_textTether = new GridBagConstraints();
		gbc_textTether.insets = new Insets(0, 0, 5, 7);
		gbc_textTether.fill = GridBagConstraints.HORIZONTAL;
		gbc_textTether.gridx = 1;
		gbc_textTether.gridy = 23;
		commentsPanel.add(textTether, gbc_textTether);
		
		JButton button_8 = new JButton("?");
		button_8.setMargin(new Insets(2, 8, 2, 8));
		GridBagConstraints gbc_button_8 = new GridBagConstraints();
		gbc_button_8.insets = new Insets(0, 0, 5, 0);
		gbc_button_8.gridx = 2;
		gbc_button_8.gridy = 23;
		commentsPanel.add(button_8, gbc_button_8);
		
		JSeparator separator_6 = new JSeparator();
		separator_6.setPreferredSize(new Dimension(500, 1));
		separator_6.setMinimumSize(new Dimension(3, 2));
		separator_6.setForeground(Color.LIGHT_GRAY);
		separator_6.setBackground(Color.WHITE);
		GridBagConstraints gbc_separator_6 = new GridBagConstraints();
		gbc_separator_6.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator_6.gridwidth = 2;
		gbc_separator_6.insets = new Insets(0, 12, 5, 5);
		gbc_separator_6.gridx = 0;
		gbc_separator_6.gridy = 24;
		commentsPanel.add(separator_6, gbc_separator_6);
		
		JLabel label_12 = new JLabel("Diag: ");
		label_12.setMinimumSize(new Dimension(125, 18));
		label_12.setFont(new Font("Tahoma", Font.ITALIC, 14));
		GridBagConstraints gbc_label_12 = new GridBagConstraints();
		gbc_label_12.fill = GridBagConstraints.HORIZONTAL;
		gbc_label_12.anchor = GridBagConstraints.WEST;
		gbc_label_12.gridwidth = 2;
		gbc_label_12.insets = new Insets(0, 25, 5, 5);
		gbc_label_12.gridx = 0;
		gbc_label_12.gridy = 25;
		commentsPanel.add(label_12, gbc_label_12);
		
		JLabel label_13 = new JLabel("Comment:");
		label_13.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_label_13 = new GridBagConstraints();
		gbc_label_13.anchor = GridBagConstraints.EAST;
		gbc_label_13.insets = new Insets(0, 0, 5, 5);
		gbc_label_13.gridx = 0;
		gbc_label_13.gridy = 26;
		commentsPanel.add(label_13, gbc_label_13);
		
		textDiag = new JTextField();
		textDiag.setPreferredSize(new Dimension(500, 20));
		textDiag.setMinimumSize(new Dimension(500, 20));
		textDiag.setColumns(200);
		GridBagConstraints gbc_textDiag = new GridBagConstraints();
		gbc_textDiag.insets = new Insets(0, 0, 5, 7);
		gbc_textDiag.fill = GridBagConstraints.HORIZONTAL;
		gbc_textDiag.gridx = 1;
		gbc_textDiag.gridy = 26;
		commentsPanel.add(textDiag, gbc_textDiag);
		
		JButton button_9 = new JButton("?");
		button_9.setMargin(new Insets(2, 8, 2, 8));
		GridBagConstraints gbc_button_9 = new GridBagConstraints();
		gbc_button_9.insets = new Insets(0, 0, 5, 0);
		gbc_button_9.gridx = 2;
		gbc_button_9.gridy = 26;
		commentsPanel.add(button_9, gbc_button_9);
		
		JPanel panel = new JPanel();
		panel.setBorder(null);
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 0, 5, 5);
		gbc_panel.gridwidth = 2;
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 27;
		commentsPanel.add(panel, gbc_panel);
		
		JButton btnSalvar = new JButton("Save");
		btnSalvar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					setComments();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (JDOMException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		panel.add(btnSalvar);
		
		JLabel lblParserOptions = new JLabel("Parser interface options:");
		lblParserOptions.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblParserOptions.setAlignmentX(0.5f);
		GridBagConstraints gbc_lblParserOptions = new GridBagConstraints();
		gbc_lblParserOptions.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblParserOptions.anchor = GridBagConstraints.WEST;
		gbc_lblParserOptions.gridwidth = 2;
		gbc_lblParserOptions.insets = new Insets(5, 5, 5, 5);
		gbc_lblParserOptions.gridx = 0;
		gbc_lblParserOptions.gridy = 29;
		commentsPanel.add(lblParserOptions, gbc_lblParserOptions);
		
		JLabel lblTextEditor = new JLabel("Text editor:");
		GridBagConstraints gbc_lblTextEditor = new GridBagConstraints();
		gbc_lblTextEditor.fill = GridBagConstraints.VERTICAL;
		gbc_lblTextEditor.anchor = GridBagConstraints.EAST;
		gbc_lblTextEditor.insets = new Insets(0, 0, 5, 5);
		gbc_lblTextEditor.gridx = 0;
		gbc_lblTextEditor.gridy = 30;
		commentsPanel.add(lblTextEditor, gbc_lblTextEditor);
		
		JPanel panel_2 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_2.getLayout();
		flowLayout.setVgap(1);
		flowLayout.setHgap(1);
		flowLayout.setAlignment(FlowLayout.LEFT);
		panel_2.setPreferredSize(new Dimension(10, 25));
		panel_2.setMinimumSize(new Dimension(10, 25));
		panel_2.setMaximumSize(new Dimension(32767, 30));
		panel_2.setBorder(null);
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.insets = new Insets(0, 0, 5, 5);
		gbc_panel_2.fill = GridBagConstraints.BOTH;
		gbc_panel_2.gridx = 1;
		gbc_panel_2.gridy = 30;
		commentsPanel.add(panel_2, gbc_panel_2);
		
		rdbtnTAnalisys = new JRadioButton("TextAnalysis");
		rdbtnTAnalisys.setToolTipText("Use TextAnalysis tool as default text editor");
		rdbtnTAnalisys.setSelected(true);
		panel_2.add(rdbtnTAnalisys);
		
		rdbtnNotepad = new JRadioButton("Notepad++");
		rdbtnNotepad.setToolTipText("Use Notepad++ as default text editor");
		panel_2.add(rdbtnNotepad);
		
		ButtonGroup editorSelector = new ButtonGroup();
		editorSelector.add(rdbtnTAnalisys);
		editorSelector.add(rdbtnNotepad);
		
		JLabel lblWordWrap = new JLabel("Word Wrap:");
		lblWordWrap.setHorizontalAlignment(SwingConstants.RIGHT);
		lblWordWrap.setMinimumSize(new Dimension(55, 23));
		lblWordWrap.setPreferredSize(new Dimension(55, 23));
		GridBagConstraints gbc_lblWordWrap = new GridBagConstraints();
		gbc_lblWordWrap.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblWordWrap.anchor = GridBagConstraints.EAST;
		gbc_lblWordWrap.insets = new Insets(0, 0, 5, 5);
		gbc_lblWordWrap.gridx = 0;
		gbc_lblWordWrap.gridy = 31;
		commentsPanel.add(lblWordWrap, gbc_lblWordWrap);
		
		chkTextWrap = new JCheckBox("");
		chkTextWrap.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				if (arg0.getStateChange() == ItemEvent.SELECTED)
					BaseWindow.getParser().getTextPane().setWrapText(true);
				else
					BaseWindow.getParser().getTextPane().setWrapText(false);
			}
		});
		GridBagConstraints gbc_chkTextWrap = new GridBagConstraints();
		gbc_chkTextWrap.anchor = GridBagConstraints.WEST;
		gbc_chkTextWrap.insets = new Insets(0, 0, 5, 5);
		gbc_chkTextWrap.gridx = 1;
		gbc_chkTextWrap.gridy = 31;
		commentsPanel.add(chkTextWrap, gbc_chkTextWrap);
		
		JLabel lblCustomfilters = new JLabel("More options:");
		lblCustomfilters.setPreferredSize(new Dimension(55, 23));
		lblCustomfilters.setMinimumSize(new Dimension(55, 23));
		lblCustomfilters.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_lblCustomfilters = new GridBagConstraints();
		gbc_lblCustomfilters.anchor = GridBagConstraints.EAST;
		gbc_lblCustomfilters.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblCustomfilters.insets = new Insets(0, 0, 5, 5);
		gbc_lblCustomfilters.gridx = 0;
		gbc_lblCustomfilters.gridy = 32;
		commentsPanel.add(lblCustomfilters, gbc_lblCustomfilters);
		
		JPanel morePane = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) morePane.getLayout();
		flowLayout_1.setVgap(1);
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		GridBagConstraints gbc_morePane = new GridBagConstraints();
		gbc_morePane.insets = new Insets(0, 0, 5, 5);
		gbc_morePane.fill = GridBagConstraints.BOTH;
		gbc_morePane.gridx = 1;
		gbc_morePane.gridy = 32;
		commentsPanel.add(morePane, gbc_morePane);
		
		JButton btnManageFilters = new JButton("Manage Filters");
		btnManageFilters.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BaseWindow.getCustomFiltersPane().open();
			}
		});
		morePane.add(btnManageFilters);
		
		JButton btnAdvanced = new JButton("Advanced");
		btnAdvanced.setPreferredSize(new Dimension(103, 23));
		morePane.add(btnAdvanced);
		btnAdvanced.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				advOptions.setLocation(BaseWindow.getLocation().x+200, BaseWindow.getLocation().y + 200);
				advOptions.setVisible(true);
			}
		});
		btnAdvanced.setToolTipText("Click to see advanced options");
		
		advOptions = new AdvancedOptionsPane(BaseWindow);
		
		try {
			getComments();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void getComments() throws IOException, JDOMException{
		//Abre o arquivo XML
		File xmlFile = new File("Data/cfgs/user_cfg.xml");
		
		//Cria o builder da estrutura XML
		SAXBuilder builder = new SAXBuilder();
		
		//Cria documento formatado de acordo com a lib XML
		Document document = (Document) builder.build(xmlFile);

		//Pega o nó raiz do XML
		Element satNode = document.getRootElement();
		
		//Gera lista de filhos do nó root
		//List<Element> satElements = satNode.getChildren();
		
		//Pega o nó referente ao option pane
		Element optionPaneNode = satNode.getChild("option_pane"); 
		for(Element e : optionPaneNode.getChildren()){
			if(e.getName().equals("full_log")){
				textConsumeFull.setText(e.getValue());
				
			} else if(e.getName().equals("screen_off")){
				textConsumeOff.setText(e.getValue());
				
			} else if(e.getName().equals("screen_on")){
				textConsumeOn.setText(e.getValue());
				
			} else if(e.getName().equals("high_current")){
				textHighCurrent.setText(e.getValue());
				
			} else if(e.getName().equals("krnl_wkl")){
				textKernelWake.setText(e.getValue());
				
			} else if(e.getName().equals("java_wkl")){
				textJavaWake.setText(e.getValue());
				
			} else if(e.getName().equals("suspicious_header")){
				textSuspiciousHeader.setText(e.getValue());
				
			} else if(e.getName().equals("suspicious")){
				textSuspicious.setText(e.getValue());
				
			} else if(e.getName().equals("alarms")){
				textAlarms.setText(e.getValue());
				
			} else if(e.getName().equals("b2g")){
				textB2g.setText(e.getValue());
				
			} else if(e.getName().equals("tether")){
				textTether.setText(e.getValue());
				
			} else if(e.getName().equals("diag")){
				textDiag.setText(e.getValue());
				
			} else if(e.getName().equals("editor")){
				if(e.getValue().equals("0"))
					rdbtnTAnalisys.setSelected(true);
				else
					rdbtnNotepad.setSelected(true);
				
			} else if(e.getName().equals("wwrap")){
				if(e.getValue().equals("0"))
					chkTextWrap.setSelected(false);
				else
					chkTextWrap.setSelected(true);
				
			}
		}
		System.out.println("Options Loaded");
		
	}
	
	public void setComments() throws IOException, JDOMException {
		//Abre o arquivo XML
		File xmlFile = new File("Data/cfgs/user_cfg.xml");
		
		//Cria o builder da estrutura XML
		SAXBuilder builder = new SAXBuilder();
		
		//Cria documento formatado de acordo com a lib XML
		Document document = (Document) builder.build(xmlFile);
		
		//Pega o nó raiz do XML
		Element satNode = document.getRootElement();
		
		//Gera lista de filhos do nó root
		//List<Element> satElements = satNode.getChildren();
		
		//Pega o nó referente ao option pane
		Element optionPaneNode = satNode.getChild("option_pane"); 
		for(Element e : optionPaneNode.getChildren()){
			if(e.getName().equals("full_log")){
				e.setText(textConsumeFull.getText());
				
			} else if(e.getName().equals("screen_off")){
				e.setText(textConsumeOff.getText());
				
			} else if(e.getName().equals("screen_on")){
				e.setText(textConsumeOn.getText());
				
			} else if(e.getName().equals("high_current")){
				e.setText(textHighCurrent.getText());
				
			} else if(e.getName().equals("krnl_wkl")){
				e.setText(textKernelWake.getText());
				
			} else if(e.getName().equals("java_wkl")){
				e.setText(textJavaWake.getText());
				
			} else if(e.getName().equals("suspicious_header")){
				e.setText(textSuspiciousHeader.getText());
				
			} else if(e.getName().equals("suspicious")){
				e.setText(textSuspicious.getText());
				
			} else if(e.getName().equals("alarms")){
				e.setText(textAlarms.getText());
				
			} else if(e.getName().equals("b2g")){
				e.setText(textB2g.getText());
				
			} else if(e.getName().equals("tether")){
				e.setText(textTether.getText());
				
			} else if(e.getName().equals("diag")){
				e.setText(textDiag.getText());
				
			} else if(e.getName().equals("editor")){
				if(rdbtnNotepad.isSelected())
					e.setText("1");
				else
					e.setText("0");
				
			} else if(e.getName().equals("wwrap")){
				if(chkTextWrap.isSelected())
					e.setText("1");
				else
					e.setText("0");
				
			}
		}
		
		//JDOM document is ready now, lets write it to file now
        XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
        //output xml to console for debugging
        //xmlOutputter.output(doc, System.out);
        xmlOutputter.output(document, new FileOutputStream(xmlFile));
        
		System.out.println("Options Saved");
	}
	
	
	//Getters
	public String getTextConsumeOn() {
		return textConsumeOn.getText();
	}


	public String getTextConsumeOff() {
		return textConsumeOff.getText();
	}


	public String getTextConsumeFull() {
		return textConsumeFull.getText();
	}

	public String getTextSuspiciousHeader() {
		return textSuspiciousHeader.getText();
	}
	
	public String getTextSuspicious() {
		return textSuspicious.getText();
	}


	public String getTextKernel() {
		return textKernelWake.getText();
	}


	public String getTextJava() {
		return textJavaWake.getText();
	}


	public String getTextAlarms() {
		return textAlarms.getText();
	}


	public String getTextB2g() {
		return textB2g.getText();
	}


	public String getTextTether() {
		return textTether.getText();
	}

	public String getTextDiag() {
		return textDiag.getText();
		
	}
	
	public String getTextHighCurrent() {
		return textHighCurrent.getText();
	}
	
	public AdvancedOptionsPane getAdvOptions() {
		return advOptions;
	}
	
	protected JRadioButton getRdbtnNotepad() {
		return rdbtnNotepad;
	}
	
	protected JCheckBox getChkTextWrap() {
		return chkTextWrap;
	}
	
	protected JRadioButton getRdbtnTAnalisys() {
		return rdbtnTAnalisys;
	}
}
