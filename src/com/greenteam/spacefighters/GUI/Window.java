package com.greenteam.spacefighters.GUI;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.greenteam.spacefighters.stage.LevelLoader;
import com.greenteam.spacefighters.stage.Stage;

public class Window extends JFrame implements WindowListener {
	private static final long serialVersionUID = 8514984102701282740L;
	private static final int WIDTH = 600;
	private static final int HEIGHT = 600;
	
	public static final double FPS = 60;
	public static final String TITLE_SCREEN = "TITLE";
	public static final String STAGE = "STAGE";
	public static final String GAMEOVERSCREEN = "GAMEOVERSCREEN";
	public static final String LEVELINCREMENTSCREEN = "LEVELINCREMENTSCREEN";
	public static final String DEATHSCREEN = "DEATHSCREEN";
	public static final String WINSCREEN = "WINSCREEN";
	
	private Stage stage;
	private LevelLoader loader;
	private TitleScreen title;
	private GameOverScreen gameover;
	private LevelIncrementScreen levelscreen;
	private DeathScreen deathscreen;
	private WinScreen winscreen;
	private boolean mouseInput;
	
	public Window() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		final JPanel contentPane = new JPanel();
		contentPane.setLayout(new CardLayout());
		KeyboardInputHandlerHolder.handler = new KeyboardInputHandler(contentPane);
		
		loader = new LevelLoader(this, Window.WIDTH, Window.HEIGHT, null);
		stage = loader.getStage();
		title = new TitleScreen(this);
		gameover = new GameOverScreen(stage, this);
		levelscreen = new LevelIncrementScreen(stage);
		deathscreen = new DeathScreen(stage);
		winscreen = new WinScreen(stage);
		
		contentPane.add(title, TITLE_SCREEN);
		contentPane.add(stage, STAGE);
		contentPane.setBounds(new Rectangle(Window.WIDTH, Window.HEIGHT));
		contentPane.add(gameover, GAMEOVERSCREEN);
		contentPane.add(levelscreen, LEVELINCREMENTSCREEN);
		contentPane.add(deathscreen, DEATHSCREEN);
		contentPane.add(winscreen, WINSCREEN);
		
		((CardLayout)contentPane.getLayout()).show(contentPane, TITLE_SCREEN);
		
		this.setBounds(new Rectangle(Window.WIDTH, Window.HEIGHT));

		this.setTitle("The Great Gatsby");
		//this.setIconImage(Player.getTexFromEnum(PlayerShipColor.RED));
		try {
			this.setIconImage(ImageIO.read(this.getClass().getResource("/com/greenteam/spacefighters/assets/gatsby/jay-gatsby-logo.png")));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		this.setMinimumSize(new Dimension(Window.WIDTH, Window.HEIGHT));
		this.setLocationRelativeTo(null);
		this.setContentPane(contentPane);
		this.setVisible(true);
		this.createBufferStrategy(2);
	}
	
    public static void main(String[] args) {
    	new Window();
    }

	@Override
	public void windowActivated(WindowEvent arg0) {}
	@Override
	public void windowClosed(WindowEvent arg0) {}

	@Override
	public void windowClosing(WindowEvent arg0) {}

	@Override
	public void windowDeactivated(WindowEvent arg0) {}

	@Override
	public void windowDeiconified(WindowEvent arg0) {}

	@Override
	public void windowIconified(WindowEvent arg0) {}

	@Override
	public void windowOpened(WindowEvent arg0) {}
	
	public void setMouseInput(boolean value) {
		mouseInput = value;
		stage.setMouseEnabled(value);
		title.updateInstructions();
	}
	
	public boolean useMouseInput() {
		return mouseInput;
	}
	
	public void setCard(String card) {
		if (card.equals(STAGE)) {
			if (title.isVisible()) {
				stage.getPlayer().setScore(0);
				stage.getPlayer().setLives(5);
				loader.startLevel();
			}
			stage.resume();
		}
		else {
			stage.pause();
		}
		((CardLayout)this.getContentPane().getLayout()).show(this.getContentPane(), card);
	}
	
	public Stage getStage() {
		return stage;
	}
	
	public TitleScreen getTitleScreen() {
		return title;
	}
}
