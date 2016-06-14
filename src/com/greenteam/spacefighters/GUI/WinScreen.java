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

public class WinScreen extends JPanel implements ActionListener, ComponentListener {
	private static final long serialVersionUID = 20160518L;
	
	private Stage stage;
	private JLabel title;
	private JTextArea winDescription;
	private JButton returnToTitle;
	//private double[] xpositions;
	//private double[] ypositions;
	
	public WinScreen(Stage stage) {
		this.stage = stage;
		
		this.addComponentListener(this);
		this.setLayout(new GridBagLayout());
		this.setBackground(Color.BLACK);
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weighty = 0;
		gbc.insets = new Insets(20, 20, 20, 20);
		title = new JLabel("You won!");
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
		winDescription = new JTextArea("Congratulations.");
		winDescription.setLineWrap(true);
		winDescription.setWrapStyleWord(true);
		winDescription.setEditable(false);
		winDescription.setOpaque(false);
		winDescription.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 24));
		winDescription.setForeground(Color.LIGHT_GRAY);
		this.add(winDescription, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.weighty = 1;
		gbc.insets = new Insets(20, 20, 20, 20);
		returnToTitle = new JButton("Return to Title");
		returnToTitle.addActionListener(this);
		this.add(returnToTitle, gbc);
		
		/*
		xpositions = new double[DeathScreen.NUM_STARS];
		ypositions = new double[DeathScreen.NUM_STARS];
		for (int i = 0; i < DeathScreen.NUM_STARS; ++i) {
			xpositions[i] = Math.random();
			ypositions[i] = Math.random();
		}
		*/
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		if (ev.getSource() == returnToTitle) {
			stage.getLevelLoader().startLevel();
			((CardLayout)this.getParent().getLayout()).show(this.getParent(), Window.TITLE_SCREEN);
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
		//winDescription.setText("Lives: "+stage.getPlayer().getLives());
	}
}
