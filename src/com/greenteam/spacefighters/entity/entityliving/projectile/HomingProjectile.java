package com.greenteam.spacefighters.entity.entityliving.projectile;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;

import com.greenteam.spacefighters.common.Vec2;
import com.greenteam.spacefighters.entity.Entity;
import com.greenteam.spacefighters.entity.entityliving.EntityLiving;
import com.greenteam.spacefighters.entity.entityliving.starship.enemy.Enemy;
import com.greenteam.spacefighters.stage.Stage;

public class HomingProjectile extends Projectile {
	private static final int INIT_DELAY = 50;
	private static final double TRAVELDISTANCE = 1800;
	private static final double BEGINFADE = 100;
	
	private double speed;
	private Entity target;
	private int startTrackDelay;
	private double remainingDistance;
	
	public HomingProjectile(Stage s, int health, int damage, double maxSpeed, Vec2 position, Vec2 velocity, Entity source) {
		super(s, health, damage, position, velocity, source);
		speed = maxSpeed;
		target = null;
		startTrackDelay = INIT_DELAY;
		remainingDistance = TRAVELDISTANCE;
		this.setTexture(Projectile.getTexFromEnum(ProjectileColor.BLUE));

	}
	
	@Override
	public void render(Graphics g) {
		if (remainingDistance <= BEGINFADE) {
		float opacity = (remainingDistance < 0 ? 0 : (float)remainingDistance) / (float)BEGINFADE;
		opacity = (float)Math.pow(opacity, 0.25);
		Composite oldComposite = ((Graphics2D)g).getComposite();
		((Graphics2D)g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
		super.render(g);
		((Graphics2D)g).setComposite(oldComposite);
		} else {
			super.render(g);
		}
	}
	
	@Override
	public void update(int ms) {
		super.update(ms);
		Stage s = this.getStage();
		startTrackDelay -= ms;
		if (startTrackDelay <= 0) {
			remainingDistance -= this.getVelocity().magnitude() * ms / 1000.0;
			if (remainingDistance >= 0) {
				if (target != null) {
					if (((EntityLiving)target).getHealth() <= 0) {
						target = s.getNearestEntity(this, Enemy.class);
					}
				}
				if (target != null) {
					Vec2 vectorToTarget = target.getPosition().subtract(this.getPosition()).normalize().scale(speed);
					this.setVelocity(vectorToTarget);
				}
				if (target == null) {
					target = s.getNearestEntity(this, Enemy.class);
				}
			} else {
				diedDueToOutofRange = true;
				this.setHealth(0);
			}
		}
	}
	
	public static int getEnergyCost() {
		return 50;
	}
}
