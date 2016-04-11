import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;


public class Console {
	
	protected JTextArea textArea;
	protected MainWin mainWin;
	
	public Console(MainWin mainWin)
	{
		this.mainWin = mainWin;
		this.textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		textArea.setLineWrap(true);
		JScrollPane sp = new JScrollPane(textArea);
		sp.setBounds((int)(mainWin.factx*10), (int)(mainWin.facty*537), (int)(mainWin.factx*988), (int)(mainWin.facty*150));
		sp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		mainWin.contentPane.add(sp);
	}
	
	public void compile(String path, boolean dowehavetorun){
		if (this.mainWin.editor.getTextState()){
			JOptionPane JOP = new JOptionPane();
			 int reply = JOP.showConfirmDialog(null, "Save file?", "UNSAVED CHANGES", JOptionPane.YES_NO_OPTION);
			if (reply == JOptionPane.YES_OPTION) {
				mainWin.editor.saveFile();
			}				
		}
		this.textArea.setText(null);
        String line;
		String command = "./compile";
		Process p = null;
		ProcessBuilder pb = new ProcessBuilder(command);
		if (dowehavetorun) {
			pb = new ProcessBuilder(command,"run");
			System.out.println(command);
		}
		pb.directory(new File(path));
		pb.redirectErrorStream(true);
		try {
			p=pb.start();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(p.getInputStream()), 1);
			while ((line = bufferedReader.readLine()) != null) {
				   this.textArea.append(line);
				   this.textArea.append("\n");
				}
				bufferedReader.close();
		} catch (IOException e1) {
			new MessageBox("Couldn't execute process.", "error");
			e1.printStackTrace();
		}
	}
	
	public void clearC() {
		this.textArea.setText(null);
	}
	
}
