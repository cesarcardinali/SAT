package Filters;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import Main.BatTracer;
import Objects.WackLock_List;
import Objects.wakelockItem;

public class Suspicious {
	
	static String result;
	static WackLock_List suspiciousWakelocks; 
	
	public static void main(String[] args) {
		PrintWriter out;
		try {
			out = new PrintWriter("_Suspicious.txt");
			String result = ("");
			StringSelection stringSelection = new StringSelection(result);
			Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
			clpbrd.setContents(stringSelection, null);
			out.print(result);
			out.close();
			System.out.println(result);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static String makelog(String path, BatTracer BaseWindow) {
		
// ---- Variaveis necess�rias -----
		BufferedReader br = null;
		result = "";
		
		
		try { // M�todo de valida��o inicial para funcionalidade da classe.

// -------- Inicializa��o das vari�veis -------- 
			String file_report = null;
			String sCurrentLine;
			
			suspiciousWakelocks = new WackLock_List();

			File folder = new File(path);
			File[] listOfFiles = folder.listFiles();
			
// -------- Listagem dos arquivos contidos na pasta -------- 
			for (int i = 0; i < listOfFiles.length; i++) {
				if (listOfFiles[i].isFile()) {
					String files = listOfFiles[i].getName();
					if (((files.endsWith(".txt")) || (files.endsWith(".TXT")))
							&& (files.contains("system"))) {
						if(path.equals("."))
							file_report = files;
						else
							file_report = path + files;
						break;
					}
				}
			}

// -------- Verifica se o arquivo necess�rio existe --------  
			if (file_report != null){
				br = new BufferedReader(new FileReader(file_report));
			}
			else {
				System.out.println("Log de sistema nao encontrado");
				return "Log de sistema nao encontrado";
			}

// -------- Busca por wake locks --------
			while ((sCurrentLine = br.readLine()) != null) {
				
				Date parsedDate = null;
				
				if (sCurrentLine.contains("PowerManagerService: Suspicious wakelock held")) 
				{
					String tag = sCurrentLine.substring(sCurrentLine.indexOf("tag=")+4, sCurrentLine.indexOf(",",sCurrentLine.indexOf("tag=")));
					String lock = sCurrentLine.substring(sCurrentLine.indexOf("lock=")+5, sCurrentLine.indexOf(",",sCurrentLine.indexOf("lock=")));
					String ws;
					if(sCurrentLine.contains("ws=null"))
						ws = sCurrentLine.substring(sCurrentLine.indexOf("ws=")+3, sCurrentLine.indexOf(",",sCurrentLine.indexOf("ws=")));
					else
						ws = sCurrentLine.substring(sCurrentLine.indexOf("ws=")+3, sCurrentLine.indexOf("}",sCurrentLine.indexOf("ws="))+1);
					//System.out.println("WS != null: " + ws);
					
					String uid = sCurrentLine.substring(sCurrentLine.indexOf("uid=")+4, sCurrentLine.indexOf(",",sCurrentLine.indexOf("uid=")));
					String process = "";
					
				// ----- Formatacao para data encontrada -----
					try
					{
					    SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd hh:mm:ss");
					    parsedDate = dateFormat.parse((String) sCurrentLine.subSequence(0, 13));
					}
					catch(Exception e)
					{
						System.out.println("Error: " + e.toString());
					}
					
				// ----- Busca pelo id do processo que provoca o wake lock -----
					if(!ws.equals("null")){
						ws = ws.substring(ws.indexOf("{")+1, ws.indexOf("}"));
						process = "userId=\"" + ws + "\"";
						uid = ws;
					} else {
						process = "userId=\"" + uid + "\"";
					}
					System.out.println("uid: " + uid);
				
					
				// ----- Criacao do objeto de wake lock a ser inserido na lista -----
					wakelockItem wl = new wakelockItem(uid, tag, lock, parsedDate, sCurrentLine);
				// ----- Verifica��o se o wakelock j� existe na lista -----
					int index = suspiciousWakelocks.wlIndexOf_v2(wl);
					
					
				// ----- Caso nao exista, o wake lock e adiciona a lista -----
					if(index == -1)
					{
						wl.quantityInc();
					// ----- Busca o nome do processo no bugreport -----
						if(uid.length() == 5)
						{
							for (int i = 0; i < listOfFiles.length; i++) {
								if (listOfFiles[i].isFile()) {
									String files = listOfFiles[i].getName();
									if (((files.endsWith(".txt")) || (files.endsWith(".TXT")))
											&& (files.contains("bugreport"))) {
										if(path.equals("."))
											file_report = files;
										else
											file_report = path + files;
										break;
									}
								}
							}
							if(file_report.contains("bugreport")){
								System.out.println("File opened: " + file_report);
								BufferedReader aux = new BufferedReader(new FileReader(file_report));
								aux.skip(1850000);
								String str;
								boolean found = false;
								while ( ( ((str = aux.readLine()) != null) && found == false) && str != null) {
									if( (str.contains("uid=" + uid + " ")/* || str.contains("uid=" + ws + " ")*/) && str.contains("packageName=")){
										process = str.substring(str.indexOf("packageName=")+12, str.indexOf(" ", str.indexOf("packageName=")+12));
										found = true;
									}
								}
								aux.close();
							} else {
								for (int i = 0; i < listOfFiles.length; i++) {
									if (listOfFiles[i].isFile()) {
										String files = listOfFiles[i].getName();
										if (((files.endsWith(".btd")) || (files.endsWith(".BTD")))
												&& (files.contains("BT9"))) {
											if(path.equals("."))
												file_report = files;
											else
												file_report = path + files;
											break;
										}
									}
								}
								BufferedReader aux = new BufferedReader(new FileReader(file_report));
								aux.skip(10000);
								String str;
								boolean found = false;
								//process = (String) process.substring(8, process.length()-1);
								while ( ( ((str = aux.readLine()) != null) && found == false)) {
									if(str.equals(""))
										str = aux.readLine();
									else if(str.matches("(.*):" + uid + ":(.*)")){
										int start = str.indexOf(":" + uid + ":")-1;
										while (str.charAt(start) != '|')
											start--;
										process = str.substring(start+1, str.indexOf(":" + uid + ":"));
										found = true;
									}
								}
								aux.close();
							}
						}
					// ----- Busca pelo nome do processo no BTD -----
						else 
						{
							for (int i = 0; i < listOfFiles.length; i++) {
								if (listOfFiles[i].isFile()) {
									String files = listOfFiles[i].getName();
									if (((files.endsWith(".btd")) || (files.endsWith(".BTD")))
											&& (files.contains("BT9"))) {
										if(path.equals("."))
											file_report = files;
										else
											file_report = path + files;
										break;
									}
								}
							}
							BufferedReader aux = new BufferedReader(new FileReader(file_report));
							aux.skip(10000);
							String str;
							boolean found = false;
							//process = (String) process.substring(8, process.length()-1);
							while ( ( ((str = aux.readLine()) != null) && found == false)) {
								if(str.equals(""))
									str = aux.readLine();
								else if(str.matches("(.*):" + uid + ":(.*)")){
									int start = str.indexOf(":" + uid + ":")-1;
									while (str.charAt(start) != '|')
										start--;
									process = str.substring(start+1, str.indexOf(":" + uid + ":"));
									found = true;
								}
							}
							aux.close();
						}
						wl.setProcess(process);
						// ----- Adiciona o wake lock completo na lista -----
						suspiciousWakelocks.add(wl);
					}
					// ----- Caso ja exista o wake lock, ele e atualizado na lista -----
					else
					{
					// Atualiza o wakelock ja existente
						wl = (wakelockItem) suspiciousWakelocks.get(index);
						wl.quantityInc();
						wl.setEnd(parsedDate);
						wl.addLogLine("\n" + sCurrentLine);
					// Atualiza a lista
						suspiciousWakelocks.set(index, wl);
					}
				}
			}
			
			try {
				br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			
			// ----- Geracao de resultados -----
			
			result = result + Issue.makelog(path, BaseWindow) + "\n\n";
			if(suspiciousWakelocks.size() > 0)
			{
				result = result + BaseWindow.getOptions().getTextSuspiciousHeader() + "\n";
				for(int i=0; i<suspiciousWakelocks.size();i++){
					result = result + "{panel}\n"
							+ BaseWindow.getOptions().getTextSuspicious()
							.replace("#pname#", suspiciousWakelocks.get(i).getProcess())
							.replace("#tag#", suspiciousWakelocks.get(i).getTag())
							.replace("#duration#", suspiciousWakelocks.get(i).getDuration())
							.replace("#log#", suspiciousWakelocks.get(i).getLog())
							.replace("\\n", "\n")
							+ "\n{panel}\n";
				}
			}
			else{
				result = "- No detailed wake locks evidences were found in text logs";
			}

		} catch (IOException e) {
			e.printStackTrace();
			try {
				br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		} finally {
			try {
				if(br!=null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		return result;
	}
	
	
	
	static public WackLock_List getWakeLocks(){
		return suspiciousWakelocks;
	}
	
	static public String getResult(){
		return result;
	}

	public static void updateResult(String editedResult) {
		result = editedResult;		
	}

}
