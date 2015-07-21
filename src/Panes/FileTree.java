package Panes;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.io.File;
import java.io.IOException;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
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



public class FileTree extends JPanel{
    
    private JTree fileTree; 
    private JScrollPane scrollPaneTree;
    private DefaultMutableTreeNode root; 
    private FileSystemView fileSystemView;
    private BatTracer BaseWindow;
    
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
        
        
        fileTree = new JTree(root);    
        
        //Select the first child of the root node
        fileTree.setSelectionPath(new TreePath(root.getFirstChild()));
        
        
        //Node selection listener. 
        fileTree.addTreeSelectionListener(new TreeSelectionListener (){

            //When user clicks on the folder, the files from that folder will be loaded
            public void valueChanged(TreeSelectionEvent e) {
                BaseWindow.getParser().RootNode.removeAllChildren(); //reseting the result tree            
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) fileTree.getLastSelectedPathComponent();
                
                if (node!=null){
                    File file = (File) node.getUserObject();   
                    BaseWindow.getParser().setCrPath(file.getAbsolutePath()+"\\");
                    if (file.isDirectory() && file.listFiles().length > 0){                    	
                        initializeNode(node);
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
                        BaseWindow.getParser().tree.updateUI(); //updating the result tree
                        BaseWindow.getParser().tree.clearSelection();//clearing the selections on result tree
                        BaseWindow.getParser().textPane.setText(""); //reset the text pane
                        BaseWindow.getParser().result = ""; //reset the result for the filters
                        BaseWindow.getParser().lblTitle.setText("Run a parser or select a result on the left"); //reset the text in the title
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
                	
                	               		
                    //if the it's a txt file, show the popup to select the text editor
                    if (checkFileExtension(fileTree.getSelectionPaths()).equals("txt"))
            			createAndShowTextEditorPopup(event.getX(), event.getY());
            		
            		else if (checkFileExtension(fileTree.getSelectionPaths()).equals("7z") || checkFileExtension(fileTree.getSelectionPaths()).equals("zip"))
            			//CREATE THE UNZIP FUNCTION - USE CESAR'S CODE
            			createAndShowUnzipPopup(event.getX(), event.getY());
            		
            		else
            			//SUGGEST TO DO NOTHING OR CREATE A POPUP DO DELETE FILES 
            			createAndShowOpenDeletePopup(event.getX(), event.getY());
                	             	
                }                
            }     
        });
        
        
        //Key listener
        fileTree.addKeyListener(new KeyAdapter (){
            public void keyPressed(KeyEvent event) {
                if(event.getKeyCode() == KeyEvent.VK_DELETE){
                	
                	if (fileTree.getSelectionPaths() != null){
                		
                		TreePath paths[] = fileTree.getSelectionPaths();
                		
                		for (TreePath p : paths){                	
                			DefaultMutableTreeNode node = (DefaultMutableTreeNode) p.getLastPathComponent();
                            deleteFile((File)node.getUserObject());
                            //if file is removed, node needs to be removed
                            node.removeFromParent();
                		}
                	}
  
                    
                }
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
    
    
    //Create a popup menu to choose the text editor if the file extension is txt
    public JPopupMenu createAndShowTextEditorPopup(int posX, int posY){
        
        JPopupMenu popup = new JPopupMenu();
        JMenu openWith = new JMenu ("Open with");
        JMenuItem item1 = new JMenuItem("Text Analysis Tool");
        JMenuItem item2 = new JMenuItem("Notepad++");
        JMenuItem delete = new JMenuItem("Delete");
        
        
        
        //Get the file selected and open with TextAnalysisTool
        item1.addActionListener(new ActionListener(){
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
        item2.addActionListener(new ActionListener(){
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
        
        
        
        delete.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent event) {
				// TODO Auto-generated method stub
				deleteFilesSelected();
				
			}
        	
        });
        
        //Get the icons for the executable programs
        item1.setIcon(fileSystemView.getSystemIcon(new File ("Data\\complements\\TextAnalysisTool.exe")));
        item2.setIcon(fileSystemView.getSystemIcon(new File ("C:\\Program Files (x86)\\Notepad++\\notepad++.exe")));
        delete.setIcon(new ImageIcon("Data\\pics\\rbin.jpg"));
        
        popup.add(openWith);
        openWith.add(item1);
        openWith.add(item2);
        popup.add(delete);
        
        popup.show(fileTree, posX, posY);
        
        return popup;
        
    }

    
    //Create a popup menu to chose: unzip files, unzip and run build-report, open zip files, delete zip files
    public JPopupMenu createAndShowUnzipPopup(int posX, int posY){
    	JPopupMenu popup = new JPopupMenu();
        JMenuItem item1 = new JMenuItem("Unzip");
        JMenuItem item2 = new JMenuItem("Unzip and run build-report");
        JMenuItem delete = new JMenuItem("Delete");
        JMenuItem open = new JMenuItem("Open");
                
        //Get the files selected and unzip them
        item1.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent arg0) {     
            	unZipFiles(false);
            }    
        });
        
        //Get the files selected, unzip them and run the build-report
        item2.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent arg0) {
            	unZipFiles(true);
            }    
        });
        
        delete.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent event) {
				// TODO Auto-generated method stub
				deleteFilesSelected();
				
			}
        	
        });
        
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

        //Get the icon for the unzip program - CHANGE TO GET THE RIGHT ICON	
        
        item1.setIcon(new ImageIcon("Data\\pics\\zip-512.png"));
        item2.setIcon(fileSystemView.getSystemIcon(new File ("Data\\scripts\\build_report.pl")));
        delete.setIcon(new ImageIcon("Data\\pics\\rbin.jpg"));
    	
        popup.add(open);
        popup.add(new JSeparator());
    	popup.add(item1);
        popup.add(item2);
        popup.add(new JSeparator());
        popup.add(delete);
        
        popup.show(fileTree, posX, posY);
        
        return popup;
    }
    
    
    //Create a popup menu to chose to open or delete a file/folder. If only folders were selected,
    //the popup will also have an option to run the build-report script
    public JPopupMenu createAndShowOpenDeletePopup(int posX, int posY){
    	JPopupMenu deletePopup = new JPopupMenu();
    	
    	JMenuItem delete = new JMenuItem("Delete");
    	JMenuItem open = new JMenuItem("Open");
    	
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
    	deletePopup.add(open);
    	
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
							for (TreePath p : fileTree.getSelectionPaths()){
								DefaultMutableTreeNode node = (DefaultMutableTreeNode) p.getLastPathComponent();
					    		File file = (File) node.getUserObject();
					    		try {
									BaseWindow.getCrsManager().runScript(file.getAbsolutePath());
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
    		deletePopup.add(runScript);
    	}
    	
    	deletePopup.add(delete);			
    	deletePopup.show(fileTree, posX, posY);
    	return deletePopup;
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
		// TODO Auto-generated method stub
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
		}
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
		        }
			}
		});
    	thread.start();
    }


	public JTree getFileTree() {
		return fileTree;
	}

}
