package com.greenteam.spacefighters.storeupgrades;

import com.greenteam.spacefighters.entity.entityliving.starship.player.Player;

public class DoNothingUpgrade extends StoreUpgrade {

	@Override
	public void modifyPlayer(Player player) {
		//do nothing
	}
	
	@Override
	public String toString() {
		return "Upgrade that does nothing.";
	}

	@Override
	public int cost() {
		return 10;
	}
}
