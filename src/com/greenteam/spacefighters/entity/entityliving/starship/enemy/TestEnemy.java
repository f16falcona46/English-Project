package com.greenteam.spacefighters.entity.entityliving.starship.enemy;

import com.greenteam.spacefighters.common.Vec2;
import com.greenteam.spacefighters.stage.Stage;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.imageio.ImageIO;

import com.greenteam.spacefighters.common.BoundRect;
import com.greenteam.spacefighters.common.Color;
import com.greenteam.spacefighters.common.RectCollisionSide;
import com.greenteam.spacefighters.common.Vec2;
import com.greenteam.spacefighters.entity.Entity;
import com.greenteam.spacefighters.entity.entityliving.EntityLiving;
import com.greenteam.spacefighters.entity.entityliving.Explosion;
import com.greenteam.spacefighters.entity.entityliving.obstacle.Obstacle;
import com.greenteam.spacefighters.entity.entityliving.projectile.ExplosiveProjectile;
import com.greenteam.spacefighters.entity.entityliving.projectile.HomingProjectile;
import com.greenteam.spacefighters.entity.entityliving.projectile.LinearProjectile;
import com.greenteam.spacefighters.entity.entityliving.projectile.Projectile;
import com.greenteam.spacefighters.entity.entityliving.starship.Starship;
import com.greenteam.spacefighters.entity.entityliving.starship.enemy.Enemy;
import com.greenteam.spacefighters.stage.Stage;

public class TestEnemy extends Enemy {
	private static final double SPAWNDIST = 400.0D;
	
	public TestEnemy(Stage s) {
		super(s, Integer.MAX_VALUE, Integer.MAX_VALUE, 0, 0);
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
		time = 0;
		timetofiremissile = GUN_TO_MISSILE_RATIO;
		score = 0;
		chargeLevel = FULLCHARGE;
		lives = DEFAULT_LIVES;
		loadImages();
		this.setBoundingBox(new BoundRect(this.getPosition().getX(), this.getPosition().getY(), BOUNDING_BOX_WIDTH, BOUNDING_BOX_HEIGHT));

	}
	
	public double getRadius() {
		return 30.0D;
	}

	@Override
	public Class<?> getSourceClass() {
		return Enemy.class;
	}
	
	@Override
	public int getPointValue() {
		return 25;
	}

	@Override
	public java.awt.Color noTextureColor() {
		return java.awt.Color.GREEN;
	}

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
	private static final double DRAG_X = 0.2;
	private static final double DRAG_Y = 0.00002;
	private static final double ANIMATION_MOVEMENT_INCREMENT = 20;
	private static final double BOUNDING_BOX_WIDTH = 18;
	private static final double BOUNDING_BOX_HEIGHT = 36;
	
	private int timetofiremissile;
	private int chargeLevel;
	private int width;
	private int height;
	private boolean couldLoadImage;
	private int maxHealth;
	private int time;
	private int score;
	private int lives;
	private MovementState movementState;
	private Direction dir;
	private boolean jumpButtonPressed;
	private double lastAnimationMovement;
	private double lastY;
	private BoundRect boundingBox;
	private RectCollisionSide collisionState;
	
	private Map<MovementState, BufferedImage> textures;

	@Override
	public void render(Graphics g) {
		Vec2 pos = this.getPosition();
		if (couldLoadImage) {
			int x = (int)this.getBoundingBox().getX();
			int y = (int)this.getBoundingBox().getY();
			int w = (int)this.getBoundingBox().getWidth();
			int h = (int)this.getBoundingBox().getHeight();
			//g.fillRect(x-w/2, y-h/2, w, h);
			BufferedImage tex = textures.get(movementState);
			
			AffineTransform tx = null;
			if (dir == Direction.LEFT) {
				tx = AffineTransform.getScaleInstance(-1, 1);
				tx.translate(-w, 0);
				AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
				tex = op.filter(tex, null);
			}
			g.drawImage(tex, x-w/2, y-h/2, w, h, null);
		}
		else {
			g.setColor(java.awt.Color.RED);
			g.fillRect((int)pos.getX(), (int)pos.getY(), width, height);
		}
	}
	
