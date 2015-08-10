package core;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class XmlMngr {

	/**
	 * Variables
	 */
	private static Document userDocument;		//User XML file
	private static Document systemDocument;		//System XML file
	private static Document messageDocument;	//Message XML file
	
	/**
	 * Initialize all variables and configure the class
	 */
	public static void initClass(){
		openXmlFiles();
	}
	
	/**
	 * Open XML files to be read/written
	 */
	private static void openXmlFiles(){
		try{
			// Create the XML document builder
			SAXBuilder builder = new SAXBuilder();
			
			// Create the document as to be read as a XML tree
			userDocument   = (Document) builder.build(SharedObjs.userCfgFile);
			systemDocument = (Document) builder.build(SharedObjs.sytemCfgFile);
			messageDocument = (Document) builder.build(SharedObjs.messageCfgFile);
		} catch (IOException | JDOMException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns value in user XML file.<br/>
	 * Returns null if element not found.
	 * <p>
	 * <strong> Example: </strong>
	 * XmlMngr.getUserValueOf(new String[] {"parser_pane","path"});
	 * <p>
	 * @param path Array of strings to following value<br/>
	 * @return Return the value as a string <br/>
	 */
	public static String getUserValueOf(String path[]) {
		Element requestedElement = userDocument.getRootElement();
		
		for(String item : path){
			requestedElement = requestedElement.getChild(item);
		}
		
		if(requestedElement != null){
			return requestedElement.getText();
		}
		return "null";
	}
	
	/**
	 * Returns value in system XML file.<br/>
	 * Returns null if element not found.
	 * <p>
	 * <strong> Example: </strong>
	 * XmlMngr.getSystemValueOf(new String[] {"configs","tool_name"});
	 * <p>
	 * @param path Array of strings to following value<br/>
	 * @return Return the value as a string <br/>
	 */
	public static String getSystemValueOf(String path[]) {
		Element requestedElement = systemDocument.getRootElement();
		
		for(String item : path){
			requestedElement = requestedElement.getChild(item);
		}
		
		if(requestedElement != null){
			return requestedElement.getText();
		}
		return "null";
	}
	
	/**
	 * Returns value in message XML file.<br/>
	 * Returns null if element not found.
	 * <p>
	 * <strong> Example: </strong>
	 * XmlMngr.getMessageValueOf(new String[] {"messages","error"});
	 * <p>
	 * @param path Array of {@link String} to following value<br/>
	 * @return Return the value as a string <br/>
	 */
	public static String getMessageValueOf(String path[]) {
		Element requestedElement = messageDocument.getRootElement();
		getMessageValueOf(path);
		for(String item : path){
			requestedElement = requestedElement.getChild(item);
		}
		
		if(requestedElement != null){
			return requestedElement.getText();
		}
		return "null";
	}
	
	/**
	 * Update value in user XML file
	 * <p>
	 * <strong> Example: </strong>
	 * XmlMngr.getUserValueOf(new String[] {"parser_pane","path"}, "Ops, fail!");
	 * <p>
	 * @param path Array of strings to following value<br/>
	 * @param value Value to be set as element text<br/>
	 * @return Return true if successful. False otherwise.<br/>
	 */
	public static boolean setUserValueOf(String path[], String value) {
		Element requestedElement = userDocument.getRootElement();
		
		for(String item : path){
			requestedElement = requestedElement.getChild(item);
		}
		
		if( requestedElement != userDocument.getRootElement() ){
			requestedElement.setText(value);
			return true;
		}
		return false;
	}
	
	/**
	 * Update value in system XML file
	 * <p>
	 * <strong> Example: </strong>
	 * XmlMngr.getSystemValueOf(new String[] {"configs","tool_name"}, "Ops, fail!");
	 * <p>
	 * @param path Array of strings to following value<br/>
	 * @param value Value to be set as element text<br/>
	 * @return Return true if successful. False otherwise.<br/>
	 */
	public static boolean setSystemValueOf(String path[], String value) {
		Element requestedElement = systemDocument.getRootElement();
		
		for(String item : path){
			requestedElement = requestedElement.getChild(item);
		}
		
		if(requestedElement != null){
			requestedElement.setText(value);
			return true;
		}
		return false;
	}
	
	/**
	 * Update value in message XML file.
	 * <p>
	 * <strong> Example: </strong>
	 * XmlMngr.setMessageValueOf(new String[] {"messages","error"}, "Ops, fail!");
	 * <p>
	 * @param path Array of strings to following value<br/>
	 * @param value Value to be set as element text<br/>
	 * @return Return true if successful. False otherwise.<br/>
	 */
	public static boolean setMessageValueOf(String path[], String value) {
		Element requestedElement = messageDocument.getRootElement();
		
		for(String item : path){
			requestedElement = requestedElement.getChild(item);
		}
		
		if(requestedElement != null){
			requestedElement.setText(value);
			return true;
		}
		return false;
	}
	
	/**
	 * Save all crsManager variables to its respective XML file.
	 */
	public static void saveParserData(){
		//TODO
	}
	
	/**
	 * Save all crsManager variables to its respective XML file.
	 */
	public static void saveCrsManagerData(){
		//TODO
	}
	
	/**
	 * Save all crsManager variables to its respective XML file.
	 */
	public static void saveOptionsData(){
		//TODO
	}
	
	/**
	 * Save all variables to its respective XML file.
	 */
	public static void saveAllData(){
		//TODO
	}
	
	/**
	 * Save and close all XMLs files.
	 */
	public static void closeXmls(){
		XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
        
		// Enable the following line to output XML to console for debugging
        //xmlOutputter.output(doc, System.out);
        
		// Save each XML file.
		try {
			xmlOutputter.output(userDocument,    new FileOutputStream(SharedObjs.userCfgFile));
			xmlOutputter.output(systemDocument,  new FileOutputStream(SharedObjs.sytemCfgFile));
			xmlOutputter.output(messageDocument, new FileOutputStream(SharedObjs.messageCfgFile));
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
}
