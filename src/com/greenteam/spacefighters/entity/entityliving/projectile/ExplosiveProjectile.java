package com.greenteam.spacefighters.entity.entityliving.projectile;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.greenteam.spacefighters.common.Vec2;
import com.greenteam.spacefighters.entity.Entity;
import com.greenteam.spacefighters.stage.Stage;

public class ExplosiveProjectile extends Projectile {
	private final static double PROJECTILERADIUS = 40.0D;
	private final static double BLASTRADIUS = 200.0D;
	private final static int DAMAGE = 15;
	private final static int COUNTDOWNTIME = 500;
	private final static int EXPLOSIONDURATION = 100;
	private double hitRadius;
	private int countdown;
	private boolean isExploding;
	private boolean textureChanged;
	
	public ExplosiveProjectile(Stage s, int health, int damage, Vec2 position, Vec2 velocity, Entity source) {
		super(s, health, damage, position, velocity, source);
		
		this.setTexture(Projectile.getTexFromEnum(ProjectileColor.RED));
		
		hitRadius = PROJECTILERADIUS;
		countdown = COUNTDOWNTIME;
		isExploding = false;
		textureChanged = false;
	}
	
	@Override
	public double getRadius() {
		return hitRadius;
	}
	
	public static int getEnergyCost() {
		return 125;
	}
	
	@Override
	public int getDamage() {
		if (!isExploding)
			return 0;
		else
			return DAMAGE;
	}
	
	@Override
	public void render(Graphics g) {
		Vec2 pos = this.getPosition();
		double angle = this.getOrientation().multiply(new Vec2(1, -1)).angle();
		double scale = hitRadius / PROJECTILERADIUS;
		double imagemidx = scale * this.getTexture().getWidth(null)/2;
		double imagemidy = scale * this.getTexture().getHeight(null)/2;
		AffineTransform tf = AffineTransform.getRotateInstance(angle, imagemidx, imagemidy);
		tf.rotate(angle, imagemidx, imagemidy);
		AffineTransformOp op = new AffineTransformOp(tf, AffineTransformOp.TYPE_BILINEAR);
		float opacity = (float) Math.pow(1 / scale, 0.25);
		Composite oldComposite = ((Graphics2D)g).getComposite();
		((Graphics2D)g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
		g.drawImage(op.filter((BufferedImage)this.getTexture(), null),
				(int)(pos.getX()-imagemidx), (int)(pos.getY()-imagemidy),
				2 * (int)imagemidx, 2 * (int)imagemidy,
				null);
		((Graphics2D)g).setComposite(oldComposite);
	}
	
	@Override
	public void damage(Entity attacker, int damage) {}
	
	@Override
	public void update(int ms) {
		super.update(ms);
		countdown -= ms;
		if (countdown <= 0) {
			if (!isExploding) {
				isExploding = true;
				setVelocity(new Vec2(0, 0));
				countdown += EXPLOSIONDURATION;
			} else {
				this.getStage().remove(this);
			}
		}
		if (isExploding && !textureChanged) {
			try {
				this.setTexture(ImageIO.read(Projectile.class.getResource("/com/greenteam/spacefighters/assets/explosion-0.png")));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (isExploding) {
			hitRadius = PROJECTILERADIUS + (BLASTRADIUS - PROJECTILERADIUS) * (1 - (countdown / (float)EXPLOSIONDURATION));
		}
	}
	
}
