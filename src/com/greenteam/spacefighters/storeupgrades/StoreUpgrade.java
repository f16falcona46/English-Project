package com.greenteam.spacefighters.storeupgrades;

import java.awt.Image;

import com.greenteam.spacefighters.entity.entityliving.starship.player.Player;

public abstract class StoreUpgrade {
	
	public Image icon() {
		return null;
	}
	
	public abstract void modifyPlayer(Player player);
	public abstract int cost();
}
