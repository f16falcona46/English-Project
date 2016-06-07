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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.greenteam.spacefighters.stage.Stage;

public class DeathScreen extends JPanel implements ActionListener, ComponentListener {
	private static final long serialVersionUID = 20160518L;
	
	private static final int NUM_STARS = 120;
	
	private Stage stage;
	private JLabel title;
	private JTextArea deathDescription;
	private JButton returnToGame;
	private double[] xpositions;
	private double[] ypositions;
	
	public DeathScreen(Stage stage) {
		this.stage = stage;
		
		this.addComponentListener(this);
		this.setLayout(new GridBagLayout());
		this.setBackground(Color.BLACK);
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weighty = 0;
		gbc.insets = new Insets(20, 20, 20, 20);
		title = new JLabel("Your ship is scrap.");
		title.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 36));
		title.setForeground(Color.LIGHT_GRAY);
		this.add(title, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(20, 20, 20, 20);
		deathDescription = new JTextArea("Unintialized deathDescription");
		deathDescription.setLineWrap(true);
		deathDescription.setWrapStyleWord(true);
		deathDescription.setEditable(false);
		deathDescription.setOpaque(false);
		deathDescription.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 24));
		deathDescription.setForeground(Color.LIGHT_GRAY);
		this.add(deathDescription, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.weighty = 1;
		gbc.insets = new Insets(20, 20, 20, 20);
		returnToGame = new JButton("Return to Game");
		returnToGame.addActionListener(this);
		this.add(returnToGame, gbc);
		
		xpositions = new double[DeathScreen.NUM_STARS];
		ypositions = new double[DeathScreen.NUM_STARS];
		for (int i = 0; i < DeathScreen.NUM_STARS; ++i) {
			xpositions[i] = Math.random();
			ypositions[i] = Math.random();
		}
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		if (ev.getSource() == returnToGame) {
			stage.resume();
			((CardLayout)this.getParent().getLayout()).show(this.getParent(), Window.STAGE);
		}
	}

	@Override
	public void componentHidden(ComponentEvent e) {
		//do nothing
	}

	@Override
	public void componentMoved(ComponentEvent e) {
		//do nothing
	}

	@Override
	public void componentResized(ComponentEvent e) {
		//do nothing
	}

	@Override
	public void componentShown(ComponentEvent e) {
		updateText();
	}
	
	private void updateText() {
		deathDescription.setText("Your ship was destroyed. Fortunately, it could be reconstructed, but unfortunately, it can only be done "+
			stage.getPlayer().getLives()+
			((stage.getPlayer().getLives()==1)?" more time.":" more times."));
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.WHITE);
		for (int i = 0; i < DeathScreen.NUM_STARS; ++i) {
			g.fillRect((int)(xpositions[i]*this.getWidth()), (int)(ypositions[i]*this.getHeight()), 1, 1);
		}
	}
}
