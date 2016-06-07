package com.greenteam.spacefighters.entity.entityliving.powerup;

import com.greenteam.spacefighters.common.Vec2;
import com.greenteam.spacefighters.entity.Entity;
import com.greenteam.spacefighters.entity.entityliving.EntityLiving;
import com.greenteam.spacefighters.entity.entityliving.starship.player.Player;
import com.greenteam.spacefighters.stage.Stage;

public abstract class Powerup extends EntityLiving {
	private static final int DURATION = 10000;
	
	protected Player player;
	protected int timeRemaining;

	public Powerup(Stage s, Player pl) {
		super(s, 1, 1);
		this.player = pl;
		timeRemaining = this.getDuration();
		this.setAcceleration(Vec2.ZERO);
		this.setVelocity(Vec2.ZERO);
		player.addPowerup(this);
	}

	public void resetTime() {
		timeRemaining = this.getDuration();
	}
	
	@Override
	public void update(int ms) {
		super.update(ms);
		timeRemaining -= ms;
		if (timeRemaining <= 0)
			player.removePowerup(this);
	}

	protected int getDuration() {
		return DURATION;
	}
	
	public double getTimerFraction() {
		return timeRemaining / this.getDuration();
	}
	
	@Override
	public int getDefaultLayer() {
		return 2;
	}

	@Override
	public Class<?> getSourceClass() {
		return Powerup.class;
	}
	
	@Override
	public Entity getSource() {
		return player;
	}
	
}
