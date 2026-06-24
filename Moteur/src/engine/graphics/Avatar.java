package engine.graphics;

import engine.entity.Entity;
import engine.geometry.ISU;
import game.Game;
import oop.graphics.Color;
import oop.graphics.Graphics;
import oop.graphics.Graphics.Colors;

public abstract class Avatar {

	protected static final double pixelPerCm;
	static {
		pixelPerCm = Game.game().pixelPerCm;
	}

	private final Color colorShape;

	protected Entity entity;

	protected Avatar(Entity entity) {
		this.colorShape = Colors.red; // default Color
		this.entity = entity;
	}

	public Entity entity() {
		return entity;
	}

	public boolean dead() {
		return entity != null && entity.dead();
	}

	public void kill() {
		if (entity != null) {
			entity.kill();
		}
	}

	public void revive() {
		if (entity != null) {
			entity.revive();
		}
	}

	public void paint(Graphics g) {
		ISU.Coord coord = entity.center();

		int xCenter = this.cmToPixel(coord.x());
		int yCenter = this.cmToPixel(coord.y());

		ISU.Dimension size = entity.size();

		int width = Math.max(1, this.cmToPixel(size.x()));
		int height = Math.max(1, this.cmToPixel(size.y()));

		int xTopLeft = xCenter - width / 2;
		int yTopLeft = yCenter - height / 2;

		g.setColor(colorShape);
		g.drawRect(xTopLeft, yTopLeft, width, height);
	}

	public abstract void updateAnimation(double delta_t);

	public abstract void initImages(Graphics g);

	protected int cmToPixel(double value) {
		return (int) Math.round(value * pixelPerCm);
	}
}