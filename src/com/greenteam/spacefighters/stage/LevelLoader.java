package com.greenteam.spacefighters.stage;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.imageio.ImageIO;
import javax.swing.Timer;

import com.greenteam.spacefighters.GUI.HUD;
import com.greenteam.spacefighters.GUI.Window;
import com.greenteam.spacefighters.common.Vec2;
import com.greenteam.spacefighters.entity.Entity;
import com.greenteam.spacefighters.entity.entityliving.obstacle.Tile;
import com.greenteam.spacefighters.entity.entityliving.obstacle.TileType;
import com.greenteam.spacefighters.entity.entityliving.starship.enemy.*;
import com.greenteam.spacefighters.entity.entityliving.starship.player.Player;
import com.greenteam.spacefighters.entity.entityliving.starship.player.Player.PlayerShipColor;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

public class LevelLoader implements ActionListener {
	private static final double[] LEVEL_INTERVAL_RATIOS = {1.0, 0.95, 0.9, 0.8, 0.5};
	private static final int ASTEROID_COUNT = 50;
	private static final int ASTEROID_MAXSIZE = 50;
	private static final int ASTEROID_MINSIZE = 20;
	private static final int TESTENEMY_SPAWNINTERVAL = 600;
	private static final int ERRATICENEMY_SPAWNINTERVAL = 640;
	private static final int SHOOTINGENEMY_SPAWNINTERVAL = 680;
	private static final int TRACKERENEMY_SPAWNINTERVAL = 740;
	private static final int ASTEROID_SPAWNINTERVAL = 1000;
	private static final int POWERUP_SPAWNINTERVAL = 8000;
	private static final int POWERUP_TYPENUMBER = 5;
	private static final String[] LEVEL_DESCRIPTIONS = {
			"The first level.",
			"You'll see some new enemies soon, but they aren't very smart.",
			"The enemy is tired of getting shot at. Now they can shoot back. You are also an entering an asteroid field.",
			"Kamikaze spirit has been instilled in the enemy.",
			"New production advances have allowed the enemy to produce significantly more spaceships than before."
		};
	private int[][] LEVEL_SCORE_THRESHOLD_TEMPALTES = {
			{500, 1200, 2000, 3000},
			{2000, 7000, 14000, 22000}
		};
	
	private int[] levelScoreThresholds;
	
	private Stage stage;
	private Timer timer;
	private int time;
	private int level;
	private Difficulty difficulty;
	
	//private Thread musicThread;
	
	public LevelLoader(Window window, int width, int height, File f) {
		stage = new Stage(window, width, height, this);
		for (CopyOnWriteArrayList<Entity> array : stage.getEntities().values())
			array.clear();
		timer = stage.getTimer();
		timer.addActionListener(this);
		level = 0;
		difficulty = Difficulty.HARD;
		this.setDifficulty(difficulty);
		
		Player player = new Player(stage, 100, 100, PlayerShipColor.RED);
		//player.setPosition(new Vec2(stage.getCanvasWidth() / 2 , stage.getCanvasHeight() / 2));
		player.setAcceleration(new Vec2(0, Stage.GRAVITY));
		stage.setPlayer(player);
		
		stage.setHUD(new HUD(stage));
	}
	
	public void startLevel() {
		stage.pause();
		Player p = stage.getPlayer();
		for (CopyOnWriteArrayList<Entity> array : stage.getEntities().values())
			array.clear();
		
		try {
			BufferedImage map = ImageIO.read(Player.class.getResource("/com/greenteam/spacefighters/assets/gatsby/map-"+Integer.toString(this.getLevel()+1)+".png"));
			stage.setCanvasWidth(map.getWidth()*Stage.TILE_HEIGHT);
			stage.setCanvasHeight(map.getHeight()*Stage.TILE_HEIGHT);
			for (int i = 0; i < map.getWidth(); ++i) {
				for (int j = 0; j < map.getHeight(); ++j) {
					int color = map.getRGB(i, j);
					TileType type = Tile.interpretColor(color);
					//if ((j == 37) && (i==39))
					//System.out.println(i+" "+j+" "+type+" "+(color&0xffffff));
					if (type == TileType.UNKNOWN) System.out.println(color&0xffffff);
					Tile.doTile(type,i,j,stage);
				}
			}
		}
		catch (IOException ex) {}
		
		/*
		for (int i = 0; i < stage.getCanvasWidth()/16+1; ++i) {
			Tile wall = new Tile(stage);
			wall.getBoundingBox().setHeight(16);
			wall.getBoundingBox().setWidth(16);
			wall.setPosition(new Vec2(16*i+8, stage.getCanvasHeight()-8));
			System.out.println(wall.getBoundingBox());
			stage.add(wall);
		}
		
		for (int j = 0; j < 10; ++j) {
			for (int i = 0; i < 10; ++i) {
				Tile wall = new Tile(stage);
				wall.getBoundingBox().setHeight(16);
				wall.getBoundingBox().setWidth(16);
				wall.setPosition(new Vec2(16*i+8+j*160, stage.getCanvasHeight()-8-j*80));
				System.out.println(wall.getBoundingBox());
				stage.add(wall);
			}
		}
		*/
		
		stage.add(p);
		p.reset();
	}
	
