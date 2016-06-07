package com.greenteam.spacefighters.entity.entityliving.obstacle.asteroid;

import java.awt.Color;
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
import com.greenteam.spacefighters.stage.Stage;

public class Asteroid extends Obstacle {
	private static final double SPAWNDIST = 100.0D;
	
	private int size;
	private boolean couldLoadImage;
	
	public Asteroid(Stage s, int size) {
		super(s, (int)Math.ceil(size * Math.random()));
		this.setPosition(randSpawnPos(s.getPlayer(), SPAWNDIST));
		this.setOrientation(Vec2.fromAngle(2 * Math.PI * Math.random()));
		int texID = (int)(3 * Math.random());
		this.setTexture(Asteroid.getTexFromID(texID));
		if (this.getTexture() != null) {
			couldLoadImage = true;
		} else {
			couldLoadImage = false;
		}
		this.size = size;
	}

	@Override
	public void render(Graphics g) {
		Vec2 pos = this.getPosition();
		if (couldLoadImage) {
			//double angle = this.getOrientation().angle();
			AffineTransform tf = new AffineTransform();
			//tf.rotate(angle, size / 2, size / 2);
			AffineTransformOp op = new AffineTransformOp(tf, AffineTransformOp.TYPE_BILINEAR);
			g.drawImage(op.filter((BufferedImage)this.getTexture(), null),
					(int)(pos.getX()- size / 2.0f), (int)(pos.getY() - size / 2.0f),
					size, size, null);
		} else {
			g.setColor(Color.DARK_GRAY);
			g.fillRect((int)pos.getX() - 5, (int)pos.getY() - 5, 10, 10);
		}
	}
	
	@Override
	public int getDamage() {
		return 5;
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
		Explosion e = new Explosion(this.getStage(), this.getPosition(), size, 100);
		this.getStage().add(e);
		super.uponDeath();
	}
	
	@Override
	public double getRadius() {
		return size / 2.0f;
	}

	@Override
	public Class<?> getSourceClass() {
		return Obstacle.class;
	}
	
	public static BufferedImage getTexFromID(int texID) {
		try {
			return ImageIO.read(Asteroid.class.getResource("/com/greenteam/spacefighters/assets/asteroid-"+ texID + ".png"));
		} catch(Exception e) {
			return null;
		}
	}
	
}
