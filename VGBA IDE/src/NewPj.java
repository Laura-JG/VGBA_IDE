import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;


@SuppressWarnings("serial")
public class NewPj extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	protected MainWin mainWin;

	/**
	 * Create the dialog.
	 */
	public NewPj(final MainWin mainWin) {
		this.mainWin = mainWin;
		setBounds(100, 100, 385, 135);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		//setDefaultCloseOperation(JDialog.CANCEL_OPTION);
		textField = new JTextField();
		textField.setBounds(10, 28, 345, 20);
		textField.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
            	createPJ(textField.getText());
				mainWin.newPj.dispose();
            }});
		contentPanel.add(textField);
		textField.setColumns(10);
		
		JLabel lblProjectName = new JLabel("Project Name");
		lblProjectName.setBounds(10, 11, 168, 14);
		contentPanel.add(lblProjectName);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						createPJ(textField.getText());
						mainWin.newPj.dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						mainWin.newPj.dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
			
		}
	}
	
	public void createPJ(String pName) {
		// Creating project folder and subdirectories of the project
		String[] subDirs = {mainWin.pathFromFile+File.separator+pName,
				mainWin.pathFromFile+File.separator+pName+File.separator+"build",
				mainWin.pathFromFile+File.separator+pName+File.separator+"data",
				mainWin.pathFromFile+File.separator+pName+File.separator+"gfx",
				mainWin.pathFromFile+File.separator+pName+File.separator+"include",
				mainWin.pathFromFile+File.separator+pName+File.separator+"source"};
		if(new File(subDirs[0]).exists()) {
			new MessageBox("Project \""+ pName+"\" already exists!", "error");
			return;
		}
		int i;
		for (i=0; i<subDirs.length; i++)
		{
			File theDir = new File(subDirs[i]);
			if (!theDir.exists()) {
			    try{
			        theDir.mkdir();
			    } 
			    catch(Exception se){
			    	new MessageBox("Problem encountered creating \""+theDir.toString()+"\" folder.", "error");
			    }
			    mainWin.newPj.dispose();
			} else
			{
				new MessageBox("\""+theDir.toString()+"\" folder already exists in this location.", "error");
			}
		}
		// Copying ressources to the new project's folders
		File[] dest = {new File(subDirs[0]+File.separator+"Makefile"),
				new File(subDirs[0]+File.separator+"compile"),
				new File(subDirs[subDirs.length-1]+File.separator+"templateC.c"),
				new File(subDirs[subDirs.length-1]+File.separator+"templateS.s")};
		File[] source = {new File(mainWin.inst_path+File.separator+"Resources"+File.separator+"Makefile"),
				new File(mainWin.inst_path+File.separator+"Resources"+File.separator+"compile"),
				new File(mainWin.inst_path+File.separator+"Resources"+File.separator+"templateC.c"),
				new File(mainWin.inst_path+File.separator+"Resources"+File.separator+"templateS.s")};
			    try {
			    	for (i=0; i<4; i++) {
			    		Files.copy(source[i].toPath(), dest[i].toPath());
			    	}
			    } catch (IOException e) {
			    	new MessageBox("Couldn't copy all the needed files.", "error");
			    }
		mainWin.browser.OpenFile(subDirs[subDirs.length-1]+File.separator+"templateC.c");
		mainWin.refreshBrowser();
	}
	
}
