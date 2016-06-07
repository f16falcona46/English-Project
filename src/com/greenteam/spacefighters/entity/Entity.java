package com.greenteam.spacefighters.entity;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.image.BufferedImage;

import com.greenteam.spacefighters.common.Vec2;
import com.greenteam.spacefighters.stage.Stage;

public abstract class Entity {
	private Vec2 position;
	private Vec2 velocity;
	private Vec2 acceleration;
	private Vec2 orientation;
	private boolean updatable;
	protected Image texture;
	private Stage stage;
	
	public Entity(Stage s) {
		position = new Vec2(0, 0);
		velocity = new Vec2(0, 0);
		acceleration = new Vec2(0, 0);
		orientation = new Vec2(0, 1);
		updatable = true;
		texture = null;
		stage = s;
	}
	
	public abstract void render(Graphics g);
	
	public int getDefaultLayer() {
		return 0;
	}
	
	public void update(int ms) {
		velocity = velocity.add(acceleration.scale(((double)ms)/1000));
		position = position.add(velocity.scale(((double)ms)/1000));
	}
	
	public void setUpdatable(boolean updatable) {
		this.updatable = updatable;
	}
	
	public boolean isUpdatable() {
		return updatable;
	}
	
	public Vec2 randSpawnPos(Entity e, double minDist) {
		Vec2 spawnPos = new Vec2(this.getRadius(), this.getRadius()).add(Vec2.random(Stage.WIDTH - 2 * this.getRadius(), Stage.HEIGHT - 2 * this.getRadius()));
		Vec2 entityPos = e.getPosition();
		double dist = entityPos.distance(spawnPos);
		if (dist < minDist) {
			Vec2 player2Enemy = spawnPos.subtract(entityPos);
			spawnPos = spawnPos.add(player2Enemy.scale(minDist / dist));
			int i = 0;
			while (!Stage.inStage(spawnPos)) {
				if (i < 3) {
					spawnPos = spawnPos.rotate(entityPos, Math.PI / 2);
					i++;
				} else {
					System.err.println("Entity placement failed!");
					System.err.println("Player:\t" + entityPos);
					System.err.println("Entity:\t" + spawnPos);
					spawnPos = Vec2.ZERO;
					break;
				}
			}
		}
		return spawnPos;
	}

	public Vec2 getOrientation() {
		return orientation;
	}

	public void setOrientation(Vec2 orientation) {
		this.orientation = orientation;
	}

	public Vec2 getPosition() {
		return position;
	}

	public void setPosition(Vec2 position) {
		this.position = position;
	}

	public Vec2 getAcceleration() {
		return acceleration;
	}

	public void setAcceleration(Vec2 acceleration) {
		this.acceleration = acceleration;
	}
	
	public void remove() {
		stage.remove(this);
	}
	
	public Vec2 getVelocity() {
		return velocity;
	}

	public void setVelocity(Vec2 velocity) {
		this.velocity = velocity;
	}

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}
	
	public Image getTexture() {
		return texture;
	}

	protected void setTexture(Image texture) {
		
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
	    GraphicsDevice device = env.getDefaultScreenDevice();
	    GraphicsConfiguration config = device.getDefaultConfiguration();
	    BufferedImage image = (BufferedImage)texture;
		
		
	    if (image.getColorModel().equals(config.getColorModel())) {
	        this.texture = image;
	    } else {
	    	final BufferedImage new_image = config.createCompatibleImage(image.getWidth(), image.getHeight(), image.getTransparency());

	    	final Graphics2D g2d = (Graphics2D) new_image.getGraphics();
	    	g2d.drawImage(image, 0, 0, null);
	    	g2d.dispose();

	    	this.texture = new_image;
	    }
	}
	
	public double getRadius() {
		return 5;
	}
	
	public boolean overlaps(Entity e) {
		return this.getPosition().distance(e.getPosition()) < this.getRadius() + e.getRadius();
	}
	
	public abstract Class<?> getSourceClass();
	
	public Entity getSource() {
		return this;
	}
	
	public int getPointValue() {
		return 100; //default
	}
	
}
