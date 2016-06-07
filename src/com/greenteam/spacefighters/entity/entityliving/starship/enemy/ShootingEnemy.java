package com.greenteam.spacefighters.entity.entityliving.starship.enemy;

import com.greenteam.spacefighters.common.Vec2;
import com.greenteam.spacefighters.entity.entityliving.projectile.LinearProjectile;
import com.greenteam.spacefighters.entity.entityliving.projectile.Projectile;
import com.greenteam.spacefighters.stage.Stage;

public class ShootingEnemy extends Enemy {
	private static final int SHOOTING_INTERVAL = 800;
	private static final int PROJECTILE_SPEED = 550;
	private static final double SPAWNDIST = 400.0D;
	private static final double SPEED = 300.0D;

	private int time;
	private Vec2 randpos;
	
	public ShootingEnemy(Stage s) {
		super(s, 1, 1, 0, 0);
		time = 0;
		this.setPosition(randSpawnPos(s.getPlayer(), SPAWNDIST));
		this.setOrientation(new Vec2(0,-1));
		
		randpos = randSpawnPos(s.getPlayer(), 0);
		this.setTexture(Enemy.getTexFromEnum(EnemyShipColor.RED));		
		if (this.getTexture() != null) {
			couldLoadImage = true;
			this.width = this.getTexture().getWidth(null);
			this.height = this.getTexture().getHeight(null);
		} else {
			couldLoadImage = false;
			this.width = 20;
			this.height = 60;
		}
	}
	
	@Override
	public double getRadius() {
		return 30.0D;
	}
	
	@Override
	public void update(int ms) {
		super.update(ms);
		Stage stage = this.getStage();
		time += ms;
		if (time > SHOOTING_INTERVAL) {
			time = 0;
			Vec2 vectorToTarget = stage.getPlayer().getPosition().subtract(this.getPosition()).normalize().scale(PROJECTILE_SPEED);
			Projectile proj = new LinearProjectile(stage, 1, 3, this.getPosition(), vectorToTarget, this);
			stage.add(proj);
		}
		if (getPosition().distance(randpos) < 5)
			randpos = randSpawnPos(this.getStage().getPlayer(), 0);
		this.setVelocity(randpos.subtract(this.getPosition()).normalize().scale(SPEED));
		this.setOrientation(this.getVelocity());
	}

	@Override
	public Class<?> getSourceClass() {
		return Enemy.class;
	}

	@Override
	public void fire(int type) {}
	
	@Override
	public int getPointValue() {
		return 100;
	}

	@Override
	public java.awt.Color noTextureColor() {
		return java.awt.Color.RED;
	}

}
