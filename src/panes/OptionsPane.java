package panes;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.jdom2.JDOMException;

import core.Logger;
import core.SharedObjs;
import core.XmlMngr;


@SuppressWarnings("serial")
public class OptionsPane extends JPanel
{
	private JTextField	 textConsumeFull;
	private JTextField	 textConsumeOff;
	private JTextField	 textConsumeOn;
	private JTextField	 textHighCurrent;
	private JTextField	 textKernelWake;
	private JTextField	 textJavaWake;
	private JTextField	 textSuspiciousHeader;
	private JTextField	 textSuspicious;
	private JTextField	 textAlarms;
	private JTextField	 textB2g;
	private JTextField	 textTether;
	private JTextField	 textDiag;
	private JRadioButton rdbtnSingleclick;
	private JRadioButton rdbtnDouble;
	private JRadioButton rdbtnTAnalisys;
	private JRadioButton rdbtnNotepad;
	private JCheckBox	 chkTextWrap;
	private JButton		 btnManageFilters;
	private JButton		 btnAdvanced;
	
	/**
	 * Create the panel.
	 */
	public OptionsPane()
	{
		setMinimumSize(new Dimension(800, 600));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {918};
		gridBagLayout.rowHeights = new int[] {590};
		gridBagLayout.columnWeights = new double[] {1.0};
		gridBagLayout.rowWeights = new double[] {1.0};
		setLayout(gridBagLayout);
		ButtonGroup editorSelector = new ButtonGroup();
		ButtonGroup breakdownSelector = new ButtonGroup();
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		add(scrollPane, gbc_scrollPane);
		JPanel panel = new JPanel();
		panel.setMinimumSize(new Dimension(700, 10));
		panel.setBorder(null);
		scrollPane.setViewportView(panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] {150, 450, 30, 0};
		gbl_panel.rowHeights = new int[] {0,
										  20,
										  0,
										  0,
										  0,
										  0,
										  0,
										  0,
										  0,
										  0,
										  0,
										  0,
										  2,
										  0,
										  0,
										  0,
										  0,
										  0,
										  0,
										  0,
										  0,
										  0,
										  0,
										  0,
										  0,
										  0,
										  0,
										  0,
										  0,
										  0,
										  0,
										  0,
										  0,
										  0};
		gbl_panel.columnWeights = new double[] {0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[] {0.0,
											 0.0,
											 0.0,
											 0.0,
											 0.0,
											 0.0,
											 0.0,
											 0.0,
											 0.0,
											 0.0,
											 0.0,
											 0.0,
											 0.0,
											 0.0,
											 0.0,
											 0.0,
											 0.0,
											 0.0,
											 0.0,
											 0.0,
											 0.0,
											 0.0,
											 0.0,
											 0.0,
											 0.0,
											 0.0,
											 0.0,
											 0.0,
											 0.0,
											 0.0,
											 0.0,
											 0.0,
											 0.0,
											 Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		JLabel label_3 = new JLabel("Options:");
		label_3.setFont(new Font("Tahoma", Font.BOLD, 24));
		GridBagConstraints gbc_label_3 = new GridBagConstraints();
		gbc_label_3.gridwidth = 3;
		gbc_label_3.insets = new Insets(10, 10, 10, 10);
		gbc_label_3.gridx = 0;
		gbc_label_3.gridy = 0;
		panel.add(label_3, gbc_label_3);
		
		JLabel label_4 = new JLabel("Comments personalization:");
		label_4.setFont(new Font("Tahoma", Font.BOLD, 16));
		label_4.setAlignmentX(0.5f);
		GridBagConstraints gbc_label_4 = new GridBagConstraints();
		gbc_label_4.fill = GridBagConstraints.HORIZONTAL;
		gbc_label_4.anchor = GridBagConstraints.WEST;
		gbc_label_4.gridwidth = 2;
		gbc_label_4.insets = new Insets(5, 5, 5, 5);
		gbc_label_4.gridx = 0;
		gbc_label_4.gridy = 1;
		panel.add(label_4, gbc_label_4);
		
		JLabel label_5 = new JLabel("High consumption apps:");
		label_5.setMinimumSize(new Dimension(96, 18));
		label_5.setFont(new Font("Tahoma", Font.ITALIC, 14));
		label_5.setAlignmentX(0.5f);
		GridBagConstraints gbc_label_5 = new GridBagConstraints();
		gbc_label_5.fill = GridBagConstraints.HORIZONTAL;
		gbc_label_5.anchor = GridBagConstraints.WEST;
		gbc_label_5.gridwidth = 2;
		gbc_label_5.insets = new Insets(10, 25, 5, 5);
		gbc_label_5.gridx = 0;
		gbc_label_5.gridy = 2;
		panel.add(label_5, gbc_label_5);
		
		JLabel label_19 = new JLabel("Full log:");
		label_19.setMaximumSize(new Dimension(100, 14));
		label_19.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_label_19 = new GridBagConstraints();
		gbc_label_19.anchor = GridBagConstraints.EAST;
		gbc_label_19.insets = new Insets(0, 0, 5, 5);
		gbc_label_19.gridx = 0;
		gbc_label_19.gridy = 3;
		panel.add(label_19, gbc_label_19);
		
		textConsumeFull = new JTextField();
		textConsumeFull.setColumns(10);
		GridBagConstraints gbc_textConsumeFull = new GridBagConstraints();
		gbc_textConsumeFull.insets = new Insets(0, 0, 5, 5);
		gbc_textConsumeFull.fill = GridBagConstraints.HORIZONTAL;
		gbc_textConsumeFull.gridx = 1;
		gbc_textConsumeFull.gridy = 3;
		panel.add(textConsumeFull, gbc_textConsumeFull);
		
		JButton button_12 = new JButton("?");
		button_12.setMargin(new Insets(2, 8, 2, 8));
		GridBagConstraints gbc_button_12 = new GridBagConstraints();
		gbc_button_12.insets = new Insets(0, 0, 5, 0);
		gbc_button_12.gridx = 2;
		gbc_button_12.gridy = 3;
		panel.add(button_12, gbc_button_12);
		
		JLabel label_20 = new JLabel("Screen Off:");
		label_20.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_label_20 = new GridBagConstraints();
		gbc_label_20.anchor = GridBagConstraints.EAST;
		gbc_label_20.insets = new Insets(0, 0, 5, 5);
		gbc_label_20.gridx = 0;
		gbc_label_20.gridy = 4;
		panel.add(label_20, gbc_label_20);
		
		textConsumeOff = new JTextField();
		textConsumeOff.setColumns(10);
		GridBagConstraints gbc_textConsumeOff = new GridBagConstraints();
		gbc_textConsumeOff.insets = new Insets(0, 0, 5, 5);
		gbc_textConsumeOff.fill = GridBagConstraints.HORIZONTAL;
		gbc_textConsumeOff.gridx = 1;
		gbc_textConsumeOff.gridy = 4;
		panel.add(textConsumeOff, gbc_textConsumeOff);
		
		JButton button_13 = new JButton("?");
		button_13.setMargin(new Insets(2, 8, 2, 8));
		GridBagConstraints gbc_button_13 = new GridBagConstraints();
		gbc_button_13.insets = new Insets(0, 0, 5, 0);
		gbc_button_13.gridx = 2;
		gbc_button_13.gridy = 4;
		panel.add(button_13, gbc_button_13);
		
		JLabel label_21 = new JLabel("Screen On:");
		label_21.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_label_21 = new GridBagConstraints();
		gbc_label_21.anchor = GridBagConstraints.EAST;
		gbc_label_21.insets = new Insets(0, 0, 5, 5);
		gbc_label_21.gridx = 0;
		gbc_label_21.gridy = 5;
		panel.add(label_21, gbc_label_21);
		
		textConsumeOn = new JTextField();
		textConsumeOn.setColumns(10);
		GridBagConstraints gbc_textConsumeOn = new GridBagConstraints();
		gbc_textConsumeOn.insets = new Insets(0, 0, 5, 5);
		gbc_textConsumeOn.fill = GridBagConstraints.HORIZONTAL;
		gbc_textConsumeOn.gridx = 1;
		gbc_textConsumeOn.gridy = 5;
		panel.add(textConsumeOn, gbc_textConsumeOn);
		
		JButton button_14 = new JButton("?");
		button_14.setMargin(new Insets(2, 8, 2, 8));
		GridBagConstraints gbc_button_14 = new GridBagConstraints();
		gbc_button_14.insets = new Insets(0, 0, 5, 0);
		gbc_button_14.gridx = 2;
		gbc_button_14.gridy = 5;
		panel.add(button_14, gbc_button_14);
		
		JSeparator separator_2 = new JSeparator();
		separator_2.setPreferredSize(new Dimension(500, 1));
		separator_2.setMinimumSize(new Dimension(3, 2));
		separator_2.setForeground(Color.LIGHT_GRAY);
		separator_2.setBackground(Color.WHITE);
		GridBagConstraints gbc_separator_2 = new GridBagConstraints();
		gbc_separator_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator_2.gridwidth = 2;
		gbc_separator_2.insets = new Insets(0, 12, 5, 5);
		gbc_separator_2.gridx = 0;
		gbc_separator_2.gridy = 6;
		panel.add(separator_2, gbc_separator_2);
		
		JLabel label_22 = new JLabel("General Issues:");
		label_22.setMinimumSize(new Dimension(100, 18));
		label_22.setFont(new Font("Tahoma", Font.ITALIC, 14));
		GridBagConstraints gbc_label_22 = new GridBagConstraints();
		gbc_label_22.fill = GridBagConstraints.HORIZONTAL;
		gbc_label_22.gridwidth = 2;
		gbc_label_22.insets = new Insets(0, 25, 5, 5);
		gbc_label_22.gridx = 0;
		gbc_label_22.gridy = 7;
		panel.add(label_22, gbc_label_22);
		
		JLabel label_23 = new JLabel("High Current:");
		label_23.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_label_23 = new GridBagConstraints();
		gbc_label_23.anchor = GridBagConstraints.EAST;
		gbc_label_23.insets = new Insets(0, 0, 5, 5);
		gbc_label_23.gridx = 0;
		gbc_label_23.gridy = 8;
		panel.add(label_23, gbc_label_23);
		
		textHighCurrent = new JTextField();
		textHighCurrent.setColumns(10);
		GridBagConstraints gbc_textHighCurrent = new GridBagConstraints();
		gbc_textHighCurrent.insets = new Insets(0, 0, 5, 5);
		gbc_textHighCurrent.fill = GridBagConstraints.HORIZONTAL;
		gbc_textHighCurrent.gridx = 1;
		gbc_textHighCurrent.gridy = 8;
		panel.add(textHighCurrent, gbc_textHighCurrent);
		
		JButton button_15 = new JButton("?");
		button_15.setMargin(new Insets(2, 8, 2, 8));
		GridBagConstraints gbc_button_15 = new GridBagConstraints();
		gbc_button_15.insets = new Insets(0, 0, 5, 0);
		gbc_button_15.gridx = 2;
		gbc_button_15.gridy = 8;
		panel.add(button_15, gbc_button_15);
		
		JLabel label_24 = new JLabel("Kernel Wakelocks:");
		label_24.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_label_24 = new GridBagConstraints();
		gbc_label_24.anchor = GridBagConstraints.EAST;
		gbc_label_24.insets = new Insets(0, 0, 5, 5);
		gbc_label_24.gridx = 0;
		gbc_label_24.gridy = 9;
		panel.add(label_24, gbc_label_24);
		
		textKernelWake = new JTextField();
		textKernelWake.setColumns(10);
		GridBagConstraints gbc_textKernelWake = new GridBagConstraints();
		gbc_textKernelWake.insets = new Insets(0, 0, 5, 5);
		gbc_textKernelWake.fill = GridBagConstraints.HORIZONTAL;
		gbc_textKernelWake.gridx = 1;
		gbc_textKernelWake.gridy = 9;
		panel.add(textKernelWake, gbc_textKernelWake);
		
		JButton button_16 = new JButton("?");
		button_16.setMargin(new Insets(2, 8, 2, 8));
		GridBagConstraints gbc_button_16 = new GridBagConstraints();
		gbc_button_16.insets = new Insets(0, 0, 5, 0);
		gbc_button_16.gridx = 2;
		gbc_button_16.gridy = 9;
		panel.add(button_16, gbc_button_16);
		
		JLabel label_25 = new JLabel("Java Wakelocks:");
		label_25.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_label_25 = new GridBagConstraints();
		gbc_label_25.anchor = GridBagConstraints.EAST;
		gbc_label_25.insets = new Insets(0, 0, 5, 5);
		gbc_label_25.gridx = 0;
		gbc_label_25.gridy = 10;
		panel.add(label_25, gbc_label_25);
		
		textJavaWake = new JTextField();
		textJavaWake.setColumns(10);
		GridBagConstraints gbc_textJavaWake = new GridBagConstraints();
		gbc_textJavaWake.insets = new Insets(0, 0, 5, 5);
		gbc_textJavaWake.fill = GridBagConstraints.HORIZONTAL;
		gbc_textJavaWake.gridx = 1;
		gbc_textJavaWake.gridy = 10;
		panel.add(textJavaWake, gbc_textJavaWake);
		
		JButton button_17 = new JButton("?");
		button_17.setMargin(new Insets(2, 8, 2, 8));
		GridBagConstraints gbc_button_17 = new GridBagConstraints();
		gbc_button_17.insets = new Insets(0, 0, 5, 0);
		gbc_button_17.gridx = 2;
		gbc_button_17.gridy = 10;
		panel.add(button_17, gbc_button_17);
		
		JSeparator separator_3 = new JSeparator();
		separator_3.setPreferredSize(new Dimension(500, 1));
		separator_3.setMinimumSize(new Dimension(3, 2));
		separator_3.setForeground(Color.LIGHT_GRAY);
		separator_3.setBackground(Color.WHITE);
		GridBagConstraints gbc_separator_3 = new GridBagConstraints();
		gbc_separator_3.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator_3.gridwidth = 2;
		gbc_separator_3.insets = new Insets(0, 12, 5, 5);
		gbc_separator_3.gridx = 0;
		gbc_separator_3.gridy = 11;
		panel.add(separator_3, gbc_separator_3);
		
		JLabel label_26 = new JLabel("Suspicious:");
		label_26.setMinimumSize(new Dimension(100, 18));
		label_26.setFont(new Font("Tahoma", Font.ITALIC, 14));
		GridBagConstraints gbc_label_26 = new GridBagConstraints();
		gbc_label_26.fill = GridBagConstraints.HORIZONTAL;
		gbc_label_26.anchor = GridBagConstraints.WEST;
		gbc_label_26.gridwidth = 2;
		gbc_label_26.insets = new Insets(0, 25, 5, 5);
		gbc_label_26.gridx = 0;
		gbc_label_26.gridy = 12;
		panel.add(label_26, gbc_label_26);
		
		JLabel label_27 = new JLabel("Header:");
		label_27.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_label_27 = new GridBagConstraints();
		gbc_label_27.anchor = GridBagConstraints.EAST;
		gbc_label_27.insets = new Insets(0, 0, 5, 5);
		gbc_label_27.gridx = 0;
		gbc_label_27.gridy = 13;
		panel.add(label_27, gbc_label_27);
		
		textSuspiciousHeader = new JTextField();
		textSuspiciousHeader.setColumns(10);
		GridBagConstraints gbc_textSuspiciousHeader = new GridBagConstraints();
		gbc_textSuspiciousHeader.insets = new Insets(0, 0, 5, 5);
		gbc_textSuspiciousHeader.fill = GridBagConstraints.HORIZONTAL;
		gbc_textSuspiciousHeader.gridx = 1;
		gbc_textSuspiciousHeader.gridy = 13;
		panel.add(textSuspiciousHeader, gbc_textSuspiciousHeader);
		
		JButton button_18 = new JButton("?");
		button_18.setMargin(new Insets(2, 8, 2, 8));
		GridBagConstraints gbc_button_18 = new GridBagConstraints();
		gbc_button_18.insets = new Insets(0, 0, 5, 0);
		gbc_button_18.gridx = 2;
		gbc_button_18.gridy = 13;
		panel.add(button_18, gbc_button_18);
		
		JLabel label_28 = new JLabel("Comment:");
		label_28.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_label_28 = new GridBagConstraints();
		gbc_label_28.anchor = GridBagConstraints.EAST;
		gbc_label_28.insets = new Insets(0, 0, 5, 5);
		gbc_label_28.gridx = 0;
		gbc_label_28.gridy = 14;
		panel.add(label_28, gbc_label_28);
		
		textSuspicious = new JTextField();
		textSuspicious.setColumns(10);
		GridBagConstraints gbc_textSuspicious = new GridBagConstraints();
		gbc_textSuspicious.insets = new Insets(0, 0, 5, 5);
		gbc_textSuspicious.fill = GridBagConstraints.HORIZONTAL;
		gbc_textSuspicious.gridx = 1;
		gbc_textSuspicious.gridy = 14;
		panel.add(textSuspicious, gbc_textSuspicious);
		
		JButton button_19 = new JButton("?");
		button_19.setMargin(new Insets(2, 8, 2, 8));
		GridBagConstraints gbc_button_19 = new GridBagConstraints();
		gbc_button_19.insets = new Insets(0, 0, 5, 0);
		gbc_button_19.gridx = 2;
		gbc_button_19.gridy = 14;
		panel.add(button_19, gbc_button_19);
		
		JSeparator separator_7 = new JSeparator();
		separator_7.setPreferredSize(new Dimension(500, 1));
		separator_7.setMinimumSize(new Dimension(3, 2));
		separator_7.setForeground(Color.LIGHT_GRAY);
		separator_7.setBackground(Color.WHITE);
		GridBagConstraints gbc_separator_7 = new GridBagConstraints();
		gbc_separator_7.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator_7.gridwidth = 2;
		gbc_separator_7.insets = new Insets(0, 12, 5, 5);
		gbc_separator_7.gridx = 0;
		gbc_separator_7.gridy = 15;
		panel.add(separator_7, gbc_separator_7);
		
		JLabel label_29 = new JLabel("Alarms: ");
		label_29.setMinimumSize(new Dimension(125, 18));
		label_29.setFont(new Font("Tahoma", Font.ITALIC, 14));
		GridBagConstraints gbc_label_29 = new GridBagConstraints();
		gbc_label_29.fill = GridBagConstraints.HORIZONTAL;
		gbc_label_29.anchor = GridBagConstraints.WEST;
		gbc_label_29.gridwidth = 2;
		gbc_label_29.insets = new Insets(0, 25, 5, 5);
		gbc_label_29.gridx = 0;
		gbc_label_29.gridy = 16;
		panel.add(label_29, gbc_label_29);
		
		JLabel label_30 = new JLabel("Comment:");
		label_30.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_label_30 = new GridBagConstraints();
		gbc_label_30.anchor = GridBagConstraints.EAST;
		gbc_label_30.insets = new Insets(0, 0, 5, 5);
		gbc_label_30.gridx = 0;
		gbc_label_30.gridy = 17;
		panel.add(label_30, gbc_label_30);
		
		textAlarms = new JTextField();
		textAlarms.setColumns(10);
		GridBagConstraints gbc_textAlarms = new GridBagConstraints();
		gbc_textAlarms.insets = new Insets(0, 0, 5, 5);
		gbc_textAlarms.fill = GridBagConstraints.HORIZONTAL;
		gbc_textAlarms.gridx = 1;
		gbc_textAlarms.gridy = 17;
		panel.add(textAlarms, gbc_textAlarms);
		
		JButton button_20 = new JButton("?");
		button_20.setMargin(new Insets(2, 8, 2, 8));
		GridBagConstraints gbc_button_20 = new GridBagConstraints();
		gbc_button_20.insets = new Insets(0, 0, 5, 0);
		gbc_button_20.gridx = 2;
		gbc_button_20.gridy = 17;
		panel.add(button_20, gbc_button_20);
		
		JSeparator separator_9 = new JSeparator();
		separator_9.setPreferredSize(new Dimension(500, 1));
		separator_9.setMinimumSize(new Dimension(3, 2));
		separator_9.setForeground(Color.LIGHT_GRAY);
		separator_9.setBackground(Color.WHITE);
		GridBagConstraints gbc_separator_9 = new GridBagConstraints();
		gbc_separator_9.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator_9.gridwidth = 2;
		gbc_separator_9.insets = new Insets(0, 12, 5, 5);
		gbc_separator_9.gridx = 0;
		gbc_separator_9.gridy = 18;
		panel.add(separator_9, gbc_separator_9);
		
		JLabel label_31 = new JLabel("Bug2Go: ");
		label_31.setMinimumSize(new Dimension(125, 18));
		label_31.setFont(new Font("Tahoma", Font.ITALIC, 14));
		GridBagConstraints gbc_label_31 = new GridBagConstraints();
		gbc_label_31.fill = GridBagConstraints.HORIZONTAL;
		gbc_label_31.anchor = GridBagConstraints.WEST;
		gbc_label_31.gridwidth = 2;
		gbc_label_31.insets = new Insets(0, 25, 5, 5);
		gbc_label_31.gridx = 0;
		gbc_label_31.gridy = 19;
		panel.add(label_31, gbc_label_31);
		
		JLabel label_32 = new JLabel("Comment:");
		label_32.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_label_32 = new GridBagConstraints();
		gbc_label_32.anchor = GridBagConstraints.EAST;
		gbc_label_32.insets = new Insets(0, 0, 5, 5);
		gbc_label_32.gridx = 0;
		gbc_label_32.gridy = 20;
		panel.add(label_32, gbc_label_32);
		
		textB2g = new JTextField();
		textB2g.setColumns(10);
		GridBagConstraints gbc_textB2g = new GridBagConstraints();
		gbc_textB2g.insets = new Insets(0, 0, 5, 5);
		gbc_textB2g.fill = GridBagConstraints.HORIZONTAL;
		gbc_textB2g.gridx = 1;
		gbc_textB2g.gridy = 20;
		panel.add(textB2g, gbc_textB2g);
		
		JButton button_21 = new JButton("?");
		button_21.setMargin(new Insets(2, 8, 2, 8));
		GridBagConstraints gbc_button_21 = new GridBagConstraints();
		gbc_button_21.insets = new Insets(0, 0, 5, 0);
		gbc_button_21.gridx = 2;
		gbc_button_21.gridy = 20;
		panel.add(button_21, gbc_button_21);
		
		JSeparator separator_10 = new JSeparator();
		separator_10.setPreferredSize(new Dimension(500, 1));
		separator_10.setMinimumSize(new Dimension(3, 2));
		separator_10.setForeground(Color.LIGHT_GRAY);
		separator_10.setBackground(Color.WHITE);
		GridBagConstraints gbc_separator_10 = new GridBagConstraints();
		gbc_separator_10.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator_10.gridwidth = 2;
		gbc_separator_10.insets = new Insets(0, 12, 5, 5);
		gbc_separator_10.gridx = 0;
		gbc_separator_10.gridy = 21;
		panel.add(separator_10, gbc_separator_10);
		
		JLabel label_33 = new JLabel("Tethering: ");
		label_33.setMinimumSize(new Dimension(125, 18));
		label_33.setFont(new Font("Tahoma", Font.ITALIC, 14));
		GridBagConstraints gbc_label_33 = new GridBagConstraints();
		gbc_label_33.fill = GridBagConstraints.HORIZONTAL;
		gbc_label_33.anchor = GridBagConstraints.WEST;
		gbc_label_33.gridwidth = 2;
		gbc_label_33.insets = new Insets(0, 25, 5, 5);
		gbc_label_33.gridx = 0;
		gbc_label_33.gridy = 22;
		panel.add(label_33, gbc_label_33);
		
		JLabel label_34 = new JLabel("Comment:");
		label_34.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_label_34 = new GridBagConstraints();
		gbc_label_34.anchor = GridBagConstraints.EAST;
		gbc_label_34.insets = new Insets(0, 0, 5, 5);
		gbc_label_34.gridx = 0;
		gbc_label_34.gridy = 23;
		panel.add(label_34, gbc_label_34);
		
		textTether = new JTextField();
		textTether.setColumns(10);
		GridBagConstraints gbc_textTether = new GridBagConstraints();
		gbc_textTether.insets = new Insets(0, 0, 5, 5);
		gbc_textTether.fill = GridBagConstraints.HORIZONTAL;
		gbc_textTether.gridx = 1;
		gbc_textTether.gridy = 23;
		panel.add(textTether, gbc_textTether);
		
		JButton button_22 = new JButton("?");
		button_22.setMargin(new Insets(2, 8, 2, 8));
		GridBagConstraints gbc_button_22 = new GridBagConstraints();
		gbc_button_22.insets = new Insets(0, 0, 5, 0);
		gbc_button_22.gridx = 2;
		gbc_button_22.gridy = 23;
		panel.add(button_22, gbc_button_22);
		
		JSeparator separator_11 = new JSeparator();
		separator_11.setPreferredSize(new Dimension(500, 1));
		separator_11.setMinimumSize(new Dimension(3, 2));
		separator_11.setForeground(Color.LIGHT_GRAY);
		separator_11.setBackground(Color.WHITE);
		GridBagConstraints gbc_separator_11 = new GridBagConstraints();
		gbc_separator_11.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator_11.gridwidth = 2;
		gbc_separator_11.insets = new Insets(0, 12, 5, 5);
		gbc_separator_11.gridx = 0;
		gbc_separator_11.gridy = 24;
		panel.add(separator_11, gbc_separator_11);
		
		JLabel label_35 = new JLabel("Diag: ");
		label_35.setMinimumSize(new Dimension(125, 18));
		label_35.setFont(new Font("Tahoma", Font.ITALIC, 14));
		GridBagConstraints gbc_label_35 = new GridBagConstraints();
		gbc_label_35.fill = GridBagConstraints.HORIZONTAL;
		gbc_label_35.anchor = GridBagConstraints.WEST;
		gbc_label_35.gridwidth = 2;
		gbc_label_35.insets = new Insets(0, 25, 5, 5);
		gbc_label_35.gridx = 0;
		gbc_label_35.gridy = 25;
		panel.add(label_35, gbc_label_35);
		
		JLabel label_36 = new JLabel("Comment:");
		label_36.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_label_36 = new GridBagConstraints();
		gbc_label_36.anchor = GridBagConstraints.EAST;
		gbc_label_36.insets = new Insets(0, 0, 5, 5);
		gbc_label_36.gridx = 0;
		gbc_label_36.gridy = 26;
		panel.add(label_36, gbc_label_36);
		
		textDiag = new JTextField();
		textDiag.setColumns(10);
		GridBagConstraints gbc_textDiag = new GridBagConstraints();
		gbc_textDiag.insets = new Insets(0, 0, 5, 5);
		gbc_textDiag.fill = GridBagConstraints.HORIZONTAL;
		gbc_textDiag.gridx = 1;
		gbc_textDiag.gridy = 26;
		panel.add(textDiag, gbc_textDiag);
		
		JButton button_23 = new JButton("?");
		button_23.setMargin(new Insets(2, 8, 2, 8));
		GridBagConstraints gbc_button_23 = new GridBagConstraints();
		gbc_button_23.insets = new Insets(0, 0, 5, 0);
		gbc_button_23.gridx = 2;
		gbc_button_23.gridy = 26;
		panel.add(button_23, gbc_button_23);
		
		JButton btnSalvar = new JButton("Save");
		btnSalvar.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				Logger.log(Logger.TAG_OPTIONS, "Saving comments ...");
				setComments();
				Logger.log(Logger.TAG_OPTIONS, "Comments saved");
			}
		});
		btnSalvar.setToolTipText("Save comment headers");
		GridBagConstraints gbc_btnSalvar = new GridBagConstraints();
		gbc_btnSalvar.gridwidth = 3;
		gbc_btnSalvar.insets = new Insets(0, 0, 5, 0);
		gbc_btnSalvar.gridx = 0;
		gbc_btnSalvar.gridy = 27;
		panel.add(btnSalvar, gbc_btnSalvar);
		
