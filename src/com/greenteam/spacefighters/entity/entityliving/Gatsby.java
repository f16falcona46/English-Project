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

public class Gatsby extends EntityLiving {
	private static final double BOUNDING_BOX_WIDTH = 18;
	private static final double BOUNDING_BOX_HEIGHT = 36;
	
	private static BufferedImage GREEN_LIGHT_TEXTURE;
	
	private boolean couldLoadImage;

	public Gatsby(Stage s) {
		super(s, Integer.MAX_VALUE, Integer.MAX_VALUE);
		couldLoadImage = false;
		loadTex();
		this.getBoundingBox().setWidth(BOUNDING_BOX_WIDTH);
		this.getBoundingBox().setHeight(BOUNDING_BOX_HEIGHT);
		this.setVelocity(Vec2.ZERO);
	}

	@Override
	public void render(Graphics g) {
		if (couldLoadImage) {
			Image tex = this.getTexture();
			
			int w = (int)this.getBoundingBox().getWidth();
			int h = (int)this.getBoundingBox().getHeight();
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
				GREEN_LIGHT_TEXTURE = ImageIO.read(GoalItem.class.getResource("/com/greenteam/spacefighters/assets/gatsby/purple1.png"));
			}
			this.setTexture(GREEN_LIGHT_TEXTURE);
			couldLoadImage = true;
		}
		catch (IOException ex) {
			couldLoadImage = false;
			ex.printStackTrace();
		}
	}
}
