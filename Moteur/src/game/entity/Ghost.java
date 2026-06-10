package game.entity;

import engine.entity.Entity;
import engine.geometry.Grid.Dimension;
import engine.shape.Rect;
import game.Game;

// == GHOST ==

public class Ghost extends Entity {

	// CONSTRUCTOR

	public Ghost() {
		super("Ghost");
		super.setPosition(super.grid.new Position(15, 15));
		this.setBounding();
	}

	// TASK COLLISION

	@Override
	protected void setBounding() {
		super.setBounding();

		double cmPerCell = Game.game().cmPerCell;

		Rect rect = new Rect(super.center(), super.isu.new Dimension(cmPerCell, cmPerCell), super.orientation());
		super.addBounding(rect);

	}

}
