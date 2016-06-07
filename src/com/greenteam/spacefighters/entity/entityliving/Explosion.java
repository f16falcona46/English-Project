package com.greenteam.spacefighters.entity.entityliving;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.greenteam.spacefighters.common.Vec2;
import com.greenteam.spacefighters.entity.entityliving.projectile.Projectile;
import com.greenteam.spacefighters.stage.Stage;

public class Explosion extends EntityLiving {
	private int timeRemaining;
	private int maxTime;
	private int size;
	
	public Explosion(Stage s, Vec2 position, int timeRemaining) {
		super(s, 1, 1);
		try {
			this.setTexture(ImageIO.read(Projectile.class.getResource("/com/greenteam/spacefighters/assets/explosion-0.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.setPosition(position);
		this.maxTime = timeRemaining;
		this.timeRemaining = timeRemaining;
		this.size = 0;
	}

	public Explosion(Stage s, Vec2 position, int size, int timeRemaining) {
		super(s, 1, 1);
		try {
			this.setTexture(ImageIO.read(Projectile.class.getResource("/com/greenteam/spacefighters/assets/explosion-0.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.setPosition(position);
		this.maxTime = timeRemaining;
		this.timeRemaining = timeRemaining;
		this.size = size;
	}

	@Override
	public void update(int ms) {
		super.update(ms);
		timeRemaining -= ms;
		if (timeRemaining <= 0) {
			this.remove();
		}
	}
	
	@Override
	public void render(Graphics g) {
		float opacity = (timeRemaining < 0 ? 0 : timeRemaining) / (float)maxTime;
		opacity = (float)Math.pow(opacity, 0.7);
		int imgSize;
		if (size != 0)
			imgSize = 2 * size;
		else
			imgSize = (int)this.getTexture().getWidth(null);
		Composite oldComposite = ((Graphics2D)g).getComposite();
		((Graphics2D)g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
		g.drawImage(this.getTexture(),
				(int)(this.getPosition().getX() - imgSize / 2.0f),
				(int)(this.getPosition().getY() - imgSize / 2.0f),
				imgSize, imgSize,
				null);
		((Graphics2D)g).setComposite(oldComposite);
	}
	
	@Override
	public int getDefaultLayer() {
		return 1;
	}
	
	@Override
	public int getDamage() {
		return 0;
	}
	
	@Override
	public double getRadius() {
		return -Double.MAX_VALUE;
	}

	@Override
	public Class<?> getSourceClass() {
		return Object.class;
	}
}
