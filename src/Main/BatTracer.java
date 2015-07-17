package Main;

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

import Objects.crItem;
import Panes.CRsManager;
import Panes.NewParserPane;
import Panes.OptionsPane;

public class BatTracer extends JFrame{

	private static final long serialVersionUID = 2000959015370060313L;
	
	
	/**
	 * Global Variables
	 */
	private JTabbedPane tabbedPane;
	private NewParserPane Parser;
	private OptionsPane Options;
	private CRsManager crsManager;
	private ArrayList<crItem> crsList;
	private static BatTracer Main;
	private String rootPath;
	private File logFile;
	private BufferedWriter logWriter;
	private Semaphore unzipSemaphore;
	
	private String updateFolder1;
	private String updateFolder2;
	PrintStream out;
	int init = 0;


	/**
	 * Create the application.
	 */
	public BatTracer() {
		initialize();
	}
	


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				Main = new BatTracer();
				//Main.setVisible(true);
				Main.requestFocus();
			}
		});
	}
	

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		//Initializing window
		setIconImage(Toolkit.getDefaultToolkit().getImage("Data\\pics\\icon.png"));
		setTitle("Search Analysis Tool  v1.0");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		double height = screenSize.getHeight();
		setBounds((int)(width/3), 0, (int)(width/1.5), (int)height-40);
		setVisible(true);
		
		try {
			out = new PrintStream(new FileOutputStream("Data\\Logs\\system-log_" + new Timestamp(System.currentTimeMillis()).toString().replace(":", "_") + ".txt"));
			System.setOut(out);
		} catch (FileNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		//Global variables
		rootPath = new File("").getAbsolutePath() + File.separator;
		System.out.println(rootPath);
		
		logFile = new File("Data\\Logs\\log_" + new Timestamp(System.currentTimeMillis()).toString().replace(":", "_") + ".txt");
		try {
			logWriter = new BufferedWriter(new FileWriter(logFile));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		unzipSemaphore = new Semaphore(1, true);


		//Set UI theme
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		
		// Initializing TabPane and Tabs
		Parser = new NewParserPane(this);
		Options = new OptionsPane(this);
		crsManager = new CRsManager(this);
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
		
		
		// Loading configurations
		loadInitialData();
		
		
		//Initializing other variables
		crsList = new ArrayList<crItem>();
		
		
		// Save configurations on close
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				int run = 0;
				while(run == 0){
					try {
						//Abre o arquivo XML
						File xmlFile = new File("Data/cfgs/system_cfg.xml");
						
						//Cria o builder da estrutura XML
						SAXBuilder builder = new SAXBuilder();
						
						//Cria documento formatado de acordo com a lib XML
						Document document = (Document) builder.build(xmlFile);
						
						//Pega o nó raiz do XML
						Element satNode = document.getRootElement();
						
						//Gera lista de filhos do nó root
						//List<Element> satElements = satNode.getChildren();
						
						//Pega o nó referente ao option pane
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
			File xmlFile = new File("Data/cfgs/system_cfg.xml");
			
			//Cria o builder da estrutura XML
			SAXBuilder builder = new SAXBuilder();
			
			//Cria documento formatado de acordo com a lib XML
			Document document = (Document) builder.build(xmlFile);
	
			//Pega o nó raiz do XML
			Element satNode = document.getRootElement();
			
			//Pega o nó referente ao option pane
			Element crs_jira_paneNode = satNode.getChild("configs");
			for(Element e : crs_jira_paneNode.getChildren()){
				if(e.getName().equals("update_path1")){
					updateFolder1 = (e.getValue());
					
				} else if(e.getName().equals("update_path2")){
					updateFolder2 = (e.getValue());
					
				}
			}
			
			System.out.println("Update path1: " + updateFolder1 + "\nUpdate path2: " + updateFolder2);
			System.out.println("Options Loaded");
		
		} catch (IOException | JDOMException e){
			e.printStackTrace();
		}
	}
	
	public crItem getCrByJira(String jiraID){
		for(crItem aux : crsList){
			if(aux.getJiraID().equals(jiraID)){
				return aux;
			}
		}
		return null;
	}
	public crItem getCrByB2g(String b2gID){
		for(crItem aux : crsList){
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
	
	public CRsManager getCrsManager() {
		return crsManager;
	}
	
	public OptionsPane getOptions() {
		return Options;
	}
	
	public JTabbedPane getTabbedPane(){
		return tabbedPane;
	}
	public ArrayList<crItem> getCrsList() {
		return crsList;
	}
	public void setCrsList(ArrayList<crItem> crsList) {
		this.crsList = crsList;
	}
	public Semaphore getUnzipSemaphore() {
		return unzipSemaphore;
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
		
		f1 = new File(updateFolder1 + "\\BatteryTool.jar");
		System.out.println("Remote file: " + f1.getAbsolutePath());
		System.out.println("Remote: " + f1.lastModified());
		f2 = new File("BatteryTool.jar");
		System.out.println("Local file: " + f2.getAbsolutePath());
		System.out.println("Local: " + f2.lastModified());
		dateRemote = f1.lastModified();
		dateLocal = f2.lastModified();
		
		if(dateLocal < dateRemote && dateLocal != 0){
			Object[] options = new Object[]{ "Yes", "No" };
			int n = JOptionPane.showOptionDialog(null,
					"Uma nova versão foi encontrada. Voce desaja atualizar agora?",
					"New version available", JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, options,
					options[1]);
			if(n == 0) {
				try {
					System.out.println("Updating the Updater first, from: " + updateFolder1);
					FileUtils.copyFile(new File(updateFolder1 + "\\Updater.jar"), new File("Updater.jar"));
				} catch (IOException e) {
					System.out.println("Updating the Updater failed");
					e.printStackTrace();
				}
				System.out.println("Updating");
				try {
					System.out.println("path: " + new File("").getAbsolutePath());
					ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "cd " + new File("").getAbsolutePath() + " && java -jar Updater.jar");
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
