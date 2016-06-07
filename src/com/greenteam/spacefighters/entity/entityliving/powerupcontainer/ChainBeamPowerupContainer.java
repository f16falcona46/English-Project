package com.greenteam.spacefighters.entity.entityliving.powerupcontainer;

import com.greenteam.spacefighters.entity.entityliving.powerup.ChainBeamPowerup;
import com.greenteam.spacefighters.entity.entityliving.starship.player.Player;
import com.greenteam.spacefighters.stage.Stage;

public class ChainBeamPowerupContainer extends PowerupContainer {

	public ChainBeamPowerupContainer(Stage s) {
		super(s);
		this.setTexture(PowerupContainer.getTexFromEnum(PowerupColor.PURPLE));
		if (this.getTexture() != null) {
			couldLoadImage = true;
		} else {
			couldLoadImage = false;
		}
	}
	
	@Override
	protected void applyPowerup(Player pl) {
		new ChainBeamPowerup(this.getStage(), this.getStage().getPlayer());
	}

	@Override
	public java.awt.Color noTextureColor() {
		return java.awt.Color.MAGENTA;
	}

}
