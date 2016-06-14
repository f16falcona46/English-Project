package com.greenteam.spacefighters.GUI;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.greenteam.spacefighters.stage.Stage;

public class GameOverScreen extends JPanel implements ActionListener, ComponentListener {
	private static final long serialVersionUID = -20160518L;
	
	private JLabel score;
	private JButton returnToMain;
	private Stage stage;
	private JFrame window;
	
	public GameOverScreen(Stage stage, JFrame window) {
		this.stage = stage;
		this.window = window;
		
		this.setBackground(Color.BLACK);
		this.setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weighty = 1;
		gbc.insets = new Insets(20, 20, 20, 20);
		JLabel gameover = new JLabel("Game Over");
		gameover.setForeground(Color.LIGHT_GRAY);
		gameover.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 48));
		this.add(gameover);
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weighty = 1;
		//score = new JLabel("Score: 0");
		score = new JLabel("");
		score.setForeground(Color.LIGHT_GRAY);
		score.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 36));
		this.add(score, gbc);
		
		JTextArea credits = new JTextArea("Background Music (Carraway): Big Beat Mario by Triple_sSs\n"+
				"Background Music (Gatsby): Thwomp Volcano from Mario and Luigi: Partners in Time\n"+
				"Sounds: Jump from Super Mario World, Death from Super Mario All-Stars\n"+
				"Sprites: Sushanth Neerumalla\n"+
				"Tiles/Maps, GUI, Sound, Controls: Jason Li\n"+
				"Rendering: Daniel Kelly, Jason Li"
			);
		credits.setOpaque(false);
		credits.setForeground(Color.LIGHT_GRAY);
		credits.setEditable(false);
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.insets = new Insets(20, 20, 20, 20);
		this.add(credits, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.insets = new Insets(20, 20, 20, 20);
		returnToMain = new JButton("Return to Main Menu");
		returnToMain.addActionListener(this);
		this.add(returnToMain, gbc);
		
		this.addComponentListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		if (ev.getSource() == returnToMain) {
			((CardLayout)window.getContentPane().getLayout()).show(window.getContentPane(), Window.TITLE_SCREEN);
		}
	}

	@Override
	public void componentHidden(ComponentEvent arg0) {
		//do nothing
	}

	@Override
	public void componentMoved(ComponentEvent arg0) {
		//do nothing
	}

	@Override
	public void componentResized(ComponentEvent arg0) {
		//do nothing
	}

	@Override
	public void componentShown(ComponentEvent arg0) {
		score.setText("Score: "+Integer.toString(stage.getPlayer().getScore()));
	}
}
