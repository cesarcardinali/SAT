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
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import org.jdom2.JDOMException;

import core.Logger;
import core.SharedObjs;
import core.XmlMngr;
import supportive.Encryptation;
import javax.swing.ImageIcon;


public class OptionsPane extends JPanel
{
	// {{ Variables
	private JTextField     textConsumeFull;
	private JTextField     textConsumeOff;
	private JTextField     textConsumeOn;
	private JTextField     textHighCurrent;
	private JTextField     textKernelWake;
	private JTextField     textJavaWake;
	private JTextField     textSuspiciousHeader;
	private JTextField     textSuspicious;
	private JTextField     textAlarms;
	private JTextField     textB2g;
	private JTextField     textTether;
	private JTextField     textDiag;
	private JRadioButton   rdbtnSingleclick;
	private JRadioButton   rdbtnDouble;
	private JRadioButton   rdbtnTAnalisys;
	private JRadioButton   rdbtnNotepad;
	private JCheckBox      chkTextWrap;
	private JButton        btnManageFilters;
	private JButton        btnAdvanced;
	private JTextField     textUsername;
	private JPasswordField textPassword;
	private JCheckBox      chkbxRemember;
	private JPanel         panel_2;
	private JLabel         lblServerStatus;
	private JLabel         label_6;
	private TextDialog warning;
	
	// }}
	
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
		
