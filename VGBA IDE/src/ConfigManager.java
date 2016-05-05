import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;

import org.ho.yaml.Yaml;
import org.ho.yaml.exception.YamlException;

public class ConfigManager {

	protected Settings settings;
	protected MainWin mainWin;
	protected File defaultSettingsFile;
	public ConfigManager(MainWin mainWin) {
		this.mainWin = mainWin;
		this.defaultSettingsFile = new File(this.mainWin.getInstPath() + File.separator + "settings.cfg");
		boolean isReadable = isReadable(this.defaultSettingsFile);
		if (defaultSettingsFile.exists() && isReadable) {
			System.out.println("Loading settings...");
			readFromFile(defaultSettingsFile, 1000);
			System.out.println(getOption(Options.OPTION_ARCH) + " " + getOption(Options.OPTION_TEXT_SIZE) + " "
					+ getOption(Options.OPTION_PATH));

		} else {
			System.out.println("Settings file not found or unreadable!");
			this.settings = new Settings();
			fillFactorySettings();
			new DefaultSettingsDialog(this);
			System.out.println("Writing settings...");
			writeToFile(mainWin.getSettingsFile(), settings);
			if (!new File(getOption(Options.OPTION_PATH)).exists()) {
				System.out.println("Creating workspace directory at: " + getOption(Options.OPTION_PATH));
				createWorkspace(getOption(Options.OPTION_PATH));
			}
		}
	}

	public void newPj() {
		new ProjectSettingsDialog(this);
		File pf = new File(this.getOption(Options.OPTION_PATH)+ File.separator + getOption(Options.OPTION_NAME) + File.separator + Options.OPTION_DEFAULT_PSETTINGS_FILE_NAME);
		createPj(getOption(Options.OPTION_NAME), pf);
		writeToFile(pf, getSettings());
		applyArch();
		mainWin.browser.OpenFile(this.getOption(Options.OPTION_PATH)+File.separator+this.getOption(Options.OPTION_NAME)+"source"+File.separator+"templateC.c");
	}
	
