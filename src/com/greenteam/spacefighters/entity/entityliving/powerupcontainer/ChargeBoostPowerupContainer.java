package com.greenteam.spacefighters.entity.entityliving.powerupcontainer;

import com.greenteam.spacefighters.stage.Stage;

public class ChargeBoostPowerupContainer extends PowerupContainer {

	public ChargeBoostPowerupContainer(Stage s) {
		super(s);
		this.setTexture(PowerupContainer.getTexFromEnum(PowerupColor.YELLOW));
		if (this.getTexture() != null) {
			couldLoadImage = true;
		} else {
			couldLoadImage = false;
		}
	}

	@Override
	public java.awt.Color noTextureColor() {
		return java.awt.Color.YELLOW;
	}

}
