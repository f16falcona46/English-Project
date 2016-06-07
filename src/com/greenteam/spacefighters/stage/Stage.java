package com.greenteam.spacefighters.stage;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import com.greenteam.spacefighters.GUI.HUD;
import com.greenteam.spacefighters.GUI.KeyboardInputHandlerHolder;
import com.greenteam.spacefighters.GUI.Window;
import com.greenteam.spacefighters.common.Vec2;
import com.greenteam.spacefighters.entity.Entity;
import com.greenteam.spacefighters.entity.entityliving.starship.player.Player;

public class Stage extends JPanel implements ActionListener, MouseListener {
	public static final int WIDTH = 2400;
	public static final int HEIGHT= 2400;
	private static final long serialVersionUID = -2937557151448523567L;
	private static final int NUM_STARS = 120;
	private static final double BACKGROUND_SCROLL_SPEED = 2.5;
	private static final int BACKGROUND_OVERSIZE_RATIO = 5;
	private static final int STARFIELD_LAYERS = 3;
	
	private static final double ALL_MOVEMENT_DEAD_ZONE = 20;
	private static final double LINEAR_MOVEMENT_DEAD_ZONE = 120;
	private static final double ROTATION_DEAD_ZONE_SECTOR_SIZE = 0.05; //radians

	
	private ConcurrentHashMap<Integer, CopyOnWriteArrayList<Entity>> entities;
	private Timer timer;
	private Player player;
	private HUD hud;
	private LevelLoader loader;
	private Timer firePrimaryTimer;
	private Timer fireSecondaryTimer;
	private Timer fireTertiaryTimer;
	private Timer fireQuaternaryTimer;
	private Image[] starfields;
	private double[] backgroundOffsets;
	private boolean upKeyPressed;
	private boolean downKeyPressed;
	private boolean leftKeyPressed;
	private boolean rightKeyPressed;
	private boolean mouseEnabled;
	private JButton returnToTitle;
	private GridBagConstraints returnToTitleGridBagConstraints;
	private Window window;
	private JButton pauseResumeButton;
	
