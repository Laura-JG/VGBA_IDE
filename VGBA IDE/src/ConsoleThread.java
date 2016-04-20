import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleThread extends Thread{
	protected Console console;
	protected Process p;
	public ConsoleThread(Console console,Process p){
		this.p=p;
		this.console=console;
	}
	public void run(){
		 String line;
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(p.getInputStream()), 1);
				try {
					while ((line = bufferedReader.readLine()) != null) {
						   this.console.textArea.append(line);
						   this.console.textArea.append("\n");
						}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					try {
						bufferedReader.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			
	}
}
