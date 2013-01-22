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

/*
 * Simple launcher-class for my game. Gives you the option to start a local server (Singleplayer)
 * or a multiplayer game where you connect to someone elses's singleplayer game.
 * Launcher is made in a JFrame and an action command starts a new instance of my game, with either no arguments or HOST IP & PORT as arguments.
 * 
 * Robin Jonsson
 */

public class StartBlasticFantastic {
	
	public static void main(String[] args) {
		
		//New JFrame
		JFrame window = new JFrame("BlasticFantastic v 0.1 Launcher");
		final JDialog connection = new JDialog();
		window.setSize(500, 400);
		window.setResizable(false);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Container
		Container container = window.getContentPane();
		container.setLayout(new GridLayout(2,1));
		container.setBackground(Color.WHITE);
		
		//Put out nice image in a JLabel as a good holder.
		JLabel logo = new JLabel(new ImageIcon("data/bflogo.png"));
		
		//Create the buttons
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
		
		//Singleplayer-button
		sp_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new BlasticFantastic();
			}
		});
		
		//Multiplayer-button
		mp_button.addActionListener(new ActionListener()  {
			public void actionPerformed(ActionEvent arg0) {
				//Show a JDialog
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
					//Connect-button pressed, try and connect to give ip and port.
					public void actionPerformed(ActionEvent e) {
						new BlasticFantastic(serverip.getText(), Integer.parseInt(serverport.getText()));
					}

				});
				
				connection.setVisible(true);
			}
		});
		
		//We need to see something
		window.setVisible(true);
	}

}