	public Stage(Window window, int width, int height, LevelLoader levelLoader) {
		this.window = window;
		this.entities = new ConcurrentHashMap<Integer, CopyOnWriteArrayList<Entity>>();
		this.hud = null;
		this.loader = levelLoader;
		this.mouseEnabled = false;
		this.backgroundOffsets = new double[STARFIELD_LAYERS];
		this.setLayout(new GridBagLayout());
		
		this.returnToTitleGridBagConstraints = new GridBagConstraints();
		returnToTitleGridBagConstraints.gridx = 0;
		returnToTitleGridBagConstraints.gridy = 1;
		returnToTitleGridBagConstraints.weightx = 1;
		returnToTitleGridBagConstraints.weighty = 1;
		returnToTitleGridBagConstraints.insets = new Insets(20, 20, 20, 20);
		returnToTitleGridBagConstraints.anchor = GridBagConstraints.PAGE_END;
		this.returnToTitle = new JButton("Return to Title");
		this.returnToTitle.addActionListener(this);
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.insets = new Insets(20, 20, 20, 20);
		gbc.anchor = GridBagConstraints.FIRST_LINE_END;
		this.pauseResumeButton = new JButton("Resume");
		this.pauseResumeButton.addActionListener(this);
		this.add(pauseResumeButton, gbc);
		
		KeyboardInputHandlerHolder.handler.addPressedAction("UP", new MoveActionPressed(DirectionKey.FORWARD));
		KeyboardInputHandlerHolder.handler.addPressedAction("DOWN", new MoveActionPressed(DirectionKey.BACK));
		KeyboardInputHandlerHolder.handler.addPressedAction("RIGHT", new MoveActionPressed(DirectionKey.RIGHT));
		KeyboardInputHandlerHolder.handler.addPressedAction("LEFT", new MoveActionPressed(DirectionKey.LEFT));
		
		KeyboardInputHandlerHolder.handler.addReleasedAction("UP", new MoveActionReleased(DirectionKey.FORWARD));
		KeyboardInputHandlerHolder.handler.addReleasedAction("DOWN", new MoveActionReleased(DirectionKey.BACK));
		KeyboardInputHandlerHolder.handler.addReleasedAction("RIGHT", new MoveActionReleased(DirectionKey.RIGHT));
		KeyboardInputHandlerHolder.handler.addReleasedAction("LEFT", new MoveActionReleased(DirectionKey.LEFT));
		
		KeyboardInputHandlerHolder.handler.addPressedAction("SPACE", new PauseAction());
		
		KeyboardInputHandlerHolder.handler.addPressedAction("Z", new FireKeyPressed(FireKey.PRIMARY));
		KeyboardInputHandlerHolder.handler.addPressedAction("X", new FireKeyPressed(FireKey.SECONDARY));
		KeyboardInputHandlerHolder.handler.addPressedAction("C", new FireKeyPressed(FireKey.TERTIARY));
		KeyboardInputHandlerHolder.handler.addPressedAction("F", new FireKeyPressed(FireKey.QUATERNARY));
		KeyboardInputHandlerHolder.handler.addPressedAction("V", new FireKeyPressed(FireKey.CHAINBEAM));
		
		KeyboardInputHandlerHolder.handler.addReleasedAction("Z", new FireKeyReleased(FireKey.PRIMARY));
		KeyboardInputHandlerHolder.handler.addReleasedAction("X", new FireKeyReleased(FireKey.SECONDARY));
		KeyboardInputHandlerHolder.handler.addReleasedAction("C", new FireKeyReleased(FireKey.TERTIARY));
		KeyboardInputHandlerHolder.handler.addReleasedAction("F", new FireKeyReleased(FireKey.QUATERNARY));
		KeyboardInputHandlerHolder.handler.addReleasedAction("V", new FireKeyReleased(FireKey.CHAINBEAM));
		
		this.starfields = new BufferedImage[STARFIELD_LAYERS];
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
	    GraphicsDevice device = env.getDefaultScreenDevice();
	    GraphicsConfiguration config = device.getDefaultConfiguration();
		for (int i = 0; i < STARFIELD_LAYERS; ++i) {
			starfields[i] = config.createCompatibleImage(WIDTH, HEIGHT);
			Graphics g = starfields[i].getGraphics();
			g.setColor(java.awt.Color.WHITE);
			for (int j = 0; j < Stage.NUM_STARS; ++j) {
				g.fillRect((int)(WIDTH * Math.random()), (int)(HEIGHT * Math.random()), 1, 1);
			}
		}
		this.addMouseListener(this);
		this.setPreferredSize(new Dimension(width, height));
		this.setSize(new Dimension(width, height));
		
		firePrimaryTimer = new Timer((int)(2000/Window.FPS), this);
		firePrimaryTimer.setInitialDelay(0);
		fireSecondaryTimer = new Timer((int)(10000/Window.FPS), this);
		fireSecondaryTimer.setInitialDelay(0);
		fireTertiaryTimer = new Timer((int)(10000/Window.FPS), this);
		fireTertiaryTimer.setInitialDelay(0);
		fireQuaternaryTimer = new Timer((int)(10000/Window.FPS), this);
		fireQuaternaryTimer.setInitialDelay(0);
		timer = new Timer((int)(1000/Window.FPS), this);
		upKeyPressed = false;
		downKeyPressed = false;
		leftKeyPressed = false;
		rightKeyPressed = false;
	}
	
