package core;


import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import objects.CustomFilterItem;
import objects.CustomFiltersList;


/**
 * It deals with all XML features
 */
public class XmlMngr
{
	/**
	 * Variables
	 */
	private static Document	userDocument;	 // User XML file
	private static Document	systemDocument;	 // System XML file
	private static Document	messageDocument; // Message XML file
	private static Document	filtersDocument; // Filters XML file
	
	/**
	 * Initialize all variables and configure the class
	 */
	public static void initClass()
	{
		openXmlFiles();
	}
	
	/**
	 * Open XML files to be read/written
	 */
	private static void openXmlFiles()
	{
		try
		{
			// Create the XML document builder
			SAXBuilder builder = new SAXBuilder();
			
			// Create the document as to be read as a XML tree
			userDocument = (Document) builder.build(SharedObjs.userCfgFile);
			systemDocument = (Document) builder.build(SharedObjs.sytemCfgFile);
			messageDocument = (Document) builder.build(SharedObjs.messageCfgFile);
			filtersDocument = (Document) builder.build(SharedObjs.filtersFile);
		}
		catch (IOException | JDOMException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns value in user XML file.<br/>
	 * Returns null if element not found.
	 * <p>
	 * <strong> Example: </strong> XmlMngr.getUserValueOf(new String[] {"parser_pane","path"});
	 * <p>
	 * 
	 * @param path Array of strings to following value<br/>
	 * @return Return the value as a string <br/>
	 */
	public static String getUserValueOf(String path[])
	{
		Element requestedElement = userDocument.getRootElement();
		
		for (String item : path)
		{
			requestedElement = requestedElement.getChild(item);
		}
		
		if (requestedElement != null)
		{
			return requestedElement.getText();
		}
		
		return "null";
	}
	
	/**
	 * Returns value in system XML file.<br/>
	 * Returns null if element not found.
	 * <p>
	 * <strong> Example: </strong> XmlMngr.getSystemValueOf(new String[] {"configs","tool_name"});
	 * <p>
	 * 
	 * @param path Array of strings to following value<br/>
	 * @return Return the value as a string <br/>
	 */
	public static String getSystemValueOf(String path[])
	{
		Element requestedElement = systemDocument.getRootElement();
		
		for (String item : path)
		{
			requestedElement = requestedElement.getChild(item);
		}
		
		if (requestedElement != null)
		{
			return requestedElement.getText();
		}
		
		return "null";
	}
	
	/**
	 * Returns value in message XML file.<br/>
	 * Returns null if element not found.
	 * <p>
	 * <strong> Example: </strong> XmlMngr.getMessageValueOf(new String[] {"messages","error"});
	 * <p>
	 * 
	 * @param path Array of {@link String} to following value<br/>
	 * @return Return the value as a string <br/>
	 */
	public static String getMessageValueOf(String path[])
	{
		Element requestedElement = messageDocument.getRootElement();
		
		for (String item : path)
		{
			requestedElement = requestedElement.getChild(item);
		}
		
		if (requestedElement != null)
		{
			return requestedElement.getText();
		}
		
		return "null";
	}
	
	/**
	 * Get user filter item
	 * 
	 * @param name Name of the filter
	 * @return Filter item as {@link CustomFilterItem}. Null if not found.
	 */
	public static CustomFilterItem getMyFiltersValueOf(String name)
	{
		Element requestedElement = filtersDocument.getRootElement().getChild("myFilters").getChild(name);
		
		if (requestedElement != null)
		{
			CustomFilterItem filter;
			filter = new CustomFilterItem(requestedElement.getChildText("owner"),
										  requestedElement.getText().replace("_", " "),
										  requestedElement.getChildText("regex"),
										  requestedElement.getChildText("header"),
										  Boolean.parseBoolean(requestedElement.getChildText("main")),
										  Boolean.parseBoolean(requestedElement.getChildText("system")),
										  Boolean.parseBoolean(requestedElement.getChildText("kernel")),
										  Boolean.parseBoolean(requestedElement.getChildText("radio")),
										  Boolean.parseBoolean(requestedElement.getChildText("bugreport")),
										  Boolean.parseBoolean(requestedElement.getChildText("routput")),
										  Boolean.parseBoolean(requestedElement.getChildText("shared")),
										  Boolean.parseBoolean(requestedElement.getChildText("editable")),
										  Boolean.parseBoolean(requestedElement.getChildText("active")));
			return filter;
		}
		
		return null;
	}
	
	/**
	 * Get shared filter item
	 * 
	 * @param name Name of the filter
	 * @param owner Filter owner name
	 * @return Filter item as {@link CustomFilterItem}. Null if not found.
	 */
	public static CustomFilterItem getSharedFiltersValueOf(String name, String owner)
	{
		Element requestedElement = filtersDocument.getRootElement().getChild("sharedFilters").getChild(name);
		
		if (requestedElement != null && requestedElement.getText().equals(name)
			&& requestedElement.getChildTextTrim("owner").equals(owner))
		{
			CustomFilterItem filter;
			filter = new CustomFilterItem(requestedElement.getChildText("owner"),
			                              requestedElement.getText().replace("_", " "),
										  requestedElement.getChildText("regex"),
										  requestedElement.getChildText("header"),
										  Boolean.parseBoolean(requestedElement.getChildText("main")),
										  Boolean.parseBoolean(requestedElement.getChildText("system")),
										  Boolean.parseBoolean(requestedElement.getChildText("kernel")),
										  Boolean.parseBoolean(requestedElement.getChildText("radio")),
										  Boolean.parseBoolean(requestedElement.getChildText("bugreport")),
										  Boolean.parseBoolean(requestedElement.getChildText("routput")),
										  Boolean.parseBoolean(requestedElement.getChildText("shared")),
										  Boolean.parseBoolean(requestedElement.getChildText("editable")),
										  Boolean.parseBoolean(requestedElement.getChildText("active")));
			return filter;
		}
		
		return null;
	}
	
	/**
	 * Get all user filters
	 * 
	 * @return Array of {@link CustomFilterItem}
	 */
	public static CustomFiltersList getAllMyFilters()
	{
		Element requestedElement = filtersDocument.getRootElement().getChild("myFilters");
		CustomFiltersList filters = new CustomFiltersList();
		
		for (int i=0; i < requestedElement.getChildren().size(); i++)
		{
			Element filterElement = requestedElement.getChildren().get(i);
			filters.add(new CustomFilterItem(filterElement.getChildText("owner"),
			                                  filterElement.getName().replace("_", " "),
			                                  filterElement.getChildText("regex"),
			                                  filterElement.getChildText("header"),
										  Boolean.parseBoolean(filterElement.getChildText("main")),
										  Boolean.parseBoolean(filterElement.getChildText("system")),
										  Boolean.parseBoolean(filterElement.getChildText("kernel")),
										  Boolean.parseBoolean(filterElement.getChildText("radio")),
										  Boolean.parseBoolean(filterElement.getChildText("bugreport")),
										  Boolean.parseBoolean(filterElement.getChildText("routput")),
										  Boolean.parseBoolean(filterElement.getChildText("shared")),
										  Boolean.parseBoolean(filterElement.getChildText("editable")),
										  Boolean.parseBoolean(filterElement.getChildText("active"))));
		}
		Logger.log(Logger.TAG_XMLMNGR, "MyFilters loaded: " + filters.size());
		
		return filters;
	}
	
	/**
	 * Get all shared filters
	 * 
	 * @return Array of {@link CustomFilterItem}
	 */
	public static CustomFiltersList getAllSharedFilters()
	{
		Element requestedElement = filtersDocument.getRootElement().getChild("sharedFilters");
		CustomFiltersList filters = new CustomFiltersList();
		
		for (int i=0; i < requestedElement.getChildren().size(); i++)
		{
			Element filterElement = requestedElement.getChildren().get(i);
			filters.add(new CustomFilterItem(filterElement.getChildText("owner"),
			                                  filterElement.getName().replace("_", " "),
			                                  filterElement.getChildText("regex"),
			                                  filterElement.getChildText("header"),
										  Boolean.parseBoolean(filterElement.getChildText("main")),
										  Boolean.parseBoolean(filterElement.getChildText("system")),
										  Boolean.parseBoolean(filterElement.getChildText("kernel")),
										  Boolean.parseBoolean(filterElement.getChildText("radio")),
										  Boolean.parseBoolean(filterElement.getChildText("bugreport")),
										  Boolean.parseBoolean(filterElement.getChildText("routput")),
										  Boolean.parseBoolean(filterElement.getChildText("shared")),
										  Boolean.parseBoolean(filterElement.getChildText("editable")),
										  Boolean.parseBoolean(filterElement.getChildText("active"))));
		}
		
		return filters;
	}
	
	/**
	 * Update value in user XML file
	 * <p>
	 * <strong> Example: </strong> XmlMngr.getUserValueOf(new String[] {"parser_pane","path"}, "Ops, fail!");
	 * <p>
	 * 
	 * @param path Array of strings to following value<br/>
	 * @param value Value to be set as element text<br/>
	 * @return Return true if successful. False otherwise.<br/>
	 */
	public static boolean setUserValueOf(String path[], String value)
	{
		Element requestedElement = userDocument.getRootElement();
		
		for (String item : path)
		{
			requestedElement = requestedElement.getChild(item);
		}
		
		if (requestedElement != userDocument.getRootElement())
		{
			requestedElement.setText(value);
			return true;
		}
		
		return false;
	}
	
	/**
	 * Update value in system XML file
	 * <p>
	 * <strong> Example: </strong> XmlMngr.getSystemValueOf(new String[] {"configs","tool_name"}, "Ops, fail!");
	 * <p>
	 * 
	 * @param path Array of strings to following value<br/>
	 * @param value Value to be set as element text<br/>
	 * @return Return true if successful. False otherwise.<br/>
	 */
	public static boolean setSystemValueOf(String path[], String value)
	{
		Element requestedElement = systemDocument.getRootElement();
		
		for (String item : path)
		{
			requestedElement = requestedElement.getChild(item);
		}
		
		if (requestedElement != null)
		{
			requestedElement.setText(value);
			return true;
		}
		
		return false;
	}
	
	/**
	 * Update value in message XML file.
	 * <p>
	 * <strong> Example: </strong> XmlMngr.setMessageValueOf(new String[] {"messages","error"}, "Ops, fail!");
	 * <p>
	 * 
	 * @param path Array of strings to following value<br/>
	 * @param value Value to be set as element text<br/>
	 * @return Return true if successful. False otherwise.<br/>
	 */
	public static boolean setMessageValueOf(String path[], String value)
	{
		Element requestedElement = messageDocument.getRootElement();
		
		for (String item : path)
		{
			requestedElement = requestedElement.getChild(item);
		}
		
		if (requestedElement != null)
		{
			requestedElement.setText(value);
			return true;
		}
		
		return false;
	}
	
	/**
	 * Set a user filter
	 * @param filter {@link CustomFilterItem}
	 * @return true if successful. false if not.
	 */
	public static boolean setMyFiltersValueOf(CustomFilterItem filter)
	{
		if (!filter.getName().equals(""))
		{
			Element myFiltersElement = filtersDocument.getRootElement().getChild("myFilters");
			Element requestedElement = myFiltersElement.getChild(filter.getName().replace(" ", "_"));
			
			if (requestedElement != null)
			{
				requestedElement.getChild("regex").setText(filter.getRegex());
				requestedElement.getChild("header").setText(filter.getHeader());
				requestedElement.getChild("owner").setText(filter.getOwner());
				requestedElement.getChild("main").setText("" + filter.isMain());
				requestedElement.getChild("system").setText("" + filter.isSystem());
				requestedElement.getChild("kernel").setText("" + filter.isKernel());
				requestedElement.getChild("radio").setText("" + filter.isRadio());
				requestedElement.getChild("bugreport").setText("" + filter.isBugreport());
				requestedElement.getChild("routput").setText("" + filter.isRoutput());
				requestedElement.getChild("shared").setText("" + filter.isShared());
				requestedElement.getChild("editable").setText("" + filter.isEditable());
				//requestedElement.getChild("active").setText("" + filter.isActive());
			}
			else
			{
				Element xmlElement = new Element(filter.getName().replace(" ", "_"));
				xmlElement.addContent(new Element("regex").setText(filter.getRegex()));
				xmlElement.addContent(new Element("header").setText(filter.getHeader()));
				xmlElement.addContent(new Element("owner").setText(filter.getOwner()));
				xmlElement.addContent(new Element("main").setText("" + filter.isMain()));
				xmlElement.addContent(new Element("system").setText("" + filter.isSystem()));
				xmlElement.addContent(new Element("kernel").setText("" + filter.isKernel()));
				xmlElement.addContent(new Element("radio").setText("" + filter.isRadio()));
				xmlElement.addContent(new Element("bugreport").setText("" + filter.isBugreport()));
				xmlElement.addContent(new Element("routput").setText("" + filter.isRoutput()));
				xmlElement.addContent(new Element("shared").setText("" + filter.isShared()));
				xmlElement.addContent(new Element("editable").setText("" + filter.isEditable()));
				xmlElement.addContent(new Element("active").setText("" + filter.isActive()));
				myFiltersElement.addContent(xmlElement);
			}
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Set a shared filter
	 * @param filter {@link CustomFilterItem}
	 * @return true if successful. false if not.
	 */
	public static boolean setSharedFiltersValueOf(CustomFilterItem filter)
	{
		Element sharedFiltersElement = filtersDocument.getRootElement().getChild("sharedFilters");
		Element requestedElement = sharedFiltersElement.getChild(filter.getName().replace(" ", "_"));
		
		if (requestedElement != null)
		{
			requestedElement.getChild("regex").setText(filter.getRegex());
			requestedElement.getChild("header").setText(filter.getHeader());
			requestedElement.getChild("owner").setText(filter.getOwner());
			requestedElement.getChild("main").setText("" + filter.isMain());
			requestedElement.getChild("system").setText("" + filter.isSystem());
			requestedElement.getChild("kernel").setText("" + filter.isKernel());
			requestedElement.getChild("radio").setText("" + filter.isRadio());
			requestedElement.getChild("bugreport").setText("" + filter.isBugreport());
			requestedElement.getChild("routput").setText("" + filter.isRoutput());
			requestedElement.getChild("shared").setText("" + filter.isShared());
			requestedElement.getChild("editable").setText("" + filter.isEditable());
			//requestedElement.getChild("active").setText("" + filter.isActive());
		}
		else
		{
			Element xmlElement = new Element(filter.getName().replace(" ", "_"));
			xmlElement.addContent(new Element("regex").setText(filter.getRegex()));
			xmlElement.addContent(new Element("header").setText(filter.getHeader()));
			xmlElement.addContent(new Element("owner").setText(filter.getOwner()));
			xmlElement.addContent(new Element("main").setText("" + filter.isMain()));
			xmlElement.addContent(new Element("system").setText("" + filter.isSystem()));
			xmlElement.addContent(new Element("kernel").setText("" + filter.isKernel()));
			xmlElement.addContent(new Element("radio").setText("" + filter.isRadio()));
			xmlElement.addContent(new Element("bugreport").setText("" + filter.isBugreport()));
			xmlElement.addContent(new Element("routput").setText("" + filter.isRoutput()));
			xmlElement.addContent(new Element("shared").setText("" + filter.isShared()));
			xmlElement.addContent(new Element("editable").setText("" + filter.isEditable()));
			xmlElement.addContent(new Element("active").setText("false"));
			sharedFiltersElement.addContent(xmlElement);
		}
		
		return true;
	}
	
	/**
	 * @param filters
	 * @return
	 */
	public static boolean addMyFilters(CustomFiltersList filters)
	{
		for (CustomFilterItem filter : filters)
		{
			setMyFiltersValueOf(filter);
		}
		
		return true;
	}
	
	/**
	 * @param filters
	 * @return
	 */
	public static boolean addSharedFilters(CustomFiltersList filters)
	{
		for (CustomFilterItem filter : filters)
		{
			setSharedFiltersValueOf(filter);
		}
		
		return true;
	}
	
	/**
	 * @param filter
	 * @return
	 */
	public static boolean removeMyFiltersValueOf(CustomFilterItem filter)
	{
		Element myFiltersElement = filtersDocument.getRootElement().getChild("myFilters");
		Element requestedElement = myFiltersElement.getChild(filter.getName().replace(" ", "_"));
		
		if(requestedElement != null)
			myFiltersElement.removeChild(filter.getName().replace(" ", "_"));
		else
			return false;
		
		return true;
	}
	
	/**
	 * @return
	 */
	public static boolean removeAllMyFilters()
	{
		Element myFiltersElement = filtersDocument.getRootElement().getChild("myFilters");
		
		if(myFiltersElement != null)
			myFiltersElement.removeContent();
		else
			return false;
							
		return true;
	}
	
	/**
	 * @param filter
	 * @return
	 */
	public static boolean removeSharedFiltersValueOf(CustomFilterItem filter)
	{
		Element myFiltersElement = filtersDocument.getRootElement().getChild("sharedFilters");
		Element requestedElement = myFiltersElement.getChild(filter.getName().replace(" ", "_"));
		
		if(requestedElement != null)
			myFiltersElement.removeChild(filter.getName().replace(" ", "_"));
		else
			return false;
		
		return true;
	}
	
	/**
	 * @return
	 */
	public static boolean removeAllSharedFilters()
	{
		Element myFiltersElement = filtersDocument.getRootElement().getChild("sharedFilters");
		
		if(myFiltersElement != null)
			myFiltersElement.removeContent();
		else
			return false;
							
		return true;
	}
	
	/**
	 * Save all crsManager variables to its respective XML file.
	 */
	public static void saveParserData()
	{
		// TODO
	}
	
	/**
	 * Save all crsManager variables to its respective XML file.
	 */
	public static void saveCrsManagerData()
	{
		// TODO
	}
	
	/**
	 * Save all crsManager variables to its respective XML file.
	 */
	public static void saveOptionsData()
	{
		// TODO
	}
	
	/**
	 * Save all variables to its respective XML file.
	 */
	public static void saveAllData()
	{
		// TODO
	}
	
	/**
	 * Save and close all XMLs files.
	 */
	public static void closeXmls()
	{
		XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
		
		// Enable the following line to output XML to console for
		// debugging
		// xmlOutputter.output(doc, System.out);
		
		// Save each XML file.
		try
		{
			xmlOutputter.output(userDocument, new FileOutputStream(SharedObjs.userCfgFile));
			xmlOutputter.output(systemDocument, new FileOutputStream(SharedObjs.sytemCfgFile));
			xmlOutputter.output(messageDocument, new FileOutputStream(SharedObjs.messageCfgFile));
			xmlOutputter.output(filtersDocument, new FileOutputStream(SharedObjs.filtersFile));
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * @param pass
	 */
	public static void savePass(String pass)
	{
		try
		{
			BufferedWriter writer = new BufferedWriter(new FileWriter(SharedObjs.pwdFile));
			writer.write(pass);
			writer.close();
		}
		catch (IOException e)
		{
			Logger.log(Logger.TAG_XMLMNGR, "Error saving password");
			e.printStackTrace();
		}
	}

}
