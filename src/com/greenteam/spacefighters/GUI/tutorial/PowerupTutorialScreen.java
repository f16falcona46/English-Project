package com.greenteam.spacefighters.GUI.tutorial;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import com.greenteam.spacefighters.GUI.Window;

public class PowerupTutorialScreen extends TutorialScreen {
	private static final long serialVersionUID = 7246934844521923388L;
	private static final String[] infoName = {
		"Health Restore-",
		"Health Boost-",
		"Charge Boost-",
		"Force Field-",
		"Chain Beam-"
	};
	private static final String[] infoDescription = {
		"Restores 25 health to player.",
		"Restores 20 health to player, augmenting player's health when possible.",
		"Temporarily increases player's charge level.",
		"Creates a forcefield around the player for 10 seconds.",
		"Seeks out and destroys nearby enemies."
	};
	
	public PowerupTutorialScreen(Window w) {
		super(w);
		
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		if (ev.getSource() == prevScreen) {
			((CardLayout)this.getParent().getLayout()).next(this.getParent());
		} else if (ev.getSource() == nextScreen) {
			((CardLayout)this.getParent().getLayout()).show(window.getContentPane(), Window.TITLE_SCREEN);
		}
	}

}
