package com.greenteam.spacefighters.entity.entityliving.starship.enemy;

import com.greenteam.spacefighters.common.Vec2;
import com.greenteam.spacefighters.stage.Stage;

public class ErraticEnemy extends Enemy {
	private int time;
	private static final double SPAWNDIST = 400.0D;
	
	public ErraticEnemy(Stage s) {
		super(s, 1, 1, 0, 0);
		time = 0;
		this.setPosition(randSpawnPos(s.getPlayer(), SPAWNDIST));
		this.setVelocity(new Vec2(1000*Math.random()-500,200));
		
		this.setTexture(Enemy.getTexFromEnum(EnemyShipColor.BLUE));		
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
		time += ms;
		//this.setOrientation(this.getOrientation().rotate(new Vec2(0,0), null, 0.1));
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
		if (this.getPosition().getX() + this.getRadius() < Stage.WIDTH &&
			this.getPosition().getX() - this.getRadius() >= 0 &&
			this.getPosition().getY() + this.getRadius() < Stage.HEIGHT &&
			this.getPosition().getY() - this.getRadius() >= 0) {
			if (time > 400 * Math.random() + 300) {
				this.setVelocity(new Vec2(1000 * Math.random() - 500,
										  1000 * Math.random() - 500));
				time = 0;
			}
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
		return 50;
	}

	@Override
	public java.awt.Color noTextureColor() {
		return java.awt.Color.BLUE;
	}

}
