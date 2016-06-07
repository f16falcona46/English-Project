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

public class LevelIncrementScreen extends JPanel implements ActionListener, ComponentListener {
	private static final long serialVersionUID = 20160518L;
	
	private static final int NUM_STARS = 120;
	
	private Stage stage;
	private JLabel levelName;
	private JTextArea levelDescription;
	private JButton returnToGame;
	private double[] xpositions;
	private double[] ypositions;
	
	public LevelIncrementScreen(Stage stage) {
		this.stage = stage;
		
		this.addComponentListener(this);
		this.setLayout(new GridBagLayout());
		this.setBackground(Color.BLACK);
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weighty = 0;
		gbc.insets = new Insets(20, 20, 20, 20);
		levelName = new JLabel("Uninitialized levelName");
		levelName.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 36));
		levelName.setForeground(Color.LIGHT_GRAY);
		this.add(levelName, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(20, 20, 20, 20);
		levelDescription = new JTextArea("Uninitialized levelDescription");
		levelDescription.setLineWrap(true);
		levelDescription.setWrapStyleWord(true);
		levelDescription.setEditable(false);
		levelDescription.setOpaque(false);
		levelDescription.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 24));
		levelDescription.setForeground(Color.LIGHT_GRAY);
		this.add(levelDescription, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.weighty = 1;
		gbc.insets = new Insets(20, 20, 20, 20);
		returnToGame = new JButton("Return to Game");
		returnToGame.addActionListener(this);
		this.add(returnToGame, gbc);
		
		xpositions = new double[LevelIncrementScreen.NUM_STARS];
		ypositions = new double[LevelIncrementScreen.NUM_STARS];
		for (int i = 0; i < LevelIncrementScreen.NUM_STARS; ++i) {
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
		levelName.setText("Level "+(stage.getLevelLoader().getLevel()+1));
		levelDescription.setText(stage.getLevelLoader().getLevelBlurb());
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.WHITE);
		for (int i = 0; i < LevelIncrementScreen.NUM_STARS; ++i) {
			g.fillRect((int)(xpositions[i]*this.getWidth()), (int)(ypositions[i]*this.getHeight()), 1, 1);
		}
	}
}
