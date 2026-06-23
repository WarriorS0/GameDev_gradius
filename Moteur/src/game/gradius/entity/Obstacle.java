package game.gradius.entity;

import engine.entity.Entity;
import engine.gal.arguments.Category;
import engine.shape.Rect;
import game.Game;

public class Obstacle extends Entity {

	public Obstacle() {
		super("Obstacle");

		double cell = Game.game().cmPerCell;

		setSize(isu.new Dimension(cell, cell));
		setStep(isu.new Dimension(cell, cell));
		category(Category.Obstacle);

		place(grid.new Position(25, grid.height() / 2));
	}

	@Override
	protected void setBounding() {
		clearBounding();

		addBounding(new Rect(center(), size(), orientation()));
	}
}