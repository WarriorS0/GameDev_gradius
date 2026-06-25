package game.gradius.entity;

import engine.entity.Entity;
import engine.gal.arguments.Category;
import engine.shape.Rect;
import game.Game;

public class Power extends Entity {

	// JUSTE POUR LE TEST DOIT ETRE FINI

	public Power() {
		super("Power");

		double cell = Game.game().cmPerCell;

		setSize(isu.new Dimension(2 * cell, 2 * cell));
		setStep(isu.new Dimension(cell, cell));
		category(Category.Power);

		place(grid.new Position(15, grid.height() / 2));
	}

	@Override
	protected void setBounding() {
		clearBounding();

		addBounding(new Rect(center(), size(), orientation()));
	}
}