		JLabel label_37 = new JLabel("Parser options:");
		label_37.setFont(new Font("Tahoma", Font.BOLD, 16));
		label_37.setAlignmentX(0.5f);
		GridBagConstraints gbc_label_37 = new GridBagConstraints();
		gbc_label_37.fill = GridBagConstraints.HORIZONTAL;
		gbc_label_37.anchor = GridBagConstraints.WEST;
		gbc_label_37.gridwidth = 2;
		gbc_label_37.insets = new Insets(15, 5, 5, 5);
		gbc_label_37.gridx = 0;
		gbc_label_37.gridy = 28;
		panel.add(label_37, gbc_label_37);
		
		JLabel label_38 = new JLabel("Text editor:");
		label_38.setToolTipText("Select default text editor");
		GridBagConstraints gbc_label_38 = new GridBagConstraints();
		gbc_label_38.fill = GridBagConstraints.VERTICAL;
		gbc_label_38.anchor = GridBagConstraints.EAST;
		gbc_label_38.insets = new Insets(0, 0, 5, 5);
		gbc_label_38.gridx = 0;
		gbc_label_38.gridy = 29;
		panel.add(label_38, gbc_label_38);
		
		JPanel panel_3 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_3.getLayout();
		flowLayout.setVgap(1);
		flowLayout.setHgap(1);
		flowLayout.setAlignment(FlowLayout.LEFT);
		panel_3.setPreferredSize(new Dimension(10, 25));
		panel_3.setMinimumSize(new Dimension(10, 25));
		panel_3.setMaximumSize(new Dimension(32767, 30));
		panel_3.setBorder(null);
		GridBagConstraints gbc_panel_3 = new GridBagConstraints();
		gbc_panel_3.anchor = GridBagConstraints.WEST;
		gbc_panel_3.fill = GridBagConstraints.BOTH;
		gbc_panel_3.insets = new Insets(0, 0, 5, 5);
		gbc_panel_3.gridx = 1;
		gbc_panel_3.gridy = 29;
		panel.add(panel_3, gbc_panel_3);
		
