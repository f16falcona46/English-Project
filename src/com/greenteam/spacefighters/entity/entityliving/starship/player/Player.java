package com.greenteam.spacefighters.entity.entityliving.starship.player;

import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.imageio.ImageIO;

import com.greenteam.spacefighters.common.Vec2;
import com.greenteam.spacefighters.entity.Entity;
import com.greenteam.spacefighters.entity.entityliving.EntityLiving;
import com.greenteam.spacefighters.entity.entityliving.Explosion;
import com.greenteam.spacefighters.entity.entityliving.obstacle.Obstacle;
import com.greenteam.spacefighters.entity.entityliving.powerup.ChainBeamPowerup;
import com.greenteam.spacefighters.entity.entityliving.powerup.Powerup;
import com.greenteam.spacefighters.entity.entityliving.powerupcontainer.ChargeBoostPowerupContainer;
import com.greenteam.spacefighters.entity.entityliving.powerupcontainer.PowerupContainer;
import com.greenteam.spacefighters.entity.entityliving.projectile.ExplosiveProjectile;
import com.greenteam.spacefighters.entity.entityliving.projectile.HomingProjectile;
import com.greenteam.spacefighters.entity.entityliving.projectile.LinearProjectile;
import com.greenteam.spacefighters.entity.entityliving.projectile.Projectile;
import com.greenteam.spacefighters.entity.entityliving.starship.Starship;
import com.greenteam.spacefighters.entity.entityliving.starship.enemy.Enemy;
import com.greenteam.spacefighters.stage.Stage;

public class Player extends Starship {
	private static final int DEFAULTARMORLEVEL = 0;
	private static final int DEFAULTWEAPONRYLEVEL= 0;
	private static final int DEFAULTWEAPONRYHEALTH = 2;
	private static final int FULLCHARGE = 500;
	public static final int MOVEMENT_SPEED = 500;
	private static final int PLAYER_PROJECTILE_SPEED = 1200;
	private static final int MISSILE_SPEED = 1000;
	private static final int EXPLOSIVE_SPEED = 400;
	private static final int HEALTH_REGEN_TIME = 1600;
	private static final int GUN_TO_MISSILE_RATIO = 5;
	private static final int MISSILE_SPREAD_COUNT = 6;
	private static final int DEFAULT_LIVES = 3;
	
	//platforming code
	private static final double DRAG_COEFFICIENT = 0.01;
	private static final double DRAG_X = 0.1;
	private static final double DRAG_Y = 0.00002;
	private static final double ANIMATION_MOVEMENT_INCREMENT = 40;
	
	private int timetofiremissile;
	private int chargeLevel;
	private int width;
	private int height;
	private boolean couldLoadImage;
	private PlayerShipColor color;
	private int maxHealth;
	private int time;
	private int score;
	private int money;
	private HashSet<Powerup> powerups;
	private int lives;
	private MovementState movementState;
	private Direction dir;
	private boolean jumpButtonPressed;
	private double lastAnimationMovement;
	private double lastY;

	public Player(Stage s, int maxHealth, int health, PlayerShipColor color) {
		super(s, maxHealth, health, DEFAULTARMORLEVEL, DEFAULTWEAPONRYLEVEL);
		this.maxHealth = health;
		time = 0;
		timetofiremissile = GUN_TO_MISSILE_RATIO;
		money = 0;
		score = 0;
		this.setColor(color);
		chargeLevel = FULLCHARGE;
		powerups = new HashSet<Powerup>();
		lives = DEFAULT_LIVES;
	}
	
	public void reset() {
		this.setFullHealth();
		this.setFullCharge();
		this.setPosition(new Vec2(Stage.WIDTH / 2 , Stage.HEIGHT / 2));
		this.setOrientation(new Vec2(0,1));
		this.removeAllPowuerups();
	}
	
	public void setColor(PlayerShipColor color) {
		this.color = color;
		this.setTexture(Player.getTexFromEnum(color));
		if (this.getTexture() != null) {
			this.width = this.getTexture().getWidth(null);
			this.height = this.getTexture().getHeight(null);
			couldLoadImage = true;
		} else {
			couldLoadImage = false;
			this.width = 20;
			this.height = 30;
		}
	}
	
