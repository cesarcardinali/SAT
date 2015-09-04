package main;


import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;

import core.Icons;
import core.Logger;
import core.SharedObjs;
import core.Strings;
import core.XmlMngr;


/**
 * Main class. Generates the UI Frame
 */
@SuppressWarnings("serial")
public class SAT extends JFrame
{
	/**
	 * Variables
	 */
	boolean updating = false; // Checking for update
	
	/**
	 * Runnable
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				// Initialize all static classes
				XmlMngr.initClass();
				Logger.initClass();
				SharedObjs.initClass();
				
				// Start UI
				SharedObjs.satFrame.setVisible(true);
			}
		});
	}
	
	/**
	 * Configure application.
	 */
	public SAT()
	{
		// Initializing window
		setIconImage(Icons.iconSat);
		setTitle(Strings.getToolName() + " " + Strings.getToolVersion());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		double height = screenSize.getHeight();
		setBounds((int) (width / 3), 0, (int) (width / 1.5), (int) height - 40);
		setVisible(true);
		setMinimumSize(new Dimension(800, 600));
		
		// Inserting the TabPane
		getContentPane().add(SharedObjs.tabbedPane);
		
		// Window Focus Listener
		addWindowFocusListener(satWFL);
		
		// Save configurations on close
		Runtime.getRuntime().addShutdownHook(onShutdown);
		
		// Start updater thread
		updateThread();
	}
	
	/**
	 * Keep looking for new updates
	 */
	private void updateThread()
	{
		new Thread(new Runnable()
		{
			int stop = 0;
			
			@Override
			public void run()
			{
				stop = checkForUpdate();
				while (stop == 0)
				{
					try
					{
						Thread.sleep(900000); // Check for update each 15 minutes
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
					stop = checkForUpdate();
				}
				if (stop == 1)
				{
					Logger.log(Logger.TAG_SAT, "\n\nExiting");
					System.exit(0);
				}
			}
		}).start();
	}
	
	/**
	 * Check for newer version
	 */
	public int checkForUpdate()
	{
		Logger.log(Logger.TAG_SAT, "Checking for update");
		
		updating = true;
		File f1;
		File f2;
		long dateRemote, dateLocal;
		f1 = new File(SharedObjs.updateFolder1 + Strings.getToolFileName());
		
		Logger.log(Logger.TAG_SAT, "Remote file: " + f1.getAbsolutePath() + " - Modified: " + f1.lastModified());
		
		f2 = new File(Strings.getToolFileName());
		
		Logger.log(Logger.TAG_SAT, "Local file: " + f2.getAbsolutePath() + " - Modified: " + f2.lastModified());
		
		dateRemote = f1.lastModified();
		dateLocal = f2.lastModified();
		
		if (dateLocal < dateRemote && dateLocal != 0)
		{
			Object[] options = new Object[] {"Yes", "No"};
			int n = JOptionPane.showOptionDialog(null,
												 "Uma nova versão foi encontrada. Voce desaja atualizar agora?",
												 "New version available", JOptionPane.YES_NO_CANCEL_OPTION,
												 JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
												 
			if (n == 0)
			{
				try
				{
					Logger.log(Logger.TAG_SAT,
							   "Updating the Updater first, from: " + SharedObjs.updateFolder1);
					FileUtils.copyFile(new File(SharedObjs.updateFolder1 + "/"
												+ Strings.getUpdaterFileName()),
									   new File(Strings.getUpdaterFileName()));
				}
				catch (IOException e)
				{
					Logger.log(Logger.TAG_SAT, "Updating the Updater failed");
					e.printStackTrace();
				}
				
				Logger.log(Logger.TAG_SAT, "Updating");
				
				try
				{
					Logger.log(Logger.TAG_SAT, "path: " + new File("").getAbsolutePath());
					ProcessBuilder builder = new ProcessBuilder("cmd.exe",
																"/c",
																"cd " + new File("").getAbsolutePath()
																	  + " && java -jar "
																	  + Strings.getUpdaterFileName());
					builder.start();
				}
				catch (IOException e2)
				{
					e2.printStackTrace();
				}
				
				return 1;
			}
			else
			{
				return 2;
			}
		}
		else
		{
			Logger.log(Logger.TAG_SAT, "SAT is up to date");
		}
		
		return 0;
	}
	
	/**
	 * Thread to run when shutting down SAT application
	 */
	Thread onShutdown = new Thread(new Runnable()
	{
		@Override
		public void run()
		{
			int run = 0;
			
			while (run == 0)
			{
				Logger.log(Logger.TAG_SAT, "Saving all user data ...");
				SharedObjs.crsManagerPane.saveUserData();
				SharedObjs.parserPane.savePaneData();
				SharedObjs.optionsPane.savePaneData();
				run = 1;
				Logger.log(Logger.TAG_SAT, "Done");
			}
			
			XmlMngr.closeXmls();
			Logger.close();
		}
	});
	
	/**
	 * Window Focus Listener used on SAT main Frame
	 */
	WindowFocusListener satWFL = new WindowFocusListener()
	{
		@Override
		public void windowLostFocus(WindowEvent e)
		{
		}
		
		@Override
		public void windowGainedFocus(WindowEvent e)
		{
		}
	};
	
	// Getters and Setters:
	public boolean isUpdating()
	{
		return updating;
	}
	
	public void setUpdating(boolean is)
	{
		updating = is;
	}
}