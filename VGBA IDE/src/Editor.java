import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import org.fife.ui.rsyntaxtextarea.FileLocation;
import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.fife.ui.rsyntaxtextarea.TextEditorPane;
import org.fife.ui.rsyntaxtextarea.FileFileLocation;

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
		this.syntaxTextArea = mainWin.syntaxTextArea;
		//this.syntaxTextArea.setCodeFoldingEnabled(true);
		mainWin.textScrollPane.setViewportView(this.syntaxTextArea);
		//textScrollPane.setBounds((int)(170*mainWin.factx), (int)(30*mainWin.facty), (int)(828*mainWin.factx), (int)(493*mainWin.facty));		
		//this.mainWin.contentPane.add(textScrollPane);	
	}
	
	public void loadFile(File file,String file_ext)
	{
		
		if(this.opened!=null){
			if (getTextState()){
				JOptionPane JOP = new JOptionPane();
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
