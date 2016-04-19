import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.ho.yaml.Yaml;
import org.ho.yaml.exception.YamlException;

import javax.swing.JLabel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import javax.swing.JComboBox;

@SuppressWarnings("serial")
public class NewPj extends JDialog {

	protected MainWin mainWin;
	protected String[] archList;
	protected String[] txtSizeList;
	protected Psettings psettings;
	protected File settingsFile;
	protected JComboBox<String> comboBox;
	protected JComboBox <String>comboBox_1;
	
	protected JFrame frame;

	/**
	 * Create the dialog.
	 */
	public NewPj(final MainWin mainWin, boolean show) {
		this.mainWin = mainWin;
		this.psettings=new Psettings();
		buildWindow(show);
	}

	public void buildWindow(boolean show) {
		this.txtSizeList = this.mainWin.settingsManager.txtSizeList;
		this.archList = this.mainWin.settingsManager.archList;
		frame = new JFrame("New Project");
		frame.setBounds(100, 100, 324, 216);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		JPanel contentPane = new JPanel();
		contentPane.setLayout(null);
		frame.getContentPane().add(contentPane);

		// setDefaultCloseOperation(JDialog.CANCEL_OPTION);
		final JTextField textField = new JTextField();
		textField.setBounds(120, 12, 190, 20);
		contentPane.add(textField);
		JLabel lblProjectName = new JLabel("Project Name:");
		lblProjectName.setBounds(12, 14, 105, 14);
		contentPane.add(lblProjectName);

		JButton okButton = new JButton("OK");
		okButton.setBounds(120, 157, 190, 25);
		okButton.setActionCommand("OK");
		getRootPane().setDefaultButton(okButton);
		
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				settingsFile = new File(mainWin.settings.getPath()+File.separator + textField.getText() +File.separator+ textField.getText() + ".cfg");
				createPJ(textField.getText());
				storeSettings();
				frame.dispose();
			}
		});
		contentPane.add(okButton);
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setBounds(12, 157, 105, 25);
		cancelButton.setActionCommand("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
		contentPane.add(cancelButton);
		
		this.comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(this.archList));
		comboBox.setSelectedIndex(0);

		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println(comboBox.getSelectedIndex());
				comboBox.setSelectedIndex(comboBox.getSelectedIndex());
				psettings.setArch(archList[comboBox.getSelectedIndex()]);
			}
		});
		comboBox.setBounds(120, 58, 190, 24);
		contentPane.add(comboBox);
		
		this.comboBox_1 = new JComboBox();
		
		comboBox_1.setModel(new DefaultComboBoxModel(this.txtSizeList));
		comboBox_1.setSelectedIndex(3);
		comboBox_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				psettings.setTextSize(toInt(txtSizeList[comboBox_1.getSelectedIndex()]));
			}
		});
		comboBox_1.setBounds(120, 110, 190, 24);
		contentPane.add(comboBox_1);
		//
		JLabel lblArchitecture = new JLabel("Architecture:");
		lblArchitecture.setBounds(12, 63, 105, 14);
		contentPane.add(lblArchitecture);

		JLabel lblTextsize = new JLabel("TextSize");
		lblTextsize.setBounds(12, 115, 105, 14);
		contentPane.add(lblTextsize);
		if (show) {
			frame.setVisible(true);
		}
	}

	public void createPJ(String pName) {
		// Creating project folder and subdirectories of the project
		String[] subDirs = { mainWin.pathFromFile + File.separator + pName,
				mainWin.pathFromFile + File.separator + pName + File.separator + "build",
				mainWin.pathFromFile + File.separator + pName + File.separator + "data",
				mainWin.pathFromFile + File.separator + pName + File.separator + "gfx",
				mainWin.pathFromFile + File.separator + pName + File.separator + "include",
				mainWin.pathFromFile + File.separator + pName + File.separator + "source" };
		if (new File(subDirs[0]).exists()) {
			new MessageBox("Project \"" + pName + "\" already exists!", "error");
			return;
		}
		int i;
		for (i = 0; i < subDirs.length; i++) {
			File theDir = new File(subDirs[i]);
			if (!theDir.exists()) {
				try {
					theDir.mkdir();
				} catch (Exception se) {
					new MessageBox("Problem encountered creating \"" + theDir.toString() + "\" folder.", "error");
				}
				dispose();
			} else {
				new MessageBox("\"" + theDir.toString() + "\" folder already exists in this location.", "error");
			}
		}
		try {
			settingsFile.createNewFile();
		} catch (IOException e1) {
			new MessageBox("Coulnd create psettings file","error");
			e1.printStackTrace();
		}
		// Copying ressources to the new project's folders
		File[] dest = { new File(subDirs[0] + File.separator + "Makefile"),
				new File(subDirs[0] + File.separator + "compile"),
				new File(subDirs[subDirs.length - 1] + File.separator + "templateC.c"),
				new File(subDirs[subDirs.length - 1] + File.separator + "templateS.s") };
		File[] source = { new File(mainWin.inst_path + File.separator + "Resources" + File.separator + "Makefile"),
				new File(mainWin.inst_path + File.separator + "Resources" + File.separator + "compile"),
				new File(mainWin.inst_path + File.separator + "Resources" + File.separator + "templateC.c"),
				new File(mainWin.inst_path + File.separator + "Resources" + File.separator + "templateS.s") };
		try {
			for (i = 0; i < 4; i++) {
				Files.copy(source[i].toPath(), dest[i].toPath());
			}
		} catch (IOException e) {
			new MessageBox("Couldn't copy all the needed files.", "error");
		}
		mainWin.browser.OpenFile(subDirs[subDirs.length - 1] + File.separator + "templateC.c");
		mainWin.refreshBrowser();
	}

	public int toInt(String source) {
		int converted = 0;
		converted = Integer.parseInt(source);

		return converted;
	}
	public void storeSettings(){
		try {
			System.out.println(comboBox_1.getSelectedIndex());
			this.psettings.setTextSize(toInt(this.txtSizeList[comboBox_1.getSelectedIndex()]));
			System.out.println(this.txtSizeList[comboBox_1.getSelectedIndex()]);
			this.psettings.setArch(this.archList[comboBox.getSelectedIndex()]);
			Yaml.dump(this.psettings,this.settingsFile); //YAML Write
			
		} catch (FileNotFoundException e) {
		
			// TODO MessageBox couldnt save settings file;
			//TODO erase previously created settings.cfg file
			e.printStackTrace();
		}
	}
	public void loadSettings(){
		try {
			this.psettings=(Psettings) Yaml.load(settingsFile); //YAML Read
			//TODO check if valid Yaml file, how?
			//TODO erase previously created settings.cfg file
		}catch (YamlException y){
		
			new DeleteDirectory(settingsFile);
			
			new SettingsManager(this.mainWin);
		}
		catch (FileNotFoundException e) {
			
			// TODO MessageBox Couldnt Read settings file
			e.printStackTrace();
		}
	}
}
