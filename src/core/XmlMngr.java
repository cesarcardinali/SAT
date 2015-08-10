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

	
	private void openUserXml(){
		try{
			//Abre o arquivo XML
			File xmlFile = new File(SharedObjs.configLocation);
			
			//Cria o builder da estrutura XML
			SAXBuilder builder = new SAXBuilder();
			
			//Cria documento formatado de acordo com a lib XML
			Document document = (Document) builder.build(xmlFile);
	}

	private void openSystemXml(){
		
	}
	
	private void loadInitialData() {
		try{
			//Abre o arquivo XML
			File xmlFile = new File(SharedObjs.configLocation);
			
			//Cria o builder da estrutura XML
			SAXBuilder builder = new SAXBuilder();
			
			//Cria documento formatado de acordo com a lib XML
			Document document = (Document) builder.build(xmlFile);
	
			//Pega o n� raiz do XML
			Element satNode = document.getRootElement();
			
			//Pega o n� referente ao option pane
			Element crs_jira_paneNode = satNode.getChild("configs");
			for(Element e : crs_jira_paneNode.getChildren()){
				if(e.getName().equals("tool_file")){
					toolFile = (e.getValue());
					
				} else if(e.getName().equals("tool_name")){
					toolName = (e.getValue());
					
				} else if(e.getName().equals("version")){
					toolVersion = (e.getValue());
					
				} else if(e.getName().equals("content_folder")){
					contentFolder = (e.getValue());
					
				} else if(e.getName().equals("updater")){
					updaterFile = (e.getValue());
					
				} else if(e.getName().equals("update_path1")){
					updateFolder1 = (e.getValue());
					
				} else if(e.getName().equals("update_path2")){
					updateFolder2 = (e.getValue());
					
				} else if(e.getName().equals("debug_mode")){
					if( e.getValue().equals("true"))
						try {
							out = new PrintStream(new FileOutputStream(contentFolder + "/Logs/system-log_" + new Timestamp(System.currentTimeMillis()).toString().replace(":", "_") + "."));
							System.setOut(out);
						} catch (FileNotFoundException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}
				}
			}
						
			System.out.println("Configs: " + configLocation);
			System.out.println("Content Folder: " + contentFolder);
			System.out.println("Tool File: " + toolFile + "\nTool Name: " + toolName + "\nTool Version: " + toolVersion + "\nUpdate File: " + updaterFile);
			System.out.println("Update path1: " + updateFolder1 + "\nUpdate path2: " + updateFolder2);
			System.out.println("Options Loaded");
		
		} catch (IOException | JDOMException e){
			e.printStackTrace();
		}
	}
}
