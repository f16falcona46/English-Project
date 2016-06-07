package com.greenteam.spacefighters.entity.entityliving.projectile;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import com.greenteam.spacefighters.common.Vec2;
import com.greenteam.spacefighters.entity.Entity;
import com.greenteam.spacefighters.stage.Stage;

public class LinearProjectile extends Projectile {
	private static final int DECAYCOUNTDOWN = 1000;
	private int decayCount;

	public LinearProjectile(Stage s, int health, int damage, Vec2 position, Vec2 velocity, Entity source) {
		super(s, health, damage, position, velocity, source);
		
		this.setTexture(Projectile.getTexFromEnum(ProjectileColor.YELLOW));
		
		decayCount = DECAYCOUNTDOWN;
	}

	public static int getEnergyCost() {
		return 20;
	}
	
	@Override
	public void render(Graphics g) {
		float opacity = (decayCount < 0 ? 0 : decayCount) / (float)DECAYCOUNTDOWN;
		opacity = (float)Math.pow(opacity, 0.25);
		Composite oldComposite = ((Graphics2D)g).getComposite();
		((Graphics2D)g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
		super.render(g);
		((Graphics2D)g).setComposite(oldComposite);
	}
	
	@Override
	public void update(int ms) {
		super.update(ms);
		if (decayCount < 0) {
			diedDueToOutofRange = true;
			this.setHealth(0);
		} else {
			decayCount -= ms;
		}
	}

}
