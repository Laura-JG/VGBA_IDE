import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;


@SuppressWarnings("serial")
public class Ws_creator extends JFrame {

	private String defaultPath;
	protected MainWin mainWin;
	/**
	 * Create the frame.
	 */
	
	
	public Ws_creator(MainWin mainWin) {
		this.mainWin = mainWin;
		this.defaultPath = System.getProperty("user.home")+File.separator+"VGBA_WS";
	}
	
	public File getWsPath() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setBounds(10, 11, 601, 397);
		//setDefaultCloseOperation(JFileChooser.CANCEL_OPTION);
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		File chemin = new File(this.defaultPath);
		if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) { 
			chemin = fileChooser.getSelectedFile();
		} else {
			File WS = new File(defaultPath);
			WS.mkdirs();
		}
		this.dispose();
		return chemin;
	}
	
	public void createWs(File wsSelected,String inst_path) {
		boolean check = new File("settings.cfg").exists();
		File conf = new File("settings.cfg"); //
		if ( check ) { //
			conf.delete();	// Commented for Clement
		} //
		try {
			FileWriter settings = new FileWriter(inst_path + File.separator + "settings.cfg");
			settings.write(wsSelected.toString());
			settings.close();
		} catch (IOException e) {
			new MessageBox("Couldn't create settings file.", "error");
		    e.printStackTrace();
		}
	}
		
}

