package com.greenteam.spacefighters.entity.entityliving.starship.enemy;

import com.greenteam.spacefighters.common.Vec2;
import com.greenteam.spacefighters.entity.entityliving.projectile.LinearProjectile;
import com.greenteam.spacefighters.entity.entityliving.projectile.Projectile;
import com.greenteam.spacefighters.entity.entityliving.starship.player.Player;
import com.greenteam.spacefighters.stage.Stage;

public class TrackerEnemy extends Enemy {
	private static final double ACCELERATION = 450;
	private static final double DRAG = 0.8 / 40000.0;
	private static final int DEFAULTARMORLEVEL = 0;
	private static final int DEFAULTWEAPONRYLEVEL= 0;
	private static final int DEFAULTWEAPONRYHEALTH = 1;
	private static final int FIREDRAIN = 1500;
	private static final int FULLCHARGE = 5000;
	private static final int PROJECTILESPEED = 550;
	private static final double SPAWNDIST = 400.0D;
	
	private int chargeLevel;
	
	public TrackerEnemy(Stage s) {
		super(s, 40, 40, DEFAULTARMORLEVEL, DEFAULTWEAPONRYLEVEL);
		this.setPosition(randSpawnPos(s.getPlayer(), SPAWNDIST));
		this.setVelocity(new Vec2(1000*Math.random()-500,200));
		this.setOrientation(new Vec2(0, -1));
		this.setTexture(Enemy.getTexFromEnum(EnemyShipColor.BLACK));		
		if (this.getTexture() != null) {
			couldLoadImage = true;
			this.width = this.getTexture().getWidth(null);
			this.height = this.getTexture().getHeight(null);
		} else {
			couldLoadImage = false;
			this.width = 20;
			this.height = 60;
		}
		chargeLevel = FULLCHARGE;
	}

	@Override
	public void update(int ms) {
		super.update(ms);
		Stage stage = this.getStage();
		Player target = (Player) stage.getNearestEntity(this, Player.class);
		if (target != null) {
			double dist = getPosition().distance(target.getPosition()); 
			
			if (dist < 10000.0D)
				fire(0);
			double speed = getVelocity().magnitude();
			double drag = speed * speed * DRAG;
			Vec2 direction = target.getPosition().subtract(getPosition()).normalize();
			setAcceleration(direction.scale(ACCELERATION).subtract(getVelocity().scale(drag)));
			this.setOrientation(direction);
		} else {
			double speed = getVelocity().magnitude();
			double drag = speed * speed * DRAG;
			setAcceleration(new Vec2(0, -1).scale(ACCELERATION).subtract(getVelocity().scale(drag)));
		}
		if (this.getPosition().getX() > Stage.WIDTH) {
			this.getPosition().setX(Stage.WIDTH);
		}
		if (this.getPosition().getX() < 0) {
			this.getPosition().setX(0);
		}
		if (this.getPosition().getY() > Stage.HEIGHT) {
			this.getPosition().setY(Stage.HEIGHT);
		}
		if (this.getPosition().getY() < 0) {
			this.getPosition().setY(0);
		}
		chargeLevel += ms;
	}

	@Override
	public void fire(int mode) {
		Stage stage = this.getStage();
		if (chargeLevel >= FIREDRAIN) {
			int damage = 10 * (getWeaponryMultiplier() + 1);
			Projectile proj = new LinearProjectile(stage, DEFAULTWEAPONRYHEALTH, damage/2, this.getPosition(), this.getOrientation().scale(PROJECTILESPEED), this);
			stage.add(proj);
			proj = new LinearProjectile(stage, DEFAULTWEAPONRYHEALTH, damage/2, this.getPosition(), this.getOrientation().scale(PROJECTILESPEED).rotate(new Vec2(0,0), -0.04), this);
			stage.add(proj);
			proj = new LinearProjectile(stage, DEFAULTWEAPONRYHEALTH, damage/2, this.getPosition(), this.getOrientation().scale(PROJECTILESPEED).rotate(new Vec2(0,0), 0.04), this);
			stage.add(proj);
			chargeLevel -= FIREDRAIN;
		}
	}

	@Override
	public Class<?> getSourceClass() {
		return Enemy.class;
	}
	
	@Override
	public double getRadius() {
		return 30.0D;
	}

	@Override
	public int getPointValue() {
		return 75;
	}

	@Override
	public java.awt.Color noTextureColor() {
		return java.awt.Color.BLACK;
	}

}
