package net.whdm.blasticfantastic;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;



public class StartBlasticFantastic {
	
	public static void main(String[] args) {
		
		JFrame window = new JFrame("BlasticFantastic v 0.1 Launcher");
		final JDialog connection = new JDialog();
		window.setSize(500, 400);
		window.setResizable(false);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container container = window.getContentPane();
		container.setLayout(new GridLayout(2,1));
		container.setBackground(Color.WHITE);
		JLabel logo = new JLabel(new ImageIcon("data/bflogo.png"));
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridBagLayout());
		buttonPanel.setBackground(Color.WHITE);
		JButton sp_button = new JButton("Start LAN/Singleplayer");
		JButton mp_button = new JButton("Join MP game");
		
		buttonPanel.add(new JPanel());
		buttonPanel.add(sp_button);
		buttonPanel.add(new JPanel());
		buttonPanel.add(mp_button);
		buttonPanel.add(new JPanel());
		
		container.add(logo);
		container.add(buttonPanel);
		
		
		sp_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new BlasticFantastic();
			}
		});
		
		mp_button.addActionListener(new ActionListener()  {
			public void actionPerformed(ActionEvent arg0) {
				connection.setSize(270, 120);
				connection.setResizable(false);
				connection.setLayout(new GridBagLayout());
				JLabel lb1 = new JLabel("IP: ");
				final JTextField serverip = new JTextField(9);
				final JTextField serverport = new JTextField(5);
				
				JLabel lb2 = new JLabel(":");
				
				JButton tryconnect = new JButton("Connect");
				
				connection.add(lb1);
				connection.add(serverip);
				
				connection.add(lb2);
				connection.add(serverport);
				
				connection.add(tryconnect);
				
				tryconnect.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						new BlasticFantastic(serverip.getText(), Integer.parseInt(serverport.getText()));
					}

				});
				
				connection.setVisible(true);
			}
		});
		
		
		window.setVisible(true);
	}

}