	public void applyArch() {
		File mf = new File(this.getOption(Options.OPTION_PATH)+File.separator+this.getOption(Options.OPTION_NAME)+File.separator+"Makefile");
        String strLine;
        StringBuilder fileContent = new StringBuilder();
		try {
			FileInputStream fstream = new FileInputStream(mf);
		 
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
        
        //Read File Line By Line
        
        	int i=0;
			while ((strLine = br.readLine()) != null) {				
				if (i==28){
					strLine="ARCH	:=	"+getOption(Options.OPTION_ARCH);
					fileContent.append(strLine);
					}else{
						fileContent.append(strLine);
					}
				fileContent.append("\n");
				i++;
			}
			FileWriter fstreamWrite = new FileWriter(mf);
	        BufferedWriter out = new BufferedWriter(fstreamWrite);
	        out.write(fileContent.toString());
	        out.close();
	        br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	// Args: name of the project and settings file
	public void createPj(String pName, File pf) {
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
			} else {
				new MessageBox("\"" + theDir.toString() + "\" folder already exists in this location.", "error");
			}
		}
		try {
			pf.createNewFile();
		} catch (IOException e1) {
			new MessageBox("Coulnd create pSettings file","error");
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
		mainWin.refreshBrowser();
		//mainWin.browser.OpenFile(subDirs[subDirs.length - 1] + File.separator + "templateC.c");
	}
	
	public void changeWs() {
		new DefaultSettingsDialog(this);
		System.out.println("Change WS, after diag: " + getOption(Options.OPTION_ARCH) + " "
				+ getOption(Options.OPTION_TEXT_SIZE) + " " + getOption(Options.OPTION_PATH));
		writeToFile(mainWin.getSettingsFile(), settings);
	}

	public void createWorkspace(File path) {
		if (!path.exists()) {
			path.mkdirs();
		} else {
			new MessageBox("Couldnt create Workspace in " + path.getAbsolutePath() + " Directory already exists",
					"error");
		}
	}

	public void createWorkspace(String path) {
		File f = new File(path);
		createWorkspace(f);
	}
	
	public void setOption(int index, String value) {
		switch (index) {
		// Unuseful case?
		case Options.OPTION_NAME: {
			this.settings.setName(value);
			break;
		}
		case Options.OPTION_PATH: {
			this.settings.setPath(value);
			break;
		}
		case Options.OPTION_TEXT_SIZE: {
			int size = Integer.parseInt(value);
			this.settings.setTextSize(size);
			break;
		}
		case Options.OPTION_ARCH: {
			this.settings.setArch(value);
			break;
		}
		}
	}

	public String getOption(int index) {
		switch (index) {
		case Options.OPTION_NAME: {
			return this.settings.getName();
		}
		case Options.OPTION_PATH: {
			return this.settings.getPathToString();
		}
		case Options.OPTION_TEXT_SIZE: {
			String s = "" + this.settings.getTextSize();
			return s;
		}
		case Options.OPTION_ARCH: {
			return this.settings.getArch();
		}
		default: {
			return "No Valid Option Has Been Selected";
		}

		}
	}
	
	public void fillFactorySettings() {
		setOption(Options.OPTION_ARCH, Options.OPTION_DEFAULT_ARCH);
		setOption(Options.OPTION_TEXT_SIZE, Options.OPTION_DEFAULT_TEXT_SIZE + "");
		setOption(Options.OPTION_PATH, Options.OPTION_DEFAULT_PATH);
		setOption(Options.OPTION_NAME, Options.OPTION_DEFAULT_NAME);
	}

	public boolean isReadable(File file) {
		boolean isReadable = true;
		try {
			Yaml.load(file);
		} catch (FileNotFoundException e) {
			new MessageBox("Requested file: " + file.getAbsolutePath() + " doesn't exist", "error");
			isReadable = false;
		} catch (YamlException e) {
			new MessageBox(file.getAbsolutePath() + " is not Yaml compatible", "error");
			isReadable = false;
		}
		return isReadable;
	}
	
	public void readFromFile(File f, int index) {
		Settings s = new Settings();
		if (f.exists()) {
			try {
				s = (Settings) Yaml.load(f);
			} catch (FileNotFoundException e) {
				new MessageBox("Requested file: " + f.getAbsolutePath() + " doesn't exist", "error");
				e.printStackTrace();
			} catch (YamlException e) {
				new MessageBox(f.getAbsolutePath() + " is not Yaml compatible", "error");
				e.printStackTrace();
			}
			switch (index) {
			case Options.OPTION_ARCH: {
				setOption(Options.OPTION_ARCH, s.getArch());
				break;
			}
			case Options.OPTION_TEXT_SIZE: {
				setOption(Options.OPTION_TEXT_SIZE, s.getTextSize() + "");
				break;
			}
			case Options.OPTION_NAME: {
				setOption(Options.OPTION_NAME, s.getName());
				break;
			}
			case Options.OPTION_PATH: {
				setOption(Options.OPTION_PATH, s.getName());
				break;
			}
			default: {
				setSettings(s);
			}
			}
		} else {
			new MessageBox("Requested file: " + f.getAbsolutePath() + " doesn't exist", "error");
		}
	}

	public void writeToFile(File f, Settings s) {
		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				new MessageBox("Couldn't create file: " + f.toString(), "error");
				e.printStackTrace();
			}
		}
		try {
			Yaml.dump(s, new File(f.toString()));
		} catch (FileNotFoundException e) {
			new MessageBox("Not Found: " + f.toString(), "error");
			e.printStackTrace();
		} catch (YamlException y) {
			new MessageBox("yaml went crazy on: " + f.toString(), "error");
			y.printStackTrace();
		}

	}

	public Settings getSettings() {
		return this.settings;
	}

	public void setSettings(Settings settings) {
		this.settings = settings;
	}

	public void loadProjectSettings(File f) {
		if (!f.getName().equals("Makefile")) {
			File psf = new File(f.getParentFile().getParent() + File.separator + Options.OPTION_DEFAULT_PSETTINGS_FILE_NAME);
			if (psf.exists()) {
				if (getFileExt(f).equals("c") || getFileExt(f).equals("s") || getFileExt(f).equals("h")) {
					readFromFile(psf, Options.OPTION_NAME);
					System.out.println("Loading Project: " + getOption(Options.OPTION_NAME));
					readFromFile(psf, Options.OPTION_TEXT_SIZE);
					readFromFile(psf, Options.OPTION_ARCH);
				}
			} else {
				System.out.println("Couldnt find settings file...\nCreating "+Options.OPTION_DEFAULT_PSETTINGS_FILE_NAME);
				setOption(Options.OPTION_NAME, new File(psf.getParent()).getName());
				System.out.println("loading Project: " + getOption(Options.OPTION_NAME));
				readFromFile(defaultSettingsFile, Options.OPTION_ARCH);
				readFromFile(defaultSettingsFile, Options.OPTION_TEXT_SIZE);
				writeToFile(psf,getSettings());
			}
		} else {
			File psf = new File(f.getParent() + File.separator + Options.OPTION_DEFAULT_PSETTINGS_FILE_NAME);
			if (psf.exists()) {
				readFromFile(psf, Options.OPTION_NAME);
				System.out.println("Loading Project: " + getOption(Options.OPTION_NAME));
				readFromFile(psf, Options.OPTION_TEXT_SIZE);
				readFromFile(psf, Options.OPTION_ARCH);
			}else {
				System.out.println("Couldnt find settings file...\nCreating "+Options.OPTION_DEFAULT_PSETTINGS_FILE_NAME);
				setOption(Options.OPTION_NAME, psf.getParent());
				System.out.println("Loading Project: " + getOption(Options.OPTION_NAME));
				readFromFile(defaultSettingsFile, Options.OPTION_ARCH);
				readFromFile(defaultSettingsFile, Options.OPTION_TEXT_SIZE);
				writeToFile(psf,getSettings());
			}
			
		}
	}
	
	public String getFileExt(File file) {
	    String name = file.getName();
	    try {
	        return name.substring(name.lastIndexOf(".") + 1);
	    } catch (Exception e) {
	        return "";
	    }
	}
}
