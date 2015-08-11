package main;


import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import core.Icons;
import core.Logger;
import core.SharedObjs;
import core.XmlMngr;


public class SAT extends JFrame{

	private static final long serialVersionUID = 2000959015370060313L;
	//static SAT satFrame;
	
	/**
	 * Runnable
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				// Initialize all static classes
				XmlMngr.initClass();
				SharedObjs.initClass();
				Logger.initClass();
				
				// Start UI
				SharedObjs.satFrame.setVisible(true);
			}
		});
	}
	
	/**
	 * Global Variables
	 */
	PrintStream out;
	int updating = 0;


	/**
	 * Create the application.
	 */
	public SAT() {
		initialize();
	}
	

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		//Initializing window
		setIconImage(Icons.iconSat);
		setTitle(SharedObjs.toolName + " " + SharedObjs.toolVersion);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		double height = screenSize.getHeight();
		setBounds((int)(width/3), 0, (int)(width/1.5), (int)height-40);
		setMinimumSize(new Dimension(800, 600));
		setVisible(true);
		
		//Inserting the TabPane
		getContentPane().add(SharedObjs.tabbedPane);
		
		//Window Focus Listener
		addWindowFocusListener(new WindowFocusListener() {
			@Override
			public void windowLostFocus(WindowEvent e) {
				System.out.println("Window lost focus");
			}
			
			@Override
			public void windowGainedFocus(WindowEvent e) {
				System.out.println("Window gained focus");
				SharedObjs.crsManagerPane.updateAllDataUI();
			}
		});
		
		// Save configurations on close
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				int run = 0;
				while(run == 0){
					try {
						//Abre o arquivo XML
						File xmlFile = SharedObjs.sytemCfgFile;
						
						//Cria o builder da estrutura XML
						SAXBuilder builder = new SAXBuilder();
						
						//Cria documento formatado de acordo com a lib XML
						Document document = (Document) builder.build(xmlFile);
						
						//Pega o n� raiz do XML
						Element satNode = document.getRootElement();
						
						//Gera lista de filhos do n� root
						//List<Element> satElements = satNode.getChildren();
						
						//Pega o n� referente ao option pane
						Element optionPaneNode = satNode.getChild("configs"); 
						for(Element e : optionPaneNode.getChildren()){
							if(e.getName().equals("update_path1")){
								e.setText(SharedObjs.updateFolder1);
								
							} else if(e.getName().equals("update_path2")){
								e.setText(SharedObjs.updateFolder2);
								
							}
						}
						
						//JDOM document is ready now, lets write it to file now
				        XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
				        //output xml to console for debugging
				        //xmlOutputter.output(doc, System.out);
				        xmlOutputter.output(document, new FileOutputStream(xmlFile));
						
						SharedObjs.crsManagerPane.saveUserData();
						SharedObjs.parserPane.savePaneData();
						SharedObjs.optionsPane.savePaneData();
						
						System.out.println("All data saved");
						
						run = 1;
					} catch (JDOMException | IOException e1) {
						e1.printStackTrace();
						System.out.println("finished3");
						run = 1;
					} finally{
						System.out.println("finished1");
						run = 1;
					}
				}
				
				XmlMngr.closeXmls();
				Logger.close();
				
				if(SharedObjs.satFrame.getUpdating() == 0)
					SharedObjs.satFrame.checkForUpdate();
			}
		});
		
		// Start updater thread
		updateThread();
	}
	
	
	


	
	
	
	
	/**
	 * Getters and Setters:
	 */
	public int getUpdating(){
		return updating;
	}
	
	public void setUpdating(int num){
		updating = num;
	}
	
	/**
	 * 
	 * @return
	 */
	public int checkForUpdate(){
		updating = 1;
		File f1;
		File f2;
		long dateRemote, dateLocal;
		
		f1 = new File(SharedObjs.updateFolder1 + SharedObjs.toolFile);
		System.out.println("Remote file: " + f1.getAbsolutePath());
		System.out.println("Remote: " + f1.lastModified());
		f2 = new File(SharedObjs.toolFile);
		System.out.println("Local file: " + f2.getAbsolutePath());
		System.out.println("Local: " + f2.lastModified());
		dateRemote = f1.lastModified();
		dateLocal = f2.lastModified();
		
		if(dateLocal < dateRemote && dateLocal != 0){
			Object[] options = new Object[]{ "Yes", "No" };
			int n = JOptionPane.showOptionDialog(null,
					"Uma nova vers�o foi encontrada. Voce desaja atualizar agora?",
					"New version available", JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, options,
					options[1]);
			if(n == 0) {
				try {
					System.out.println("Updating the Updater first, from: " + SharedObjs.updateFolder1);
					FileUtils.copyFile(new File(SharedObjs.updateFolder1 + "/" + SharedObjs.updaterFile), new File(SharedObjs.updaterFile));
				} catch (IOException e) {
					System.out.println("Updating the Updater failed");
					e.printStackTrace();
				}
				System.out.println("Updating");
				try {
					System.out.println("path: " + new File("").getAbsolutePath());
					ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "cd " + new File("").getAbsolutePath() + " && java -jar " + SharedObjs.updaterFile);
					builder.start();
				} catch (IOException e2) {
					e2.printStackTrace();
				}
				return 1;
			} else {
				return 2;
			}
		}
		return 0;
	}
	
	/**
	 * 
	 */
	private void updateThread(){
		new Thread(new Runnable() {
			int stop = 0;
			
			@Override
			public void run() {
				stop = checkForUpdate();
				while(stop == 0){
					try {
						Thread.sleep(900000);
						//Thread.sleep(3000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					stop = checkForUpdate();
				}
				if(stop == 1){
					System.out.println("\n\nExiting");
					System.exit(0);
				}
			}
		}).start();
	}
	
}
