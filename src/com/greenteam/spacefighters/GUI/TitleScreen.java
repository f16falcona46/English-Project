package com.greenteam.spacefighters.GUI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;


public class TitleScreen extends JPanel implements ActionListener {
	private static final long serialVersionUID = -8833873967148164038L;
	
	private JLabel title;
	private JButton startGatsby;
	//private JButton settingsButton;
	//private JButton tutorialButton;
	private JTextArea basicInstructions;
	private Window window;
	private JButton startNick;
	
	public TitleScreen(Window window) {
		this.window = window;
		
		this.setBackground(Color.BLACK);
		this.setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.gridwidth = 2;
		ImageIcon titleIcon = null;
		try {
			Image img = ImageIO.read(TitleScreen.class.getResource("/com/greenteam/spacefighters/assets/title-0.png"));
			titleIcon = new ImageIcon(img.getScaledInstance(4 * img.getWidth(null) / 5, 4 * img.getHeight(null) / 5, Image.SCALE_SMOOTH));
		} catch (IOException e) {}
		if (titleIcon != null) {
			title = new JLabel(titleIcon);
		} else {
			title = new JLabel("SpaceFighters");
		}
		title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 36));
		title.setForeground(Color.LIGHT_GRAY);
		this.add(title, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(10, 60, 10, 10);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		if (!window.useMouseInput())
			basicInstructions = new JTextArea("Use arrow keys to move, and use Z, X, C, V, and F to fire. Use Space to pause.");
		else
			basicInstructions = new JTextArea("Move mouse to move, use mouse buttons to fire, use Space to pause.");
		basicInstructions.setLineWrap(true);
		basicInstructions.setWrapStyleWord(true);
		basicInstructions.setEditable(false);
		basicInstructions.setOpaque(false);
		basicInstructions.setForeground(Color.LIGHT_GRAY);
		this.add(basicInstructions, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.weightx = 1;
		gbc.weighty = 1;
		startNick = new JButton("Play as Nick");
		startNick.addActionListener(this);
		this.add(startNick, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.weightx = 1;
		gbc.weighty = 1;
		startGatsby = new JButton("Play as Gatsby");
		startGatsby.addActionListener(this);
		this.add(startGatsby, gbc);
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		if (ev.getSource() == startNick) {
			window.getStage().getLevelLoader().setLevel(0);
			window.setCard(Window.STAGE);
		}
		else if (ev.getSource() == startGatsby) {
			window.getStage().getLevelLoader().setLevel(1);
			window.setCard(Window.STAGE);
		}
	}
	
	public void updateInstructions() {
		//if (!window.useMouseInput())
		//	basicInstructions.setText("Use arrow keys to move, and use Z, X, C, V, and F to fire. Use Space to pause.");
		//else
		//	basicInstructions.setText("Move mouse to move, use mouse buttons to fire, use Space to pause.");
		basicInstructions.setText("Left and Right to move, and Z to jump.");
	}
}
