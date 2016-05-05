import java.io.File;

public class Settings {
	File path;
	int textSize;
	String arch;
	String name;

	public Settings() {
	}

	public void setPath(File path) {
		this.path = path;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setPath(String path) {
		this.path = new File(path);
	}

	public void setTextSize(int textSize) {
		this.textSize = textSize;
	}

	public void setArch(String arch) {
		this.arch = arch;
	}

	public File getPath() {
		return this.path;
	}

	public String getPathToString() {
		return this.path.toString();
	}

	public int getTextSize() {
		return this.textSize;
	}

	public String getArch() {
		return this.arch;
	}
}
