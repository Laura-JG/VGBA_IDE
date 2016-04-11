import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JDialog;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JLabel;

import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


@SuppressWarnings("serial")
public class MainWin extends JFrame {

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
	protected JLabel label;
	protected float factx;
	protected float facty;
	protected String es;
	protected int width;
	protected int heigth;
	
	/**
	 * Create the frame.
	 * @throws IOException 
	 */
	public MainWin(boolean check, String inst_path) throws IOException {
		// Creation du workspace si c'est la premiere execution; sinon lecture du fichier
		this.inst_path = inst_path;
		this.wsCreator = new Ws_creator(this);
		if ( !check ) {
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
		
		// Fenetre principale
		setTitle("VGBA IDE");
		setResizable(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.width = (int)screenSize.getWidth();
		this.factx = width/1024.f;
		this.heigth = (int)screenSize.getHeight();
		this.facty = heigth/768.f;
		setBounds(0, 0, width, heigth);
		
		// Contenu du scollpane principal, contient tous les composants
		contentPane = new JPanel();
		contentPane.setPreferredSize(new Dimension(width - 20, heigth - 80));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);

		// Le scrollpane contient le contentPane, conteneur de tous les composants
		JScrollPane sP = new JScrollPane(contentPane);
		//sP.setBounds(0, 0, width, heigth);
		sP.setPreferredSize(new Dimension(width - 5, heigth - 5));
		sP.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		sP.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		//sP.setVisible(true);
		//contentPane.add(sP);
		setContentPane(sP);
		sP.setViewportView(contentPane);
		
		// Sous-fenetres
		this.editor = new Editor(this);
		this.console = new Console(this);
		makeMenu();
		this.browser = new Browser(this);
		this.newPj = new NewPj(this);
		this.newFile = new NewFile(this);
		
		this.setVisible(true);
	}
	
	// Menu barre du haut
	public void makeMenu() {
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnNewMenu = new JMenu("File");
		menuBar.add(mnNewMenu);
		
		JMenu mnNewProject = new JMenu("New");
		mnNewMenu.add(mnNewProject);
		
		JMenu mnNewMenu_2 = new JMenu("File");
		mnNewProject.add(mnNewMenu_2);
		
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
		mnNewMenu_2.add(mntmcFile);
		
		JMenuItem mntmsFile = new JMenuItem(".S File", 0);
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
		mnNewMenu_2.add(mntmsFile);
		
		JMenuItem mntmhFile = new JMenuItem(".H File", 0);
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
		mnNewMenu_2.add(mntmhFile);
		
		// Nouveau projet
		JMenuItem mntmProject = new JMenuItem("Project");
		mntmProject.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newPj.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				newPj.setVisible(true);
			}
		});
		mntmProject.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK));
		mnNewProject.add(mntmProject);
		
		JMenuItem mntmSave = new JMenuItem("Save");
		mntmSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editor.saveFile();
			}
		});
		mntmSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		mnNewMenu.add(mntmSave);
		
		JMenu mnCompile = new JMenu("Compile");
		mnNewMenu.add(mnCompile);
		
		JMenuItem mntmCompile = new JMenuItem("Compile");
		mntmCompile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
		mntmCompile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (editor.opened == null) {
					new MessageBox("Please open the file in \"source\" folder that you want to compile and run", "error");
				}
				// si ca s'est pas un makefile
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
		mnCompile.add(mntmCompile);
		
		JMenuItem mntmCompileAndRun = new JMenuItem("Compile and Run");
		mntmCompileAndRun.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0));
		mntmCompileAndRun.addActionListener(new ActionListener() {
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
		mnCompile.add(mntmCompileAndRun);
		
		JMenu mnNewMenu_1 = new JMenu("Edit");
		menuBar.add(mnNewMenu_1);
		
		JMenuItem mntmClearConsole = new JMenuItem("Clear Console");
		mntmClearConsole.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				console.clearC();
			}
		});
		mnNewMenu_1.add(mntmClearConsole);
		
		JMenuItem mntmref = new JMenuItem("Refresh browser");
		mntmref.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK));
		mntmref.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refreshBrowser();
			}
		});
		mnNewMenu_1.add(mntmref);
		
		// Change workspace
		JMenuItem mntmChangeWorkspace = new JMenuItem("Change WorkSpace");
		mntmChangeWorkspace.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rebuildWorkspace();
			}
		});
		mnNewMenu_1.add(mntmChangeWorkspace);
		
		JMenu mnNewMenu_3 = new JMenu("Info");
		menuBar.add(mnNewMenu_3);
		
		JMenuItem mntmCredits = new JMenuItem("Credits");
		mntmCredits.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Credits(width, heigth, inst_path);
			}
		});
		mnNewMenu_3.add(mntmCredits);
		
		// Popup menu du browser (ouvert avec click droit)
		JPopupMenu popupMenu = new JPopupMenu();
		contentPane.add(popupMenu);
		this.popupMenu=popupMenu;
		
		JMenu mnNew = new JMenu("New...");
		popupMenu.add(mnNew);
		
		JMenu mnFile_1 = new JMenu("File...");
		mnNew.add(mnFile_1);
		
		JMenuItem mntmcFile_1 = new JMenuItem(".C File");
		mntmcFile_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newFile.ext = "c";
				newFile.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				newFile.setVisible(true);
			}
		});
		mnFile_1.add(mntmcFile_1);
		
		JMenuItem mntmcFile_3 = new JMenuItem(".H File");
		mntmcFile_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newFile.ext = "h";
				newFile.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				newFile.setVisible(true);
			}
		});
		mnFile_1.add(mntmcFile_3);
		
		JMenuItem mntmsFile_1 = new JMenuItem(".S File");
		mntmsFile_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newFile.ext = "s";
				newFile.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				newFile.setVisible(true);
			}
		});
		mnFile_1.add(mntmsFile_1);
		
		JMenuItem mntmProject_1 = new JMenuItem("Project");
		mntmProject_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newPj.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				newPj.setVisible(true);
			}
		});
		mnNew.add(mntmProject_1);
		
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
		
		this.label = new JLabel("");
		label.setBounds((int)(factx*170), (int)(facty*15), (int)(factx*478), (int)(facty*14));
		contentPane.add(label);
		
		JLabel lblConsole = new JLabel("Console:");
		lblConsole.setBounds((int)(factx*10), (int)(facty*521), (int)(factx*100), (int)(facty*14));
		contentPane.add(lblConsole);
		
		JLabel lblNewLabel = new JLabel("A");
		lblNewLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				float size = Math.min(editor.syntaxTextArea.getFont().getSize2D()+1, 30);
				editor.syntaxTextArea.setFont(getFont().deriveFont(size));
			}
		});
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel.setBounds((int)(982*factx), (int)(10*facty), (int)(11*factx), (int)(14*facty));
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("A");
		lblNewLabel_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				float size = Math.max(editor.syntaxTextArea.getFont().getSize2D()-1, 10);
				editor.syntaxTextArea.setFont(getFont().deriveFont(size));
			}
		});
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblNewLabel_1.setBounds((int)(956*factx), (int)(10*facty), (int)(11*factx), (int)(14*facty));
		contentPane.add(lblNewLabel_1);
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
		es=ts.getExpansionState();
		contentPane.remove(this.browser.scrollPane);	
		this.browser = new Browser(this);
		ts=new TreeState(this.browser.fileTree);
		ts.setExpansionState(es);
		this.browser.scrollPane.getViewport().setView(this.browser.fileTree);
	}
	
}
