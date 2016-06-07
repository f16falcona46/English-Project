package com.greenteam.spacefighters.entity.entityliving.starship.enemy;

import com.greenteam.spacefighters.common.Vec2;
import com.greenteam.spacefighters.stage.Stage;

public class TestEnemy extends Enemy {
	private static final double SPAWNDIST = 400.0D;
	
	public TestEnemy(Stage s) {
		super(s, 1, 1, 0, 0);
		this.setPosition(randSpawnPos(s.getPlayer(), SPAWNDIST));
		this.setVelocity(new Vec2(-440,200));
		
		this.setTexture(Enemy.getTexFromEnum(EnemyShipColor.GREEN));		
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
		this.setOrientation(this.getVelocity());
		if ((this.getPosition().getX() + this.getRadius() >= Stage.WIDTH &&
				 this.getVelocity().getX() > 0) ||
				(this.getPosition().getX() - this.getRadius() < 0) &&
				 this.getVelocity().getX() < 0){
				this.getVelocity().setX(this.getVelocity().getX() * -1);
			}
		if ((this.getPosition().getY() + this.getRadius() >= Stage.HEIGHT &&
			 this.getVelocity().getY() > 0) ||
			(this.getPosition().getY() - this.getRadius() < 0) &&
		 	 this.getVelocity().getY() < 0){
			this.getVelocity().setY(this.getVelocity().getY() * -1);
		}
	}

	@Override
	public Class<?> getSourceClass() {
		return Enemy.class;
	}

	@Override
	public void fire(int type) {}
	
	@Override
	public int getPointValue() {
		return 25;
	}

	@Override
	public java.awt.Color noTextureColor() {
		return java.awt.Color.GREEN;
	}

}
