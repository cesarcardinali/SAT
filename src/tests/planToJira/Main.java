package tests.planToJira;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class Main extends JFrame
{
	
	private JPanel contentPane;
	private JButton btnLoadData;
	private JPanel crsPane;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					Main frame = new Main();
					frame.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Create the frame.
	 */
	public Main()
	{
		setTitle("JiraPane");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1110, 481);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0, 0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		crsPane = new JiraPane();
		GridBagConstraints gbc_crsPane = new GridBagConstraints();
		gbc_crsPane.gridheight = 4;
		gbc_crsPane.fill = GridBagConstraints.BOTH;
		gbc_crsPane.gridx = 2;
		gbc_crsPane.gridy = 0;
		contentPane.add(crsPane, gbc_crsPane);
		
		btnLoadData = new JButton("Load data");
		GridBagConstraints gbc_btnLoadData = new GridBagConstraints();
		gbc_btnLoadData.insets = new Insets(0, 0, 5, 5);
		gbc_btnLoadData.gridx = 0;
		gbc_btnLoadData.gridy = 1;
		contentPane.add(btnLoadData, gbc_btnLoadData);
	}
	
}
