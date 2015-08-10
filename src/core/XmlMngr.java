package core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Timestamp;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class XmlMngr {

	/**
	 * Variables
	 */
	private Document userDocument;		//User XML file
	private Document systemDocument;	//System XML file
	
	/**
	 * Initialize all variables and configure the class
	 */
	public void initClass(){
		openUserXml();
		openSystemXml();
	}
	
	/**
	 * Open user XML file to be read/written
	 */
	private void openUserXml(){
		try{
			//Cria o builder da estrutura XML
			SAXBuilder builder = new SAXBuilder();
			
			//Cria documento formatado de acordo com a lib XML
			userDocument = (Document) builder.build(SharedObjs.userCfgFile);
		} catch (IOException | JDOMException e){
			e.printStackTrace();
		}
	}

	/**
	 * Open system XML file to be read/written
	 */
	private void openSystemXml(){
		try{
			//Cria o builder da estrutura XML
			SAXBuilder builder = new SAXBuilder();
			
			//Cria documento formatado de acordo com a lib XML
			systemDocument = (Document) builder.build(SharedObjs.sytemCfgFile);
		} catch (IOException | JDOMException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Find value in user XML file
	 * <p>
	 * <strong> Example: </strong>
	 * XmlMngr.getUserValueOf(new String[] {"parser_pane","path"});
	 * <p>
	 * @param path Array of strings to following value<br/>
	 * @return Return the value as a string <br/>
	 */
	public static String getUserValueOf(String path[]) {
		String value = "";
		return value;
	}
	
	/**
	 * Find value in system XML file
	 * <p>
	 * <strong> Example: </strong>
	 * XmlMngr.getUserValueOf(new String[] {"parser_pane","path"});
	 * <p>
	 * @param path Array of strings to following value<br/>
	 * @return Return the value as a string <br/>
	 */
	public static String getSystemValueOf(String path[]) {
		String value = "";
		return value;
	}
}
