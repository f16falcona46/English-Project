package com.greenteam.spacefighters.GUI.tutorial;

import java.awt.Image;
import javax.imageio.ImageIO;
import com.greenteam.spacefighters.GUI.Window;

public class InputTutorialScreen extends TutorialScreen {
	private static final long serialVersionUID = -5026032644278835727L;
	private static final String[] infoName = {
			"Movement Keys-",
			"Weapon Keys-",
			"Space Key-"
		};
		private static final String[] infoDescription = {
			"Up and down keys move the player foreward and backward, respectively. Right and left keys rotate the player clockwise and anticlockwise, respectively.",
			"Z, X, C, and V each fire different weapons: Z, the linear projectile, X, the homing projectile, C, the explosive projectile, and V, the chain beam.",
			"The space key toggles game pausing."
		};
		
		public InputTutorialScreen(Window w) {
			super(w);
			int i = 0;
			for (; i < 3; i++) {
				this.addTutorialComponent(i, InputTutorialScreen.getTexFromID(i), infoName[i], infoDescription[i]);
			}
		}
		
		private static Image getTexFromID(int texID) {
			try {
				Image img = ImageIO.read(InputTutorialScreen.class.getResource("/com/greenteam/spacefighters/assets/keys-"+ texID + ".png")); 
				return img.getScaledInstance(3 * img.getWidth(null) / 4, 3 * img.getHeight(null) / 4, Image.SCALE_SMOOTH);
			} catch(Exception e) {
				return null;
			}
		}

}
