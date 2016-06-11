package com.greenteam.spacefighters.entity.entityliving.obstacle;

import java.awt.Color;
import java.awt.Graphics;

import com.greenteam.spacefighters.stage.Stage;

public class Wall extends Obstacle {

	public Wall(Stage s, int health) {
		super(s, health);
	}

	@Override
	public void update(int ms) {
		super.update(ms);
	}
	
	@Override
	public void render(Graphics g) {
		g.setColor(Color.DARK_GRAY);
		int x = (int)this.getBoundingBox().getX();
		int y = (int)this.getBoundingBox().getY();
		int w = (int)this.getBoundingBox().getWidth();
		int h = (int)this.getBoundingBox().getHeight();
		//System.out.println(x+" "+y+" "+w+" "+h);
		g.fillRect(x-w/2, y-h/2, w, h);
	}
}
