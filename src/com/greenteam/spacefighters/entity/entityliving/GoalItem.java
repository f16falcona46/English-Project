package com.greenteam.spacefighters.entity.entityliving;

import java.awt.Graphics;

import com.greenteam.spacefighters.entity.Entity;
import com.greenteam.spacefighters.stage.Stage;

public class GoalItem extends EntityLiving {

	public GoalItem(Stage s, int maxHealth, int health) {
		super(s, maxHealth, health);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void render(Graphics g) {
		
	}

	@Override
	public Class<?> getSourceClass() {
		return Entity.class;
	}

}