		rdbtnTAnalisys = new JRadioButton("TextAnalysis");
		rdbtnTAnalisys.setToolTipText("Use TextAnalysis tool as default text editor");
		rdbtnTAnalisys.setSelected(true);
		panel_3.add(rdbtnTAnalisys);
		
		rdbtnNotepad = new JRadioButton("Notepad++");
		rdbtnNotepad.setToolTipText("Use Notepad++ as default text editor");
		panel_3.add(rdbtnNotepad);
		
		JLabel label_39 = new JLabel("Word Wrap:");
		label_39.setToolTipText("Word wrap on/off");
		label_39.setPreferredSize(new Dimension(55, 23));
		label_39.setMinimumSize(new Dimension(55, 23));
		label_39.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_label_39 = new GridBagConstraints();
		gbc_label_39.fill = GridBagConstraints.HORIZONTAL;
		gbc_label_39.insets = new Insets(0, 0, 5, 5);
		gbc_label_39.gridx = 0;
		gbc_label_39.gridy = 30;
		panel.add(label_39, gbc_label_39);
		
		chkTextWrap = new JCheckBox("");
		chkTextWrap.addItemListener(new ItemListener()
		{
			@Override
			public void itemStateChanged(ItemEvent arg0)
			{
				if (arg0.getStateChange() == ItemEvent.SELECTED)
					SharedObjs.parserPane.getResultsTxtPane().setWrapText(true);
				else
					SharedObjs.parserPane.getResultsTxtPane().setWrapText(false);
			}
		});
		GridBagConstraints gbc_chkTextWrap = new GridBagConstraints();
		gbc_chkTextWrap.anchor = GridBagConstraints.WEST;
		gbc_chkTextWrap.insets = new Insets(0, 0, 5, 5);
		gbc_chkTextWrap.gridx = 1;
		gbc_chkTextWrap.gridy = 30;
		panel.add(chkTextWrap, gbc_chkTextWrap);
		
