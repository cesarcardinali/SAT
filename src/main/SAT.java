package main;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.commons.io.FileUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import objects.CustomFiltersList;
import objects.CrItem;
import panes.CrsManager;
import panes.CustomFilters;
import panes.NewParserPane;
import panes.OptionsPane;

public class SAT extends JFrame{

	private static final long serialVersionUID = 2000959015370060313L;
	
	
	/**
	 * Global Variables
	 */
	private JTabbedPane tabbedPane;
	private NewParserPane Parser;
	private OptionsPane Options;
	private CrsManager crsManager;
	private ArrayList<CrItem> crsList;
	private static SAT Main;
	private File logFile;
	private BufferedWriter logWriter;
	private Semaphore unzipSemaphore;
	private static final String configLocation = "Data/cfgs/system_cfg.xml";
	private String toolFile;
	private String toolName;
	private String toolVersion;
	private String updaterFile;
	private String contentFolder;
	private String updateFolder1;
	private String updateFolder2;
	private CustomFiltersList customFiltersList;
	private CustomFilters customFiltersPane;
	PrintStream out;
	int init = 0;


	/**
	 * Create the application.
	 */
	public SAT() {
		initialize();
	}
	


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				Main = new SAT();
				//main.setVisible(true);
				Main.requestFocus();
			}
		});
	}
	

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		// Loading configurations
		loadInitialData();
		
		
		//Initializing window
		setIconImage(Toolkit.getDefaultToolkit().getImage(contentFolder + "/pics/icon.png"));
		setTitle(toolName + " " + toolVersion);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		double height = screenSize.getHeight();
		setBounds((int)(width/3), 0, (int)(width/1.5), (int)height-40);
		setMinimumSize(new Dimension(600, 600));
		setVisible(true);
		
		logFile = new File(contentFolder + "/Logs/log_" + new Timestamp(System.currentTimeMillis()).toString().replace(":", "_") + ".log");
		try {
			logWriter = new BufferedWriter(new FileWriter(logFile));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		
		//Set UI theme
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		// Initializing Variables and Panes
		unzipSemaphore = new Semaphore(1, true);
		customFiltersList = new CustomFiltersList();
		customFiltersPane = new CustomFilters(this);
		customFiltersPane.loadFilters();
		Parser = new NewParserPane(this);
		Options = new OptionsPane(this);
		crsManager = new CrsManager(this);
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if(tabbedPane.getSelectedIndex() == 1)
					crsManager.updateAllDataUI();
			}
		});
		
		
		//Inserting tabs
		tabbedPane.addTab("<html><body leftmargin=15 topmargin=3 marginwidth=15 marginheight=5>Parser</body></html>", Parser);
		tabbedPane.addTab("<html><body leftmargin=15 topmargin=3 marginwidth=15 marginheight=5>CRs and Jira</body></html>", crsManager);
		tabbedPane.addTab("<html><body leftmargin=15 topmargin=3 marginwidth=15 marginheight=5>Options</body></html>", Options);
		

		//Inserting the TabPane
		getContentPane().add(tabbedPane);
		
		
		//Initializing other variables
		crsList = new ArrayList<CrItem>();
		
		
		// Save configurations on close
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				int run = 0;
				while(run == 0){
					try {
						//Abre o arquivo XML
						File xmlFile = new File(configLocation);
						
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
								e.setText(updateFolder1);
								
							} else if(e.getName().equals("update_path2")){
								e.setText(updateFolder2);
								
							}
						}
						
						//JDOM document is ready now, lets write it to file now
				        XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
				        //output xml to console for debugging
				        //xmlOutputter.output(doc, System.out);
				        xmlOutputter.output(document, new FileOutputStream(xmlFile));
						
						crsManager.saveUserData();
						Parser.savePaneData();
						Options.savePaneData();
						
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
				try {
					logWriter.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(updating == 0)
					checkForUpdate();
			}
		});
		
		
		//Window Focus Listener
		addWindowFocusListener(new WindowFocusListener() {
			@Override
			public void windowLostFocus(WindowEvent e) {
				System.out.println("Window lost focus");
			}
			
			@Override
			public void windowGainedFocus(WindowEvent e) {
				System.out.println("Window gained focus");
				crsManager.updateAllDataUI();
			}
		});
		
		init = 1;
		
		updateThread();
	}

	
	



	/**
	 * Supportive functions
	 */
	private void loadInitialData() {
		try{
			//Abre o arquivo XML
			File xmlFile = new File(configLocation);
			
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
	
	public CrItem getCrByJira(String jiraID){
		for(CrItem aux : crsList){
			if(aux.getJiraID().equals(jiraID)){
				return aux;
			}
		}
		return null;
	}
	public CrItem getCrByB2g(String b2gID){
		for(CrItem aux : crsList){
			if(aux.getJiraID().equals(b2gID)){
				return aux;
			}
		}
		return null;
	}


	
	
	
	
	/**
	 * Getters and Setters:
	 */
	public JFrame getFrame(){
		return this;
	}
	
	public NewParserPane getParser(){
		return Parser;
	}
	
	public CrsManager getCrsManager() {
		return crsManager;
	}
	
	public OptionsPane getOptions() {
		return Options;
	}
	
	public JTabbedPane getTabbedPane(){
		return tabbedPane;
	}
	
	public ArrayList<CrItem> getCrsList() {
		return crsList;
	}
	
	public void setCrsList(ArrayList<CrItem> crsList) {
		this.crsList = crsList;
	}
	
	public Semaphore getUnzipSemaphore() {
		return unzipSemaphore;
	}
	
	public CustomFiltersList getCustomFiltersList() {
		return customFiltersList;
	}
	
	public CustomFilters getCustomFiltersPane() {
		return customFiltersPane;
	}
	
	public void setCustomFiltersList(CustomFiltersList customFiltersList) {
		this.customFiltersList = customFiltersList;
	}
	
	public void setCustomFiltersPane(CustomFilters customFiltersPane) {
		this.customFiltersPane = customFiltersPane;
	}
	
	
	public static void copyScript(File source, File dest)
			throws IOException {
		FileUtils.copyFile(source, dest);
	}
	
	int updating = 0;
	private int checkForUpdate(){
		updating = 1;
		File f1;
		File f2;
		long dateRemote, dateLocal;
		
		f1 = new File(updateFolder1 + toolFile);
		System.out.println("Remote file: " + f1.getAbsolutePath());
		System.out.println("Remote: " + f1.lastModified());
		f2 = new File(toolFile);
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
					System.out.println("Updating the Updater first, from: " + updateFolder1);
					FileUtils.copyFile(new File(updateFolder1 + "/" + updaterFile), new File(updaterFile));
				} catch (IOException e) {
					System.out.println("Updating the Updater failed");
					e.printStackTrace();
				}
				System.out.println("Updating");
				try {
					System.out.println("path: " + new File("").getAbsolutePath());
					ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "cd " + new File("").getAbsolutePath() + " && java -jar " + updaterFile);
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
	
	
	public void logWrite(String text){
		try {
			logWriter.write(text + "\n");
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Could not write to log file");
			e.printStackTrace();
		}
	}
}
