package Panes;

import java.awt.Desktop;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import Main.BatTracer;
import Supportive.UnZip;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;



@SuppressWarnings("serial")
public class FileTree extends JPanel{
    
    private JTree fileTree; 
    private JScrollPane scrollPaneTree;
    private DefaultMutableTreeNode root; 
    private FileSystemView fileSystemView;
    private BatTracer BaseWindow;
    private String lastDirectory;
    
    //File Tree constructor. It will initialize the file tree
    public FileTree(BatTracer batTracer){
    	
    	BaseWindow = batTracer;
        
        setPreferredSize(new Dimension(1000, 800));
        setLocation(350, 200);
        
        root = new DefaultMutableTreeNode();
        fileSystemView = FileSystemView.getFileSystemView();
        
        //initialize the file tree based on the system roots 
        for (File file : fileSystemView.getRoots()){
            DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(file);
            if (file.isDirectory()){
                for (File f : file.listFiles()){
                    DefaultMutableTreeNode node = new DefaultMutableTreeNode (f); 
                    newNode.add(node);
                }
            }
            root.add(newNode);
        }
        
        lastDirectory = "";
        
        fileTree = new JTree(root);    
        
        //Select the first child of the root node
        fileTree.setSelectionPath(new TreePath(root.getFirstChild()));
        
        
        //Node selection listener. 
        fileTree.addTreeSelectionListener(new TreeSelectionListener (){

            //When user clicks on the folder, the files from that folder will be loaded
            public void valueChanged(TreeSelectionEvent e) {
                //BaseWindow.getParser().rootNode.removeAllChildren(); //reseting the result tree            
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) fileTree.getLastSelectedPathComponent();
                System.out.println("-------------" + node.toString());
                if (node!=null){
                    File file = (File) node.getUserObject();
                    
                    if (file.isDirectory()){
                    	if(file.listFiles().length > 0)
                        	initializeNode(node);
                    	if(isCrNode(node)){
                    		if(!lastDirectory.equals(node.toString())) {
                    		lastDirectory = node.toString();
                    		BaseWindow.getParser().setCrPath(file.getAbsolutePath()+"\\");
	                        BaseWindow.getParser().getFiltersResultsTree().clearTree();
	                        BaseWindow.getParser().textPane.setText(""); //reset the text pane
	                        BaseWindow.getParser().result = ""; //reset the result for the filters
	                        BaseWindow.getParser().lblTitle.setText("Run a parser or select a result on the left"); //reset the text in the title
                    		}
	                    }
                	}
                }
                
                //Calling updateUI causes the tree's UI to be uninstalled and then a new one installed. The developer 
                //is calling this method in the middle of events being dispatched, some of which go to the UI. Because 
                //the events are already being dispatched, they will still go to the uninstalled UI which no longer has 
                //reference to the tree. If the developer really wants this behavior, they can wrap the call to updateUI 
                //in a SwingUtilities.invokeLater(). This method allows us to post a "job" to Swing, which it will then 
                //run on the event dispatch thread at its next convenience.
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                      // Here, we can safely update the GUI
                      // because we'll be called from the
                      // event dispatch thread
                        fileTree.updateUI();
                    }
                });
            }
        });
        
        
        fileTree.addTreeWillExpandListener(new TreeWillExpandListener(){

			@Override
			public void treeWillCollapse(TreeExpansionEvent arg0)
					throws ExpandVetoException {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void treeWillExpand(TreeExpansionEvent arg0)
					throws ExpandVetoException {
				// TODO Auto-generated method stub
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) fileTree.getLastSelectedPathComponent();
				initializeNode(node);
				SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                      // Here, we can safely update the GUI
                      // because we'll be called from the
                      // event dispatch thread
                        fileTree.updateUI();
                    }
                });
				
			}
        	
        });
        
        
        //Mouse listener
        fileTree.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent event){
                //if left click, double click opens the file with the default system program
                if (event.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(event)){
                    
                	DefaultMutableTreeNode nodeSelected = (DefaultMutableTreeNode) fileTree.getLastSelectedPathComponent();
                    openFile((File)nodeSelected.getUserObject());
                    
                }
                            
              //if right click was detected, get the node where the right click happened
                if (SwingUtilities.isRightMouseButton(event) && 
                        fileTree.getPathForLocation(event.getX(), event.getY()) != null){
                	
                	if (fileTree.getSelectionPaths() == null || fileTree.getSelectionPaths().length == 1){	           
                		fileTree.setSelectionPath(fileTree.getPathForLocation(event.getX(), event.getY()));     
	                    //DefaultMutableTreeNode node = (DefaultMutableTreeNode) fileTree.getLastSelectedPathComponent();
	                    //File file = (File) node.getUserObject();	                    
                	}               	               	
                	getPopup (event.getX(), event.getY());             	
                }                
            }     
        });
        
        
        //Key listener
        fileTree.addKeyListener(new KeyAdapter (){
            public void keyPressed(KeyEvent event) {
                if(event.getKeyCode() == KeyEvent.VK_DELETE){              	
                	deleteFilesSelected();  
                }
            }
        });

        setLayout(new GridLayout(0, 1, 0, 0));
        
        fileTree.setCellRenderer(new FileTreeNodeRenderer());
        fileTree.expandRow(1);
        fileTree.setRootVisible(false);
        fileTree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
        
        scrollPaneTree = new JScrollPane(fileTree); 
        scrollPaneTree.setPreferredSize(new Dimension(1080, 780));
        add(scrollPaneTree);
    }

    
    //Initialize the node with with files from the folder
    public void initializeNode(DefaultMutableTreeNode node){    
        if (node!=null){
            File file = (File) node.getUserObject();
            node.removeAllChildren();
            for (File f : file.listFiles()){
                DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(f);
                node.add(newNode);
            }
        }
    }
        
    
    //Open file with the default system program
    public void openFile(File file){
        try {
        	if (file.isFile())
        		Desktop.getDesktop().open(file);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    
    //Creates the popup menu
    public JPopupMenu getPopup (int posX, int posY){
    	
    	JPopupMenu popup = new JPopupMenu();
    	JMenuItem open = new JMenuItem("Open");
        JMenuItem delete = new JMenuItem("Delete");

        open.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent event) {
				// TODO Auto-generated method stub
				TreePath paths[] = fileTree.getSelectionPaths();
				
				for (TreePath p : paths){                	
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) p.getLastPathComponent();
					try {
						Desktop.getDesktop().open((File)node.getUserObject());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}		            
				}
			}
    		
    	});
    	
    	
    	delete.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent event) {
				// TODO Auto-generated method stub
				deleteFilesSelected();
        		
			}
    		
    	});
    	
    	delete.setIcon(new ImageIcon("Data\\pics\\rbin.jpg"));
    	popup.add(open);
    	
    	if (isSelectedNodesFolder(fileTree.getSelectionPaths())){
    		JMenuItem runScript = new JMenuItem("Run build-report");
    		
    		runScript.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent event) {
					// TODO Auto-generated method stub
					
					Thread thread = new Thread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							int count = 0;
							for (TreePath p : fileTree.getSelectionPaths()){
								ProgressDialog dialog = new ProgressDialog(BaseWindow.getFrame(), fileTree.getSelectionPaths().length);
								DefaultMutableTreeNode node = (DefaultMutableTreeNode) p.getLastPathComponent();
					    		File file = (File) node.getUserObject();
					    		try {
									BaseWindow.getCrsManager().runScript(file.getAbsolutePath());
									dialog.updateDialogView(++count);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();			
								}
							}
						}
					});	
					
					thread.start();
				}
    			
    		});
    		
    		runScript.setIcon(fileSystemView.getSystemIcon(new File ("Data\\scripts\\build_report.pl")));
    		popup.add(runScript);
    	}
    	

    	//if the it's a txt file, show the popup to select the text editor
        if (checkFileExtension(fileTree.getSelectionPaths()).equals("txt")){
        	
        	JMenu openWith = new JMenu ("Open with");
            JMenuItem textTool = new JMenuItem("Text Analysis Tool");
            JMenuItem notepad = new JMenuItem("Notepad++");
            
            //Get the file selected and open with TextAnalysisTool
            textTool.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent arg0) {
                    
                    File file;
                    DefaultMutableTreeNode node;
                    TreePath[] paths = fileTree.getSelectionPaths();
            		
                	for (int i = 0; i < paths.length; i++){   		
                	    node = (DefaultMutableTreeNode) paths[i].getLastPathComponent();
                		file = (File) node.getUserObject();
                		try {
                            Runtime.getRuntime().exec(new String[] {"Data\\complements\\TextAnalysisTool.exe ", file.getAbsolutePath()});
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } 	
                	}     
                }    
            });
            
            //Get the file selected and open with Notepad++
            notepad.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent arg0) {
                    
                	File file;
                    DefaultMutableTreeNode node;
                    TreePath[] paths = fileTree.getSelectionPaths();
            		
                	for (int i = 0; i < paths.length; i++){   		
                	    node = (DefaultMutableTreeNode) paths[i].getLastPathComponent();
                		file = (File) node.getUserObject();
                		try {
                            Runtime.getRuntime().exec(new String[] {"C:\\Program Files (x86)\\Notepad++\\notepad++.exe ", file.getAbsolutePath()});
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } 	
                	}      
                }    
            });
            
            //Get the icons for the executable programs
            textTool.setIcon(fileSystemView.getSystemIcon(new File ("Data\\complements\\TextAnalysisTool.exe")));
            notepad.setIcon(fileSystemView.getSystemIcon(new File ("C:\\Program Files (x86)\\Notepad++\\notepad++.exe")));
            
            popup.add(openWith);
            openWith.add(textTool);
            openWith.add(notepad);

        }

		else if (checkFileExtension(fileTree.getSelectionPaths()).equals("7z") || checkFileExtension(fileTree.getSelectionPaths()).equals("zip")){
			JMenuItem unzip = new JMenuItem("Unzip");
	        JMenuItem unzipRun = new JMenuItem("Unzip and run build-report");
	        
	        //Get the files selected and unzip them
	        unzip.addActionListener(new ActionListener(){
	            public void actionPerformed(ActionEvent arg0) {     
	            	unZipFiles(false);
	            }    
	        });
	        
	        //Get the files selected, unzip them and run the build-report
	        unzipRun.addActionListener(new ActionListener(){
	            public void actionPerformed(ActionEvent arg0) {
	            	unZipFiles(true);
	            }    
	        });
	        
	        unzip.setIcon(new ImageIcon("Data\\pics\\zip-512.png"));
	        unzipRun.setIcon(fileSystemView.getSystemIcon(new File ("Data\\scripts\\build_report.pl")));
	        
	        popup.add(unzip);
	        popup.add(unzipRun);
		}
        
        popup.add(delete);
			               
        popup.show(fileTree, posX, posY);
        
        return popup;
        
    }
    
  
    //Delete the file  
    public void deleteFile(File file){
        
        if (file.isDirectory())
            FileUtils.deleteQuietly(file);
        else
            file.delete();
    }
     
    
    //Delete the files selected 
  	public void deleteFilesSelected() {
  		
  		new Thread(new Runnable() {
  			
  			@Override
  			public void run() {

  				try {
  					TreePath paths[] = fileTree.getSelectionPaths();
  					
  					for (TreePath p : paths){                	
  						DefaultMutableTreeNode node = (DefaultMutableTreeNode) p.getLastPathComponent();
  			            deleteFile((File)node.getUserObject());
  			            //if file is removed, node needs to be removed
  			            node.removeFromParent();
  			            
  			            SwingUtilities.invokeLater(new Runnable() {
  			                public void run() {
  			                  // Here, we can safely update the GUI
  			                  // because we'll be called from the
  			                  // event dispatch thread
  			                    fileTree.updateUI();
  			                }
  			            });
  					} // end for
  				} catch(Exception e){
  					
  					e.printStackTrace();
  					System.out.println("Error while deleting file(s)");
  					
  				}
  				
  			} // end run
  		}).start();
  		
  	}
    		
    		
    //check the extension of the files selected
    public String checkFileExtension(TreePath paths[]){
    	
    	DefaultMutableTreeNode node = (DefaultMutableTreeNode) paths[0].getLastPathComponent();
		File file = (File) node.getUserObject();
		if (file.isDirectory())
			return "Selection contains at least one folder";
		String fileExtension = FilenameUtils.getExtension(file.getAbsolutePath());
		
    	for (int i = 1; i < paths.length; i++){   		
    	    node = (DefaultMutableTreeNode) paths[i].getLastPathComponent();
    		file = (File) node.getUserObject();
    		if (!FilenameUtils.getExtension(file.getAbsolutePath()).equals(fileExtension))
    			return "Different file extensions";
    	}
    	
    	return fileExtension;
    }
       
    
    //Check if the nodes currently selected are folders
    public boolean isSelectedNodesFolder(TreePath paths[]){
    	
    	DefaultMutableTreeNode node;
		File file;
    	
    	for (TreePath p : fileTree.getSelectionPaths()){
    		node = (DefaultMutableTreeNode) p.getLastPathComponent();
    		file = (File) node.getUserObject();
    		if (!file.isDirectory())
    			return false;
    	}
    	
    	return true;

    }
        
    
    //Unzip the files and if necessary runs the build report script
    public void unZipFiles(boolean runScript){
    	
    	final boolean run = runScript;
 	
    	Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				File file;
		    	File newFile;
		        DefaultMutableTreeNode node;
		        TreePath[] paths = fileTree.getSelectionPaths();
		        ProgressDialog dialog = new ProgressDialog(BaseWindow.getFrame(), paths.length);
		        int length = 0;
		        
		        for (TreePath p : paths){
		        	node = (DefaultMutableTreeNode) p.getLastPathComponent();
		    		file = (File) node.getUserObject();
		    		node = (DefaultMutableTreeNode) node.getParent();
		    		UnZip.unZipIt(file.getAbsolutePath(), file.getAbsolutePath().substring(0, file.getAbsolutePath().length()-28));
		    		newFile = new File(file.getAbsolutePath().substring(0, file.getAbsolutePath().length()-28));
		    		System.out.println(newFile);
		    		if (newFile != null) {
		    			node.add(new DefaultMutableTreeNode(newFile));
		    			if (run){
		    				try {
								BaseWindow.getCrsManager().runScript(newFile.getAbsolutePath());
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
		    			}	
		    		}
		    		fileTree.updateUI();
		    		dialog.updateDialogView(++length);		    		
		        }
			}
		});
    	thread.start();
    }


	public JTree getFileTree() {
		return fileTree;
	}
	
	public boolean isCrNode(DefaultMutableTreeNode node){
		DefaultMutableTreeNode Node = node;
		File aux = (File)Node.getUserObject();
		int Length;
		
		if(aux.isDirectory()) {
			Length = Node.getChildCount();
			for(int i = 0; i < Length; i++)
			{
				if(Node.getChildAt(i).toString().toLowerCase().contains("report_info") 
						|| Node.getChildAt(i).toString().toLowerCase().contains(".btd")
						|| Node.getChildAt(i).toString().toLowerCase().contains("entry.txt")){
					return true;
				}
			}
			return false;
		} else {
			Node = (DefaultMutableTreeNode) Node.getParent();
			Length = Node.getChildCount();
			for(int i = 0; i < Length; i++)
			{
				if(Node.getChildAt(i).toString().toLowerCase().contains("report_info") 
						|| Node.getChildAt(i).toString().toLowerCase().contains(".btd")
						|| Node.getChildAt(i).toString().toLowerCase().contains("entry.txt")){
					return true;
				}
			}
			return false;
		}
	}

}
