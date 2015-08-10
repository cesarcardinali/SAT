package filters;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import com.google.common.base.Throwables;

import core.SharedObjs;


public class Diag {
	
	
	static String result;
	private static boolean enabled = true;
	
	
	public static String makelog(String path) {
		long duration, diagDuration;
		String regex, diagAllKernel, diagMs, product, line;
		Pattern pattern;
		Matcher matcher;
		
		diagAllKernel="";
		diagMs = "";
		product="";
		duration = 0;
		diagDuration = 0;
		
		BufferedReader reader = null;
		result = "";
		
		try {
			// File seek and load configuration
			String file_report = "";
			File folder = new File(path);
			File[] listOfFiles = folder.listFiles();
			
			if(!folder.isDirectory()){
				result = "Not a directory";
				return result;
			}

			// Look for the file
			for (int i = 0; i < listOfFiles.length; i++) {
				//System.out.println(folder.listFiles()[i]);
				if (listOfFiles[i].isFile()) {
					String files = listOfFiles[i].getName();
					if (((files.endsWith(".txt")) || (files.endsWith(".TXT"))) && (files.contains("bugreport"))) {
						file_report = path + "\\" + files;
						break;
					}
				}
			}
			
			try {
				System.out.println("Log file: " + file_report);
				reader = new BufferedReader(new FileReader(file_report));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				result = "FileNotFoundException\n" + Throwables.getStackTraceAsString(e);
				return result;
			}
			

			//Find DIAG_WS data
			while ((line = reader.readLine()) != null) {
				//find product name
				if(line.contains("Build fingerprint:") && product.equals("")){
					//'motorola/quark_verizon_df/quark:5.1/LCF23.38/38:user/intcfg,release-keys'
					regex = ".*/([a-z]*)_.*";
					pattern = Pattern.compile(regex);
					matcher = pattern.matcher(line);
					
					if (!matcher.matches()){
						System.out.println("Nao encontrou produto");
					} else {
						System.out.println("Produto: " + matcher.group(1));
						product = matcher.group(1);
					}
				}
				
				//find duration
				if(line.contains("[ID=BT_DISCHARGE_SUMMARY;") && line.contains("duration=")){
					regex = ".*;duration=([0-9]+);.*";
					pattern = Pattern.compile(regex);
					matcher = pattern.matcher(line);
					
					if (!matcher.matches()){
						System.out.println("Nao encontrou duracao da CR");
					} else {
						System.out.println("Duração: " + matcher.group(1) + "ms");
						duration = Long.parseLong(matcher.group(1));
					}
				}
				
				//find "all kernel" data
				if (line.contains("All kernel wake locks")){
					line = reader.readLine();
					if(line.contains("Kernel Wake lock DIAG_WS")){
						diagAllKernel = diagAllKernel + "||" + line.replace(": ", "|").concat("|") + "\n";
					} else {
						line = reader.readLine();
						if(line.contains("Kernel Wake lock DIAG_WS")){
							diagAllKernel = diagAllKernel + "||" + line.replace(": ", "|").concat("|") + "\n";
						}
					}
				}
				
				//find kernel ms's data
				if(line.contains("DIAG_WS") && !line.contains(",") && !line.contains(";") && !line.contains(".") && !line.contains("Kernel")){
					String parts[] = line.split("\t\t|\t");
					diagDuration = Long.parseLong(parts[6]);
					diagMs = "DIAG_WS is held for more than " + (diagDuration/3600000) + " hours following max_time:\n" +
					"||name		|active_count	|event_count	|wakeup_count	|expire_count	|active_since	|total_time	|max_time	|last_change | prevent_suspend_time|" +
					"\n||" + line.replaceAll("\t\t|\t", "|") + "|\n";
				}
				
				if(line.contains("DUMP OF SERVICE entropy:")){
					break;
				}
			}
			if(diagDuration > duration*0.5){
				System.out.println("Diag!");
			} else {
				System.out.println("Not Diag! The DIAG period seems too short");
				diagMs = "";
			}
			reader.close();
			
			/*System.out.println("Produto:\t\t" + product);
			System.out.println("Duracao da CR:\t\t" + duration);
			System.out.println("Duracao do Diag:\t" + diagDuration);
			System.out.println("All Kernel:\t\t" + diagAllKernel);
			System.out.println("General mode:\t\t" + diagMs);*/
			
			//prepare the final comment:
			if(!diagAllKernel.equals("") || !diagMs.equals("")){
				result = SharedObjs.optionsPane.getTextDiag()
						.replace("#log#", diagMs + "\n" + diagAllKernel + "\n")
						.replace("\\n", "\n");
				
				//Abre o arquivo XML
				File xmlFile = new File("Data/cfgs/user_cfg.xml");
				
				//Cria o builder da estrutura XML
				SAXBuilder builder = new SAXBuilder();
				
				//Cria documento formatado de acordo com a lib XML
				Document document = (Document) builder.build(xmlFile);

				//Pega o nó raiz do XML
				Element satNode = document.getRootElement();
				
				//Pega o nó referente ao diag
				Element diagNode = satNode.getChild("diag_dup");

				result = result.replace("#dupcr#", diagNode.getChildText(product));
			} else {
				result = "No diag issue could be found in the logs";
			}
		}  catch (FileNotFoundException e) {
			result = "FileNotFoundException\n" + Throwables.getStackTraceAsString(e);
			e.printStackTrace();
			return result;
		} catch (IOException e) {
			result = "IOException\n" + Throwables.getStackTraceAsString(e);
			e.printStackTrace();
			return result;
		} catch (JDOMException e) {
			result = "JDOMException\n" + Throwables.getStackTraceAsString(e);
			e.printStackTrace();
			return result;
		}
		
		return result;
	}



	public static String getResult() {
		return result;
	}

	public static void updateResult(String editedResult) {
		result = editedResult;		
	}

	public static boolean isEnabled(){
		return enabled;
	}
	
	public static void setEnabled(boolean onoff){
		enabled = onoff;
	}
	
}
