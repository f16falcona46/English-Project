package com.greenteam.spacefighters.entity;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import com.greenteam.spacefighters.common.BoundRect;
import com.greenteam.spacefighters.common.Vec2;
import com.greenteam.spacefighters.stage.Stage;

public abstract class Entity {
	private Vec2 position;
	private Vec2 velocity;
	private Vec2 acceleration;
	private Vec2 orientation;
	private boolean updatable;
	private BoundRect rect;
	protected Image texture;
	private Stage stage;
	
	public Entity(Stage s) {
		position = new Vec2(0, 0);
		velocity = new Vec2(0, 0);
		acceleration = new Vec2(0, 0);
		orientation = new Vec2(0, 1);
		updatable = true;
		texture = null;
		rect = new BoundRect(0,0,-1,-1);
		stage = s;
	}
	
	public abstract void render(Graphics g);
	
	public int getDefaultLayer() {
		return 0;
	}
	
	public void update(int ms) {
		this.getBoundingBox().setX(this.getPosition().getX());
		this.getBoundingBox().setY(this.getPosition().getY());
		Vec2 newPosition = position.add(velocity.scale(((double)ms)/1000));
		if (this.canMove(newPosition)) {
			position = newPosition;
		}
		velocity = velocity.add(acceleration.scale(((double)ms)/1000));
	}
	
	public boolean canMove(Vec2 newPosition) {
		return true;
	}
	
	public void setUpdatable(boolean updatable) {
		this.updatable = updatable;
	}
	
	public boolean isUpdatable() {
		return updatable;
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
		if (texture != null) {
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
	}
	
	public BoundRect getBoundingBox() {
		return rect;
	}
	
	public boolean overlaps(Entity e) {
		return this.getBoundingBox().intersects(e.getBoundingBox());
	}
	
	public abstract Class<?> getSourceClass();
	
	public Entity getSource() {
		return this;
	}
	
	public int getPointValue() {
		return 100; //default
	}
	
}
