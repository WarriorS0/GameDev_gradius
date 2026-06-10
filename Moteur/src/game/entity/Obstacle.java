package game.entity;

import engine.entity.Entity;
import engine.shape.Rect;
import game.Game;

public class Obstacle extends Entity {

	// CONSTRUCTOR

	public Obstacle(int x_ncell, int y_ncell) {
		super("Obstacle");
		super.setPosition(super.grid.new Position(x_ncell, y_ncell));
		super.setSize(super.isu.new Dimension(Game.game().cmPerCell, Game.game().cmPerCell));
		this.setBounding();

	}

	// === Task COLLISION ===
	@Override
	protected void setBounding() {
		super.setBounding();
		this.addBounding(new Rect(super.center(), super.size(), super.orientation()));
	}

}
