package game.gradius.entity;

import engine.entity.Entity;
import engine.gal.arguments.Category;
import engine.shape.Rect;
import game.Game;

public class Cannon extends Entity {

	private final CannonSlot slot;
	private Ship owner;

	public Cannon(CannonSlot slot) {
		super(slot.name() + "Cannon");

		this.slot = slot;

		double cell = Game.game().cmPerCell;

		setSize(isu.new Dimension(0.9 * cell, 0.30 * cell));
		setStep(isu.new Dimension(cell, cell));
		category(Category.Team);

		place(grid.new Position(5, grid.height() / 2));
	}

	public CannonSlot slot() {
		return slot;
	}
	
	public void attachTo(Ship owner) {
		if (owner == null) {
			throw new IllegalArgumentException("owner cannot be null");
		}

		this.owner = owner;
	}

	public Ship owner() {
		return owner;
	}

	public void placeRelativeTo(Entity anchor) {
		if (anchor == null || anchor.center() == null) {
			throw new IllegalArgumentException("anchor must be placed before placing cannon");
		}

		double cell = Game.game().cmPerCell;

		double dx = 0.65 * cell;
		double dy = slot.isTop() ? -2 * cell : 2 * cell;

		place(isu.new Coord(anchor.center().x() + dx, anchor.center().y() + dy));
	}

	@Override
	protected void setBounding() {
		clearBounding();

		addBounding(new Rect(center(), size(), orientation()));
	}
}