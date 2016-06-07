package com.greenteam.spacefighters.GUI;

import java.util.List;
import java.awt.event.ActionEvent;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

public class KeyboardInputHandler {
	private static final String PRESSED = "pressed ";
	private static final String RELEASED = "released ";
	private static final String[] keysToListen = {
			"UP", "DOWN", "LEFT", "RIGHT", "Z", "X", "C", "V", "F", "SPACE"
	};
	
	private JComponent component;
	private Map<String, Boolean> keys;
	private Map<String, List<AbstractAction>> pressedActions;
	private Map<String, List<AbstractAction>> releasedActions;
	
	public KeyboardInputHandler(JComponent component) {
		this.component = component;
		this.keys = new ConcurrentHashMap<String, Boolean>();
		this.pressedActions = new ConcurrentHashMap<String, List<AbstractAction>>();
		this.releasedActions = new ConcurrentHashMap<String, List<AbstractAction>>();
		
		for (String key : keysToListen) {
			initKey(key);
		}
	}
	
	private void initKey(String key) {
		component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(PRESSED+key),PRESSED+key);
		component.getActionMap().put(PRESSED+key, new KeyPressedAction(key));
		component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(RELEASED+key),RELEASED+key);
		component.getActionMap().put(RELEASED+key, new KeyReleasedAction(key));
		pressedActions.put(key, new CopyOnWriteArrayList<AbstractAction>());
		releasedActions.put(key, new CopyOnWriteArrayList<AbstractAction>());
		keys.put(key, false);
	}
	
	public Map<String, Boolean> getKeys() {
		return keys;
	}
	
	public void addPressedAction(String key, AbstractAction action) {
		if (!keys.containsKey(key)) initKey(key);
		pressedActions.get(key).add(action);
	}
	
	public void addReleasedAction(String key, AbstractAction action) {
		if (!keys.containsKey(key)) initKey(key);
		releasedActions.get(key).add(action);
	}
	
	public boolean getKeyState(String key) {
		if (!keys.containsKey(key)) initKey(key);
		return keys.get(key);
	}
	
	class KeyPressedAction extends AbstractAction {
		private static final long serialVersionUID = 20160517L;
		
		String key;
		
		public KeyPressedAction(String key) {
			this.key = key;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			KeyboardInputHandler.this.keys.put(key, true);
			List<AbstractAction> actions = KeyboardInputHandler.this.pressedActions.get(key);
			for (AbstractAction action : actions) {
				action.actionPerformed(e);
			}
		}
	}
	
	class KeyReleasedAction extends AbstractAction {
		private static final long serialVersionUID = 20160517L;
		
		String key;
		
		public KeyReleasedAction(String key) {
			this.key = key;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			KeyboardInputHandler.this.keys.put(key, false);
			List<AbstractAction> actions = KeyboardInputHandler.this.releasedActions.get(key);
			for (AbstractAction action : actions) {
				action.actionPerformed(e);
			}
		}
	}
}
