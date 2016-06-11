package com.greenteam.spacefighters.entity.entityliving.obstacle;

import java.awt.Color;
import java.awt.Graphics;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.greenteam.spacefighters.entity.entityliving.starship.player.Player;
import com.greenteam.spacefighters.stage.Stage;

public class Tile extends Obstacle {
	private TileType type;

	public Tile(Stage s, int color) {
		super(s, Integer.MAX_VALUE);
		this.getBoundingBox().setX(this.getPosition().getX());
		this.getBoundingBox().setY(this.getPosition().getY());
		this.getBoundingBox().setWidth(Stage.TILE_HEIGHT);
		this.getBoundingBox().setHeight(Stage.TILE_HEIGHT);
		this.setTex();
	}
	
	public Tile(Stage s) {
		super(s, Integer.MAX_VALUE);
		this.getBoundingBox().setX(this.getPosition().getX());
		this.getBoundingBox().setY(this.getPosition().getY());
		this.getBoundingBox().setWidth(Stage.TILE_HEIGHT);
		this.getBoundingBox().setHeight(Stage.TILE_HEIGHT);
		this.setTexture(null);
	}

	@Override
	public void update(int ms) {
		super.update(ms);
	}
	
	@Override
	public void render(Graphics g) {
		int x = (int)this.getBoundingBox().getX();
		int y = (int)this.getBoundingBox().getY();
		int w = (int)this.getBoundingBox().getWidth();
		int h = (int)this.getBoundingBox().getHeight();
		//System.out.println(x+" "+y+" "+w+" "+h);
		if (this.getTexture() == null) {
			g.setColor(Color.DARK_GRAY);
			g.fillRect(x-w/2, y-h/2, w, h);
		}
		else {
			g.drawImage(this.getTexture(), x-w/2, y-h/2, w, h, null);
		}
	}
	
	private void setTex() {
		try {
			switch (type) {
			case GRASS:
				this.setTexture(ImageIO.read(Player.class.getResource("/com/greenteam/spacefighters/assets/gatsby/tile-grass.png")));
				break;
			case SOIL:
				this.setTexture(ImageIO.read(Player.class.getResource("/com/greenteam/spacefighters/assets/gatsby/tile-soil.png")));
				break;
			case BRICK:
				this.setTexture(ImageIO.read(Player.class.getResource("/com/greenteam/spacefighters/assets/gatsby/tile-brick.png")));
				break;
			}
		}
		catch (IOException ex) { }
	}
	
	public static TileType interpretColor(int color) {
		color &= 0xffffff;
		switch (color) {
		case (-3947581)&0xffffff: //player (light grey)
			return TileType.PLAYER;
		case (-4621737)&0xffffff: //soil (light brown)
			return TileType.SOIL;
		case (-7864299)&0xffffff: //grass (dark brown)
			return TileType.GRASS;
		case (-32985)&0xffffff: //brick (dark orange)
			return TileType.BRICK;
		case 0xffffff:
			return TileType.AIR;
		default:
			return TileType.UNKNOWN;
		}
	}
	
	public static void doTile(TileType type, int x, int y, Stage stage) {
		
	}
}
