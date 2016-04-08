import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;


@SuppressWarnings("serial")
public class NewFile extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	protected MainWin mainWin;
	protected String ext;

	/**
	 * Create the dialog.
	 */
	public NewFile(final MainWin mainWin) {
		this.mainWin = mainWin;
		setBounds(100, 100, 385, 135);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		//setDefaultCloseOperation(JDialog.CANCEL_OPTION);
		textField = new JTextField();
		textField.setBounds(10, 28, 345, 20);
		textField.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent e){
            	createFile(textField.getText());
				mainWin.newFile.dispose();

            }});
		contentPanel.add(textField);
		textField.setColumns(10);
		
		JLabel lblProjectName = new JLabel("File Name");
		lblProjectName.setBounds(10, 11, 86, 14);
		contentPanel.add(lblProjectName);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						createFile(textField.getText());
						mainWin.newFile.dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						mainWin.newFile.dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
			
		}
	}
	
	public void createFile(String fileName)
	{
		File file = new File((mainWin.browser.getFileFromTree().toString())+File.separator+fileName+"."+ext);
		try {
			if (file.createNewFile()){
				this.mainWin.refreshBrowser();
			}
		} catch (IOException e) {
			new MessageBox("Couldn't create new file.", "error");
			e.printStackTrace();
		}
		
		
	}

}
