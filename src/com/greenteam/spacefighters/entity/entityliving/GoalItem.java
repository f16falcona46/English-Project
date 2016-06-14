package com.greenteam.spacefighters.entity.entityliving;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.greenteam.spacefighters.entity.Entity;
import com.greenteam.spacefighters.stage.Stage;

public class GoalItem extends EntityLiving {
	private static BufferedImage GREEN_LIGHT_TEXTURE;
	
	private boolean couldLoadImage;

	public GoalItem(Stage s, int maxHealth, int health) {
		super(s, maxHealth, health);
		couldLoadImage = false;
		loadTex();
		this.getBoundingBox().setWidth(-1);
		this.getBoundingBox().setHeight(-1);
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
}
