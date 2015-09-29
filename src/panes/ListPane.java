package panes;

import java.awt.EventQueue;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;

public class ListPane extends JFrame
{
	
	private JPanel contentPane;
	private JScrollPane scrollPane;
	private JTextPane textCrKeyList;
	private JScrollPane scrollPane_1;
	private JTextPane textCrResolution;
	
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
					ListPane frame = new ListPane();
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
	public ListPane()
	{
		setTitle("Automatically closed CRs");
		setResizable(false);
		setAlwaysOnTop(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 274, 411);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(1, 2, 0, 0));
		
		scrollPane = new JScrollPane();
		contentPane.add(scrollPane);
		
		textCrKeyList = new JTextPane();
		scrollPane.setViewportView(textCrKeyList);
		
		scrollPane_1 = new JScrollPane();
		contentPane.add(scrollPane_1);
		
		textCrResolution = new JTextPane();
		scrollPane_1.setViewportView(textCrResolution);
		
		textCrKeyList.setText("");
		textCrResolution.setText("");
	}
	
	public void addItemList1(String item)
	{
		textCrKeyList.setText(textCrKeyList.getText() + item + "\n");
	}
	
	public void addItemList2(String item)
	{
		textCrResolution.setText(textCrResolution.getText() + item + "\n");
	}
}
