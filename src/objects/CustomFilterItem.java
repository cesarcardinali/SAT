package objects;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import core.Logger;


/**
 * Defines a filter item
 */
public class CustomFilterItem
{
	// Variables
	private BufferedReader reader;
	private String		   name;
	private String		   regex;
	private String		   header;
	private String		   owner;
	private String		   mares;
	private String		   syres;
	private String		   keres;
	private String		   rares;
	private String		   bures;
	private String		   rores;
	private String		   res;
	private boolean		   main;
	private boolean		   system;
	private boolean		   kernel;
	private boolean		   radio;
	private boolean		   bugreport;
	private boolean		   routput;
	private boolean		   shared;
	private boolean		   editable;
	private boolean		   active;
	private boolean		   modified;
	
	public CustomFilterItem()
	{
		name = "";
		regex = "";
		header = "";
		mares = "";
		syres = "";
		keres = "";
		rares = "";
		bures = "";
		rores = "";
		owner = "";
		res = "";
		main = false;
		system = false;
		kernel = false;
		radio = false;
		bugreport = false;
		routput = false;
		active = true;
		modified = false;
	}
	
	/**
	 * Class constructor
	 * 
	 * @param own Filter owner
	 * @param name Filter name
	 * @param regex Filter regex
	 * @param header Filter header
	 * @param m If looks at main log
	 * @param s If looks at system log
	 * @param k If looks at kernel log
	 * @param r If looks at radio log
	 * @param b If looks at bugreport log
	 * @param ro If looks at report_output log
	 * @param share If filter is shared
	 * @param edit If filter is editable
	 */
	public CustomFilterItem(String own, String name, String regex, String header, boolean m, boolean s,
							boolean k, boolean r, boolean b, boolean ro, boolean share, boolean edit)
	{
		this.name = name;
		this.regex = regex;
		this.header = header;
		mares = "";
		syres = "";
		keres = "";
		rares = "";
		bures = "";
		rores = "";
		res = "";
		owner = own;
		main = m;
		system = s;
		kernel = k;
		radio = r;
		bugreport = b;
		routput = ro;
		shared = share;
		editable = edit;
		active = true;
		modified = false;
	}
	
