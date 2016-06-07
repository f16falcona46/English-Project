package com.greenteam.spacefighters.entity.entityliving;

import com.greenteam.spacefighters.entity.Entity;
import com.greenteam.spacefighters.stage.Stage;

public abstract class EntityLiving extends Entity {
	private int maxHealth;
	private int health;
	private Entity lastAttacker;

	public EntityLiving(Stage s, int maxHealth, int health) {
		super(s);
		this.maxHealth = maxHealth;
		this.health = health;
		this.lastAttacker = null;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}
	
	public boolean healthIsAugmented() {
		return this.getHealth() > this.getMaxHealth();
	}
	
	public int getDamage() {
		return 5; //is only a default, override if you want something different
	}
	
	public void damage(Entity attacker, int damage) {
			this.health -= damage;
			this.lastAttacker = attacker.getSource();
	}
	
	public int getMaxHealth() {
		return maxHealth;
	}
	
	public boolean isDead() {
		return health <= 0;
	}
	
	public void kill() {
		health = 0;
	}
	
	public Entity getLastAttacker() {
		return lastAttacker;
	}
	
	public void uponDeath() {
		this.getStage().remove(this);
	}
		

	@Override
	public void update(int ms) {
		super.update(ms);
		if (this.isDead()) {
			this.uponDeath();
		}
	}
	
}