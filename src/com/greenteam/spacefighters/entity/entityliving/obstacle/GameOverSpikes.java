package com.greenteam.spacefighters.entity.entityliving.obstacle;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.greenteam.spacefighters.stage.Stage;

public class GameOverSpikes extends Obstacle {
	private static BufferedImage SPIKES = null;
	private static boolean allTexturesLoaded = false;

	public GameOverSpikes(Stage s) {
		super(s, Integer.MAX_VALUE);
		this.getBoundingBox().setX(this.getPosition().getX());
		this.getBoundingBox().setY(this.getPosition().getY());
		this.getBoundingBox().setWidth(Stage.TILE_HEIGHT);
		this.getBoundingBox().setHeight(Stage.TILE_HEIGHT);
		loadImages();
		this.setTexture(SPIKES);
	}

	@Override
	public void render(Graphics g) {
		int x = (int)this.getBoundingBox().getX();
		int y = (int)this.getBoundingBox().getY();
		int w = (int)this.getBoundingBox().getWidth();
		int h = (int)this.getBoundingBox().getHeight();
		if (w < 0) {
			if (this.getTexture() != null) {
				w = this.getTexture().getWidth(null);
			}
			else {
				w = Stage.TILE_HEIGHT;
			}
		}
		if (h < 0) {
			if (this.getTexture() != null) {
				h = this.getTexture().getHeight(null);
			}
			else {
				h = Stage.TILE_HEIGHT;
			}
		}
		//System.out.println(x+" "+y+" "+w+" "+h);
		if (this.getTexture() == null) {
			g.setColor(Color.DARK_GRAY);
			g.fillRect(x-w/2, y-h/2, w, h);
		}
		else {
			g.drawImage(this.getTexture(), x-w/2, y-h/2, w, h, null);
		}
	}
	
	private void loadImages() {
		if (!allTexturesLoaded) {
			try {
				SPIKES = ImageIO.read(this.getClass().getResource("/com/greenteam/spacefighters/assets/gatsby/tile-spikes.png"));
				allTexturesLoaded = true;
			}
			catch (IOException ex) {
				allTexturesLoaded = false;
				ex.printStackTrace();
			}
		}
	}
}
