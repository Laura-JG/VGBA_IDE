import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.UIManager;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;


public class Editor {
	
	protected RSyntaxTextArea syntaxTextArea;
	protected RTextScrollPane textScrollPane;
	protected MainWin mainWin;
	protected File opened;
	
	public Editor(MainWin mainWin)
	{
		this.mainWin = mainWin;
		this.syntaxTextArea = new RSyntaxTextArea();
		this.syntaxTextArea.setCodeFoldingEnabled(true);
		RTextScrollPane textScrollPane = new RTextScrollPane(this.syntaxTextArea);
		textScrollPane.setBounds((int)(170*mainWin.factx), (int)(30*mainWin.facty), (int)(828*mainWin.factx), (int)(493*mainWin.facty));		
		this.mainWin.contentPane.add(textScrollPane);
	}
	
	public void loadFile(File file,String file_ext)
	{
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
		

	}
	
	public void refresh() {
		this.syntaxTextArea.setText("");
	}

}
