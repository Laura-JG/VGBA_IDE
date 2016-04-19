public class Psettings {
	int textSize;
	String arch;
	public Psettings(){
	}
	
	public void setTextSize(int textSize){
		this.textSize=textSize;
	}
	public void setArch(String arch){
		this.arch=arch;
	}
	public int getTextSize(){
		return this.textSize;
	}
	public String getArch(){
		return this.arch;
	}
}
