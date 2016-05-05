import java.awt.Font;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

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

	public Editor(MainWin mainWin) {

		this.mainWin = mainWin;
		this.syntaxTextArea = mainWin.syntaxTextArea;
		this.syntaxTextArea.setFont(syntaxTextArea.getFont()
				.deriveFont((float) Integer.parseInt(mainWin.getConfigManager().getOption(Options.OPTION_TEXT_SIZE))));
		mainWin.textScrollPane.setViewportView(this.syntaxTextArea);

	}

	public void setTextSize(int size){
		this.syntaxTextArea.setFont(syntaxTextArea.getFont()
				.deriveFont((float) size));
	}
	
	public int getTextSize(){
		return (int)this.syntaxTextArea.getFont().getSize2D();
	}
	
	public void loadFile(File file, String file_ext) {
		setTextSize(Integer.parseInt(mainWin.getConfigManager().getOption(Options.OPTION_TEXT_SIZE)));
		if (this.opened != null) {
			if (getTextState()) {
				JOptionPane JOP = new JOptionPane();
				@SuppressWarnings("static-access")
				int reply = JOP.showConfirmDialog(null, "Save file?", "UNSAVED CHANGES", JOptionPane.YES_NO_OPTION);
				if (reply == JOptionPane.YES_OPTION) {
					saveFile();
				}
			}

		}
		this.opened = file;
		switch (file_ext) {
		case ("c"):
			this.syntaxTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_C);
			break;
		case ("h"):
			this.syntaxTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_C);
			break;
		case ("s"):
			this.syntaxTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_ASSEMBLER_X86);
			break;
		case (""):
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
		originalValue = this.syntaxTextArea.getText();
	}

	public void saveFile() 
							
	{
		if (getTextSize() != Integer.parseInt(this.mainWin.getConfigManager().getOption(Options.OPTION_TEXT_SIZE)))
		{
			this.mainWin.getConfigManager().setOption(Options.OPTION_TEXT_SIZE, getTextSize()+"");
			this.mainWin.getConfigManager().writeToFile(new File(this.mainWin.getConfigManager().getOption(Options.OPTION_PATH)+File.separator+this.mainWin.getConfigManager().getOption(Options.OPTION_NAME)+File.separator+Options.OPTION_DEFAULT_PSETTINGS_FILE_NAME),this.mainWin.getConfigManager().getSettings());
		}
		if (this.opened == null) {
			JOptionPane JOP = new JOptionPane();
			@SuppressWarnings("static-access")
			int reply = JOP.showConfirmDialog(null, "Create a new project?", "New Project?", JOptionPane.YES_NO_OPTION);
			if (reply == JOptionPane.YES_OPTION) {
				mainWin.getConfigManager().newPj();
			} else {
				return;
			}
		}
		String text = this.syntaxTextArea.getText();
		try {
			BufferedWriter w = new BufferedWriter(new FileWriter(opened));
			w.write(text);
			w.close();
		} catch (IOException ioe) {
			new MessageBox("A problem has occured with the writing of the file.", "error");
			ioe.printStackTrace();
			UIManager.getLookAndFeel().provideErrorFeedback(this.syntaxTextArea);
		}
		originalValue = this.syntaxTextArea.getText();
		setTextState(false);
	}

	public boolean getTextState() {
		int oriHash = originalValue.hashCode();
		int openedHash = this.syntaxTextArea.getText().hashCode();
		if (oriHash != openedHash) {
			textModified = true;
		} else {
			textModified = false;
		}
		return textModified;
	}

	public void setTextState(boolean mod) {
		textModified = mod;
	}

	public void refresh(String oldWs) {
		Float size = (float) Integer.parseInt(mainWin.getConfigManager().getOption(Options.OPTION_TEXT_SIZE));
		Font font = syntaxTextArea.getFont().deriveFont(size);
		this.syntaxTextArea.setFont(font);
		if (this.opened == null) {
			return;
		}
		String newWs = mainWin.configManager.getOption(Options.OPTION_PATH);
		// Se il nuovo WS coincide con il vecchio
		if (oldWs.equals(newWs)) {
			System.out.println("Same Workspace Detected");
			//this.mainWin.browser.OpenFile(this.opened.toString()); no need to specify, wont be erased
		} else {
			System.out.println("Different Workspace Detected");
			this.opened = null;
			mainWin.label.setText("");	// No way to erase text
			
		}
	}

}
