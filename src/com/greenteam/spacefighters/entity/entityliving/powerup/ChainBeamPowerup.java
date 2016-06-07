package com.greenteam.spacefighters.entity.entityliving.powerup;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

import com.greenteam.spacefighters.common.Color;
import com.greenteam.spacefighters.common.Vec2;
import com.greenteam.spacefighters.entity.Entity;
import com.greenteam.spacefighters.entity.entityliving.EntityLiving;
import com.greenteam.spacefighters.entity.entityliving.starship.enemy.Enemy;
import com.greenteam.spacefighters.entity.entityliving.starship.player.Player;
import com.greenteam.spacefighters.stage.Stage;

public class ChainBeamPowerup extends Powerup {
	private static final int DURATION = Integer.MAX_VALUE;
	private static final double TRAVELDISTANCE = 4000;
	private static final int SCANINTERVAL = 25;
	private static final double INNERRADIUS = 30;
	private static final double OUTERRADIUS = 40;
	private static final double INNERWIDTH = 20;
	private static final double OUTERWIDTH = 40;
	private final Color INNERCOLOR = new Color(148, 91, 250, 127);
	private final Color OUTERCOLOR = new Color(148, 91, 250, 63);
	private LinkedHashSet<Entity> targets;
	private double remainingDistance;
	private int elapsedTime;
	private boolean fired;
	
	public ChainBeamPowerup(Stage s, Player pl) {
		super(s, pl);
		targets = new LinkedHashSet<Entity>();
		remainingDistance = TRAVELDISTANCE;
		elapsedTime = SCANINTERVAL;
	}
	
	private Shape rectBetween(Vec2 p1, Vec2 p2, double width) {
		Vec2 dir = p2.subtract(p1).normalize();
		Vec2 midPt = p2.midpoint(p1);
		double angle = dir.multiply(new Vec2(1.0, -1.0)).angle();
		double length = p2.distance(p1);
		Rectangle2D rect = new Rectangle2D.Double(midPt.getX() - width / 2, midPt.getY() - length /2, width, length);
		AffineTransform tr = new AffineTransform();
		tr.rotate(angle, midPt.getX(), midPt.getY());
		return tr.createTransformedShape(rect);
	}

	@Override
	public void render(Graphics g) {
		if (fired) {
			Graphics2D g2 = ((Graphics2D) g);
			ArrayList<Entity> entities = new ArrayList<Entity>();
			entities.add(this.player);
			entities.addAll(targets);

			Vec2 entPos = entities.get(0).getPosition();
			
			g2.setColor(OUTERCOLOR.toAWTColor());
			Area outerArea = new Area();
			outerArea.add(new Area(new Ellipse2D.Double(entPos.getX() - OUTERRADIUS, entPos.getY() - OUTERRADIUS, 2 * OUTERRADIUS, 2 * OUTERRADIUS)));

			for(int i = 0; i < entities.size() - 1; i++) {
				Entity e1 = entities.get(i);
				Entity e2 = entities.get(i + 1);
				Vec2 pos1 = e1.getPosition();
				Vec2 pos2 = e2.getPosition();
				outerArea.add(new Area(new Ellipse2D.Double(pos2.getX() - OUTERRADIUS, pos2.getY() - OUTERRADIUS, 2 * OUTERRADIUS, 2 * OUTERRADIUS)));
				outerArea.add(new Area(rectBetween(pos1, pos2, OUTERWIDTH)));
			}
			g2.fill(outerArea);
			g2.setColor(INNERCOLOR.toAWTColor());
			Area innerArea = new Area();
			innerArea.add(new Area(new Ellipse2D.Double(entPos.getX() - INNERRADIUS, entPos.getY() - INNERRADIUS, 2 * INNERRADIUS, 2 * INNERRADIUS)));

			for(int i = 0; i < entities.size() - 1; i++) {
				Entity e1 = entities.get(i);
				Entity e2 = entities.get(i + 1);
				Vec2 pos1 = e1.getPosition();
				Vec2 pos2 = e2.getPosition();
				innerArea.add(new Area(new Ellipse2D.Double(pos2.getX() - INNERRADIUS, pos2.getY() - INNERRADIUS, 2 * INNERRADIUS, 2 * INNERRADIUS)));
				innerArea.add(new Area(rectBetween(pos1, pos2, INNERWIDTH)));
			}
			g2.fill(innerArea);
		}

	}
	
	@Override
	public double getRadius() {
		return Double.NEGATIVE_INFINITY;
	}
	
	@Override
	public int getDamage() {
		return 50;
	}
	
	protected boolean isOppositeFaction(Entity e) {
		return (Enemy.class.isAssignableFrom(e.getSourceClass()));
	}

	protected int getDuration() {
		return DURATION;
	}
	
	public void fire() {
		fired = true;
	}
	
	@Override
	public void update(int ms) {
		super.update(ms);
		this.setPosition(player.getPosition());
		if (fired) {
			elapsedTime += ms;
		
			if (elapsedTime >= SCANINTERVAL) {
				elapsedTime = 0;
				Stage s = this.getStage();
				
				Entity currentTarget;
				
				if (targets.size() > 0) {
					currentTarget = (Entity)targets.toArray()[targets.size() - 1];
				} else {
					currentTarget = this;
				}
				
				Enemy target = (Enemy)s.getNearestEntity(currentTarget, (Set<Entity>)targets, Enemy.class);
				
				double dist = Double.POSITIVE_INFINITY;
				if (target != null) {
					dist = this.getPosition().distance(target.getPosition()); 
				}
				if (dist <= remainingDistance) {
					remainingDistance -= dist;
					target.setUpdatable(false);
					targets.add(target);
				} else {
					for (Entity e : targets) {
						((EntityLiving)e).damage(this, this.getDamage());
						e.setUpdatable(true);
					}
					player.removePowerup(this);
				}
			}
		}
	}

}
