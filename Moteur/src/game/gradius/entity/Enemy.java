package game.gradius.entity;

import engine.entity.Entity;
import engine.shape.Rect;
import game.Game;

public class Enemy extends Entity{
	public Enemy() {
		super("Enemy");
		place(grid.new Position(15, 15));
	}
	
	@Override
	protected void setBounding() {
		clearBounding();

		double cmPerCell = Game.game().cmPerCell;

		Rect rect = new Rect(
			center(),
			isu.new Dimension(cmPerCell, cmPerCell),
			orientation()
		);

		addBounding(rect);
	}
}
