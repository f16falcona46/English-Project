package com.greenteam.spacefighters.GUI.tutorial;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.greenteam.spacefighters.GUI.Window;

public abstract class TutorialScreen extends JPanel implements ActionListener{
	private static final long serialVersionUID = 3798035225065059125L;

	private static final int NUM_STARS = 120;
	
	protected Window window;
	protected JLabel title;
	protected JPanel centerGrid;
	protected JButton prevScreen;
	protected JButton nextScreen;

	private double[] xpositions;
	private double[] ypositions;
	
	public TutorialScreen(Window w) {
		window = w;
		this.setOpaque(true);
		this.setBackground(Color.BLACK);
		this.setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weighty = 0;
		gbc.weightx = 0;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(20, 20, 20, 20);
		title = new JLabel("Tutorial");
		title.setOpaque(false);
		title.setForeground(Color.LIGHT_GRAY);
		title.setFont(title.getFont().deriveFont(24.0f));
		this.add(title, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weighty = 1;
		gbc.weightx = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(20, 20, 20, 20);
		centerGrid = new JPanel(new GridBagLayout());
		centerGrid.setOpaque(false);
		this.add(centerGrid, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.weighty = 0;
		gbc.insets = new Insets(20, 20, 20, 20);
		JPanel buttonPanel = new JPanel();
		prevScreen = new JButton("Back");
		nextScreen = new JButton("Next");
		prevScreen.addActionListener(this);
		nextScreen.addActionListener(this);
		buttonPanel.add(prevScreen);
		buttonPanel.add(nextScreen);
		buttonPanel.setOpaque(false);
		this.add(buttonPanel, gbc);
		
		xpositions = new double[TutorialScreen.NUM_STARS];
		ypositions = new double[TutorialScreen.NUM_STARS];
		for (int i = 0; i < TutorialScreen.NUM_STARS; ++i) {
			xpositions[i] = Math.random();
			ypositions[i] = Math.random();
		}
	}
	
	protected void addTutorialComponent(int i, Image image, String name, String description) {
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.gridx = 0;
		gbc.gridy = i;
		gbc.weightx = 0;
		gbc.weighty = 1;
		gbc.gridwidth = 1;
		centerGrid.add(Box.createRigidArea(new Dimension(20, 0)), gbc);
		
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.gridx = 1;
		gbc.weightx = 0;
		ImageIcon icon = new ImageIcon(image);
		centerGrid.add(new JLabel(icon), gbc);
		
		gbc.weightx = 0;
		gbc.gridx = 2;
		centerGrid.add(Box.createRigidArea(new Dimension(20, 0)), gbc);
		
		gbc.anchor = GridBagConstraints.WEST;
		gbc.weightx = 1;
		gbc.gridx = 3;
		JTextArea nameTextBox = new JTextArea(name);
		nameTextBox.setLineWrap(false);
		nameTextBox.setEditable(false);
		nameTextBox.setOpaque(false);
		nameTextBox.setForeground(Color.LIGHT_GRAY);
		centerGrid.add(nameTextBox, gbc);
		
		gbc.weightx = 0;
		gbc.gridx = 4;
		centerGrid.add(Box.createRigidArea(new Dimension(20, 0)), gbc);
		
		gbc.anchor = GridBagConstraints.WEST;
		gbc.weightx = 1;
		gbc.gridx = 5;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		JTextArea descriptionTextBox = new JTextArea(description);
		descriptionTextBox.setLineWrap(true);
		descriptionTextBox.setWrapStyleWord(true);
		descriptionTextBox.setEditable(false);
		descriptionTextBox.setOpaque(false);
		descriptionTextBox.setForeground(Color.LIGHT_GRAY);
		centerGrid.add(descriptionTextBox, gbc);
	}
	/*
	@Override
	public Component add(Component component) {
		if (JComponent.class.isAssignableFrom(component.getClass())) ((JComponent)component).setOpaque(false);
		return super.add(component);
	}
	*/
	
	@Override
	public void actionPerformed(ActionEvent ev) {
		if (ev.getSource() == prevScreen) {
			((CardLayout)this.getParent().getLayout()).next(this.getParent());
		} else if (ev.getSource() == nextScreen) {
			((CardLayout)this.getParent().getLayout()).previous(this.getParent());
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.WHITE);
		for (int i = 0; i < TutorialScreen.NUM_STARS; ++i) {
			g.fillRect((int)(xpositions[i]*this.getWidth()), (int)(ypositions[i]*this.getHeight()), 1, 1);
		}
	}

}
