package game.gradius.entity;

import engine.entity.Entity;
import engine.gal.arguments.Category;
import engine.shape.Rect;
import game.Game;

public class Enemy extends Entity{
	public Enemy() {
		super("Enemy");

		double cell = Game.game().cmPerCell;

		setSize(isu.new Dimension(2.0 * cell, 2.0 * cell));
		setStep(isu.new Dimension(cell, cell));
		category(Category.Adversary);

		place(grid.new Position(50, grid.height() / 2));
	}

	@Override
	protected void setBounding() {
		clearBounding();

		addBounding(new Rect(center(), size(), orientation()));
	}
}
