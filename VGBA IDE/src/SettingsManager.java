import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import org.ho.yaml.Yaml;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class SettingsManager{
	
	protected MainWin mainWin;
	protected String defaultPath;
	protected JTextField textField;
	protected Settings settings;
	protected File selectedFile;
	protected File settingsFile;
	protected boolean settingsFileExists,firstStart;
	
	public SettingsManager(MainWin mainWin) {
		
		this.settings= new Settings();
		this.mainWin = mainWin;
		settingsFile=new File(mainWin.inst_path+File.separator+"settings.cfg");
		this.defaultPath = System.getProperty("user.home")+File.separator+"VGBA_WS";
		settings.setPath(defaultPath);
		settingsFileExists=settingsFile.exists();
		if (!settingsFileExists){
			this.firstStart=true;
			setDefaults();
		}else
		{
			this.firstStart=false;
			loadSettings();
		}
	}
	
	/**
	 * @wbp.parser.entryPoint
	 */
	public void setDefaults(){
		
		try {
			if (!settingsFile.getParentFile().exists())
			    settingsFile.getParentFile().mkdirs();
			settingsFileExists=settingsFile.createNewFile();
			}
		 catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		final String[] archList=new String[] {"-marm", "-mthumb -mthumb-interwork"};
		final int[] txtSizeList= new int[] {10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20};
		final JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		final JFrame frame=new JFrame("VGBA_IDE Configuration");
		frame.setSize(450, 240);
		frame.setResizable(false);
		JPanel contentPane = new JPanel();
		frame.getContentPane().add(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblConfigureVgbaide = new JLabel("Configure VGBA_IDE");
		lblConfigureVgbaide.setBounds(144, 12, 145, 15);
		contentPane.add(lblConfigureVgbaide);
		
		JLabel lblNewLabel = new JLabel("Workspace Path:");
		lblNewLabel.setBounds(12, 56, 126, 15);
		contentPane.add(lblNewLabel);
		
		JLabel lblDefaultArchitecture = new JLabel("Default architecture:");
		lblDefaultArchitecture.setBounds(12, 96, 167, 15);
		contentPane.add(lblDefaultArchitecture);
		
		JLabel lblDefaultTextSize = new JLabel("Default text size:");
		lblDefaultTextSize.setBounds(12, 139, 167, 15);
		contentPane.add(lblDefaultTextSize);
		
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (settings.getPath()!= null && settings.getArch() != null &&  settings.getTextSize() != 0){
					storeSettings();
					firstStart=false;
					generateWorkspace(selectedFile);
					frame.setVisible(false);
					frame.dispose();
					mainWin.buildWindow();
				}
			}
		});
		btnSave.setBounds(12, 177, 424, 25);
		contentPane.add(btnSave);
		
		textField = new JTextField();
		textField.setBounds(171, 54, 191, 19);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JButton btnNewButton = new JButton("...");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (fc.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) { 
					selectedFile=fc.getSelectedFile();
					settings.setPath(selectedFile);
					textField.setText(selectedFile.toString());
				} else {
					settings.setPath(defaultPath);
					textField.setText(defaultPath);
					selectedFile=settings.getPath();
				}
			}
		});
		btnNewButton.setBounds(374, 54, 62, 19);
		contentPane.add(btnNewButton);
		
		final JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"-marm", "-mthumb -mthumb-interwork"}));
		comboBox.setSelectedIndex(0);
		//comboBox.setSelectedIndex(0);
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				settings.setArch(archList[comboBox.getSelectedIndex()]);
			}
		});
		//comboBox.setModel(new DefaultComboBoxModel(arch));
		comboBox.setBounds(171, 94, 191, 17);
		contentPane.add(comboBox);
		
		final JComboBox comboBox_1 = new JComboBox();
		comboBox_1.setModel(new DefaultComboBoxModel(new String[] {"10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20"}));
		comboBox_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				settings.setTextSize(txtSizeList[comboBox_1.getSelectedIndex()]);
			}
		});
		//comboBox.setSelectedIndex(3);
		//comboBox_1.setModel(new DefaultComboBoxModel(txtSizeList));
		comboBox_1.setBounds(171, 134, 191, 17);
		contentPane.add(comboBox_1);
		
		frame.setVisible(true);
	}
	
	public void generateWorkspace(File Path){
		File workspace = Path;
		Path.getAbsoluteFile();
		if (!workspace.exists()){
		workspace.mkdirs();
		}
	}
	
	
	public void storeSettings(){
		try {
			Yaml.dump(settings,settingsFile); //YAML Write
		} catch (FileNotFoundException e) {
			// TODO MessageBox couldnt save settings file;
			e.printStackTrace();
		}
	}
	public void loadSettings(){
		try {
			this.settings=(Settings) Yaml.load(settingsFile); //YAML Read
			//TODO check if valid Yaml file, how?
		} catch (FileNotFoundException e) {
			// TODO MessageBox Couldnt Read settings file
			e.printStackTrace();
		}
	}
	public Settings getSettings(){
		return this.settings;
	}
	
}
