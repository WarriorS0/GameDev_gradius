package game.entity;

import engine.entity.Entity;
import engine.shape.Circle;
import game.Game;

public class PacMan extends Entity {

	public PacMan() {
		super("PacMan");
		place(grid.new Position(10, 10));
	}

	@Override
	protected void setBounding() {
		clearBounding();

		addBounding(new Circle(
			center(),
			Game.game().cmPerCell / 2
		));
	}
}