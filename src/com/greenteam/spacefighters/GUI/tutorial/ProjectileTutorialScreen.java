package com.greenteam.spacefighters.GUI.tutorial;

import java.awt.GridBagConstraints;
import com.greenteam.spacefighters.GUI.Window;
import com.greenteam.spacefighters.entity.entityliving.projectile.Projectile;
import com.greenteam.spacefighters.entity.entityliving.projectile.Projectile.ProjectileColor;

public class ProjectileTutorialScreen extends TutorialScreen {
	private static final long serialVersionUID = 2491164676643249778L;
	private static final String[] infoName = {
		"Linear Projectile-",
		"Homing Projectile-",
		"Explosive Projectile-",
	};
	private static final String[] infoDescription = {
		"Travels in a straight line before dissipating.",
		"Seeks out and follows the nearest enemy.",
		"Travels in a straight line before exploding.",
	};
	
	public ProjectileTutorialScreen(Window w) {
		super(w);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.WEST;
		for (ProjectileColor color : ProjectileColor.values()) {
			if (!color.equals(ProjectileColor.GREEN)) {
				int i = color.ordinal();
				this.addTutorialComponent(i, Projectile.getTexFromEnum(color), infoName[i], infoDescription[i]);
			}
		}
	}

}
