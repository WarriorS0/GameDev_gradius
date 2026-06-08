package game.entity;

import engine.entity.Entity;
import engine.shape.Rect;
import game.Game;

class Obstacle extends Entity {

	// CONSTRUCTOR

	Obstacle(int x_ncell, int y_ncell) {
		super("Obstacle");
		super.setPosition(super.grid.new Position(x_ncell, y_ncell));
		super.setSize(super.isu.new Dimension(Game.game().cmPerCell, Game.game().cmPerCell));

	}

	// === Task COLLISION ===
	@Override
	protected void setBounding() {
		this.addBounding(new Rect(super.center(), super.size(), super.orientation()));
	}

}
