package engine.graphics;

import oop.graphics.Graphics;

public interface HudElement {

	/**
	 * Draws the element in screen space.
	 *
	 * @param g the graphics
	 */
	void draw(Graphics g);

	/**
	 * Tells whether the element should be drawn.
	 *
	 * @return true if visible
	 */
	boolean isVisible();

	/**
	 * Sets the visibility.
	 *
	 * @param shown true to show, false to hide
	 */
	void setVisibility(boolean shown);
}