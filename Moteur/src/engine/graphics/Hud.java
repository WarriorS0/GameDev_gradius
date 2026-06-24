package engine.graphics;

import java.util.LinkedList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import engine.logs.LoggerManager;
import oop.graphics.Graphics;

/**
 * Unlike Avatars who requires entities, hud allow to display element without
 * the need for one Hud element will always be drawn on top of any entity (due
 * to the drawing order)
 */
public class Hud {

	private static final boolean LOGGING;
	private static final boolean INFO;
	private static Logger logger;
	static {
		logger = LoggerManager.getLogger(Hud.class.getName());
		LOGGING = logger.getLevel() != Level.OFF;
		INFO = logger.isLoggable(Level.INFO);
	}

	private final List<HudElement> elements;
	private final List<HudElement> toRemoveNext;

	/**
	 * Creates a hud object
	 */
	public Hud() {
		if (LOGGING && INFO)
			logger.info("Created new HUD");
		this.elements = new LinkedList<>();
		this.toRemoveNext= new LinkedList<>();
	}

	/**
	 * Adds an element to the hud
	 * 
	 * @param e the hud element to add
	 * @return true if sucessfully added, false if not
	 */
	public boolean add(HudElement e) {
		if (e == null)
			return false;
		if (LOGGING && INFO)
			logger.info(String.format("Added %s to the HUD", e.toString()));
		return elements.add(e);
	}

	/**
	 * Removes an element from the hud.
	 *
	 * @param e the element to remove
	 * @return true if sucessfully removed, false if not
	 */
	public boolean remove(HudElement e) {
		if (e == null)
			return false;
		if (LOGGING && INFO)
			logger.info(String.format("Removed %s from the HUD", e.toString()));
		return elements.remove(e);
	}

	public void clear() {
		if (LOGGING && INFO)
			logger.info("Cleared all elements from HUD");
		this.elements.clear();
	}

	public Iterator<HudElement> iterator() {
		return this.elements.iterator();
	}

	/**
	 * Draws all visible elements in order (oldest first, newest on top).
	 *
	 * @param graphics the graphics in raw canvas pixels
	 */
	public void draw(Graphics graphics) {
		for(HudElement e : elements) {
			if (e instanceof FollowerLabel fl) {
				if (fl.isTargetEntityStillAlive()) {
					fl.update();
				} else {
					this.toRemoveNext.add(fl);
				}
			}
			if (e.isVisible())
				e.draw(graphics);
		}
		for(HudElement e : toRemoveNext) {
			this.elements.remove(e);
		}
		toRemoveNext.clear();
	}
}