	private void nextLevel() {
		level++;
		startLevel();
		if (level == 2) {
			for (int i = 0; i < ASTEROID_COUNT; i++) {
				int size = (int)((ASTEROID_MAXSIZE - ASTEROID_MINSIZE) * Math.random()) + ASTEROID_MINSIZE;
				//stage.add(new Asteroid(stage, size));
			}
		}
		((CardLayout)stage.getParent().getLayout()).show(stage.getParent(), Window.LEVELINCREMENTSCREEN);
	}
	
	public void setLevel(int level) {
		this.level = level;
	}
	
	public int getLevel() {
		return this.level;
	}

	public Stage getStage() {
		return stage;
	}
	
	@Override
	public void actionPerformed(ActionEvent ev) {
		/*
		if (ev.getSource() == timer) {
			time += timer.getDelay();
			if ((time % (int)(TESTENEMY_SPAWNINTERVAL*LEVEL_INTERVAL_RATIOS[level])) == 0) {
				stage.add(new TestEnemy(stage));
			}
			if ((time % (int)(ERRATICENEMY_SPAWNINTERVAL*LEVEL_INTERVAL_RATIOS[level])) == 0) {
				if (level >= 1) stage.add(new ErraticEnemy(stage)); else stage.add(new TestEnemy(stage));
			}
			if ((time % (int)(SHOOTINGENEMY_SPAWNINTERVAL*LEVEL_INTERVAL_RATIOS[level])) == 0) {
				if (level >= 2) stage.add(new ShootingEnemy(stage)); else if (level >= 1) stage.add(new ErraticEnemy(stage));
			}
			if ((time % (int)(TRACKERENEMY_SPAWNINTERVAL*LEVEL_INTERVAL_RATIOS[level])) == 0) {
				if (level >= 3) stage.add(new TrackerEnemy(stage)); else if (level >= 2) stage.add(new ShootingEnemy(stage));
			}
			if ((time % (int)(ASTEROID_SPAWNINTERVAL*LEVEL_INTERVAL_RATIOS[level])) == 0) {
				if (level >= 2) {
					int size = (int)((ASTEROID_MAXSIZE - ASTEROID_MINSIZE) * Math.random()) + ASTEROID_MINSIZE;
					stage.add(new Asteroid(stage, size));
				}
			}
			if ((time % (int)(POWERUP_SPAWNINTERVAL*LEVEL_INTERVAL_RATIOS[level])) == 0) {
				switch((int)(Math.random() * POWERUP_TYPENUMBER)) {
					case 0 :
						stage.add(new HealthRestorePowerupContainer(stage));
						break;
					case 1 :
						stage.add(new ForceFieldPowerupContainer(stage));
						break;
					case 2 :
						stage.add(new HealthBoostPowerupContainer(stage));
						break;
					case 3 :
						stage.add(new ChargeBoostPowerupContainer(stage));
						break;
					case 4 :
						stage.add(new ChainBeamPowerupContainer(stage));
						break;
					default :
						stage.add(new HealthRestorePowerupContainer(stage));
						break;
				}
			}
		}
		if ((level < levelScoreThresholds.length) && (stage.getPlayer().getScore() >= levelScoreThresholds[level])) {
			nextLevel();
		}
		*/
	}
	
	public String getLevelBlurb() {
		return LEVEL_DESCRIPTIONS[level];
	}
	
	public void handleDeath() {
		startLevel();
		((CardLayout)stage.getParent().getLayout()).show(stage.getParent(), Window.DEATHSCREEN);
	}
	
	public void setDifficulty(Difficulty difficulty) {
		switch (difficulty) {
		case EASY:
			levelScoreThresholds = LEVEL_SCORE_THRESHOLD_TEMPALTES[0];
			break;
		case HARD:
			levelScoreThresholds = LEVEL_SCORE_THRESHOLD_TEMPALTES[1];
			break;
		default:
			levelScoreThresholds = LEVEL_SCORE_THRESHOLD_TEMPALTES[0];
			break;
		}
		this.difficulty = difficulty;
	}
	
	public enum Difficulty {
		EASY, HARD
	}
	
	public void setEasyMode(boolean easy) {
		if (easy) setDifficulty(Difficulty.EASY);
		else setDifficulty(Difficulty.HARD);
	}
	
	public boolean isInEasyMode() {
		return difficulty == Difficulty.EASY;
	}
}