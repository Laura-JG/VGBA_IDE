import javax.swing.JOptionPane;
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
		this.mainWin.scrollPane_1.setViewportView(textArea);
		this.mainWin.scrollPane_1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		
	}
	
	public void compile(String path, boolean dowehavetorun){
		if (this.mainWin.editor.getTextState()){
			JOptionPane JOP = new JOptionPane();
			 @SuppressWarnings("static-access")
			int reply = JOP.showConfirmDialog(null, "Save file?", "UNSAVED CHANGES", JOptionPane.YES_NO_OPTION);
			if (reply == JOptionPane.YES_OPTION) {
				mainWin.editor.saveFile();
			}				
		}
		this.textArea.setText(null);
		CompilerThread cT = new CompilerThread(this,dowehavetorun,path);
		cT.start();
	}
	
	public void clearC() {
		this.textArea.setText(null);
	}
	
}
