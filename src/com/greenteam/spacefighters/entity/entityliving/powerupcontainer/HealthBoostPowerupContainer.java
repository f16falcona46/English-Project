package com.greenteam.spacefighters.entity.entityliving.powerupcontainer;

import com.greenteam.spacefighters.entity.entityliving.starship.player.Player;
import com.greenteam.spacefighters.stage.Stage;

public class HealthBoostPowerupContainer extends PowerupContainer {

	public HealthBoostPowerupContainer(Stage s) {
		super(s);
		this.setTexture(PowerupContainer.getTexFromEnum(PowerupColor.GREEN));
		if (this.getTexture() != null) {
			couldLoadImage = true;
		} else {
			couldLoadImage = false;
		}
	}
	
	@Override
	public int getDamage() {
		return -20;
	}
	
	@Override
	protected void applyPowerup(Player pl) {
		pl.damage(this, this.getDamage());
		pl.setHealth(Math.min(pl.getMaxHealth() - this.getDamage(), pl.getHealth()));
	}

	@Override
	public java.awt.Color noTextureColor() {
		return java.awt.Color.GREEN;
	}

}
