package game.entity;

import engine.entity.Entity;
import engine.shape.Circle;
import game.Game;

public class Gum extends Entity {

	public Gum() {
		super("Gum");
		place(grid.new Position(5, 5));
	}

	@Override
	protected void setBounding() {
		clearBounding();

		addBounding(new Circle(
			center(),
			Game.game().cmPerCell / 6
		));
	}
}