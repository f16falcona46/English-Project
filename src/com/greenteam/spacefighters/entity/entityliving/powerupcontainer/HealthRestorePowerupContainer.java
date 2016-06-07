package com.greenteam.spacefighters.entity.entityliving.powerupcontainer;

import com.greenteam.spacefighters.stage.Stage;

public class HealthRestorePowerupContainer extends PowerupContainer {

	public HealthRestorePowerupContainer(Stage s) {
		super(s);
		this.setTexture(PowerupContainer.getTexFromEnum(PowerupColor.RED));
		if (this.getTexture() != null) {
			couldLoadImage = true;
		} else {
			couldLoadImage = false;
		}
	}
	
	@Override
	public int getDamage() {
		return -25;
	}

	@Override
	public java.awt.Color noTextureColor() {
		return java.awt.Color.RED;
	}
	
}
