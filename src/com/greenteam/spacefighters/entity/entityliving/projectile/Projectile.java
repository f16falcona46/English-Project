package com.greenteam.spacefighters.entity.entityliving.projectile;

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
import com.greenteam.spacefighters.entity.entityliving.starship.enemy.Enemy;
import com.greenteam.spacefighters.entity.entityliving.starship.player.Player;
import com.greenteam.spacefighters.stage.Stage;

public abstract class Projectile extends EntityLiving {
	private Entity source;
	private int damage;
	protected boolean diedDueToOutofRange = false;
	
	public Projectile(Stage s, int health, int damage, Vec2 position, Vec2 velocity, Entity source) {
		super(s, health, health);
		this.damage = damage;
		this.source = source;
		this.setVelocity(velocity);
		this.setPosition(position);
	}
	
	protected boolean isOppositeFaction(Entity e) {
		if (Enemy.class.isAssignableFrom(this.getSourceClass())) {
			if (!(e instanceof Projectile)) {
				if (Player.class.isAssignableFrom(e.getSourceClass())|| Obstacle.class.isAssignableFrom(e.getSourceClass())) return true;
			}
		}
		else if (Player.class.isAssignableFrom(this.getSourceClass())) {
			if (!(e instanceof Projectile)) {
				if (Enemy.class.isAssignableFrom(e.getSourceClass()) || Obstacle.class.isAssignableFrom(e.getSourceClass())) return true;
			}
		}
		else if (Obstacle.class.isAssignableFrom(this.getSourceClass())) {
			if (!(e instanceof Projectile)) {
				if (Enemy.class.isAssignableFrom(e.getSourceClass()) || Player.class.isAssignableFrom(e.getSourceClass())) return true;
			}
		}
		return false;
	}
	
	@Override
	public void update(int ms) {
		super.update(ms);
		if ((this.getPosition().getX() > Stage.WIDTH) ||
				(this.getPosition().getX() < 0) ||
				(this.getPosition().getY() > Stage.HEIGHT) ||
				(this.getPosition().getY() < 0)) {
			diedDueToOutofRange = true;
			this.getStage().remove(this);
		}
		for (CopyOnWriteArrayList<Entity> array : this.getStage().getEntities().values()) {
			for (Entity e : array) {
				if (e == this) continue;
				if (this.overlaps(e) &&
					this.isOppositeFaction(e) &&
					e instanceof EntityLiving &&
					!((EntityLiving)e).isDead()) {
					((EntityLiving)e).damage(this, this.getDamage());
				}
			}
		}
	}
	
	@Override
	public void render(Graphics g) {
		Vec2 pos = this.getPosition();
		double angle = this.getVelocity().normalize().multiply(new Vec2(1, -1)).angle();
		double imagemidx = this.getTexture().getWidth(null)/2;
		double imagemidy = this.getTexture().getHeight(null)/2;
		AffineTransform tf = AffineTransform.getRotateInstance(angle, imagemidx, imagemidy);
		tf.rotate(angle, imagemidx, imagemidy);
		AffineTransformOp op = new AffineTransformOp(tf, AffineTransformOp.TYPE_BILINEAR);
		g.drawImage(op.filter((BufferedImage)this.getTexture(), null), (int)(pos.getX()-imagemidx), (int)(pos.getY()-imagemidy), null);
	}

	@Override
	public int getDamage() {
		return damage;
	}
	
	@Override
	public void uponDeath() {
		if (!diedDueToOutofRange) {
			this.getStage().add(new Explosion(this.getStage(), this.getPosition(), 10, 100));
		}
		super.uponDeath();
	}

	public static int getEnergyCost() {
		return 60;
	}
	
	@Override
	public Class<?> getSourceClass() {
		return source.getClass();
	}
	
	@Override
	public Entity getSource() {
		return source;
	}
	
	public static BufferedImage getTexFromEnum(ProjectileColor color) {
		try {
			switch(color) {
				case YELLOW:
					return ImageIO.read(Projectile.class.getResource("/com/greenteam/spacefighters/assets/projectile-0.png"));
				case BLUE:
					return ImageIO.read(Projectile.class.getResource("/com/greenteam/spacefighters/assets/projectile-1.png"));
				case RED:
					return ImageIO.read(Projectile.class.getResource("/com/greenteam/spacefighters/assets/projectile-2.png"));
				case GREEN:
					return ImageIO.read(Projectile.class.getResource("/com/greenteam/spacefighters/assets/projectile-3.png"));
				default :
					return ImageIO.read(Projectile.class.getResource("/com/greenteam/spacefighters/assets/projectile-0.png"));
			}
		} catch(Exception e) {
			return null;
		}
	}
	
	public int getDefaultLayer() {
		return 3;
	}
	
	public enum ProjectileColor {
		YELLOW, BLUE, RED, GREEN
	}

}
