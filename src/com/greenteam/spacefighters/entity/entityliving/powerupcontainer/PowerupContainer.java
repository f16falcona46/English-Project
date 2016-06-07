package com.greenteam.spacefighters.entity.entityliving.powerupcontainer;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import com.greenteam.spacefighters.common.Vec2;
import com.greenteam.spacefighters.entity.Entity;
import com.greenteam.spacefighters.entity.entityliving.EntityLiving;
import com.greenteam.spacefighters.entity.entityliving.starship.player.Player;
import com.greenteam.spacefighters.stage.Stage;

public abstract class PowerupContainer extends EntityLiving {
	private static final int SELECT_NEW_POSITION_INTERVAL = 1000;
	private static final double SPAWNDIST = 400.0D;
	private static final int SPEED = 100;
	private static final int TIME_ON_SCREEN = 15000;
	private static final int BEGIN_FADING_TIME = 500;

	protected boolean couldLoadImage;
	private int timeRemaining;
	private Vec2 randpos;
	private int time;
	
	public PowerupContainer(Stage s) {
		super(s, 1, 1);
		time = SELECT_NEW_POSITION_INTERVAL;
		timeRemaining = TIME_ON_SCREEN;
		
		this.setPosition(this.randSpawnPos(s.getPlayer(), SPAWNDIST));
		this.setOrientation(new Vec2(0,-1));
		
		randpos = new Vec2(Stage.WIDTH * Math.random(), Stage.HEIGHT * Math.random());
	}
	
	public abstract java.awt.Color noTextureColor();
	
	@Override
	public void damage(Entity attacker, int damage) {}
	
	@Override
	public void render(Graphics g) {
		Vec2 pos = this.getPosition();
		if (couldLoadImage) {
			double angle = this.getOrientation().angle();
			double imagemidx = this.getTexture().getWidth(null)/2;
			double imagemidy = this.getTexture().getHeight(null)/2;
			AffineTransform tf = AffineTransform.getRotateInstance(angle, imagemidx, imagemidy);
			AffineTransformOp op = new AffineTransformOp(tf, AffineTransformOp.TYPE_BILINEAR);
			if ((timeRemaining <= BEGIN_FADING_TIME) && (timeRemaining >= 0)) {
				float opacity = timeRemaining/(float)BEGIN_FADING_TIME;
				opacity = (float)Math.pow(opacity, 0.25);
				Composite oldComposite = ((Graphics2D)g).getComposite();
				((Graphics2D)g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
				g.drawImage(op.filter((BufferedImage)this.getTexture(), null), (int)(pos.getX()-imagemidx), (int)(pos.getY()-imagemidy), null);
				((Graphics2D)g).setComposite(oldComposite);
			}
			else {
				g.drawImage(op.filter((BufferedImage)this.getTexture(), null), (int)(pos.getX()-imagemidx), (int)(pos.getY()-imagemidy), null);
			}
		} else {
			g.setColor(Color.RED);
			g.fillRect((int)pos.getX(), (int)pos.getY(), 10, 10);
		}
	}
	
	protected void applyPowerup(Player pl) {
		if (!pl.healthIsAugmented()) {
			pl.damage(this, this.getDamage());
			pl.setHealth(Math.min(pl.getMaxHealth(), pl.getHealth()));
		}
	}
	
	@Override
	public void update(int ms) {
		super.update(ms);
		Stage stage = this.getStage();
		Player pl = stage.getPlayer();
		time -= ms;
		if (time <= 0) {
			time = SELECT_NEW_POSITION_INTERVAL;
			randpos = new Vec2(Stage.WIDTH * Math.random(), Stage.HEIGHT * Math.random());
		}
		this.setVelocity(randpos.subtract(this.getPosition()).normalize().scale(SPEED));
		if (this.overlaps(pl)) {
			this.applyPowerup(pl);
			this.remove();
		}
		timeRemaining -= ms;
		if (timeRemaining <= 0)
			this.remove();
	}
	
	@Override
	public int getDamage() {
		return 0;
	}
	
	@Override
	public void uponDeath() {
		Stage stage = this.getStage();
		Entity lastAttacker = this.getLastAttacker();
		if (lastAttacker != null && lastAttacker instanceof Player) {
			Player pl = ((Player)lastAttacker);
			pl.setScore(stage.getPlayer().getScore() + this.getPointValue());
		}
		super.uponDeath();
	}
	
	@Override
	public double getRadius() {
		return 30;
	}
	
	@Override
	public Class<?> getSourceClass() {
		return PowerupContainer.class;
	}
	
	public static BufferedImage getTexFromEnum(PowerupColor color) {
		try {
			switch(color) {
				case BLUE:
					return ImageIO.read(PowerupContainer.class.getResource("/com/greenteam/spacefighters/assets/powerup-0.png"));
				case PURPLE:
					return ImageIO.read(PowerupContainer.class.getResource("/com/greenteam/spacefighters/assets/powerup-1.png"));
				case ORANGE:
					return ImageIO.read(PowerupContainer.class.getResource("/com/greenteam/spacefighters/assets/powerup-2.png"));
				case RED:
					return ImageIO.read(PowerupContainer.class.getResource("/com/greenteam/spacefighters/assets/powerup-3.png"));
				case GREEN:
					return ImageIO.read(PowerupContainer.class.getResource("/com/greenteam/spacefighters/assets/powerup-4.png"));
				case YELLOW:
					return ImageIO.read(PowerupContainer.class.getResource("/com/greenteam/spacefighters/assets/powerup-5.png"));
				default :
					return ImageIO.read(PowerupContainer.class.getResource("/com/greenteam/spacefighters/assets/powerup-0.png"));
			}
		} catch(Exception e) {
			return null;
		}
	}
	
	public enum PowerupColor {
		RED, GREEN, YELLOW, BLUE, PURPLE, ORANGE
	}

}