		warning = new TextDialog();
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);
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
		gbl_panel.columnWidths = new int[] {150, 500, 0};
		gbl_panel.rowHeights = new int[] {0, 0, 0, 0, 20, 100, 0, 0, 0};
		gbl_panel.columnWeights = new double[] {1.0, 1.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		JLabel label_3 = new JLabel("User data:");
		label_3.setFont(new Font("Tahoma", Font.BOLD, 16));
		GridBagConstraints gbc_label_3 = new GridBagConstraints();
		gbc_label_3.anchor = GridBagConstraints.WEST;
		gbc_label_3.insets = new Insets(0, 5, 5, 5);
		gbc_label_3.gridx = 0;
		gbc_label_3.gridy = 0;
		panel.add(label_3, gbc_label_3);
		
		lblServerStatus = new JLabel("Server Status");
		lblServerStatus.setFont(new Font("Cambria Math", Font.BOLD, 11));
		lblServerStatus.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_lblServerStatus = new GridBagConstraints();
		gbc_lblServerStatus.anchor = GridBagConstraints.EAST;
		gbc_lblServerStatus.insets = new Insets(0, 0, 5, 15);
		gbc_lblServerStatus.gridx = 1;
		gbc_lblServerStatus.gridy = 0;
		panel.add(lblServerStatus, gbc_lblServerStatus);
		
		JPanel panel_7 = new JPanel();
		panel_7.setBorder(new LineBorder(Color.LIGHT_GRAY, 1, true));
		GridBagConstraints gbc_panel_7 = new GridBagConstraints();
		gbc_panel_7.gridwidth = 2;
		gbc_panel_7.insets = new Insets(0, 15, 5, 15);
		gbc_panel_7.fill = GridBagConstraints.BOTH;
		gbc_panel_7.gridx = 0;
		gbc_panel_7.gridy = 1;
		panel.add(panel_7, gbc_panel_7);
		GridBagLayout gbl_panel_7 = new GridBagLayout();
		gbl_panel_7.columnWidths = new int[] {0, 50, 0, 50, 0, 0};
		gbl_panel_7.rowHeights = new int[] {0, 0};
		gbl_panel_7.columnWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel_7.rowWeights = new double[] {0.0, Double.MIN_VALUE};
		panel_7.setLayout(gbl_panel_7);
		
		JLabel label_4 = new JLabel("Username: ");
		label_4.setPreferredSize(new Dimension(60, 14));
		label_4.setMinimumSize(new Dimension(60, 14));
		label_4.setMaximumSize(new Dimension(60, 14));
		label_4.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_label_4 = new GridBagConstraints();
		gbc_label_4.insets = new Insets(0, 0, 0, 5);
		gbc_label_4.anchor = GridBagConstraints.EAST;
		gbc_label_4.gridx = 0;
		gbc_label_4.gridy = 0;
		panel_7.add(label_4, gbc_label_4);
		
		textUsername = new JTextField();
		textUsername.setToolTipText("Motorola username");
		textUsername.setText((String) null);
		textUsername.setPreferredSize(new Dimension(90, 20));
		textUsername.setMinimumSize(new Dimension(90, 20));
		textUsername.addFocusListener(new FocusListener()
		{
			@Override
			public void focusLost(FocusEvent e)
			{
				updateUserdata();
				System.out.println("fooooooooooooocus lost");
			}
			
			@Override
			public void focusGained(FocusEvent e)
			{
				
			}
		});
		GridBagConstraints gbc_textUsername = new GridBagConstraints();
		gbc_textUsername.insets = new Insets(0, 0, 0, 5);
		gbc_textUsername.fill = GridBagConstraints.HORIZONTAL;
		gbc_textUsername.gridx = 1;
		gbc_textUsername.gridy = 0;
		panel_7.add(textUsername, gbc_textUsername);
		
		JLabel label_5 = new JLabel("Password: ");
		label_5.setPreferredSize(new Dimension(60, 14));
		label_5.setMinimumSize(new Dimension(60, 14));
		label_5.setMaximumSize(new Dimension(60, 14));
		label_5.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_label_5 = new GridBagConstraints();
		gbc_label_5.anchor = GridBagConstraints.EAST;
		gbc_label_5.insets = new Insets(0, 0, 0, 5);
		gbc_label_5.gridx = 2;
		gbc_label_5.gridy = 0;
		panel_7.add(label_5, gbc_label_5);
		
		textPassword = new JPasswordField();
		textPassword.setToolTipText("Motorola password");
		textPassword.setPreferredSize(new Dimension(90, 20));
		textPassword.setMinimumSize(new Dimension(90, 20));
		textPassword.addFocusListener(new FocusListener()
		{
			@Override
			public void focusLost(FocusEvent e)
			{
				updateUserdata();
			}
			
			@Override
			public void focusGained(FocusEvent e)
			{
				
			}
		});
		
		GridBagConstraints gbc_textPassword = new GridBagConstraints();
		gbc_textPassword.insets = new Insets(0, 0, 0, 5);
		gbc_textPassword.fill = GridBagConstraints.HORIZONTAL;
		gbc_textPassword.gridx = 3;
		gbc_textPassword.gridy = 0;
		panel_7.add(textPassword, gbc_textPassword);
		
		chkbxRemember = new JCheckBox("Remember?");
		chkbxRemember.setToolTipText("Remember your login and password");
		chkbxRemember.setSelected(true);
		chkbxRemember.addFocusListener(new FocusListener()
		{
			@Override
			public void focusLost(FocusEvent e)
			{
				updateUserdata();
			}
			
			@Override
			public void focusGained(FocusEvent e)
			{
				
			}
		});
		GridBagConstraints gbc_chkbxRemember = new GridBagConstraints();
		gbc_chkbxRemember.gridx = 4;
		gbc_chkbxRemember.gridy = 0;
		panel_7.add(chkbxRemember, gbc_chkbxRemember);
		
		JPanel panel_8 = new JPanel();
		GridBagConstraints gbc_panel_8 = new GridBagConstraints();
		gbc_panel_8.fill = GridBagConstraints.BOTH;
		gbc_panel_8.insets = new Insets(10, 5, 5, 5);
		gbc_panel_8.gridx = 0;
		gbc_panel_8.gridy = 2;
		panel.add(panel_8, gbc_panel_8);
		GridBagLayout gbl_panel_8 = new GridBagLayout();
		gbl_panel_8.columnWidths = new int[] {150, 0, 0};
		gbl_panel_8.rowHeights = new int[] {0};
		gbl_panel_8.columnWeights = new double[] {0.0, 0.0, Double.MIN_VALUE};
		gbl_panel_8.rowWeights = new double[] {0.0};
		panel_8.setLayout(gbl_panel_8);
		
		JLabel label = new JLabel("Comments personalization");
		label.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.insets = new Insets(0, 0, 0, 5);
		gbc_label.anchor = GridBagConstraints.WEST;
		gbc_label.gridx = 0;
		gbc_label.gridy = 0;
		panel_8.add(label, gbc_label);
		label.setToolTipText("Click to hide/show");
		label.addMouseListener(new MouseListener()
		{
			@Override
			public void mouseReleased(MouseEvent e)
			{
			}
			
			@Override
			public void mousePressed(MouseEvent e)
			{
			}
			
			@Override
			public void mouseExited(MouseEvent e)
			{
			}
			
			@Override
			public void mouseEntered(MouseEvent e)
			{
			}
			
			@Override
			public void mouseClicked(MouseEvent e)
			{
				if (panel_2.isVisible())
				{
					panel_2.setVisible(false);
					label_6.setIcon(new ImageIcon("Data\\pics\\expand.png"));
				}
				else
				{
					panel_2.setVisible(true);
					label_6.setIcon(new ImageIcon("Data\\pics\\collapse.png"));
				}
			}
		});
		label.setFont(new Font("Tahoma", Font.BOLD, 16));
		label.setAlignmentX(0.5f);
		
		label_6 = new JLabel("");
		label_6.setVerticalAlignment(SwingConstants.BOTTOM);
		label_6.addMouseListener(new MouseListener()
		{
			@Override
			public void mouseReleased(MouseEvent e)
			{
			}
			
			@Override
			public void mousePressed(MouseEvent e)
			{
			}
			
			@Override
			public void mouseExited(MouseEvent e)
			{
			}
			
			@Override
			public void mouseEntered(MouseEvent e)
			{
			}
			
			@Override
			public void mouseClicked(MouseEvent e)
			{
				if (panel_2.isVisible())
				{
					panel_2.setVisible(false);
					label_6.setIcon(new ImageIcon("Data\\pics\\expand.png"));
				}
				else
				{
					panel_2.setVisible(true);
					label_6.setIcon(new ImageIcon("Data\\pics\\collapse.png"));
				}
			}
		});
		GridBagConstraints gbc_label_6 = new GridBagConstraints();
		gbc_label_6.anchor = GridBagConstraints.SOUTHWEST;
		gbc_label_6.insets = new Insets(0, 0, 0, 5);
		gbc_label_6.gridx = 1;
		gbc_label_6.gridy = 0;
		panel_8.add(label_6, gbc_label_6);
		label_6.setIcon(new ImageIcon("Data\\pics\\collapse.png"));
		label_6.setToolTipText("Click to hide/show");
		label_6.setFont(new Font("Tahoma", Font.BOLD, 16));
		label_6.setAlignmentX(0.5f);
		
		panel_2 = new JPanel();
		panel_2.setBorder(new LineBorder(new Color(192, 192, 192), 1, true));
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.fill = GridBagConstraints.BOTH;
		gbc_panel_2.insets = new Insets(0, 15, 5, 15);
		gbc_panel_2.gridwidth = 2;
		gbc_panel_2.gridx = 0;
		gbc_panel_2.gridy = 3;
		panel.add(panel_2, gbc_panel_2);
		GridBagLayout gbl_panel_2 = new GridBagLayout();
		gbl_panel_2.columnWidths = new int[] {175, 450, 30, 0};
		gbl_panel_2.rowHeights = new int[] {
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
		        0,
		        0,
		        0,
		        0,
		        0,
		        0};
		gbl_panel_2.columnWeights = new double[] {0.0, 2.0, 0.0, Double.MIN_VALUE};
		gbl_panel_2.rowWeights = new double[] {
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
		panel_2.setLayout(gbl_panel_2);
		
		JLabel label_1 = new JLabel("High consumption apps:");
		label_1.setVerticalAlignment(SwingConstants.BOTTOM);
		label_1.setForeground(Color.DARK_GRAY);
		label_1.setMinimumSize(new Dimension(96, 18));
		label_1.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		label_1.setAlignmentX(0.5f);
		GridBagConstraints gbc_label_1 = new GridBagConstraints();
		gbc_label_1.anchor = GridBagConstraints.EAST;
		gbc_label_1.insets = new Insets(0, 0, 5, 5);
		gbc_label_1.gridx = 0;
		gbc_label_1.gridy = 0;
		panel_2.add(label_1, gbc_label_1);
		
		JLabel label_19 = new JLabel("Full log:");
		GridBagConstraints gbc_label_19 = new GridBagConstraints();
		gbc_label_19.anchor = GridBagConstraints.EAST;
		gbc_label_19.insets = new Insets(0, 0, 5, 5);
		gbc_label_19.gridx = 0;
		gbc_label_19.gridy = 1;
		panel_2.add(label_19, gbc_label_19);
		label_19.setMaximumSize(new Dimension(100, 14));
		label_19.setHorizontalAlignment(SwingConstants.RIGHT);
		
		textConsumeFull = new JTextField();
		GridBagConstraints gbc_textConsumeFull = new GridBagConstraints();
		gbc_textConsumeFull.fill = GridBagConstraints.HORIZONTAL;
		gbc_textConsumeFull.insets = new Insets(0, 0, 5, 5);
		gbc_textConsumeFull.gridx = 1;
		gbc_textConsumeFull.gridy = 1;
		panel_2.add(textConsumeFull, gbc_textConsumeFull);
		textConsumeFull.setColumns(10);
		
		JButton button_12 = new JButton("?");
		button_12.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				warning.setText("#pname#: Process name\n#avgconsume#: Average process CPU consumption\n"
								+ "#scoffconsume#: Average consumption while screen off\n"
								+ "#sconconsume#: Average consumption while screen on\n"
								+ "#logfull#: Show complete log results\n#logoff#: Log lines while screen off\n"
								+ "#logon#: Log lines while screen on\n");
				warning.setVisible(true);
			}
		});
		button_12.setToolTipText("Click for help creating comment");
		GridBagConstraints gbc_button_12 = new GridBagConstraints();
		gbc_button_12.fill = GridBagConstraints.VERTICAL;
		gbc_button_12.gridheight = 3;
		gbc_button_12.insets = new Insets(0, 0, 5, 5);
		gbc_button_12.gridx = 2;
		gbc_button_12.gridy = 1;
		panel_2.add(button_12, gbc_button_12);
		button_12.setMargin(new Insets(2, 8, 2, 8));
		
		JLabel label_20 = new JLabel("Screen Off:");
		GridBagConstraints gbc_label_20 = new GridBagConstraints();
		gbc_label_20.anchor = GridBagConstraints.EAST;
		gbc_label_20.insets = new Insets(0, 0, 5, 5);
		gbc_label_20.gridx = 0;
		gbc_label_20.gridy = 2;
		panel_2.add(label_20, gbc_label_20);
		label_20.setHorizontalAlignment(SwingConstants.RIGHT);
		
		textConsumeOff = new JTextField();
		GridBagConstraints gbc_textConsumeOff = new GridBagConstraints();
		gbc_textConsumeOff.fill = GridBagConstraints.HORIZONTAL;
		gbc_textConsumeOff.insets = new Insets(0, 0, 5, 5);
		gbc_textConsumeOff.gridx = 1;
		gbc_textConsumeOff.gridy = 2;
		panel_2.add(textConsumeOff, gbc_textConsumeOff);
		textConsumeOff.setColumns(10);
		
		JLabel label_21 = new JLabel("Screen On:");
		GridBagConstraints gbc_label_21 = new GridBagConstraints();
		gbc_label_21.insets = new Insets(0, 0, 5, 5);
		gbc_label_21.anchor = GridBagConstraints.EAST;
		gbc_label_21.gridx = 0;
		gbc_label_21.gridy = 3;
		panel_2.add(label_21, gbc_label_21);
		label_21.setHorizontalAlignment(SwingConstants.RIGHT);
		
		textConsumeOn = new JTextField();
		GridBagConstraints gbc_textConsumeOn = new GridBagConstraints();
		gbc_textConsumeOn.fill = GridBagConstraints.HORIZONTAL;
		gbc_textConsumeOn.insets = new Insets(0, 0, 5, 5);
		gbc_textConsumeOn.gridx = 1;
		gbc_textConsumeOn.gridy = 3;
		panel_2.add(textConsumeOn, gbc_textConsumeOn);
		textConsumeOn.setColumns(10);
		
		JSeparator separator = new JSeparator();
		separator.setPreferredSize(new Dimension(500, 1));
		separator.setMinimumSize(new Dimension(3, 2));
		separator.setForeground(Color.LIGHT_GRAY);
		separator.setBackground(Color.WHITE);
		GridBagConstraints gbc_separator = new GridBagConstraints();
		gbc_separator.gridwidth = 3;
		gbc_separator.insets = new Insets(0, 0, 5, 0);
		gbc_separator.gridx = 0;
		gbc_separator.gridy = 4;
		panel_2.add(separator, gbc_separator);
		
		JLabel label_22 = new JLabel("Wakelocks:");
		label_22.setVerticalAlignment(SwingConstants.BOTTOM);
		label_22.setForeground(Color.DARK_GRAY);
		GridBagConstraints gbc_label_22 = new GridBagConstraints();
		gbc_label_22.anchor = GridBagConstraints.EAST;
		gbc_label_22.insets = new Insets(0, 0, 5, 5);
		gbc_label_22.gridx = 0;
		gbc_label_22.gridy = 5;
		panel_2.add(label_22, gbc_label_22);
		label_22.setMinimumSize(new Dimension(100, 18));
		label_22.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		
		JLabel label_23 = new JLabel("High Current:");
		GridBagConstraints gbc_label_23 = new GridBagConstraints();
		gbc_label_23.anchor = GridBagConstraints.EAST;
		gbc_label_23.insets = new Insets(0, 0, 5, 5);
		gbc_label_23.gridx = 0;
		gbc_label_23.gridy = 6;
		panel_2.add(label_23, gbc_label_23);
		label_23.setHorizontalAlignment(SwingConstants.RIGHT);
		
		textHighCurrent = new JTextField();
		GridBagConstraints gbc_textHighCurrent = new GridBagConstraints();
		gbc_textHighCurrent.fill = GridBagConstraints.HORIZONTAL;
		gbc_textHighCurrent.insets = new Insets(0, 0, 5, 5);
		gbc_textHighCurrent.gridx = 1;
		gbc_textHighCurrent.gridy = 6;
		panel_2.add(textHighCurrent, gbc_textHighCurrent);
		textHighCurrent.setColumns(10);
		
		JButton button_15 = new JButton("?");
		button_15.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				warning.setText("Edit headers from \"Wakelocks\" filter result");
				warning.setVisible(true);
			}
		});
		button_15.setToolTipText("Click for help creating comment");
		GridBagConstraints gbc_button_15 = new GridBagConstraints();
		gbc_button_15.fill = GridBagConstraints.VERTICAL;
		gbc_button_15.gridheight = 3;
		gbc_button_15.insets = new Insets(0, 0, 5, 5);
		gbc_button_15.gridx = 2;
		gbc_button_15.gridy = 6;
		panel_2.add(button_15, gbc_button_15);
		button_15.setMargin(new Insets(2, 8, 2, 8));
		
		JLabel label_24 = new JLabel("Kernel Wakelocks:");
		GridBagConstraints gbc_label_24 = new GridBagConstraints();
		gbc_label_24.anchor = GridBagConstraints.EAST;
		gbc_label_24.insets = new Insets(0, 0, 5, 5);
		gbc_label_24.gridx = 0;
		gbc_label_24.gridy = 7;
		panel_2.add(label_24, gbc_label_24);
		label_24.setHorizontalAlignment(SwingConstants.RIGHT);
		
		textKernelWake = new JTextField();
		GridBagConstraints gbc_textKernelWake = new GridBagConstraints();
		gbc_textKernelWake.fill = GridBagConstraints.HORIZONTAL;
		gbc_textKernelWake.insets = new Insets(0, 0, 5, 5);
		gbc_textKernelWake.gridx = 1;
		gbc_textKernelWake.gridy = 7;
		panel_2.add(textKernelWake, gbc_textKernelWake);
		textKernelWake.setColumns(10);
		
		JLabel label_25 = new JLabel("Java Wakelocks:");
		GridBagConstraints gbc_label_25 = new GridBagConstraints();
		gbc_label_25.anchor = GridBagConstraints.EAST;
		gbc_label_25.insets = new Insets(0, 0, 5, 5);
		gbc_label_25.gridx = 0;
		gbc_label_25.gridy = 8;
		panel_2.add(label_25, gbc_label_25);
		label_25.setHorizontalAlignment(SwingConstants.RIGHT);
		
		textJavaWake = new JTextField();
		GridBagConstraints gbc_textJavaWake = new GridBagConstraints();
		gbc_textJavaWake.fill = GridBagConstraints.HORIZONTAL;
		gbc_textJavaWake.insets = new Insets(0, 0, 5, 5);
		gbc_textJavaWake.gridx = 1;
		gbc_textJavaWake.gridy = 8;
		panel_2.add(textJavaWake, gbc_textJavaWake);
		textJavaWake.setColumns(10);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setPreferredSize(new Dimension(500, 1));
		separator_1.setMinimumSize(new Dimension(3, 2));
		separator_1.setForeground(Color.LIGHT_GRAY);
		separator_1.setBackground(Color.WHITE);
		GridBagConstraints gbc_separator_1 = new GridBagConstraints();
		gbc_separator_1.gridwidth = 3;
		gbc_separator_1.insets = new Insets(0, 0, 5, 0);
		gbc_separator_1.gridx = 0;
		gbc_separator_1.gridy = 9;
		panel_2.add(separator_1, gbc_separator_1);
		
		JLabel label_26 = new JLabel("Suspicious:");
		label_26.setForeground(Color.DARK_GRAY);
		GridBagConstraints gbc_label_26 = new GridBagConstraints();
		gbc_label_26.anchor = GridBagConstraints.EAST;
		gbc_label_26.insets = new Insets(0, 0, 5, 5);
		gbc_label_26.gridx = 0;
		gbc_label_26.gridy = 10;
		panel_2.add(label_26, gbc_label_26);
		label_26.setMinimumSize(new Dimension(100, 18));
		label_26.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		
		JLabel label_27 = new JLabel("Header:");
		GridBagConstraints gbc_label_27 = new GridBagConstraints();
		gbc_label_27.anchor = GridBagConstraints.EAST;
		gbc_label_27.insets = new Insets(0, 0, 5, 5);
		gbc_label_27.gridx = 0;
		gbc_label_27.gridy = 11;
		panel_2.add(label_27, gbc_label_27);
		label_27.setHorizontalAlignment(SwingConstants.RIGHT);
		
		textSuspiciousHeader = new JTextField();
		GridBagConstraints gbc_textSuspiciousHeader = new GridBagConstraints();
		gbc_textSuspiciousHeader.fill = GridBagConstraints.HORIZONTAL;
		gbc_textSuspiciousHeader.insets = new Insets(0, 0, 5, 5);
		gbc_textSuspiciousHeader.gridx = 1;
		gbc_textSuspiciousHeader.gridy = 11;
		panel_2.add(textSuspiciousHeader, gbc_textSuspiciousHeader);
		textSuspiciousHeader.setColumns(10);
		
		JButton button_18 = new JButton("?");
		button_18.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				warning.setText("#pname#*: Process name\n#tag#: Process tag held\n#duration#: Wakelock duration\n#log#: Android logs");
				warning.setVisible(true);
			}
		});
		button_18.setToolTipText("Click for help creating comment");
		GridBagConstraints gbc_button_18 = new GridBagConstraints();
		gbc_button_18.fill = GridBagConstraints.VERTICAL;
		gbc_button_18.gridheight = 2;
		gbc_button_18.insets = new Insets(0, 0, 5, 5);
		gbc_button_18.gridx = 2;
		gbc_button_18.gridy = 11;
		panel_2.add(button_18, gbc_button_18);
		button_18.setMargin(new Insets(2, 8, 2, 8));
		
		JLabel label_28 = new JLabel("Comment:");
		GridBagConstraints gbc_label_28 = new GridBagConstraints();
		gbc_label_28.anchor = GridBagConstraints.EAST;
		gbc_label_28.insets = new Insets(0, 0, 5, 5);
		gbc_label_28.gridx = 0;
		gbc_label_28.gridy = 12;
		panel_2.add(label_28, gbc_label_28);
		label_28.setHorizontalAlignment(SwingConstants.RIGHT);
		
		textSuspicious = new JTextField();
		GridBagConstraints gbc_textSuspicious = new GridBagConstraints();
		gbc_textSuspicious.fill = GridBagConstraints.HORIZONTAL;
		gbc_textSuspicious.insets = new Insets(0, 0, 5, 5);
		gbc_textSuspicious.gridx = 1;
		gbc_textSuspicious.gridy = 12;
		panel_2.add(textSuspicious, gbc_textSuspicious);
		textSuspicious.setColumns(10);
		
		JSeparator separator_2 = new JSeparator();
		separator_2.setPreferredSize(new Dimension(500, 1));
		separator_2.setMinimumSize(new Dimension(3, 2));
		separator_2.setForeground(Color.LIGHT_GRAY);
		separator_2.setBackground(Color.WHITE);
		GridBagConstraints gbc_separator_2 = new GridBagConstraints();
		gbc_separator_2.gridwidth = 3;
		gbc_separator_2.insets = new Insets(0, 0, 5, 0);
		gbc_separator_2.gridx = 0;
		gbc_separator_2.gridy = 13;
		panel_2.add(separator_2, gbc_separator_2);
		
		JLabel label_29 = new JLabel("Alarms: ");
		label_29.setForeground(Color.DARK_GRAY);
		GridBagConstraints gbc_label_29 = new GridBagConstraints();
		gbc_label_29.anchor = GridBagConstraints.EAST;
		gbc_label_29.insets = new Insets(0, 0, 5, 5);
		gbc_label_29.gridx = 0;
		gbc_label_29.gridy = 14;
		panel_2.add(label_29, gbc_label_29);
		label_29.setMinimumSize(new Dimension(125, 18));
		label_29.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		
		JLabel label_30 = new JLabel("Comment:");
		GridBagConstraints gbc_label_30 = new GridBagConstraints();
		gbc_label_30.anchor = GridBagConstraints.EAST;
		gbc_label_30.insets = new Insets(0, 0, 5, 5);
		gbc_label_30.gridx = 0;
		gbc_label_30.gridy = 15;
		panel_2.add(label_30, gbc_label_30);
		label_30.setHorizontalAlignment(SwingConstants.RIGHT);
		
		textAlarms = new JTextField();
		GridBagConstraints gbc_textAlarms = new GridBagConstraints();
		gbc_textAlarms.fill = GridBagConstraints.HORIZONTAL;
		gbc_textAlarms.insets = new Insets(0, 0, 5, 5);
		gbc_textAlarms.gridx = 1;
		gbc_textAlarms.gridy = 15;
		panel_2.add(textAlarms, gbc_textAlarms);
		textAlarms.setColumns(10);
		
		JButton button_20 = new JButton("?");
		button_20.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				warning.setText("#pname#*: Process name\n#log#: Android logs");
				warning.setVisible(true);
			}
		});
		button_20.setToolTipText("Click for help creating comment");
		GridBagConstraints gbc_button_20 = new GridBagConstraints();
		gbc_button_20.insets = new Insets(0, 0, 5, 5);
		gbc_button_20.gridx = 2;
		gbc_button_20.gridy = 15;
		panel_2.add(button_20, gbc_button_20);
		button_20.setMargin(new Insets(2, 8, 2, 8));
		
		JSeparator separator_3 = new JSeparator();
		separator_3.setPreferredSize(new Dimension(500, 1));
		separator_3.setMinimumSize(new Dimension(3, 2));
		separator_3.setForeground(Color.LIGHT_GRAY);
		separator_3.setBackground(Color.WHITE);
		GridBagConstraints gbc_separator_3 = new GridBagConstraints();
		gbc_separator_3.insets = new Insets(0, 0, 5, 0);
		gbc_separator_3.gridwidth = 3;
		gbc_separator_3.gridx = 0;
		gbc_separator_3.gridy = 16;
		panel_2.add(separator_3, gbc_separator_3);
		
		JLabel label_31 = new JLabel("Bug2Go: ");
		label_31.setForeground(Color.DARK_GRAY);
		GridBagConstraints gbc_label_31 = new GridBagConstraints();
		gbc_label_31.anchor = GridBagConstraints.EAST;
		gbc_label_31.insets = new Insets(0, 0, 5, 5);
		gbc_label_31.gridx = 0;
		gbc_label_31.gridy = 17;
		panel_2.add(label_31, gbc_label_31);
		label_31.setMinimumSize(new Dimension(125, 18));
		label_31.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		
		JLabel label_32 = new JLabel("Comment:");
		GridBagConstraints gbc_label_32 = new GridBagConstraints();
		gbc_label_32.anchor = GridBagConstraints.EAST;
		gbc_label_32.insets = new Insets(0, 0, 5, 5);
		gbc_label_32.gridx = 0;
		gbc_label_32.gridy = 18;
		panel_2.add(label_32, gbc_label_32);
		label_32.setHorizontalAlignment(SwingConstants.RIGHT);
		
		textB2g = new JTextField();
		GridBagConstraints gbc_textB2g = new GridBagConstraints();
		gbc_textB2g.fill = GridBagConstraints.HORIZONTAL;
		gbc_textB2g.insets = new Insets(0, 0, 5, 5);
		gbc_textB2g.gridx = 1;
		gbc_textB2g.gridy = 18;
		panel_2.add(textB2g, gbc_textB2g);
		textB2g.setColumns(10);
		
		JButton button_21 = new JButton("?");
		button_21.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				warning.setText("#log#: Android logs");
				warning.setVisible(true);
			}
		});
		button_21.setToolTipText("Click for help creating comment");
		GridBagConstraints gbc_button_21 = new GridBagConstraints();
		gbc_button_21.insets = new Insets(0, 0, 5, 5);
		gbc_button_21.gridx = 2;
		gbc_button_21.gridy = 18;
		panel_2.add(button_21, gbc_button_21);
		button_21.setMargin(new Insets(2, 8, 2, 8));
		
		JSeparator separator_4 = new JSeparator();
		separator_4.setPreferredSize(new Dimension(500, 1));
		separator_4.setMinimumSize(new Dimension(3, 2));
		separator_4.setForeground(Color.LIGHT_GRAY);
		separator_4.setBackground(Color.WHITE);
		GridBagConstraints gbc_separator_4 = new GridBagConstraints();
		gbc_separator_4.gridwidth = 3;
		gbc_separator_4.insets = new Insets(0, 0, 5, 0);
		gbc_separator_4.gridx = 0;
		gbc_separator_4.gridy = 19;
		panel_2.add(separator_4, gbc_separator_4);
		
		JLabel label_33 = new JLabel("Tethering: ");
		label_33.setForeground(Color.DARK_GRAY);
		GridBagConstraints gbc_label_33 = new GridBagConstraints();
		gbc_label_33.anchor = GridBagConstraints.EAST;
		gbc_label_33.insets = new Insets(0, 0, 5, 5);
		gbc_label_33.gridx = 0;
		gbc_label_33.gridy = 20;
		panel_2.add(label_33, gbc_label_33);
		label_33.setMinimumSize(new Dimension(125, 18));
		label_33.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		
		JLabel label_34 = new JLabel("Comment:");
		GridBagConstraints gbc_label_34 = new GridBagConstraints();
		gbc_label_34.anchor = GridBagConstraints.EAST;
		gbc_label_34.insets = new Insets(0, 0, 5, 5);
		gbc_label_34.gridx = 0;
		gbc_label_34.gridy = 21;
		panel_2.add(label_34, gbc_label_34);
		label_34.setHorizontalAlignment(SwingConstants.RIGHT);
		
		textTether = new JTextField();
		GridBagConstraints gbc_textTether = new GridBagConstraints();
		gbc_textTether.fill = GridBagConstraints.HORIZONTAL;
		gbc_textTether.insets = new Insets(0, 0, 5, 5);
		gbc_textTether.gridx = 1;
		gbc_textTether.gridy = 21;
		panel_2.add(textTether, gbc_textTether);
		textTether.setColumns(10);
		
		JButton button_22 = new JButton("?");
		button_22.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				warning.setText("#log#: Android logs");
				warning.setVisible(true);
			}
		});
		button_22.setToolTipText("Click for help creating comment");
		GridBagConstraints gbc_button_22 = new GridBagConstraints();
		gbc_button_22.insets = new Insets(0, 0, 5, 5);
		gbc_button_22.gridx = 2;
		gbc_button_22.gridy = 21;
		panel_2.add(button_22, gbc_button_22);
		button_22.setMargin(new Insets(2, 8, 2, 8));
		
		JSeparator separator_5 = new JSeparator();
		separator_5.setPreferredSize(new Dimension(500, 1));
		separator_5.setMinimumSize(new Dimension(3, 2));
		separator_5.setForeground(Color.LIGHT_GRAY);
		separator_5.setBackground(Color.WHITE);
		GridBagConstraints gbc_separator_5 = new GridBagConstraints();
		gbc_separator_5.insets = new Insets(0, 0, 5, 0);
		gbc_separator_5.gridwidth = 3;
		gbc_separator_5.gridx = 0;
		gbc_separator_5.gridy = 22;
		panel_2.add(separator_5, gbc_separator_5);
		
		JLabel label_35 = new JLabel("Diag: ");
		label_35.setForeground(Color.DARK_GRAY);
		GridBagConstraints gbc_label_35 = new GridBagConstraints();
		gbc_label_35.anchor = GridBagConstraints.EAST;
		gbc_label_35.insets = new Insets(0, 0, 5, 5);
		gbc_label_35.gridx = 0;
		gbc_label_35.gridy = 23;
		panel_2.add(label_35, gbc_label_35);
		label_35.setMinimumSize(new Dimension(125, 18));
		label_35.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		
		JLabel label_36 = new JLabel("Comment:");
		GridBagConstraints gbc_label_36 = new GridBagConstraints();
		gbc_label_36.anchor = GridBagConstraints.EAST;
		gbc_label_36.insets = new Insets(0, 0, 0, 5);
		gbc_label_36.gridx = 0;
		gbc_label_36.gridy = 24;
		panel_2.add(label_36, gbc_label_36);
		label_36.setHorizontalAlignment(SwingConstants.RIGHT);
		
		textDiag = new JTextField();
		GridBagConstraints gbc_textDiag = new GridBagConstraints();
		gbc_textDiag.fill = GridBagConstraints.HORIZONTAL;
		gbc_textDiag.insets = new Insets(0, 0, 0, 5);
		gbc_textDiag.gridx = 1;
		gbc_textDiag.gridy = 24;
		panel_2.add(textDiag, gbc_textDiag);
		textDiag.setColumns(10);
		
		JButton button_23 = new JButton("?");
		button_23.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				warning.setText("#log#: Android logs\n#dupcr#: Duplicate CR ID");
				warning.setVisible(true);
			}
		});
		button_23.setToolTipText("Click for help creating comment");
		GridBagConstraints gbc_button_23 = new GridBagConstraints();
		gbc_button_23.insets = new Insets(0, 0, 0, 5);
		gbc_button_23.gridx = 2;
		gbc_button_23.gridy = 24;
		panel_2.add(button_23, gbc_button_23);
		button_23.setMargin(new Insets(2, 8, 2, 8));
		
		JLabel label_37 = new JLabel("Parser options:");
		label_37.setFont(new Font("Tahoma", Font.BOLD, 16));
		label_37.setAlignmentX(0.5f);
		GridBagConstraints gbc_label_37 = new GridBagConstraints();
		gbc_label_37.fill = GridBagConstraints.HORIZONTAL;
		gbc_label_37.insets = new Insets(10, 5, 5, 0);
		gbc_label_37.gridwidth = 2;
		gbc_label_37.gridx = 0;
		gbc_label_37.gridy = 4;
		panel.add(label_37, gbc_label_37);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(new Color(192, 192, 192), 1, true));
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.insets = new Insets(0, 15, 5, 15);
		gbc_panel_1.gridwidth = 2;
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 0;
		gbc_panel_1.gridy = 5;
		panel.add(panel_1, gbc_panel_1);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[] {175, 450, 0};
		gbl_panel_1.rowHeights = new int[] {0, 0, 0, 0};
		gbl_panel_1.columnWeights = new double[] {0.0, 1.0, Double.MIN_VALUE};
		gbl_panel_1.rowWeights = new double[] {0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel_1.setLayout(gbl_panel_1);
		
		JLabel label_38 = new JLabel("Text editor:");
		label_38.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_label_38 = new GridBagConstraints();
		gbc_label_38.fill = GridBagConstraints.HORIZONTAL;
		gbc_label_38.insets = new Insets(0, 0, 5, 5);
		gbc_label_38.gridx = 0;
		gbc_label_38.gridy = 0;
		panel_1.add(label_38, gbc_label_38);
		label_38.setToolTipText("Select default text editor");
		
		JPanel panel_3 = new JPanel();
		GridBagConstraints gbc_panel_3 = new GridBagConstraints();
		gbc_panel_3.insets = new Insets(0, 0, 5, 0);
		gbc_panel_3.fill = GridBagConstraints.BOTH;
		gbc_panel_3.gridx = 1;
		gbc_panel_3.gridy = 0;
		panel_1.add(panel_3, gbc_panel_3);
		FlowLayout flowLayout = (FlowLayout) panel_3.getLayout();
		flowLayout.setVgap(1);
		flowLayout.setHgap(1);
		flowLayout.setAlignment(FlowLayout.LEFT);
		panel_3.setPreferredSize(new Dimension(10, 25));
		panel_3.setMinimumSize(new Dimension(10, 25));
		panel_3.setMaximumSize(new Dimension(32767, 30));
		panel_3.setBorder(null);
		
		rdbtnTAnalisys = new JRadioButton("TextAnalysis");
		rdbtnTAnalisys.setToolTipText("Use TextAnalysis tool as default text editor");
		rdbtnTAnalisys.setSelected(true);
		panel_3.add(rdbtnTAnalisys);
		
		rdbtnNotepad = new JRadioButton("Notepad++");
		rdbtnNotepad.setToolTipText("Use Notepad++ as default text editor");
		panel_3.add(rdbtnNotepad);
		
		editorSelector.add(rdbtnTAnalisys);
		editorSelector.add(rdbtnNotepad);
		
		JLabel label_39 = new JLabel("Word Wrap:");
		GridBagConstraints gbc_label_39 = new GridBagConstraints();
		gbc_label_39.fill = GridBagConstraints.HORIZONTAL;
		gbc_label_39.insets = new Insets(0, 0, 5, 5);
		gbc_label_39.gridx = 0;
		gbc_label_39.gridy = 1;
		panel_1.add(label_39, gbc_label_39);
		label_39.setToolTipText("Word wrap on/off");
		label_39.setPreferredSize(new Dimension(55, 23));
		label_39.setMinimumSize(new Dimension(55, 23));
		label_39.setHorizontalAlignment(SwingConstants.RIGHT);
		
		chkTextWrap = new JCheckBox("");
		GridBagConstraints gbc_chkTextWrap = new GridBagConstraints();
		gbc_chkTextWrap.insets = new Insets(0, 0, 5, 0);
		gbc_chkTextWrap.anchor = GridBagConstraints.WEST;
		gbc_chkTextWrap.gridx = 1;
		gbc_chkTextWrap.gridy = 1;
		panel_1.add(chkTextWrap, gbc_chkTextWrap);
		
		JLabel label_40 = new JLabel("Tree Breakdown:");
		label_40.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_label_40 = new GridBagConstraints();
		gbc_label_40.fill = GridBagConstraints.HORIZONTAL;
		gbc_label_40.insets = new Insets(0, 0, 0, 5);
		gbc_label_40.gridx = 0;
		gbc_label_40.gridy = 2;
		panel_1.add(label_40, gbc_label_40);
		label_40.setToolTipText("Changes affect just filters/Results tree");
		
		JPanel panel_4 = new JPanel();
		GridBagConstraints gbc_panel_4 = new GridBagConstraints();
		gbc_panel_4.anchor = GridBagConstraints.WEST;
		gbc_panel_4.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel_4.gridx = 1;
		gbc_panel_4.gridy = 2;
		panel_1.add(panel_4, gbc_panel_4);
		FlowLayout flowLayout_1 = (FlowLayout) panel_4.getLayout();
		flowLayout_1.setVgap(1);
		flowLayout_1.setHgap(1);
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		panel_4.setPreferredSize(new Dimension(10, 25));
		panel_4.setMinimumSize(new Dimension(10, 25));
		
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
		breakdownSelector.add(rdbtnDouble);
		breakdownSelector.add(rdbtnSingleclick);
		
		JLabel label_2 = new JLabel("More Options:");
		label_2.setFont(new Font("Tahoma", Font.BOLD, 16));
		label_2.setAlignmentX(0.5f);
		GridBagConstraints gbc_label_2 = new GridBagConstraints();
		gbc_label_2.anchor = GridBagConstraints.WEST;
		gbc_label_2.insets = new Insets(10, 5, 5, 5);
		gbc_label_2.gridx = 0;
		gbc_label_2.gridy = 6;
		panel.add(label_2, gbc_label_2);
		
		JPanel panel_6 = new JPanel();
		panel_6.setBorder(new LineBorder(Color.LIGHT_GRAY, 1, true));
		GridBagConstraints gbc_panel_6 = new GridBagConstraints();
		gbc_panel_6.gridwidth = 2;
		gbc_panel_6.insets = new Insets(0, 15, 0, 15);
		gbc_panel_6.fill = GridBagConstraints.BOTH;
		gbc_panel_6.gridx = 0;
		gbc_panel_6.gridy = 7;
		panel.add(panel_6, gbc_panel_6);
		GridBagLayout gbl_panel_6 = new GridBagLayout();
		gbl_panel_6.columnWidths = new int[] {0, 0};
		gbl_panel_6.rowHeights = new int[] {0, 0};
		gbl_panel_6.columnWeights = new double[] {0.0, Double.MIN_VALUE};
		gbl_panel_6.rowWeights = new double[] {0.0, Double.MIN_VALUE};
		panel_6.setLayout(gbl_panel_6);
		
		JPanel panel_5 = new JPanel();
		GridBagConstraints gbc_panel_5 = new GridBagConstraints();
		gbc_panel_5.gridx = 0;
		gbc_panel_5.gridy = 0;
		panel_6.add(panel_5, gbc_panel_5);
		
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
				SharedObjs.advOptions.setLocationRelativeTo(SharedObjs.satFrame);
				SharedObjs.advOptions.setVisible(true);
			}
		});
		btnAdvanced.setToolTipText("Click to see advanced options");
		btnAdvanced.setPreferredSize(new Dimension(103, 23));
		panel_5.add(btnAdvanced);
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
		
		loadDataPane();
	}
	
	// Supportive methods
	/**
	 * 
	 */
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
	}
	
	public void loadDataPane()
	{
		Logger.log(Logger.TAG_OPTIONS, "Loading option pane values data");
		
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
		
		textUsername.setText(XmlMngr.getUserValueOf(new String[] {"option_pane", "uname"}));
		SharedObjs.setUser("" + textUsername.getText());
		
		try
		{
			BufferedInputStream bin;
			bin = new BufferedInputStream(new FileInputStream(SharedObjs.pwdFile));
			String encrypt_len = XmlMngr.getUserValueOf(new String[] {"option_pane", "encrypt_len"});
			byte[] toDecrypt = new byte[Integer.parseInt(encrypt_len)];
			
			bin.read(toDecrypt);
			textPassword.setText("" + Encryptation.decrypt(toDecrypt));
			SharedObjs.setPass("" + Encryptation.decrypt(toDecrypt));
			bin.close();
		}
		catch (Exception e2)
		{
			e2.printStackTrace();
		}
		
		chkbxRemember.setSelected(Boolean.parseBoolean(XmlMngr.getUserValueOf(new String[] {
		        "option_pane",
		        "remember"})));
		
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
		
		XmlMngr.setUserValueOf(new String[] {"option_pane", "uname"}, textUsername.getText());
		
		try
		{
			BufferedOutputStream bout;
			bout = new BufferedOutputStream(new FileOutputStream(SharedObjs.pwdFile));
			byte[] encPass = Encryptation.encrypt(String.copyValueOf(textPassword.getPassword()));
			bout.write(encPass);
			bout.close();
			
			XmlMngr.setUserValueOf(new String[] {"option_pane", "encrypt_len"}, "" + encPass.length);
		}
		catch (Exception e2)
		{
			e2.printStackTrace();
		}
		
		XmlMngr.setUserValueOf(new String[] {"option_pane", "remember"}, chkbxRemember.isSelected() + "");
		
		setComments();
		
		Logger.log(Logger.TAG_OPTIONS, "Option pane values saved");
	}
	
	/**
	 * 
	 */
	private void updateUserdata()
	{
		SharedObjs.setUser(textUsername.getText());
		SharedObjs.setPass(String.copyValueOf(textPassword.getPassword()));
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
	
	public JTextField getTextUsername()
	{
		return textUsername;
	}
	
	public JPasswordField getTextPassword()
	{
		return textPassword;
	}
	
	public void setServerStatus(boolean status)
	{
		if (status)
		{
			lblServerStatus.setForeground(style.Colors.verdeEscuro);
			lblServerStatus.setText("Connected to SAT DB");
		}
		else
		{
			lblServerStatus.setForeground(style.Colors.vermelhoEscuro);
			lblServerStatus.setText("Not connected to SAT DB");
		}
	}
}