	public LevelLoader getLevelLoader() {
		return loader;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		g.setColor(java.awt.Color.BLACK);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		Vec2 offsetMax = new Vec2(WIDTH - this.getWidth(), HEIGHT - this.getHeight());
		Vec2 offsetMin = Vec2.ZERO;
		
		Vec2 offset = new Vec2(player.getPosition().getX() - this.getWidth() / 2,
							   player.getPosition().getY() - this.getHeight() / 2);
		
		offset = offset.min(offsetMax).max(offsetMin);
		g.translate(-(int)offset.getX(), -(int)offset.getY());
		g.setClip((int)(offset.getX()), (int)(offset.getY()), (int)(offset.getX() + this.getWidth()), (int)(offset.getY() + this.getHeight()));
		for (int i = 0; i < STARFIELD_LAYERS; ++i) {
			if (starfields[i] != null) {
				g.drawImage(starfields[i], 0, 0, null);
				g.drawImage(starfields[i], 0, 0, null);
			}
		}
		for (Entry<Integer, CopyOnWriteArrayList<Entity>> entry : entities.entrySet()) {
			for (Entity e : entry.getValue()) {
				if (e != player) {
					e.render(g);
				}
			}
			if (entry.getKey() == player.getDefaultLayer())
				player.render(g);
		}
		
		g.translate((int)offset.getX(), (int)offset.getY());
		if (hud != null) {
			hud.render(g);
		}
	}
	
	private void doUpKey() {
		Vec2 orientation = player.getOrientation();
		player.setVelocity(orientation.scale(Player.MOVEMENT_SPEED).multiply(new Vec2(1, -1)));
	}
	
	private void doDownKey() {
		Vec2 orientation = player.getOrientation();
		player.setVelocity(orientation.scale(-Player.MOVEMENT_SPEED).multiply(new Vec2(1, -1)));
	}
	
	public Map<Integer, CopyOnWriteArrayList<Entity>> getEntities() {return entities;}

	private Vec2 getPlayerOffset() {
		Vec2 offsetMax = new Vec2(WIDTH - this.getWidth(), HEIGHT - this.getHeight());
		Vec2 offsetMin = Vec2.ZERO;
		
		Vec2 offset = new Vec2(player.getPosition().getX() - this.getWidth() / 2,
							   player.getPosition().getY() - this.getHeight() / 2);
		
		offset = offset.min(offsetMax).max(offsetMin);
		return new Vec2((player.getPosition().getX()-offset.getX()),(player.getPosition().getY()-offset.getY()));
	}
	
