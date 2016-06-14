package com.greenteam.spacefighters.entity.entityliving.obstacle;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.greenteam.spacefighters.common.Vec2;
import com.greenteam.spacefighters.entity.Entity;
import com.greenteam.spacefighters.entity.entityliving.GoalItem;
import com.greenteam.spacefighters.entity.entityliving.starship.enemy.Enemy;
import com.greenteam.spacefighters.entity.entityliving.starship.enemy.TestEnemy;
import com.greenteam.spacefighters.entity.entityliving.starship.player.Player;
import com.greenteam.spacefighters.stage.Stage;

public class Tile extends Obstacle {
	private static BufferedImage GRASS = null;
	private static BufferedImage SOIL = null;
	private static BufferedImage BRICK = null;
	private static BufferedImage SPIKES = null;
	private static boolean allTexturesLoaded = false;
	
	private TileType type;
	private Class<?> source;

	public Tile(Stage s, TileType type, boolean touchable) {
		super(s, Integer.MAX_VALUE);
		this.getBoundingBox().setX(this.getPosition().getX());
		this.getBoundingBox().setY(this.getPosition().getY());
		if (touchable) {
			this.getBoundingBox().setWidth(Stage.TILE_HEIGHT);
			this.getBoundingBox().setHeight(Stage.TILE_HEIGHT);
		}
		else {
			this.getBoundingBox().setWidth(-1);
			this.getBoundingBox().setHeight(-1);
		}
		this.type = type;
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
	
	private void setTex() {
		try {
			if (!allTexturesLoaded) {
				GRASS = ImageIO.read(Player.class.getResource("/com/greenteam/spacefighters/assets/gatsby/tile-grass.png"));
				SOIL = ImageIO.read(Player.class.getResource("/com/greenteam/spacefighters/assets/gatsby/tile-soil.png"));
				BRICK = ImageIO.read(Player.class.getResource("/com/greenteam/spacefighters/assets/gatsby/tile-brick.png"));
				SPIKES = ImageIO.read(Player.class.getResource("/com/greenteam/spacefighters/assets/gatsby/tile-spikes.png"));
				allTexturesLoaded = true;
			}
		}
		catch (IOException ex) {
			allTexturesLoaded = false;
		}
		switch (type) {
		case GRASS:
			this.setTexture(GRASS);
			break;
		case SOIL:
			this.setTexture(SOIL);
			break;
		case BRICK:
			this.setTexture(BRICK);
			break;
		case SPIKES:
			this.setTexture(SPIKES);
			break;
		default:
			break;
		}
	}
	
	public static TileType interpretColor(int color) {
		color &= 0xffffff;
		switch (color) {
		case (-3947581)&0xffffff: //player (light grey)
			return TileType.PLAYER;
		case 4147404: //red enemy (deep blue)
			return TileType.REDENEMY;
		case (-4621737)&0xffffff: //soil (light brown)
			return TileType.SOIL;
		case (-7864299)&0xffffff: //grass (dark brown)
			return TileType.GRASS;
		case (-32985)&0xffffff: //brick (dark orange)
			return TileType.BRICK;
		case 15539236: //spikes (red)
			return TileType.SPIKES;
		case 0xffffff: //empty space (white)
			return TileType.AIR;
		case 2273612: //start position of goal item (dark green)
			return TileType.GOALITEMSTART;
		default:
			return TileType.UNKNOWN;
		}
	}
	
	public static void doTile(TileType type, int x, int y, Stage stage) {
		Entity entityToAdd = null;
		switch (type) {
		case SOIL:
			entityToAdd = new Tile(stage, type, false);
			((Tile)entityToAdd).setSourceClass(Obstacle.class);
			break;
		case GRASS:
		case BRICK:
			entityToAdd = new Tile(stage, type, true);
			((Tile)entityToAdd).setSourceClass(Obstacle.class);
			break;
		case SPIKES:
			entityToAdd = new Tile(stage, type, true);
			((Tile)entityToAdd).setSourceClass(Enemy.class);
			break;
		case PLAYER:
			stage.getPlayer().setPosition(new Vec2(Stage.TILE_HEIGHT/2+Stage.TILE_HEIGHT*x, Stage.TILE_HEIGHT*(1+y)-2)); //the -2 is needed because of collision detection glitches
			return;
		case REDENEMY:
			entityToAdd = new TestEnemy(stage);
			entityToAdd.setAcceleration(new Vec2(-Stage.PLAYER_HORIZONTAL_SPEED/3, Stage.GRAVITY));
			entityToAdd.setPosition(new Vec2(Stage.TILE_HEIGHT+Stage.TILE_HEIGHT*x, Stage.TILE_HEIGHT*(1+y)-6));
			stage.add(entityToAdd);
			return;
		case GOALITEMSTART:
			entityToAdd = new GoalItem(stage);
			((GoalItem)entityToAdd).addDest(new Vec2(Stage.TILE_HEIGHT/2+Stage.TILE_HEIGHT*x, Stage.TILE_HEIGHT/2+Stage.TILE_HEIGHT*y));
			((GoalItem)entityToAdd).addDest(new Vec2(Stage.TILE_HEIGHT/2+Stage.TILE_HEIGHT*x+300, Stage.TILE_HEIGHT/2+Stage.TILE_HEIGHT*y));
			break;
		case AIR:
			return;
		case UNKNOWN:
			return;
		}
		entityToAdd.setPosition(new Vec2(Stage.TILE_HEIGHT/2+Stage.TILE_HEIGHT*x, Stage.TILE_HEIGHT/2+Stage.TILE_HEIGHT*y));
		stage.add(entityToAdd);
	}
	
	@Override
	public Class<?> getSourceClass() {
		return source;
	}
	
	public void setSourceClass(Class<?> source) {
		this.source = source;
	}
}
