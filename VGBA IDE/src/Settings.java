import java.io.File;

public class Settings {
File path;
int textSize;
String arch;
public Settings(){
}
public void setPath(File Path){
	this.path=Path;
}
public void setPath(String Path){
	this.path=new File(Path);
}
public void setTextSize(int textSize){
	this.textSize=textSize;
}
public void setArch(String arch){
	this.arch=arch;
}

public File getPath(){
	return this.path;
}
public String getPathToString(){
	return this.path.toString();
}
public int getTextSize(){
	return this.textSize;
}
public String getArch(){
	return this.arch;
}
}
