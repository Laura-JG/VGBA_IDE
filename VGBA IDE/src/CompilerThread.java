import java.io.File;
import java.io.IOException;

public class CompilerThread extends Thread{

	protected Console console;
	protected boolean dowehavetorun;
	protected String path;
	protected Process p;
	
	public CompilerThread(Console console,boolean dowehavetorun,String path){
		this.console=console;
		this.dowehavetorun=dowehavetorun;
		this.path=path;
		
	}
	public void run(){
			String command = "./compile";
			p = null;
			ProcessBuilder pb = new ProcessBuilder(command);
			if (this.dowehavetorun) {
				pb = new ProcessBuilder(command,"run");
				System.out.println(command);
			}
			pb.directory(new File(this.path));
			pb.redirectErrorStream(true);
			try {
				p=pb.start();
				ConsoleThread cT = new ConsoleThread(this.console,p);
				cT.start();
			} catch (IOException e1) {
				new MessageBox("Couldn't execute process.", "error");
				e1.printStackTrace();
			}
	}
	
}