	private Vec2 convertMousePosition() {
		Point p = getMousePosition();
		if (p==null) {
			return null;
		}
		else {
			return new Vec2(p.x, p.y);
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent ev) {
		if (ev.getSource() == timer) {
			//Map<String, Boolean> keyMap = KeyboardInputHandlerHolder.handler.getKeys();
			//Set<String> keys = keyMap.keySet();
			
			if (mouseEnabled) {
				Vec2 playerPos = this.getPlayerOffset();
				Vec2 mousePos = this.convertMousePosition();
				if ((playerPos != null) && (mousePos != null)) {
					Vec2 relativeMousePosition = playerPos.subtract(mousePos);
					if (relativeMousePosition.magnitude() > ALL_MOVEMENT_DEAD_ZONE) {
						Vec2 transformedPosition = relativeMousePosition.rotate(Vec2.ZERO, -player.getOrientation().angle());
						double angle = transformedPosition.angle();
						if (Math.abs(angle) > ROTATION_DEAD_ZONE_SECTOR_SIZE) {
							if (angle > 0) {
								leftKeyPressed = true;
								rightKeyPressed = false;
							}
							else {
								rightKeyPressed = true;
								leftKeyPressed = false;
							}
						}
						else {
							leftKeyPressed = false;
							rightKeyPressed = false;
						}
						if (relativeMousePosition.magnitude() > LINEAR_MOVEMENT_DEAD_ZONE) {
							upKeyPressed = true;
							downKeyPressed = false;
							doUpKey();
						}
						else {
							player.setVelocity(Vec2.ZERO);
							upKeyPressed = false;
							downKeyPressed = false;
						}
					}
				}
				/*
				//makes control very annoying as player stops when the mouse exits screen for any reason
				else if ((mousePos == null) && (playerPos != null)) {
					player.setVelocity(Vec2.ZERO);
					upKeyPressed = false;
					leftKeyPressed = false;
					rightKeyPressed = false;
				}
				*/
			}
			
			if (leftKeyPressed && !rightKeyPressed) {
				player.setOrientation(player.getOrientation().rotate(Vec2.ZERO, Math.PI / 32));
				if (upKeyPressed && !downKeyPressed) {
					doUpKey();
				} else if (downKeyPressed && !upKeyPressed) {
					doDownKey();
				}
			} else if (rightKeyPressed && !leftKeyPressed) {
				player.setOrientation(player.getOrientation().rotate(Vec2.ZERO, -Math.PI / 32));
				if (upKeyPressed && !downKeyPressed) {
					doUpKey();
				} else if (downKeyPressed && !upKeyPressed) {
					doDownKey();
				}
			}
			for (int i = 0; i < STARFIELD_LAYERS; ++i) {
				backgroundOffsets[i] += (double)Stage.BACKGROUND_SCROLL_SPEED/Math.pow((i+1),0.5);
				if (backgroundOffsets[i] > Stage.BACKGROUND_OVERSIZE_RATIO * this.getHeight()) {
					backgroundOffsets[i] = 0;
				}
			}
			
			for (CopyOnWriteArrayList<Entity> array : entities.values()) {
				for (Entity e : array) {
					if (e.isUpdatable())
						e.update((int)(700 / Window.FPS));
				}
			}
			this.repaint();
		}
		else if ((ev.getSource() == firePrimaryTimer) && (!this.isPaused())) {
			player.fire(0);
		}
		else if ((ev.getSource() == fireSecondaryTimer) && (!this.isPaused())) {
			player.fire(1);
		}
		else if ((ev.getSource() == fireTertiaryTimer) && (!this.isPaused())) {
			player.fire(2);
		}
		else if ((ev.getSource() == fireQuaternaryTimer) && (!this.isPaused())) {
			player.fire(3);
		}
		else if (ev.getSource() == returnToTitle) {
			int prompt = 0;
			prompt = JOptionPane.showConfirmDialog(window, "Are you sure you want to return to the main menu?", "Confirmation", JOptionPane.YES_NO_OPTION);
			if (prompt == JOptionPane.YES_OPTION) {
				window.setCard(Window.TITLE_SCREEN);
			}
		}
		else if (ev.getSource() == pauseResumeButton) {
			if (this.isPaused()) {
				this.resume();
			}
			else {
				this.pause();
			}
		}
	}

	public void remove(Entity entity) {
		for (CopyOnWriteArrayList<Entity> arr : entities.values())
			if (arr.contains(entity))
				arr.remove(entity);
	}
	
	public void add(Entity entity) {
		if (!entities.containsKey(entity.getDefaultLayer()))
			entities.put(entity.getDefaultLayer(), new CopyOnWriteArrayList<Entity>());
		entities.get(entity.getDefaultLayer()).add(entity);
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.add(player);
		this.player = player;
	}
	
	public void setHUD(HUD hud) {
		this.hud = hud;
	}
	
	public void gameOver() {
		/*
		hud.setGameOver(true);
		this.pause();
		*/
		((CardLayout)this.getParent().getLayout()).show(this.getParent(), Window.GAMEOVERSCREEN);
	}
	
	public void pause() {
		timer.stop();
		this.add(returnToTitle, returnToTitleGridBagConstraints);
		pauseResumeButton.setText("Resume");
		this.revalidate();
		this.repaint();
	}
	
	public void resume() {
		timer.start();
		this.remove(returnToTitle);
		pauseResumeButton.setText("Pause");
	}
	
	public boolean isPaused() {
		return !timer.isRunning();
	}
	
	public Timer getTimer() {
		return timer;
	}
	
	public static boolean inStage(Vec2 pos) {
		return (pos.getX() > 0 && pos.getX() < Stage.WIDTH &&
				pos.getY() > 0 && pos.getY() < Stage.HEIGHT);
	}
	
	public Entity getNearestEntity(Entity entity) {
	    double bestDistance = Double.POSITIVE_INFINITY;
	    Entity nearestEntity = null;
	    for (CopyOnWriteArrayList<Entity> array : entities.values()) {
	    	for (Entity testEntity : array) {
	    		if (testEntity != entity) {
	    			double distance = entity.getPosition().distance(testEntity.getPosition());
	    			if (distance < bestDistance) {
	    				nearestEntity = testEntity;
	    				bestDistance = distance;
	    			}	
	    		}
	    	}
	    }
	    return nearestEntity;
	}
	
	public Entity getNearestEntity(Entity entity, Class<?> cl) {
	    double bestDistance = Double.POSITIVE_INFINITY;
	    Entity nearestEntity = null;
		for (CopyOnWriteArrayList<Entity> array : entities.values()) {
			for (Entity testEntity : array) {
				if (testEntity != entity && cl.isInstance(testEntity)) {
					double distance = entity.getPosition().distance(testEntity.getPosition()); 
					if (distance < bestDistance) {
						nearestEntity = testEntity;
						bestDistance = distance;
					}	
				}
			}
		}
	    return nearestEntity;
	}
	
	public Entity getNearestEntity(Entity entity, Set<Entity> ignore) {
	    double bestDistance = Double.POSITIVE_INFINITY;
	    Entity nearestEntity = null;
		for (CopyOnWriteArrayList<Entity> array : entities.values()) {
			for (Entity testEntity : array) {
				if (testEntity != entity && !ignore.contains(testEntity)) {
					double distance = entity.getPosition().distance(testEntity.getPosition());
					if (distance < bestDistance) {
						nearestEntity = testEntity;
						bestDistance = distance;
					}	
				}
			}
		}
	    return nearestEntity;
	}
	
	public Entity getNearestEntity(Entity entity, Set<Entity> ignore, Class<?> cl) {
	    double bestDistance = Double.POSITIVE_INFINITY;
	    Entity nearestEntity = null;
		for (CopyOnWriteArrayList<Entity> array : entities.values()) {
			for (Entity testEntity : array) {
				if (testEntity != entity && cl.isInstance(testEntity) && !ignore.contains(testEntity)) {
					double distance = entity.getPosition().distance(testEntity.getPosition()); 
					if (distance < bestDistance) {
						nearestEntity = testEntity;
						bestDistance = distance;
					}
				}
			}
		}
	    return nearestEntity;
	}
	
	private class MoveActionPressed extends AbstractAction {
		private static final long serialVersionUID = 481979749241664534L;
		
		private DirectionKey key;
		
		public MoveActionPressed(DirectionKey key) {
			super();
			this.key = key;
		}

		@Override
		public void actionPerformed(ActionEvent ev) {
			if (player != null) {
				switch (key) {
				case LEFT:
				{
					leftKeyPressed = true;
				}
					break;
				case RIGHT:
				{
					rightKeyPressed = true;
				}
					break;
				case FORWARD:
					upKeyPressed = true;
					doUpKey();
					break;
				case BACK:
					downKeyPressed = true;
					doDownKey();
					break;
				default:
					break;
				}
			}
		}
	}
	
	private class MoveActionReleased extends AbstractAction {
		private static final long serialVersionUID = 3135951535219119594L;
		
		private DirectionKey key;
		
		public MoveActionReleased(DirectionKey key) {
			super();
			this.key = key;
		}

		@Override
		public void actionPerformed(ActionEvent ev) {
			if (player != null) {
				switch (key) {
				case LEFT:
					leftKeyPressed = false;
					break;
				case RIGHT:
					rightKeyPressed = false;
					break;
				case FORWARD:
					player.setVelocity(Vec2.ZERO);
					upKeyPressed = false;
					break;
				case BACK:
					player.setVelocity(Vec2.ZERO);
					downKeyPressed = false;
					break;
				default:
					break;
				}
			}
		}
	}
	
	private class FireKeyPressed extends AbstractAction {
		private static final long serialVersionUID = -6212427508037713506L;
		
		private FireKey weapon;
		
		public FireKeyPressed(FireKey weapon) {
			this.weapon = weapon;
		}

		@Override
		public void actionPerformed(ActionEvent ev) {
			switch (weapon) {
			case PRIMARY:
				if (!firePrimaryTimer.isRunning()) {
					firePrimaryTimer.restart();
					firePrimaryTimer.start();
				}
				break;
			case SECONDARY:
				if (!fireSecondaryTimer.isRunning()) {
					fireSecondaryTimer.restart();
					fireSecondaryTimer.start();
				}
				break;
			case TERTIARY:
				if (!fireTertiaryTimer.isRunning()) {
					fireTertiaryTimer.restart();
					fireTertiaryTimer.start();
				}
				break;
			case QUATERNARY:
				if (!fireQuaternaryTimer.isRunning()) {
					fireQuaternaryTimer.restart();
					fireQuaternaryTimer.start();
				}
				break;
			case CHAINBEAM:
				player.fire(4);
				break;
			default:
				break;
			}
		}
	}
	
	private class FireKeyReleased extends AbstractAction {
		private static final long serialVersionUID = -2618332729409396890L;
		
		private FireKey weapon;
		
		public FireKeyReleased(FireKey weapon) {
			this.weapon = weapon;
		}

		@Override
		public void actionPerformed(ActionEvent ev) {
			switch (weapon) {
			case PRIMARY:
				firePrimaryTimer.stop();
				break;
			case SECONDARY:
				fireSecondaryTimer.stop();
				break;
			case TERTIARY:
				fireTertiaryTimer.stop();
				break;
			case QUATERNARY:
				fireQuaternaryTimer.stop();
				break;
			default:
				break;
			}
		}
	}
	
	private class PauseAction extends AbstractAction {
		private static final long serialVersionUID = 5280811003086658604L;

		public PauseAction() {}

		@Override
		public void actionPerformed(ActionEvent ev) {
			if (Stage.this.isPaused() && ((Stage.this.player == null) || (Stage.this.player.getHealth()>0))) {
				Stage.this.resume();
			}
			else {
				Stage.this.pause();
			}
		}
	}
	
	private enum DirectionKey {
		FORWARD, BACK, LEFT, RIGHT
	}
	
	private enum FireKey {
		PRIMARY, SECONDARY, TERTIARY, QUATERNARY, CHAINBEAM
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		//do nothing
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		//do nothing
	}

	@Override
	public void mouseExited(MouseEvent e) {
		//do nothing
	}

	@Override
	public void mousePressed(MouseEvent ev) {
		if (mouseEnabled) {
			switch (ev.getButton()) {
			case 1: //left mouse button (primary)
				if (!firePrimaryTimer.isRunning()) {
					firePrimaryTimer.restart();
					firePrimaryTimer.start();
				}
				break;
			case 2: //middle mouse button (secondary)
				if (!fireSecondaryTimer.isRunning()) {
					fireSecondaryTimer.restart();
					fireSecondaryTimer.start();
				}
				break;
			case 3: //right mouse button (tertiary)
				if (!fireTertiaryTimer.isRunning()) {
					fireTertiaryTimer.restart();
					fireTertiaryTimer.start();
					player.fire(4);
				}
				break;
			default: break; //do nothing
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent ev) {
		if (mouseEnabled) {
			switch (ev.getButton()) {
			case 1: //left mouse button (primary)
				firePrimaryTimer.stop();
				break;
			case 2: //middle mouse button (secondary)
				fireSecondaryTimer.stop();
				break;
			case 3: //right mouse button (tertiary)
				fireTertiaryTimer.stop();
				break;
			default: break; //do nothing
			}
		}
	}
	
	public void setMouseEnabled(boolean mouseEnabled) {
		this.mouseEnabled = mouseEnabled;
	}
}