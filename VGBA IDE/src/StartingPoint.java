import java.io.File;
import java.io.IOException;
	
public class StartingPoint {
		
		public static void main(String[] args) {
			String inst_path = System.getProperty("user.home")+File.separator+"VGBA_IDE";
			boolean check = new File(inst_path+File.separator+"settings.cfg").exists();
			try {
				new MainWin(check, inst_path);
			} catch (IOException e) {
				new MessageBox("A major error has occured.", "error");
				e.printStackTrace();
			}
		}
}
