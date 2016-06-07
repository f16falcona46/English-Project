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

import com.greenteam.spacefighters.stage.Stage;

public class GameOverScreen extends JPanel implements ActionListener, ComponentListener {
	private static final long serialVersionUID = -20160518L;
	
	private static final int NUM_STARS = 120;
	
	private JLabel score;
	private JButton returnToMain;
	private Stage stage;
	private JFrame window;

	private double[] xpositions;
	private double[] ypositions;
	
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
		score = new JLabel("Score: 0");
		score.setForeground(Color.LIGHT_GRAY);
		score.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 36));
		this.add(score, gbc);
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.insets = new Insets(20, 20, 20, 20);
		returnToMain = new JButton("Return to Main Menu");
		returnToMain.addActionListener(this);
		this.add(returnToMain, gbc);
		
		this.addComponentListener(this);
		
		xpositions = new double[GameOverScreen.NUM_STARS];
		ypositions = new double[GameOverScreen.NUM_STARS];
		for (int i = 0; i < GameOverScreen.NUM_STARS; ++i) {
			xpositions[i] = Math.random();
			ypositions[i] = Math.random();
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.WHITE);
		for (int i = 0; i < GameOverScreen.NUM_STARS; ++i) {
			g.fillRect((int)(xpositions[i]*this.getWidth()), (int)(ypositions[i]*this.getHeight()), 1, 1);
		}
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