		JLabel label_40 = new JLabel("Tree Breakdown:");
		label_40.setToolTipText("Changes affect just filters/Results tree");
		GridBagConstraints gbc_label_40 = new GridBagConstraints();
		gbc_label_40.anchor = GridBagConstraints.EAST;
		gbc_label_40.insets = new Insets(0, 0, 5, 5);
		gbc_label_40.gridx = 0;
		gbc_label_40.gridy = 31;
		panel.add(label_40, gbc_label_40);
		
		JPanel panel_4 = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel_4.getLayout();
		flowLayout_1.setVgap(1);
		flowLayout_1.setHgap(1);
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		panel_4.setPreferredSize(new Dimension(10, 25));
		panel_4.setMinimumSize(new Dimension(10, 25));
		GridBagConstraints gbc_panel_4 = new GridBagConstraints();
		gbc_panel_4.anchor = GridBagConstraints.WEST;
		gbc_panel_4.fill = GridBagConstraints.BOTH;
		gbc_panel_4.insets = new Insets(0, 0, 5, 5);
		gbc_panel_4.gridx = 1;
		gbc_panel_4.gridy = 31;
		panel.add(panel_4, gbc_panel_4);
		
		rdbtnDouble = new JRadioButton("DoubleClick");
		rdbtnDouble.setSelected(true);
		rdbtnDouble.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				SharedObjs.parserPane.getFiltersResultsTree().setToggleClickCount(2);
			}
		});
		panel_4.add(rdbtnDouble);
		
		rdbtnSingleclick = new JRadioButton("SingleClick");
		rdbtnSingleclick.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				SharedObjs.parserPane.getFiltersResultsTree().setToggleClickCount(1);
			}
		});
		panel_4.add(rdbtnSingleclick);
		
		JLabel label_41 = new JLabel("More options:");
		label_41.setToolTipText("More options for parser pane");
		label_41.setPreferredSize(new Dimension(55, 23));
		label_41.setMinimumSize(new Dimension(55, 23));
		label_41.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_label_41 = new GridBagConstraints();
		gbc_label_41.fill = GridBagConstraints.HORIZONTAL;
		gbc_label_41.anchor = GridBagConstraints.EAST;
		gbc_label_41.insets = new Insets(0, 0, 0, 5);
		gbc_label_41.gridx = 0;
		gbc_label_41.gridy = 32;
		panel.add(label_41, gbc_label_41);
		
		JPanel panel_5 = new JPanel();
		GridBagConstraints gbc_panel_5 = new GridBagConstraints();
		gbc_panel_5.anchor = GridBagConstraints.WEST;
		gbc_panel_5.fill = GridBagConstraints.VERTICAL;
		gbc_panel_5.insets = new Insets(0, 0, 0, 5);
		gbc_panel_5.gridx = 1;
		gbc_panel_5.gridy = 32;
		panel.add(panel_5, gbc_panel_5);
		
		btnManageFilters = new JButton("Manage filters");
		btnManageFilters.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				SharedObjs.getCustomFiltersPane().open();
			}
		});
		panel_5.add(btnManageFilters);
		
		btnAdvanced = new JButton("Advanced");
		btnAdvanced.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				SharedObjs.advOptions.setLocation(SharedObjs.satFrame.getLocation().x + 200,
												  SharedObjs.satFrame.getLocation().y + 200);
				SharedObjs.advOptions.setVisible(true);
			}
		});
		btnAdvanced.setToolTipText("Click to see advanced options");
		btnAdvanced.setPreferredSize(new Dimension(103, 23));
		panel_5.add(btnAdvanced);
		
		editorSelector.add(rdbtnTAnalisys);
		editorSelector.add(rdbtnNotepad);
		breakdownSelector.add(rdbtnDouble);
		breakdownSelector.add(rdbtnSingleclick);
		
		loadDataPane();
	}
	
	public void getComments()
	{
		textConsumeFull.setText(XmlMngr.getUserValueOf(new String[] {"option_pane", "full_log"}));
		textConsumeOff.setText(XmlMngr.getUserValueOf(new String[] {"option_pane", "screen_off"}));
		textConsumeOn.setText(XmlMngr.getUserValueOf(new String[] {"option_pane", "screen_on"}));
		textHighCurrent.setText(XmlMngr.getUserValueOf(new String[] {"option_pane", "high_current"}));
		textKernelWake.setText(XmlMngr.getUserValueOf(new String[] {"option_pane", "krnl_wkl"}));
		textJavaWake.setText(XmlMngr.getUserValueOf(new String[] {"option_pane", "java_wkl"}));
		textSuspiciousHeader.setText(XmlMngr.getUserValueOf(new String[] {"option_pane", "suspicious_header"}));
		textSuspicious.setText(XmlMngr.getUserValueOf(new String[] {"option_pane", "suspicious"}));
		textAlarms.setText(XmlMngr.getUserValueOf(new String[] {"option_pane", "alarms"}));
		textB2g.setText(XmlMngr.getUserValueOf(new String[] {"option_pane", "b2g"}));
		textTether.setText(XmlMngr.getUserValueOf(new String[] {"option_pane", "tether"}));
		textDiag.setText(XmlMngr.getUserValueOf(new String[] {"option_pane", "diag"}));
		
		// Logger.log(Logger.TAG_OPTIONS, "Comments Loaded");
	}
	
	public void loadDataPane()
	{
		if (XmlMngr.getUserValueOf(new String[] {"option_pane", "tree_breakdown"}).equals("1"))
		{
			rdbtnSingleclick.setSelected(true);
			SharedObjs.parserPane.getFiltersResultsTree().setToggleClickCount(1);
		}
		else
		{
			SharedObjs.parserPane.getFiltersResultsTree().setToggleClickCount(2);
			rdbtnDouble.setSelected(true);
		}

		if (XmlMngr.getUserValueOf(new String[] {"option_pane", "wwrap"}).equals("0"))
			chkTextWrap.setSelected(false);
		else
			chkTextWrap.setSelected(true);
		
		if (XmlMngr.getUserValueOf(new String[] {"option_pane", "editor"}).equals("0"))
			rdbtnTAnalisys.setSelected(true);
		else
			rdbtnNotepad.setSelected(true);
		
		getComments();

		Logger.log(Logger.TAG_OPTIONS, "Option pane values loaded");
	}
	
	public void setComments()
	{
		XmlMngr.setUserValueOf(new String[] {"option_pane", "full_log"}, textConsumeFull.getText());
		XmlMngr.setUserValueOf(new String[] {"option_pane", "screen_off"}, textConsumeOff.getText());
		XmlMngr.setUserValueOf(new String[] {"option_pane", "screen_on"}, textConsumeOn.getText());
		XmlMngr.setUserValueOf(new String[] {"option_pane", "high_current"}, textHighCurrent.getText());
		XmlMngr.setUserValueOf(new String[] {"option_pane", "krnl_wkl"}, textKernelWake.getText());
		XmlMngr.setUserValueOf(new String[] {"option_pane", "java_wkl"}, textJavaWake.getText());
		XmlMngr.setUserValueOf(new String[] {"option_pane", "suspicious_header"},
							   textSuspiciousHeader.getText());
		XmlMngr.setUserValueOf(new String[] {"option_pane", "suspicious"}, textSuspicious.getText());
		XmlMngr.setUserValueOf(new String[] {"option_pane", "alarms"}, textAlarms.getText());
		XmlMngr.setUserValueOf(new String[] {"option_pane", "b2g"}, textB2g.getText());
		XmlMngr.setUserValueOf(new String[] {"option_pane", "tether"}, textTether.getText());
		XmlMngr.setUserValueOf(new String[] {"option_pane", "diag"}, textDiag.getText());
	}
	
	/**
	 * Save all data in the pane on a xml file.
	 * 
	 * @throws JDOMException
	 * @throws IOException
	 */
	public void savePaneData()
	{
		if (rdbtnNotepad.isSelected())
			XmlMngr.setUserValueOf(new String[] {"option_pane", "editor"}, "1");
		else
			XmlMngr.setUserValueOf(new String[] {"option_pane", "editor"}, "0");
			
		if (chkTextWrap.isSelected())
			XmlMngr.setUserValueOf(new String[] {"option_pane", "wwrap"}, "1");
		else
			XmlMngr.setUserValueOf(new String[] {"option_pane", "wwrap"}, "0");
			
		if (rdbtnSingleclick.isSelected())
			XmlMngr.setUserValueOf(new String[] {"option_pane", "tree_breakdown"}, "1");
		else
			XmlMngr.setUserValueOf(new String[] {"option_pane", "tree_breakdown"}, "2");
		
		setComments();
			
		Logger.log(Logger.TAG_OPTIONS, "Option pane values saved");
	}
	
	// Getters
	public String getTextConsumeOn()
	{
		return textConsumeOn.getText();
	}
	
	public String getTextConsumeOff()
	{
		return textConsumeOff.getText();
	}
	
	public String getTextConsumeFull()
	{
		return textConsumeFull.getText();
	}
	
	public String getTextSuspiciousHeader()
	{
		return textSuspiciousHeader.getText();
	}
	
	public String getTextSuspicious()
	{
		return textSuspicious.getText();
	}
	
	public String getTextKernel()
	{
		return textKernelWake.getText();
	}
	
	public String getTextJava()
	{
		return textJavaWake.getText();
	}
	
	public String getTextAlarms()
	{
		return textAlarms.getText();
	}
	
	public String getTextB2g()
	{
		return textB2g.getText();
	}
	
	public String getTextTether()
	{
		return textTether.getText();
	}
	
	public String getTextDiag()
	{
		return textDiag.getText();
	}
	
	public String getTextHighCurrent()
	{
		return textHighCurrent.getText();
	}
	
	protected JRadioButton getRdbtnNotepad()
	{
		return rdbtnNotepad;
	}
	
	protected JCheckBox getChkTextWrap()
	{
		return chkTextWrap;
	}
	
	protected JRadioButton getRdbtnTAnalisys()
	{
		return rdbtnTAnalisys;
	}
}
