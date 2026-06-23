package game.gradius.entity;

import engine.entity.Entity;
import engine.gal.arguments.Category;
import engine.shape.Rect;
import game.Game;

public class Ship extends Entity {

	public Ship() {
		super("Ship");

		double cell = Game.game().cmPerCell;

		setSize(isu.new Dimension(4.0 * cell, 2.0 * cell));
		setStep(isu.new Dimension(cell, cell));
		category(Category.Team);

		place(grid.new Position(5, grid.height() / 2));
	}

	@Override
	protected void setBounding() {
		clearBounding();

		addBounding(new Rect(center(), size(), orientation()));
	}
}