package game.gradius.entity;

import engine.entity.Entity;
import engine.shape.Rect;
import game.Game;

public class Obstacle extends Entity {

	public Obstacle(int x_ncell, int y_ncell) {
		super("Obstacle");

		double cmPerCell = Game.game().cmPerCell;

		setSize(isu.new Dimension(cmPerCell, cmPerCell));
		place(grid.new Position(x_ncell, y_ncell));
	}

	@Override
	protected void setBounding() {
		clearBounding();

		addBounding(new Rect(
			center(),
			size(),
			orientation()
		));
	}
}