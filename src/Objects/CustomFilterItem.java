package Objects;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Main.BatTracer;

public class CustomFilterItem {
	
	//Variables
	private String name;
	private String regex;
	private String header;
	private String result;
	private String whereToSearch; //r=Radio; m=Main; s=System; k=Kernel; b=BugReport
	
	private BatTracer BaseWindow;
	
	
	public CustomFilterItem(BatTracer parent){
		BaseWindow = parent;
		name = "";
		regex = "";
		header = "";
		result = "";
		whereToSearch = "mskrb";
	}
	
	public CustomFilterItem(BatTracer parent, String name, String regex, String header, String wheretosearch){
		BaseWindow = parent;
		this.name = name;
		this.regex = regex;
		this.header = header;
		result = "";
		this.whereToSearch = wheretosearch;
	}
	
	
	
	public void runFilter(String path){
		//File reader
		BufferedReader reader = null;
		
		//Regex config
		Matcher matcherRegex = null;
		//Pattern patternRegex = Pattern.compile(regex);
		
		//File path
		String file = "";
		
		if(whereToSearch.contains("m")){
			// Find log file
			File folder = new File(path);
			File[] listOfFiles = folder.listFiles();
			for (int i = 0; i < listOfFiles.length; i++)
			{
				if ( listOfFiles[i].getName().endsWith(".txt") && listOfFiles[i].getName().toLowerCase().contains("main") )
				{
					file = path + listOfFiles[i].getName();
				}
			}
			System.out.println("file: " + file);
			System.out.println("path: " + path);

			if (file.equals(""))
			{
				System.out.println("Log de sistema nao encontrado");
				result = "Log de sistema nao encontrado\n";
			}
			else
			{
				try {
					reader = new BufferedReader(new FileReader(file));
					System.out.println("Log de sistema encontrado!" + file);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		} else if(whereToSearch.contains("s")){
			
		} else if(whereToSearch.contains("k")){
			
		} else if(whereToSearch.contains("r")){
			
		} else if(whereToSearch.contains("b")){
			
		}
	}
	
	
	//Getters and Setters
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRegex() {
		return regex;
	}
	public void setRegex(String regex) {
		this.regex = regex;
	}
	public String getHeader() {
		return header;
	}
	public void setHeader(String header) {
		this.header = header;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	
}
