package com.greenteam.spacefighters.entity.entityliving;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.greenteam.spacefighters.common.BoundRect;
import com.greenteam.spacefighters.common.Vec2;
import com.greenteam.spacefighters.entity.Entity;
import com.greenteam.spacefighters.stage.Stage;

public class GoalItem extends EntityLiving {
	private static final double GATSBY_DISTANCE_TO_MOVE = 100;
	private static final double MOVEMENT_RATIO = 2.3;
	private static final double DISTANCE_TOLER_TO_TARGET = 100;
	
	private static BufferedImage GREEN_LIGHT_TEXTURE;
	
	private boolean couldLoadImage;
	private ArrayList<Vec2> destPositions;
	private BoundRect moveWhenGatsbyGetsHere;
	private int currentDestPosition;

	public GoalItem(Stage s) {
		super(s, Integer.MAX_VALUE, Integer.MAX_VALUE);
		couldLoadImage = false;
		loadTex();
		this.getBoundingBox().setWidth(-1);
		this.getBoundingBox().setHeight(-1);
		this.destPositions = new ArrayList<Vec2>();
		this.moveWhenGatsbyGetsHere = new BoundRect(this.getPosition().getX(),
				this.getPosition().getY(),
				GATSBY_DISTANCE_TO_MOVE,
				GATSBY_DISTANCE_TO_MOVE*10
				);
		this.setVelocity(Vec2.ZERO);
		this.currentDestPosition = 0;
	}
	
	@Override
	public void update(int ms) {
		super.update(ms);
		moveWhenGatsbyGetsHere.setX(this.getPosition().getX());
		moveWhenGatsbyGetsHere.setY(this.getPosition().getY());
		this.setVelocity(destPositions.get(currentDestPosition).subtract(this.getPosition()).scale(MOVEMENT_RATIO));
		if ((this.getVelocity().magnitude() < (DISTANCE_TOLER_TO_TARGET*MOVEMENT_RATIO)) && this.getStage().getPlayer().getBoundingBox().intersects(moveWhenGatsbyGetsHere)) {
			++currentDestPosition;
			if (currentDestPosition >= destPositions.size()) this.uponDeath();
		}
	}

	@Override
	public void render(Graphics g) {
		if (couldLoadImage) {
			Image tex = this.getTexture();
			
			int w = tex.getWidth(null);
			int h = tex.getHeight(null);
			int x = (int)this.getPosition().getX() - w/2;
			int y = (int)this.getPosition().getY() - w/2;
			
			g.drawImage(tex, x, y, w, h, null);
		}
		else {
			int w = Stage.TILE_HEIGHT;
			int h = Stage.TILE_HEIGHT;
			int x = (int)this.getPosition().getX() - w/2;
			int y = (int)this.getPosition().getY() - w/2;
			
			g.setColor(Color.GREEN);
			g.fillRect(x, y, w, h);
		}
	}

	@Override
	public Class<?> getSourceClass() {
		return Entity.class;
	}
	
	private void loadTex() {
		try {
			if (GREEN_LIGHT_TEXTURE == null) {
				GREEN_LIGHT_TEXTURE = ImageIO.read(GoalItem.class.getResource("/com/greenteam/spacefighters/assets/gatsby/green-light.png"));
			}
			this.setTexture(GREEN_LIGHT_TEXTURE);
			couldLoadImage = true;
		}
		catch (IOException ex) {
			couldLoadImage = false;
			ex.printStackTrace();
		}
	}
	
	public void addDest(Vec2 pos) {
		destPositions.add(pos);
	}
}
