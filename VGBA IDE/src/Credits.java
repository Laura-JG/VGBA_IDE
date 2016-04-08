import javax.swing.JFrame;
import javax.swing.ImageIcon;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import java.io.File;

import javax.swing.SwingConstants;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class Credits {
	
	private int w;
	private int h;
	private String icoPath;
	
	public Credits(int width, int heigth, String icoPath) {
		this.w = width;
		this.h= heigth;
		this.icoPath = icoPath;
		showCredits();
	}
	
	public void showCredits() {
		JFrame frmCredits = new JFrame();
		frmCredits.setAlwaysOnTop(true);
		frmCredits.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmCredits.setTitle("Credits");
		frmCredits.setResizable(false);
		frmCredits.getContentPane().setLayout(new BorderLayout(0, 0));
		frmCredits.setBounds((int)(w/2)-250, (int)(h/2)-100, 500, 200);
		
		/*JLabel lblVgbaide = new JLabel("VGBA_IDE");
		lblVgbaide.setHorizontalAlignment(SwingConstants.CENTER);
		lblVgbaide.setFont(new Font("Dialog", Font.BOLD, 16));
		frmCredits.getContentPane().add(lblVgbaide, BorderLayout.NORTH);*/
		
		JTextPane txtrDevelopers = new JTextPane();
		txtrDevelopers.setBackground(UIManager.getColor("Button.background"));
		txtrDevelopers.setEditable(false);
		txtrDevelopers.setText("\nDevelopers:\n\nGiancarmine Salucci (giancarmine.salucci@student.univaq.it)\nLaura Juan Galmes (Laura.Juan.1@etu.unige.ch)");
		StyledDocument doc = txtrDevelopers.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
		frmCredits.getContentPane().add(txtrDevelopers, BorderLayout.CENTER);
		
		JLabel lblNewLabel = new JLabel();
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setIcon(new ImageIcon(icoPath+File.separator+"VGBA_IDE.png"));
		frmCredits.getContentPane().add(lblNewLabel, BorderLayout.NORTH);
		lblNewLabel.setVisible(true);
		
		frmCredits.setVisible(true);
	}
}