	/**
	 * Run the filter and generate a result
	 * 
	 * @param path CR path
	 * @return Result as {@link String}
	 */
	public String runFilter(String path)
	{
		Matcher matcher;
		Pattern pattern;
		mares = "";
		syres = "";
		keres = "";
		rares = "";
		bures = "";
		rores = "";
		res = "";
		Logger.log(Logger.TAG_CUSTOMFILTER, "regex: " + regex);
		Logger.log(Logger.TAG_CUSTOMFILTER, "main: " + main);
		Logger.log(Logger.TAG_CUSTOMFILTER, "system: " + system);
		Logger.log(Logger.TAG_CUSTOMFILTER, "kernel: " + kernel);
		Logger.log(Logger.TAG_CUSTOMFILTER, "radio: " + radio);
		Logger.log(Logger.TAG_CUSTOMFILTER, "bugreport: " + bugreport);
		Logger.log(Logger.TAG_CUSTOMFILTER, "routput: " + routput);
		
		// File path
		String file = "";
		header = header.replace("\\n", "\n");
		res = header + "\n";
		
		if (main)
		{
			// Find log file
			File folder = new File(path);
			File[] listOfFiles = folder.listFiles();
			
			if (!folder.isDirectory())
			{
				mares = "Not a directory";
				res = res + "\n********From main log:\n" + mares;
			}
			
			for (int i = 0; i < listOfFiles.length; i++)
			{
				if (listOfFiles[i].getName().endsWith(".txt")
					&& listOfFiles[i].getName().toLowerCase().contains("main"))
				{
					file = path + listOfFiles[i].getName();
				}
			}
			
			Logger.log(Logger.TAG_CUSTOMFILTER, "file: " + file);
			Logger.log(Logger.TAG_CUSTOMFILTER, "path: " + path);
			
			try
			{
				reader = new BufferedReader(new FileReader(file));
				pattern = Pattern.compile(regex);
				String currentLine;
				
				while ((currentLine = reader.readLine()) != null)
				{
					matcher = pattern.matcher(currentLine);
					if (matcher.matches())
					{
						mares = mares + currentLine + "\n";
					}
				}
				
				reader.close();
				res = res + "\n********From main log:\n" + mares;
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
				Logger.log(Logger.TAG_CUSTOMFILTER, "main log missing");
				mares = "main log missing\n";
				res = res + "\n********From main log:\n" + mares;
			}
			catch (IOException e)
			{
				e.printStackTrace();
				Logger.log(Logger.TAG_CUSTOMFILTER, "IOException");
				mares = "SAT IOException\n";
				res = res + "\n********From main log:\n" + mares;
			}
		}
		
		if (system)
		{
			// Find log file
			File folder = new File(path);
			File[] listOfFiles = folder.listFiles();
			
			if (!folder.isDirectory())
			{
				syres = "Not a directory";
				res = res + "\n********From system log:\n" + syres;
			}
			
			for (int i = 0; i < listOfFiles.length; i++)
			{
				if (listOfFiles[i].getName().endsWith(".txt")
					&& listOfFiles[i].getName().toLowerCase().contains("system"))
				{
					file = path + listOfFiles[i].getName();
				}
			}
			
			Logger.log(Logger.TAG_CUSTOMFILTER, "file: " + file);
			Logger.log(Logger.TAG_CUSTOMFILTER, "path: " + path);
			
			try
			{
				reader = new BufferedReader(new FileReader(file));
				pattern = Pattern.compile(regex);
				String currentLine;
				
				while ((currentLine = reader.readLine()) != null)
				{
					matcher = pattern.matcher(currentLine);
					
					if (matcher.matches())
					{
						syres = syres + currentLine + "\n";
					}
				}
				
				reader.close();
				res = res + "\n********From system log:\n" + syres;
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
				Logger.log(Logger.TAG_CUSTOMFILTER, "System log missing");
				syres = "System log missing\n";
				res = res + "\n********From system log:\n" + syres;
			}
			catch (IOException e)
			{
				e.printStackTrace();
				Logger.log(Logger.TAG_CUSTOMFILTER, "IOException");
				syres = "SAT IOException\n";
				res = res + "\n********From system log:\n" + syres;
			}
		}
		
		if (kernel)
		{
			// Find log file
			File folder = new File(path);
			File[] listOfFiles = folder.listFiles();
			keres = "";
			
			if (!folder.isDirectory())
			{
				keres = "Not a directory";
				res = res + "\n********From kernel log:\n" + keres;
			}
			
			for (int i = 0; i < listOfFiles.length; i++)
			{
				if (listOfFiles[i].getName().endsWith(".txt")
					&& listOfFiles[i].getName().toLowerCase().contains("kernel"))
				{
					file = path + listOfFiles[i].getName();
				}
			}
			
			Logger.log(Logger.TAG_CUSTOMFILTER, "file: " + file);
			Logger.log(Logger.TAG_CUSTOMFILTER, "path: " + path);
			
			try
			{
				reader = new BufferedReader(new FileReader(file));
				pattern = Pattern.compile(regex);
				String currentLine;
				
				while ((currentLine = reader.readLine()) != null)
				{
					matcher = pattern.matcher(currentLine);
					
					if (matcher.matches())
					{
						keres = keres + currentLine + "\n";
					}
				}
				reader.close();
				res = res + "\n********From kernel log:\n" + keres;
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
				Logger.log(Logger.TAG_CUSTOMFILTER, "Kernel log missing");
				keres = "Kernel log missing\n";
				res = res + "\n********From kernel log:\n" + keres;
			}
			catch (IOException e)
			{
				e.printStackTrace();
				Logger.log(Logger.TAG_CUSTOMFILTER, "IOException");
				keres = "SAT IOException\n";
				res = res + "\n********From kernel log:\n" + keres;
			}
		}
		
		if (radio)
		{
			// Find log file
			File folder = new File(path);
			File[] listOfFiles = folder.listFiles();
			rares = "";
			
			if (!folder.isDirectory())
			{
				rares = "Not a directory";
				res = res + "\n********From radio log:\n" + rares;
			}
			
			for (int i = 0; i < listOfFiles.length; i++)
			{
				if (listOfFiles[i].getName().endsWith(".txt")
					&& listOfFiles[i].getName().toLowerCase().contains("radio"))
				{
					file = path + listOfFiles[i].getName();
				}
			}
			
			Logger.log(Logger.TAG_CUSTOMFILTER, "file: " + file);
			Logger.log(Logger.TAG_CUSTOMFILTER, "path: " + path);
			
			try
			{
				reader = new BufferedReader(new FileReader(file));
				pattern = Pattern.compile(regex);
				String currentLine;
				
				while ((currentLine = reader.readLine()) != null)
				{
					matcher = pattern.matcher(currentLine);
					if (matcher.matches())
					{
						rares = rares + currentLine + "\n";
					}
				}
				
				reader.close();
				res = res + "\n********From radio log:\n" + rares;
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
				Logger.log(Logger.TAG_CUSTOMFILTER, "radio log missing");
				rares = "radio log missing\n";
				res = res + "\n********From radio log:\n" + rares;
			}
			catch (IOException e)
			{
				e.printStackTrace();
				Logger.log(Logger.TAG_CUSTOMFILTER, "IOException");
				rares = "SAT IOException\n";
				res = res + "\n********From radio log:\n" + rares;
			}
		}
		
		if (bugreport)
		{
			// Find log file
			File folder = new File(path);
			File[] listOfFiles = folder.listFiles();
			bures = "";
			
			if (!folder.isDirectory())
			{
				bures = "Not a directory";
				res = res + "\n********From bugreport log:\n" + bures;
			}
			
			for (int i = 0; i < listOfFiles.length; i++)
			{
				if (listOfFiles[i].getName().endsWith(".txt")
					&& listOfFiles[i].getName().toLowerCase().contains("bugreport"))
				{
					file = path + listOfFiles[i].getName();
				}
			}
			
			Logger.log(Logger.TAG_CUSTOMFILTER, "file: " + file);
			Logger.log(Logger.TAG_CUSTOMFILTER, "path: " + path);
			
			try
			{
				reader = new BufferedReader(new FileReader(file));
				pattern = Pattern.compile(regex);
				String currentLine;
				
				while ((currentLine = reader.readLine()) != null)
				{
					matcher = pattern.matcher(currentLine);
					
					if (matcher.matches())
					{
						bures = bures + currentLine + "\n";
					}
				}
				
				reader.close();
				res = res + "\n********From bugreport log:\n" + bures;
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
				Logger.log(Logger.TAG_CUSTOMFILTER, "bugreport log missing");
				bures = "bugreport log missing\n";
				res = res + "\n********From bugreport log:\n" + bures;
			}
			catch (IOException e)
			{
				e.printStackTrace();
				Logger.log(Logger.TAG_CUSTOMFILTER, "IOException");
				bures = "SAT IOException\n";
				res = res + "\n********From bugreport log:\n" + bures;
			}
		}
		
		if (routput)
		{
			// Find log file
			File folder = new File(path);
			File[] listOfFiles = folder.listFiles();
			rores = "";
			
			if (!folder.isDirectory())
			{
				rores = "Not a directory";
				res = res + "\n********From report output log:********\n" + rores;
			}
			
			for (int i = 0; i < listOfFiles.length; i++)
			{
				if (listOfFiles[i].getName().endsWith(".txt")
					&& listOfFiles[i].getName().toLowerCase().contains("report output"))
				{
					file = path + listOfFiles[i].getName();
				}
			}
			
			Logger.log(Logger.TAG_CUSTOMFILTER, "file: " + file);
			Logger.log(Logger.TAG_CUSTOMFILTER, "path: " + path);
			
			try
			{
				reader = new BufferedReader(new FileReader(file));
				pattern = Pattern.compile(regex);
				String currentLine;
				
				while ((currentLine = reader.readLine()) != null)
				{
					matcher = pattern.matcher(currentLine);
					if (matcher.matches())
					{
						rores = rores + currentLine + "\n";
					}
				}
				
				reader.close();
				res = res + "\n********From report output log:********\n" + rores;
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
				Logger.log(Logger.TAG_CUSTOMFILTER, "report output log missing");
				rores = "report output log missing\n";
				res = res + "\n********From report output log:********\n" + rores;
			}
			catch (IOException e)
			{
				e.printStackTrace();
				Logger.log(Logger.TAG_CUSTOMFILTER, "IOException");
				rores = "SAT IOException\n";
				res = res + "\n********From report output log:********\n" + rores;
			}
		}
		
		return res;
	}
	
