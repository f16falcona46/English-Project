package com.greenteam.spacefighters.entity.entityliving.starship.enemy;

import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.imageio.ImageIO;

import com.greenteam.spacefighters.common.Vec2;
import com.greenteam.spacefighters.entity.Entity;
import com.greenteam.spacefighters.entity.entityliving.EntityLiving;
import com.greenteam.spacefighters.entity.entityliving.Explosion;
import com.greenteam.spacefighters.entity.entityliving.obstacle.Obstacle;
import com.greenteam.spacefighters.entity.entityliving.starship.Starship;
import com.greenteam.spacefighters.entity.entityliving.starship.player.Player;
import com.greenteam.spacefighters.stage.Stage;

public abstract class Enemy extends Starship {
	protected boolean couldLoadImage;
	protected int width;
	protected int height;
	
	public Enemy(Stage s, int maxHealth, int health, int armorMultiplier, int weaponryMultiplier) {
		super(s, maxHealth, health, armorMultiplier, weaponryMultiplier);
	}
	
	public abstract java.awt.Color noTextureColor();

	@Override
	public void render(Graphics g) {
		Vec2 pos = this.getPosition();
		if (couldLoadImage) {
			double angle = this.getOrientation().multiply(new Vec2(1,-1)).angle();
			double imagemidx = this.getTexture().getWidth(null)/2;
			double imagemidy = this.getTexture().getHeight(null)/2;
			AffineTransform tf = AffineTransform.getRotateInstance(angle, imagemidx, imagemidy);
			AffineTransformOp op = new AffineTransformOp(tf, AffineTransformOp.TYPE_BILINEAR);
			g.drawImage(op.filter((BufferedImage)this.getTexture(), null), (int)(pos.getX()-imagemidx), (int)(pos.getY()-imagemidy), null);
		}
		else {
			g.setColor(this.noTextureColor());
			g.fillRect((int)pos.getX(), (int)pos.getY(), width, height);
		}
	}
	
	protected boolean isOppositeFaction(Entity e) {
		return (Player.class.isAssignableFrom(e.getSourceClass()) || Obstacle.class.isAssignableFrom(e.getSourceClass()));
	}

	@Override
	public void update(int ms) {
		super.update(ms);
	    for (CopyOnWriteArrayList<Entity> array : this.getStage().getEntities().values()) {
	    	for (Entity e : array) {
	    		if (e == this) continue;
	    		if (this.overlaps(e) &&
	    		   (e instanceof EntityLiving) &&
	    			!((EntityLiving)e).isDead() &&
	    			this.isOppositeFaction(e)) {
	    			((EntityLiving)e).damage(this, this.getDamage());
	    		}
			}
		}
	}
	
	@Override
	public void uponDeath() {
		Stage stage = this.getStage();
		Entity lastAttacker = this.getLastAttacker();
		if (lastAttacker != null && lastAttacker instanceof Player) {
			Player pl = ((Player)lastAttacker);
			pl.setScore(stage.getPlayer().getScore() + this.getPointValue());
			pl.setMoney(stage.getPlayer().getMoney() + this.getPointValue()/10);
		}
		Explosion e = new Explosion(this.getStage(), this.getPosition(), 100);
		this.getStage().add(e);
		super.uponDeath();
	}
	
	@Override
	public Class<?> getSourceClass() {
		return Enemy.class;
	}
	
	public static BufferedImage getTexFromEnum(EnemyShipColor color) {
		try {
			switch(color) {
				case GREEN:
					return ImageIO.read(Enemy.class.getResource("/com/greenteam/spacefighters/assets/enemy-0.png"));
				case BLUE:
					return ImageIO.read(Enemy.class.getResource("/com/greenteam/spacefighters/assets/enemy-1.png"));
				case RED:
					return ImageIO.read(Enemy.class.getResource("/com/greenteam/spacefighters/assets/enemy-2.png"));
				case BLACK:
					return ImageIO.read(Enemy.class.getResource("/com/greenteam/spacefighters/assets/enemy-3.png"));
				default :
					return ImageIO.read(Enemy.class.getResource("/com/greenteam/spacefighters/assets/enemy-0.png"));
			}
		} catch(Exception e) {
			return null;
		}
	}
	
	public enum EnemyShipColor {
		GREEN, BLUE, RED, BLACK
	}
	
}