	public PlayerShipColor getColor() {
		return color;
	}
	
	public void setCharge(int charge) {
		this.chargeLevel = charge;
	}
	
	public void setFullHealth() {
		this.setHealth(maxHealth);
	}
	
	public void setFullCharge() {
		this.setCharge(FULLCHARGE);
	}
	
	public int getLives() {
		return lives;
	}

	@Override
	public void render(Graphics g) {
		Vec2 pos = this.getPosition();
		if (couldLoadImage) {
			double angle = this.getOrientation().angle();
			double imagemidx = this.getTexture().getWidth(null)/2;
			double imagemidy = this.getTexture().getHeight(null)/2;
			AffineTransform tf = AffineTransform.getRotateInstance(angle, imagemidx, imagemidy);
			AffineTransformOp op = new AffineTransformOp(tf, AffineTransformOp.TYPE_BILINEAR);
			g.drawImage(op.filter((BufferedImage)this.getTexture(), null), (int)(pos.getX()-imagemidx), (int)(pos.getY()-imagemidy), null);
		}
		else {
			g.setColor(noTextureColor(color));
			g.fillRect((int)pos.getX(), (int)pos.getY(), width, height);
		}
	}
	
	@Override
	public void update(int ms) {
		super.update(ms);
		time += ms;
		
		if (this.getPosition().getX() > Stage.WIDTH) {
			this.getPosition().setX(Stage.WIDTH);
		}
		if (this.getPosition().getX() < 0) {
			this.getPosition().setX(0);
		}
		if (this.getPosition().getY() > Stage.HEIGHT) {
			this.getPosition().setY(Stage.HEIGHT);
			this.setVelocity(this.getVelocity().multiply(new Vec2(1,0)));
		}
		if (this.getPosition().getY() < 0) {
			this.getPosition().setY(0);
			this.setVelocity(this.getVelocity().multiply(new Vec2(1,0)));
		}
		
		double deltaY = this.getPosition().getY() - lastY;
		lastY = this.getPosition().getY();
		
		if (deltaY < 0) {
			this.movementState = MovementState.JUMPUP;
		}
		else if (deltaY > 0) {
			this.movementState = MovementState.JUMPFALL;
		}
		else {
			if (this.getVelocity().getX() == 0) {
				this.movementState = MovementState.STATIONARY;
			}
			else {
				if (Math.abs(this.getPosition().getX() - lastAnimationMovement) > ANIMATION_MOVEMENT_INCREMENT) {
					lastAnimationMovement = this.getPosition().getX();
					switch (this.movementState) {
					case HORIZ1:
						this.movementState = MovementState.HORIZ2;
						break;
					case HORIZ2:
						this.movementState = MovementState.HORIZ3;
						break;
					case HORIZ3:
						this.movementState = MovementState.HORIZ1;
						break;
					default:
						this.movementState = MovementState.HORIZ1;
						break;
					}
				}
			}
		}
		
		if (this.getVelocity().getX() > 0) {
			this.dir = Direction.RIGHT;
		}
		else if (this.getVelocity().getX() < 0) {
			this.dir = Direction.LEFT;
		}
		
		if (jumpButtonPressed && ((movementState == MovementState.JUMPUP) || (movementState == MovementState.JUMPFALL))) {
			this.getAcceleration().setY(Stage.GRAVITY/3);
		}
		else {
			this.getAcceleration().setY(Stage.GRAVITY);
		}
		
		if (jumpButtonPressed && (movementState != MovementState.JUMPFALL) && (movementState != MovementState.JUMPUP)) {
			System.out.println(this.getVelocity().getY()-Stage.PLAYER_JUMP_VELOCITY);
			this.getVelocity().setY(this.getVelocity().getY()-Stage.PLAYER_JUMP_VELOCITY);
		}
		
		//System.out.println(movementState);
		
		if (time > HEALTH_REGEN_TIME) {
			if (this.getHealth() < this.getMaxHealth()) {
				this.setHealth(this.getHealth()+1);
			}
			time = 0;
		}
	    for (CopyOnWriteArrayList<Entity> array : this.getStage().getEntities().values()) {
	    	for (Entity e : array) {
	    		if (e == this) continue;
	    		if (this.overlaps(e) &&
	    			(Obstacle.class.isAssignableFrom(e.getSourceClass()) ||
	    			 Enemy.class.isAssignableFrom(e.getSourceClass()) 	||
	    			 PowerupContainer.class.isAssignableFrom(e.getSourceClass())) &&
	    			e instanceof EntityLiving && !((EntityLiving)e).isDead()) {
	    			((EntityLiving)e).damage(this, this.getDamage());
	    			boolean augmentChargePowerup = e instanceof ChargeBoostPowerupContainer;
	    			if (augmentChargePowerup) {
	    				chargeLevel = Math.min(chargeLevel + Player.FULLCHARGE, 2 * Player.FULLCHARGE);
	    			}
				}
			}
		}
		boolean hasAugmentedCharge = this.getCharge() > this.getMaxCharge();
		if (!hasAugmentedCharge) {
			chargeLevel += ms/5;
			if (chargeLevel > Player.FULLCHARGE) {
				chargeLevel = Player.FULLCHARGE;
			}
		}
		//this.setAcceleration(this.getAcceleration().subtract(this.getVelocity().scale(DRAG_COEFFICIENT)));
		double speedY = getVelocity().getY();
		double dragY = speedY * speedY * DRAG_Y * Math.signum(speedY);
		this.getVelocity().setY(this.getVelocity().getY() - dragY);
		
		double speedX = getVelocity().getX();
		double dragX = Math.abs(speedX) * DRAG_X * Math.signum(speedX);
		this.getVelocity().setX(this.getVelocity().getX() - dragX);
		if (this.getVelocity().magnitude() < 30) this.setVelocity(Vec2.ZERO);
		
		
	}

