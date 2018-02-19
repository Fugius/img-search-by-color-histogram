package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class WelcomeWindow extends JFrame implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	private JButton button;
	private JPanel container;
	
	public WelcomeWindow() {
		container = new JPanel();
		button = new JButton("Start");
	}
	
	public void InitUI() {
		this.setTitle("Image Comparator");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.setSize(1280, 720);
		
		container.setBackground(Color.white);
		container.setLayout(new BorderLayout());
		container.add(button, BorderLayout.CENTER);
		
		button.setSize(100, 50);
		button.addActionListener(this);
		
		this.setContentPane(container);
		
		this.setVisible(true);
		
		
		
		
	}
	
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource() == button) {
			button.setText("ok");
		}
	}
	
}