	// Getters and Setters
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getRegex()
	{
		return regex;
	}
	
	public void setRegex(String regex)
	{
		this.regex = regex;
	}
	
	public String getHeader()
	{
		return header;
	}
	
	public void setHeader(String header)
	{
		this.header = header;
	}
	
	public BufferedReader getReader()
	{
		return reader;
	}
	
	public void setReader(BufferedReader reader)
	{
		this.reader = reader;
	}
	
	public boolean isMain()
	{
		return main;
	}
	
	public void setMain(boolean main)
	{
		this.main = main;
	}
	
	public boolean isSystem()
	{
		return system;
	}
	
	public void setSystem(boolean system)
	{
		this.system = system;
	}
	
	public boolean isKernel()
	{
		return kernel;
	}
	
	public void setKernel(boolean kernel)
	{
		this.kernel = kernel;
	}
	
	public boolean isRadio()
	{
		return radio;
	}
	
	public void setRadio(boolean radio)
	{
		this.radio = radio;
	}
	
	public boolean isBugreport()
	{
		return bugreport;
	}
	
	public void setBugreport(boolean bugreport)
	{
		this.bugreport = bugreport;
	}
	
	public boolean isRoutput()
	{
		return routput;
	}
	
	public void setRoutput(boolean routput)
	{
		this.routput = routput;
	}
	
