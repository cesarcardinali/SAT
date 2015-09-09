package supportive;


import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;

import core.Logger;
import core.SharedObjs;


public class CrsCloser implements Runnable
{
	/*
	 * closeDiagCrs
	 */
	public static void closeDiagCrs()
	{
		Object[] options = {"It's OK. Go!", "Cancel, I need to check"};
		int n = JOptionPane.showOptionDialog(SharedObjs.crsManagerPane,
		                                     "Please, make sure that your username and password are set correctly at \"CRs and Jira\" tab. Otherwise, cancel this window and "
		                                                     + "check your login data.", "Warning",
		                                     JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
		                                     null, options, options[1]);
		Logger.log(Logger.TAG_CRSCLOSER, "Resposta: " + n);
		if (n == 0)
		{
			
		}
		else
		{
			Logger.log(Logger.TAG_CRSCLOSER, "Action cancelled");
		}
	}
	
	public static void copyScript(File source, File dest) throws IOException
	{
		FileUtils.copyFile(source, dest);
	}
	
	private static void sleep(int millis)
	{
		try
		{
			Thread.sleep(millis);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void run()
	{
		closeDiagCrs();
	}
}
