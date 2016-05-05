import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class ProjectSettingsDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8410343876042970955L;
	
	private final JPanel contentPane = new JPanel();
	private JTextField textField;
	protected ConfigManager configManager;
	private String[] txtSizeList = new String[21];
	protected String[] archList = new String[] { "-marm", "-mthumb -mthumb-interwork" };
	protected JComboBox<String> comboBox;
	protected JComboBox<String> comboBox_1;
	protected File selectedFile;
	

	public ProjectSettingsDialog(ConfigManager cm) {
		for (int i = 0; i < 21; i++) {
			txtSizeList[i] = "" + (10 + i); // super classy int to string
											// conversion ;)
		}

		this.configManager = cm;

		setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(200, 200, 460, 163);
		getContentPane().setLayout(new BorderLayout());
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPane, BorderLayout.CENTER);
		contentPane.setLayout(null);

		textField = new JTextField();
		textField.setBounds(106, 36, 254, 25);
		contentPane.add(textField);
		textField.setColumns(10);

		JLabel lblTitle = new JLabel("Chose project settings");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setBounds(0, 12, 458, 15);
		contentPane.add(lblTitle);

		JLabel lblNewLabel = new JLabel("Project:");
		lblNewLabel.setBounds(10, 41, 70, 15);
		contentPane.add(lblNewLabel);

		JLabel lblArchitecture = new JLabel("Architecture:");
		lblArchitecture.setBounds(10, 82, 107, 15);
		contentPane.add(lblArchitecture);

		this.comboBox = new JComboBox<String>();
		comboBox.setBounds(106, 73, 129, 24);
		comboBox.setModel(new DefaultComboBoxModel<String>(archList));
		comboBox.setSelectedIndex(0);
		contentPane.add(comboBox);

		JLabel lblTextSize = new JLabel("Text size:");
		lblTextSize.setBounds(245, 82, 70, 15);
		contentPane.add(lblTextSize);

		this.comboBox_1 = new JComboBox<String>();
		comboBox_1.setBounds(317, 73, 129, 24);
		comboBox_1.setModel(new DefaultComboBoxModel<String>(txtSizeList));
		comboBox_1.setSelectedIndex(3);
		contentPane.add(comboBox_1);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton okButton = new JButton("OK");
		okButton.setActionCommand("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				configManager.setOption(Options.OPTION_NAME, textField.getText());
				configManager.setOption(Options.OPTION_ARCH, archList[(int) comboBox.getSelectedIndex()]);
				configManager.setOption(Options.OPTION_TEXT_SIZE, txtSizeList[(int) comboBox_1.getSelectedIndex()]);
				setVisible(false);
				dispose();

			}
		});
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.setActionCommand("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new MessageBox("Default or previous settings will be applied", "info");
				setVisible(false);
				dispose();
			}
		});
		buttonPane.add(cancelButton);
		setVisible(true);
	}

}