	public String getOwner()
	{
		return owner;
	}
	
	public void setOwner(String owner)
	{
		this.owner = owner;
	}
	
	public String getMares()
	{
		return mares;
	}
	
	public void setMares(String mares)
	{
		this.mares = mares;
	}
	
	public String getSyres()
	{
		return syres;
	}
	
	public void setSyres(String syres)
	{
		this.syres = syres;
	}
	
	public String getKeres()
	{
		return keres;
	}
	
	public void setKeres(String keres)
	{
		this.keres = keres;
	}
	
	public String getRares()
	{
		return rares;
	}
	
	public void setRares(String rares)
	{
		this.rares = rares;
	}
	
	public String getBures()
	{
		return bures;
	}
	
	public void setBures(String bures)
	{
		this.bures = bures;
	}
	
	public String getRores()
	{
		return rores;
	}
	
	public void setRores(String rores)
	{
		this.rores = rores;
	}
	
	public boolean isShared()
	{
		return shared;
	}
	
	public void setShared(boolean shared)
	{
		this.shared = shared;
	}
	
	public boolean isEditable()
	{
		return editable;
	}
	
	public void setEditable(boolean edit)
	{
		this.editable = edit;
	}
	
	public String getResult()
	{
		return res;
	}
	
	public boolean isActive()
	{
		return active;
	}
	
	public void setActive(boolean active)
	{
		this.active = active;
	}
	
	public String getRes()
	{
		return res;
	}

	public boolean isModified()
	{
		return modified;
	}

	public void setRes(String res)
	{
		this.res = res;
	}

	public void setModified(boolean modified)
	{
		this.modified = modified;
	}

	public String toString()
	{
		return "Filter: " + name + "\nRegex: " + regex + "\nHeader: " + header + "\nOwner: " + owner
			   + "\nMain: " + main + "\nSystem: " + system + "\nKernel: " + kernel + "\nRadio: " + radio
			   + "\nBugreport: " + bugreport + "\nRepOutput: " + routput + "\nShared: " + shared
			   + "\nEditable: " + editable + "\nActive: " + active;
	}
}