	@Override
	public void update(int ms) {
		Vec2 nextPos = this.getPosition().add(this.getVelocity().scale(((double)ms)/1000));
		canMove(nextPos);
		
		//System.out.println(this.collisionState);
		time += ms;
		
		switch (collisionState) {
		case LEFT:
		case RIGHT:
			this.getVelocity().setX(0);
			break;
		case TOP:
		case BOTTOM:
			this.getVelocity().setY(0);
			break;
		default:
			break;
		}
		
		if (this.getPosition().getX() > this.getStage().getCanvasWidth()) {
			this.getPosition().setX(this.getStage().getCanvasWidth());
		}
		if (this.getPosition().getX() < 0) {
			this.getPosition().setX(0);
		}
		if (this.getPosition().getY() > this.getStage().getCanvasHeight()) {
			this.getPosition().setY(this.getStage().getCanvasHeight());
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
		
		if (jumpButtonPressed && (collisionState == RectCollisionSide.BOTTOM)) {
			this.getVelocity().setY(this.getVelocity().getY()-Stage.PLAYER_JUMP_VELOCITY);
		}
		
	    for (CopyOnWriteArrayList<Entity> array : this.getStage().getEntities().values()) {
	    	for (Entity e : array) {
	    		if (e == this) continue;
	    		if (this.overlaps(e) &&
	    			(Obstacle.class.isAssignableFrom(e.getSourceClass()) ||
	    			 Enemy.class.isAssignableFrom(e.getSourceClass())) &&
	    			e instanceof EntityLiving && !((EntityLiving)e).isDead()) {
	    			((EntityLiving)e).damage(this, this.getDamage());
				}
			}
		}
		
		double speedY = getVelocity().getY();
		double dragY = speedY * speedY * DRAG_Y * Math.signum(speedY);
		this.getVelocity().setY(this.getVelocity().getY() - dragY);
		
		double speedX = getVelocity().getX();
		double dragX = Math.abs(speedX) * DRAG_X * Math.signum(speedX);
		this.getVelocity().setX(this.getVelocity().getX() - dragX);
		if (this.getVelocity().magnitude() < 30) this.setVelocity(Vec2.ZERO);
		
		super.update(ms);
	}
	
	public void loadImages() {
		try {
			textures = new HashMap<MovementState, BufferedImage>();
			for (MovementState state : MovementState.values()) {
				switch (state) {
				case STATIONARY:
				case HORIZ3:
					textures.put(state, ImageIO.read(TestEnemy.class.getResource("/com/greenteam/spacefighters/assets/gatsby/red1.png")));
					break;
				case HORIZ1:
					textures.put(state, ImageIO.read(TestEnemy.class.getResource("/com/greenteam/spacefighters/assets/gatsby/red2.png")));
					break;
				case HORIZ2:
					textures.put(state, ImageIO.read(TestEnemy.class.getResource("/com/greenteam/spacefighters/assets/gatsby/red3.png")));
					break;
				case JUMPUP:
					textures.put(state, ImageIO.read(TestEnemy.class.getResource("/com/greenteam/spacefighters/assets/gatsby/red5.png")));
					break;
				case JUMPFALL:
					textures.put(state, ImageIO.read(TestEnemy.class.getResource("/com/greenteam/spacefighters/assets/gatsby/red6.png")));
					break;
				}
			}
		} catch(Exception e) {
			return;
		}
	}

	public void setBoundingBox(BoundRect box) {
		this.boundingBox = box;
	}
	
	@Override
	public BoundRect getBoundingBox() {
		return this.boundingBox;
	}
	
	@Override
	public boolean canMove(Vec2 newPos) {
		BoundRect rect = new BoundRect(newPos.getX(), newPos.getY(), this.getBoundingBox().getWidth(), this.getBoundingBox().getHeight());
		for (CopyOnWriteArrayList<Entity> entities : this.getStage().getEntities().values()) {
			for (Entity e : entities) {
				if (e == this) continue;
				if (rect.intersects(e.getBoundingBox())) {
					if (Enemy.class.isAssignableFrom(e.getSourceClass())) {
						this.getStage().playerDied();
					}
					this.collisionState = rect.whichSideIntersected(e.getBoundingBox());
					return false;
				}
			}
		}
		this.collisionState = RectCollisionSide.NONE;
		return true;
	}

	@Override
	public void fire(int type) {
		//do nothing
	}
}

enum MovementState {
	STATIONARY, HORIZ1, HORIZ2, HORIZ3, JUMPUP, JUMPFALL
}

enum Direction {
	LEFT, RIGHT
}
