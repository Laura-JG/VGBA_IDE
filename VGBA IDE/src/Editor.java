import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

public class Editor {
	
	protected RSyntaxTextArea syntaxTextArea;
	protected RTextScrollPane textScrollPane;
	protected MainWin mainWin;
	protected File opened;
	protected boolean textModified;
	protected String originalValue;
	
	public Editor(MainWin mainWin)
	{
		
		this.mainWin = mainWin;
		
		this.syntaxTextArea=mainWin.syntaxTextArea;
		this.syntaxTextArea.setFont(syntaxTextArea.getFont().deriveFont((float)mainWin.settings.getTextSize()));
		mainWin.textScrollPane.setViewportView(this.syntaxTextArea);
		
	}
	
	public void loadFile(File file,String file_ext)
	{
		if(this.opened!=null){
			if (getTextState()){
				JOptionPane JOP = new JOptionPane();
				 @SuppressWarnings("static-access")
				int reply = JOP.showConfirmDialog(null, "Save file?", "UNSAVED CHANGES", JOptionPane.YES_NO_OPTION);
				if (reply == JOptionPane.YES_OPTION) {
					saveFile();
				}				
			}
				
		}
		
		this.opened = file;
		
		switch (file_ext)
		{
			case("c"):
				this.syntaxTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_C);
				break;
			case("h"):
				this.syntaxTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_C);
				break;
			case("s"):
				this.syntaxTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_ASSEMBLER_X86);
				break;
			case(""):
				this.syntaxTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_MAKEFILE);
			break;
		}
		try {
			BufferedReader r = new BufferedReader(new FileReader(file));
			this.syntaxTextArea.read(r, null);
			r.close();
		} catch (IOException ioe) {
			new MessageBox("A problem has occured with the reading of the file.", "error");
			ioe.printStackTrace();
			UIManager.getLookAndFeel().provideErrorFeedback(this.syntaxTextArea);
		}
		originalValue=this.syntaxTextArea.getText();
	}
	
	public void saveFile()
	{
		if (this.opened==null){
			JOptionPane JOP = new JOptionPane();
			 @SuppressWarnings("static-access")
			int reply = JOP.showConfirmDialog(null, "Create a new project?", "New Project?", JOptionPane.YES_NO_OPTION);
			if (reply == JOptionPane.YES_OPTION) {
				mainWin.newPj.buildWindow(true);
			}				
		}
		String text=this.syntaxTextArea.getText();
		try {
			BufferedWriter w = new BufferedWriter(new FileWriter(opened));
			w.write(text);
			w.close();
		} catch (IOException ioe) {
			new MessageBox("A problem has occured with the writing of the file.", "error");
			ioe.printStackTrace();
			UIManager.getLookAndFeel().provideErrorFeedback(this.syntaxTextArea);
		}
		originalValue=this.syntaxTextArea.getText();
		setTextState(false);
	}
	public boolean getTextState()
	{
		int oriHash = originalValue.hashCode();
		int openedHash=this.syntaxTextArea.getText().hashCode();
		if (oriHash != openedHash){
			textModified=true;
		}
		else {
			textModified = false;
		}
		return textModified;
	}
	
	public void setTextState(boolean mod)
	{
		textModified=mod;
	}
	
	public void refresh() {
		this.syntaxTextArea.setText("");
	}

}