	@Override
	public Class<?> getSourceClass() {
		return this.getClass();
	}
	
	@Override
	public double getRadius() {
		return 20;
	}

	@Override
	public void fire(int type) {
		Stage stage = this.getStage();
		int damage = 10 * (getWeaponryMultiplier() + 1);
		Vec2 playerVel = this.getVelocity();
		switch(type) {
			case 0 :
			{
				if (chargeLevel >= LinearProjectile.getEnergyCost()) {
					--timetofiremissile;
					Projectile proj = new LinearProjectile(stage, DEFAULTWEAPONRYHEALTH, damage, this.getPosition(), getOrientation().scale(PLAYER_PROJECTILE_SPEED).multiply(new Vec2(1, -1)).add(playerVel), this);
					stage.add(proj);
					if (timetofiremissile == 0) {
						proj = new HomingProjectile(stage, DEFAULTWEAPONRYHEALTH, damage, MISSILE_SPEED, this.getPosition(), getOrientation().scale(MISSILE_SPEED).multiply(new Vec2(1, -1)).add(playerVel), this);
						stage.add(proj);
						timetofiremissile = GUN_TO_MISSILE_RATIO;
					}
					chargeLevel -= LinearProjectile.getEnergyCost();
				}
			}
			break;
			case 1 :
			{
				if (chargeLevel >= HomingProjectile.getEnergyCost()*MISSILE_SPREAD_COUNT) {
					for (int i = 0; i < MISSILE_SPREAD_COUNT; ++i) {
						Projectile proj = new HomingProjectile(stage, DEFAULTWEAPONRYHEALTH, damage, MISSILE_SPEED, this.getPosition(), getOrientation().scale(MISSILE_SPEED).multiply(new Vec2(1, -1)).rotate(new Vec2(0,0), (i-(double)MISSILE_SPREAD_COUNT/2)/MISSILE_SPREAD_COUNT*2*Math.PI).add(playerVel), this);
						stage.add(proj);
					}
					chargeLevel -= HomingProjectile.getEnergyCost()*MISSILE_SPREAD_COUNT;
				}
			}
			break;
			case 2 :
			{
				if (chargeLevel >= ExplosiveProjectile.getEnergyCost()) {
					Projectile proj = new ExplosiveProjectile(stage, DEFAULTWEAPONRYHEALTH, damage, this.getPosition(), getOrientation().scale(EXPLOSIVE_SPEED).multiply(new Vec2(1, -1)).add(playerVel), this);
					stage.add(proj);
					chargeLevel -= ExplosiveProjectile.getEnergyCost();
				}
			}
			break;
			case 3 :
			{
				if (chargeLevel >= ExplosiveProjectile.getEnergyCost()) {
					Projectile proj = new ExplosiveProjectile(stage, DEFAULTWEAPONRYHEALTH, damage, this.getPosition(), Vec2.ZERO, this);
					stage.add(proj);
					chargeLevel -= ExplosiveProjectile.getEnergyCost();
				}
			}
			break;
			case 4 :
			{
				if (hasPowerup(ChainBeamPowerup.class)) {
					((ChainBeamPowerup)getPowerup(ChainBeamPowerup.class)).fire();
				}
			}
			break;
			default :
				break;
		}
	}
	
