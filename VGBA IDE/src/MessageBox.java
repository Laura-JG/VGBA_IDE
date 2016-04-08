import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class MessageBox {
	public MessageBox(String message,String type){
		JFrame frame=new JFrame();
		int option = JOptionPane.PLAIN_MESSAGE;
		String titre = "";
		switch(type)
		{
			case("info"):
			{
				titre = "Information";
				option = JOptionPane.INFORMATION_MESSAGE;
				break;
			}
			case("warn"):
			{
				titre = "Warning!";
				option = JOptionPane.WARNING_MESSAGE;
				break;
			}
			case("error"):
			{
				titre = "ERROR";
				option = JOptionPane.ERROR_MESSAGE;
				break;
			}
			default:
			{
				titre = "VGBA";
				option = JOptionPane.PLAIN_MESSAGE;
				break;
			}
		}
		JOptionPane.showMessageDialog(frame, message, titre, option);
	}
}
