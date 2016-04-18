import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javax.swing.JMenuBar;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SpringLayout;

import com.sun.xml.internal.ws.api.Component;
import com.sun.xml.internal.ws.api.server.Container;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;

public class MainWin{
	
	protected float factx;
	protected float facty;
	protected int width;
	protected int heigth;
	protected JPanel contentPane;
	protected JPopupMenu popupMenu;
	protected Ws_creator wsCreator;
	protected String inst_path;
	protected File pathFromFile;
	protected Editor editor;
	protected Browser browser;
	protected Console console;
	protected NewPj newPj;
	protected NewFile newFile;
	protected JLabel label,browserLabel;
	protected String es;
	protected JFrame frame;
	protected RTextScrollPane textScrollPane;
	protected JScrollPane scrollPane;
	protected JScrollPane scrollPane_1;
	protected RSyntaxTextArea syntaxTextArea;
	
	public MainWin(boolean check,String inst_path) throws IOException{

		// Creation du workspace si c'est la premiere execution; sinon lecture du fichier
		this.inst_path = inst_path;
		this.wsCreator = new Ws_creator(this);
		if (!check) {
			this.pathFromFile = this.wsCreator.getWsPath();
			this.wsCreator.createWs(this.pathFromFile, inst_path);
		} else {
			InputStream extracteur = new FileInputStream(inst_path + File.separator + "settings.cfg");
			InputStreamReader isr = new InputStreamReader(extracteur, Charset.forName("UTF-8"));
			BufferedReader br = new BufferedReader(isr);
			String line;
			while ((line = br.readLine()) != null) {
				this.pathFromFile = new File(line);
			}
			br.close();
		}
		// Verification du cas ou le dossier n existe plus
		if (!pathFromFile.exists()) {
			new MessageBox("Previous workspace doesn't exist anymore.", "warn");
			this.pathFromFile = this.wsCreator.getWsPath();
			this.wsCreator.createWs(this.pathFromFile, inst_path);
		}
		
		//MAIN FRAME
		this.frame = new JFrame("VGBA_IDE");
		JPanel contentPane = new JPanel();
		
		frame.getContentPane().add(contentPane);
		SpringLayout sl_contentPane = new SpringLayout();
		contentPane.setLayout(sl_contentPane);
			//LABELS
		JLabel lblconsole = new JLabel("Console:");
		this.label = new JLabel("");
		this.browserLabel = new JLabel("");
		JLabel lblNewLabel = new JLabel("A");
		lblNewLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				float size = Math.min(editor.syntaxTextArea.getFont().getSize2D()+1, 30);
				editor.syntaxTextArea.setFont(editor.syntaxTextArea.getFont().deriveFont(size));
			}
		});
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		
		
		
		JLabel lblNewLabel_1 = new JLabel("A");
		lblNewLabel_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				float size = Math.max(editor.syntaxTextArea.getFont().getSize2D()-1, 10);
				editor.syntaxTextArea.setFont(editor.syntaxTextArea.getFont().deriveFont(size));
			}
		});
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 10));
		
		//---LABELS---
		
		//Containers
		this.scrollPane = new JScrollPane();
		this.scrollPane_1 = new JScrollPane();
		this.syntaxTextArea=new RSyntaxTextArea();
		this.syntaxTextArea.setCodeFoldingEnabled(true);
		this.textScrollPane = new RTextScrollPane(this.syntaxTextArea);
		//-------------
		this.popupMenu = new JPopupMenu();
		addPopup(this.scrollPane, this.popupMenu);
		//Variable Screen Size
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.width = (int)screenSize.getWidth();
		this.factx = width/1024.f;
		this.heigth = (int)screenSize.getHeight();
		this.facty = heigth/768.f;
		this.frame.setBounds(0, 0, width, heigth);
		//-----------
		
		
		
		//Cointainers Properties
		sl_contentPane.putConstraint(SpringLayout.NORTH, scrollPane, 30, SpringLayout.NORTH, contentPane); //top browser
		sl_contentPane.putConstraint(SpringLayout.SOUTH, scrollPane, (int)facty*-170, SpringLayout.SOUTH, contentPane); //bottom Browser
		sl_contentPane.putConstraint(SpringLayout.WEST, scrollPane, 10, SpringLayout.WEST, contentPane); //left browser
		sl_contentPane.putConstraint(SpringLayout.EAST, scrollPane, (int)factx*200, SpringLayout.WEST, contentPane);	//right browser
		sl_contentPane.putConstraint(SpringLayout.WEST, this.browserLabel, 0, SpringLayout.WEST, scrollPane);	//left WSName Label
		sl_contentPane.putConstraint(SpringLayout.SOUTH, this.browserLabel, -5, SpringLayout.NORTH, scrollPane); //bot WSName Label
		
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblconsole, 5, SpringLayout.SOUTH, scrollPane); //top lblconsole
		sl_contentPane.putConstraint(SpringLayout.WEST, lblconsole, 10, SpringLayout.WEST, contentPane); //left lblconsole
		sl_contentPane.putConstraint(SpringLayout.NORTH, scrollPane_1, 5, SpringLayout.SOUTH, lblconsole); //top Console
		sl_contentPane.putConstraint(SpringLayout.SOUTH, scrollPane_1, -10, SpringLayout.SOUTH, contentPane); //bottom console
		sl_contentPane.putConstraint(SpringLayout.WEST, scrollPane_1, 10, SpringLayout.WEST, contentPane); //left console
		sl_contentPane.putConstraint(SpringLayout.EAST, scrollPane_1, -10, SpringLayout.EAST, contentPane); //right console
		
		sl_contentPane.putConstraint(SpringLayout.NORTH, textScrollPane, 30, SpringLayout.NORTH, contentPane);	//top editor
		sl_contentPane.putConstraint(SpringLayout.SOUTH, textScrollPane, -5, SpringLayout.NORTH, lblconsole);	//bot editor
		sl_contentPane.putConstraint(SpringLayout.WEST, textScrollPane, 10, SpringLayout.EAST, scrollPane);	//left editor
		sl_contentPane.putConstraint(SpringLayout.EAST, textScrollPane, -10, SpringLayout.EAST, contentPane); //right editor
		sl_contentPane.putConstraint(SpringLayout.WEST, this.label, 0, SpringLayout.WEST, textScrollPane);	//left FileName Label
		sl_contentPane.putConstraint(SpringLayout.SOUTH, this.label, -5, SpringLayout.NORTH, textScrollPane); //bot FileName Label
		sl_contentPane.putConstraint(SpringLayout.EAST, lblNewLabel, 0, SpringLayout.EAST, textScrollPane);	//right +TextSize Label
		sl_contentPane.putConstraint(SpringLayout.SOUTH, lblNewLabel, -5, SpringLayout.NORTH, textScrollPane); //bot +TextSize Label
		sl_contentPane.putConstraint(SpringLayout.EAST, lblNewLabel_1, -30, SpringLayout.WEST, lblNewLabel);	//right -TextSize Label
		sl_contentPane.putConstraint(SpringLayout.SOUTH, lblNewLabel_1, -5, SpringLayout.NORTH, textScrollPane); //bot -TextSize Label
		
		//------------
		
		//CreatingObjects
		makeMenu();
		this.editor = new Editor(this);
		this.console = new Console(this);
		this.browser = new Browser(this);
		this.newPj = new NewPj(this);
		this.newFile = new NewFile(this);
		//-------------------

		//Adding Containers
		contentPane.add(scrollPane);
		contentPane.add(scrollPane_1);
		contentPane.add(textScrollPane);
		contentPane.add(this.label);
		contentPane.add(this.browserLabel);
		contentPane.add(lblconsole);
		contentPane.add(lblNewLabel);
		contentPane.add(lblNewLabel_1);
		//------------
		
		frame.setVisible(true);
		//------MAIN FRAME---------------
	}

	public void makeMenu() {
		//MENUBAR
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenu mnNew = new JMenu("New");
		mnFile.add(mnNew);
		
		JMenu mnFile_1 = new JMenu("File");
		mnNew.add(mnFile_1);
		
		JMenuItem mntmcFile = new JMenuItem(".C File");
		mntmcFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File fft = browser.getFileFromTree();
		        if (fft.getName().equals("source")) {
				newFile.ext = "c";
				newFile.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				newFile.setVisible(true);
		        }else{
		        	new MessageBox("New .c files must be placed in \"source\" folder.", "info");
		        }  
			}
		});
		mntmcFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.ALT_MASK));
		mnFile_1.add(mntmcFile);
		
		JMenuItem mntmsFile = new JMenuItem(".S File");
		mntmsFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.ALT_MASK));
		mntmsFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File fft = browser.getFileFromTree();
		        if (fft.getName().equals("source")) {
				newFile.ext = "s";
				newFile.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				newFile.setVisible(true);
		        } else {
		        	new MessageBox("New .s files must be placed in \"source\" folder.", "info");
		        }
			}
		});
		mnFile_1.add(mntmsFile);
		
		JMenuItem mntmhFile = new JMenuItem(".H File");
		mntmhFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.ALT_MASK));
		mntmhFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File fft = browser.getFileFromTree();
		        if (fft.getName().equals("include")) {
				newFile.ext = "h";
				newFile.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				newFile.setVisible(true);
		        } else {
		        	new MessageBox("New .h files must be placed in \"include\" folder.", "info");
		        }
			}
		});
		mnFile_1.add(mntmhFile);
		
		JMenuItem mntmProject = new JMenuItem("Project");
		mntmProject.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newPj.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				newPj.setVisible(true);
			}
		});
		mntmProject.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK));
		mnNew.add(mntmProject);
		
		JMenuItem mntmSave = new JMenuItem("Save");
		mntmSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editor.saveFile();
			}
		});
		mntmSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		mnFile.add(mntmSave);
		
		JMenu mnCompile_1 = new JMenu("Compile");
		mnFile.add(mnCompile_1);
		
		JMenuItem mntmCompile = new JMenuItem("Compile");
		mntmCompile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
		mntmCompile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (editor.opened == null) {
					new MessageBox("Please open the file in \"source\" folder that you want to compile and run", "error");
				}
				// if this is not a makefile
				try {
				if (!editor.opened.getName().equals("Makefile")) {
					console.compile(editor.opened.getParentFile().getParent(), false);
				} else {
					new MessageBox("Please open the file in \"source\" folder that you want to compile.", "error");
				}
				}
				catch (NullPointerException e1) {
				}
			}
		});
		mnCompile_1.add(mntmCompile);
		
		JMenuItem mntmCompileRun = new JMenuItem("Compile & Run");
		mntmCompileRun.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0));
		mntmCompileRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (editor.opened == null) {
					new MessageBox("Please open the file in \"source\" folder that you want to compile and run", "error");
				}
				// Fait un make et run
				if (!editor.opened.getName().equals("Makefile")) {
					console.compile(editor.opened.getParentFile().getParent(), true);
				} else {
					new MessageBox("Please open the file in \"source\" folder that you want to compile and run", "error");
				}
			}
		});
		mnCompile_1.add(mntmCompileRun);
		
		
		//Edit Menu
		JMenu mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);
		
		JMenuItem mntmClearConsole = new JMenuItem("Clear Console");
		mntmClearConsole.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				console.clearC();
			}
		});
		mnEdit.add(mntmClearConsole);
		
		JMenuItem mntmRefreshBrowser = new JMenuItem("Refresh Browser");
		mntmRefreshBrowser.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK));
		mntmRefreshBrowser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refreshBrowser();
			}
		});
		mnEdit.add(mntmRefreshBrowser);
		
		JMenuItem mntmChangeWorkspace = new JMenuItem("Change Workspace");
		mntmChangeWorkspace.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rebuildWorkspace();
			}
		});
		mnEdit.add(mntmChangeWorkspace);
		//Info Menu
		JMenu mnCompile = new JMenu("Info");
		menuBar.add(mnCompile);
		
		JMenuItem mntmCredits = new JMenuItem("Credits");
		mntmCredits.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Credits(width, heigth, inst_path);
			}
		});
		mnCompile.add(mntmCredits);
		//---MENUBAR---
		
		//POPUPMENU
		JMenu mnNew1 = new JMenu("New...");
		popupMenu.add(mnNew1);
		
		JMenu mnFile_11 = new JMenu("File...");
		mnNew1.add(mnFile_11);
		
		JMenuItem mntmcFile_1 = new JMenuItem(".C File");
		mntmcFile_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newFile.ext = "c";
				newFile.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				newFile.setVisible(true);
			}
		});
		mnFile_11.add(mntmcFile_1);
		
		JMenuItem mntmcFile_3 = new JMenuItem(".H File");
		mntmcFile_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newFile.ext = "h";
				newFile.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				newFile.setVisible(true);
			}
		});
		mnFile_11.add(mntmcFile_3);
		
		JMenuItem mntmsFile_1 = new JMenuItem(".S File");
		mntmsFile_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newFile.ext = "s";
				newFile.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				newFile.setVisible(true);
			}
		});
		mnFile_11.add(mntmsFile_1);
		
		JMenuItem mntmProject_1 = new JMenuItem("Project");
		mntmProject_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newPj.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				newPj.setVisible(true);
			}
		});
		mnNew1.add(mntmProject_1);
		
		JMenuItem mntmDelete = new JMenuItem("Delete");
		mntmDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File tobdeleted = browser.getFileFromTree();
				boolean cond = tobdeleted.equals(pathFromFile);
				if(cond){
					new MessageBox("Cannot delete the workspace.", "error");
				}else {
					new DeleteDirectory(tobdeleted);
					refreshBrowser();	
				}
				
			}
		});
		popupMenu.add(mntmDelete);
		//---POPUPMENU
	}

	// Mis a jour du workspace et de l interface
	public void rebuildWorkspace() {
		this.pathFromFile = this.wsCreator.getWsPath();
		this.wsCreator.createWs(this.pathFromFile, inst_path);
		this.editor.refresh();
		this.refreshBrowser();
	}
	// Mise a jour de la gueule du browser
	public void refreshBrowser() {
		TreeState ts = new TreeState(this.browser.fileTree);
		es = ts.getExpansionState();
		this.browser = new Browser(this);
		ts = new TreeState(this.browser.fileTree);
		ts.setExpansionState(es);
		this.scrollPane.getViewport().setView(this.browser.fileTree);
	}
	private static void addPopup(java.awt.Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
}