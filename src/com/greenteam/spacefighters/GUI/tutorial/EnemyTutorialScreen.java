package com.greenteam.spacefighters.GUI.tutorial;

import com.greenteam.spacefighters.GUI.Window;
import com.greenteam.spacefighters.entity.entityliving.obstacle.asteroid.Asteroid;
import com.greenteam.spacefighters.entity.entityliving.starship.enemy.Enemy;
import com.greenteam.spacefighters.entity.entityliving.starship.enemy.Enemy.EnemyShipColor;

public class EnemyTutorialScreen extends TutorialScreen {
	private static final long serialVersionUID = -3840244545725760175L;
	private static final String[] infoName = {
		"Asteroid-",
		"Invader-",
		"Scout-",
		"Tiger Shark-",
		"Raptor-"
	};
	private static final String[] infoDescription = {
		"Damages the player and enemies alike on collision. Can shield from projectiles.",
		"Travels in straight lines, turning at the edge of the screen.",
		"Travels toward an arbitrary direction.",
		"Fires toward the player while travelling to selected destination points.",
		"Tracks whiles firing toward the player."
	};
	
	public EnemyTutorialScreen(Window w) {
		super(w);
		this.addTutorialComponent(0, Asteroid.getTexFromID(0), infoName[0], infoDescription[0]);
		for (EnemyShipColor color : EnemyShipColor.values()) {
			int i = color.ordinal() + 1;
			this.addTutorialComponent(i, Enemy.getTexFromEnum(color), infoName[i], infoDescription[i]);
		}
	}

}
