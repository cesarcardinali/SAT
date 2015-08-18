package panes;


import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Font;


@SuppressWarnings("serial")
public class ProgressDialog extends JDialog
{
	private JPanel	panel;
	private JLabel	message;
	private JLabel	filesToProcess;
	private JLabel	filesDone;
	private JButton	button;
	private JFrame	frame;
	private int		files;
	
	public ProgressDialog(JFrame parentFrame, int numberOfFiles)
	{	
		files = numberOfFiles;
		frame = parentFrame;
		
		setTitle("Processing ...");
		setLocationRelativeTo(parentFrame);
		setVisible(true);
		setResizable(false);
		setSize(436, 220);
		
		panel = new JPanel();
		
		button = new JButton("Hide");
		button.setBounds(272, 128, 118, 34);
		button.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				// TODO Auto-generated method stub
				if (button.getText().equals("Hide"))
					setVisible(false);
				else
				{
					dispose();
				}
			}
		});
		
		message = new JLabel("Wait while SAT processes your request");
		message.setFont(new Font("Tahoma", Font.BOLD, 15));
		message.setBounds(71, 22, 303, 23);
		
		filesToProcess = new JLabel("Files to be processed: " + String.valueOf(numberOfFiles));
		filesToProcess.setBounds(41, 68, 150, 23);
		
		filesDone = new JLabel("Done: 0");
		filesDone.setBounds(41, 92, 118, 23);
		
		panel = new JPanel();
		panel.setLayout(null);
		panel.add(message);
		panel.add(filesToProcess);
		panel.add(filesDone);
		panel.add(button);
		getContentPane().add(panel);
	}
	
	public void updateDialogView(int remainingCRs)
	{
		filesDone.setText("Done: " + String.valueOf(remainingCRs));
		filesDone.updateUI();
		
		if (files == remainingCRs)
		{
			setTitle("Done");
			setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
			toFront();
			setAlwaysOnTop(true);
			setVisible(true);
			
			button.setText("Done");
			frame.toFront();
		}
	}
}