	@Override
	public void uponDeath() {
		Stage stage = this.getStage();
		Explosion e = new Explosion(this.getStage(), this.getPosition(), 100);
		stage.add(e);
		lives--;
		if (lives <=0) {
			stage.gameOver();
			super.uponDeath();
		} else {
			stage.getLevelLoader().handleDeath();
		}
	}
	
	public void addPowerup(Powerup p) {
		for (Powerup powerup : powerups) {
			if (p.getClass().isInstance(powerup)) {
				powerup.resetTime();
				return;
			}
		}
		powerups.add(p);
		this.getStage().add(p);
	}
	
	public Powerup getPowerup(Class<?> cl) {
		for (Powerup p : powerups)
			if (cl.isInstance(p))
				return p;
		return null;
	}
	
	public void removePowerup(Powerup p) {
		powerups.remove(p);
		p.remove();
	}
	
	public void removeAllPowuerups() {
		powerups.clear();
	}
	
	public boolean hasPowerup(Class<?> cl) {
		return getPowerup(cl) != null;
	}
	
	public void setMaxHealth(int max) {
		this.maxHealth = max;
	}
	
	public int getMaxHealth() {
		return maxHealth;
	}
	
	public int getCharge() {
		return chargeLevel;
	}
	
	public int getMaxCharge() {
		return FULLCHARGE;
	}
	
	@Override
	public int getDamage() {
		return 50;
	}
	
	public static BufferedImage getTexFromEnum(PlayerShipColor color) {
		try {
			switch(color) {
				case RED:
					return ImageIO.read(Player.class.getResource("/com/greenteam/spacefighters/assets/spaceship-0.png"));
				case BLUE:
					return ImageIO.read(Player.class.getResource("/com/greenteam/spacefighters/assets/spaceship-1.png"));
				case GREEN:
					return ImageIO.read(Player.class.getResource("/com/greenteam/spacefighters/assets/spaceship-2.png"));
				case YELLOW:
					return ImageIO.read(Player.class.getResource("/com/greenteam/spacefighters/assets/spaceship-3.png"));
				default :
					return ImageIO.read(Player.class.getResource("/com/greenteam/spacefighters/assets/spaceship-0.png"));
			}
		} catch(Exception e) {
			return null;
		}
	}
	
	public static java.awt.Color noTextureColor(PlayerShipColor color) {
		try {
			switch(color) {
				case RED:
					return java.awt.Color.RED;
				case BLUE:
					return java.awt.Color.BLUE;
				case GREEN:
					return java.awt.Color.GREEN;
				case YELLOW:
					return java.awt.Color.YELLOW;
				default :
					return java.awt.Color.RED;
			}
		} catch(Exception e) {
			return null;
		}
	}
	
	public enum PlayerShipColor {
		RED, BLUE, GREEN, YELLOW
	}
	
	public int getScore() {
		return score;
	}
	
	public void setScore(int score) {
		this.score = score;
	}
	
	public int getMoney() {
		return money;
	}
	
	public void setMoney(int money) {
		this.money = money;
	}

	public void setLives(int lives) {
		this.lives = lives;
	}
	
	public void setJumpButtonState(boolean state) {
		this.jumpButtonPressed = state;
	}
	
	public boolean getJumpButtonState() {
		return this.jumpButtonPressed;
	}
}

enum MovementState {
	STATIONARY, HORIZ1, HORIZ2, HORIZ3, JUMPUP, JUMPFALL
}

enum Direction {
	LEFT, RIGHT
}
